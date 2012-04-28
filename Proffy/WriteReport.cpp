/*
    Copyright (c) 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

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

#include "WriteReport.h"

#include "Assert.h"
#include "CommandLineArguments.h"
#include "Results.h"
#include "Utilities.h"
#include "XercesInitialize.h"

namespace Proffy {
    void WriteXmlReport(
        const CommandLineArguments* const arguments,
        const std::wstring& title,
        const int numberOfSamples,
        const double wallClockTimeInSeconds,
        const ResultsForSingleThread* const results)
    {
        XercesInitialize xerces;
        xercesc::DOMImplementation* const domImplementation =
            xercesc::DOMImplementationRegistry::getDOMImplementation(NULL);
        ASSERT(domImplementation != NULL);

        xercesc::DOMDocument* const document = domImplementation->createDocument();
        document->appendChild(document->createProcessingInstruction(L"xml-stylesheet", L"type=\"text/xsl\" href=\"Xhtml.xsl\""));
        xercesc::DOMElement* const root = document->createElement(L"ProffyResults");
        document->appendChild(root);

        {
            xercesc::DOMElement* const summary = document->createElement(L"Summary");

            xercesc::DOMElement* const delayBetweenSamplesInSeconds = document->createElement(L"DelayBetweenSamplesInSeconds");
            delayBetweenSamplesInSeconds->setTextContent(Utilities::ToWString(arguments->fDelayBetweenSamplesInSeconds).c_str());
            summary->appendChild(delayBetweenSamplesInSeconds);

            xercesc::DOMElement* const titleNode = document->createElement(L"Title");
            titleNode->setTextContent(title.c_str());
            summary->appendChild(titleNode);

            xercesc::DOMElement* const sampleCount = document->createElement(L"SampleCount");
            sampleCount->setTextContent(Utilities::ToWString(numberOfSamples).c_str());
            summary->appendChild(sampleCount);

            xercesc::DOMElement* const callstackCount = document->createElement(L"CallstackCount");
            callstackCount->setTextContent(Utilities::ToWString(results->fNumberOfCallstacks).c_str());
            summary->appendChild(callstackCount);

            xercesc::DOMElement* const wallClockTimeInSecondsNode = document->createElement(L"WallClockTimeInSeconds");
            wallClockTimeInSecondsNode->setTextContent(Utilities::ToWString(wallClockTimeInSeconds).c_str());
            summary->appendChild(wallClockTimeInSecondsNode);

            root->appendChild(summary);
        }

        const std::set<const PointInProgram*> encounteredPoints = results->EncounteredPoints();

        std::set<std::wstring> sourceFiles;
        {
            xercesc::DOMElement* const pointsEncountered = document->createElement(L"PointsEncountered");

            for (std::set<const PointInProgram*>::const_iterator i = encounteredPoints.begin();
                i != encounteredPoints.end();
                ++i) {
                const int id = static_cast<int>(std::distance(encounteredPoints.begin(), i));

                sourceFiles.insert((*i)->fFileName);

                xercesc::DOMElement* const point = document->createElement(L"Point");
                point->setAttribute(L"Id",
                    Utilities::ToWString<int>(id).c_str());
                point->setAttribute(L"SymbolName",
                    (*i)->fSymbolName.c_str());
                point->setAttribute(L"SymbolDisplacement",
                    Utilities::ToWString<int>((*i)->fSymbolDisplacement).c_str());
                point->setAttribute(L"FileName",
                    (*i)->fFileName.c_str());
                point->setAttribute(L"LineNumber",
                    Utilities::ToWString<int>((*i)->fLineNumber).c_str());
                point->setAttribute(L"LineDisplacement",
                    Utilities::ToWString<int>((*i)->fLineDisplacement).c_str());

                pointsEncountered->appendChild(point);
            }

            root->appendChild(pointsEncountered);
        }

        {
            xercesc::DOMElement* const callCounters = document->createElement(L"CallCounters");

            for (std::map<std::pair<const PointInProgram*, const PointInProgram*>, int>::const_iterator i = results->fHits.begin();
                i != results->fHits.end();
                ++i) {
                    const PointInProgram* const caller = i->first.first;
                    const PointInProgram* const callee = i->first.second;
                    const int callerId = static_cast<int>(
                            std::distance(encounteredPoints.begin(), encounteredPoints.find(caller)));
                    const int calleeId = (callee == NULL) ? -1 : static_cast<int>(
                            std::distance(encounteredPoints.begin(), encounteredPoints.find(callee)));

                    xercesc::DOMElement* const counter = document->createElement(L"Counter");

                    counter->setAttribute(L"CallerId",
                        Utilities::ToWString<int>(callerId).c_str());
                    counter->setAttribute(L"CalleeId",
                        Utilities::ToWString<int>(calleeId).c_str());
                    counter->setAttribute(L"Count",
                        Utilities::ToWString<int>(i->second).c_str());

                    callCounters->appendChild(counter);
            }

            root->appendChild(callCounters);
        }

        {
            xercesc::DOMElement* const files = document->createElement(L"Files");

            for (std::set<std::wstring>::const_iterator i = sourceFiles.begin();
                i != sourceFiles.end();
                ++i) {

                const std::wstring filename = *i;
                xercesc::DOMElement* const file = document->createElement(L"File");
                file->setAttribute(L"Name", filename.c_str());

                std::wifstream fileStream(filename.c_str());
                if (fileStream.is_open()) {
                    for (int lineNumber = 1; fileStream.eof() == false; lineNumber++) {
                        std::wstring lineContents;
                        std::getline(fileStream, lineContents);

                        xercesc::DOMElement* const line = document->createElement(L"Line");
                        line->setAttribute(L"Number", Utilities::ToWString<int>(lineNumber).c_str());

                        line->appendChild(document->createCDATASection(lineContents.c_str()));
                        file->appendChild(line);
                    }
                }
                files->appendChild(file);
            }
            root->appendChild(files);
        }

        std::wostringstream outputFilename;
        outputFilename << arguments->fOutputDirectory << L"\\" << title << L".xml";

        xercesc::DOMLSSerializer* const domSerializer = domImplementation->createLSSerializer();
        domSerializer->getDomConfig()->setParameter(xercesc::XMLUni::fgDOMWRTFormatPrettyPrint, true);
        xercesc::XMLFormatTarget* const target = new xercesc::LocalFileFormatTarget(outputFilename.str().c_str());
        xercesc::DOMLSOutput* const output = domImplementation->createLSOutput();
        output->setByteStream(target);
        domSerializer->write(document, output);
    }

    namespace {
        template<typename T1, typename T2, typename T3> class triple
        {
        public:
            typedef T1 Type1;
            typedef T2 Type2;
            typedef T3 Type3;

            Type1 first;
            Type2 second;
            Type3 third;

            triple() :
                first(Type1()),
                second(Type2()),
                third(Type3())
            {
            }
        };
    }

    void WriteDotReport(
        const CommandLineArguments* const arguments,
        const std::wstring& title,
        const ResultsForSingleThread* const results)
    {
        std::wostringstream outputFilename;
        outputFilename << arguments->fOutputDirectory << L"\\" << title << L".dot";
        std::wofstream dotStream(outputFilename.str().c_str());

        dotStream << L"strict digraph Awesome {\n";

        std::map<std::wstring, triple<int, std::map<std::wstring, int>, int> > tally;

        // Ensure each symbol is present in the tally map.
        const std::set<const PointInProgram*> encounteredPoints = results->EncounteredPoints();
        for (std::set<const PointInProgram*>::const_iterator i = encounteredPoints.begin();
            i != encounteredPoints.end();
            ++i) {
            tally[(*i)->fSymbolName];
        }

        // Accumulate symbol to symbol call counters
        for (std::map<std::pair<const PointInProgram*, const PointInProgram*>, int>::const_iterator i = results->fHits.begin();
            i != results->fHits.end();
            ++i) {
            const PointInProgram* const caller = i->first.first;
            const PointInProgram* const callee = i->first.second;

            if (callee != NULL) {
                std::map<std::wstring, int>& tmp = tally.find(caller->fSymbolName)->second.second;
                tmp[callee->fSymbolName] += i->second;
            } else {
                tally.find(caller->fSymbolName)->second.third += i->second;
            }
        }

        // Give each symbol a unique id number and output nodes.
        int counter = 0;
        for (std::map<std::wstring, triple<int, std::map<std::wstring, int>, int > >::iterator i = tally.begin();
            i != tally.end();
            ++i) {
            i->second.first = (counter++);

            dotStream << L"    node_" << (i->second.first) << L" [label=\"" << (i->first) << L"\\n" << i->second.third << "\"];\n";
        }

        dotStream << L"\n";

        // Output edges.
        for (std::map<std::wstring, triple<int, std::map<std::wstring, int>, int  > >::const_iterator i = tally.begin();
            i != tally.end();
            ++i) {
            for (std::map<std::wstring, int>::const_iterator j = i->second.second.begin();
                j != i->second.second.end();
                ++j) {
                const int callerId = tally.find(i->first)->second.first;
                const int calleeId = tally.find(j->first)->second.first;
                const int callCount = tally.find(i->first)->second.second.find(j->first)->second;
                dotStream << L"    node_" << callerId << L" -> node_" << calleeId << L" [label=\"" << callCount << L"\", weight=" << callCount << L"];\n";
            }
        }

        dotStream << L"}\n";

        dotStream.close();
    }

    void WriteSingleReport(
        const CommandLineArguments* const arguments,
        const std::wstring& title,
        const int numberOfSamples,
        const double wallClockTimeInSeconds,
        const ResultsForSingleThread* const results)
    {
        WriteXmlReport(arguments, title, numberOfSamples, wallClockTimeInSeconds, results);
        WriteDotReport(arguments, title, results);
    }

    void WriteAllReports(
        const CommandLineArguments* const arguments,
        const ResultsForAllThreads* const results)
    {
        ResultsForSingleThread accumulatedResultsForAllThreads;
        const double wallClockTimeInSeconds = results->fEndTimeInSeconds - results->fBeginTimeInSeconds;

        for (std::map<std::wstring, ResultsForSingleThread>::const_iterator i = results->fThreadResults.begin();
            i != results->fThreadResults.end();
            ++i) {
            const std::wstring& threadId = i->first;
            const ResultsForSingleThread* const threadResults = &(i->second);
            accumulatedResultsForAllThreads.Accumulate(threadResults);
            WriteSingleReport(
                arguments,
                threadId,
                results->fNumberOfSamples,
                wallClockTimeInSeconds,
                threadResults);
        }

        WriteSingleReport(
            arguments,
            L"AllThreads",
            results->fNumberOfSamples,
            wallClockTimeInSeconds,
            &accumulatedResultsForAllThreads);
    }
}