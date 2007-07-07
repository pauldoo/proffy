/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
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
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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

        out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
        out.print("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        out.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        out.print("<head>\n");
        out.print("  <title>" + title + "</title>\n");
        out.print("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        out.print("  <style type=\"text/css\" media=\"screen\">\n");
        out.print("    body { font-family: Verdana, sans-serif; white-space: nowrap; }\n");
        out.print("    .outer { text-align:center; page-break-after: always; }\n");
        out.print("    .outer.last { page-break-after: auto; }\n");
        out.print("    h1 { margin-bottom:10px; font-size:18pt; }\n");
        out.print("    h2 { font-size:16pt; }\n");
        out.print("    h3 { font-size:14pt; }\n");
        out.print("    h2, h3 { margin-top:0; margin-bottom:5px; }\n");
        out.print("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:8pt; margin-top:20px; }\n");
        out.print("    th { border-bottom:3px solid #000000; text-align: left; }\n");
        out.print("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }\n");
        out.print("  </style>\n");
        out.print("  <style type=\"text/css\" media=\"print\">\n");
        out.print("    body { font-family: Verdana, sans-serif; white-space: nowrap; }\n");
        out.print("    .outer { text-align:center; page-break-after: always; }\n");
        out.print("    .outer.last { page-break-after: auto; }\n");
        out.print("    h1 { margin-bottom:10px; font-size:18pt; }\n");
        out.print("    h2 { font-size:16pt; }\n");
        out.print("    h3 { font-size:14pt; }\n");
        out.print("    h2, h3 { margin-top:0; margin-bottom:5px; }\n");
        out.print("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:6pt; margin-top:20px; }\n");
        out.print("    th { border-bottom:3px solid #000000; text-align: left; }\n");
        out.print("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }\n");
        out.print("  </style>\n");
        out.print("</head>\n");
        out.print("<body>\n");
        if (out.checkError()) {
            throw new IOException();
        }

        return out;
    }

    public static void writeHtmlFooter(PrintStream out) throws IOException
    {
        out.print("</body>\n");
        out.print("</html>\n");
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

    public static BirdResult calculateVelocity(Organization club, Race race, Clock clock, Time time)
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
}
