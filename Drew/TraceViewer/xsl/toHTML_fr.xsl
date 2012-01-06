<?xml version="1.0" encoding="iso-8859-1"?>
<!-- $Id: toHTML_fr.xsl,v 1.1 2007/02/20 16:03:41 collins Exp $ -->

<xsl:stylesheet version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:pws="http://www.euroscale.org/PWS/version-0.01" 
	xmlns:foo="foo" 
>


	<xsl:output method="html" omit-xml-declaration="yes" standalone="yes" indent="yes" encoding="utf-8"/>

<xsl:param name="user"/>

<!-- ###################################################################### -->
<xsl:template match="/drew">
<html>
<head>
<title><xsl:value-of select="normalize-space(//subject[1])"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<style>
body {	background-color: #cccccc ;
	background-repeat: repeat-y;
	margin-left:  5%;
	margin-right: 5%;
    	color: #336699;
    	font-family: arial;
	font-size: 10pt;
	font-weight: bold;
}

address {	font-style: italic;
		text-align: right}

applet {
	border: 1px #00a solid;
}

p.titre {text-align: center}

p.sig {
	font-size: 10pt;
	text-align: right;
	border-top: 1px dashed #114477;
	padding-top: 6px;
}

h1 {
	font-size:16pt;
	margin-left: -5%;
	padding-bottom: 12px;
	border-bottom: 2px solid #114477;
}

h2 {
	font-size: 14pt;
	margin-left:-3%;
}

h3 {
	font-size:14pt;
}

hr { 	color: #114477;
}

a[href] {	
	text-decoration: none;
	color: #114477;
	font-weight:bold;
}

a:hover[href] {
	color: #996699;
}       

span.object>a[href] {
	font-weight: normal ;
	color: inherit ;
}

span.object>a:hover[href] {
	font-weight: normal ;
	color: inherit ;
	background: #ffffcc ;
}

li {	padding-bottom: 0pt;
	padding-top: 0pt;
}

table {
	border: 1px solid #114477;
	border-spacing: 0 ;
	/*text-align:center;*/
	background-color: #dddddd ;
	width: 100% ;
}

.color {
	border-width:0px;
	background-color: #eeeeee ;
	text-align:center;
}

th, td {
	border-bottom: 1px solid #114477;
	border-spacing: 0 ;
	margin: 0 ;
	font-size: 10pt;
	vertical-align: top;
	padding-top: 0.25em ;
}

.text {
    	font-family: garamond;
	font-size: 12pt;
	text-align:justify;
	padding: 1em 1em ;
	vertical-align: top;
	color: #000000 ;
}

th {
	border-bottom: 1px solid #336699;
}

td li {
	text-align:left;
}

td.other {
        /* font-size:small; */
        color: #000000 ;
        font-style:italic;
	font-weight:normal;
	text-align:justify;
}

td.numbering {
	color: #ffffff ;
	background: #666666 ;
	font-weight: bold ;
	text-align: center ;
	vertical-align: middle ;
}

.grapheur {
        color:darkblue;
	text-align:left;
}

.chat {
        color: #008800 ;
	text-align:left ;
	font-style: italic ;
}

.edit {
	color: #0000ff ;
}

.move {
	color: #844756 ;
}

.delete {
	color: #cc0000 ;
}

.create {
	color: #00aa00 ;
}

.date {
	color: #000000 ;
	background: #bbbbbb ;
	font-weight: bold ;
	text-align: left ;
	vertical-align: top ;
	width: 5em ;
	padding-left: 0.5em ;
	padding-right: 0.5em ;
}

.identity {
	color: #888800 ;
	/*font-weight: bold ;*/
	font-style: italic ;
}

.server {
	color: #888888 ;
	background-color: #cccccc ;
	font-weight: bold :
}

.control {
	color: #888888 ;
	background-color: #cccccc ;
	font-weight: bold :
}

