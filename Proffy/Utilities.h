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
#pragma once

namespace Proffy {
    namespace Utilities {
        const double TimeInSeconds();

        const std::string DebugStatusReportToString(const ULONG64);

        const std::string DebugSessionStatusToString(const ULONG64);

        const std::string ExecutionStatusToString(const ULONG64);

        const std::string HresultToString(const HRESULT);

        template<typename T>
        const std::wstring ToWString(const T& val)
        {
            std::wostringstream stream;
            stream << val;
            return stream.str();
        }

        template<typename T>
        const T FromWString(const std::wstring& val)
        {
            std::wistringstream stream(val);
            T result;
            stream >> result;
            return result;
        }

        const int Round(const double x);
    }
}
