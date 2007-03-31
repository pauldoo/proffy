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

/**
    Generates an HTML report for a single race.

    Federation and section results are generated on the same page.
*/
public final class RaceReporter implements Reporter {

    private final Organization club;
    private final Race race;
    private final boolean listClubNames;

    /** Creates a new instance of RaceReporter */
    public RaceReporter(Organization club, Race race, boolean listClubNames) {
        this.club = club;
        this.race = race;
        this.listClubNames = listClubNames;
    }

    public void write(OutputStream stream) throws IOException {
        String raceDate = pigeon.view.Utilities.DATE_FORMAT.format(race.getLiberationDate());
        String raceTime = pigeon.view.Utilities.TIME_FORMAT_WITH_LOCALE.format(race.getLiberationDate());
        PrintStream out = Utilities.writeHtmlHeader(stream, race.getRacepoint().toString() + " on " + raceDate);

        List<String> sections = Utilities.participatingSections(race);
        // Push the null section to the front to guarantee we do the whole lot.
        sections.add(0, null);

        for (String section: sections) {
            if (section != sections.get(sections.size() - 1)) {
                out.print("<div final class=\"outer\">\n");
            } else {
                out.print("<div final class=\"outer last\">\n");
            }

            out.print("<h1>" + club.getName() + "</h1>\n");
            if (section != null) {
                out.print("<h2>Section: " + section + "</h2>\n");
            }
            out.print("<h2>Race from " + race.getRacepoint().toString() + "</h2>\n");
            out.print("<h3>Liberated at " + raceTime + " on " + raceDate + " in a " + race.getWindDirection() + " wind</h3>\n");
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
                    line.append("<td>" + pigeon.view.Utilities.TIME_FORMAT_WITH_LOCALE.format(correctedClockTime) + "</td>");
                    line.append("<td>" + distance.getMiles() + "</td>");
                    line.append("<td>" + distance.getYardsRemainder() + "</td>");
                    line.append("<td>" + time.getRingNumber() + "</td>");
                    line.append("<td>Purple</td>");
                    line.append("<td>H</td>");
                    line.append("<td></td>");
                    line.append("<td></td>");
                    line.append("<td>" + Utilities.StringPrintf("%.3f", velocity * Constants.METRES_PER_SECOND_TO_YARDS_PER_MINUTE) + "</td>");
                    results.put(-velocity, line.toString());
                }
            }
            out.print("<h3>" + memberCount + " members sent in a total of " + birdCount + " birds</h3>\n");
            out.print("<table>\n");
            out.print("<tr><th>Position</th><th>Member</th>");
            if (listClubNames) {
                out.print("<th>Club</th>");
            }
            if (race.getDaysCovered() > 1) {
                out.print("<th>Day</th>");
            }
            out.print("<th>Time</th><th>Miles</th><th>Yards</th><th>Ring Number</th><th>Colour</th><th>Sex</th><th>Pools</th><th>Prize</th><th>Velocity</th></tr>\n");
            int pos = 0;
            for (String line: results.values()) {
                pos ++;
                out.print("<tr><td>" + pos + "</td>");
                out.print(line);
                out.print("</tr>\n");
            }
            out.print("</table>\n");
            out.print("</div>\n");
        }

        Utilities.writeHtmlFooter(out);
    }
}
