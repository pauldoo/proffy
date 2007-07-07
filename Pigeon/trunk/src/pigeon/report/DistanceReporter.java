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

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import pigeon.model.Distance;

/**
    Produces an HTML report of the distances for a member or racepoint.
*/
public final class DistanceReporter<Target> implements Reporter
{
    final String organization;
    final String source;
    final String targetTypeName;
    final Map<Target, Distance> distances;

    public DistanceReporter(String organization, String source, String targetTypeName, Map<Target, Distance> distances)
    {
        this.organization = organization;
        this.source = source;
        this.targetTypeName = targetTypeName;
        this.distances = distances;
    }

    public void write(OutputStream stream) throws IOException
    {
        PrintStream out = Utilities.writeHtmlHeader(stream, "Distances for " + source);
        out.print("<div class='outer'>\n");
        out.print("<h1>" + organization + "</h1>\n");
        out.print("<h2>Distances for " + source + "</h2>\n");

        out.print("<table>\n");
        out.print("<tr><th>" + targetTypeName + "</th><th>Distance</th></tr>\n");
        for (Map.Entry<Target, Distance> entry: distances.entrySet()) {
            out.print("<tr><td>" + entry.getKey().toString() + "</td><td>" + entry.getValue().toString() + "</td></tr>\n");
        }
        out.print("</table>\n");
        out.print("</div>\n");
        Utilities.writeHtmlFooter(out);
    }
}
