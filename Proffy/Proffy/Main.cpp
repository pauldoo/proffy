/*
    Copyright (C) 2008  Paul Richards.

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
#include "Exception.h"
#include "DebugEventCallbacks.h"
#include "DebugOutputCallbacks.h"

namespace {
    const std::string lines = std::string(50, '-') + "\n";
}

namespace Proffy {
    int main(void)
    {
        try {
            // Start something which we will attempt to profile.
            STARTUPINFO startupInfo = {0};
            PROCESS_INFORMATION processInformation = {0};
            BOOL processStarted = ::CreateProcess(
                L"../Debug/SampleTarget.exe",
                NULL,
                NULL,
                NULL,
                FALSE,
                CREATE_NEW_CONSOLE,
                NULL,
                NULL,
                &startupInfo,
                &processInformation);
            if (processStarted != TRUE) {
                std::ostringstream message;
                message << ::GetLastError();
                throw Exception(message.str());
            }

            // Initialize COM, no idea if this is necessary.
            HRESULT result = S_OK;
            result = ::CoInitialize(0);
            assert(result == S_OK);

            // Get the IDebugClient to start.
            IDebugClient* debugClient = NULL;
            result = ::DebugCreate(
                __uuidof(IDebugClient),
                reinterpret_cast<void**>(&debugClient));
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Set our output callbacks.
            DebugOutputCallbacks debugOutputCallbacks;
            result = debugClient->SetOutputCallbacks(&debugOutputCallbacks);
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Set our event callbacks.
            DebugEventCallbacks debugEventCallbacks;
            result = debugClient->SetEventCallbacks(&debugEventCallbacks);
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Get the IDebugControl.
            IDebugControl* debugControl = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugControl),
                reinterpret_cast<void**>(&debugControl));
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Get the IDebugSystemObjects.
            IDebugSystemObjects* debugSystemObjects = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugSystemObjects),
                reinterpret_cast<void**>(&debugSystemObjects));
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Done getting and setting, so now attach.
            result = debugClient->AttachProcess(
                0,
                processInformation.dwProcessId,
                DEBUG_ATTACH_NONINVASIVE);
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);

            // Verify we have attached to what we think we have.
            ULONG debugeeTypeClass;
            ULONG debugeeTypeQualifier;
            result = debugControl->GetDebuggeeType(&debugeeTypeClass, &debugeeTypeQualifier);
            ASSERT(result == S_OK);
            ASSERT(debugClient->FlushCallbacks() == S_OK);
            ASSERT(debugeeTypeClass == DEBUG_CLASS_USER_WINDOWS);
            ASSERT(debugeeTypeQualifier == DEBUG_USER_WINDOWS_PROCESS);

            while (true) {
                std::cout << lines << "Waiting..\n" << lines;
                result = debugControl->WaitForEvent(0, 3000);
                ASSERT(result == S_OK);
                std::cout << lines << "Done Waiting..\n" << lines;

                ULONG executionStatus;
                result = debugControl->GetExecutionStatus(&executionStatus);
                ASSERT(result == S_OK);
                ASSERT(debugClient->FlushCallbacks() == S_OK);
                std::cout << "ExecutionStatus: " << executionStatus << "\n";

                ULONG numberThreads;
                result = debugSystemObjects->GetNumberThreads(&numberThreads);
                ASSERT(result == S_OK);
                ASSERT(debugClient->FlushCallbacks() == S_OK);
                std::cout << "NumberThreads: " << numberThreads << "\n";

                ASSERT(executionStatus == DEBUG_STATUS_BREAK);

                std::cout << "OutputStackTrace:\n";
                result = debugControl->OutputStackTrace(
                    DEBUG_OUTCTL_THIS_CLIENT,
                    NULL,
                    10,
                    DEBUG_STACK_FRAME_NUMBERS);
                ASSERT(result == S_OK);
                ASSERT(debugClient->FlushCallbacks() == S_OK);

                std::cout << "Sleeping..\n";
                ::Sleep(5000);
                std::cout << "Done sleeping..\n";
                break;
            }

            return EXIT_SUCCESS;
        } catch (const std::exception& ex) {
            std::cerr << typeid(ex).name() << ": " << ex.what() << "\n";
        }
        return EXIT_FAILURE;
    }
}

int main(void) {
    return Proffy::main();
}
