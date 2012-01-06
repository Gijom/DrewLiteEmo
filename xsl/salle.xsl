<?xml version="1.0" encoding="iso-8859-1"?>
<!-- $Id: salle.xsl,v 1.1 2007/02/20 16:03:42 collins Exp $ -->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

	<xsl:output method="xml" omit-xml-declaration="yes" standalone="yes" encoding="utf-8"/>

	<xsl:param name="salle">Hall</xsl:param>

	<xsl:template match="/drew">

			<xsl:apply-templates/>

	</xsl:template>

	<xsl:template match="event">
		<xsl:if test="@room=$salle">
			<xsl:copy-of select="."/>
		</xsl:if>	
		<xsl:if test='descendant::room[@where=$salle]'>
			<xsl:copy-of select="."/>
		</xsl:if>
	</xsl:template>

	<xsl:template match="@*|node()"/>

</xsl:stylesheet>
