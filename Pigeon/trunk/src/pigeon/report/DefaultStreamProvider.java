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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public final class DefaultStreamProvider implements StreamProvider
{
    private final Collection<File> files = new LinkedList<File>();
    private final Collection<OutputStream> streams = new LinkedList<OutputStream>();
    
    public void closeAllStreams() throws IOException
    {
        for (OutputStream stream: streams) {
            stream.close();
        }
    }
    
    public Collection<File> getFiles()
    {
        return Collections.unmodifiableCollection(files);
    }
    
    @Override
    public OutputStream createNewStream(String name) throws IOException
    {
        OutputStream result;
        File outputFile = File.createTempFile(name, "");
        FileOutputStream fileOut = new FileOutputStream(outputFile);
        files.add(outputFile);
        result = new BufferedOutputStream(fileOut);
        streams.add(result);
        return result;
    }
}
