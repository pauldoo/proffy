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

    class OwnedHandle
    {
    public:
        OwnedHandle(const HANDLE handle) : fHandle(handle)
        {
        }

        ~OwnedHandle()
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
            std::wostringstream uid;
            uid << ::GetCurrentProcessId() << L"-" << this;
            const std::wstring semaphoreStartName = L"Proffy_" + uid.str() + L"_start";
            const std::wstring semaphoreStopName = L"Proffy_" + uid.str() + L"_stop";
            
            const OwnedHandle startFlag = OwnedHandle(::CreateSemaphoreW(NULL, 0, 10, semaphoreStartName.c_str()));
            fStopFlag.reset(new OwnedHandle(::CreateSemaphoreW(NULL, 0, 10, semaphoreStopName.c_str())));
            if (startFlag.fHandle == NULL || fStopFlag->fHandle == NULL) {
                throw LauncherException("Unable to create semaphores.");
            }

            std::wostringstream buf;
            buf
                << proffyExecutable
                << L" " << ::GetCurrentProcessId()
                << L" \"" << outputDirectory << L"\""
                << L" " << semaphoreStartName
                << L" " << semaphoreStopName
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
                FALSE,
                CREATE_NEW_CONSOLE,
                NULL,
                NULL,
                &startupInfo,
                &processInformation);

            if (result == FALSE) {
                throw LauncherException("Failed to launch profiler.");
            }

            ::CloseHandle(processInformation.hThread);
            fProfilerProcess.reset(new OwnedHandle(processInformation.hProcess));

            if (::WaitForSingleObject(startFlag.fHandle, INFINITE) != WAIT_OBJECT_0) {
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
        std::auto_ptr<OwnedHandle> fStopFlag;
        std::auto_ptr<OwnedHandle> fProfilerProcess;
    };
}

#pragma warning(pop)
