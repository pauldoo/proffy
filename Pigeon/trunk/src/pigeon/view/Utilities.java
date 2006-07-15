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

package pigeon.view;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author pauldoo
 */
class Utilities {

    public static final int BASE_YEAR = 2000;
    
    public static <T extends Comparable<T>> Vector<T> sortCollection(Collection<T> collection) {
        return new Vector<T>(new TreeSet<T>(collection));
    }
}
