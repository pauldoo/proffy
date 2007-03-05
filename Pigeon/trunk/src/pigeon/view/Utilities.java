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
import java.util.Collection;
import java.util.SortedSet;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import pigeon.model.Constants;
import pigeon.model.Member;
import pigeon.model.Organization;

/**
 * Public static methods for doing various things (mainly manipulating time).
 */
public final class Utilities {

    // Non-Creatable
    private Utilities()
    {
    }
    
    public static final int BASE_YEAR = 2000;

    /**
        DateFormat for formatting times that span just a single day.
    
        Their 'long' representation spans only from 0 to 24 * 60 * 60 * 1000.
    */
    public static final DateFormat TIME_FORMAT_WITHOUT_LOCALE;
    static {
        TIME_FORMAT_WITHOUT_LOCALE = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        TIME_FORMAT_WITHOUT_LOCALE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    /**
        DateFormat for formatting times that occur on a real calendar.
    
        Their 'long' representation is not confined to spanning just a single day.
    */
    public static final DateFormat TIME_FORMAT_WITH_LOCALE;
    static {
        TIME_FORMAT_WITH_LOCALE = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    }

    public static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    }

    public static long startOfDay(long time) {
        return (time / Constants.MILLISECONDS_PER_DAY) * Constants.MILLISECONDS_PER_DAY;
    }

    /**
        Given an Organization, return a list of all the club names mentioned in member profiles.
    */
    public static SortedSet<String> findClubNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getClub() != null && !m.getClub().equals("")) {
                result.add(m.getClub());
            }
        }
        return result;
    }

    /**
        Given an Organization, return a list of all the section names mentioned in member profiles.
    */
    public static SortedSet<String> findSectionNames(Organization organization)
    {
        SortedSet<String> result = new TreeSet<String>();
        for (Member m: organization.getMembers()) {
            if (m.getSection() != null && !m.getSection().equals("")) {
                result.add(m.getSection());
            }
        }
        return result;
    }
}
