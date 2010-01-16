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
#include "LookupSymbols.h"
#include "Results.h"
#include "Utilities.h"
#include "Version.h"
#include "WriteReport.h"

namespace Proffy {
    const std::wstring lines = std::wstring(50, '-') + L"\n";

    const std::wstring AboutText()
    {
        std::wostringstream result;
        result
            << L"Proffy (" << Version() << L").\n"
            << L"\n"
            << L"\n"
            << L"Copyright (C) 2008, 2009, 2010  Paul Richards.\n"
            << L"\n"
            << L"This program is free software: you can redistribute it and/or modify\n"
            << L"it under the terms of the GNU General Public License as published by\n"
            << L"the Free Software Foundation, either version 3 of the License, or\n"
            << L"(at your option) any later version.\n"
            << L"\n"
            << L"This program is distributed in the hope that it will be useful,\n"
            << L"but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
            << L"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
            << L"GNU General Public License for more details.\n"
            << L"\n"
            << L"You should have received a copy of the GNU General Public License\n"
            << L"along with this program.  If not, see <http://www.gnu.org/licenses/>.\n"
            << L"\n"
            << L"\n"
            << L"Created by Paul Richards <paul.richards@gmail.com>.\n"
            << L"\n"
            << L"http://pauldoo.com/proffy/\n";
            
        return result.str();
    }

    void FlushCallbacks(IDebugClient* const debugClient)
    {
        ASSERT(debugClient->FlushCallbacks() == S_OK);
    }

    const int main(
        const int argc,
        const wchar_t* const * const argv)
    {
        try {
            std::wcout
                << AboutText() << L"\n"
                << lines;
            
            const CommandLineArguments arguments = CommandLineArguments::Parse(argc, argv);

            // Initialize COM, no idea if this is necessary.
            const ComInitialize com;

            HRESULT result = S_OK;
            SymbolCache symbolCache;

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
            DebugEventCallbacks debugEventCallbacks(&symbolCache);
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
            std::wcout << lines << Utilities::TimeInSeconds() << L": Attaching..\n" << lines;
            result = debugClient->AttachProcess(
                0,
                arguments.fProcessId,
                0);
            ASSERT(result == S_OK);
            std::wcout << lines << Utilities::TimeInSeconds() << L": Done Attaching..\n" << lines;

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
                    L"../bin64/Proffy64.exe",
                    arguments.fXmlOutputFilename,
                    arguments.fDotOutputFilename,
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
                        const Maybe<const PointInProgram> pip = LookupSymbols(
                            frames.at(i).InstructionOffset,
                            debugSymbols,
                            &symbolCache);
                        
                        if (pip.Ok()) {
                            resolvedFrames.push_back(pip.Get());
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
            std::wcerr << ex.getType() << L": " << ex.getMessage() << L"\n";
        } catch (const std::exception& ex) {
            std::wcerr << typeid(ex).name() << L": " << ex.what() << L"\n";
        }
        system("pause");
        return EXIT_FAILURE;
    }
}

int wmain(int argc, wchar_t** argv)
{
    std::ios::sync_with_stdio(false);
    return Proffy::main(argc, argv);
}
