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
 * File: TraceFile.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: TraceFile.java,v 1.16 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur;

import java.io.*;
import java.util.*;

import Drew.Util.XMLmp.*;

/**
* abstraction pour un fichier de trace
*/
public class TraceFile {

	private File f;
	private XMLPrinter trace = null;

    	public TraceFile( String dir, String name) throws IOException {
		f = new File( dir,name );
		setTrace();
    	}

    	public TraceFile( String name) throws IOException {
		f = new File( name );
		setTrace();
	}
	
	private void setTrace() throws IOException {

		if( f.exists() && (!f.canWrite()) ) {
			/* if trace file is read only, we don't create the XML printer and trace field is null. */
			System.err.println("TraceFile : Be carrefull, trace "+f.toString()+" is in read only mode" );
		}
		else {
			trace = new XMLPrinter( 
					new OutputStreamWriter( 
						new FileOutputStream(f.getCanonicalPath(),true), Config.getEncoding()
					)
				);
		}
    	}

	public synchronized void enregistre( XMLTree event ) {
		if (trace != null) {
			trace.print( event );
			trace.flush();
		}
		/* else trace file is read only. */
	}


    	public void close() throws IOException {
		if (trace != null) {
			System.err.println("	Closing tracefile " + f.toString() );
			trace.close(); trace = null;
		}
		/* else trace file is read only. */
	}
	
	/**
	* send context state to client (usefull when connecting or changing room )
	*/
	public synchronized void sendState(String theroom, Connexion conn, XMLTree syn)  throws IOException {

		XMLParser parser = new XMLParser( new InputStreamReader(new FileInputStream(f), Config.getEncoding()));
		XMLTree event;

		while ( (event = parser.parse(XMLParser.STRICT)) != XMLParser.PEOF ) {
			if(event == XMLParser.PERROR) continue;
			// don't send event for others rooms
			if(!theroom.equals( event.getAttributeValue( "room", "very_unknown")))  continue;
			
			XMLTree res = new XMLTree( "event" );
			res.setAttribute( "user", event.getAttributeValue("user","unknown") );
			for (Enumeration e = event.elements() ; e.hasMoreElements() ;) {
				Object o = e.nextElement();
				if( ( o instanceof XMLTree ) ) {
					String op = ((XMLTree)o).tag();

					// don't send server and controls events
					if( op.equalsIgnoreCase("server")  ||
				    	op.equalsIgnoreCase("time")    ||
				    	op.equalsIgnoreCase("control" ) ) continue;

/* send event for this drewlet only (todo)
					if( drewlet == null || NS.equals( drewlet ) || cod_op.equals( drewlet ) ) {
						if( ExternalService.exist( "user",cod_op ) ) {
							data = ExternalService.get("user",cod_op).doCmd(data);
						}
						else if(  ExternalService.exist( "room",cod_op ) ) {
							data = ExternalService.get("room",cod_op).doCmd(data, conn);
						}
*/
					res.add( o );
				}
			}
			//send data only if there is something to send
			if( res.contents().length() > 0 ) conn.send(res);
		}
		parser = null;
		conn.flush();
	}

	public void finalizer() {
		try {
			close();
		}
		catch(IOException e) {}
	}
	
}
