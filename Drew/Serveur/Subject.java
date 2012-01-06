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
 * File: Subject.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Subject.java,v 1.6 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Serveur;

import java.io.*;

/**
* abstraction pour un sujet, on trouve tout ce dont on peut avoir besoin
*/
public class Subject {

	boolean DEBUG = true;

    	/**
    	* Le fichier de trace associe au sujet
    	*/
    	private TraceFile trace = null;

    	/**
    	* Le Room server associe au sujet
    	*/
    	private RoomServer roomserver = null;

    	/**
    	* Constructeur: création d'un sujet.
    	*/

    	public Subject( String title) throws IOException {
		trace = new TraceFile( Config.getDir(), title );
    	}

    	public Subject( String title, RoomServer rs) throws IOException {
		trace = new TraceFile( Config.getDir(), title );
		roomserver = rs;
    	}

	/**
	* Mise a jour du RoomServer
    	* @param rs RoomServer pour le sujet
    	* @see RoomServer
    	*/
    	public void setRoomServer(RoomServer rs) {
		roomserver = rs;
    	}

	/**
	* accesseur du RoomServer
    	* @see RoomServer
    	*/
    	public RoomServer getRoomServer() {
		return roomserver;
    	}

	/**
	* accesseur du TraceFile
    	* @see TraceFile
    	*/
    	public TraceFile getTraceFile() {
		return trace;
    	}


	/**
	* End subject if not used
	*/
	public boolean End() throws IOException {
		if( roomserver.isEmpty() ) {
			trace.close(); trace = null;
			roomserver = null;
			return true;
		}
		return false;
	}
}

