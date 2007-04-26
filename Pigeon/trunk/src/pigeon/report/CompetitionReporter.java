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
        List<Competition> competitions,
        Map<String, Map<String, Integer>> entrantsCount
    )
    {
        this.club = club;
        this.race = race;
        this.listClubNames = listClubNames;
        this.competitions = competitions;
        this.entrantsCount = entrantsCount;
    }
    
    public void write(OutputStream stream) throws IOException
    {
        String raceDate = pigeon.view.Utilities.DATE_FORMAT.format(race.getLiberationDate());
        String raceTime = pigeon.view.Utilities.TIME_FORMAT_WITH_LOCALE.format(race.getLiberationDate());
        PrintStream out = Utilities.writeHtmlHeader(stream, race.getRacepoint().toString() + " on " + raceDate);

        List<String> sections = Utilities.participatingSections(race);
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
