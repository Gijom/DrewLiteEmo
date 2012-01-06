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
 * $Id: Socket.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 *
 * $Log: Socket.java,v $
 * Revision 1.3  2003/11/24 10:54:26  serpaggi
 *  - LGPL header for all .java files
 *
 * Revision 1.2  2003/11/05 17:37:13  jaillon
 * Just a correction, now embeded drewlets work again
 *
 * Revision 1.1  2003/11/04 11:13:20  jaillon
 * Add a standalone, http and raw mode
 *
 */
/**
*/
package Drew.Client.Util.XMLSocket;

import java.io.*;
import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

public class Socket implements XMLSocket {

private boolean connected = false;
private java.net.Socket sock = null;
private java.net.InetAddress server = null;
private int port = 32000;

private XMLParser in = null;
private XMLPrinter out = null;

	public Socket(java.net.InetAddress s, int p) throws java.net.UnknownHostException, java.io.IOException {
		server = s; port = p;
		connect();
	}

	private void connect() throws java.net.UnknownHostException, java.io.IOException {
		System.err.println( "connect to " + server + ":" + port );
		sock = new java.net.Socket(server, port);

	//The writing part
		out = new XMLPrinter( new OutputStreamWriter( sock.getOutputStream(),Config.getEncoding()));

	//The reading part
                in = new XMLParser( new InputStreamReader(sock.getInputStream(),Config.getEncoding()));

		connected = true;
	}

	public void write( XMLTree o ) throws java.io.IOException {
		if( connected == false ) connect();
		//System.err.println( "Socket send " + o.toString() );
		out.print( o );
	}

	public XMLTree read() throws java.io.IOException {
		if( connected == false ) connect();
		//System.err.println( "Socket read" );
		return in.parse(0);
	}

	public void close() {
		connected = false;
		try{
			sock.close();
		}
		catch( IOException e ) {}
	}

	public void flush() {
		out.flush();
		//System.err.println( "Socket flush" );
	}
}
