/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;
import pigeon.model.Clock;
import pigeon.model.Club;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Race;
import pigeon.model.Time;
import pigeon.view.Utilities;

/**
 *
 * @author pauldoo
 */
public class RaceReporter {
    
    private Club club;
    private Race race;
    
    /** Creates a new instance of RaceReporter */
    public RaceReporter(Club club, Race race) {
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
        
    public void write(OutputStream stream) throws IOException {
        PrintStream out = new PrintStream(stream, false, "UTF-8");
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        String raceDate = Utilities.DATE_FORMAT.format(race.getLiberationDate());
        String raceTime = Utilities.TIME_FORMAT.format(race.getLiberationDate());
        out.println("<title>" + race.getRacepoint().toString() + " on " + raceDate + "</title>");
        out.println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("<style>");
        out.println("#outer { text-align:center; }");
        out.println("h1 { margin-bottom:10px; font-size:18pt; }");
        out.println("h2 { font-size:16pt; }");
        out.println("h3 { font-size:14pt; }");
        out.println("h2, h3 { margin-top:0; margin-bottom:5px; }");
        out.println("table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:10pt; margin-top:20px; }");
        out.println("th { border-bottom:3px solid #000000; text-align: left; }");
        out.println("td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body><div id=\"outer\">");
        out.println("<h1>" + club.getName() + "</h1>");
        out.println("<h2>Race from " + race.getRacepoint().toString() + "</h2>");
        out.println("<h3>Liberated at " + raceTime + " on " + raceDate + " in a " + race.getWindDirection() + " wind</h3>");
        int memberCount = 0;
        int birdCount = 0;
        SortedMap<Double, String> results = new TreeMap<Double, String>();
        for (Clock clock: race.getClocks()) {
            memberCount ++;
            for (Time time: clock.getTimes()) {
                birdCount ++;
                Date correctedClockTime = clock.convertMemberTimeToMasterTime(new Date(time.getMemberTime()), race);
                double flyTimeInSeconds = (correctedClockTime.getTime() - race.getLiberationDate().getTime()) / 1000.0;
                Distance distance = club.getDistanceEntry(clock.getMember(), race.getRacepoint()).getDistance();
                double velocity = distance.getMetres() / flyTimeInSeconds;
                StringBuffer line = new StringBuffer();
                line.append("<td>" + clock.getMember().toString() + "</td>");
                if (race.getDaysCovered() > 1) {
                    int days = (int)((correctedClockTime.getTime() - race.liberationDayOffset().getTime()) / Constants.MILLISECONDS_PER_DAY);
                    line.append("<td>" + (days + 1) + "</td>");
                }
                line.append("<td>" + Utilities.TIME_FORMAT.format(correctedClockTime) + "</td>");
                line.append("<td>" + distance.getMiles() + "</td>");
                line.append("<td>"+ distance.getYardsRemainder() + "</td>");
                line.append("<td>" + time.getRingNumber() + "</td>");
                line.append("<td>Purple</td>");
                line.append("<td>H</td>");
                line.append("<td></td>");
                line.append("<td></td>");
                line.append("<td>" + StringPrintf("%.3f", velocity * Constants.METRES_PER_SECOND_TO_YARDS_PER_MINUTE) + "</td>");
                line.append("<tr>");
                results.put(-velocity, line.toString());
            }
        }
        out.println("<h3>" + memberCount + " members sent in a total of " + birdCount + " birds</h3>");
        out.println("<table>");
        out.print("<tr><th>Position</th><th>Member</th>");
        if (race.getDaysCovered() > 1) {
            out.print("<th>Day</th>");
        }
        out.println("<th>Time</th><th>Miles</th><th>Yards</th><th>Ring Number</th><th>Colour</th><th>Sex</th><th>Pools</th><th>Prize</th><th>Velocity</th></tr>");
        int pos = 0;
        for (String line: results.values()) {
            pos ++;
            out.print("<tr><td>" + pos + "</td>");
            out.println(line);
        }
        out.println("</table>");
        out.println("</div></body>");
        out.println("</html>");
        if (out.checkError()) {
            throw new IOException();
        }
    }
}
