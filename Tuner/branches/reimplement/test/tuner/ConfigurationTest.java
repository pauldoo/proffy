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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import org.junit.Test;
import static org.junit.Assert.*;

public final class ConfigurationTest {

    private static InputStream dummyDocument()
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<TunerInstruments>");
            writer.println("<MiddleC>700.5</MiddleC>");
            writer.println("<Instrument name=\"Shoe\">");
            writer.println("<String name=\"foo\"><Semitone>3.4</Semitone></String>");
            writer.println("<String name=\"bar\"><Semitone>7.9</Semitone></String>");
            writer.println("</Instrument>");
            writer.println("<Instrument name=\"Glove\">");
            writer.println("<String name=\"flim\"><Semitone>-2</Semitone></String>");
            writer.println("<String name=\"du\"><Semitone>-7</Semitone></String>");
            writer.println("</Instrument>");
            writer.println("</TunerInstruments>");
            writer.close();
            if (writer.checkError()) {
                throw new RuntimeException("Preparing dummy document failed.");
            }
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
        
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
    
    @Test
    public void successCases() throws IOException
    {
        Configuration config = Configuration.loadFromStream(dummyDocument());
        assertEquals(config.frequencyInHzOfMiddleC(), 700.5);
        
        assertEquals(config.instrumentNames().size(), 2);
        
        assertEquals(config.instrumentNames().get(0), "Shoe");
        assertEquals(config.stringNames("Shoe").size(), 2);
        assertEquals(config.stringNames("Shoe").get(0), "foo");
        assertEquals(config.stringSemitone("Shoe", "foo"), 3.4);
        assertEquals(config.stringNames("Shoe").get(1), "bar");
        assertEquals(config.stringSemitone("Shoe", "bar"), 7.9);

        assertEquals(config.instrumentNames().get(1), "Glove");
        assertEquals(config.stringNames("Glove").size(), 2);
        assertEquals(config.stringNames("Glove").size(), 2);
        assertEquals(config.stringNames("Glove").get(0), "flim");
        assertEquals(config.stringSemitone("Glove", "flim"), -2.0);
        assertEquals(config.stringNames("Glove").get(1), "du");
        assertEquals(config.stringSemitone("Glove", "du"), -7.0);
    }
}