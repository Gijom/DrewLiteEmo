<?xml version="1.0" encoding="iso-8859-1"?>
<!-- $Id: toSYLK_en.xsl,v 1.1 2007/02/20 16:03:41 collins Exp $ -->

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:pws="foo" 
>

<xsl:output method="text" indent="no" encoding="iso-8859-1" omit-xml-declaration="yes"/>

<xsl:strip-space elements="*"/>

<!-- Header of the file (required) -->
<!-- ############################# -->
<xsl:template match="/">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="drew">
<xsl:text>ID;P</xsl:text><xsl:value-of select="normalize-space(//subject[1])"/><xsl:text>;N;E
P;PGeneral
P;Ph:mm:ss
F;P0;DG0G10;M264
B;Y</xsl:text><xsl:value-of select="count(//event)"/><xsl:text>;X5;D0 0 0 4
O;L;D;V0;K47;G100 0.001
F;W1 2 15
F;W3 3 45
F;W4 5 15
F;W6 6 11
F;P1;FG0G;C1
F;P0;FG0C;SDSM8;M312;R1
F;P0;FG0C;Y1;X1
C;K"Heure"
F;X2
C;K"Speaker"
F;X3
C;K"Utterance"
F;X4
C;K"Who"
F;X5
C;K"Tool"
</xsl:text>
<xsl:apply-templates select="event"/>
<xsl:text>E
</xsl:text>
</xsl:template>

<!-- An event: every action is an event -->
<!-- ################################## -->
<xsl:template match="event">
<xsl:apply-templates>
<xsl:with-param name="pos" select="position()+1"/>
<xsl:with-param name="who">
<xsl:choose>
<xsl:when test="@user=''">Control</xsl:when>
<xsl:otherwise><xsl:value-of select="@user"/></xsl:otherwise>
</xsl:choose>
</xsl:with-param>
</xsl:apply-templates>
</xsl:template>

<!-- Time (milliseconds since 1970/01/01) -->
<!-- ###################################  -->
<xsl:template match="time">
<xsl:param name="pos">0</xsl:param>
<!-- First cell: Heure -->
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X1
C;K"</xsl:text><xsl:value-of select="pws:getSlkDate(date)"/><xsl:text>"</xsl:text>
<xsl:text>
</xsl:text>
<!-- Second cell: Speaker -->
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X2
</xsl:text>
<xsl:choose>
<xsl:when test="following-sibling::server or following-sibling::control">
<xsl:text>C;K"---"
</xsl:text>
</xsl:when>
<xsl:otherwise>
<xsl:text>C;K"</xsl:text><xsl:value-of select="normalize-space(../@user)"/><xsl:text>"
</xsl:text>
</xsl:otherwise>
</xsl:choose>
</xsl:template>

<!-- Here are the "administrative" parts: server and so -->
<!-- ################################################## -->
<!-- ### serveur .......................................................... -->
<xsl:template match="server">
<xsl:param name="pos">0</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
</xsl:text>
<xsl:apply-templates>
<xsl:with-param name="pos" select="$pos"/>
</xsl:apply-templates>
</xsl:template>

<xsl:template match="connection">
<xsl:param name="pos">0</xsl:param>
<xsl:choose>
<xsl:when test="@state='new'">
<xsl:text>C;K"Connection of </xsl:text><xsl:value-of select="identity/@user"/><xsl:text xsl:lang="en"> in room </xsl:text><xsl:value-of select="room/@where"/><xsl:text>"
</xsl:text>
</xsl:when>
<xsl:when test="@state='end'">
<xsl:text>C;K"Deconnection of </xsl:text><xsl:value-of select="../../@user"/><xsl:text>"
</xsl:text>
</xsl:when>
</xsl:choose>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"Server"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"Control"
</xsl:text>
</xsl:template>

<xsl:template match="syn">
<xsl:param name="pos">0</xsl:param>
<xsl:text>C;K"</xsl:text><xsl:value-of select="../../@user"/><xsl:text xsl:lang="en"> synchronizes."
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"Server"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"Control"
</xsl:text>
</xsl:template>

<!-- ### control .......................................................... -->
<xsl:template match="control">
<xsl:param name="pos">0</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:value-of select="normalize-space(info)"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"Server"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"Control"
</xsl:text>
</xsl:template>

