/*
    Copyright (C) 2007  Paul Richards.

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

package tuner;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
    Parses the XML configuration file, and provides accessors to get the list
    of instruments and their string frequencies.
*/
public final class Configuration
{
    private final Document document;
    
    public Configuration(Document document)
    {
        this.document = document;
    }
    
    public static Configuration loadFromStream(InputStream stream) throws IOException
    {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
            return new Configuration(document);
        } catch (ParserConfigurationException ex) {
            throw new IOException(ex.toString());
        } catch (SAXException ex) {
            throw new IOException(ex.toString());
        }
    }

    private Node findTunerInstrumentsNode()
    {
        for (int i = 0; i < document.getChildNodes().getLength(); i++) {
            Node child = document.getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("TunerInstruments")) {
                return child;
            }
        }
        throw new RuntimeException("Could not find 'TunerInstruments' node.");
    }
    
    public double frequencyInHzOfMiddleC()
    {
        for (int i = 0; i < findTunerInstrumentsNode().getChildNodes().getLength(); i++) {
            Node child = findTunerInstrumentsNode().getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("MiddleC")) {
                return Double.parseDouble(child.getTextContent());
            }
        }
        throw new RuntimeException("Couldn't find MiddleC from configuration.");
    }
    
    /**
        Returns the list of instruments configured.
    */
    public List<String> instrumentNames()
    {
        List<String> result = new ArrayList<String>();
        for (int i = 0; i < findTunerInstrumentsNode().getChildNodes().getLength(); i++) {
            Node child = findTunerInstrumentsNode().getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("Instrument")) {
                String instrumentName = child.getAttributes().getNamedItem("name").getTextContent();
                result.add(instrumentName);
            }
        }
        return result;
    }
    
    private Node findInstrumentNode(String instrumentName)
    {
        for (int i = 0; i < findTunerInstrumentsNode().getChildNodes().getLength(); i++) {
            Node child = findTunerInstrumentsNode().getChildNodes().item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE && child.getNodeName().equals("Instrument")) {
                String name = child.getAttributes().getNamedItem("name").getTextContent();
                if (name.equals(instrumentName)) {
                    return child;
                }
            }
        }
        throw new RuntimeException("No instrument called '" + instrumentName + "'.");
    }
    
    /**
        Returns the names of the strings on a given instrument.
    */
    public List<String> stringNames(String instrumentName)
    {
        Node instrumentNode = findInstrumentNode(instrumentName);
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < instrumentNode.getChildNodes().getLength(); i++) {
            Node stringNode = instrumentNode.getChildNodes().item(i);
            if (stringNode.getNodeType() == Node.ELEMENT_NODE && stringNode.getNodeName().equals("String")) {
                String name = stringNode.getAttributes().getNamedItem("name").getTextContent();
                result.add(name);
            }
        }
        return result;
    }
    
    private Node findStringNode(String instrumentName, String stringName)
    {
        Node instrumentNode = findInstrumentNode(instrumentName);
        List<String> result = new ArrayList<String>();

        for (int i = 0; i < instrumentNode.getChildNodes().getLength(); i++) {
            Node stringNode = instrumentNode.getChildNodes().item(i);
            if (stringNode.getNodeType() == Node.ELEMENT_NODE && stringNode.getNodeName().equals("String")) {
                String name = stringNode.getAttributes().getNamedItem("name").getTextContent();
                if (name.equals(stringName)) {
                    return stringNode;
                }
            }
        }
        throw new RuntimeException("No string called '" + stringName + "' on instrument '" + instrumentName + "'.");
    }
    
    /**
        Returns the semitone (relative to middle C) of a string on a given instrument.
    */
    public double stringSemitone(String instrumentName, String stringName)
    {
        Node stringNode = findStringNode(instrumentName, stringName);
        
        for (int i = 0; i < stringNode.getChildNodes().getLength(); i++) {
            Node semitoneNode = stringNode.getChildNodes().item(i);
            if (semitoneNode.getNodeType() == Node.ELEMENT_NODE && semitoneNode.getNodeName().equals("Semitone")) {
                return Double.parseDouble(semitoneNode.getTextContent());
            }
        }
        throw new RuntimeException("Missing Semitone node on '" + instrumentName + "' / '" + stringName + "'.");
    }
}
