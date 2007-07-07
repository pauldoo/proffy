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
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import pigeon.competitions.Competition;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Time;

/**
    Generates an HTML report for the pools on a single race.

    Federation and section results are generated on the same page.
*/
public class CompetitionReporter implements Reporter
{
    private final Organization club;
    private final Race race;
    private final boolean listClubNames;
    private final List<Competition> competitions;
    private final Map<String, Map<String, Integer>> entrantsCount;

    /**
        Creates a new instance of CompetitionReporter.
    */
    public CompetitionReporter(
        Organization club,
        Race race,
        boolean listClubNames,
        List<Competition> competitions
    )
    {
        this.club = club;
        this.race = race;
        this.listClubNames = listClubNames;
        this.competitions = competitions;
        this.entrantsCount = race.getBirdsEnteredInPools();
    }

    public void write(OutputStream stream) throws IOException
    {
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
            } else {
                out.print("<h2>Open</h2>\n");
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
                    row.html.append("<td>" + time.getRingNumber() + "</td>");
                    results.add(row);
                }
            }

            out.print("<table>\n");
            out.print("<tr><th>Member</th>");
            if (listClubNames) {
                out.print("<th>Club</th>");
            }
            out.print("<th>Ring Number</th>");
            for (Competition c: competitions) {
                out.print("<th>" + c.getName() + "</th>");
            }
            out.print("<th>Total</th>");
            out.print("</tr>\n");

            // For each competition name keep a track of how many of the winners we have found.
            Map<String, Integer> competitionPositions = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                competitionPositions.put(c.getName(), 0);
            }

            // For each competition within this section, calculate the number of winners
            Map<String, Integer> numberOfWinners = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                numberOfWinners.put(c.getName(), c.maximumNumberOfWinners(entrants));
            }

            // Iterate each of the birds, in order they would appear in the race result.
            for (BirdResult row: results) {
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
                            int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                            double prize = c.prize(position, entrants);
                            row.html.append("<td>" + Utilities.stringPrintf("%.2f", prize) + "</td>");
                            totalPrizeWonByThisBird += prize;
                            competitionPositions.put(c.getName(), position);
                            continue;
                        }
                    }
                    row.html.append("<td/>");
                }
                if (totalPrizeWonByThisBird > 0) {
                    // If this member has taken a place in any competition, print their line.
                    out.print("<tr>\n");
                    out.print(row.html.toString());
                    out.print("<td>" + Utilities.stringPrintf("%.2f", totalPrizeWonByThisBird) + "</td>");
                    out.print("</tr>\n");
                }
            }

            Map<String, Double> totalForCompetition = new TreeMap<String, Double>();
            {
                // Print totals for each competition
                out.print("<tr><td/><td/><td>Total</td>");
                double totalPrizeGivenToEveryone = 0.0;
                for (Competition c: competitions) {
                    double totalPrizeGivenForThisCompetition = 0.0;
                    int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                    for (int pos = 1; pos <= competitionPositions.get(c.getName()); ++pos) {
                        totalPrizeGivenForThisCompetition += c.prize(pos, entrants);
                    }
                    totalForCompetition.put(c.getName(), totalPrizeGivenForThisCompetition);
                    totalPrizeGivenToEveryone += totalPrizeGivenForThisCompetition;
                    out.print("<td>" + Utilities.stringPrintf("%.2f", totalPrizeGivenForThisCompetition) + "</td>");
                }
                out.print("<td>" + Utilities.stringPrintf("%.2f", totalPrizeGivenToEveryone) + "</td>");
                out.print("</tr>\n");
            }

            {
                // Print unclaimed row
                out.print("<tr><td/><td/><td>Unclaimed</td>");
                double totalUnclaimed = 0.0;
                for (Competition c: competitions) {
                    int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                    double unclaimed = c.totalPoolMoney(entrants) - c.totalClubTake(entrants) - totalForCompetition.get(c.getName());
                    totalUnclaimed += unclaimed;
                    out.print("<td>" + Utilities.stringPrintf("%.2f", unclaimed) + "</td>");
                }
                out.print("<td>" + Utilities.stringPrintf("%.2f", totalUnclaimed) + "</td>");
                out.print("</tr>\n");
            }

            {
                // Print club take row
                out.print("<tr><td/><td/><td>Club take</td>");
                double totalClubTake = 0.0;
                for (Competition c: competitions) {
                    int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                    double clubTake = c.totalClubTake(entrants);
                    totalClubTake += clubTake;
                    out.print("<td>" + Utilities.stringPrintf("%.2f", clubTake) + "</td>");
                }
                out.print("<td>" + Utilities.stringPrintf("%.2f", totalClubTake) + "</td>");
                out.print("</tr>\n");
            }

            {
                // Print total pool money
                out.print("<tr><td/><td/><td>Total pool money</td>");
                double totalPoolMoney = 0.0;
                for (Competition c: competitions) {
                    int entrants = entrantsCount.get(sectionNotNull).get(c.getName());
                    double poolMoney = c.totalPoolMoney(entrants);
                    totalPoolMoney += poolMoney;
                    out.print("<td>" + Utilities.stringPrintf("%.2f", poolMoney) + "</td>");
                }
                out.print("<td>" + Utilities.stringPrintf("%.2f", totalPoolMoney) + "</td>");
                out.print("</tr>\n");
            }

            // Done!
            out.print("</table>\n");
            out.print("</div>\n");
        }

        Utilities.writeHtmlFooter(out);
    }

}
