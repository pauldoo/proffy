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

#include "Exception.h"
#include "Utilities.h"

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
        ULONG64 argument)
    {
        std::cout << __FUNCTION__ ": Begin.\n";
        if (flags & DEBUG_CES_CURRENT_THREAD) {
            flags &= ~DEBUG_CES_CURRENT_THREAD;
            std::cout << "The current thread has changed, which implies that the current target and current process might also have changed.\n";
        }
        if (flags & DEBUG_CES_EFFECTIVE_PROCESSOR) {
            flags &= ~DEBUG_CES_EFFECTIVE_PROCESSOR;
            std::cout << "The effective processor has changed.\n";
        }
        if (flags & DEBUG_CES_BREAKPOINTS) {
            flags &= ~DEBUG_CES_BREAKPOINTS;
            std::cout << "One or more breakpoints have changed.\n";
        }
        if (flags & DEBUG_CES_CODE_LEVEL) {
            flags &= ~DEBUG_CES_CODE_LEVEL;
            std::cout << "The code interpretation level has changed.\n";
        }
        if (flags & DEBUG_CES_EXECUTION_STATUS) {
            flags &= ~DEBUG_CES_EXECUTION_STATUS;
            std::cout << "The execution status has changed.\n";
            const bool insideWait = (argument & DEBUG_STATUS_INSIDE_WAIT) != 0;
 
            std::cout << "  Inside wait: " << insideWait << "\n";
            std::cout << "  Status: " << Utilities::DebugStatusReportToString(argument & ~DEBUG_STATUS_INSIDE_WAIT) << "\n";
        }
        if (flags & DEBUG_CES_ENGINE_OPTIONS) {
            flags &= ~DEBUG_CES_ENGINE_OPTIONS;
            std::cout << "The engine options have changed.\n";
        }
        if (flags & DEBUG_CES_LOG_FILE) {
            flags &= ~DEBUG_CES_LOG_FILE;
            std::cout << "The log file has been opened or closed.\n";
        }
        if (flags & DEBUG_CES_RADIX) {
            flags &= ~DEBUG_CES_RADIX;
            std::cout << "The default radix has changed.\n";
        }
        if (flags & DEBUG_CES_EVENT_FILTERS) {
            flags &= ~DEBUG_CES_EVENT_FILTERS;
            std::cout << "The event filters have changed.\n";
        }
        if (flags & DEBUG_CES_PROCESS_OPTIONS) {
            flags &= ~DEBUG_CES_PROCESS_OPTIONS;
            std::cout << "The process options for the current process have changed.\n";
        }
        if (flags & DEBUG_CES_EXTENSIONS) {
            flags &= ~DEBUG_CES_EXTENSIONS;
            std::cout << "Extension DLLs have been loaded or unloaded. (For more information, see Loading Debugger Extension DLLs.)\n";
        }
        if (flags & DEBUG_CES_SYSTEMS) {
            flags &= ~DEBUG_CES_SYSTEMS;
            std::cout << "A target has been added or removed.\n";
        }
        if (flags & DEBUG_CES_ASSEMBLY_OPTIONS) {
            flags &= ~DEBUG_CES_ASSEMBLY_OPTIONS;
            std::cout << "The assemble options have changed.\n";
        }
        if (flags & DEBUG_CES_EXPRESSION_SYNTAX) {
            flags &= ~DEBUG_CES_EXPRESSION_SYNTAX;
            std::cout << "The default expression syntax has changed.\n";
        }
        if (flags & DEBUG_CES_TEXT_REPLACEMENTS) {
            flags &= ~DEBUG_CES_TEXT_REPLACEMENTS;
            std::cout << "Text replacements have changed.\n";
        }
        if (flags != 0) {
            std::ostringstream message;
            message << "Unknown flag: " << flags;
            throw Proffy::Exception(message.str());
        }
        std::cout << __FUNCTION__ ": End.\n";

        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::Breakpoint(IDebugBreakpoint* /*breakpoint*/)
    {
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }
}

