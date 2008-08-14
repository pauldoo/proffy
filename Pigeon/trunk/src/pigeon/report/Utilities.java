/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Member;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Time;

/**
    Shared HTML bits.
*/
public final class Utilities
{
    // Non-Creatable
    private Utilities()
    {
    }

    public static PrintStream writeHtmlHeader(OutputStream stream, String title) throws IOException
    {
        PrintStream out = new PrintStream(stream, false, "UTF-8");

        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("  <title>" + title + "</title>");
        out.println("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("  <style type=\"text/css\" media=\"screen\">");
        out.println("    body { font-family: Verdana, sans-serif; white-space: nowrap; }");
        out.println("    .outer { text-align:center; page-break-after: always; }");
        out.println("    .outer.last { page-break-after: auto; }");
        out.println("    h1 { margin-bottom:10px; font-size:18pt; }");
        out.println("    h2 { font-size:16pt; }");
        out.println("    h3 { font-size:14pt; }");
        out.println("    h2, h3 { margin-top:0; margin-bottom:5px; }");
        out.println("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:8pt; margin-top:20px; }");
        out.println("    th { border-bottom:3px solid #000000; text-align: left; }");
        out.println("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; text-align: left; }");
        out.println("    .numeric { text-align: right; }");
        out.println("  </style>");
        out.println("  <style type=\"text/css\" media=\"print\">");
        out.println("    body { font-family: Verdana, sans-serif; white-space: nowrap; }");
        out.println("    .outer { text-align:center; page-break-after: always; }");
        out.println("    .outer.last { page-break-after: auto; }");
        out.println("    h1 { margin-bottom:10px; font-size:18pt; }");
        out.println("    h2 { font-size:16pt; }");
        out.println("    h3 { font-size:14pt; }");
        out.println("    h2, h3 { margin-top:0; margin-bottom:5px; }");
        out.println("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:6pt; margin-top:20px; }");
        out.println("    th { border-bottom:3px solid #000000; text-align: left; }");
        out.println("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; text-align: left; }");
        out.println("    .numeric { text-align: right; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        if (out.checkError()) {
            throw new IOException();
        }

        return out;
    }

    public static void writeHtmlFooter(PrintStream out) throws IOException
    {
        out.println("</body>");
        out.println("</html>");
        out.flush();
        if (out.checkError()) {
            throw new IOException();
        }
    }

    /**
        Returns a list of all the different sections,
        or an empty collection if no section information is available.
    */
    public static List<String> participatingSections(Organization club)
    {
        Set<String> result = new TreeSet<String>();
        for (Member member: club.getMembers()) {
            String section = member.getSection();
            if (section != null) {
                result.add(section);
            }
        }
        return new ArrayList<String>(result);
    }

    public static String stringPrintf(String format, Object... args) {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);
            writer.printf(format, args);
            writer.flush();
            return buffer.toString();
    }

    static BirdResult calculateVelocity(Organization club, Race race, Clock clock, Time time)
    {
        Date correctedClockTime = clock.convertMemberTimeToMasterTime(new Date(time.getMemberTime()), race);
        int nightsSpentSleeping = (int)(time.getMemberTime() / Constants.MILLISECONDS_PER_DAY);
        long timeSpentSleeping = nightsSpentSleeping * race.getLengthOfDarknessEachNight();
        double flyTimeInSeconds = (correctedClockTime.getTime() - race.getLiberationDate().getTime() - timeSpentSleeping) / 1000.0;
        Distance distance = club.getDistanceEntry(clock.getMember(), race.getRacepoint()).getDistance();
        double velocityInMetresPerSecond = distance.getMetres() / flyTimeInSeconds;

        return new BirdResult(velocityInMetresPerSecond, time, correctedClockTime, distance);
    }

    /**
        Takes a multi-line string and makes it ready for HTML output by
        inserting the required "br" tags.
    */
    public static String insertBrTags(final String lines)
    {
        try {
            BufferedReader reader = new BufferedReader(new StringReader(lines));
            StringBuffer result = new StringBuffer();

            boolean firstLine = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (!firstLine) {
                    result.append("<br/>");
                }
                result.append(line);
                firstLine = false;
            }
            return result.toString();
        } catch (IOException e) {
            throw new IllegalArgumentException("Not expecting any IOExceptions");
        }
    }
    
    private static void copyFile(File source, File destination) throws FileNotFoundException, IOException
    {
        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(destination).getChannel();
            final long bytesToCopy = inputChannel.size();
            final long bytesCopied = inputChannel.transferTo(0, bytesToCopy, outputChannel);
            if (bytesToCopy != bytesCopied) {
                throw new IOException("FileChannel.transferTo() failed");
            }
        } finally {
            if (inputChannel != null) {
                inputChannel.close();
            }
            if (outputChannel != null) {
                outputChannel.close(); 
            }
        }
    }
    
    public static void copyStream(InputStream in, OutputStream out) throws IOException
    {
        final byte[] buffer = new byte[1024];
        while (true) {
            int bytesRead = in.read(buffer);
            if (bytesRead != -1) {
                out.write(buffer, 0, bytesRead);
            } else {
                break;
            }
        }
    }
    
    public static File createTemporaryDirectory(final String prefix) throws IOException
    {
        File result = File.createTempFile(prefix, "");
        if (result.delete() == false) {
            throw new IOException("Failed to delete temporary file.");
        }
        if (result.mkdir() == false) {
            throw new IOException("Failed to create temporary directory.");
        }
        return result;
    }
    
    static void appendLineElements(Element parentElement, String textWithMultipleLines, Document document)
    {
        BufferedReader reader = new BufferedReader(new StringReader(textWithMultipleLines));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Element lineElement = document.createElement("Line");
                lineElement.setTextContent(line);
                parentElement.appendChild(lineElement);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
