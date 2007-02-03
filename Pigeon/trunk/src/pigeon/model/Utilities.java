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

package pigeon.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Utility functions for manipulating time.
 */
public class Utilities
{
    /**
     * Given a date returns the beggining of the day (using local timezone).
     */
    public static Date beginningOfDay(Date date) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal = new GregorianCalendar(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

    public static long roundToNearestSecond(long time) {
        return ((time + ((time >= 0) ? 500 : -500)) / 1000) * 1000;
    }
}