.object {
	color: inherit;
	font-weight: inherit ;
	font-style: inherit ;
	text-decoration: inherit ;
	border: solid 1px black ;
	padding-left: 0.5em ;
	padding-right: 0.5em ;
}
</style>
</head>
<body>
	<table>
		<xsl:apply-templates select="event"/>
	</table>
</body>
</html>
</xsl:template>

<!-- ###################################################################### -->
<xsl:template match="event">
	<tr><td class="numbering"><xsl:value-of select="position()"/></td><xsl:apply-templates/></tr>
</xsl:template>

<xsl:template match="time">
	<td class="date"><xsl:value-of select="foo:getDate(date)"/></td>
	<!--<td class="date"><xsl:value-of select="pws:getDate(date)"/></td>-->
	<!--<td class="date"><xsl:value-of select="date"/></td> -->
	<xsl:if test="@user">
		<td class="user"><xsl:value-of select="../@user"/></td>
	</xsl:if>
</xsl:template>

<!-- ### serveur .......................................................... -->
<xsl:template match="server">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="connection">
	<xsl:choose>
		<xsl:when test="@state='new'">
			<td colspan="2" class="server"><xsl:text>Connection de </xsl:text><span class="identity"><xsl:value-of select="identity/@user"/></span><xsl:text> dans la salle </xsl:text><xsl:value-of select="room/@where"/></td>
		</xsl:when>
		<xsl:when test="@state='end'">
			<td colspan="2" class="server"><xsl:text>Déconnection de </xsl:text><span class="identity"><xsl:value-of select="../../@user"/></span></td>
		</xsl:when>
	</xsl:choose>
</xsl:template>

<xsl:template match="syn">
	<td colspan="2" class="server"><span class="identity"><xsl:value-of select="../../@user"/></span><xsl:text> se synchronise.</xsl:text></td>
</xsl:template>

<!-- ### control .......................................................... -->
<xsl:template match="control">
	<td colspan="2" class="control"><xsl:value-of select="info"/></td>
</xsl:template>

<!-- ### chat ............................................................. -->
<xsl:template match="chat">
	<!-- td class="chat"><xsl:value-of select="../@user"/></td -->
<td class="chat"><xsl:text>CHAT</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text></td>
	<td class="chat"><xsl:value-of select="text"/></td>	
</xsl:template>

<!-- ### grapheur ......................................................... -->
<xsl:template match="grapheur">
	<td class="grapheur">
		<xsl:text>GRAPHEUR</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span>
		<xsl:if test="@player"><xsl:text> as </xsl:text><xsl:value-of select="@player"/></xsl:if>
		<xsl:text>)</xsl:text>
	</td>
	<td><xsl:apply-templates/></td>	
</xsl:template>

<xsl:template match="engaged">
	<xsl:choose>
		<xsl:when test="@status='true'">
			<xsl:text>s'engage</xsl:text>
		</xsl:when>
		<xsl:when test="@status='false'">
			<xsl:text>se désengage</xsl:text>
		</xsl:when>
	</xsl:choose>
</xsl:template>

