/*
    Copyright (c) 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

    Permission to use, copy, modify, and/or distribute this software for any
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
                << L"\"" << proffyExecutable << L"\""
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
