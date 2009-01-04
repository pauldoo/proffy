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

#include "Assert.h"
#include "ComInitialize.h"
#include "ConsoleColor.h"
#include "Exception.h"
#include "DebugEventCallbacks.h"
#include "DebugOutputCallbacks.h"
#include "Results.h"
#include "Utilities.h"
#include "XercesInitialize.h"

namespace {
    const std::string lines = std::string(50, '-') + "\n";
}

namespace Proffy {
    void FlushCallbacks(IDebugClient* debugClient)
    {
        //std::cout << __FUNCTION__ << " Begin.\n";
        ASSERT(debugClient->FlushCallbacks() == S_OK);
        //std::cout << __FUNCTION__ << " End.\n";
    }

    int main(void)
    {
        try {
            // Start something which we will attempt to profile.
            STARTUPINFO startupInfo = {0};
            PROCESS_INFORMATION processInformation = {0};
            BOOL processStarted = ::CreateProcess(
                L"../Debug/SampleTarget.exe",
                NULL,
                NULL,
                NULL,
                FALSE,
                CREATE_NEW_CONSOLE,
                NULL,
                NULL,
                &startupInfo,
                &processInformation);
            if (processStarted != TRUE) {
                std::ostringstream message;
                message << ::GetLastError();
                throw Exception(message.str());
            }

            // Initialize COM, no idea if this is necessary.
            const ComInitialize com;

            HRESULT result = S_OK;

            // Get the IDebugClient to start.
            IDebugClient* debugClient = NULL;
            result = ::DebugCreate(
                __uuidof(IDebugClient),
                reinterpret_cast<void**>(&debugClient));
            ASSERT(result == S_OK);

            // Set our output callbacks.
            DebugOutputCallbacks debugOutputCallbacks;
            result = debugClient->SetOutputCallbacks(&debugOutputCallbacks);
            ASSERT(result == S_OK);

            // Set our event callbacks.
            DebugEventCallbacks debugEventCallbacks;
            result = debugClient->SetEventCallbacks(&debugEventCallbacks);
            ASSERT(result == S_OK);

            // Get the IDebugControl.
            IDebugControl* debugControl = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugControl),
                reinterpret_cast<void**>(&debugControl));
            ASSERT(result == S_OK);

