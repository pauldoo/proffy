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

package pigeon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import pigeon.report.StreamProvider;

final class RegressionStreamProvider implements StreamProvider
{
    final SortedMap<String, ByteArrayOutputStream> streams = new TreeMap<String, ByteArrayOutputStream>();

    @Override
    public OutputStream createNewStream(String filename) throws IOException {
        if (streams.containsKey(filename)) {
            throw new IOException("Duplicate filename");
        }
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        streams.put(filename, result);
        return result;
    }
    
    public byte[] getBytes()
    {
        if (streams.size() != 1) {
            throw new IllegalStateException("Do not one stream");
        }
        return getBytes(streams.firstKey());
    }
    
    public byte[] getBytes(String filename)
    {
        return streams.get(filename).toByteArray();
    }
    
    public Set<String> getFilenames()
    {
        return Collections.unmodifiableSet(new TreeSet<String>(streams.keySet()));
    }
}
