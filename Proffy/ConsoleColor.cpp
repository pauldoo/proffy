/*
    Copyright (c) 2008, 2012 Paul Richards <paul.richards@gmail.com>

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
            case Color_Cyan:
                result = ::SetConsoleTextAttribute(handle,
                    FOREGROUND_GREEN | FOREGROUND_BLUE);
                break;
            case Color_Magenta:
                result = ::SetConsoleTextAttribute(handle,
                    FOREGROUND_RED | FOREGROUND_BLUE);
                break;
            case Color_Yellow:
                result = ::SetConsoleTextAttribute(handle,
                    FOREGROUND_RED | FOREGROUND_GREEN);
                break;
            case Color_Normal:
                result = ::SetConsoleTextAttribute(handle,
                    FOREGROUND_RED | FOREGROUND_GREEN | FOREGROUND_BLUE);
                break;
            default:
                ASSERT(false);
        }
        ASSERT(result != 0);

        //std::cout << Utilities::TimeInSeconds() << "\n";
    }

    ConsoleColor::~ConsoleColor()
    {
        const HANDLE handle = ::GetStdHandle(STD_OUTPUT_HANDLE);
        ::SetConsoleTextAttribute(handle, static_cast<WORD>(fOldAttributes));
    }
}