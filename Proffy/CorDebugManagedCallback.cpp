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

#include "Assert.h"
#include "ConsoleColor.h"
#include "Utilities.h"

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
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::Breakpoint(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugBreakpoint* /*pBreakpoint*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::BreakpointSetError(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugBreakpoint* /*pBreakpoint*/,
        unsigned long /*dwError*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ControlCTrap(
        ICorDebugProcess* /*pProcess*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateAppDomain(
        ICorDebugProcess* pProcess,
        ICorDebugAppDomain* pAppDomain)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        HRESULT result = S_OK;

        result = pAppDomain->Attach();
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        result = pProcess->Continue(FALSE);
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateProcess(
        ICorDebugProcess* pProcess)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        const HRESULT result = pProcess->Continue(FALSE);
        ASSERT(result == S_OK);

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::CreateThread(
        ICorDebugAppDomain* pAppDomain,
        ICorDebugThread* thread)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        HRESULT result = S_OK;

        result = thread->SetDebugState(THREAD_RUN);
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        result = pAppDomain->Continue(FALSE);
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::DebuggerError(
        ICorDebugProcess* /*pProcess*/,
        HRESULT errorHR,
        unsigned long errorCode)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        std::wcout << L"HRESULT: " << Utilities::HresultToString(errorHR).c_str() << L"\n";
        std::wcout << L"CLR Code: " << errorCode << L"\n";

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EditAndContinueRemap(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pFunction*/,
        BOOL /*fAccurate*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EvalComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugEval* /*pEval*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::EvalException(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugEval* /*pEval*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::Exception(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        BOOL /*unhandled*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitAppDomain(
        ICorDebugProcess* /*pProcess*/,
        ICorDebugAppDomain* /*pAppDomain*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitProcess(
        ICorDebugProcess* /*pProcess*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::ExitThread(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*thread*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadAssembly(
        ICorDebugAppDomain* pAppDomain,
        ICorDebugAssembly* pAssembly)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        HRESULT result = S_OK;

        std::vector<wchar_t> nameBuffer(1024);
        ULONG32 nameLength = 0;
        result = pAssembly->GetName(
            static_cast<int>(nameBuffer.size()),
            &nameLength,
            &(nameBuffer.front()));
        ASSERT(result == S_OK);
        nameBuffer.resize(nameLength);
        std::wstring name(nameBuffer.begin(), nameBuffer.end());
        std::wcout << "LoadAssembly: " << name << "\n";

        result = pAppDomain->Continue(FALSE);
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadClass(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugClass* /*c*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LoadModule(
        ICorDebugAppDomain* pAppDomain,
        ICorDebugModule* pModule)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";

        HRESULT result = S_OK;

        std::vector<wchar_t> nameBuffer(1024);
        ULONG32 nameLength = 0;
        result = pModule->GetName(
            static_cast<int>(nameBuffer.size()),
            &nameLength,
            &(nameBuffer.front()));
        ASSERT(result == S_OK);
        nameBuffer.resize(nameLength);
        std::wstring name(nameBuffer.begin(), nameBuffer.end());
        std::wcout << "LoadModule: " << name << "\n";

        result = pAppDomain->Continue(FALSE);
        std::wcout << __LINE__ << ": " << Utilities::HresultToString(result).c_str() << "\n";
        ASSERT(result == S_OK);

        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::LogMessage(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        LONG /*lLevel*/,
        WCHAR* /*pLogSwitchName*/,
        WCHAR* /*pMessage*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
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
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::NameChange(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::StepComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugStepper* /*pStepper*/,
        CorDebugStepReason /*reason*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadAssembly(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugAssembly* /*pAssembly*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadClass(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugClass* /*c*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UnloadModule(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugModule* /*pModule*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT _stdcall CorDebugManagedCallback::UpdateModuleSymbols(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugModule* /*pModule*/,
        IStream* /*pSymbolStream*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::FunctionRemapOpportunity(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pOldFunction*/,
        ICorDebugFunction* /*pNewFunction*/,
        ULONG32 /*oldILOffset*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::CreateConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/,
        WCHAR* /*pConnName*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::ChangeConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::DestroyConnection(
        ICorDebugProcess* /*pProcess*/,
        CONNID /*dwConnectionId*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
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
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::ExceptionUnwind(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        CorDebugExceptionUnwindCallbackType /*dwEventType*/,
        DWORD /*dwFlags*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::FunctionRemapComplete(
        ICorDebugAppDomain* /*pAppDomain*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugFunction* /*pFunction*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall CorDebugManagedCallback::MDANotification(
        ICorDebugController* /*pController*/,
        ICorDebugThread* /*pThread*/,
        ICorDebugMDA* /*pMDA*/)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }
}

