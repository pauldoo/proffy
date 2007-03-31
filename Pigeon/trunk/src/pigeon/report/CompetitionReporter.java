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
    
    private static final class Row implements Comparable<Row>
    {
        public final double velocity;
        public final Time time;
        public final StringBuffer html = new StringBuffer();
        
        public Row(double velocity, Time time)
        {
            this.velocity = velocity;
            this.time = time;
        }
        
        public boolean equals(Object rhs)
        {
            return equals((Row)rhs);
        }
        
        public boolean equals(Row rhs)
        {
            return compareTo(rhs) == 0;
        }
        
        public int compareTo(Row rhs)
        {
            final Row lhs = this;
            if (lhs == rhs) {
                return 0;
            }
            
            int result = -Double.compare(lhs.velocity, rhs.velocity);
            if (result == 0) {
                result = lhs.time.compareTo(rhs.time);
            }
            return result;
        }
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
            if (section != sections.get(sections.size() - 1)) {
                out.print("<div final class=\"outer\">\n");
            } else {
                out.print("<div final class=\"outer last\">\n");
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
            SortedSet<Row> results = new TreeSet<Row>();
            
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
                    
                    Row row = new Row(velocity, time);
                    row.html.append("<td>" + clock.getMember().toString() + "</td>");
                    if (listClubNames) {
                        row.html.append("<td>" + clock.getMember().getClub() + "</td>");
                    }
                    row.html.append("<td>" + time.getRingNumber() + "</td>");
                    results.add(row);
                }
            }
            out.print("<h3>" + memberCount + " members sent in a total of " + birdCount + " birds</h3>\n");
            out.print("<table>\n");
            out.print("<tr><th>Member</th>");
            if (listClubNames) {
                out.print("<th>Club</th>");
            }
            out.print("<th>Ring Number</th>");
            for (Competition c: competitions) {
                out.print("<th>" + c.getName() + "</th>");
            }
            out.print("</tr>\n");
                        
            // For each competition name keep a track of how many of the winners we have found.
            Map<String, Integer> competitionPositions = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                competitionPositions.put(c.getName(), 0);
            }

            // For each competition within this section, calculate the number of winners
            Map<String, Integer> numberOfWinners = new TreeMap<String, Integer>();
            for (Competition c: competitions) {
                int entrants = entrantsCount.get(section == null ? "Open" : section).get(c.getName());
                numberOfWinners.put(c.getName(), c.maximumNumberOfWinners(entrants));
            }
            
            // Iterate each of the birds, in order they would appear in the race result.
            for (Row row: results) {
                boolean birdHasWonSomething = false;
                Collection<String> competitionsEnteredByThisBird = null;
                if (section == null) {
                    competitionsEnteredByThisBird = row.time.getOpenCompetitionsEntered();
                } else {
                    competitionsEnteredByThisBird = row.time.getSectionCompetitionsEntered();
                }
                for (Competition c: competitions) {
                    if (competitionsEnteredByThisBird.contains(c.getName())) {
                        int position = competitionPositions.get(c.getName()) + 1;
                        if (position <= numberOfWinners.get(c.getName())) {
                            birdHasWonSomething = true;
                            row.html.append("<td>" + position + "</td>");
                            competitionPositions.put(c.getName(), position);
                            continue;
                        }
                    }
                    row.html.append("<td/>");
                }
                if (birdHasWonSomething) {
                    // If this member has taken a place in any competition, print their line.
                    out.print("<tr>\n");
                    out.print(row.html.toString());
                    out.print("</tr>\n");
                }
            }
            out.print("</table>\n");
            out.print("</div>\n");
        }

        Utilities.writeHtmlFooter(out);
    }
    
}
