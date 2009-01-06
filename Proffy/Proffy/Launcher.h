/*
    Copyright (C) 2009  Paul Richards.

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

    /**
        This class is implemented entirely in the header and can be
        used by applications to launch Proffy.
    */
    class Launcher
    {
    public:
        Launcher(
            const std::wstring& proffyDirectory,
            const std::wstring& outputFilename)
        {
            const std::wstring proffyExecutable = proffyDirectory + L"/Proffy.exe";
            std::wostringstream buf;
            buf << proffyExecutable << L" " << ::GetCurrentProcessId() << L" \"" << outputFilename << L"\"";
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


        }

        ~Launcher()
        {
        }

    private:
    };
}

#pragma warning(pop)
