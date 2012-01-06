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
 * File: GrandSioux.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: GrandSioux.java,v 1.19 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur;

import Drew.Serveur.Control.*;

/**
* Main class launching server. It create the control server and the servers
* handling raw and http connections. It quit quikly. After starting the server management
* is under the "Control application" control.
*/
public class GrandSioux {


	/**
	* Launch the drew server. Parameters can be specified as java properties. See Config.java.
	* @param args directory where to store trace file
	*/
	public static void main(String[] args) {
		try {
			if(args.length > 0) Config.setDir( args[0] );
		}
		catch (Exception e) {
			System.err.println("arguments : "+e);
		}

		//Config.getProperties().list( System.out );

		Application app = new Application();
		Config.setApp( app );

		// Control is the only "not daemon" thread, server quit with it
		Control c = new Control(app); c.start();
		app.add( c ); 

		// startup an UDP responder for zeroconf clients if needed
		if( Config.getProperty("ZeroConf.responder", "false").equals("true") ) {
			Drew.Util.UDP.Responder resp = new Drew.Util.UDP.Responder(); 
			app.add( resp );
			resp.start();
		}
		// Raw server
		Drew.Serveur.Raw.SiouxServer raw = new Drew.Serveur.Raw.SiouxServer();
		app.add( raw ); 
		raw.start();
		// HTTP server
		Drew.Serveur.HTTP.SiouxServer HTTP=new Drew.Serveur.HTTP.SiouxServer();
		app.add( HTTP); 
		HTTP.start();
	}
}
