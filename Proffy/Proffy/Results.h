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
#pragma once

namespace Proffy {
    struct PointInProgram
    {
        PointInProgram() :
            fSymbolDisplacement(0),
            fLineNumber(0),
            fLineDisplacement(0)
        {
        }

        std::wstring fSymbolName;
        int fSymbolDisplacement;
        std::wstring fFileName;
        int fLineNumber;
        int fLineDisplacement;
    };

    const bool operator < (const PointInProgram&, const PointInProgram&);

    /**
        Samples during the profile run are accumulated into an instance of this
        class.
    */
    class Results
    {
    public:
        Results();

        ~Results();

        void AccumulateCallstack(
            const std::vector<PointInProgram>& resolvedFrames);

        /**
            Number of callstacks collected.  May be larger than
            fNumberOfSamples due to multiple threads running
            in the target.
        */
        int fNumberOfCallstacks;

        /**
            Number of times the profiler paused the target in order
            to collect callstacks. 
        */
        int fNumberOfSamples;

        /**
            Wall clock time at which profiling started.
        */
        double fBeginTimeInSeconds;

        /**
            Wall clock time at which profiling ended.
        */
        double fEndTimeInSeconds;

        std::set<PointInProgram> fEncounteredPoints;

        /**
            Whenever A is found to be calling B, increment the counter
            at fHits[A, B].
        */
        std::map<std::pair<const PointInProgram*, const PointInProgram*>, int> fHits;
    };
}
