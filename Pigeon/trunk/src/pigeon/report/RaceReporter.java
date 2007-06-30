/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
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
                out.print("<div class=\"outer\">\n");
            } else {
                out.print("<div class=\"outer last\">\n");
            }

            out.print("<h1>" + club.getName() + "</h1>\n");
            if (section != null) {
                out.print("<h2>Section: " + section + "</h2>\n");
            }
            out.print("<h2>Race from " + race.getRacepoint().toString() + "</h2>\n");
            out.print("<h3>Liberated at " + raceTime + " on " + raceDate + " in a " + race.getWindDirection() + " wind</h3>\n");
            int memberCount = 0;
            int birdCount = 0;
            SortedSet<BirdResult> results = new TreeSet<BirdResult>();

            for (Clock clock: race.getClocks()) {
                if (section != null && !clock.getMember().getSection().equals(section)) {
                    continue;
                }
                memberCount ++;
                for (Time time: clock.getTimes()) {
                    birdCount ++;
                    BirdResult row = Utilities.calculateVelocity(club, race, clock, time);

                    row.html.append("<td>" + clock.getMember().toString() + "</td>");
                    if (listClubNames) {
                        row.html.append("<td>" + clock.getMember().getClub() + "</td>");
                    }
                    if (race.getDaysCovered() > 1) {
                        int days = (int)((row.correctedClockTime.getTime() - race.liberationDayOffset().getTime()) / Constants.MILLISECONDS_PER_DAY);
                        row.html.append("<td>" + (days + 1) + "</td>");
                    }
                    row.html.append("<td>" + pigeon.view.Utilities.TIME_FORMAT_WITH_LOCALE.format(row.correctedClockTime) + "</td>");
                    row.html.append("<td>" + row.distance.getMiles() + "</td>");
                    row.html.append("<td>" + row.distance.getYardsRemainder() + "</td>");
                    row.html.append("<td>" + time.getRingNumber() + "</td>");
                    row.html.append("<td>" + time.getColor() + "</td>");
                    row.html.append("<td>H</td>");
                    row.html.append("<td></td>");
                    row.html.append("<td></td>");
                    row.html.append("<td>" + Utilities.stringPrintf("%.3f", row.velocityInMetresPerSecond * Constants.METRES_PER_SECOND_TO_YARDS_PER_MINUTE) + "</td>");
                    results.add(row);
                }
            }
            out.print("<h3>" + memberCount + " members sent in a total of " + birdCount + " birds</h3>\n");
            out.print("<table>\n");
            out.print("<tr><th>Pos.</th><th>Member</th>");
            if (listClubNames) {
                out.print("<th>Club</th>");
            }
            if (race.getDaysCovered() > 1) {
                out.print("<th>Day</th>");
            }
            out.print("<th>Time</th><th>Miles</th><th>Yards</th><th>Ring No.</th><th>Colour</th><th>Sex</th><th>Pools</th><th>Prize</th><th>Velocity</th></tr>\n");
            int pos = 0;
            for (BirdResult row: results) {
                pos ++;
                out.print("<tr><td>" + pos + "</td>");
                out.print(row.html.toString());
                out.print("</tr>\n");
            }
            out.print("</table>\n");
            out.print("</div>\n");
        }

        Utilities.writeHtmlFooter(out);
    }
}
