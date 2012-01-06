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
 * Author: Ph. Jaillon
 * Description:
 *
 * $Id: Config.java,v 1.13 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur;

import java.util.Properties;
import java.util.Enumeration;

public class Config {

private static Drew.Serveur.Control.Application app;
static Properties properties = Config.initProperties();
static int debuglevel = Config.getProperty( "Debug", 0);

static int MESSAGES   =   1;
static int STACKTRACE = 128;

	public static boolean Debug( int level ) {
		return (debuglevel & level) != 0;	
	}

	public static Properties getProperties() {
		return properties;
	}

	private static void setProperty(String key, String value) {
		if( properties.getProperty( key ) == null ) properties.put(key,value);
	}

	private static Properties initProperties() {

		properties = new Properties( System.getProperties());

		// PJ Add server properties for standalone preconfigured server
		try {
			properties.load( Class.forName("Drew.Serveur.Config").getClassLoader().getResourceAsStream("server.properties"));

			// Using -D swich on java command line add the specified property to the System properties
                        // and we want to overide properties defined in properties file in such way
			for( Enumeration e = properties.propertyNames(); e.hasMoreElements() ;) {
                                // Remove properties defined on the java command line
                                String key = (String) e.nextElement();
                                if(System.getProperties().getProperty(key) != null) {
                                        properties.remove( key );
                                }
                        }
		}
		catch( java.lang.Exception e ) {
			System.err.println( "Can't load server.properties file");
			e.printStackTrace();
		}


		//setProperty( "Encoding", "utf-8" );
		//setProperty( "Debug", "0" );
		//setProperty( "ExternalService.user.dict", "org.dict.client.JDict" );
		//setProperty( "ExternalService.user.default", "Drew.Serveur.DefaultService" );
		//setProperty( "ExternalService.room.verlan", "Drew.Serveur.Verlan" );

		//setProperty( "Control.port", "3000" );
		//setProperty( "Control.bindaddress", "127.0.0.1" );
		//p.put( "ExternalService.room.cht", "Drew.Serveur.Verlan" );

		//PJ
		properties.list(System.out);

		return properties;
	}

	public static String getProperty( String key, String dummy) {
		return properties.getProperty( key, dummy);
	}

	public static int getProperty( String key, int dummy) {
	int val;

		try {
			val = Integer.parseInt( properties.getProperty(key, String.valueOf(dummy)) );
		}
		catch( NumberFormatException e ) {
			val = dummy;
		}

		return val;
	}

//#############################################################@

	public static void setApp(Drew.Serveur.Control.Application x) {
		app = x;
	}

	public static Drew.Serveur.Control.Application getApp() {
		return app;
	}

//#############################################################@

	public static String  getEncoding() {
		return getProperty("Encoding","utf-8");
		//return "utf-8";
		//return "UTF8";
		//return "8859_1";
		//return "UnicodeBig";
	}

	public static String  getDir() {
		return getProperty("Drew.traces.dir", ".");
	}

	public static void setDir(String str) {
		setProperty( "Drew.traces.dir", str );
	}

	public static int  getRawPort() {
		return getProperty("Drew.raw.port", 32000);
	}

	public static int  getHTTPPort() {
		return getProperty("Drew.http.port", 32080);
	}

	public static boolean getDebug() {
		return getProperty("Debug", "false").equals("true");
	}

	public static boolean getAuth() {
		return getProperty("Control.auth", "true").equals("true");
	}

	public static String getAuthFile() {
		return getProperty("Control.auth.file", "drew.auth");
	}

	public static boolean allowRestrictedUser() {
		return getProperty("Control.restrictedUser.allow", "false").equals("true");
	}

	public static String getRestrictedUser() {
		return getProperty("Control.restrictedUser", "drew");
	}

	public static String getRestrictedUserPassword() {
		return getProperty("Control.restrictedUser.passwd", "drew");
	}

	public static int getControlPort() {
		return getProperty("Control.port", 3000);
	}

	public static String getControlBindAdress() {
		return getProperty("Control.bindaddress", "127.0.0.1");
	}
}
