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
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output
      method="html"
      doctype-public="-//W3C//DTD XHTML 1.0 Strict//EN"
      doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"
      indent="yes"
      media-type="application/xhtml+xml"
        />

    <xsl:key
        name="PointsBy_Id"
        match="/ProffyResults/PointsEncountered/Point"
        use="@Id"/>

    <xsl:key
        name="PointsBy_SymbolName"
        match="/ProffyResults/PointsEncountered/Point"
        use="@SymbolName"/>

    <xsl:key
        name="PointsBy_Id_SymbolName"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@Id, '#', @SymbolName)"/>

    <xsl:key
        name="PointsBy_Id_SymbolName_FileName_LineNumber"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@Id, '#', @SymbolName, '#', @FileName, '#', @LineNumber)"/>

    <xsl:key
        name="PointsBy_SymbolName_FileName"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@SymbolName, '#', @FileName)"/>

    <xsl:key
        name="PointsBy_SymbolName_FileName_LineNumber"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@SymbolName, '#', @FileName, '#', @LineNumber)"/>

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
                  border-top: 1px dotted;
                  border-bottom: 1px dotted;
              }
              pre {
                  margin: 0px;
                  padding: 0px;
              }
              .detail {
                  font-size: smaller;
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
                <xsl:apply-templates select="Summary"/>
                <xsl:apply-templates select="PointsEncountered" mode="TableOfContents"/>
                <xsl:apply-templates select="PointsEncountered" mode="Details"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="Summary">
        <h1>Summary</h1>
        <table>
            <tr>
                <th>Wall clock time (seconds)</th>
                <td class="numeric"><xsl:value-of select="WallClockTimeInSeconds"/></td>
            </tr>
            <tr>
                <th>Application time (seconds)</th>
                <td class="numeric"><xsl:value-of select="SampleCount * DelayBetweenSamplesInSeconds"/></td>
            </tr>
            <tr>
                <th>Delay between samples (seconds)</th>
                <td class="numeric"><xsl:value-of select="DelayBetweenSamplesInSeconds"/></td>
            </tr>
            <tr>
                <th>Samples gathered</th>
                <td class="numeric"><xsl:value-of select="SampleCount"/></td>
            </tr>
            <tr>
                <th>Callstacks gathered</th>
                <td class="numeric"><xsl:value-of select="CallstackCount"/></td>
            </tr>
        </table>
    </xsl:template>

    <xsl:template match="PointsEncountered" mode="TableOfContents">
        <h1>Contents</h1>
        <table>
            <tr><th colspan="2">TimeSpent</th><th/></tr>
            <tr><th>Inclusive</th><th>Exclusive</th><th>Symbol</th></tr>
            <xsl:for-each select="Point">
                <xsl:variable name="mysymbol" select="@SymbolName"/>
                <xsl:variable name="myid" select="@Id"/>
                <xsl:if test="count(key('PointsBy_SymbolName', $mysymbol)[@Id &lt; $myid]) = 0">
                    <tr>
                        <td class="numeric">
                            <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[key('PointsBy_Id_SymbolName', concat(@CallerId, '#', $mysymbol))]/@Count)"/>
                            <xsl:value-of select="$total"/>
                        </td>
                        <td class="numeric">
                            <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[@CalleeId = -1 and key('PointsBy_Id_SymbolName', concat(@CallerId, '#', $mysymbol))]/@Count)"/>
                            <xsl:value-of select="$total"/>
                        </td>
                        <td>
                            <a><xsl:attribute name="href">#Symbol_<xsl:value-of select="$myid"/></xsl:attribute>
                                <xsl:value-of select="$mysymbol"/>
                            </a>
                        </td>
                    </tr>
                </xsl:if>
            </xsl:for-each>
        </table>
    </xsl:template>

    <xsl:template match="PointsEncountered" mode="Details">
        <h1>Details</h1>
        <xsl:for-each select="Point">
            <xsl:variable name="mysymbol" select="@SymbolName"/>
            <xsl:variable name="myid" select="@Id"/>
            <xsl:if test="count(key('PointsBy_SymbolName', $mysymbol)[@Id &lt; $myid]) = 0">
                <h2>
                    <!-- Insert anchors for links to this symbol. -->
                    <xsl:for-each select="key('PointsBy_SymbolName', $mysymbol)">
                        <a><xsl:attribute name="name">Symbol_<xsl:value-of select="@Id"/></xsl:attribute></a>
                    </xsl:for-each>
                    <xsl:value-of select="@SymbolName"/>
                </h2>
                <xsl:for-each select="/ProffyResults/Files/File">
                    <xsl:variable name="filename" select="@Name"/>
                    <xsl:if test="count(key('PointsBy_SymbolName_FileName', concat($mysymbol, '#', $filename))) > 0">
                        <h3>
                            <xsl:value-of select="@Name"/>
                        </h3>
                        <h4>Callers</h4>
                        <table>
                            <tr>
                                <th>Symbol + File + Line Number</th>
                                <th>Count</th>
                            </tr>
                            <xsl:for-each select="/ProffyResults/PointsEncountered/Point">
                                <xsl:variable name="callersymbol" select="@SymbolName"/>
                                <xsl:variable name="callerfile" select="@FileName"/>
                                <xsl:variable name="callerline" select="@LineNumber"/>
                                <xsl:variable name="callerid" select="@Id"/>
                                <xsl:if test="count(key('PointsBy_SymbolName_FileName_LineNumber', concat($callersymbol, '#', $callerfile, '#', $callerline))[@Id &lt; $callerid]) = 0">
                                    <!--
                                        This is the first point that represents this symbol+file+line, so
                                        now sum the number of counters whose caller is the same symbol+file+line
                                    -->
                                    <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[key('PointsBy_Id_SymbolName', concat(@CalleeId, '#', $mysymbol)) and key('PointsBy_Id_SymbolName_FileName_LineNumber', concat(@CallerId, '#', $callersymbol, '#', $callerfile, '#', $callerline))]/@Count)"/>
                                    <xsl:if test="$total > 0">
                                        <tr>
                                            <td>
                                                <a><xsl:attribute name="href">#Point_<xsl:value-of select="$callerid"/></xsl:attribute>
                                                    <xsl:value-of select="$callersymbol"/><br/>
                                                    <xsl:value-of select="$callerfile"/>:<xsl:value-of select="$callerline"/>
                                                </a>
                                            </td>
                                            <td class="numeric">
                                                <xsl:value-of select="$total"/>
                                            </td>
                                        </tr>
                                    </xsl:if>
                                </xsl:if>
                            </xsl:for-each>
                        </table>

                        <h4>Code</h4>
                        <table>
                            <tr>
                                <th/>
                                <th colspan="2">Time spent</th>
                                <th/>
                                <th colspan="2">Callees</th>
                            </tr>
                            <tr>
                                <th>Line Number</th>
                                <th>Inclusive</th>
                                <th>Exclusive</th>
                                <th>Code</th>
                                <th>Symbol</th>
                                <th>Count</th>
                            </tr>
                            <xsl:for-each select="Line">
                                <xsl:variable name="linenumber" select="@Number"/>
                                <xsl:variable name="isinteresting" select="count(key('PointsBy_SymbolName_FileName_LineNumber', concat($mysymbol, '#', $filename, '#', $linenumber))) > 0"/>
                                <xsl:if test="$isinteresting or key('PointsBy_SymbolName_FileName', concat($mysymbol, '#', $filename))[(@LineNumber - $linenumber) &lt;= 3 and (@LineNumber - $linenumber) >= -3]">
                                    <tr>
                                        <td class="numeric">
                                            <!-- Insert anchors for people linking to this (by caller id). -->
                                            <xsl:if test="$isinteresting">
                                                <xsl:for-each select="key('PointsBy_SymbolName_FileName_LineNumber', concat($mysymbol, '#', $filename, '#', $linenumber))">
                                                    <a><xsl:attribute name="name">Point_<xsl:value-of select="@Id"/></xsl:attribute></a>
                                                </xsl:for-each>
                                            </xsl:if>
                                            <!-- Line number -->
                                            <xsl:value-of select="@Number"/>
                                        </td>
                                        <td class="numeric">
                                            <!-- Inclusive -->
                                            <xsl:if test="$isinteresting">
                                                <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[key('PointsBy_Id_SymbolName_FileName_LineNumber', concat(@CallerId, '#', $mysymbol, '#', $filename, '#', $linenumber))]/@Count)"/>
                                                <xsl:value-of select="$total"/>
                                            </xsl:if>
                                        </td>
                                        <td class="numeric">
                                            <!-- Exclusive -->
                                            <xsl:if test="$isinteresting">
                                                <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[@CalleeId = -1 and key('PointsBy_Id_SymbolName_FileName_LineNumber', concat(@CallerId, '#', $mysymbol, '#', $filename, '#', $linenumber))]/@Count)"/>
                                                <xsl:value-of select="$total"/>
                                            </xsl:if>
                                       </td>
                                        <td>
                                            <!-- Code -->
                                            <pre><xsl:value-of select="."/></pre>
                                        </td>
                                        <td colspan="2"/>
                                    </tr>
                                    <!--
                                        Show for this symbol+file+line the list of callees.
                                        A particular callee may be listed multiple times for this line of source,
                                        so sum these up for this line.
                                    -->
                                    <xsl:if test="$isinteresting">
                                        <xsl:for-each select="/ProffyResults/PointsEncountered/Point">
                                            <xsl:variable name="calleesymbol" select="@SymbolName"/>
                                            <xsl:variable name="calleeid" select="@Id"/>
                                            <xsl:if test="count(key('PointsBy_SymbolName', $calleesymbol)[@Id &lt; $calleeid]) = 0">
                                                <!--
                                                    This is the first point that represents this callee symbol, so
                                                    now sum the number of counters whose callee is the same symbol.
                                                -->
                                                <xsl:variable name="total" select="sum(/ProffyResults/CallCounters/Counter[key('PointsBy_Id_SymbolName', concat(@CalleeId, '#', $calleesymbol)) and  key('PointsBy_Id_SymbolName_FileName_LineNumber', concat(@CallerId, '#', $mysymbol, '#', $filename, '#', $linenumber))]/@Count)"/>
                                                <xsl:if test="$total > 0">
                                                <tr>
                                                    <td colspan="4"/>
                                                    <td class="detail">
                                                        <a><xsl:attribute name="href">#Symbol_<xsl:value-of select="$calleeid"/></xsl:attribute>
                                                            <xsl:value-of select="$calleesymbol"/>
                                                        </a>
                                                    </td>
                                                    <td class="numeric detail"><xsl:value-of select="$total"/></td>
                                                </tr>
                                                </xsl:if>
                                            </xsl:if>
                                        </xsl:for-each>
                                    </xsl:if>
                                </xsl:if>
                            </xsl:for-each>
                        </table>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
