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
 * File: SubjectServer.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: SubjectServer.java,v 1.7 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Serveur;

import java.util.Hashtable;
import java.io.*;

/**
* Table de hachage regroupant les rooms servers par sujets
*/
public class SubjectServer {

	boolean DEBUG = Config.getDebug();

    	/**
    	* Table de hachage
    	*/
    	private static Hashtable subjects = new Hashtable();

    	/**
    	* Constructeur: creation du serveur de sujet.
    	*/

    	public SubjectServer() {
    	}

	/**
	* Insertion d'un RoomServer pour le sujet specifie s'il n'existe pas deja.
    	* @param subject nom du sujet concerne
    	* @see RoomServer
    	*/
    	public Subject add(String subject) throws IOException { 
		Subject s = (Subject)subjects.get( subject ); 	
		if( s == null ) {
			s = new Subject( subject, new RoomServer() );
			subjects.put(subject, s);
		}
		return s;
    	}

	/**
	* Supress the subject if nobody connected
    	* @param subject name of the subject
    	* @see Subject
    	*/
    	public void remove(String subject) throws IOException { 
		Subject s = (Subject)subjects.get( subject ); 	
		System.err.println( "SubjectServer : looking for empty subject " + subject );
		if( (s != null) && s.End() ) {
 			subjects.remove( subject );
			System.err.println( "SubjectServer : end subject " + subject );
		}
    	}

    	/**
    	* Retourne le sujet specifie
    	* @param subject nom du sujet desire
    	* @return le Subject concerne
    	* @see Subject
    	*/
    	public Subject get(String subject) {
        	return (Subject)subjects.get(subject);
    	}

    	/**
    	* Retourne le room server pour un sujet specifique
    	* @param subject nom du sujet desire
    	* @return le RoomServer concerne
    	* @see RoomServer
    	*/
    	public RoomServer getRoomServer(String subject) {
        	Subject s = get(subject);
		if( s != null ) {
        		return s.getRoomServer();
		}
		else return null;
    	}
}

