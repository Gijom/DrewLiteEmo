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
 * File: Control.java
 * Author:
 * Description:
 *
 * $Id: Control.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur.Control;

import java.io.*;
import java.net.*;
import  Drew.Serveur.*;

/**
 * class for manage a control connection to the GrandSioux server
 */
public class Control extends Thread implements Infos {

private Application app;
	private int port = 3000;

	public Control(Application app) {
		this.app = app;
		setDaemon(true);
	}

	public void run() {
	Socket s;
	ServerSocket server;
	ControlConnection c;
	boolean ok = true;
	InetAddress bindAddr = null;

		try {
			port     = Config.getControlPort();
			bindAddr = InetAddress.getByName( Config.getControlBindAdress() );
		}
		catch( NumberFormatException e ) { 
			port = 3000; 
		}
		catch( UnknownHostException e ) { 
			try {
				System.err.println("Control: " + e.getMessage() );
				bindAddr = InetAddress.getLocalHost(); 
			}
			catch( UnknownHostException ee ) {
				System.err.println( "Control: " + ee.getMessage() + ", closing control.");
				return;
			}
		}

		try {
			server = new ServerSocket( port, 2, bindAddr );
			System.err.println("Control connection started on port " + port );

			while( ok == true ) {
				s = server.accept();
				c = new ControlConnection(app, s );
				c.start();
			}
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
 
	}

	public String infos() {
		if( isAlive() ) {
			return "listening Control connection on port " + port ;
		}
		else {
			return "Control server dead";
		}
	}
	public static void main( String[] args ) {

		Control c = new Control( new Application() );
		c.start();
	}
}
