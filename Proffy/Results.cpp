/*
    Copyright (c) 2008, 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

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

#include "Results.h"

#include "Assert.h"

namespace Proffy
{
    ResultsForSingleThread::ResultsForSingleThread() :
        fNumberOfCallstacks(0)
    {
    }

    ResultsForSingleThread::~ResultsForSingleThread()
    {
    }

    void ResultsForSingleThread::Accumulate(
        const ResultsForSingleThread* const otherResults)
    {
        ASSERT(this != otherResults);

        fNumberOfCallstacks += otherResults->fNumberOfCallstacks;

        for (std::map<std::pair<const PointInProgram*, const PointInProgram*>, int>::const_iterator i = otherResults->fHits.begin(); i != otherResults->fHits.end(); ++i) {
            fHits[i->first] += i->second;
        }
    }

    const std::set<const PointInProgram*> ResultsForSingleThread::EncounteredPoints() const
    {
        std::set<const PointInProgram*> result;
        for (std::map<std::pair<const PointInProgram*, const PointInProgram*>, int>::const_iterator i = fHits.begin(); i != fHits.end(); ++i) {
            if (i->first.first != NULL) {
                result.insert(i->first.first);
            }
            if (i->first.second != NULL) {
                result.insert(i->first.second);
            }
        }
        return result;
    }

    void ResultsForSingleThread::AccumulateCallstack(
        const std::vector<const PointInProgram*>& resolvedFrames)
    {
        fNumberOfCallstacks++;

        const PointInProgram* child = NULL;
        for (int i = 0; i < static_cast<int>(resolvedFrames.size()); i++) {
            const PointInProgram* const current = resolvedFrames.at(i);
            fHits[std::make_pair(current, child)] ++;
            child = current;
        }
    }

    ResultsForAllThreads::ResultsForAllThreads() :
        fNumberOfSamples(0),
        fBeginTimeInSeconds(0.0),
        fEndTimeInSeconds(0.0)
    {
    }

    ResultsForAllThreads::~ResultsForAllThreads()
    {
    }

    void ResultsForAllThreads::AccumulateCallstack(
        const std::wstring& threadId,
        const std::vector<PointInProgram>& resolvedFrames)
    {
        std::vector<const PointInProgram*> resolvedFramesAsPointers;
        for (int i = 0; i < static_cast<int>(resolvedFrames.size()); i++) {
            const PointInProgram* const pip =
                &(*(fEncounteredPoints.insert(resolvedFrames.at(i)).first));
            resolvedFramesAsPointers.push_back(pip);
        }

        fThreadResults[threadId].AccumulateCallstack(resolvedFramesAsPointers);
    }

    const bool operator < (const PointInProgram& lhs, const PointInProgram& rhs)
    {
        if (&lhs == &rhs) {
            return false;
        }
        if (lhs.fSymbolName != rhs.fSymbolName) {
            return lhs.fSymbolName < rhs.fSymbolName;
        }
        if (lhs.fSymbolDisplacement != rhs.fSymbolDisplacement) {
            return lhs.fSymbolDisplacement < rhs.fSymbolDisplacement;
        }
        if (lhs.fFileName != rhs.fFileName) {
            return lhs.fFileName < rhs.fFileName;
        }
        if (lhs.fLineNumber != rhs.fLineNumber) {
            return lhs.fLineNumber < rhs.fLineNumber;
        }
        if (lhs.fLineDisplacement != rhs.fLineDisplacement) {
            return lhs.fLineDisplacement < rhs.fLineDisplacement;
        }
        return false;
    }
}
