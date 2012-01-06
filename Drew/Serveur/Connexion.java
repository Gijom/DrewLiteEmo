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
 * File: Connexion.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Connexion.java,v 1.12 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Serveur;

import java.net.*;
import java.io.*;

import Drew.Util.XMLmp.*;

/**
* Represente une connection aupres du serveur GrandSioux.
* Regroupe les information necessaires pour la gestion des utilisateurs.
*/
public class Connexion
{
	/**
	* Socket de la connection
	*/
	//Socket socket;
	private ConnectionImpl socket;
	
	/**
	* user name
	*/
	private String name = "unknown";

	/**
	* current room used by user
	*/
	private String room = "unknown";

	/**
	* session subject
	*/
	private String subject;

	/**
	* language used by client for this session
	*/
	private String lang = Config.properties.getProperty("lang","en");
	private Drew.Util.Locale comment = null;

	/**
	* the XMLPrinter used for the connection
	*/
	private XMLPrinter outpw;

	/**
	* Constructeur
	* @param socket recupere la socket de connection
	* @see GrandSioux
	*/
	public Connexion(Socket socket) throws IOException {
		this.socket = new Drew.Serveur.Raw.Socket( socket );
		//PJ address = socket.getInetAddress();
		outpw = new XMLPrinter( new OutputStreamWriter( this.socket.getOutputStream(), Config.getEncoding() ));
	}

	public Connexion(ConnectionImpl socket) throws IOException {
		this.socket = socket;
		//PJ address = socket.getInetAddress();
		outpw = new XMLPrinter( new OutputStreamWriter( this.socket.getOutputStream(), Config.getEncoding() ));
	}

	public InetAddress getInetAddress() {
		return socket.getInetAddress();
	}

	/**
	* return the associated imputStream for this connection
	*/
	public InputStream getInputStream() throws IOException {
		return socket.getInputStream();
	}

	/**
	* Close the connection with the client
	*/
	public void close() throws IOException {
		socket.close();
	}

	/**
	* set the user name for this connection
	* @param name the user name to set
	*/
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	* Defini le sujet auquel la connexion fait reference
	* @param sujet le sujet
	*/
	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getSubject() {
		return subject;
	}

	/**
	* Situe l'utilisateur dans une piece
	* @param room la piece ou est l'utilisateur
	*/
	public void setRoom(String room) {
		this.room = room;
	}

	public String getRoom() {
		return room;
	}

	/**
	* Envoi un message a l'utilisateur
	* @param texte texte du message
	*/
	public void averti(Object texte) {
		sendMessage( new XMLTree( "event", new XMLTree( "control", new XMLTree( "warning", texte ))));
	}
		
	/**
	* Envoi un message a l'utilisateur qui est connecte
	* @param msg texte du message
	*/
        public void sendMessage(XMLTree msg) {
		send(msg);
		flush();
        }

	/**
	* Envoi un message a l'utilisateur qui est connecte, sans forcer l'emission
	* @param msg texte du message
	*/
        void send(XMLTree msg) {
		//Config.setSendComment( comment );
		CurrentConnection.setLocale( comment );
		outpw.print( msg );
        }

	/**
	* force l'envoi des messages 
	*/
        void flush() {
		outpw.flush();
        }


	void setLang( String l ) {
		lang = l;
	        comment = new Drew.Util.Locale("Drew.Locale.Serveur.Serveur",new java.util.Locale(lang,"xx","xx"));
	}

	String getLang() {
		return lang;
	}

	
	public String toString() {
		return "name=" + name + "	subject=" + subject + "	room=" + room + "	host=" + socket.getInetAddress().getHostAddress();
	}
}
