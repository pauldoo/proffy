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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import pigeon.competitions.Competition;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Member;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Season;
import pigeon.model.Time;
import pigeon.model.ValidationException;

/**
    Public static methods that don't have a natural home in any
    of the view classes.
*/
public final class Utilities {

    // Non-Creatable
    private Utilities()
    {
    }
    
    /**
        The start range for year drop down combo boxes.
    */
    public static final int YEAR_DISPLAY_START = 2005;

    /**
        The end range for year drop down combo boxes.
    */
    public static final int YEAR_DISPLAY_END = YEAR_DISPLAY_START + 10;

    /**
        DateFormat for formatting times that span just a single day.
    
        Their 'long' representation spans only from 0 to 24 * 60 * 60 * 1000.
        The resulting string will be in 24hr time (hopefully).
    */
    public static final DateFormat TIME_FORMAT_WITHOUT_LOCALE;
    static {
        TIME_FORMAT_WITHOUT_LOCALE = new SimpleDateFormat("HH:mm:ss");
        TIME_FORMAT_WITHOUT_LOCALE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
        DateFormat for formatting times that occur on a real calendar.
    
        Their 'long' representation is not confined to spanning just a single day.
        The local time zone is taken into account.
    */
    public static final DateFormat TIME_FORMAT_WITH_LOCALE;
    static {
        TIME_FORMAT_WITH_LOCALE = new SimpleDateFormat("HH:mm:ss");
    }

    /**
        DateFormat for formatting dates that occur on a real calendar.
    
        Their 'long' representation is not confined to spanning just a single day.
        The local time zone is taken into account.
    */
    public static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = new SimpleDateFormat("dd/MM/yy");
    }

    /**
        Given a long value representing a time, returns the beginning of that day.
    
        Does not take into account timezones or locale information, so should only
        be used for times that are relative and span only a few days.
    */
    public static long startOfDay(long time) {
        return (time / Constants.MILLISECONDS_PER_DAY) * Constants.MILLISECONDS_PER_DAY;
    }

    /**
        Given an Organization, return a list of all the club names mentioned in member profiles.
    */
    public static Collection<String> findClubNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getClub() != null && !m.getClub().equals("")) {
                result.add(m.getClub());
            }
        }
        return Collections.unmodifiableCollection(result);
    }

    /**
        Given an Organization, return a list of all the section names mentioned in member profiles.
    */
    public static Collection<String> findSectionNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getSection() != null && !m.getSection().equals("")) {
                result.add(m.getSection());
            }
        }
        return Collections.unmodifiableCollection(result);
    }
    
    /**
        Checks all the times entered in a season and returns all the competitions
        mentioned in the pigeon time entries.
        
        Used when loading a season to verify that all of the competitions used when
        saving the season are still present in the configuration file.
    */
    private static Collection<String>  getCompetitionNames(Season season)
    {
        // Use a Set here to ensure that duplicates are removed.
        Set<String> result = new TreeSet<String>();
        for (Race r: season.getRaces()) {
            for (Clock c: r.getClocks()) {
                for (Time t: c.getTimes()) {
                    result.addAll(t.getOpenCompetitionsEntered());
                    result.addAll(t.getSectionCompetitionsEntered());
                }
            }
        }
        return Collections.unmodifiableCollection(result);
    }
    
    /**
        Returns a list of all the competition names in a configuration.
    */
    public static List<String>  getCompetitionNames(List<Competition> competitions)
    {
        // Don't expect duplicate names in the configuration, so we can use
        // any kind of collection.
        List<String> result = new ArrayList<String>();
        for (Competition c: competitions) {
            result.add(c.getName());
        }
        return Collections.unmodifiableList(result);
    }
    
    /**
        Verifies that a season is valid with respect to the current
        application version and configuration.
    
        Checks (for example) that the competition names mentioned
        in the loaded file are still present in the application configuration
        file.
    */
    public static void validateSeason(Season season, Configuration configuration) throws ValidationException
    {
        Collection<String> competitionsMentionedInFile = getCompetitionNames(season);
        Collection<String> competitionsConfigured = getCompetitionNames(configuration.getCompetitions());
        if (!competitionsConfigured.containsAll(competitionsMentionedInFile)) {
            // The application configuration does not mention all the competitions mentioned
            // in the season, so we shouldn't load it.
            throw new ValidationException("The season cannot be loaded.\nThe application is not configured with the pools used in this season.");
        }
    }
}
