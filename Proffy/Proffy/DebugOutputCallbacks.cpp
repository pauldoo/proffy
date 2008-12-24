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
