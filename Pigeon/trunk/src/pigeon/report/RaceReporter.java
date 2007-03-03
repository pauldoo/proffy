/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.
    
        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.
    
        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
*/

package pigeon.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import pigeon.model.Clock;
import pigeon.model.Organization;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Race;
import pigeon.model.Time;
import pigeon.view.Utilities;

/**
 * Generates an HTML report for a single race.
 * 
 * Federation and section results are generated on the same page.
 */
public class RaceReporter {

    private Organization club;
    private Race race;

    /** Creates a new instance of RaceReporter */
    public RaceReporter(Organization club, Race race) {
        this.club = club;
        this.race = race;
    }

    private String StringPrintf(String format, Object... args) {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);
            writer.printf(format, args);
            writer.flush();
            return buffer.toString();
    }

    /**
     * Returns a list of the sections that were involved in this race,
     * or an empty collection if no section information is available.
     */
    private List<String> participatingSections()
    {
        Set<String> result = new TreeSet<String>();
        for (Clock clock: race.getClocks()) {
            String section = clock.getMember().getSection();
            if (section != null) {
                result.add(section);
            }
        }
        return new LinkedList<String>(result);
    }
    
    public void write(OutputStream stream, boolean listClubNames) throws IOException {
        PrintStream out = new PrintStream(stream, false, "UTF-8");
        String raceDate = Utilities.DATE_FORMAT.format(race.getLiberationDate());
        String raceTime = Utilities.TIME_FORMAT_WITH_LOCALE.format(race.getLiberationDate());
        writeHtmlHeader(out, raceDate);
        
        List<String> sections = participatingSections();
        // Push the null section to the front to guarantee we do the whole lot.
        sections.add(0, null);
        
        for (String section: sections) {
            if (section != sections.get(sections.size() - 1)) {
                out.println("<div class=\"outer\">");
            } else {
                out.println("<div class=\"outer last\">");
            }

            out.println("<h1>" + club.getName() + "</h1>");
            if (section != null) {
                out.println("<h2>Section: " + section + "</h2>");
            }
            out.println("<h2>Race from " + race.getRacepoint().toString() + "</h2>");
            out.println("<h3>Liberated at " + raceTime + " on " + raceDate + " in a " + race.getWindDirection() + " wind</h3>");
            int memberCount = 0;
            int birdCount = 0;
            SortedMap<Double, String> results = new TreeMap<Double, String>();
            for (Clock clock: race.getClocks()) {
                if (section != null && !clock.getMember().getSection().equals(section)) {
                    continue;
                }
                memberCount ++;
                for (Time time: clock.getTimes()) {
                    birdCount ++;
                    //String debugBefore = new Date(time.getMemberTime() + race.liberationDayOffset().getTime()).toString();
                    Date correctedClockTime = clock.convertMemberTimeToMasterTime(new Date(time.getMemberTime()), race);
                    //String debugAfter = correctedClockTime.toString();
                    int nightsSpentSleeping = (int)(time.getMemberTime() / Constants.MILLISECONDS_PER_DAY);
                    long timeSpentSleeping = nightsSpentSleeping * race.getLengthOfDarknessEachNight();
                    double flyTimeInSeconds = (correctedClockTime.getTime() - race.getLiberationDate().getTime() - timeSpentSleeping) / 1000.0;
                    Distance distance = club.getDistanceEntry(clock.getMember(), race.getRacepoint()).getDistance();
                    double velocity = distance.getMetres() / flyTimeInSeconds;
                    StringBuffer line = new StringBuffer();
                    line.append("<td>" + clock.getMember().toString() + "</td>");
                    if (listClubNames) {
                        line.append("<td>" + clock.getMember().getClub() + "</td>");
                    }
                    if (race.getDaysCovered() > 1) {
                        int days = (int)((correctedClockTime.getTime() - race.liberationDayOffset().getTime()) / Constants.MILLISECONDS_PER_DAY);
                        line.append("<td>" + (days + 1) + "</td>");
                    }
                    line.append("<td>" + Utilities.TIME_FORMAT_WITH_LOCALE.format(correctedClockTime) + "</td>");
                    line.append("<td>" + distance.getMiles() + "</td>");
                    line.append("<td>" + distance.getYardsRemainder() + "</td>");
                    line.append("<td>" + time.getRingNumber() + "</td>");
                    line.append("<td>Purple</td>");
                    line.append("<td>H</td>");
                    line.append("<td></td>");
                    line.append("<td></td>");
                    line.append("<td>" + StringPrintf("%.3f", velocity * Constants.METRES_PER_SECOND_TO_YARDS_PER_MINUTE) + "</td>");
                    results.put(-velocity, line.toString());
                }
            }
            out.println("<h3>" + memberCount + " members sent in a total of " + birdCount + " birds</h3>");
            out.println("<table>");
            out.print("<tr><th>Position</th><th>Member</th>");
            if (listClubNames) {
                out.print("<th>Club</th>");
            }
            if (race.getDaysCovered() > 1) {
                out.print("<th>Day</th>");
            }
            out.println("<th>Time</th><th>Miles</th><th>Yards</th><th>Ring Number</th><th>Colour</th><th>Sex</th><th>Pools</th><th>Prize</th><th>Velocity</th></tr>");
            int pos = 0;
            for (String line: results.values()) {
                pos ++;
                out.print("<tr><td>" + pos + "</td>");
                out.print(line);
                out.println("</tr>");
            }
            out.println("</table>");
            out.println("</div>");
        }
        
        out.println("</body>");
        out.println("</html>");
        if (out.checkError()) {
            throw new IOException();
        }
    }

    private void writeHtmlHeader(PrintStream out, String raceDate)
    {
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("<title>" + race.getRacepoint().toString() + " on " + raceDate + "</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<style type=\"text/css\">");
        out.println(".outer { text-align:center; page-break-after: always; }");
        out.println(".outer.last { page-break-after: auto; }");
        out.println("h1 { margin-bottom:10px; font-size:18pt; }");
        out.println("h2 { font-size:16pt; }");
        out.println("h3 { font-size:14pt; }");
        out.println("h2, h3 { margin-top:0; margin-bottom:5px; }");
        out.println("table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:10pt; margin-top:20px; }");
        out.println("th { border-bottom:3px solid #000000; text-align: left; }");
        out.println("td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
    }
}
