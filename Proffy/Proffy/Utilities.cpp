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
        const std::string DebugStatusReportToString(const ULONG64 status)
        {
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

        const std::string HresultToString(const HRESULT result)
        {
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
    }
}
