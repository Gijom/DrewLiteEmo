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
 * File: ConnectionEcoute.java
 * Author:
 * Description:
 *
 * $Id: ConnectionEcoute.java,v 1.15 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.TableauDeBord;

import java.util.*;
import java.io.*;

import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;
import Drew.Client.Util.XMLSocket.*;
//import Drew.Client.TableauDeBord.*;



/**
* Thread listening to the GrandSioux server
*/
public class ConnectionEcoute extends Thread {

/**
* mother applet
* @see CentreDeConnection
*/
public CentreDeConnection central_applet;

/**
* connection socket to the GrandSioux server
* @see CentreDeConnection
*/
//PJ 20031021 public Socket sock;
private XMLSocket sock;

/**
* Status variable, used to decide if we drop the connection or not.
*/
private boolean isOK = true;

  	/**
  	* Thread constructor
  	* @param cdc get ancestor applet
  	*/ 
  	public ConnectionEcoute(CentreDeConnection cdc) {
		super( "ConnectionEcoute" );
        	central_applet=cdc;
        	sock = central_applet.acces_serveur;
      	}

  	/**
  	* Lancement du thread d'ecoute: lecture ligne a ligne sur la socket et affichage
  	* dans les modules concernes(chat, whiteboard, tableau de bord ...)
  	*/
  	public void run() {
		try {
			if( Config.getDebug() ) {
				System.err.println( "ConnectionEcoute started" );
			}
			// processus d'ecoute du serveur
			XMLTree event = null;

			while ( isOK == true ) {

				yield();

				event = sock.read();
				
				if(event == XMLParser.PEOF  ) break;
				if(event == XMLParser.PERROR) continue;

				if( Config.getDebug() ) {
					System.err.println( "ConnectionEcoute.run isOK=" + isOK + ", " + event.toString() );
				}
			
				String user = event.getAttributeValue("user","unknown");

				for (Enumeration e = event.elements() ; e.hasMoreElements() ;) {
					Object o = e.nextElement();
					if( o instanceof XMLTree ) central_applet.messageDeliver( user, (XMLTree)o );
				}
			}
		}
		catch (IOException e) {
			System.err.println("ecoute: "+e);
			central_applet.errorOccur( e );
		}
		finally
		{
			try {
				central_applet.acces_serveur = null;
				central_applet.reconnection();
				sock.close();
			}
			catch( IOException e ) {}
		}
  	}

	/**
	* Permet arreter proprement le thread d'ecoute
	*/
	public void abordConnexion() {
		isOK = false;
	}
}