<!-- Here we find the templates about the drew tools -->
<!-- ############################################### -->
<!-- ### chat ............................................................. -->
<xsl:template match="chat">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:value-of select="normalize-space(text)"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text><xsl:value-of select="$who"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"chat"
</xsl:text>
</xsl:template>

<!-- ### grapheur ......................................................... -->
<xsl:template match="grapheur">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:apply-templates><xsl:with-param name="who" select="$who"/></xsl:apply-templates><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text>
<xsl:value-of select="$who"/><xsl:if test="@player"><xsl:text> (</xsl:text><xsl:value-of select="@player"/><xsl:text>)</xsl:text></xsl:if>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"grapher"
</xsl:text>
</xsl:template>

<xsl:template match="engaged">
<xsl:choose>
<xsl:when test="@status='true'">
<xsl:text xsl:lang="en">participates</xsl:text>
</xsl:when>
<xsl:when test="@status='false'">
<xsl:text xsl:lang="en">just listens</xsl:text>
</xsl:when>
</xsl:choose>
</xsl:template>

<xsl:template match="argument">
<xsl:choose>
<xsl:when test="@edit">
<xsl:choose>
<xsl:when test="@edit='true'"><xsl:text xsl:lang="en">starts editing </xsl:text></xsl:when>
<xsl:when test="@edit='false'"><xsl:text xsl:lang="en">stops editing </xsl:text></xsl:when>
</xsl:choose>
<xsl:text xsl:lang="en">argument </xsl:text><xsl:value-of select="@id"/>
</xsl:when>
<xsl:when test="@action">
<xsl:choose>
<xsl:when test="@action='create'">
<xsl:text xsl:lang="en">creates the </xsl:text>
<xsl:choose>
<xsl:when test="@type='box'"><xsl:text xsl:lang="en">box </xsl:text></xsl:when>
<xsl:when test="@type='arrow'"><xsl:text xsl:lang="en">arrow </xsl:text></xsl:when>
<xsl:otherwise><xsl:text xsl:lang="en">unknown thing! </xsl:text></xsl:otherwise>
</xsl:choose>
</xsl:when>
<xsl:when test="@action='delete'"><xsl:text xsl:lang="en">deletes the object </xsl:text></xsl:when>
</xsl:choose>
<xsl:value-of select="@id"/>
</xsl:when>
<xsl:when test="@support">
<xsl:choose>
<xsl:when test="@support='true'"><xsl:text xsl:lang="en">is pro </xsl:text></xsl:when>
<xsl:when test="@support='false'"><xsl:text xsl:lang="en">is against </xsl:text></xsl:when>
<xsl:otherwise><xsl:text xsl:lang="en">is neither pro or against </xsl:text></xsl:otherwise>
</xsl:choose><xsl:text xsl:lang="en">the argument </xsl:text><xsl:value-of select="@id"/>
</xsl:when>
</xsl:choose>
<xsl:choose>
<xsl:when test="child::position and following-sibling::relation[@action='create']">
<xsl:text xsl:lang="en">creates the link </xsl:text><xsl:value-of select="@id"/>
<xsl:text xsl:lang="en"> between </xsl:text><xsl:value-of select="translate(../relation[1]/@from,' ','')"/>
<xsl:text xsl:lang="en"> and </xsl:text><xsl:value-of select="../relation[2]/@to"/>
</xsl:when>
<xsl:otherwise>
<xsl:apply-templates>
<xsl:with-param name="id" select="@id"/>
</xsl:apply-templates>
</xsl:otherwise>
</xsl:choose>
</xsl:template>

<xsl:template match="relation">
<xsl:if test="not(preceding-sibling::argument and following-sibling::relation) and not(preceding-sibling::relation)">
<xsl:choose>
<xsl:when test="@action='create'"><xsl:text xsl:lang="en">creates</xsl:text></xsl:when>
<xsl:otherwise><xsl:text xsl:lang="en">deletes</xsl:text></xsl:otherwise>
</xsl:choose>
<xsl:text xsl:lang="en"> a link between </xsl:text><xsl:value-of select="@from"/><xsl:text xsl:lang="en"> and </xsl:text><xsl:value-of select="@to"/>
</xsl:if>
</xsl:template>

