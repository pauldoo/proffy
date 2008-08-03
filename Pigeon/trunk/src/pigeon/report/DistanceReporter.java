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

package pigeon.report;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import pigeon.model.Distance;

/**
    Produces an HTML report of the distances for a member or racepoint.
*/
public final class DistanceReporter<Target> extends Reporter
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

    @Override
    public void write() throws IOException
    {
        final OutputStream stream = createNewStream("Distances");
        PrintStream out = Utilities.writeHtmlHeader(stream, "Distances for " + source);
        out.println("<div class='outer'>");
        out.println("<h1>" + organization + "</h1>");
        out.println("<h2>Distances for " + source + "</h2>");

        out.println("<table>");
        out.println("<tr><th>" + targetTypeName + "</th><th>Distance</th></tr>");
        for (Map.Entry<Target, Distance> entry: distances.entrySet()) {
            out.println("<tr><td>" + entry.getKey().toString() + "</td><td>" + entry.getValue().toString() + "</td></tr>");
        }
        out.println("</table>");
        out.println("</div>");
        Utilities.writeHtmlFooter(out);
    }
}
