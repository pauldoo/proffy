/*
    Copyright (C) 2008, 2009  Paul Richards.

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
#include "stdafx.h"

#include "Assert.h"
#include "ComInitialize.h"
#include "CommandLineArguments.h"
#include "ConsoleColor.h"
#include "Exception.h"
#include "DebugEventCallbacks.h"
#include "DebugOutputCallbacks.h"
#include "Launcher.h"
#include "Results.h"
#include "Utilities.h"
#include "WriteReport.h"

namespace Proffy {
    const std::string lines = std::string(50, '-') + "\n";

    void FlushCallbacks(IDebugClient* const debugClient)
    {
        ASSERT(debugClient->FlushCallbacks() == S_OK);
    }

    const int main(
        const int argc,
        const wchar_t* const * const argv)
    {
        try {
            const CommandLineArguments arguments = CommandLineArguments::Parse(argc, argv);

            // Initialize COM, no idea if this is necessary.
            const ComInitialize com;

            HRESULT result = S_OK;

            // Get the IDebugClient to start.
            IDebugClient* debugClient = NULL;
            result = ::DebugCreate(
                __uuidof(IDebugClient),
                reinterpret_cast<void**>(&debugClient));
            ASSERT(result == S_OK);

            // Set our output callbacks.
            DebugOutputCallbacks debugOutputCallbacks;
            result = debugClient->SetOutputCallbacks(&debugOutputCallbacks);
            ASSERT(result == S_OK);

            // Set our event callbacks.
            DebugEventCallbacks debugEventCallbacks;
            result = debugClient->SetEventCallbacks(&debugEventCallbacks);
            ASSERT(result == S_OK);

            // Get the IDebugControl.
            IDebugControl* debugControl = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugControl),
                reinterpret_cast<void**>(&debugControl));
            ASSERT(result == S_OK);

            // Get the IDebugSystemObjects.
            IDebugSystemObjects* debugSystemObjects = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugSystemObjects),
                reinterpret_cast<void**>(&debugSystemObjects));
            ASSERT(result == S_OK);

            // Get the IDebugSymbols.
            IDebugSymbols3* debugSymbols = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugSymbols3),
                reinterpret_cast<void**>(&debugSymbols));
            ASSERT(result == S_OK);

            // Done getting and setting, so now attach.
            std::cout << lines << Utilities::TimeInSeconds() << ": Attaching..\n" << lines;
            result = debugClient->AttachProcess(
                0,
                arguments.fProcessId,
                0);
            ASSERT(result == S_OK);
            std::cout << lines << Utilities::TimeInSeconds() << ": Done Attaching..\n" << lines;

            // Verify we have attached to what we think we have.
            ULONG debugeeTypeClass;
            ULONG debugeeTypeQualifier;
            result = debugControl->GetDebuggeeType(&debugeeTypeClass, &debugeeTypeQualifier);
            ASSERT(result == S_OK);
            ASSERT(debugeeTypeClass == DEBUG_CLASS_USER_WINDOWS);
            ASSERT(debugeeTypeQualifier == DEBUG_USER_WINDOWS_PROCESS);

            FlushCallbacks(debugClient);
            ConsoleColor c(Color_Normal);
            Results results;

            // Profiler when used for self-profiling
            std::auto_ptr<Launcher> profiler;
            if (arguments.fProfileTheProfiler) {
                profiler.reset(new Launcher(
                    L"../Release",
                    arguments.fOutputFilename,
                    arguments.fDelayBetweenSamplesInSeconds,
                    false));
            }

            ASSERT(::ReleaseSemaphore(arguments.fStartFlag, 1, NULL) != FALSE);
            results.fBeginTimeInSeconds = Utilities::TimeInSeconds();

            while (true) {
                result = ::WaitForSingleObject(arguments.fStopFlag, 0);
                if (result == WAIT_OBJECT_0) {
                    // Flag is raised
                    break;
                } else if (result == WAIT_TIMEOUT) {
                    // Flag is not raised
                } else {
                    ASSERT(false);
                }

                const int delayBetweenSamplesInMilliseconds = Utilities::Round(arguments.fDelayBetweenSamplesInSeconds * 1000.0);
                result = debugControl->WaitForEvent(
                    DEBUG_WAIT_DEFAULT,
                    delayBetweenSamplesInMilliseconds);
                ASSERT(result == S_OK || result == S_FALSE);

                ULONG executionStatus;
                result = debugControl->GetExecutionStatus(&executionStatus);
                ASSERT(result == S_OK);
                ASSERT(executionStatus == DEBUG_STATUS_BREAK);

                results.fNumberOfSamples++;

                ULONG numberThreads;
                ULONG largestProcess;
                result = debugSystemObjects->GetTotalNumberThreads(&numberThreads, &largestProcess);
                ASSERT(result == S_OK);

                for (int i = 0; i < static_cast<int>(numberThreads); i++) {
                    result = debugSystemObjects->SetCurrentThreadId(i);
                    ASSERT(result == S_OK);

                    std::vector<DEBUG_STACK_FRAME> frames(100);
                    ULONG framesFilled;
                    result = debugControl->GetStackTrace(
                        NULL,
                        NULL,
                        NULL,
                        &(frames.front()),
                        static_cast<int>(frames.size()),
                        &framesFilled);
                    ASSERT(result == S_OK);
                    frames.resize(framesFilled);

                    std::vector<PointInProgram> resolvedFrames;
                    for (int i = 0; i < static_cast<int>(frames.size()); i++) {
                        std::vector<wchar_t> symbolNameAsVector(MAX_PATH * 2);
                        ULONG symbolNameSize;
                        ULONG64 symbolDisplacement;
                        result = debugSymbols->GetNameByOffsetWide(
                            frames.at(i).InstructionOffset,
                            &(symbolNameAsVector.front()),
                            static_cast<int>(symbolNameAsVector.size()),
                            &symbolNameSize,
                            &symbolDisplacement);

                        if (result == S_OK) {
                            // Last valid character returned is actually NULL, which we're not interested in keeping.
                            ASSERT(symbolNameAsVector.at(symbolNameSize - 1) == NULL);
                            symbolNameAsVector.resize(symbolNameSize - 1);

                            std::vector<__int8> fpoBuffer(1000);
                            ULONG fpoBufferUsed;
                            result = debugSymbols->GetFunctionEntryByOffset(
                                frames.at(i).InstructionOffset,
                                0,
                                &(fpoBuffer.front()),
                                static_cast<int>(fpoBuffer.size()),
                                &fpoBufferUsed);

                            if (result == S_OK) {
                                fpoBuffer.resize(fpoBufferUsed);

                                size_t functionAddress = 0;
                                switch (fpoBuffer.size()) {
                                    case sizeof(FPO_DATA):
                                        {
                                            const FPO_DATA* const fpoData = reinterpret_cast<FPO_DATA*>(&(fpoBuffer.front()));
                                            functionAddress = fpoData->ulOffStart;
                                            break;
                                        }

                                    case sizeof(IMAGE_FUNCTION_ENTRY):
                                        {
                                            const IMAGE_FUNCTION_ENTRY* const imageFunctionEntry = reinterpret_cast<IMAGE_FUNCTION_ENTRY*>(&(fpoBuffer.front()));
                                            functionAddress = imageFunctionEntry->StartingAddress;
                                            break;
                                        }

                                    default:
                                        ASSERT(false);
                                }

                                ULONG line;
                                std::vector<wchar_t> fileNameAsVector(MAX_PATH * 2);
                                ULONG fileNameSize;
                                ULONG64 lineDisplacement;
                                result = debugSymbols->GetLineByOffsetWide(
                                    frames.at(i).InstructionOffset,
                                    &line,
                                    &(fileNameAsVector.front()),
                                    static_cast<int>(fileNameAsVector.size()),
                                    &fileNameSize,
                                    &lineDisplacement);

                                if (result == S_OK) {
                                    // Last valid character returned is actually NULL, which we're not interested in keeping.
                                    ASSERT(fileNameAsVector.at(fileNameSize - 1) == NULL);
                                    fileNameAsVector.resize(fileNameSize - 1);

                                    PointInProgram pip;
                                    pip.fSymbolName = std::wstring(symbolNameAsVector.begin(), symbolNameAsVector.end()) +
                                        L"@" + Utilities::ToWString(functionAddress);
                                    pip.fSymbolDisplacement = static_cast<int>(symbolDisplacement);
                                    pip.fFileName = std::wstring(fileNameAsVector.begin(), fileNameAsVector.end());
                                    pip.fLineNumber = line;
                                    pip.fLineDisplacement = static_cast<int>(lineDisplacement);

                                    resolvedFrames.push_back(pip);
                                }
                            }
                        }
                    }
                    results.AccumulateCallstack(resolvedFrames);
                }
            }
            results.fEndTimeInSeconds = Utilities::TimeInSeconds();
            profiler.reset();

            if (arguments.fProfileTheProfiler == false) {
                std::wcout << L"Saving report..";
                std::wcout.flush();
                WriteReport(&arguments, &results);
                std::wcout << "Done.\n";
            }

            result = debugClient->DetachProcesses();
            ASSERT(result == S_OK);

            return EXIT_SUCCESS;
        } catch (const xercesc::XMLException& ex) {
            std::wcerr << ex.getType() << ": " << ex.getMessage() << "\n";
        } catch (const std::exception& ex) {
            std::cerr << typeid(ex).name() << ": " << ex.what() << "\n";
        }
        return EXIT_FAILURE;
    }
}

int wmain(int argc, wchar_t** argv)
{
    std::ios::sync_with_stdio(false);
    return Proffy::main(argc, argv);
}
