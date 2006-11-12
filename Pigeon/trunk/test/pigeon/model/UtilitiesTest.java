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

import junit.framework.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author pauldoo
 */
public class UtilitiesTest extends TestCase
{
    
    public UtilitiesTest(String testName)
    {
        super(testName);
    }

    protected void setUp() throws Exception
    {
    }

    protected void tearDown() throws Exception
    {
    }

    public void testRoundToNearestSecond()
    {
        assertEquals(0, Utilities.roundToNearestSecond(0));
        assertEquals(0, Utilities.roundToNearestSecond(499));
        assertEquals(1000, Utilities.roundToNearestSecond(500));
        assertEquals(1000, Utilities.roundToNearestSecond(501));

        assertEquals(0, Utilities.roundToNearestSecond(-0));
        assertEquals(0, Utilities.roundToNearestSecond(-499));
        assertEquals(-1000, Utilities.roundToNearestSecond(-500));
        assertEquals(-1000, Utilities.roundToNearestSecond(-501));
    }
    
}
