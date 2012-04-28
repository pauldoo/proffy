/*
    Copyright (c) 2008, 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

    Permission to use, copy, modify, and distribute this software for any
    purpose with or without fee is hereby granted, provided that the above
    copyright notice and this permission notice appear in all copies.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
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
            << L"Copyright (c) 2008, 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>\n"
            << L"\n"
            << L"Permission to use, copy, modify, and distribute this software for any\n"
            << L"purpose with or without fee is hereby granted, provided that the above\n"
            << L"copyright notice and this permission notice appear in all copies.\n"
            << L"\n"
            << L"THE SOFTWARE IS PROVIDED \"AS IS\" AND THE AUTHOR DISCLAIMS ALL WARRANTIES\n"
            << L"WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF\n"
            << L"MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR\n"
            << L"ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES\n"
            << L"WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN\n"
            << L"ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF\n"
            << L"OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.\n"
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
            ResultsForAllThreads results;

            // Profiler when used for self-profiling
            std::auto_ptr<Launcher> profiler;
            if (arguments.fProfileTheProfiler) {
                profiler.reset(new Launcher(
                    L"../bin64/Proffy64.exe",
                    arguments.fOutputDirectory,
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
                    if (result == S_OK) {
                        ULONG threadSystemId;
                        result = debugSystemObjects->GetCurrentThreadSystemId(&threadSystemId);
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

                        std::wostringstream threadId;
                        threadId << "thread-" << threadSystemId;
                        results.AccumulateCallstack(threadId.str(), resolvedFrames);
                    }
                }
            }
            results.fEndTimeInSeconds = Utilities::TimeInSeconds();
            profiler.reset();

            if (arguments.fProfileTheProfiler == false) {
                std::wcout << L"Saving report..";
                std::wcout.flush();
                WriteAllReports(&arguments, &results);
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
