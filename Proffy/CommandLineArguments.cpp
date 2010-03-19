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
#include "stdafx.h"

#include "CommandLineArguments.h"

#include "Assert.h"
#include "Utilities.h"

namespace Proffy {
    CommandLineArguments::CommandLineArguments() :
        fProcessId(0),
        fStartFlag(0),
        fStopFlag(0),
        fDelayBetweenSamplesInSeconds(0.0),
        fProfileTheProfiler(false)
    {
    }

    CommandLineArguments::~CommandLineArguments()
    {
    }

    const CommandLineArguments CommandLineArguments::Parse(
        const int argc,
        const wchar_t* const * const argv)
    {
        ASSERT(argc == 7);
        CommandLineArguments result;
        result.fProcessId = Utilities::FromWString<int>(argv[1]);
        result.fOutputDirectory = argv[2];
        result.fStartFlag = reinterpret_cast<HANDLE>(Utilities::FromWString<uintptr_t>(argv[3]));
        result.fStopFlag = reinterpret_cast<HANDLE>(Utilities::FromWString<uintptr_t>(argv[4]));
        result.fDelayBetweenSamplesInSeconds = Utilities::FromWString<double>(argv[5]);
        result.fProfileTheProfiler = Utilities::FromWString<bool>(argv[6]);
        return result;
    }
}
