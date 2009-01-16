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
        name="PointsBy_SymbolName"
        match="/ProffyResults/PointsEncountered/Point"
        use="@SymbolName"/>

    <xsl:key
        name="PointsBy_Id_SymbolName_FileName_LineNumber"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@Id, '#', @SymbolName, '#', @FileName, '#', @LineNumber)"/>

    <xsl:key
        name="PointsBy_SymbolName_FileName"
        match="/ProffyResults/PointsEncountered/Point"
        use="concat(@SymbolName, '#', @FileName)"/>

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
                <xsl:apply-templates select="PointsEncountered"/>
            </body>
        </html>
    </xsl:template>

    <xsl:template match="Counter" name="CallerCounters">
        <xsl:param name="symbol"/>
        <xsl:param name="filename"/>
        <xsl:param name="linenumber"/>
        <xsl:param name="docallers" select="0"/>
        <xsl:param name="docallees" select="0"/>

        <xsl:if test="$docallers = 1">
            <xsl:variable name="calleeid" select="@CalleeId"/>

            <xsl:if test="count(key('PointsBy_Id_SymbolName_FileName_LineNumber', concat($calleeid, '#', $symbol, '#', $filename, '#', $linenumber))) > 0">
                <a><xsl:attribute name="href">#<xsl:value-of select="@CallerId"/></xsl:attribute><xsl:value-of select="@CallerId"/>:<xsl:value-of select="@Count"/></a><br/>
            </xsl:if>
        </xsl:if>

        <xsl:if test="$docallees = 1">
            <xsl:variable name="callerid" select="@CallerId"/>

            <xsl:if test="count(key('PointsBy_Id_SymbolName_FileName_LineNumber', concat($callerid, '#', $symbol, '#', $filename, '#', $linenumber))) > 0">
                <a><xsl:attribute name="href">#<xsl:value-of select="@CalleeId"/></xsl:attribute><xsl:value-of select="@CalleeId"/>:<xsl:value-of select="@Count"/></a><br/>
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template match="Point">
        <xsl:param name="symbol"/>
        <xsl:param name="filename"/>
        <xsl:param name="linenumber"/>
        <xsl:if test="@SymbolName = $symbol and @FileName = $filename and @LineNumber = $linenumber">
            <a><xsl:attribute name="name"><xsl:value-of select="@Id"/></xsl:attribute></a>
        </xsl:if>
    </xsl:template>

    <xsl:template match="PointsEncountered">
        <xsl:for-each select="Point">
            <xsl:variable name="mysymbol" select="@SymbolName"/>
            <xsl:variable name="myid" select="@Id"/>
            <xsl:if test="count(key('PointsBy_SymbolName', $mysymbol)[@Id &lt; $myid]) = 0">
                <h2>
                    <xsl:value-of select="@SymbolName"/>
                </h2>
                <xsl:for-each select="/ProffyResults/Files/File">
                    <xsl:variable name="filename" select="@Name"/>
                    <xsl:if test="count(key('PointsBy_SymbolName_FileName', concat($mysymbol, '#', $filename))) > 0">
                        <h3>
                            <xsl:value-of select="@Name"/>
                        </h3>
                        <table>
                            <tr>
                                <th>Line Number</th>
                                <th>Callers</th>
                                <th>Callees</th>
                                <th>Code</th>
                            </tr>
                            <xsl:for-each select="Line">
                                <xsl:variable name="linenumber" select="@Number"/>
                                <xsl:if test="count(key('PointsBy_SymbolName_FileName', concat($mysymbol, '#', $filename))[(@LineNumber - $linenumber) &lt;= 3 and (@LineNumber - $linenumber) >= -3]) > 0">
                                    <tr>
                                        <td>
                                            <xsl:apply-templates select="/ProffyResults/PointsEncountered/Point">
                                                <xsl:with-param name="symbol" select="$mysymbol"/>
                                                <xsl:with-param name="filename" select="$filename"/>
                                                <xsl:with-param name="linenumber" select="$linenumber"/>
                                            </xsl:apply-templates>
                                            <xsl:value-of select="@Number"/>
                                        </td>
                                        <td>
                                            <xsl:apply-templates select="/ProffyResults/CallCounters/Counter">
                                                <xsl:with-param name="symbol" select="$mysymbol"/>
                                                <xsl:with-param name="filename" select="$filename"/>
                                                <xsl:with-param name="linenumber" select="$linenumber"/>
                                                <xsl:with-param name="docallers" select="1"/>
                                            </xsl:apply-templates>
                                        </td>
                                        <td>
                                            <xsl:apply-templates select="/ProffyResults/CallCounters/Counter">
                                                <xsl:with-param name="symbol" select="$mysymbol"/>
                                                <xsl:with-param name="filename" select="$filename"/>
                                                <xsl:with-param name="linenumber" select="$linenumber"/>
                                                <xsl:with-param name="docallees" select="1"/>
                                            </xsl:apply-templates>
                                        </td>
                                        <td>
                                            <pre><xsl:value-of select="."/></pre>
                                        </td>
                                    </tr>
                                </xsl:if>
                            </xsl:for-each>
                        </table>
                    </xsl:if>
                </xsl:for-each>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
