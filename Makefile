#####################################
#
# $Id: Makefile,v 1.52 2007/02/20 16:03:41 collins Exp $
# 
#####################################
##      CUSTOMIZATION SECTION      ##

#JAVA = /bin/java/bin
#JAVACLASSES     = $(JAVA)/Classes

# Theses commands have to be set
#JAVAC   =       $(JAVA)/javac
#JAVADOC =       $(JAVA)/javadoc
#JAR =           $(JAVA)/jar

# normal javac commande (use any java compile 1.4+ )
JAVAC   =	javac
# for local emse use to compile with java 1.4
#JAVAC   =       javac4
JAVADOC =       javadoc
JAR =           jar

# To handle encoding , if files are UTF-8, use LANG=UTF-8
LANG =

##  END OF CUSTOMIZATION SECTION   ##
#####################################

MYSOURCEPATH = 	.
MYCLASSPATH = 	../classes:
MYDOCPATH = 	../Doc/programmation

# Where to find the binary classes required for compilation (*.class)
#CLASSPATH = 	$(MYCLASSPATH):$(JAVACLASSES)

# Flags of JAVAC and JAVADOC
#JAVACFLAGS = 	-d $(MYCLASSPATH) -classpath $(CLASSPATH)
JAVACFLAGS =    -encoding ISO-8859-1 -deprecation -classpath .:lib/jfreechart-1.0.3.jar:lib/jcommon-1.0.6.jar
#JAVACFLAGS =    -target 1.1 
#JAVACFLAGS =    -target 1.1 -encoding UTF8
JAVADOCFLAGS = 	-encoding ISO-8859-1 -private -verbose -d $(MYDOCPATH)  -sourcepath $(MYSOURCEPATH):$(JAVACLASSES)

#####################################

.SUFFIXES: .java .class

.java.class:
	$(LANG) $(JAVAC) $(JAVACFLAGS) $<

PACKAGES=	Drew.Util \
		Drew.Util.XMLmp \
		Drew.Serveur \
		Drew.Serveur.Control \
		Drew.Serveur.Raw \
		Drew.Serveur.HTTP \
		Drew.Locale.Serveur \
		Drew.Locale.Client \
		Drew.Client.Util \
		Drew.Client.Util.XML \
		Drew.Client.Util.XMLSocket \
		Drew.Client.GEW \
		Drew.Client.EmotionAwareness \
		Drew.Client.Chat \
		Drew.Client.TableauDeBord \
		Drew.Client.TextBoard \
		Drew.Client.FeuTricolore \
		Drew.Client.Grapheur \
		Drew.Client.WhiteBoard \
		Drew.Client.MultiModules \
		Drew.Client.Rejoueur \
		Drew.Client.MiSc\
		Drew.Client.Dict \
		Drew.Client.Vote \
		Drew.Client.App \
		Drew.Client.ViewBoard \
		Drew.Client.SimpleAwareness \
		Drew.Client.ImageViewer \
		Sch \
		alex \
		org.dict.client \
		org.jfree \
		Drew.TraceViewer 

