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
 * File: Communication.java
 * Author:
 * Description:
 *
 * $Id: Communication.java,v 1.10 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Util;

import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.Applet;

import Drew.Util.XMLmp.*;
import Drew.Client.Util.XMLSocket.*;

/**
* Base class for the user connexion center and the replayer
*/
public class Communication extends Applet {

	/**
	* Web server internet address
	*/
	public  InetAddress server;

	/**
	* port number for GrandSioux server
	*/
	public  int port;

	/**
	* Connected socket with GrandSioux server
	*/
	public  XMLSocket  acces_serveur;

	/**
	* User name
	*/
	public  String nom;

	/**
	* Method to be define by connexion center
	*/
	public void connection(String texte) throws IOException { }

	/**
	* Method to be define by connexion center
	*/
	public void reconnection () { }

	/**
	* Method to be define by connexion center
	* to send messages to server as an XML object
	*/
	public void envoiserveur (XMLTree event) { }
	public void envoiserveur (XMLTree event, Duration d) { }

	/**
	* Method to be define by connexion center
	* to send messages to server
	*/
	public void envoiserveur (String texte) { }

	/**
	* Method to be define by connexion center
	* to send messages to server
	*/
	public void envoiserveur (String code, String msg) { }

	public URL getCodeBase() {
	URL url = null ;

		try {
			url = super.getCodeBase();
		}
		catch (Exception ex) {
			try {	
			//what that ?????? and if doesn't exist
			url = new URL("http://cesifs.emse.fr/DREW/classes");
			} 
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return url;
	}

	public Image getImage(URL url, String name){
	Image im;

		try {
			im = super.getImage(url, name);
		}
		catch (Exception e) {
			im = Toolkit.getDefaultToolkit().getImage(name);
		}
		return im;
	}

}
