<?xml version="1.0" encoding="UTF-8"?>
<!--
    Copyright (C) 2009  Paul Richards.

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
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output
        method="html"
        doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
        doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
        indent="yes"
        media-type="application/xhtml+xml"
        />

    <xsl:template name="CssStylesheet">
        <style type="text/css">
          body {
              font-family: Verdana, sans-serif;
              white-space: nowrap;
              font-size: small;
          }
          .numeric {
              text-align: right;
          }
          table {
              border: 1px solid;
              border-collapse: collapse;
          }
          td, th {
              border-left: 1px solid;
              border-right: 1px solid;
          }
          pre {
              margin: 0px;
              padding: 0px;
          }
        </style>
    </xsl:template>

    <xsl:template match="/ProffyResults">
        <html>
            <head>
                <title>Proffy Results</title>
                <xsl:call-template name="CssStylesheet"/>
            </head>
            <body>
                <h1>Hello</h1>
                <xsl:apply-templates select="File"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="File">
        <h2><xsl:value-of select="@Name"/></h2>
        <table>
            <tr><th>Inclusive</th><th>Exclusive</th><th>Line Number</th><th>Code</th></tr>
            <xsl:apply-templates select="Line"/>
        </table>
    </xsl:template>

    <xsl:template match="Line">
        <tr>
            <td class="numeric"><xsl:if test="@NonTerminalHits"><xsl:value-of select="@NonTerminalHits + @TerminalHits"/></xsl:if></td>
            <td class="numeric"><xsl:if test="@TerminalHits"><xsl:value-of select="@TerminalHits"/></xsl:if></td>
            <td class="numeric"><xsl:value-of select="@Number"/></td>
            <td><pre><xsl:value-of select="."/></pre></td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
