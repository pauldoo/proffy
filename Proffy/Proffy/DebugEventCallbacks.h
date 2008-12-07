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
#pragma once

namespace Proffy {
    class DebugEventCallbacks : public DebugBaseEventCallbacks
    {
    public:
        virtual ~DebugEventCallbacks();

        virtual ULONG __stdcall AddRef(void);

        virtual ULONG __stdcall Release(void);

        virtual HRESULT __stdcall GetInterestMask(ULONG* mask);

        virtual HRESULT __stdcall ChangeEngineState(
            ULONG flags,
            ULONG64 argument);

        virtual HRESULT __stdcall Breakpoint(IDebugBreakpoint* breakpoint);
    };
}
