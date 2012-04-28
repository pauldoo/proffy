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

#include "LookupSymbols.h"

#include "Assert.h"
#include "Utilities.h"

namespace Proffy {
    const Maybe<const PointInProgram> LookupSymbols(
        const unsigned __int64 instructionOffset,
        IDebugSymbols3* const debugSymbols,
        SymbolCache* const cache)
    {
        const SymbolCache::const_iterator iter = cache->find(instructionOffset);

        if (iter != cache->end()) {
            return iter->second;
        } else {
            HRESULT result = S_OK;

            std::vector<wchar_t> symbolNameAsVector(MAX_PATH * 2);
            ULONG symbolNameSize;
            ULONG64 symbolDisplacement;
            result = debugSymbols->GetNameByOffsetWide(
                instructionOffset,
                &(symbolNameAsVector.front()),
                static_cast<int>(symbolNameAsVector.size()),
                &symbolNameSize,
                &symbolDisplacement);

            if (result == S_OK) {
                // Last valid character returned is actually NULL, which we're not interested in keeping.
                ASSERT(symbolNameAsVector.at(symbolNameSize - 1) == NULL);
                symbolNameAsVector.resize(symbolNameSize - 1);

                std::vector<__int8> fpoBuffer(1000);
                ULONG fpoBufferUsed;
                result = debugSymbols->GetFunctionEntryByOffset(
                    instructionOffset,
                    0,
                    &(fpoBuffer.front()),
                    static_cast<int>(fpoBuffer.size()),
                    &fpoBufferUsed);

                if (result == S_OK) {
                    fpoBuffer.resize(fpoBufferUsed);

                    size_t functionAddress = 0;
                    switch (fpoBuffer.size()) {
                        case sizeof(FPO_DATA):
                            {
                                // 32-bit x86
                                const FPO_DATA* const fpoData = reinterpret_cast<FPO_DATA*>(&(fpoBuffer.front()));
                                functionAddress = fpoData->ulOffStart;
                                break;
                            }

                        case sizeof(IMAGE_FUNCTION_ENTRY):
                            {
                                // 64-bit x64
                                const IMAGE_FUNCTION_ENTRY* const imageFunctionEntry = reinterpret_cast<IMAGE_FUNCTION_ENTRY*>(&(fpoBuffer.front()));
                                functionAddress = imageFunctionEntry->StartingAddress;
                                break;
                            }

                        default:
                            ASSERT(false);
                    }

                    ULONG line;
                    std::vector<wchar_t> fileNameAsVector(MAX_PATH * 2);
                    ULONG fileNameSize;
                    ULONG64 lineDisplacement;
                    result = debugSymbols->GetLineByOffsetWide(
                        instructionOffset,
                        &line,
                        &(fileNameAsVector.front()),
                        static_cast<int>(fileNameAsVector.size()),
                        &fileNameSize,
                        &lineDisplacement);

                    if (result == S_OK) {
                        // Last valid character returned is actually NULL, which we're not interested in keeping.
                        ASSERT(fileNameAsVector.at(fileNameSize - 1) == NULL);
                        fileNameAsVector.resize(fileNameSize - 1);

                        const Maybe<const PointInProgram> result(PointInProgram(
                            std::wstring(symbolNameAsVector.begin(), symbolNameAsVector.end()) + L"@" + Utilities::ToWString(functionAddress),
                            static_cast<int>(symbolDisplacement),
                            std::wstring(fileNameAsVector.begin(), fileNameAsVector.end()),
                            line,
                            static_cast<int>(lineDisplacement)));

                        ASSERT(cache->insert(std::make_pair(
                            instructionOffset,
                            result)).second);
                        return LookupSymbols(instructionOffset, debugSymbols, cache);
                    }
                }
            }
        }

        ASSERT(cache->insert(std::make_pair(
            instructionOffset,
            Maybe<const PointInProgram>())).second);
        return LookupSymbols(instructionOffset, debugSymbols, cache);
    }
}

