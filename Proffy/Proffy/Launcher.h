/*
    Copyright (C) 2009, 2010  Paul Richards.

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
#pragma once

#include <Windows.h>
#include <exception>
#include <memory>
#include <sstream>
#include <string>

#pragma warning(push)
#pragma warning(disable: 4512) // assignment operator could not be generated

namespace Proffy {
    class LauncherException : public std::exception
    {
    public:
        LauncherException(const std::string& reason) :
            fReason(reason)
        {
        }

        /// std::exception
        virtual const char* what () const throw ()
        {
            return fReason.c_str();
        }

    private:
        const std::string fReason;
    };

    class LauncherOwnedHandle
    {
    public:
        LauncherOwnedHandle(const HANDLE handle) : fHandle(handle)
        {
        }

        ~LauncherOwnedHandle()
        {
            ::CloseHandle(fHandle);
        }

        const HANDLE fHandle;
    };

    /**
        This class is implemented entirely in the header and can be
        used by applications to launch Proffy.
    */
    class Launcher
    {
    public:
        Launcher(
            const std::wstring& proffyExecutable,
            const std::wstring& outputDirectory,
            const double delayBetweenSamplesInSeconds,
            const bool profileTheProfiler = false)
        {
            SECURITY_ATTRIBUTES security = {0};
            security.nLength = sizeof(security);
            security.bInheritHandle = TRUE;
            fStartFlag.reset(new LauncherOwnedHandle(::CreateSemaphoreW(&security, 0, LONG_MAX, NULL)));
            fStopFlag.reset(new LauncherOwnedHandle(::CreateSemaphoreW(&security, 0, LONG_MAX, NULL)));

            std::wostringstream buf;
            buf
                << proffyExecutable
                << L" " << ::GetCurrentProcessId()
                << L" \"" << outputDirectory << L"\""
                << L" " << reinterpret_cast<uintptr_t>(fStartFlag->fHandle)
                << L" " << reinterpret_cast<uintptr_t>(fStopFlag->fHandle)
                << L" " << delayBetweenSamplesInSeconds
                << L" " << profileTheProfiler;
            const std::wstring commandLine = buf.str();

            STARTUPINFOW startupInfo = {0};
            PROCESS_INFORMATION processInformation = {0};

            BOOL result = ::CreateProcessW(
                proffyExecutable.c_str(),
                const_cast<wchar_t*>(commandLine.c_str()),
                NULL,
                NULL,
                TRUE,
                CREATE_NEW_CONSOLE,
                NULL,
                NULL,
                &startupInfo,
                &processInformation);

            if (result == FALSE) {
                throw LauncherException("Failed to launch profiler.");
            }

            ::CloseHandle(processInformation.hThread);
            fProfilerProcess.reset(new LauncherOwnedHandle(processInformation.hProcess));

            if (::WaitForSingleObject(fStartFlag->fHandle, INFINITE) != WAIT_OBJECT_0) {
                throw LauncherException("Failed while waiting for the profiler to start.");
            }
        }

        /**
            Signal the profiler to stop, and wait for it to do so.
        */
        void Finish()
        {
            if (::ReleaseSemaphore(fStopFlag->fHandle, 1, NULL) == FALSE) {
                throw LauncherException("Failed to raise stop flag.");
            }

            if (::WaitForSingleObject(fProfilerProcess->fHandle, INFINITE) != WAIT_OBJECT_0) {
                throw LauncherException("Failed while waiting for the profiler to exit.");
            }
        }

        ~Launcher()
        {
            try {
                Finish();
            } catch (const LauncherException&) {
            }
        }

    private:
        std::auto_ptr<LauncherOwnedHandle> fStartFlag;
        std::auto_ptr<LauncherOwnedHandle> fStopFlag;
        std::auto_ptr<LauncherOwnedHandle> fProfilerProcess;
    };
}

#pragma warning(pop)
