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
final class Configuration
{
    private final Mode mode;
    private final Collection<Competition> competitions;
    
    public static enum Mode {
        FEDERATION,
        CLUB
    }

    public Configuration(InputStream input) throws IOException
    {
        final Properties properties = new Properties();
        properties.loadFromXML(input);
        
        this.mode = loadMode(properties);
        this.competitions = loadCompetitions(properties);
    }

    private static Mode loadMode(Properties properties)
    {
        return Mode.valueOf(properties.getProperty("Mode"));
    }

    private static Collection<Competition> loadCompetitions(Properties properties) throws IOException
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

            if ("Pool".equals(type)) {
                // Parse out Pool specific fields
                final int payoutPeriod = Integer.parseInt(properties.getProperty(competitionPrefix + ".PayoutPeriod"));
                result.add(new Pool(name, cost, clubTake, payoutPeriod));
            } else if ("Nomination".equals(type)) {
                // Parse out Nomination specific fields
                final String payoutRatios = properties.getProperty(competitionPrefix + ".PayoutRatios");
                final StringTokenizer tokenizer = new StringTokenizer(payoutRatios, ":");
                final int payoutCount = tokenizer.countTokens();
                final double[] payouts = new double[payoutCount];
                for (int j = 0; j < payoutCount; ++j) {
                    payouts[j] = Double.parseDouble(tokenizer.nextToken());
                }
                result.add(new Nomination(name, cost, clubTake, payouts));
            } else {
                throw new IOException("Unknown competiion type: '" + type + "'");
            }
        }
        return Collections.unmodifiableCollection(result);
    }
    
    public Mode getMode()
    {
        return this.mode;
    }
    
    public Collection<Competition> getCompetitions()
    {
        return this.competitions;
    }
}
