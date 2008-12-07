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

#include "DebugEventCallbacks.h"

namespace Proffy {
    DebugEventCallbacks::~DebugEventCallbacks()
    {
    }

    ULONG __stdcall DebugEventCallbacks::AddRef(void)
    {
        return 1;
    }

    ULONG __stdcall DebugEventCallbacks::Release(void)
    {
        return 1;
    }

    HRESULT __stdcall DebugEventCallbacks::GetInterestMask(ULONG* mask)
    {
        *mask =
            DEBUG_EVENT_BREAKPOINT |
            DEBUG_EVENT_EXCEPTION |
            DEBUG_EVENT_CREATE_THREAD |
            DEBUG_EVENT_EXIT_THREAD |
            DEBUG_EVENT_CREATE_PROCESS |
            DEBUG_EVENT_EXIT_PROCESS |
            DEBUG_EVENT_LOAD_MODULE |
            DEBUG_EVENT_UNLOAD_MODULE |
            DEBUG_EVENT_SYSTEM_ERROR |
            DEBUG_EVENT_SESSION_STATUS |
            DEBUG_EVENT_CHANGE_DEBUGGEE_STATE |
            DEBUG_EVENT_CHANGE_ENGINE_STATE |
            DEBUG_EVENT_CHANGE_SYMBOL_STATE;
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ChangeEngineState(
        ULONG flags,
        ULONG64 /*argument*/)
    {
        //if (flags & DEBUG_CES_CURRENT_THREAD) {
        //    std::cout << __FUNCTION__ << ": The current thread has changed, which implies that the current target and current process might also have changed.\n";
        //}
        if (flags & DEBUG_CES_EFFECTIVE_PROCESSOR) {
            std::cout << __FUNCTION__ << ": The effective processor has changed.\n";
        }
        if (flags & DEBUG_CES_BREAKPOINTS) {
            std::cout << __FUNCTION__ << ": One or more breakpoints have changed.\n";
        }
        if (flags & DEBUG_CES_CODE_LEVEL) {
            std::cout << __FUNCTION__ << ": The code interpretation level has changed.\n";
        }
        //if (flags & DEBUG_CES_EXECUTION_STATUS) {
        //    std::cout << __FUNCTION__ << ": The execution status has changed.\n";
        //}
        if (flags & DEBUG_CES_ENGINE_OPTIONS) {
            std::cout << __FUNCTION__ << ": The engine options have changed.\n";
        }
        if (flags & DEBUG_CES_LOG_FILE) {
            std::cout << __FUNCTION__ << ": The log file has been opened or closed.\n";
        }
        if (flags & DEBUG_CES_RADIX) {
            std::cout << __FUNCTION__ << ": The default radix has changed.\n";
        }
        if (flags & DEBUG_CES_EVENT_FILTERS) {
            std::cout << __FUNCTION__ << ": The event filters have changed.\n";
        }
        if (flags & DEBUG_CES_PROCESS_OPTIONS) {
            std::cout << __FUNCTION__ << ": The process options for the current process have changed.\n";
        }
        if (flags & DEBUG_CES_EXTENSIONS) {
            std::cout << __FUNCTION__ << ": Extension DLLs have been loaded or unloaded. (For more information, see Loading Debugger Extension DLLs.)\n";
        }
        if (flags & DEBUG_CES_SYSTEMS) {
            std::cout << __FUNCTION__ << ": A target has been added or removed.\n";
        }
        if (flags & DEBUG_CES_ASSEMBLY_OPTIONS) {
            std::cout << __FUNCTION__ << ": The assemble options have changed.\n";
        }
        if (flags & DEBUG_CES_EXPRESSION_SYNTAX) {
            std::cout << __FUNCTION__ << ": The default expression syntax has changed.\n";
        }
        if (flags & DEBUG_CES_TEXT_REPLACEMENTS) {
            std::cout << __FUNCTION__ << ": Text replacements have changed.\n";
        }

        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::Breakpoint(IDebugBreakpoint* /*breakpoint*/)
    {
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }
}