<xsl:template match="argument">
	<xsl:choose>
		<xsl:when test="@edit">
			<span class="edit">
				<xsl:choose>
					<xsl:when test="@edit='true'"><xsl:text>débute l'édition </xsl:text></xsl:when>
					<xsl:otherwise><xsl:text>termine l'édition </xsl:text></xsl:otherwise>
				</xsl:choose>
				<xsl:text>de l'objet </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span>
			</span>
		</xsl:when>
		<xsl:when test="@action">
			<xsl:choose>
				<xsl:when test="@action='create'"><span class="create"><xsl:text>créé l'objet </xsl:text><span class="object"><a><xsl:attribute name="name"><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span></span></xsl:when>
				<xsl:when test='@action="delete"'><span class="delete"><xsl:text>supprime l'objet </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span></span></xsl:when>
			</xsl:choose>
			
		</xsl:when>
		<xsl:when test="@support">
			<span class="support">
				<xsl:choose>
					<xsl:when test="@support='true'"><xsl:text>est pour l'argument </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span></xsl:when>
					<xsl:otherwise><xsl:text>est contre l'argument </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span></xsl:otherwise>
				</xsl:choose>
			</span>
		</xsl:when>
	</xsl:choose>
	<xsl:choose>
		<xsl:when test="child::position and following-sibling::relation[@action='create']">
			<xsl:text>crée la relation </xsl:text>
			<span class="object"><a><xsl:attribute name="name"><xsl:value-of select="translate(@id,' ','')"/></xsl:attribute><xsl:value-of select="@id"/></a></span>
			<xsl:text> entre </xsl:text>
			<span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(../relation[1]/@from,' ','')"/></xsl:attribute><xsl:value-of select="translate(../relation[1]/@from,' ','')"/></a></span>
			<xsl:text> et </xsl:text>
			<span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(../relation[2]/@to,' ','')"/></xsl:attribute><xsl:value-of select="../relation[2]/@to"/></a></span>
		</xsl:when>
		<xsl:otherwise><xsl:apply-templates/></xsl:otherwise>
	</xsl:choose>
	<br/>
</xsl:template>

<xsl:template match="relation">
	<xsl:if test="not(preceding-sibling::argument and following-sibling::relation) and not(preceding-sibling::relation)">
	<span>
	<xsl:choose>
		<xsl:when test="@action='create'">
			<xsl:attribute name="class">create</xsl:attribute><xsl:text>crée une </xsl:text>
		</xsl:when>
		<xsl:otherwise>
			<xsl:attribute name="class">delete</xsl:attribute><xsl:text>supprime une </xsl:text>
		</xsl:otherwise>
	</xsl:choose>
	<b><xsl:text>relation</xsl:text></b><xsl:text> entre </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@from,' ','')"/></xsl:attribute><xsl:value-of select="@from"/></a></span><xsl:text> et </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(@to,' ','')"/></xsl:attribute><xsl:value-of select="@to"/></a></span>
	</span><br/>
	</xsl:if>
</xsl:template>

<xsl:template match="name">
	<span class="edit">
		<xsl:text>édite l'argument </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(../../argument/@id,' ','')"/></xsl:attribute><xsl:value-of select="../../argument/@id"/></a></span><xsl:text> : </xsl:text><br/>
		<b><xsl:text>nom : </xsl:text></b><xsl:value-of select="text"/><br/>
		<b><xsl:text>commentaire : </xsl:text></b><xsl:value-of select="../comment/text"/>
	</span>
</xsl:template>

<xsl:template match="comment">
</xsl:template>

<xsl:template match="position">
	<span class="move"><xsl:text>déplace l'objet </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(../../argument/@id,' ','')"/></xsl:attribute><xsl:value-of select="../../argument/@id"/></a></span><xsl:text> à la position </xsl:text><xsl:value-of select="@x"/><xsl:text>,</xsl:text><xsl:value-of select="@y"/></span>
</xsl:template>

<xsl:template match="size">
	<xsl:text>redimensionne l'objet </xsl:text><span class="object"><a><xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="translate(../../argument/@id,' ','')"/></xsl:attribute><xsl:value-of select="../../argument/@id"/></a></span><xsl:text> à la taille </xsl:text>
	<xsl:value-of select="@width"/><xsl:text>,</xsl:text><xsl:value-of select="@height"/>
</xsl:template>

<!-- ### trafficlight ..................................................... -->
<xsl:template match="traficlight">
	<td class="trafficlight">
		<xsl:text>Feu rouge / feu vert</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text>
	</td>
	<td class="trafficlight">
		<xsl:choose>
			<xsl:when test="count(red)!=0">
				<xsl:text>passe au rouge</xsl:text>
			</xsl:when>
			<xsl:when test="count(green)!=0">
				<xsl:text>passe au vert</xsl:text>
			</xsl:when>
			<xsl:otherwise>
				<xsl:text>n'a pas de couleur</xsl:text>
			</xsl:otherwise>
		</xsl:choose>
	</td>	
