/*  
 * The Drew software is a CMI (Computer Mediated Interaction) set of tools that
 * combines synchronous exchanges activities with browser-driven web page
 * consultation.
 * Copyright (C) 2003  The Drew Team
 * 
 * The Drew software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * The Drew software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * File: Config.java
 * Author:
 * Description:
 *
 * $Id: Config.java,v 1.30.2.2 2007/10/31 14:22:58 collins Exp $
 */

package Drew.Client.Util;

import java.applet.*;
import java.awt.Panel;
import java.util.*;


public class Config {
/**
* Tous les modules connus doivent etre d�clarer ici (et seulement ici normalement)
* on a le nom du module tel qu'il doit apparaitre dans les parametres de l'applet
* puis, la chaine de caractere sous laquelle il doit etre presente dans les truc a cocher
* puis le nom de la classe (complete) qu'il faudra utiliser
*/

static private String T[][] = {
        {"GraphGEW",    "GraphGEW",     "Drew.Client.MultiModules.GraphGEW"},    
        {"GEW",         "GEW",          "Drew.Client.GEW.GEW"},
	{"chat",        "chat",         "Drew.Client.Chat.Chat_fenetre"},
	{"viewboard",   "viewboard",    "Drew.Client.ViewBoard.ViewBoard"},
	{"textboard",       "textboard",        "Drew.Client.TextBoard.TextBoard"},
	{"tokentextboard","textboard bloquant",        "Drew.Client.TokenTextBoard.TokenTextBoard"},
	{"whiteboard",  "whiteboard",   "Drew.Client.WhiteBoard.WhiteBoard"},
	{"feutricolore","feu R/V",      "Drew.Client.FeuTricolore.FeuTricolore"},
	{"misc",        "MiSc",         "Drew.Client.MiSc.MiSc"},
	{"grapheur",    "graph",        "Drew.Client.Grapheur.Grapheur"},
	{"multi",       "multi",        "Drew.Client.MultiModules.MultiModules"},
	{"graphchat",   "graph & chat", "Drew.Client.MultiModules.GraphChat"},
	{"2modules",    "2 modules",    "Drew.Client.MultiModules.TwoModules"},
//	{"dict",        "dict",         "Drew.Client.Dict.Dict"},
	{"alex",        "Alex",         "alex.Alex"},
	{"vote",        "Vote",         "Drew.Client.Vote.Vote"},
	{"awareness",   "Awareness",    "Drew.Client.SimpleAwareness.SimpleAwareness"},
	{"imageviewer",   "Image Viewer",    "Drew.Client.ImageViewer.ImageViewer"},
};

/**
* experimental configuration, using java properties
*/
private static Properties initProperties() {
	Properties p = new Properties();

	p.put( "drewlet.chat.text",  "chat");
	p.put( "drewlet.chat.param", "chat");
	p.put( "drewlet.chat.class", "Drew.Client.Chat.Chat_fenetre");

	return p;
}

static private java.util.Locale locale = java.util.Locale.getDefault();

	static public int listLength() 
	{
		return T.length;
	}

	static public String getName( int i ) 
	{
		return T[i][0];
	}

	static public String getComment( int i ) 
	{
		return T[i][1];
	}

	static public String getClass( int i ) 
	{
		return T[i][2];
	}

	static public int getIndex( String name ) 
	{
		for( int i=0; i< listLength(); i++ ) {
                        if( name.equals(getName(i)) ) return i;
                }
		return -1;
	}

	static public java.util.Locale getLocale() {
		return locale;
	}

	static public void setLocale(String l) {
		locale = new Locale(l, "xx") ;
	}

	static public void setLocale(String l, String c) {
		locale = new Locale(l, c) ;
	}

	static public void setLocale(String l, String c,String v ) {
		locale = new Locale(l, c, v) ;
	}

	static public void setLocale(Locale l ) {
		locale = l;
	}

	static public Locale getLocale(Applet app ) {
		setLocale(app);
		return locale;
	}

	static public void setLocale(Applet app ) {
		String dummy = app.getParameter("locale");

		if( dummy != null && !dummy.equals("")) {
		    if( getDebug() ) {
				System.err.println( "Config.getLocale parameter: " + dummy);
				}
			StringTokenizer st = new StringTokenizer(dummy,"_");
			String l = null,c = "xx",v = "xx";

			l = st.nextToken();
			if(st.hasMoreTokens()) c = st.nextToken();
			if(st.hasMoreTokens()) v = st.nextToken();

			locale = new Locale(l,c,v);
		}
		else {
			locale = app.getLocale();
		}

		if( getDebug() ) {
			System.err.println( "Config.getLocale : " + locale.toString() );
		}
	}

