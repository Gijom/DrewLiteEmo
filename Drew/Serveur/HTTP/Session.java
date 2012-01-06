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
 * File: Session.java
 * Author:
 * Description:
 *
 * $Id: Session.java,v 1.7 2004/01/13 14:35:48 jaillon Exp $
 */

package Drew.Serveur.HTTP;

import java.net.*;
import java.io.*;

import Drew.Serveur.Config;
import Drew.Serveur.ConnectionImpl;


public class Session implements ConnectionImpl {
private static final int BUFFSZ = 1024*4;
private Socket sock = null;
private ByteArrayOutputStream out = new ByteArrayOutputStream( BUFFSZ );
private ByteArrayInputStream   in = null;
private boolean isclosed = false;
private byte[] buff;
private InetAddress addr = null;
private InputStream  inStream = null;
private OutputStream outStream = null;
private String id = null;
private SiouxServer sioux;

	public Session() {
		inStream  = new InputStream( this );
		outStream = new OutputStream( this );
	}

	public Session( Socket s) throws IOException {
		this();
		connect( s );
	}

	public Session( Socket s, Headers h ) throws IOException {
		this();
		connect( s,h );
	}

	public void setId( SiouxServer sioux, String id ) {
		this.sioux = sioux;
		this.id = id;
	}

	public void connect( Socket s ) throws IOException {
		//getting all the http headers
		//We find the content-length header
		connect(s,new Headers( s.getInputStream() ));
	}

	synchronized public void connect( Socket s, Headers H ) throws IOException {
	int rest = available();

		if( sock != null ) { // we are alway connected, flushing data
			System.out.println( "flushing " + sock );
			flush();
		}

		// may be intresting to test the value of the remote address for prevent spoofing
		addr = s.getInetAddress();
		sock = s;

		int content_length;

		content_length = Integer.parseInt( H.getField( "content-length" ) );
/*
		try {
			content_length = Integer.parseInt( H.getField( "content-length" ) );
		}
		catch( NumberFormatException e ) {
			content_length = 0;
			e.printStackTrace();
		}
*/
		boolean isempty = (content_length == 0);

		if( rest > 0 ) { // there are available() unused bytes
			byte[] old = buff;
			buff = new byte[content_length + rest];
			System.arraycopy(old,old.length - rest, buff, 0, rest);
		}
		else {
			buff = new byte[content_length];
		}

		// read all the data from the stream
		int cnt;
		while( content_length > 0 ) {
			cnt = sock.getInputStream().read( buff,rest, content_length );
			rest += cnt;
			content_length -= cnt;
		}
System.out.println( "Session.connect  : available data " + buff.length + " ("+ new String(buff) + ")"  );

		in = new ByteArrayInputStream( buff );

		if( isempty == true ) flush();
		//Now we can notify the reader
		notifyAll();
	}

	synchronized public void flush() throws IOException {
		// Sending data if we can
		if( sock != null ) {
			if( out.size()==0 ) out.write(0); // add a byte, netscape don't like empty answer
System.out.println( "Session.flush  : sending available data ("+ out.toString() + ")"  );
			//write HTTP headers
			String str = "HTTP/1.0 200 ok\nContent-Length: "+out.size() +"\n\n";
			sock.getOutputStream().write( str.getBytes(Config.getEncoding()) );
			//write all data we have
			out.writeTo(sock.getOutputStream());
			sock.close(); sock = null;
			out.reset();
		}
	}

	synchronized public void close() throws IOException {
		System.out.println( "Closing " + sock );
		flush();
		isclosed = true;
		if( sioux != null ) {
			sioux.remove( id );
		}

		notifyAll();
	}

	int available() throws IOException {
System.out.println( "Session.available  : asking for available data (" + ((in == null ) ? -2 :in.available()) + ")"  );

		if(in == null ) {
			return 0;
		}
		else {
			int n = in.available();
			if( (n < 0)  && (isclosed == false) ) return 0;
			return n;
		}
	}

 	synchronized public int read() throws IOException {
		byte[] b = new byte[1];
		int n = read( b, 0, 1 );
		if (n <= 0) {
            		return -1;
        	}
        	return b[0] & 0xff;
	}

	synchronized public int read(byte b[]) throws IOException {
		return read( b, 0, b.length );
	}

	synchronized public int read(byte b[], int off, int len) throws IOException {
		while( true ) {
			if( isclosed == true ) return -1;
			if( available() == 0 ) {
				try {
					System.out.println( "Session.read  : Waiting for more data" );
					wait();
				}
				catch (InterruptedException e) {
					throw new IOException( "Session.read interupted" );
				}
			}
			else break;
		}
		return in.read( b, off, len);
	}

 	public void write(int b) throws IOException {
		// All outgoing bytes are stored
		out.write( b );
	}
	
	public InetAddress getInetAddress() {
		return addr;
	}

	public java.io.InputStream getInputStream() {
		return (java.io.InputStream)inStream;
	}

	public java.io.OutputStream getOutputStream() {
		return (java.io.OutputStream)outStream;
	}
}