            // Get the IDebugSystemObjects.
            IDebugSystemObjects* debugSystemObjects = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugSystemObjects),
                reinterpret_cast<void**>(&debugSystemObjects));
            ASSERT(result == S_OK);

            // Get the IDebugSymbols.
            IDebugSymbols3* debugSymbols = NULL;
            result = debugClient->QueryInterface(
                __uuidof(IDebugSymbols3),
                reinterpret_cast<void**>(&debugSymbols));
            ASSERT(result == S_OK);

            // Done getting and setting, so now attach.
            std::cout << lines << Utilities::TimeInSeconds() << ": Attaching..\n" << lines;
            result = debugClient->AttachProcess(
                0,
                processInformation.dwProcessId,
                0);
                //DEBUG_ATTACH_INVASIVE_NO_INITIAL_BREAK);
                //DEBUG_ATTACH_NONINVASIVE | DEBUG_ATTACH_NONINVASIVE_NO_SUSPEND);
            ASSERT(result == S_OK);
            std::cout << lines << Utilities::TimeInSeconds() << ": Done Attaching..\n" << lines;

            // Verify we have attached to what we think we have.
            ULONG debugeeTypeClass;
            ULONG debugeeTypeQualifier;
            result = debugControl->GetDebuggeeType(&debugeeTypeClass, &debugeeTypeQualifier);
            ASSERT(result == S_OK);
            ASSERT(debugeeTypeClass == DEBUG_CLASS_USER_WINDOWS);
            ASSERT(debugeeTypeQualifier == DEBUG_USER_WINDOWS_PROCESS);

            FlushCallbacks(debugClient);
            ConsoleColor c(Color_Normal);
            Results results;

            for (int i = 0; i < 100; i++) {
                //FlushCallbacks(debugClient);

                //std::cout << lines << Utilities::TimeInSeconds() << ": Waiting..\n" << lines;
                result = debugControl->WaitForEvent(
                    DEBUG_WAIT_DEFAULT,
                    10);
                //std::cout << "WaitForEvent returned: " << Utilities::HresultToString(result) << "\n";
                ASSERT(result == S_OK || result == S_FALSE);
                //std::cout << lines << Utilities::TimeInSeconds() << ": Done Waiting..\n" << lines;
                //FlushCallbacks(debugClient);

                ULONG executionStatus;
                result = debugControl->GetExecutionStatus(&executionStatus);
                //std::cout << "GetExecutionStatus returned: " << Utilities::HresultToString(result) << "\n";
                ASSERT(result == S_OK);
                //std::cout << "ExecutionStatus: " << Utilities::ExecutionStatusToString(executionStatus) << "\n";
                ASSERT(executionStatus == DEBUG_STATUS_BREAK);
                //FlushCallbacks(debugClient);

                ULONG numberThreads;
                ULONG largestProcess;
                result = debugSystemObjects->GetTotalNumberThreads(&numberThreads, &largestProcess);
                //std::cout << "GetNumberThreads returned: " << Utilities::HresultToString(result) << "\n";
                ASSERT(result == S_OK);
                //std::cout << "NumberThreads: " << numberThreads << "\n";
                //FlushCallbacks(debugClient);

                for (int i = 0; i < static_cast<int>(numberThreads); i++) {
                    //std::cout << "Thread #" << i << "\n";

                    result = debugSystemObjects->SetCurrentThreadId(i);
                    //std::cout << "SetCurrentThreadId returned: " << Utilities::HresultToString(result) << "\n";
                    ASSERT(result == S_OK);
                    //FlushCallbacks(debugClient);

                    //std::cout << "OutputStackTrace:\n";
                    //result = debugControl->OutputStackTrace(
                    //    DEBUG_OUTCTL_THIS_CLIENT,
                    //    NULL,
                    //    10,
                    //    DEBUG_STACK_FRAME_NUMBERS);
                    ////std::cout << "OutputStackTrace returned: " << Utilities::HresultToString(result) << "\n";
                    //ASSERT(result == S_OK);
                    ////FlushCallbacks(debugClient);

                    std::vector<DEBUG_STACK_FRAME> frames(100);
                    ULONG framesFilled;
                    result = debugControl->GetStackTrace(
                        NULL,
                        NULL,
                        NULL,
                        &(frames.front()),
                        frames.size(),
                        &framesFilled);
                    ASSERT(result == S_OK);
                    frames.resize(framesFilled);

                    for (int i = 0; i < static_cast<int>(frames.size()); i++) {
                        //result = debugSymbols->SetScope(NULL, &(frames.at(k)), NULL, NULL);
                        //ASSERT(result == S_OK);
                        
                        ULONG line;
                        std::vector<wchar_t> filenameAsVector(MAX_PATH * 2);
                        ULONG filenameSize;
                        ULONG64 displacement;
                        result = debugSymbols->GetLineByOffsetWide(
                            frames.at(i).InstructionOffset, 
                            &line, 
                            &(filenameAsVector.front()),
                            filenameAsVector.size(), 
                            &filenameSize, 
                            &displacement);

                        if (result == S_OK) {
                            const std::wstring filename(filenameAsVector.begin(), filenameAsVector.begin() + filenameSize - 1);
                            results.AccumulateSample(filename, line, i==0);
                        }
                    }
                }

                //result = debugControl->SetExecutionStatus(DEBUG_STATUS_GO);
                //std::cout << "SetExecutionStatus returned: " << Utilities::HresultToString(result) << "\n";
                //ASSERT(result == S_OK);
                //FlushCallbacks(debugClient);

                //std::cout << "Sleeping..\n";
                //::Sleep(5000);
                //std::cout << "Done sleeping..\n";
                //break;
            }

            {
                XercesInitialize xerces;
                xercesc::DOMImplementation* const domImplementation =
                    xercesc::DOMImplementationRegistry::getDOMImplementation(NULL);
                ASSERT(domImplementation != NULL);

                xercesc::DOMDocument* const document = domImplementation->createDocument();
                document->appendChild(document->createProcessingInstruction(L"xml-stylesheet", L"type=\"text/xsl\" href=\"Xhtml.xsl\""));
                xercesc::DOMElement* const root = document->createElement(L"ProffyResults");
                document->appendChild(root);

                for (std::map<std::wstring, std::map<int, std::pair<int, int> > >::const_iterator i = results.AllHits().begin();
                    i != results.AllHits().end();
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
                xercesc::XMLFormatTarget* const target = new xercesc::LocalFileFormatTarget(L"ProffyFoobar.xml");
                xercesc::DOMLSOutput* const output = domImplementation->createLSOutput();
                output->setByteStream(target);
                domSerializer->write(document, output);
            }

            return EXIT_SUCCESS;
        } catch (const xercesc::XMLException& ex) {
            std::wcerr << ex.getType() << ": " << ex.getMessage() << "\n";
        } catch (const std::exception& ex) {
            std::cerr << typeid(ex).name() << ": " << ex.what() << "\n";
        }
        return EXIT_FAILURE;
    }
}

int main(void) {
    std::ios::sync_with_stdio(false);
    return Proffy::main();
}
