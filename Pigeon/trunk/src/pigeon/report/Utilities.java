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

/**
    Shared HTML bits.
*/
final class Utilities
{
    // Non-Creatable
    private Utilities()
    {
    }
    
    public static PrintStream writeHtmlHeader(OutputStream stream, String title) throws IOException
    {
        PrintStream out = new PrintStream(stream, false, "UTF-8");
        
        out.println("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\"");
        out.println("\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
        out.println("<head>");
        out.println("  <title>" + title + "</title>");
        out.println("  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />");
        out.println("  <style type=\"text/css\">");
        out.println("    .outer { text-align:center; page-break-after: always; }");
        out.println("    .outer.last { page-break-after: auto; }");
        out.println("    h1 { margin-bottom:10px; font-size:18pt; }");
        out.println("    h2 { font-size:16pt; }");
        out.println("    h3 { font-size:14pt; }");
        out.println("    h2, h3 { margin-top:0; margin-bottom:5px; }");
        out.println("    table { width:95%; border:1px solid #000000; border-collapse:collapse; font-size:10pt; margin-top:20px; }");
        out.println("    th { border-bottom:3px solid #000000; text-align: left; }");
        out.println("    td { border-bottom:1px solid #000000; page-break-inside:avoid; padding:3px 0 3px 0; }");
        out.println("  </style>");
        out.println("</head>");
        out.println("<body>");
        if (out.checkError()) {
            throw new IOException();
        }
        
        return out;
    }

    public static void writeHtmlFooter(PrintStream out) throws IOException
    {
        out.println("</body>");
        out.println("</html>");
        out.flush();
        if (out.checkError()) {
            throw new IOException();
        }
    }
}
