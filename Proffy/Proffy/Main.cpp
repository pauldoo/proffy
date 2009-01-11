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
#include "ConsoleColor.h"
#include "Exception.h"
#include "DebugEventCallbacks.h"
#include "DebugOutputCallbacks.h"
#include "Results.h"
#include "Utilities.h"
#include "WriteReport.h"

namespace Proffy {
    const std::string lines = std::string(50, '-') + "\n";

    struct CommandLineArguments
    {
        int fProcessId;
        std::wstring fOutputFilename;
        HANDLE fStartFlag;
        HANDLE fStopFlag;
    };

    void FlushCallbacks(IDebugClient* const debugClient)
    {
        ASSERT(debugClient->FlushCallbacks() == S_OK);
    }

    const CommandLineArguments ParseCommandLine(
        const int argc,
        const wchar_t* const * const argv)
    {
        ASSERT(argc == 5);
        CommandLineArguments result;
        result.fProcessId = Utilities::FromWString<int>(argv[1]);
        result.fOutputFilename = argv[2];
        result.fStartFlag = reinterpret_cast<HANDLE>(Utilities::FromWString<uintptr_t>(argv[3]));
        result.fStopFlag = reinterpret_cast<HANDLE>(Utilities::FromWString<uintptr_t>(argv[4]));
        return result;
    }

    const int main(
        const int argc,
        const wchar_t* const * const argv)
    {
        try {
            const CommandLineArguments arguments = ParseCommandLine(argc, argv);

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

            ASSERT(::ReleaseSemaphore(arguments.fStartFlag, 1, NULL) != FALSE);

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

                result = debugControl->WaitForEvent(
                    DEBUG_WAIT_DEFAULT,
                    10);
                ASSERT(result == S_OK || result == S_FALSE);

                ULONG executionStatus;
                result = debugControl->GetExecutionStatus(&executionStatus);
                ASSERT(result == S_OK);
                ASSERT(executionStatus == DEBUG_STATUS_BREAK);

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
                        frames.size(),
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
                            symbolNameAsVector.size(),
                            &symbolNameSize,
                            &symbolDisplacement);

                        if (result == S_OK) {
                            ULONG line;
                            std::vector<wchar_t> fileNameAsVector(MAX_PATH * 2);
                            ULONG fileNameSize;
                            ULONG64 lineDisplacement;
                            result = debugSymbols->GetLineByOffsetWide(
                                frames.at(i).InstructionOffset, 
                                &line, 
                                &(fileNameAsVector.front()),
                                fileNameAsVector.size(), 
                                &fileNameSize, 
                                &lineDisplacement);

                            if (result == S_OK) {
                                PointInProgram pip;
                                
                                pip.fSymbolName = std::wstring(
                                    symbolNameAsVector.begin(),
                                    symbolNameAsVector.begin() + symbolNameSize - 1);
                                pip.fSymbolDisplacement = static_cast<int>(symbolDisplacement);
                                pip.fFileName = std::wstring(
                                    fileNameAsVector.begin(),
                                    fileNameAsVector.begin() + fileNameSize - 1);
                                pip.fLineNumber = line;
                                pip.fLineDisplacement = static_cast<int>(lineDisplacement);

                                resolvedFrames.push_back(pip);
                            }
                        }
                    }
                    results.AccumulateSample(resolvedFrames);
                }
            }

            std::wcout << L"Saving report..";
            std::wcout.flush();
            WriteReport(&results, arguments.fOutputFilename);
            std::wcout << "Done.\n";

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