<xsl:template match="name">
<xsl:param name="id">0</xsl:param>
<xsl:text xsl:lang="en">edits the argument </xsl:text><xsl:value-of select="$id"/><xsl:text xsl:lang="en"> : Name = </xsl:text><xsl:value-of select="normalize-space(text)"/><xsl:text xsl:lang="en"> Comment = </xsl:text><xsl:value-of select="normalize-space(../comment/text)"/>
</xsl:template>

<xsl:template match="comment">
</xsl:template>

<xsl:template match="position">
<xsl:param name="id">0</xsl:param>
<xsl:text xsl:lang="en">moves the object </xsl:text><xsl:value-of select="$id"/><xsl:text xsl:lang="en"> to </xsl:text><xsl:value-of select="@x"/><xsl:text>,</xsl:text><xsl:value-of select="@y"/>
</xsl:template>

<xsl:template match="size">
<xsl:param name="id">0</xsl:param>
<xsl:text xsl:lang="en">resizes the object </xsl:text><xsl:value-of select="$id"/><xsl:text xsl:lang="en"> to </xsl:text>
<xsl:value-of select="@width"/><xsl:text>,</xsl:text><xsl:value-of select="@height"/>
</xsl:template>

<!-- ### trafficlight ..................................................... -->
<xsl:template match="traficlight">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"trafficlight </xsl:text>
<xsl:choose>
<xsl:when test="count(red)!=0"><xsl:text xsl:lang="en">becomes red</xsl:text></xsl:when>
<xsl:when test="count(green)!=0"><xsl:text xsl:lang="en">becomes green</xsl:text></xsl:when>
<xsl:otherwise><xsl:text xsl:lang="en">is not activated yet</xsl:text></xsl:otherwise>
</xsl:choose>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text>
<xsl:value-of select="$who"/><xsl:if test="@player"><xsl:text> (</xsl:text><xsl:value-of select="@player"/><xsl:text>)</xsl:text></xsl:if>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"trafficlight"
</xsl:text>
</xsl:template>

<!-- ### textboard ........................................................ -->
<!-- The 'fragment' element is not taken into accound yet.
.. I haven't seen any use of it at that time...
-->
<xsl:template match="textboard">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:value-of select="text"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text><xsl:value-of select="$who"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"textboard"
</xsl:text>
</xsl:template>

<!-- ### whiteboard ....................................................... -->
<xsl:template match="whiteboard">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:apply-templates><xsl:with-param name="who" select="$who"/></xsl:apply-templates><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text>
<xsl:value-of select="$who"/><xsl:if test="@player"><xsl:text> (</xsl:text><xsl:value-of select="@player"/><xsl:text>)</xsl:text></xsl:if>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"whiteboard"
</xsl:text>
</xsl:template>

<xsl:template match="svg">
<xsl:apply-templates/>
</xsl:template>

<xsl:template match="clear">
<xsl:text xsl:lang="en">clears the whiteboard</xsl:text>
</xsl:template>

<xsl:template match="line">
<xsl:if test="not(boolean(preceding-sibling::text))">
<xsl:text xsl:lang="en">draws a line </xsl:text>
<xsl:if test="@stroke-width">
<xsl:text xsl:lang="en">of width </xsl:text>
<xsl:value-of select="@stroke-width"/><xsl:text xsl:lang="en"> and </xsl:text>
</xsl:if>
<xsl:text xsl:lang="en">with colour </xsl:text><xsl:value-of select="@stroke"/>
<xsl:text xsl:lang="en"> between points (</xsl:text>
<xsl:value-of select="@x1"/><xsl:text>,</xsl:text>
<xsl:value-of select="@y1"/><xsl:text xsl:lang="en">) and (</xsl:text>
<xsl:value-of select="@x2"/><xsl:text>,</xsl:text>
<xsl:value-of select="@y2"/><xsl:text>)</xsl:text>
</xsl:if>
</xsl:template>

<xsl:template match="ellipse">
<xsl:text xsl:lang="en">draws a circle </xsl:text>
<xsl:if test="@stroke-width">
<xsl:text xsl:lang="en">of width </xsl:text>
<xsl:value-of select="@stroke-width"/>
<xsl:text xsl:lang="en"> and </xsl:text>
</xsl:if>
<xsl:text xsl:lang="en">with colour </xsl:text>
<xsl:value-of select="@stroke"/>
<xsl:if test="@fill">
<xsl:text xsl:lang="en"> filled with colour </xsl:text><xsl:value-of select="@fill"/>
</xsl:if>
<xsl:text xsl:lang="en"> centrered at (</xsl:text>
<xsl:value-of select="@x"/><xsl:text>,</xsl:text>
<xsl:value-of select="@y"/>
<xsl:text xsl:lang="en">) which radius is (</xsl:text>
<xsl:value-of select="@rx"/><xsl:text>/</xsl:text>
<xsl:value-of select="@ry"/><xsl:text>)</xsl:text>
</xsl:template>

