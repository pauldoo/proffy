/*
    Copyright (c) 2008, 2009, 2012 Paul Richards <paul.richards@gmail.com>

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

#include "DebugEventCallbacks.h"

#include "Assert.h"
#include "ConsoleColor.h"
#include "Exception.h"
#include "Utilities.h"

namespace Proffy {
    DebugEventCallbacks::DebugEventCallbacks(SymbolCache* const cache) :
        fSymbolCache(cache)
    {
    }

    DebugEventCallbacks::~DebugEventCallbacks()
    {
    }

    HRESULT __stdcall DebugEventCallbacks::QueryInterface(REFIID interfaceId, PVOID* result)
    {
        ConsoleColor c(Color_Yellow);
        std::cout << __FUNCTION__ << "\n";
        *result = NULL;

        if (IsEqualIID(interfaceId, __uuidof(IUnknown)) ||
            IsEqualIID(interfaceId, __uuidof(IDebugEventCallbacks))) {
            *result = this;
            AddRef();
            return S_OK;
        } else {
            return E_NOINTERFACE;
        }
    }

    ULONG __stdcall DebugEventCallbacks::AddRef(void)
    {
        return 1;
    }

    ULONG __stdcall DebugEventCallbacks::Release(void)
    {
        return 0;
    }

    HRESULT __stdcall DebugEventCallbacks::GetInterestMask(ULONG* mask)
    {
        *mask =
            //DEBUG_EVENT_BREAKPOINT |
            //DEBUG_EVENT_EXCEPTION |
            //DEBUG_EVENT_CREATE_THREAD |
            //DEBUG_EVENT_EXIT_THREAD |
            //DEBUG_EVENT_CREATE_PROCESS |
            //DEBUG_EVENT_EXIT_PROCESS |
            //DEBUG_EVENT_LOAD_MODULE |
            //DEBUG_EVENT_UNLOAD_MODULE |
            //DEBUG_EVENT_SYSTEM_ERROR |
            //DEBUG_EVENT_SESSION_STATUS |
            //DEBUG_EVENT_CHANGE_DEBUGGEE_STATE |
            //DEBUG_EVENT_CHANGE_ENGINE_STATE |
            DEBUG_EVENT_CHANGE_SYMBOL_STATE |
            0;
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ChangeEngineState(
        ULONG flags,
        ULONG64 argument)
    {
        ConsoleColor c(Color_Cyan);
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
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::Exception(
        EXCEPTION_RECORD64* exception,
        ULONG firstChance)
    {
        ASSERT(false);
        exception;
        firstChance;

        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return DEBUG_STATUS_GO_HANDLED;
    }

    HRESULT __stdcall DebugEventCallbacks::CreateThread(
        ULONG64 /*handle*/,
        ULONG64 /*dataOffset*/,
        ULONG64 /*startOffset*/)
    {
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ChangeSymbolState(
        ULONG flags,
        ULONG64 /*argument*/)
    {
        /*
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        */
        switch (flags) {
            case DEBUG_CSS_SCOPE:
                // Ignore
                break;
            case DEBUG_CSS_LOADS:
            case DEBUG_CSS_UNLOADS:
            case DEBUG_CSS_PATHS:
            case DEBUG_CSS_SYMBOL_OPTIONS:
            case DEBUG_CSS_TYPE_OPTIONS:
                {
                    ConsoleColor c(Color_Cyan);
                    std::cout << __FUNCTION__ ": Clearing symbol cache.\n";
                    fSymbolCache->clear();
                    break;
                }
            default:
                ASSERT(false);
        }
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ChangeDebuggeeState(
        ULONG flags,
        ULONG64 /*argument*/)
    {
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        switch (flags) {
            case DEBUG_CDS_ALL:
                std::cout << "    All\n";
                break;
            case DEBUG_CDS_REGISTERS:
                std::cout << "    Registers\n";
                break;
            case DEBUG_CDS_DATA:
                std::cout << "    Data\n";
                break;
            default:
                std::cout << "    " << flags << "\n";
        }
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::SessionStatus(
        ULONG status)
    {
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << ": " << Utilities::DebugSessionStatusToString(status) << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::SystemError(
        ULONG error,
        ULONG level)
    {
        ASSERT(false);
        error;
        level;

        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        __debugbreak();
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::UnloadModule(
        PCSTR /*imageBaseName*/,
        ULONG64 /*baseOffset*/)
    {
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::LoadModule(
        ULONG64 imageFileHandle,
        ULONG64 baseOffset,
        ULONG moduleSize,
        PCSTR moduleName,
        PCSTR imageName,
        ULONG checkSum,
        ULONG timeDateStamp)
    {
        ASSERT(false);
        imageFileHandle;
        baseOffset;
        moduleSize;
        moduleName;
        imageName;
        checkSum;
        timeDateStamp;

        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ExitProcess(
        ULONG /*exitCode*/)
    {
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::CreateProcess(
        ULONG64 /*imageFileHandle*/,
        ULONG64 /*handle*/,
        ULONG64 /*baseOffset*/,
        ULONG /*moduleSize*/,
        PCSTR /*moduleName*/,
        PCSTR /*imageName*/,
        ULONG /*checkSum*/,
        ULONG /*timeDateStamp*/,
        ULONG64 /*initialThreadHandle*/,
        ULONG64 /*threadDataOffset*/,
        ULONG64 /*startOffset*/)
    {
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }

    HRESULT __stdcall DebugEventCallbacks::ExitThread(
        ULONG /*exitCode*/)
    {
        ASSERT(false);
        ConsoleColor c(Color_Cyan);
        std::cout << __FUNCTION__ << "\n";
        return S_OK;
    }
}

