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
 * File: Socket.java
 * $Author: serpaggi $
 * Description: 
 *
 * $Id: NullSocket.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 *
 * $Log: NullSocket.java,v $
 * Revision 1.3  2003/11/24 10:54:26  serpaggi
 *  - LGPL header for all .java files
 *
 * Revision 1.2  2003/11/04 17:02:58  jaillon
 * Just some minor modification to this nice version
 *
 * Revision 1.1  2003/11/04 11:13:19  jaillon
 * Add a standalone, http and raw mode
 *
 */
/**
*/
package Drew.Client.Util.XMLSocket;

import Drew.Util.XMLmp.*;
import Drew.Client.TableauDeBord.*;

public class NullSocket implements XMLSocket {
private CubbyHole c = null;
private boolean connected = false;
private CentreDeConnection cdc = null;

	public NullSocket( CentreDeConnection c ) {
		cdc = c;
		connect();
	}

	private void connect() {
		connected = true;
		c = new CubbyHole();
	}

	public void write( XMLTree o ) {
		if( connected == false ) connect();
		//System.err.println("NullSocket write");
		o = new XMLTree( "event",
			XMLTree.Attributes(
				"user", cdc.nom,
				"room", cdc.room
			),
			o.contents()
		);
		c.put( o );
	}

	public XMLTree read() {
		if( connected == false ) connect();
		//System.err.println("NullSocket read");
		return (XMLTree)c.get();
	}

	public void flush() {
	}

	public void close() {
		connected = false;
		c = null;
	}
}
