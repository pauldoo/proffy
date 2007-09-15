/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package pigeon.view;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Simple implementation of javax.swing.filechooser.FileFilter to filter *.pcs files.
 */
final class SimpleFileFilter extends FileFilter {

    private final String extensions[];
    private final String description;

    public SimpleFileFilter(String[] extensions, String description) {
        this.extensions = extensions;
        this.description = description;
    }

    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        for (String extension: extensions) {
            if (file.getName().endsWith("." + extension)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }

    public static SimpleFileFilter createSeasonFileFilter() {
        return new SimpleFileFilter(new String[]{"pcs"}, "Pigeon Club Season");
    }

}
