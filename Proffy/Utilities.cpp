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

#include "Utilities.h"

#include "Exception.h"

namespace Proffy {
    namespace Utilities {
        const double TimeInSeconds()
        {
            return static_cast<double>(clock()) / CLOCKS_PER_SEC;
        }

        const std::string DebugStatusReportToString(const ULONG64 status)
        {
            // See: http://msdn.microsoft.com/en-gb/library/cc267466.aspx
            switch (status) {
                case DEBUG_STATUS_NO_DEBUGGEE:
                    return "No debugging session is active.";
                case DEBUG_STATUS_BREAK:
                    return "The target is suspended.";
                case DEBUG_STATUS_STEP_INTO:
                    return "The target is executing a single instruction.";
                case DEBUG_STATUS_STEP_BRANCH:
                    return "The target is executing until the next branch instruction.";
                case DEBUG_STATUS_STEP_OVER:
                    return "The target is executing a single instruction or-if that instruction is a subroutine call-subroutine.";
                case DEBUG_STATUS_GO:
                    return "The target is executing normally.";
                case DEBUG_STATUS_RESTART_REQUESTED:
                    return "The target is restarting.";
                case DEBUG_STATUS_NO_CHANGE:
                    return "No change.";
                case DEBUG_STATUS_GO_NOT_HANDLED:
                case DEBUG_STATUS_GO_HANDLED:
                case DEBUG_STATUS_IGNORE_EVENT:
                    return "Not a debug status for reporting.";
                default:
                    return "Not a debug status.";
            }
        }

        const std::string DebugSessionStatusToString(const ULONG64 status)
        {
            // See: http://msdn.microsoft.com/en-gb/library/cc265715.aspx
            switch (status) {
                case DEBUG_SESSION_ACTIVE:
                    return "A debugger session has started.";
                case DEBUG_SESSION_END_SESSION_ACTIVE_TERMINATE:
                    return "The session was ended by sending DEBUG_END_ACTIVE_TERMINATE to EndSession.";
                case DEBUG_SESSION_END_SESSION_ACTIVE_DETACH:
                    return "The session was ended by sending DEBUG_END_ACTIVE_DETACH to EndSession.";
                case DEBUG_SESSION_END_SESSION_PASSIVE:
                    return "The session was ended by sending DEBUG_END_PASSIVE to EndSession.";
                case DEBUG_SESSION_END:
                    return "The target ran to completion, ending the session.";
                case DEBUG_SESSION_REBOOT:
                    return "The target computer rebooted, ending the session.";
                case DEBUG_SESSION_HIBERNATE:
                    return "The target computer went into hibernation, ending the session.";
                case DEBUG_SESSION_FAILURE:
                    return "The engine was unable to continue the session.";
                default:
                    return "Unknown debug session status.";
            }
        }

        const std::string HresultToString(const HRESULT result)
        {
            // See: http://msdn.microsoft.com/en-gb/library/cc267455.aspx
            switch (result) {
                case S_OK:
                    return "Successful completion.";
                case S_FALSE:
                    return "Completed without error, but only partial results were obtained.";
                case E_FAIL:
                    return "The operation could not be performed.";
                case E_INVALIDARG:
                    return "One of the arguments passed was invalid.";
                case E_NOINTERFACE:
                    return "The object searched for was not found.";
                case E_OUTOFMEMORY:
                    return "A memory allocation attempt failed.";
                case E_UNEXPECTED:
                    return "The target was not accessible, or the engine was not in a state where the function or method could be processed.";
                case E_NOTIMPL:
                    return "Not implemented.";
                case E_HANDLE:
                    return "The handle is invalid.";
                case __HRESULT_FROM_WIN32(ERROR_ACCESS_DENIED):
                    return "The operation was denied because the debugger is in Secure Mode.";
                case __HRESULT_FROM_WIN32(ERROR_READ_FAULT):
                    return "WIN32: ERROR_READ_FAULT.";
                case __HRESULT_FROM_WIN32(ERROR_WRITE_FAULT):
                    return "WIN32: ERROR_WRITE_FAULT.";
                case HRESULT_FROM_NT(STATUS_CONTROL_C_EXIT):
                    return "NT: STATUS_CONTROL_C_EXIT.";
                case HRESULT_FROM_NT(STATUS_NO_MORE_ENTRIES):
                    return "NT: STATUS_NO_MORE_ENTRIES.";
                default:
                    std::ostringstream message;
                    message << "Unknown (" << result << ").";
                    return message.str();
            }
        }

        const std::string ExecutionStatusToString(const ULONG64 status)
        {
            // See: http://msdn.microsoft.com/en-gb/library/cc266036.aspx
            switch (status) {
                case DEBUG_STATUS_NO_DEBUGGEE:
                    return "The engine is not attached to a target.";
                case DEBUG_STATUS_STEP_OVER:
                    return "The target is currently executing a single instruction. If that instruction is a subroutine call, the entire call will be executed.";
                case DEBUG_STATUS_STEP_INTO:
                    return "The target is currently executing a single instruction.";
                case DEBUG_STATUS_STEP_BRANCH:
                    return "The target is currently running until it encounters a branch instruction.";
                case DEBUG_STATUS_GO:
                    return "The target is currently running normally. It will continue normal execution until an event occurs.";
                case DEBUG_STATUS_BREAK:
                    return "The target is not running.";
                default:
                    return "Not an execution status.";
            }
        }

        const int Round(const double x)
        {
            return static_cast<int>(floor(x + 0.5));
        }
    }
}
