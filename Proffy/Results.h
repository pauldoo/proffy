/*
    Copyright (c) 2008, 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

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
