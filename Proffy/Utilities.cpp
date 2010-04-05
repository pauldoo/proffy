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
            std::ostringstream message;
            message << _com_error(result).ErrorMessage();
            return message.str();
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
