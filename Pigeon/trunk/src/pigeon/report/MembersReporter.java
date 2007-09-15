/*
    Copyright (C) 2005, 2006, 2007  Paul Richards.

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
import java.util.Collection;
import pigeon.model.Member;
import pigeon.view.Configuration;

/**
    Produces a table of all the member details.
*/
public final class MembersReporter extends Reporter
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

    public void write() throws IOException
    {
        final OutputStream stream = createNewStream("Members");
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
