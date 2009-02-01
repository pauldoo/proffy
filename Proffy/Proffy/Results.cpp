/*
    Copyright (C) 2008, 2009  Paul Richards.

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
    Results::Results() :
        fNumberOfCallstacks(0),
        fNumberOfSamples(0),
        fBeginTimeInSeconds(0.0),
        fEndTimeInSeconds(0.0)
    {
    }

    Results::~Results()
    {
    }

    void Results::AccumulateCallstack(
        const std::vector<PointInProgram>& resolvedFrames)
    {
        fNumberOfCallstacks++;

        const PointInProgram* child = NULL;
        for (int i = 0; i < static_cast<int>(resolvedFrames.size()); i++) {
            const PointInProgram* const current =
                &(*(fEncounteredPoints.insert(resolvedFrames.at(i)).first));

            fHits[std::make_pair(current, child)] ++;
            child = current;
        }
    }

    const bool operator < (const PointInProgram& lhs, const PointInProgram& rhs)
    {
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
