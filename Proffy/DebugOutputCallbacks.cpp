/*
    Copyright (c) 2008, 2012 Paul Richards <paul.richards@gmail.com>

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

#include "DebugOutputCallbacks.h"

#include "ConsoleColor.h"

namespace Proffy {
    DebugOutputCallbacks::~DebugOutputCallbacks()
    {
    }

    HRESULT __stdcall DebugOutputCallbacks::QueryInterface(REFIID interfaceId, PVOID* result)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        *result = NULL;

        if (IsEqualIID(interfaceId, __uuidof(IUnknown)) ||
            IsEqualIID(interfaceId, __uuidof(IDebugOutputCallbacks))) {
            *result = this;
            AddRef();
            return S_OK;
        } else {
            return E_NOINTERFACE;
        }
    }

    ULONG __stdcall DebugOutputCallbacks::AddRef(void)
    {
        return 1;
    }

    ULONG __stdcall DebugOutputCallbacks::Release(void)
    {
        return 0;
    }

    HRESULT __stdcall DebugOutputCallbacks::Output(ULONG /*mask*/, PCSTR text)
    {
        ConsoleColor c(Color_Magenta);
        std::cout << text;
        return S_OK;
    }
}
