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
 * $Id: SiouxServer.java,v 1.4 2004/01/28 17:44:50 jaillon Exp $
 */

package Drew.Serveur.Raw;

import java.util.*;
import java.net.*;
import java.io.*;

import Drew.Serveur.*;

/**
* Sioux server for raw connections
*/

public class SiouxServer extends Thread implements Infos {

	/**
	* SiouxServeur is a Thread listen ing for incoming connections on 
	* the raw connection port. For each one, it start a new thread
	* (a Sioux) linked to a socket
	* The Sioux server thread isn't under the application control, 
	* but is a daemon.
	*/
	public void SiouxServer() {
		setDaemon( true );
	}

	public void run() {

		try {
			ServerSocket ss = new ServerSocket( Config.getRawPort() );
			System.out.println("Listening raw connections on port " + ss.getLocalPort());

			while (true) {
				try {
					java.net.Socket s = ss.accept();

					Sioux gs = new Sioux( new Connexion(s) );
					gs.start();
					System.out.println("new raw Sioux started." );
				}
				catch (IOException e) {
					System.err.println( "Connexion aborted " + e );
				}
			}
		}
		catch (IOException e) {
			System.err.println("Initialisation: "+e);
		}
	}

	public String infos() {
		if( isAlive() ) {
			return "Listening Raw connections on port " + Config.getRawPort();
		}
		else {
			return "Raw server dead";
		}
	}
}	  
