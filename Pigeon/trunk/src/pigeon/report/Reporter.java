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

package pigeon.report;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
    Interface implemented by all the HTML reporters.
*/
public abstract class Reporter
{
    private final Collection<File> files = new LinkedList<File>();
    private final Collection<OutputStream> streams = new LinkedList<OutputStream>();
    private final List<OutputStream> forcedStreams = new LinkedList<OutputStream>();
    
    public abstract void write() throws IOException;
    
    public void closeAllStreams() throws IOException
    {
        for (OutputStream stream: streams) {
            stream.close();
        }
    }
    
    /**
        Override the stream used during write(), used by unit tests.
    */
    public void forceOutputStream(OutputStream out)
    {
        forcedStreams.add(out);
    }
    
    public Collection<File> getFiles()
    {
        return Collections.unmodifiableCollection(files);
    }
    
    protected OutputStream createNewStream(String prefix) throws IOException
    {
        OutputStream result;
        if (forcedStreams.isEmpty()) {
            File outputFile = File.createTempFile(prefix, ".html");
            FileOutputStream fileOut = new FileOutputStream(outputFile);
            files.add(outputFile);
            result = new BufferedOutputStream(fileOut);
        } else {
            result = forcedStreams.remove(0);
        }
        streams.add(result);
        return result;
    }
}
