/*
    Copyright (C) 2008  Paul Richards.

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

package pigeon.report;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Collection;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pigeon.model.Member;
import pigeon.view.Configuration;

final public class MembersReporter implements Reporter
{
    private final Document document;
    
    public MembersReporter(String organization, Collection<Member> members, Configuration.Mode mode)
    {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.appendChild(document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"racepoint.xsl\""));

            final Element rootElement = document.createElement("MembersReport");

            final Element organisationElement = document.createElement("Organisation");
            organisationElement.setTextContent(organization);
            rootElement.appendChild(organisationElement);
            
            final Element memberListElement = document.createElement("MemberList");
            for (Member member: members) {
                final Element memberElement = document.createElement("Member");

                final Element nameElement = document.createElement("Name");
                nameElement.setTextContent(member.getName());
                memberElement.appendChild(nameElement);
                final Element addressElement = document.createElement("Address");
                Utilities.appendLineElements(addressElement, member.getAddress(), document);
                memberElement.appendChild(addressElement);
                final Element telephoneElement = document.createElement("Telephone");
                telephoneElement.setTextContent(member.getTelephone());
                memberElement.appendChild(telephoneElement);
                final Element shuNumberElement = document.createElement("ShuNumber");
                shuNumberElement.setTextContent(member.getSHUNumber());
                memberElement.appendChild(shuNumberElement);
                
                switch (mode) {
                    case CLUB:
                        break;
                    case FEDERATION:
                        final Element clubElement = document.createElement("Club");
                        clubElement.setTextContent(member.getClub());
                        memberElement.appendChild(clubElement);
                        final Element sectionElement = document.createElement("Section");
                        sectionElement.setTextContent(member.getSection());
                        memberElement.appendChild(sectionElement);
                }
                
                memberListElement.appendChild(memberElement);
            }
            rootElement.appendChild(memberListElement);
            document.appendChild(rootElement);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(StreamProvider streamProvider) throws IOException
    {
        try {
            final Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            
            transformer.transform(
                    new DOMSource(document),
                    new StreamResult(streamProvider.createNewStream("members.xml", true)));

            pigeon.report.Utilities.copyStream(
                    new BufferedInputStream(ClassLoader.getSystemResourceAsStream("resources/racepoint.xsl")),
                    streamProvider.createNewStream("racepoint.xsl", false));
            
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }
}
