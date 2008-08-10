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

package pigeon.report2;

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
import pigeon.report.Reporter;
import pigeon.report.StreamProvider;
import pigeon.view.Configuration;

final public class MembersReporter implements Reporter
{
    private final Document document;
    
    public MembersReporter(String organization, Collection<Member> members, Configuration.Mode mode)
    {
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            document.appendChild(document.createProcessingInstruction("xml-stylesheet", "type=\"text/xsl\" href=\"racepoint.xsl\""));

            final Element memberListElement = document.createElement("members");

            for (Member member: members) {
                final Element nameElement = document.createElement("name");
                nameElement.setTextContent(member.getName());

                final Element memberElement = document.createElement("member");
                memberElement.appendChild(nameElement);

                memberListElement.appendChild(memberElement);
            }

            document.appendChild(memberListElement);        
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
                    new StreamResult(streamProvider.createNewStream("members.xml")));

            pigeon.report.Utilities.copyStream(
                    new BufferedInputStream(ClassLoader.getSystemResourceAsStream("resources/racepoint.xsl")),
                    streamProvider.createNewStream("racepoint.xsl"));
            
        } catch (TransformerConfigurationException e) {
            throw new RuntimeException(e);
        } catch (TransformerException e) {
            throw new IOException(e);
        }
    }
}