<xsl:template match="rect">
<xsl:text xsl:lang="en">draws a rectangle </xsl:text>
<xsl:if test="@stroke-width">
<xsl:text xsl:lang="en">of width </xsl:text>
<xsl:value-of select="@stroke-width"/><xsl:text xsl:lang="en"> and </xsl:text>
</xsl:if>
<xsl:text xsl:lang="en">with colour </xsl:text>
<xsl:value-of select="@stroke"/>
<xsl:if test="@fill">
<xsl:text xsl:lang="en"> filled with colour </xsl:text><xsl:value-of select="@fill"/>
</xsl:if>
<xsl:text xsl:lang="en"> whose lower left corner position is (</xsl:text>
<xsl:value-of select="@x"/><xsl:text>,</xsl:text>
<xsl:value-of select="@y"/><xsl:text xsl:lang="en">), height is </xsl:text>
<xsl:value-of select="@width"/><xsl:text xsl:lang="en"> and width is </xsl:text>
<xsl:value-of select="@height"/>
</xsl:template>

<xsl:template match="polyline">
<xsl:text xsl:lang="en">draws a broken line </xsl:text>
<xsl:if test="@stroke-width"><xsl:text xsl:lang="en">of width </xsl:text><xsl:value-of select="@stroke-width"/>
<xsl:text xsl:lang="en"> and </xsl:text>
</xsl:if>
<xsl:text xsl:lang="en">with colour </xsl:text><xsl:value-of select="@stroke"/>
<xsl:if test="@fill"><xsl:text xsl:lang="en"> filled with colour </xsl:text><xsl:value-of select="@fill"/></xsl:if>
<xsl:text xsl:lang="en"> whose points are (</xsl:text>
<xsl:value-of select="@points"/><xsl:text>)</xsl:text>
</xsl:template>

<xsl:template match="text">
<xsl:text xsl:lang="en">writes </xsl:text><xsl:value-of select="text()"/><xsl:text> </xsl:text>
<xsl:if test="following-sibling::line">
<xsl:text xsl:lang="en"> in an arrow </xsl:text>
</xsl:if>
<xsl:if test="@fill">
<xsl:text xsl:lang="en">filled with colour </xsl:text><xsl:value-of select="@fill"/>
</xsl:if>
<xsl:if test="@x">
<xsl:text xsl:lang="en"> at position (</xsl:text><xsl:value-of select="@x"/><xsl:text>,</xsl:text><xsl:value-of select="@y"/><xsl:text>)</xsl:text>
</xsl:if>
</xsl:template>

<!-- ### alex ............................................................. -->
<xsl:template match="alex">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text><xsl:value-of select="text()"/><xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text>
<xsl:value-of select="$who"/><xsl:if test="@player"><xsl:text> (</xsl:text><xsl:value-of select="@player"/><xsl:text>)</xsl:text></xsl:if>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"alex"
</xsl:text>
</xsl:template>

<!-- ### vote ............................................................. -->
<xsl:template match="vote">
<xsl:param name="pos">0</xsl:param>
<xsl:param name="who">??</xsl:param>
<xsl:text>F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X3
C;K"</xsl:text>
<xsl:choose>
<xsl:when test="text()='-1'"><xsl:text xsl:lang="en">remove its poll. </xsl:text></xsl:when>
<xsl:otherwise><xsl:text xsl:lang="en">gives </xsl:text><xsl:value-of select="text()"/></xsl:otherwise>
</xsl:choose>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X4
C;K"</xsl:text>
<xsl:value-of select="$who"/><xsl:if test="@player"><xsl:text> (</xsl:text><xsl:value-of select="@player"/><xsl:text>)</xsl:text></xsl:if>
<xsl:text>"
F;P1;FG0C;Y</xsl:text><xsl:value-of select="$pos"/><xsl:text>;X5
C;K"vote"
</xsl:text>
</xsl:template>

</xsl:stylesheet>
