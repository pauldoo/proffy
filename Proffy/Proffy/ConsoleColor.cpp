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

#include "ConsoleColor.h"

#include "Assert.h"

namespace Proffy
{
    ConsoleColor::ConsoleColor(const eColor color)
    {
        CONSOLE_SCREEN_BUFFER_INFO info = {0};
        const HANDLE handle = ::GetStdHandle(STD_OUTPUT_HANDLE);
        BOOL result = ::GetConsoleScreenBufferInfo(handle, &info);
        ASSERT(result != 0);
        fOldAttributes = info.wAttributes;

        switch (color) {
            case Color_Red:
                result = ::SetConsoleTextAttribute(handle, FOREGROUND_RED);
                break;
            case Color_Green:
                result = ::SetConsoleTextAttribute(handle, FOREGROUND_GREEN);
                break;
            case Color_Blue:
                result = ::SetConsoleTextAttribute(handle, FOREGROUND_BLUE);
                break;
            case Color_Normal:
                result = ::SetConsoleTextAttribute(handle,
                    FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);
                break;
            default:
                ASSERT(false);
        }
        ASSERT(result != 0);
    }

    ConsoleColor::~ConsoleColor()
    {
        const HANDLE handle = ::GetStdHandle(STD_OUTPUT_HANDLE);
        ::SetConsoleTextAttribute(handle, static_cast<WORD>(fOldAttributes));
    }
}