	static public CooperativeModule newInstance( int i, Communication cdc , boolean window, Panel ctrl) 
	{
                Class c;
                CooperativeModule m;
		String cn = getClass(i);

		try {
			c = Class.forName(cn);
			m = (CooperativeModule)c.newInstance();
			m.constructor(cdc);

			// modification pour les modules, c'est la qu'on rajoute la fenetre
			if( window == true ) {
				return new ModuleWindow( m, ctrl, getComment(i) );
			}
			else {
				return m;
			}
		}
		catch( Exception e ) {
			System.out.println( "Config.newInstance : " + cn + " est introuvable, je rend null [" + e + "]" );
			e.printStackTrace();
			return null;
		}
	}

	static public CooperativeModule newInstance( int i, Communication cdc ) {
		return newInstance( i, cdc, true, null );
	}

	static public CooperativeModule newInstance( String module_name, Communication cdc, boolean window ) {
		return newInstance( getIndex(module_name), cdc, false, null );
	}

	static public CooperativeModule newInstance( String module_name, Communication cdc, boolean window, Panel ctrl ) {
		return newInstance( getIndex(module_name), cdc, window, ctrl );
	}

	/** To access to the ControlPanel from anywhere */
	static private Drew.Client.TableauDeBord.CentreDeConnection CDC = null;

	static public void setcdc( Drew.Client.TableauDeBord.CentreDeConnection cdc ) {
		CDC = cdc;
	}

	static public void selectModule( String name, boolean state) {

		if( CDC != null ) CDC.controlPanel().selectModule(name, state);
	}

        static public String getEncoding() {
                return "UTF8";
                //return "8859_1";
                //return "UnicodeBig";
        }

	/**
	* Gestion des messages de debug, on prend des ^2 et on masque
	*/
	static private long DEBUG = 0;

        static public boolean getDebug() {
                return DEBUG != 0;
        }

        static public boolean getDebug(long mask) {
                return (DEBUG & mask) != 0;
        }

        static public void setDebug(long val) {
                DEBUG |= val;
        }

	/**
	* Using HTTP as communication support
	*/
	static private String HTTP = null;

	static public void setHTTPConnection( String base ) {
		HTTP = base;
	}
	
	static public String getHTTPConnection() {
		return HTTP;
	}

	/**
	* For running drewlet without control panel
	*/
	static private boolean CONTROLPANEL = true;

	static public boolean getControlPanel() {
		return CONTROLPANEL;
	}

	static public void setControlPanel( boolean is ) {
		CONTROLPANEL = is;
	}

	/**
	* Initial room (in any known language)
	*/
	static private String INITIALROOM = "Hall";

	static public String getInitialRoom() {
		return INITIALROOM;
	}

	static public void setInitialRoom( String is ) {
		INITIALROOM = is;
	}

	/**
	* User can change room
	*/
	static private boolean CANCHANGEROOM = true;

	static public boolean canChangeRoom() {
		return CANCHANGEROOM;
	}

	static public void setChangeRoom( boolean is ) {
		CANCHANGEROOM = is;
	}

	/**
	* User can choose its drewlet
	*/
	static private boolean CANCHOOSEDREWLET = true;

	static public boolean canChooseDrewlet() {
		return CANCHOOSEDREWLET;
	}

	static public void setChooseDrewlet( boolean is ) {
		CANCHOOSEDREWLET = is;
	}

// it's a bad idea to add drewlet specific parameters in a global place
	/**
	* For running grapheur in a non interactive mode
	*/
	static private boolean INERACTIVEGRAPH = true;

	static public boolean getGraphInteraction() {
		return INERACTIVEGRAPH;
	}

	static public void setGraphInteraction( boolean is ) {
		INERACTIVEGRAPH = is;
	}

	/**
	* For printing in grapheur
	*/
	static private boolean PRINTGRAPH = false;

	static public boolean getGraphPrint() {
		return PRINTGRAPH;
	}

	static public void setGraphPrint( boolean is ) {
		PRINTGRAPH = is;
	}


	/**
	* For running and debuging Clients without server connexion
	*/
	static private boolean STANDALONE = false;

	static public boolean getStandAlone() {
		return STANDALONE;
	}

	static public void setStandAlone( boolean is ) {
		STANDALONE = is;
	}


	/**
	* Add a mark to each even
	*/
	static private String MARK = null;

	static public String getMark() {
		return MARK;
	}

	static public void setMark( String mark ) {
		MARK = mark;
	}

	/**
	* Add support for properties like acces
	*/
	static public int getRawPort() {
		return CDC.port;
	}

	public static String getProperty( String key, String dummy) {
                String str =  CDC.getParameter( key );
		if( str != null ) {
			return str; 
		}
		else {
			return dummy;
		}
        }

        public static int getProperty( String key, int dummy) {
        int val;

                try {
                        val = Integer.parseInt( CDC.getParameter( key ) );
                }
                catch( NumberFormatException e ) {
                        val = dummy;
                }

                return val;
        }

}
