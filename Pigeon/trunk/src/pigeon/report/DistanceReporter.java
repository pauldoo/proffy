/*
    Copyright (c) 2005-2007, Paul Richards
    All rights reserved.

    Redistribution and use in source and binary forms, with or without
    modification, are permitted provided that the following conditions are met:

        * Redistributions of source code must retain the above copyright notice,
        this list of conditions and the following disclaimer.

        * Redistributions in binary form must reproduce the above copyright
        notice, this list of conditions and the following disclaimer in the
        documentation and/or other materials provided with the distribution.

        * Neither the name of Paul Richards nor the names of contributors may be
        used to endorse or promote products derived from this software without
        specific prior written permission.

    THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
    AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
    IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
    ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
    LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
    CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
    SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
    INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
    CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
    ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
    POSSIBILITY OF SUCH DAMAGE.
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
