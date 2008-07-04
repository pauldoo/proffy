/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

package pigeon.model;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
    Utility functions for manipulating time.
*/
public final class Utilities
{
    // Non-Creatable
    private Utilities()
    {
    }

    /**
        Given a long value representing a time, returns the beginning of that day.

        Does not take into account timezones or locale information, so should only
        be used for times that are relative and span only a few days.
    */
    public static long truncateToMidnight(long time)
    {
        if (time >= Constants.MILLISECONDS_PER_DAY * 10) {
            throw new IllegalArgumentException("Expecting a small 'relative' date");
        }
        return (time / Constants.MILLISECONDS_PER_DAY) * Constants.MILLISECONDS_PER_DAY;
    }

    
    /**
        Given a date returns the beggining of the day (using local timezone).
    */
    public static Date beginningOfCalendarDay(Date date)
    {
        if (date.getTime() <= Constants.MILLISECONDS_PER_DAY * 10) {
            throw new IllegalArgumentException("Expecting an 'absolute' date");
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }
    
    /**
        Given a rate retuns the fraction of the day elapsed (using local timezone).
    */
    public static Date fractionOfCalendarDay(Date date)
    {
        if (date.getTime() <= Constants.MILLISECONDS_PER_DAY * 10) {
            throw new IllegalArgumentException("Expecting an 'absolute' date");
        }
        return new Date(date.getTime() - beginningOfCalendarDay(date).getTime());
    }

    public static long roundToNearestSecond(long time) {
        return ((time + ((time >= 0) ? 500 : -500)) / 1000) * 1000;
    }

    /**
        Sorts the given list (modifies in place), and then returns an unmodifiable copy.
     */
    public static <T extends Comparable<? super T>>
    List<T> unmodifiableSortedList(List<T> items)
    {
        Collections.sort(items);
        return Collections.unmodifiableList(items);
    }
}
