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
#pragma once

namespace Proffy {
    struct PointInProgram
    {
        PointInProgram(
            const std::wstring& symbolName,
            const int symbolDisplacement,
            const std::wstring& fileName,
            const int lineNumber,
            const int lineDisplacement
        ) :
            fSymbolName(symbolName),
            fSymbolDisplacement(symbolDisplacement),
            fFileName(fileName),
            fLineNumber(lineNumber),
            fLineDisplacement(lineDisplacement)
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
        Records results of the samples collected for a single thread.
        
        Multiple instances of this class are aggregated into ResultsForAllThreads.
    */
    class ResultsForSingleThread
    {
    public:
        ResultsForSingleThread();

        ~ResultsForSingleThread();

        /**
            Does not take ownership of the PointInProgram instances.
            Instead the structures are expected to stay alive for the duration
            of this class.  Usually held in a ResultsForAllThreads object.
        */
        void AccumulateCallstack(
            const std::vector<const PointInProgram*>& resolvedFrames);
        
        /**
            Merges the given results object into this one.
        */
        void Accumulate(
            const ResultsForSingleThread* const);

        /**
            Number of callstacks collected for this thread.
        */
        int fNumberOfCallstacks;
        
        /**
            Returns the set of all PointInProgram objects which are
            present in the fHits map.
        */
        const std::set<const PointInProgram*> EncounteredPoints() const;
        
        /**
            Whenever A is found to be calling B, increment the counter
            at fHits[A, B].
        */
        std::map<std::pair<const PointInProgram*, const PointInProgram*>, int> fHits;
    };
    
    /**
        Samples during the profile run are accumulated into an instance of this
        class.
    */
    class ResultsForAllThreads
    {
    public:
        ResultsForAllThreads();

        ~ResultsForAllThreads();

        void AccumulateCallstack(
            const std::wstring& threadId,
            const std::vector<PointInProgram>& resolvedFrames);

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

    private:
        /**
            Never read from directly.  Used only to store
            PointInProgram instances for the lifetime they
            require.
            
            The ResultsForSingleThread objects store pointers
            to these.
        */
        std::set<PointInProgram> fEncounteredPoints;

    public:
        std::map<std::wstring, ResultsForSingleThread> fThreadResults;
    };    
}