CLOCALEFILES=	$(wildcard Drew/Locale/Client/*.java)

SLOCALEFILES=	$(wildcard Drew/Locale/Serveur/*.java)

SERVEURFILES=	$(wildcard Drew/Serveur/*.java \
			   Drew/Locale/Serveur/*.java \
			   Drew/Serveur/Control/*.java \
			   Drew/Serveur/HTTP/*.java \
			   Drew/Serveur/Raw/*.java \
			   org/dict/client/*.java)

UTILFILES=	$(wildcard Drew/Util/*.java)

XMLMPFILES=	$(wildcard Drew/Util/XMLmp/*.java )

SCHEMEFILES=	$(wildcard Sch/*.java )

GEWFILES=	$(wildcard Drew/Client/GEW/*.java)

EMOTIONAWARENESSFILES = $(wildcard Drew/Client/EmotionAwareness/*.java)

CHATFILES=	$(wildcard Drew/Client/Chat/*.java)

FEUFILES=	$(wildcard Drew/Client/FeuTricolore/*.java )

DICTFILE=       $(wildcard Drew/Client/Dict/*.java org/dict/client/*.java)

GRAPHEURFILES=	$(wildcard Drew/Client/Grapheur/*.java )

MISCFILES=	$(wildcard Drew/Client/MiSc/*.java)

REJOUEURFILES=	$(wildcard Drew/Client/Rejoueur/*.java )

TABLEAUFILES=	$(wildcard Drew/Client/TableauDeBord/*.java )

TEXTFILES=	$(wildcard Drew/Client/TextBoard/*.java)

MULTIFILES=	$(wildcard Drew/Client/MultiModules/*.java)

CL_UTILFILES=	$(wildcard Drew/Client/Util/*.java Drew/Client/Util/XMLSocket/*.java Drew/Client/Util/XML/*.java)

WHITEBOARDFILES=$(wildcard Drew/Client/WhiteBoard/*.java)

VOTEFILE=       $(wildcard Drew/Client/Vote/*.java)

ALEXFILE=       $(wildcard alex/*.java)

APPFILE=        $(wildcard Drew/Client/App/*.java)

VIEWFILES=	$(wildcard Drew/Client/ViewBoard/*.java)

AWARENESSFILES=	$(wildcard Drew/Client/SimpleAwareness/*.java)

IMAGEVIEWERFILES= $(wildcard Drew/Client/ImageViewer/*.java)

TRACEFILES=	$(wildcard Drew/TraceViewer/*.java)

targets:
	@echo "Specify the target of make :"
	@echo "    make drew       : build the complete system, including the documentation."
	@echo "    make code       : build only the programs, without the documentation."
	@echo "    make doc        : build the documentation."
	@echo "    make clean      : suppress all .class modules, keeping only the .jar."
	@echo "    make cleanall   : suppress .class, .jar and .html files."
	@echo "    -               see also the Makefile for more specific targets."

drew: all

util: $(UTILFILES:.java=.class) $(XMLMPFILES:.java=.class)

clocale: $(CLOCALEFILES:.java=.class)

slocale: $(SLOCALEFILES:.java=.class)

cutil: util clocale $(CL_UTILFILES:.java=.class) 

sutil: util slocale 

scheme: $(SCHEMEFILES:.java=.class)

gew: cutil $(GEWFILES:.java=.class)

emotionawareness: cutil $(EMOTIONAWARENESSFILES:.java=.class)

chat: cutil $(CHATFILES:.java=.class) 

feu: cutil $(FEUFILES:.java=.class) 

grapheur: cutil $(GRAPHEURFILES:.java=.class) 

misc: cutil scheme $(MISCFILES:.java=.class) 

rejoueur: cutil $(REJOUEURFILES:.java=.class) 

tableau: cutil $(TABLEAUFILES:.java=.class) 

text: cutil $(TEXTFILES:.java=.class) 

multi: cutil $(MULTIFILES:.java=.class) 

whiteboard: cutil $(WHITEBOARDFILES:.java=.class) 

viewboard: cutil $(VIEWFILES:.java=.class)

simpleawareness: cutil $(AWARENESSFILES:.java=.class)

imageviewer: cutil $(IMAGEVIEWERFILES:.java=.class)

client: cutil gew emotionawareness chat feu grapheur rejoueur whiteboard text tableau multi dict misc alex vote app viewboard simpleawareness imageviewer

alex: cutil $(ALEXFILE:.java=.class)

dict: cutil $(DICTFILE:.java=.class)

jfree: cutil $(JFREEFILES:.java=.class)

vote: cutil $(VOTEFILE:.java=.class)

app: cutil $(APPFILE:.java=.class)

client.jar: client
	@if [ ! -d classes ]; then mkdir classes; fi
	@if [ ! -d classes/lib ]; then mkdir classes/lib; fi
	@find Drew/Client/* Drew/Locale/Client/* Drew/Util/* alex Sch org -name \*.class | cpio -u -dp classes
	@cp client.manifest classes/
	@cp client.properties classes/
	@find Image -name \*.gif | cpio -u -dp classes
	@find alex -name TemplatesUTF8.txt.\* | cpio -u -dp classes
#	@cd  classes; $(JAR) cvmf client.manifest Client.jar Drew/Client/* Drew/Locale/Client/* Drew/Util/* alex client.properties Image Sch/* org/dict/client/*
	@cd  classes; $(JAR) cvmf client.manifest Client.jar Drew/Client/* Drew/Locale/Client/* Drew/Util/* alex client.properties Image Sch/* org/dict/client/*

serveur: sutil $(SERVEURFILES:.java=.class)

serveur.jar: serveur
	@if [ ! -d classes ]; then mkdir classes; fi
	@find Drew/Serveur/* org/dict/client/* Drew/Util/* Drew/Locale/Serveur/* -name \*.class | cpio -u -dp classes
	@cp server.manifest classes/
	@cp server.properties classes/
	@cd classes; $(JAR) cvmf server.manifest Server.jar Drew/Serveur/* Drew/Locale/Serveur/* Drew/Util/* org/dict/client/* alex server.properties

# manque peutetre une ligne pour recuperer les fichiers xsl de Drew/TraceViewer/xsl
traceviewer.jar: client traceviewer
	@if [ ! -d classes ]; then mkdir classes; fi
	@find Drew/TraceViewer/* Drew/Client/* Drew/Locale/Client/* Drew/Util/* alex Sch org -name \*.class | cpio -u -dp classes
	@cp traceviewer.manifest classes/
	@cp client.properties classes/
	@find Image -name \*.gif | cpio -u -dp classes
	@find alex -name TemplatesUTF8.txt.\* | cpio -u -dp classes
	@cd classes; $(JAR) cvmf traceviewer.manifest TraceViewer.jar Drew/Client/* Drew/Locale/Client/* Drew/Util/* alex Image Drew/TraceViewer/* client.properties

traceviewer: $(TRACEFILES:.java=.class)


java: client serveur

jar: client.jar serveur.jar traceviewer.jar

doc: 
#	$(JAVADOC) $(JAVADOCFLAGS) $(PACKAGES)

docSch: 
#	$(JAVADOC) $(JAVADOCFLAGS) -encoding C Sch

all: util serveur client traceviewer doc jar 

code: util serveur client jar

clean:
	@find . -name \*.class -exec rm {} \;
	@cd classes || exit 2; rm -fr Drew  Image  alex  org *.manifest

clean-doc: clean
	@cd $(MYDOCPATH) || exit 3; \
		rm -f Drew.*.html Package-Drew.*.html; \
		rm -f Sch.*.html  Package-Sch.*.html; \
		rm -f tree.html Package.html; \
		rm -fr Drew Sch org alex

cleanall: clean
	@cd $(MYCLASSPATH) || exit 4; rm -f Client.jar Serveur.jar

