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
 * File: VeryBufferedReader.java
 * Author:
 * Description:
 *
 * $Id: VeryBufferedReader.java,v 1.8.2.1 2007/06/29 08:50:47 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.io.*;
import java.util.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

public class VeryBufferedReader extends BufferedReader implements Runnable {
private Vector v = new Vector( 100, 100 );
private CubbyHole cc;
private boolean ready = false;
private Thread th;

	class CubbyHole {
		private Vector v;
		int count =  0;
		int max   = -1;
		int xx    =  0;

		CubbyHole( Vector v )  {
			this.v = v;
		}

		synchronized void add( XMLTree event ) {
			v.addElement( event );
			if( Config.getDebug(8) ) {
				System.err.println( "Add " + xx + " : " + event.toString() );
				xx++;
			}
			notifyAll();
		}

		synchronized XMLTree get() throws IOException {
		XMLTree event;

			// End of the trace file
			if( count >= max ) {
				if( Config.getDebug(4) ) { System.err.println(  "EOF " + count ); }
				throw new IOException( "VeryBufferedReader EOF" );
			}

			// Can read ?
			while( count > v.size() ) {
				try {
					if( Config.getDebug(8) ) { System.out.println( "Wait " + count + "/" + v.size() ); }
					wait();
				} 
				catch (InterruptedException e) {
				}
			}

			try {
				event = (XMLTree)v.elementAt( count );
				if( Config.getDebug(8) ) { System.err.println(  "Get " + count + " : " + event.toString() ); }
				count++;
			}
			catch( ArrayIndexOutOfBoundsException e) {
				throw new IOException( "VeryBufferedReader may be EOF" );
			}
			return event;
		}

		synchronized void seek( int i ) throws IOException {
			if( i < 0 || i > v.size() ) throw new IOException( "VeryBufferedReader seek out of range" );
			count = i;
		}
/*
		synchronized void add( String str ) {
			v.addElement( "DATA~" + str );
			if( Config.getDebug(8) ) {
				System.err.println( "Add " + xx + " : " + str );
				xx++;
			}
			notifyAll();
		}

		synchronized String get() throws IOException {
		String str;

			// End of the trace file
			if( count >= max ) {
				if( Config.getDebug(4) ) { System.err.println(  "EOF " + count ); }
				throw new IOException( "VeryBufferedReader EOF" );
			}

			// Can read ?
			while( count > v.size() ) {
				try {
					if( Config.getDebug(8) ) { System.out.println( "Wait " + count + "/" + v.size() ); }
					wait();
				} 
				catch (InterruptedException e) {
				}
			}

			try {
				str = (String)v.elementAt( count );
				if( Config.getDebug(8) ) { System.err.println(  "Get " + count + " : " + str ); }
				count++;
			}
			catch( ArrayIndexOutOfBoundsException e) {
				throw new IOException( "VeryBufferedReader may be EOF" );
			}
			return str;
		}
*/

	}

	private InputStreamReader stream;

	public VeryBufferedReader(InputStreamReader r) {
		super( r );
		stream = r;
		//On cree un thread pour lire tout le fichier et le stocker dans v
		cc = new CubbyHole( v );
		th = new Thread(this);
		th.setDaemon(true);
		th.start();
	}

	public void run() {
		XMLParser parser = new XMLParser( stream );
		XMLTree event = null;

		while ( (event = parser.parse(0)) != XMLParser.PEOF ) {
			cc.add( event );
		}
/*
		String str;

		try {
			while( (str = super.readLine()) != null ) {
				cc.add( str );
			}
		}
		catch( IOException e ) {
		}
*/
		cc.max = v.size();
		System.err.println( "All stream readed, max = " + cc.max );
		ready=true;
	}
	
	public void waitReady() {
		while (ready!=true) {
			try {
				this.th.join();
			} catch (InterruptedException ie) {

			}
		}	
	}

	public XMLTree nextEvent() throws IOException {
		return cc.get();
	}

	public void seek( int i ) throws IOException {
		cc.seek(i);
	}
}

