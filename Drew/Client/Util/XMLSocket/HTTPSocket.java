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
 * File: HTTPSocket.java
 * $Author: jaillon $
 * Description: 
 *
 * $Id: HTTPSocket.java,v 1.4 2004/03/01 10:06:50 jaillon Exp $
 *
 * $Log: HTTPSocket.java,v $
 * Revision 1.4  2004/03/01 10:06:50  jaillon
 * Add name to timer thread
 *
 * Revision 1.3  2004/01/14 13:48:56  jaillon
 * Add error handling for embeded drewlet
 *
 * Revision 1.2  2003/11/24 10:54:26  serpaggi
 *  - LGPL header for all .java files
 *
 * Revision 1.1  2003/11/04 11:13:19  jaillon
 * Add a standalone, http and raw mode
 *
 */
/**
*/
package Drew.Client.Util.XMLSocket;

import java.io.*;
import java.net.*;
import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

public class HTTPSocket implements XMLSocket {

private URL url = null;
private URLConnection U = null;

private XMLParser in = null;
private XMLPrinter out = null;

private boolean closed = false;

private Timer timer;

	class Timer extends Thread {
		HTTPSocket s;
		boolean end = false;
		long stamp; 
		long D = 1000 * 5;

		Timer( HTTPSocket s) {
			super( "HTTPSocket Timer" );
			this.s = s;
			Stamp();
			setDaemon(true);
			start();
		}

		public void Stamp() {
			stamp = System.currentTimeMillis();
		}

		public void run() {
		long now, d = 0;

			while( end != true ) {
				try {
					sleep(D-d);
					now = System.currentTimeMillis();
					d = Math.min(now-stamp, D);
					if( d >= D ) {
						if( Config.getDebug() ) System.err.println("timer : poll " + d);
						try { 
							s.flush(); 
						} catch( IOException e ) { 
							if( Config.getDebug() ) System.err.println( "Timer run : " +  e ); 
							s.close();
						}
						d = 0;
					}
					else if( Config.getDebug() ) System.err.println("timer : no poll " + d);
				}
				catch( InterruptedException e ) {
					e.printStackTrace();
				}
			}
			if( Config.getDebug() ) System.err.println("timer : end");
       		}

		public void end() {
			end = true;
		}
	}

	public HTTPSocket( URL url ) throws IOException {
		timer = new Timer( this );
		this.url = url;
	}

	private synchronized void connect() throws java.io.IOException {

		if( closed == true ) {
			notifyAll(); 
			throw new java.io.IOException( "HTTPSocket closed (no read allowed)" );
		}

		in = null; out = null; //input and output are unavaible

		U = url.openConnection();
		U.setDoOutput( true );
		U.setUseCaches( false );
		
		//set the writing part
		out = new XMLPrinter( new OutputStreamWriter( U.getOutputStream(),Config.getEncoding()));

		timer.Stamp();
		if( Config.getDebug() ) System.err.println( "connected to " + url );
	}

	public synchronized void write( XMLTree o ) throws java.io.IOException {

		while ( true ){
			if( closed == true ) throw new java.io.IOException( "HTTPSocket closed (can't write)" );
			if( in != null ) { // we don't read all the data
				try {
					if( Config.getDebug() ) System.err.println( "Write : waiting for end of input" );
					wait();
				}
				catch (InterruptedException e) {
				}
			}
			else break;
		}

		if( out == null )  connect();

		if( Config.getDebug() ) System.err.println( "HTTPSocket write " + o.toString() );
		out.print( o );
	}

	public synchronized void flush() throws java.io.IOException {

		
		while ( true ) {
			if( closed == true ) throw new java.io.IOException( "HTTPSocket closed (can't flush)" );
			if( in != null ) { // we don't read all the data
				try {
					if( Config.getDebug() ) System.err.println( "flush : waiting for end of input" );
					wait();
				}
				catch (InterruptedException e) {
				}
			}
			else break;
		}

		if( out == null ) connect();

		out.flush();
		//Close the output stream, create the input stream and notify every body waiting for read
		U.getOutputStream().close(); out = null;
		in = new XMLParser( new InputStreamReader(U.getInputStream(),Config.getEncoding()));

		notifyAll();
	}

	public synchronized XMLTree read() throws java.io.IOException {
		XMLTree event = null;
		//System.err.println( "HTTPSocket read" );

		while ( true ) {
			if( closed == true ) throw new java.io.IOException( "HTTPSocket closed (can't read)" );
			if( in == null ) {
				try {
					if( Config.getDebug() ) System.err.println( "read : waiting for end of output" );
					wait();
				}
				catch (InterruptedException e) {
				}
			}
			else break;
		}
		event = in.parse(0);
		if( event == XMLParser.PEOF ) { // end of this http stream, wait for the next one
			if( Config.getDebug() ) System.err.println( "read : all stream read" );
			U.getInputStream().close(); in = null; U = null;

			notify();

			return read();
		}
		else return event;
	}

	public synchronized void close() {
		closed = true;
		out = null; in = null; U = null;
		timer.end();
		notifyAll();
	}
}
