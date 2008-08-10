/*
    Copyright (C) 2008  Paul Richards.

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

package pigeon.report;

import java.io.IOException;
import java.io.OutputStream;

/**
    Reporters call onto this instace to obtain OutputStream
    instances for writing out the report files.  This interface
    is useful for unit tests, which want to capture the output
    of the reporters to an in memory buffer.
*/
public interface StreamProvider
{
    /**
        Called by the reporter when it wants to write a file.  The name of the file
        (e.g. "members.xml") is passed as a hint as to what the file should be
        named.
    */
    OutputStream createNewStream(String filename, boolean showToUser) throws IOException;
}
