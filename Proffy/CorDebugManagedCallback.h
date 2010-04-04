/*
    Copyright (C) 2010  Paul Richards.

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
    class CorDebugManagedCallback : public ICorDebugManagedCallback, public ICorDebugManagedCallback2
    {
    public:
        CorDebugManagedCallback();

        virtual ~CorDebugManagedCallback();

        virtual HRESULT __stdcall QueryInterface(
            REFIID interfaceId,
            PVOID* result);

        virtual ULONG __stdcall AddRef(void);

        virtual ULONG __stdcall Release(void);

        virtual HRESULT _stdcall Break(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* thread);

        virtual HRESULT _stdcall Breakpoint(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugBreakpoint* pBreakpoint);

        virtual HRESULT _stdcall BreakpointSetError(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugBreakpoint* pBreakpoint,
            unsigned long dwError);

        virtual HRESULT _stdcall ControlCTrap(
            ICorDebugProcess* pProcess);

        virtual HRESULT _stdcall CreateAppDomain(
            ICorDebugProcess* pProcess,
            ICorDebugAppDomain* pAppDomain);

        virtual HRESULT _stdcall CreateProcess(
            ICorDebugProcess* pProcess);

        virtual HRESULT _stdcall CreateThread(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* thread);

        virtual HRESULT _stdcall DebuggerError(
            ICorDebugProcess* pProcess,
            HRESULT errorHR,
            unsigned long errorCode);

        virtual HRESULT _stdcall EditAndContinueRemap(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugFunction* pFunction,
            BOOL fAccurate);

        virtual HRESULT _stdcall EvalComplete(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugEval* pEval);

        virtual HRESULT _stdcall EvalException(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugEval* pEval);

        virtual HRESULT _stdcall Exception(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            BOOL unhandled);

        virtual HRESULT _stdcall ExitAppDomain(
            ICorDebugProcess* pProcess,
            ICorDebugAppDomain* pAppDomain);

        virtual HRESULT _stdcall ExitProcess(
            ICorDebugProcess* pProcess);

        virtual HRESULT _stdcall ExitThread(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* thread);

        virtual HRESULT _stdcall LoadAssembly(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugAssembly* pAssembly);

        virtual HRESULT _stdcall LoadClass(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugClass* c);

        virtual HRESULT _stdcall LoadModule(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugModule* pModule);

        virtual HRESULT _stdcall LogMessage(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            LONG lLevel,
            WCHAR* pLogSwitchName,
            WCHAR* pMessage);

        virtual HRESULT _stdcall LogSwitch(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            LONG lLevel,
            ULONG ulReason,
            WCHAR* pLogSwitchName,
            WCHAR* pParentName);

        virtual HRESULT _stdcall NameChange(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread);

        virtual HRESULT _stdcall StepComplete(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugThread* pThread,
            ICorDebugStepper* pStepper,
            CorDebugStepReason reason);

        virtual HRESULT _stdcall UnloadAssembly(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugAssembly* pAssembly);

        virtual HRESULT _stdcall UnloadClass(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugClass* c);

        virtual HRESULT _stdcall UnloadModule(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugModule* pModule);

        virtual HRESULT _stdcall UpdateModuleSymbols(
            ICorDebugAppDomain* pAppDomain,
            ICorDebugModule* pModule,
            IStream* pSymbolStream);

        virtual HRESULT __stdcall FunctionRemapOpportunity(
            ICorDebugAppDomain *pAppDomain,
            ICorDebugThread *pThread,
            ICorDebugFunction *pOldFunction,
            ICorDebugFunction *pNewFunction,
            ULONG32 oldILOffset);

        virtual HRESULT __stdcall CreateConnection(
            ICorDebugProcess *pProcess,
            CONNID dwConnectionId,
            WCHAR *pConnName);

        virtual HRESULT __stdcall ChangeConnection(
            ICorDebugProcess *pProcess,
            CONNID dwConnectionId);

        virtual HRESULT __stdcall DestroyConnection(
            ICorDebugProcess *pProcess,
            CONNID dwConnectionId);

        virtual HRESULT __stdcall Exception(
            ICorDebugAppDomain *pAppDomain,
            ICorDebugThread *pThread,
            ICorDebugFrame *pFrame,
            ULONG32 nOffset,
            CorDebugExceptionCallbackType dwEventType,
            DWORD dwFlags);

        virtual HRESULT __stdcall ExceptionUnwind(
            ICorDebugAppDomain *pAppDomain,
            ICorDebugThread *pThread,
            CorDebugExceptionUnwindCallbackType dwEventType,
            DWORD dwFlags);

        virtual HRESULT __stdcall FunctionRemapComplete(
            ICorDebugAppDomain *pAppDomain,
            ICorDebugThread *pThread,
            ICorDebugFunction *pFunction);

        virtual HRESULT __stdcall MDANotification(
            ICorDebugController *pController,
            ICorDebugThread *pThread,
            ICorDebugMDA *pMDA);
    };
}

