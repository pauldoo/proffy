/*
    Copyright (C) 2009, 2010  Paul Richards.

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
    const std::wstring Version()
    {
        std::wostringstream result;
        result
            << L"build id " <<
#include "Version.inc"
            << L", compiled on " << __DATE__ << L" at " << __TIME__;
        return result.str();
    }
}
