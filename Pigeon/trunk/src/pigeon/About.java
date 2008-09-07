/*
    Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
    Provides access to the public information about the program.
*/
public final class About
{
    public static final String VERSION = "0.8" + " (build " + getBuildId() + ")";
    public static final String TITLE = "RacePoint v" + VERSION;
    public static final String CREDITS = "Created by Paul Richards <paul.richards@gmail.com>.";
    public static final String WEBSITE = "http://pauldoo.co.uk/racepoint/";
    
    // NonCreatable
    private About()
    {
    }

    /**
        Attempt to read the Subversion ID from the BuildID.txt file inside the Jar file.
    
        Returns "unknown" if this cannot be found.
    */
    public static String getBuildId()
    {
        InputStream in = ClassLoader.getSystemResourceAsStream("BuildID.txt");
        try {
            if (in != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String buildId = reader.readLine();
                if (buildId != null) {
                    return buildId;
                }
            }
        } catch (IOException e) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
        return "unknown";
    }
    
    /**
        Returns a copy of the license.
    */
    public static String getLicense()
    {
        StringWriter string = new StringWriter();
        PrintWriter writer = new PrintWriter(string);
        writer.println("Copyright (C) 2005, 2006, 2007, 2008  Paul Richards.");
        writer.println();
        writer.println("This program is free software: you can redistribute it and/or modify");
        writer.println("it under the terms of the GNU General Public License as published by");
        writer.println("the Free Software Foundation, either version 3 of the License, or");
        writer.println("(at your option) any later version.");
        writer.println("");
        writer.println("This program is distributed in the hope that it will be useful,");
        writer.println("but WITHOUT ANY WARRANTY; without even the implied warranty of");
        writer.println("MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the");
        writer.println("GNU General Public License for more details.");
        writer.println("");
        writer.println("You should have received a copy of the GNU General Public License");
        writer.println("along with this program.  If not, see <http://www.gnu.org/licenses/>.");
        writer.close();
        return string.toString();
    }    
}
