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
    namespace Utilities {
        const double TimeInSeconds();

        const std::string DebugStatusReportToString(const ULONG64);

        const std::string DebugSessionStatusToString(const ULONG64);

        const std::string ExecutionStatusToString(const ULONG64);

        const std::string HresultToString(const HRESULT);
    }
}