</xsl:template>

<!-- ### textboard ........................................................ -->
<!-- The 'fragment' element is not taken into accound yet.
  .. I haven't seen any use of it at that time...
  -->
<xsl:template match="textboard">
	<td class="textboard">
		<xsl:text>Textboard</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text>
	</td>
	<td class="textboard">
		<xsl:template match="text">
			écrit <span class="texte"><xsl:value-of select="text"/></span>
		</xsl:template>
		<xsl:template match="textBoardAction">
			<span class="texte"><xsl:value-of select="text"/></span>
		</xsl:template>
		<xsl:template match="textBoardInternalMsg">
		</xsl:template>

	</td>
</xsl:template>

<!-- ### whiteboard ....................................................... -->
<xsl:template match="whiteboard">
	<td class="whiteboard">
		<xsl:text>Tableau Blanc</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text>
	</td>
	<td class="whiteboard">
		<xsl:apply-templates/>
	</td>
</xsl:template>

<xsl:template match="svg">
	<xsl:apply-templates/>
</xsl:template>

<xsl:template match="clear">
	<xsl:text>efface le tableau</xsl:text>
</xsl:template>

<xsl:template match="line">
	<xsl:if test="not(boolean(preceding-sibling::text))">
		<xsl:text>trace une ligne </xsl:text>
		<xsl:if test="@stroke-width">
			<xsl:text>de largeur </xsl:text><xsl:value-of select="@stroke-width"/><xsl:text> et </xsl:text>
		</xsl:if>
		<xsl:text>de couleur </xsl:text>
		<xsl:call-template name="colorSpan">
			<xsl:with-param name="theColor"><xsl:value-of select="@stroke"/></xsl:with-param>
		</xsl:call-template>
		<xsl:text>entre les points (</xsl:text>
		<xsl:value-of select="@x1"/><xsl:text>,</xsl:text>
		<xsl:value-of select="@y1"/><xsl:text>) et (</xsl:text>
		<xsl:value-of select="@x2"/><xsl:text>,</xsl:text>
		<xsl:value-of select="@y2"/><xsl:text>)</xsl:text>
	</xsl:if>
</xsl:template>

<xsl:template name="colorSpan">
	<xsl:param name="theColor"/>
	<span>
		<xsl:attribute name="style">
			<xsl:text>color: #</xsl:text><xsl:value-of select="$theColor"/><xsl:text> ; background: #</xsl:text><xsl:value-of select="$theColor"/><xsl:text> ; padding: 0 ; </xsl:text>
		</xsl:attribute>
		<xsl:text>#</xsl:text><xsl:value-of select="$theColor"/>
	</span>
	<xsl:text> </xsl:text>
</xsl:template>

<xsl:template match="ellipse">
	<xsl:text>trace un rond </xsl:text>
	<xsl:if test="@stroke-width">
		<xsl:text>de largeur </xsl:text><xsl:value-of select="@stroke-width"/><xsl:text> et </xsl:text>
	</xsl:if>
	<xsl:text>de couleur </xsl:text>
	<xsl:call-template name="colorSpan">
		<xsl:with-param name="theColor"><xsl:value-of select="@stroke"/></xsl:with-param>
	</xsl:call-template>
	<xsl:if test="@fill">
		<xsl:text>rempli avec la couleur </xsl:text>
		<xsl:call-template name="colorSpan">
			<xsl:with-param name="theColor"><xsl:value-of select="@fill"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
	<xsl:text>centré en (</xsl:text>
	<xsl:value-of select="@x"/><xsl:text>,</xsl:text>
	<xsl:value-of select="@y"/><xsl:text>) et de rayons (</xsl:text>
	<xsl:value-of select="@rx"/><xsl:text>/</xsl:text>
	<xsl:value-of select="@ry"/><xsl:text>)</xsl:text>
