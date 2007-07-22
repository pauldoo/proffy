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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import pigeon.competitions.Competition;
import pigeon.model.Clock;
import pigeon.model.Organization;
import pigeon.model.Constants;
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
    private final List<Competition> competitions;

    /** Creates a new instance of RaceReporter */
    public RaceReporter(Organization club, Race race, boolean listClubNames, List<Competition> competitions) {
        this.club = club;
        this.race = race;
        this.listClubNames = listClubNames;
        this.competitions = competitions;
    }

    public void write(OutputStream stream) throws IOException {
        String raceDate = pigeon.view.Utilities.DATE_FORMAT.format(race.getLiberationDate());
        String raceTime = pigeon.view.Utilities.TIME_FORMAT_WITH_LOCALE.format(race.getLiberationDate());
        PrintStream out = Utilities.writeHtmlHeader(stream, race.getRacepoint().toString() + " on " + raceDate);

        List<String> sections = Utilities.participatingSections(club);
        // Push the null section to the front to guarantee we do the whole lot.
        sections.add(0, null);

        for (String section: sections) {
            final String sectionNotNull = (section == null) ? "Open" : section;
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
            SortedSet<BirdResult> results = new TreeSet<BirdResult>();

            for (Clock clock: race.getClocks()) {
                if (section != null && !clock.getMember().getSection().equals(section)) {
                    continue;
                }
                for (Time time: clock.getTimes()) {
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
                    row.html.append("<td>" + time.getSex().toString() + "</td>");
                    row.html.append("<td>");
                    for (String pool: (section == null) ? time.getOpenCompetitionsEntered() : time.getSectionCompetitionsEntered()) {
                        row.html.append(pool);
                    }
                    row.html.append("</td>");
                    results.add(row);
                }
            }
            int memberCount;
            int birdCount;
            if (section != null) {
                memberCount = race.getMembersEntered().get(section);
                birdCount = race.getBirdsEntered().get(section);
            } else {
                memberCount = race.getTotalNumberOfMembersEntered();
                birdCount = race.getTotalNumberOfBirdsEntered();
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
            out.print("<th>Time</th><th>Miles</th><th>Yards</th><th>Ring No.</th><th>Colour</th><th>Sex</th><th>Pools</th><th/>");
            if (section != null) {
                out.print("<th>Prize</th>");
            }
            out.print("<th>Velocity</th></tr>\n");
            
            // For each competition name keep a track of how many of the winners we have found.
            Map<String, Integer> competitionPositions = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                competitionPositions.put(c.getName(), 0);
            }

            Map<String, Map<String, Integer>> birdsInPools = race.getBirdsEnteredInPools();
            // For each competition within this section, calculate the number of winners
            Map<String, Integer> numberOfWinners = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                if (section != null || c.isAvailableInOpen()) {
                    int entrants = birdsInPools.get(sectionNotNull).get(c.getName());
                    numberOfWinners.put(c.getName(), c.maximumNumberOfWinners(entrants));
                }
            }

            List<Double> prizes = (section == null) ? null : race.getPrizes().get(section);
            int pos = 0;
            for (BirdResult row: results) {
                pos ++;
                out.print("<tr><td>" + pos + "</td>");

                double totalPrizeWonByThisBird = 0.0;

                Collection<String> competitionsEnteredByThisBird = null;
                if (section == null) {
                    competitionsEnteredByThisBird = row.time.getOpenCompetitionsEntered();
                } else {
                    competitionsEnteredByThisBird = row.time.getSectionCompetitionsEntered();
                }

                // Check the competitions that this bird entered
                for (Competition c: competitions) {
                    if (competitionsEnteredByThisBird.contains(c.getName())) {
                        int position = competitionPositions.get(c.getName()) + 1;
                        if (position <= numberOfWinners.get(c.getName())) {
                            int entrants = birdsInPools.get(sectionNotNull).get(c.getName());
                            double prize = c.prize(position, entrants);
                            totalPrizeWonByThisBird += prize;
                            competitionPositions.put(c.getName(), position);
                        }
                    }
                }
                if (totalPrizeWonByThisBird > 0) {
                    row.html.append("<td>" + Utilities.stringPrintf("%.2f", totalPrizeWonByThisBird) + "</td>");
                } else {
                    row.html.append("<td/>");
                }
                
                if (section != null) {
                    if (prizes != null && pos <= prizes.size() && prizes.get(pos-1) > 0) {
                        row.html.append("<td>" + Utilities.stringPrintf("%.2f", prizes.get(pos-1)) + "</td>");
                    } else {
                        row.html.append("<td/>");
                    }
                }
                    
                row.html.append("<td>" + Utilities.stringPrintf("%.3f", row.velocityInMetresPerSecond * Constants.METRES_PER_SECOND_TO_YARDS_PER_MINUTE) + "</td>");

                out.print(row.html.toString());
                out.print("</tr>\n");
            }
            out.print("</table>\n");
            out.print("</div>\n");
        }

        Utilities.writeHtmlFooter(out);
    }
}
