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
    class DebugEventCallbacks : public IDebugEventCallbacks
    {
    public:
        virtual ~DebugEventCallbacks();

        virtual HRESULT __stdcall QueryInterface(
            REFIID interfaceId,
            PVOID* result);

        virtual ULONG __stdcall AddRef(void);

        virtual ULONG __stdcall Release(void);

        virtual HRESULT __stdcall GetInterestMask(
            ULONG* mask);

        virtual HRESULT __stdcall ChangeEngineState(
            ULONG flags,
            ULONG64 argument);

        virtual HRESULT __stdcall Breakpoint(
            IDebugBreakpoint* breakpoint);

        virtual HRESULT __stdcall Exception(
            EXCEPTION_RECORD64* exception,
            ULONG firstChance);

        virtual HRESULT __stdcall CreateThread(
            ULONG64 handle,
            ULONG64 dataOffset,
            ULONG64 startOffset);

        virtual HRESULT __stdcall ChangeSymbolState(
            ULONG flags,
            ULONG64 argument);

        virtual HRESULT __stdcall ChangeDebuggeeState(
            ULONG flags,
            ULONG64 argument);

        virtual HRESULT __stdcall SessionStatus(
            ULONG status);

        virtual HRESULT __stdcall SystemError(
            ULONG error,
            ULONG level);

        virtual HRESULT __stdcall UnloadModule(
            PCSTR imageBaseName,
            ULONG64 baseOffset);

        virtual HRESULT __stdcall LoadModule(
            ULONG64 imageFileHandle,
            ULONG64 baseOffset,
            ULONG moduleSize,
            PCSTR moduleName,
            PCSTR imageName,
            ULONG checkSum,
            ULONG timeDateStamp);

        virtual HRESULT __stdcall ExitProcess(
            ULONG exitCode);

        virtual HRESULT __stdcall CreateProcess(
            ULONG64 imageFileHandle,
            ULONG64 handle,
            ULONG64 baseOffset,
            ULONG moduleSize,
            PCSTR moduleName,
            PCSTR imageName,
            ULONG checkSum,
            ULONG timeDateStamp,
            ULONG64 initialThreadHandle,
            ULONG64 threadDataOffset,
            ULONG64 startOffset);

        virtual HRESULT __stdcall ExitThread(
            ULONG exitCode);
    };
}
