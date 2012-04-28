<?xml version="1.0" encoding="UTF-8"?>
<!--
    Copyright (c) 2009, 2010, 2012 Paul Richards <paul.richards@gmail.com>

    Permission to use, copy, modify, and distribute this software for any
    purpose with or without fee is hereby granted, provided that the above
    copyright notice and this permission notice appear in all copies.

    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
-->
<xsl:stylesheet
  version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns="http://www.w3.org/1999/xhtml">
    <xsl:output
      method="xml"
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
        name="PointsBy_SymbolName_FileName"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@SymbolName, '#', @FileName)"/>

    <xsl:key
        name="PointsBy_SymbolName_FileName_LineNumber"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@SymbolName, '#', @FileName, '#', @LineNumber)"/>

    <xsl:key
        name="CountersBy_CallerSymbol"
        match="/ProffyResults/CallCounters/Counter"
        use="key('PointsBy_Id', @CallerId)/@SymbolName"/>

    <xsl:key
        name="CountersBy_CallerSymbol_CallerFileName_CallerLineNumber"
        match="/ProffyResults/CallCounters/Counter"
        use="concat(key('PointsBy_Id', @CallerId)/@SymbolName, '#', key('PointsBy_Id', @CallerId)/@FileName, '#', key('PointsBy_Id', @CallerId)/@LineNumber)"/>

    <xsl:key
        name="CountersBy_CalleeSymbol_CallerSymbol_CallerFileName_CallerLineNumber"
        match="/ProffyResults/CallCounters/Counter"
        use="concat(key('PointsBy_Id', @CalleeId)/@SymbolName, '#', key('PointsBy_Id', @CallerId)/@SymbolName, '#', key('PointsBy_Id', @CallerId)/@FileName, '#', key('PointsBy_Id', @CallerId)/@LineNumber)"/>

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
                <th>Title</th>
                <td><xsl:value-of select="Title"/></td>
            </tr>
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
                <xsl:sort select="sum(key('CountersBy_CallerSymbol', @SymbolName)/@Count)" data-type="number" order="descending"/>
                <xsl:variable name="mysymbol" select="@SymbolName"/>
                <xsl:variable name="myid" select="@Id"/>
                <xsl:if test="count(key('PointsBy_SymbolName', $mysymbol)[@Id &lt; $myid]) = 0">
                    <tr>
                        <td class="numeric">
                            <xsl:variable name="total" select="sum(key('CountersBy_CallerSymbol', $mysymbol)/@Count)"/>
                            <xsl:value-of select="$total"/>
                        </td>
                        <td class="numeric">
                            <xsl:variable name="total" select="sum(key('CountersBy_CallerSymbol', $mysymbol)[@CalleeId = -1]/@Count)"/>
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
                                    <xsl:variable name="total" select="sum(key('CountersBy_CalleeSymbol_CallerSymbol_CallerFileName_CallerLineNumber', concat($mysymbol, '#', $callersymbol, '#', $callerfile, '#', $callerline))/@Count)"/>
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
                                                <xsl:variable name="total" select="sum(key('CountersBy_CallerSymbol_CallerFileName_CallerLineNumber', concat($mysymbol, '#', $filename, '#', $linenumber))/@Count)"/>
                                                <xsl:value-of select="$total"/>
                                            </xsl:if>
                                        </td>
                                        <td class="numeric">
                                            <!-- Exclusive -->
                                            <xsl:if test="$isinteresting">
                                                <xsl:variable name="total" select="sum(key('CountersBy_CallerSymbol_CallerFileName_CallerLineNumber', concat($mysymbol, '#', $filename, '#', $linenumber))[@CalleeId = -1]/@Count)"/>
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
                                                <xsl:variable name="total" select="sum(key('CountersBy_CalleeSymbol_CallerSymbol_CallerFileName_CallerLineNumber', concat($calleesymbol, '#', $mysymbol, '#', $filename, '#', $linenumber))/@Count)"/>
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
