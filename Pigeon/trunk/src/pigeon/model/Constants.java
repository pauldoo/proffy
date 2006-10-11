/*
 * Pigeon: A pigeon club race result management program.
 * Copyright (C) 2005-2006  Paul Richards
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

/**
 * Some simple constants.
 * @author pauldoo
 */
public class Constants
{
    public static final double METRES_PER_YARD = 0.9144;
    public static final int YARDS_PER_MILE = 1760;
    public static final double METRES_PER_SECOND_TO_YARDS_PER_MINUTE = 60 / METRES_PER_YARD;
    public static final int MILLISECONDS_PER_MINUTE = 60 * 1000;
    public static final int MILLISECONDS_PER_DAY = 24 * 60 * MILLISECONDS_PER_MINUTE;
}
