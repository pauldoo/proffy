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

package pigeon.view;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import pigeon.competitions.Competition;
import pigeon.competitions.Nomination;
import pigeon.competitions.Pool;

/**
 * Loads the application configuration from an XML file.
 */
public final class Configuration
{
    private final Mode mode;
    private final String resultsFooter;
    private final List<Competition> competitions;

    public static enum Mode {
        FEDERATION,
        CLUB
    }

    public Configuration(InputStream input) throws IOException
    {
        final Properties properties = new Properties();
        properties.loadFromXML(input);

        this.mode = loadMode(properties);
        this.resultsFooter = loadResultsFooter(properties);
        this.competitions = loadCompetitions(properties);
    }

    private static Mode loadMode(Properties properties)
    {
        return Mode.valueOf(properties.getProperty("Mode"));
    }
    
    private static String loadResultsFooter(Properties properties)
    {
        return properties.getProperty("ResultsFooter");
    }

    private static List<Competition> loadCompetitions(Properties properties) throws IOException
    {
        /**
            This code is a bit messy, not sure what the ideal
            method should be for doing this.
        */
        final int count = Integer.parseInt(properties.getProperty("Competition.Count"));
        List<Competition> result = new ArrayList<Competition>();
        for (int i = 1; i <= count; ++i) {
            final String competitionPrefix = "Competition." + i;
            final String name = properties.getProperty(competitionPrefix + ".Name");
            final String type = properties.getProperty(competitionPrefix + ".Type");
            final double cost = Double.parseDouble(properties.getProperty(competitionPrefix + ".Cost"));
            final double clubTake = Double.parseDouble(properties.getProperty(competitionPrefix + ".ClubTake"));
            final boolean availableInOpen = Boolean.parseBoolean(properties.getProperty(competitionPrefix + ".AvailableInOpen"));
            
            if ("Pool".equals(type)) {
                // Parse out Pool specific fields
                final int payoutPeriod = Integer.parseInt(properties.getProperty(competitionPrefix + ".PayoutPeriod"));
                result.add(new Pool(name, cost, clubTake, availableInOpen, payoutPeriod));
            } else if ("Nomination".equals(type)) {
                // Parse out Nomination specific fields
                final String payoutRatios = properties.getProperty(competitionPrefix + ".PayoutRatios");
                final StringTokenizer tokenizer = new StringTokenizer(payoutRatios, ":");
                final int payoutCount = tokenizer.countTokens();
                final double[] payouts = new double[payoutCount];
                for (int j = 0; j < payoutCount; ++j) {
                    payouts[j] = Double.parseDouble(tokenizer.nextToken());
                }
                result.add(new Nomination(name, cost, clubTake, availableInOpen, payouts));
            } else {
                throw new IOException("Unknown competiion type: '" + type + "'");
            }
        }
        return Collections.unmodifiableList(result);
    }

    public Mode getMode()
    {
        return this.mode;
    }

    public List<Competition> getCompetitions()
    {
        return this.competitions;
    }

    public String getResultsFooter()
    {
        return resultsFooter;
    }
}
