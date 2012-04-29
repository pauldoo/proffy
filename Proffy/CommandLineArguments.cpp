/*
    Copyright (c) 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

    Permission to use, copy, modify, and/or distribute this software for any
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
