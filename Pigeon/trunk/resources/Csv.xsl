<?xml version="1.0" encoding="UTF-8"?>
<!--
    Copyright (C) 2008  Paul Richards.

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
        method="text"
        media-type="text/csv"
        />

    <xsl:variable name="containsClub" select="count(//MemberList/Member/Club) > 0"/>
    <xsl:variable name="containsSection" select="count(//MemberList/Member/Section) > 0"/>
    <xsl:variable name="newline">
        <xsl:text>
</xsl:text>
    </xsl:variable>

    <xsl:template match="/MembersReport"><xsl:apply-templates select="MemberList"/></xsl:template>
    
    <xsl:template match="MemberList">
        <xsl:text>Name,</xsl:text>
        <xsl:if test="$containsClub"><xsl:text>Club,</xsl:text></xsl:if>
        <xsl:if test="$containsSection"><xsl:text>Section,</xsl:text></xsl:if>
        <xsl:text>Address,Telephone,SHU Number</xsl:text>
        <xsl:value-of select="$newline"/>
        <xsl:apply-templates select="Member"/>
    </xsl:template>
    
    <xsl:template match="Member">
        <xsl:value-of select="Name"/>
        <xsl:text>,</xsl:text>
        <xsl:if test="$containsClub"><xsl:value-of select="Club"/><xsl:text>,</xsl:text></xsl:if>
        <xsl:if test="$containsSection"><xsl:value-of select="Section"/><xsl:text>,</xsl:text></xsl:if>
        <xsl:text>"</xsl:text><xsl:apply-templates select="Address/Line"/><xsl:text>",</xsl:text>
        <xsl:value-of select="Telephone"/><xsl:text>,</xsl:text>
        <xsl:value-of select="ShuNumber"/>
        <xsl:value-of select="$newline"/>
    </xsl:template>
    
    <xsl:template match="Line">
        <xsl:value-of select="."/>
        <xsl:if test="count(following-sibling::Line) > 0"><xsl:value-of select="$newline"/></xsl:if>
    </xsl:template>
</xsl:stylesheet>
