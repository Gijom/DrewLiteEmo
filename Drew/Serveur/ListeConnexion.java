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
 * File: ListeConnexion.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: ListeConnexion.java,v 1.10 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur;

import Drew.Util.XMLmp.*;


/**
* Double linked liste for connections
* @see Liste
*/
class ListeConnexion extends Liste {

boolean DEBUG = true;

	/**
	* Constructor
	* @see Liste
	*/
	ListeConnexion() {
		super();
	}

	/**
	* Send message to every members of the list
	* @param msg to be sent
	*/
	public void sendMessage(XMLTree msg) {

		Connexion c;

		for (Enumeration e = elements() ; e.hasMoreElements() ;) {
                        c = (Connexion)e.nextElement();
			c.sendMessage( msg );
                }
	}

	/**
	* Send message to every members of the list
	* @param msg to be send
	* @param filter to be apply for each connexion
	* @see Iterator
	*/
	public void sendMessage(String msg, String cmd, Service s) {

                Connexion c;

                for (Enumeration e = elements() ; e.hasMoreElements() ;) {
                        c = (Connexion)e.nextElement();
			c.sendMessage( new XMLTree( msg + s.doCmd( cmd, c ) ) );
		}
	}

	/**
	* Send participant list to this connection
	* @param connexion connection requesting the list
	* @see Connexion 
	*/
	public void participantList(Connexion connexion) {

		Connexion c;
		XMLTree msg = new XMLTree( "control" );

		//msg.add( new XMLTree( "info", "Liste des participants :\n" ));
		msg.add( XML.info( XML.message( "msg007" )));	// Participants list
                for (Enumeration e = elements() ; e.hasMoreElements() ;) {
                        c = (Connexion)e.nextElement();
			msg.add( new XMLTree( "info", " - " + c.getName() + "\n" ));
		}
		connexion.sendMessage( new XMLTree( "event",msg) );
	}

	/**
	* Verify if an already connected people use the same name
	* @param connexion the user connection
	* @return false if we find a connected user with the same name, true otherwise
	* @see Connexion
	*/ 
	public boolean verifie_connection(Connexion connexion) {

		Connexion c;

                for (Enumeration e = elements() ; e.hasMoreElements() ;) {
                        c = (Connexion)e.nextElement();
			if( (c != connexion) && (c.getName().equals(connexion.getName())) ) return false;
		}
		return true;
    	}
}