</xsl:template>

<xsl:template match="rect">
	<xsl:text>trace un rectangle </xsl:text>
	<xsl:if test="@stroke-width">
		<xsl:text>de largeur </xsl:text><xsl:value-of select="@stroke-width"/><xsl:text> et </xsl:text>
	</xsl:if>
	<xsl:text>de couleur </xsl:text>
	<xsl:call-template name="colorSpan">
		<xsl:with-param name="theColor"><xsl:value-of select="@stroke"/></xsl:with-param>
	</xsl:call-template>
	<xsl:if test="@fill">
		<xsl:text>rempli avec la couleur </xsl:text>
		<xsl:call-template name="colorSpan">
			<xsl:with-param name="theColor"><xsl:value-of select="@fill"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
	<xsl:text>dont le coin en bas à gauche est (</xsl:text>
	<xsl:value-of select="@x"/><xsl:text>,</xsl:text>
	<xsl:value-of select="@y"/><xsl:text>), la hauteur est </xsl:text>
	<xsl:value-of select="@width"/><xsl:text> et la largeur est </xsl:text>
	<xsl:value-of select="@height"/>
</xsl:template>

<xsl:template match="polyline">
	<xsl:text>trace une ligne brisée </xsl:text>
	<xsl:if test="@stroke-width">
		<xsl:text>de largeur </xsl:text><xsl:value-of select="@stroke-width"/><xsl:text> et </xsl:text>
	</xsl:if>
	<xsl:text>de couleur </xsl:text>
	<xsl:call-template name="colorSpan">
		<xsl:with-param name="theColor"><xsl:value-of select="@stroke"/></xsl:with-param>
	</xsl:call-template>
	<xsl:if test="@fill">
		<xsl:text>rempli avec la couleur </xsl:text>
		<xsl:call-template name="colorSpan">
			<xsl:with-param name="theColor"><xsl:value-of select="@fill"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
	<xsl:text>dont les point sont (</xsl:text>
	<xsl:value-of select="@points"/><xsl:text>)</xsl:text>
</xsl:template>

<xsl:template match="text">
	<xsl:text>écrit </xsl:text><xsl:value-of select="text()"/><xsl:text> </xsl:text>
	<xsl:if test="following-sibling::line">
		<xsl:text> dans une flèche </xsl:text>
	</xsl:if>
	<xsl:if test="@fill">
		<xsl:text>remplie avec la couleur </xsl:text>
		<xsl:call-template name="colorSpan">
			<xsl:with-param name="theColor"><xsl:value-of select="@fill"/></xsl:with-param>
		</xsl:call-template>
	</xsl:if>
	<xsl:if test="@x">
		<xsl:text>à la position (</xsl:text>
		<xsl:value-of select="@x"/><xsl:text>,</xsl:text>
		<xsl:value-of select="@y"/><xsl:text>)</xsl:text>
	</xsl:if>
</xsl:template>
<!-- ### alex ............................................................. -->
<xsl:template match="alex">
	<td class="alex">
		<xsl:text>Alex</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text>
	</td>
	<td class="alex">
		<span class="alexTemplate"><xsl:value-of select="text()"/></span>
	</td>
</xsl:template>

<!-- ### vote ............................................................. -->
<xsl:template match="vote">
	<td class="vote">
		<xsl:text>Vote</xsl:text><br/><xsl:text>(</xsl:text><span class="identity"><xsl:value-of select="../@user"/></span><xsl:text>)</xsl:text>
	</td>
	<td class="vote">
		<xsl:choose>
			<xsl:when test="text()='-1'">
				supprime sa voix.
			</xsl:when>
			<xsl:otherwise>
				donne <xsl:value-of select="text()"/>
			</xsl:otherwise>
		</xsl:choose>
	</td>
</xsl:template>


</xsl:stylesheet>
