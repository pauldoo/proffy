/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2007  Paul Richards
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package pigeon.view;

import java.text.DateFormat;
import java.util.Collection;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.Vector;
import pigeon.model.Constants;

/**
 * Public static methods for doing various things (mainly manipulating time).
 */
public class Utilities {

    public static final int BASE_YEAR = 2000;

    // DateFormat for formatting times that span just a single day.
    // Their 'long' representation spans only from 0 to 24 * 60 * 60 * 1000.
    public static final DateFormat TIME_FORMAT_WITHOUT_LOCALE;
    static {
        TIME_FORMAT_WITHOUT_LOCALE = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        TIME_FORMAT_WITHOUT_LOCALE.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    // DateFormat for formatting times that occur on a real calendar.
    // Their 'long' representation is not confined to spanning just a single day.
    public static final DateFormat TIME_FORMAT_WITH_LOCALE;
    static {
        TIME_FORMAT_WITH_LOCALE = DateFormat.getTimeInstance(DateFormat.MEDIUM);
    }

    public static final DateFormat DATE_FORMAT;
    static {
        DATE_FORMAT = DateFormat.getDateInstance(DateFormat.SHORT);
    }

    public static <T extends Comparable<T>> Vector<T> sortCollection(Collection<T> collection) {
        return new Vector<T>(new TreeSet<T>(collection));
    }

    public static long startOfDay(long time) {
        return (time / Constants.MILLISECONDS_PER_DAY) * Constants.MILLISECONDS_PER_DAY;
    }
}
