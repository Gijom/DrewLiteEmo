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
 * File: RoomServer.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: RoomServer.java,v 1.5 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Serveur;

import java.util.Hashtable;
import java.util.Enumeration;

import Drew.Util.XMLmp.XMLTree;

/**
* class managing people connected in a room
*/
public class RoomServer {

    	/**
    	* Hashtable storing connections for each rooms
    	*/
    	private Hashtable rooms = new Hashtable();

    	/**
    	* Constructor: add a ListeConnexion for each room needed
    	* @see ListeConnexion
    	* @see Drew.Client.TableauDeBord.Room
    	*/
	public RoomServer() {
		rooms.put("Hall", new ListeConnexion());
		rooms.put("Salon", new ListeConnexion());
		rooms.put("Boudoir", new ListeConnexion());
		rooms.put("Jardin", new ListeConnexion());
		rooms.put("Salle 1", new ListeConnexion());
		rooms.put("Salle 2", new ListeConnexion());
		rooms.put("Salle 3", new ListeConnexion());
		rooms.put("Salle 4", new ListeConnexion());
		rooms.put("Salle 5", new ListeConnexion());
		rooms.put("Salle 6", new ListeConnexion());
		rooms.put("Salle 7", new ListeConnexion());
		rooms.put("Salle 8", new ListeConnexion());
	}

    	/**
    	* return the ListeConnexion for the specified room
    	* @param room the required room
    	* @return the ListeConnexion for this room
    	* @see ListeConnexion
    	*/
    	public ListeConnexion getConnectionList(String room) {
        	return((ListeConnexion)rooms.get(room));
    	}

	/**
	* Add the connection in the room
	* @param conn the connection to add
	* @param room in which one
	*/
	public void add( String room, Connexion c ) {
		getConnectionList(room).insert( c );
	}

	/**
	* remove the connection from the room
	* @param conn the connection to remove
	* @param room in which one
	*/
	public void remove( String room, Connexion c ) {
		getConnectionList(room).remove( c );
	}

	/**
	* send message to each client connected in the room
	* @param room the room where to send 
    	* @param msg the xml message to send
	*/
	public void sendMessage(String room, XMLTree msg) {
		getConnectionList(room).sendMessage(msg);
	}

    	/**
    	* Send message to every connected clients 
    	* @param msg the xml message to send
	*/
	public void sendMessage(XMLTree msg) {

		for(Enumeration e = rooms.elements(); e.hasMoreElements(); ) {
			ListeConnexion lc = (ListeConnexion)e.nextElement();
			if(!(lc.vide())) lc.sendMessage( msg );
		}
	}

    	/**
    	* look at connected clients 
	*/
	public boolean isEmpty() {

		for(Enumeration e = rooms.elements(); e.hasMoreElements(); ) {
			ListeConnexion lc = (ListeConnexion)e.nextElement();
			if(!(lc.vide())) return false;
		}
		return true;
	}

    	/**
    	* Reject connection if a user with the same name exist
    	* @param connexion current connection
    	* @return true if a user with same name exist
    	* @see Connexion
    	*/
    	public boolean verifie(Connexion connexion) {
        boolean verification;

        	for(Enumeration e = rooms.elements(); e.hasMoreElements(); ) {
            		ListeConnexion lc = (ListeConnexion)e.nextElement();
            		if(!(lc.vide())) {
                		verification = lc.verifie_connection(connexion);
                		if(!verification) {
                    			return true;
                		}
            		}
        	}
        	return false;
    	}
}
