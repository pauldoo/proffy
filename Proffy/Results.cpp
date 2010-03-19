/*
    Copyright (C) 2008, 2009, 2010  Paul Richards.

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
