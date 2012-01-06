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
 * File: ControlConnection.java
 * Author:
 * Description:
 *
 * $Id: ControlConnection.java,v 1.9 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur.Control;

import java.io.*;
import java.net.*;
import java.util.*;
import Drew.Serveur.Infos;
import Drew.Serveur.Sioux;
import Drew.Serveur.Config;

/**
 * class for manage a control connection to the GrandSioux server
 */
public class ControlConnection extends Thread {

private Application app;
private Socket sock;
private BufferedReader in;
private PrintStream   out;
static private String prompt = "Drew # ";
private boolean restricted = false;

	ControlConnection(Application app,  Socket sock ) throws IOException {
		this.sock = sock;
		this.app = app;

		in  = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		out = new PrintStream( sock.getOutputStream() );

		setDaemon( true );
	}

	private String readPasswordFile(String user) throws LogException, IOException {
		BufferedReader passwd = new BufferedReader(new FileReader(Config.getAuthFile()));
		String line = null;
		while( (line = passwd.readLine()) != null) {
			// line are like drew:$apr1$v06Rb/..$1PgeQs3oMefDVKzqM/gKj0
			if(line.startsWith(user + ":")) break;
		}
		passwd.close();

		if(line == null) throw new LogException("User unknown :" + user);
		return line.substring(line.indexOf(":")+1).trim();
	}

	private String apacheSalt( String str) {
		// Apache password look like $apr1$v06Rb/..$1PgeQs3oMefDVKzqM/gKj0
		//                           -----$||||||||$//////////////////////
		return str.substring(6,str.indexOf("$",6));
	}

	private void login() throws LogException, IOException {
		out.print( "Login: " );
		String user = in.readLine();
		out.print( "Password: " );

		// a restricted mode just to see active connections
		if( Config.allowRestrictedUser() && user.equals( Config.getRestrictedUser() ) ) {
			String passwd = in.readLine();
			if( passwd.equals( Config.getRestrictedUserPassword() )) {
				restricted = true;
				return;
			}
			else throw new LogException("Incorrect password for " + user);
		}

		String xx = readPasswordFile( user );
		String salt = apacheSalt( xx );
		String passwd = MD5Crypt.apacheCrypt( in.readLine(), salt );
		if( passwd.equals( xx ) == true ) return;

//System.err.println( "user=["+user+"], salt=["+salt+"] passwd=["+passwd+"]/["+xx+"]" );
		out.print( "Password: " );
		passwd = MD5Crypt.apacheCrypt( in.readLine(), salt );
		if( passwd.equals( xx ) == false ) throw new LogException("Incorrect password for " + user);
	}

	public void run() {

	String line, cmd,args;
	boolean ok = true;
	StringTokenizer tok;

		try {

			//if authentication needed (the default)
			if( Config.getAuth() == true ) login();

			do {
				out.print( prompt );
 				line = in.readLine();

				if(line==null) break;

				try {
					tok = new StringTokenizer( line );
					cmd = tok.nextToken();
				}
				catch( Exception e ) {
					// it's a bad line, just spaces for examples
					continue;
				}	
				
				if( cmd.equals( "help" ) || cmd.equals( "?" ) ) {
					out.println( "Drew Control Connexion :");	
					out.println( "	help, ?      : this message");
					out.println( "	list         : list actives connexions");
					out.println( "	status       : server status");
					out.println( "	kill id [id] : close listed connexions, deconnecting client");
					out.println( "	stop         : stop the drew server");
					out.println( "	quit         : quit the control session");
				}
				else if( cmd.equals( "quit" ) || cmd.equals( "q" ) ) {
					ok = false;
				}
				else if( cmd.equals( "stop" ) || cmd.equals( "s" ) ) {
					if( restricted == true ) continue;
					Sioux o;
					String key;
					// Stopping all Threads before ending
					for (Enumeration e = app.sioux() ; e.hasMoreElements() ;) {
						key = (String)e.nextElement();	
						o = app.get( key );
						o.end();
					}
					app.end();
					ok = false;
				}
				else if( cmd.equals( "status" ) || cmd.equals( "st" ) ) {
					for (Enumeration e = app.servers() ; e.hasMoreElements() ;) {
						Infos o = (Infos)e.nextElement();	
						if( o != null ) {
         						out.println(o.infos());
						}
     					}
				}
				else if( cmd.equals( "list" ) || cmd.equals( "l" ) ) {
					Sioux o;
					String key;
					for (Enumeration e = app.sioux() ; e.hasMoreElements() ;) {
						key = (String)e.nextElement();	
						o = app.get( key );
						if( o != null ) {
         						out.println(key + "	" + o.info());
						}
						else {
         						out.println(key + "	(null)" );
						}
     					}
				}
				else if( cmd.equals( "kill" ) || cmd.equals( "k" ) ) {
					if( restricted == true ) continue;
					String arg;
					Sioux o;
					while( tok.hasMoreElements()) {
						arg = tok.nextToken();	
						o = app.get( arg );
						app.remove( arg );
						o.end();
						out.println( arg + " killed" );	
					}
				}
				else {
					out.println( "unknown command " + cmd );
				}
			} while( ok == true );
		}
		catch( Exception e ) {
			e.printStackTrace();
		}
		finally {
			try {
				sock.close();
			}
			catch( IOException e ) {}
		}
	}
}
