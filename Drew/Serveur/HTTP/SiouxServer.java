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
 * File: SiouxServer.java
 * Author:
 * Description:
 *
 * $Id: SiouxServer.java,v 1.5 2004/01/28 17:44:50 jaillon Exp $
 *
 * $Log: SiouxServer.java,v $
 * Revision 1.5  2004/01/28 17:44:50  jaillon
 * Add a status command to control connection
 *
 * Revision 1.4  2004/01/13 14:35:48  jaillon
 * minor modifications in error handling
 *
 */

package Drew.Serveur.HTTP;

import java.util.*;
import java.net.*;
import java.io.*;

import Drew.Serveur.*;

/**
* SiouxServeur is a Thread listen ing for incoming connections on 
* the HTTP connection port. For each one, it try to reconnect the
* HTTP socket to an active Session, else it start a new thread
* (a Sioux) linked to a new Session.
* The Sioux server thread isn't under the application control, 
* but is a daemon.
*
* I have a pb, Session never dies !!!!!!!!
*/
public class SiouxServer extends Thread implements Infos {
private Hashtable H = new Hashtable();

	public void SiouxServer() {
		setDaemon( true );
	}

	public void run() {
	Headers h   = null;
	Session S   = null;
	String  url = null;
	Socket  s   = null;

		try {
			ServerSocket ss = new ServerSocket( Config.getHTTPPort() );
			System.out.println("Listening HTTP connections on port " + ss.getLocalPort());

			while (true) {
				try {
					s = ss.accept();

					h = new Headers( s );
					url = h.getUrl();
                                	S = (Session)H.get( url );
                                	if( S == null ) { // Connection dosen't exist, we add it
                                        	S = new Session();
						S.setId( this, url );
                                        	H.put( url, S);
                                        	// start a new Sioux to manage it
                                        	new Sioux( new Connexion(S) ).start();
						System.out.println("new HTTP Sioux started for [" + h.getUrl() + "].");
                                	}
                                	// reconnect the network stream to the Session
                                	S.connect( s, h );
				}
				catch (Throwable e) {
					System.err.println( "Connexion aborted " + e );
					// remove it from current sessions
					if( S != null ) {
						System.err.println( "Remove " + S );
						remove( url );
						try { 
							S.close(); 
						} catch (IOException io ) {}
					}
					else {
						try { s.close(); } catch (IOException io ) {}
					}
				}
				//catch (Throwable t) {
					//t.printStackTrace();
				//}
			}
		}
		catch (IOException e) {
			System.err.println("Initialisation: "+e);
		}
	}

	public void remove( String key ) {
		H.remove(key);		
	}

	public String infos() {
		if( isAlive() ) {
			return "Listening HTTP connections on port " + Config.getHTTPPort();
		}
		else {
			return "HTTP server dead";
		}
	}
}	  
