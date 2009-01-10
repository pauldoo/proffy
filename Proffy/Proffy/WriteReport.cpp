/*
    Copyright (C) 2009  Paul Richards.

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

#include "WriteReport.h"

#include "Assert.h"
#include "Results.h"
#include "Utilities.h"
#include "XercesInitialize.h"

namespace Proffy {
    void WriteReport(
        const Results* const results,
        const std::wstring& outputFilename)
    {
        XercesInitialize xerces;
        xercesc::DOMImplementation* const domImplementation =
            xercesc::DOMImplementationRegistry::getDOMImplementation(NULL);
        ASSERT(domImplementation != NULL);

        xercesc::DOMDocument* const document = domImplementation->createDocument();
        document->appendChild(document->createProcessingInstruction(L"xml-stylesheet", L"type=\"text/xsl\" href=\"Xhtml.xsl\""));
        xercesc::DOMElement* const root = document->createElement(L"ProffyResults");
        document->appendChild(root);

        for (std::map<std::wstring, std::map<int, std::pair<int, int> > >::const_iterator i = results->AllHits().begin();
            i != results->AllHits().end();
            ++i) {

            const std::wstring filename = i->first;
            xercesc::DOMElement* const file = document->createElement(L"File");
            file->setAttribute(L"Name", filename.c_str());

            std::wifstream fileStream(filename.c_str());
            if (fileStream.is_open()) {
                for (int lineNumber = 1; fileStream.eof() == false; lineNumber++) {
                    std::wstring lineContents;
                    std::getline(fileStream, lineContents);
                    
                    xercesc::DOMElement* const line = document->createElement(L"Line");
                    line->setAttribute(L"Number", Utilities::ToWString<int>(lineNumber).c_str());

                    std::map<int, std::pair<int, int> >::const_iterator hits = i->second.find(lineNumber);
                    if (hits != i->second.end()) {
                        line->setAttribute(L"TerminalHits", Utilities::ToWString<int>(hits->second.first).c_str());
                        line->setAttribute(L"NonTerminalHits", Utilities::ToWString<int>(hits->second.second).c_str());
                    }
                    line->appendChild(document->createCDATASection(lineContents.c_str()));
                    
                    file->appendChild(line);
                }
            }

            root->appendChild(file);
        }

        xercesc::DOMLSSerializer* const domSerializer = domImplementation->createLSSerializer();
        domSerializer->getDomConfig()->setParameter(xercesc::XMLUni::fgDOMWRTFormatPrettyPrint, true);
        xercesc::XMLFormatTarget* const target = new xercesc::LocalFileFormatTarget(outputFilename.c_str());
        xercesc::DOMLSOutput* const output = domImplementation->createLSOutput();
        output->setByteStream(target);
        domSerializer->write(document, output);
    }
}