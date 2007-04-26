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
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import pigeon.model.Clock;
import pigeon.model.Constants;
import pigeon.model.Distance;
import pigeon.model.Organization;
import pigeon.model.Race;
import pigeon.model.Time;

/**
    Shared HTML bits.
*/
public final class Utilities
{
    // Non-Creatable
    private Utilities()
    {
    }
        
    public static PrintStream writeHtmlHeader(OutputStream stream, String title) throws IOException
    {
        PrintStream out = new PrintStream(stream, false, "UTF-8");
        
        out.print("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        out.print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"\n");
        out.print("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n");
        out.print("<html xmlns=\"http://www.w3.org/1999/xhtml\">\n");
        out.print("<head>\n");
        out.print("  <title>" + title + "</title>\n");
        out.print("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\n");
        out.print("  <style type=\"text/css\">\n");
        out.print("    body { font-family: Verdana, sans-serif; }\n");
        out.print("    .outer { text-align:center; page-break-after: always; }\n");
        out.print("    .outer.last { page-break-after: auto; }\n");
        out.print("    h1 { margin-bottom:10px; font-size:18pt; }\n");
        out.print("    h2 { font-size:16pt; }\n");
        out.print("    h3 { font-size:14pt; }\n");
        out.print("    h2, h3 { margin-top:0; margin-bottom:5px; }\n");
        out.print("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:10pt; margin-top:20px; }\n");
        out.print("    th { border-bottom:3px solid #000000; text-align: left; }\n");
        out.print("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }\n");
        out.print("  </style>\n");
        out.print("</head>\n");
        out.print("<body>\n");
        if (out.checkError()) {
            throw new IOException();
        }
        
        return out;
    }

    public static void writeHtmlFooter(PrintStream out) throws IOException
    {
        out.print("</body>\n");
        out.print("</html>\n");
        out.flush();
        if (out.checkError()) {
            throw new IOException();
        }
    }
    
    /**
        Returns a list of the sections that were involved in a race,
        or an empty collection if no section information is available.
    */
    public static List<String> participatingSections(Race race)
    {
        Set<String> result = new TreeSet<String>();
        for (Clock clock: race.getClocks()) {
            String section = clock.getMember().getSection();
            if (section != null) {
                result.add(section);
            }
        }
        return new ArrayList<String>(result);
    }    
    
    public static String stringPrintf(String format, Object... args) {
            StringWriter buffer = new StringWriter();
            PrintWriter writer = new PrintWriter(buffer);
            writer.printf(format, args);
            writer.flush();
            return buffer.toString();
    }
    
    public static BirdResult calculateVelocity(Organization club, Race race, Clock clock, Time time)
    {
        Date correctedClockTime = clock.convertMemberTimeToMasterTime(new Date(time.getMemberTime()), race);
        int nightsSpentSleeping = (int)(time.getMemberTime() / Constants.MILLISECONDS_PER_DAY);
        long timeSpentSleeping = nightsSpentSleeping * race.getLengthOfDarknessEachNight();
        double flyTimeInSeconds = (correctedClockTime.getTime() - race.getLiberationDate().getTime() - timeSpentSleeping) / 1000.0;
        Distance distance = club.getDistanceEntry(clock.getMember(), race.getRacepoint()).getDistance();
        double velocityInMetresPerSecond = distance.getMetres() / flyTimeInSeconds;        
        
        return new BirdResult(velocityInMetresPerSecond, time, correctedClockTime, distance);
    }
}
