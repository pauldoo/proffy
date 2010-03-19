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

#include "Assert.h"

#include "Exception.h"

namespace Proffy {
    void Assert(
        const bool v,
        const std::string& expression,
        const std::string& function,
        const std::string& file,
        const int lineNumber)
    {
        if (v == false) {
            std::ostringstream message;
            message << "Assertion failure: \"" << expression << "\" in " << function << " (" << file << ", line " << lineNumber << ")";
            throw Exception(message.str());
        }
    }
}
