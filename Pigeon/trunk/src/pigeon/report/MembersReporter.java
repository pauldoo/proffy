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
import java.util.Collection;
import pigeon.model.Member;
import pigeon.view.Configuration;

/**
    Produces a table of all the member details.
*/
public final class MembersReporter implements Reporter
{
    private final String organization;
    private final Collection<Member> members;
    private final Configuration.Mode mode;
    
    public MembersReporter(final String organization, final Collection<Member> members, Configuration.Mode mode)
    {
        this.organization = organization;
        this.members = members;
        this.mode = mode;
    }
    
    public void write(OutputStream stream) throws IOException
    {
        PrintStream out = Utilities.writeHtmlHeader(stream, "Members for " + organization);
        out.print("<div class='outer'>\n");
        out.print("<h1>" + organization + "</h1>\n");
        out.print("<h2>Members</h2>\n");

        out.print("<table>\n");
        out.print("<tr>");
        out.print("<th>Name</th>");
        if (mode == Configuration.Mode.CLUB) {
        } else if (mode == Configuration.Mode.FEDERATION) {
            out.print("<th>Club</th>");
            out.print("<th>Section</th>");
        } else {
            throw new IllegalArgumentException("Unexpected application mode");
        }
        out.print("<th>Address</th>");
        out.print("<th>Telephone</th>");
        out.print("<th>SHU Number</th>");
        out.print("</tr>\n");

        for (Member member: members) {
            out.print("<tr>");
            out.print("<td>" + member.getName() + "</td>");
            if (mode == Configuration.Mode.CLUB) {
            } else if (mode == Configuration.Mode.FEDERATION) {
                out.print("<td>" + member.getClub() + "</td>");
                out.print("<td>" + member.getSection() + "</td>");
            } else {
                throw new IllegalArgumentException("Unexpected application mode");
            }
            out.print("<td>" + Utilities.insertBrTags(member.getAddress()) + "</td>");
            out.print("<td>" + member.getTelephone() + "</td>");
            out.print("<td>" + member.getSHUNumber() + "</td>");
            out.print("</tr>\n");
        }

        out.print("</table>\n");
        out.print("</div>\n");
        Utilities.writeHtmlFooter(out);
    }
}
