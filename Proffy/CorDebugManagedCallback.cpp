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
#include "stdafx.h"

#include "CorDebugManagedCallback.h"

#include "ConsoleColor.h"

namespace Proffy {
    CorDebugManagedCallback::CorDebugManagedCallback()
    {
    }

    CorDebugManagedCallback::~CorDebugManagedCallback()
    {
    }

    HRESULT __stdcall CorDebugManagedCallback::QueryInterface(REFIID interfaceId, PVOID* result)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        *result = NULL;

        if (IsEqualIID(interfaceId, __uuidof(IUnknown)) ||
            IsEqualIID(interfaceId, __uuidof(ICorDebugManagedCallback))) {
            *result = static_cast<ICorDebugManagedCallback*>(this);
            AddRef();
            return S_OK;
        } else if (IsEqualIID(interfaceId, __uuidof(ICorDebugManagedCallback2))) {
            *result = static_cast<ICorDebugManagedCallback2*>(this);
            AddRef();
            return S_OK;
        } else {
            return E_NOINTERFACE;
        }
    }

    ULONG __stdcall CorDebugManagedCallback::AddRef(void)
    {
        return 1;
    }

    ULONG __stdcall CorDebugManagedCallback::Release(void)
    {
        return 0;
    }

    HRESULT _stdcall CorDebugManagedCallback::Break(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*thread*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::Breakpoint(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugBreakpoint* /*pBreakpoint*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::BreakpointSetError(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugBreakpoint* /*pBreakpoint*/,
        unsigned long /*dwError*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ControlCTrap(
        ICorDebugProcess* /*pProcess*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateAppDomain(
        ICorDebugProcess* /*pProcess*/,
        ICorDebugAppDomain* /*pAppDomain*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateProcess(
        ICorDebugProcess* /*pProcess*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateThread(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*thread*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::DebuggerError(
        ICorDebugProcess* /*pProcess*/,
        HRESULT /*errorHR*/,
        unsigned long /*errorCode*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EditAndContinueRemap(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pFunction*/,
        BOOL /*fAccurate*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EvalComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugEval* /*pEval*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EvalException(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugEval* /*pEval*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::Exception(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        BOOL /*unhandled*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitAppDomain(
        ICorDebugProcess* /*pProcess*/,
        ICorDebugAppDomain* /*pAppDomain*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitProcess(
        ICorDebugProcess* /*pProcess*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitThread(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*thread*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadAssembly(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugAssembly* /*pAssembly*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadClass(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugClass* /*c*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadModule(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugModule* /*pModule*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LogMessage(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        LONG /*lLevel*/,
        WCHAR* /*pLogSwitchName*/,
        WCHAR* /*pMessage*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LogSwitch(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        LONG /*lLevel*/,
        ULONG /*ulReason*/,
        WCHAR* /*pLogSwitchName*/,
        WCHAR* /*pParentName*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::NameChange(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::StepComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugStepper* /*pStepper*/,
        CorDebugStepReason /*reason*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadAssembly(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugAssembly* /*pAssembly*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadClass(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugClass* /*c*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadModule(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugModule* /*pModule*/)
    {
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UpdateModuleSymbols(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugModule* /*pModule*/,
        IStream* /*pSymbolStream*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::FunctionRemapOpportunity(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pOldFunction*/,
        ICorDebugFunction* /*pNewFunction*/,
        ULONG32 /*oldILOffset*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::CreateConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/,
        WCHAR* /*pConnName*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::ChangeConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::DestroyConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::Exception(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFrame* /*pFrame*/,
        ULONG32 /*nOffset*/,
        CorDebugExceptionCallbackType /*dwEventType*/,
        DWORD /*dwFlags*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::ExceptionUnwind(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        CorDebugExceptionUnwindCallbackType /*dwEventType*/,
        DWORD /*dwFlags*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::FunctionRemapComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pFunction*/)
    {
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::MDANotification(
        ICorDebugController* /*pController*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugMDA* /*pMDA*/)
    {
        return S_OK;
    }
}

