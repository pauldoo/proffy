/*
    Copyright (C) 2008, 2009, 2010  Paul Richards.

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
#include "CorDebugManagedCallback.h"
#include "CorDebugUnmanagedCallback.h"
#include "DebugEventCallbacks.h"
#include "DebugOutputCallbacks.h"
#include "Exception.h"
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

            const OwnedHandle processHandle = OwnedHandle(::OpenProcess(
                PROCESS_VM_READ | PROCESS_QUERY_INFORMATION,
                FALSE,
                arguments.fProcessId));
            ASSERT(processHandle.fHandle != NULL);

            std::vector<wchar_t> versionBuffer(1024);
            DWORD versionBufferLength = 0;
            result = ::GetVersionFromProcess(
                processHandle.fHandle,
                &(versionBuffer.front()),
                static_cast<int>(versionBuffer.size()),
                &versionBufferLength);
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);
            versionBuffer.resize(versionBufferLength);

            IUnknown* corDebugAsUnknown = NULL;
            result = ::CreateDebuggingInterfaceFromVersion(
                CorDebugVersion_2_0,
                &(versionBuffer.front()),
                &corDebugAsUnknown);
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);
            ASSERT(corDebugAsUnknown != NULL);

            ICorDebug* corDebug = NULL;
            result = corDebugAsUnknown->QueryInterface(
                __uuidof(ICorDebug),
                reinterpret_cast<void**>(&corDebug));
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);
            ASSERT(corDebug != NULL);

            result = corDebug->Initialize();
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);

            CorDebugManagedCallback managedCallback;
            result = corDebug->SetManagedHandler(&managedCallback);
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);

            CorDebugUnmanagedCallback unmanagedCallback;
            result = corDebug->SetUnmanagedHandler(&unmanagedCallback);
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);

            ICorDebugProcess* corDebugProcess = NULL;
            result = corDebug->DebugActiveProcess(
                arguments.fProcessId,
                FALSE,
                &corDebugProcess);
            std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
            ASSERT(result == S_OK);
            ASSERT(corDebugProcess != NULL);

            const int delayBetweenSamplesInMilliseconds = Utilities::Round(arguments.fDelayBetweenSamplesInSeconds * 1000.0);

            OwnedHandle stopFlag = OwnedHandle(::CreateSemaphoreW(NULL, 0, 10, arguments.fStopFlag.c_str()));
            ASSERT(stopFlag.fHandle != NULL);
            ASSERT(::GetLastError() == ERROR_ALREADY_EXISTS);

            {
                OwnedHandle startFlag = OwnedHandle(::CreateSemaphoreW(NULL, 0, 10, arguments.fStartFlag.c_str()));
                ASSERT(startFlag.fHandle != NULL);
                ASSERT(::GetLastError() == ERROR_ALREADY_EXISTS);
                ASSERT(::ReleaseSemaphore(startFlag.fHandle, 1, NULL) != FALSE);
            }

            while (true) {
                std::wcout << L".";
                std::wcout.flush();
                result = ::WaitForSingleObject(stopFlag.fHandle, delayBetweenSamplesInMilliseconds);
                if (result == WAIT_OBJECT_0) {
                    // Flag is raised
                    break;
                } else if (result == WAIT_TIMEOUT) {
                    // Flag is not raised
                } else {
                    ASSERT(false);
                }

                result = corDebugProcess->Stop(INFINITE);
                ASSERT(result == S_OK);

                DWORD helperThreadId = 0;
                result = corDebugProcess->GetHelperThreadID(&helperThreadId);
                ASSERT(result == S_OK);

                {
                    OwnedHandle snapshot = OwnedHandle(::CreateToolhelp32Snapshot(TH32CS_SNAPTHREAD, 0));
                    ASSERT(snapshot.fHandle != INVALID_HANDLE_VALUE);

                    THREADENTRY32 threadEntry;
                    threadEntry.dwSize = static_cast<int>(sizeof(threadEntry));
                    if (Thread32First(snapshot.fHandle, &threadEntry) == TRUE) {
                        do {
                            if (threadEntry.th32OwnerProcessID == arguments.fProcessId &&
                                threadEntry.th32ThreadID != helperThreadId) {

                                ICorDebugThread* corDebugThread = NULL;
                                result = corDebugProcess->GetThread(
                                    threadEntry.th32ThreadID,
                                    &corDebugThread);

                                if (result == S_OK) {
                                    ASSERT(result == S_OK);
                                    ASSERT(corDebugThread != NULL);

                                    ICorDebugThread2* corDebugThread2 = NULL;
                                    result = corDebugThread->QueryInterface(
                                        __uuidof(ICorDebugThread2),
                                        reinterpret_cast<void**>(&corDebugThread2));
                                    ASSERT(result == S_OK);
                                    ASSERT(corDebugThread2 != NULL);


                                    std::vector<COR_ACTIVE_FUNCTION> activeFunctions(100);
                                    ULONG32 activeFunctionsSize = 0;
                                    result = corDebugThread2->GetActiveFunctions(
                                        static_cast<int>(activeFunctions.size()),
                                        &activeFunctionsSize,
                                        &(activeFunctions.front()));
                                    ASSERT(result == S_OK);
                                    ASSERT(activeFunctionsSize <= activeFunctions.size());
                                    activeFunctions.resize(activeFunctionsSize);

                                    std::wcout << "Stack:\n";
                                    for (int i = 0; i < activeFunctions.size(); i++) {
                                        std::wcout << activeFunctions.at(i).ilOffset << "\n";
                                    }
                                }
                            }
                        } while (Thread32Next(snapshot.fHandle, &threadEntry) == TRUE);
                    }
                }

                result = corDebugProcess->Continue(FALSE);
                ASSERT(result == S_OK);
            }
            std::wcout << L"\n";

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
