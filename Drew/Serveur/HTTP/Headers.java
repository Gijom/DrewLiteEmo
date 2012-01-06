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
 * File: Headers.java
 * Author:
 * Description:
 *
 * $Id: Headers.java,v 1.6 2004/01/13 14:35:48 jaillon Exp $
 */

package Drew.Serveur.HTTP;

import java.io.*;
import java.util.*;

import Drew.Util.*;
import Drew.Serveur.*;

/**
* Purpose for this class is interpreting stream as an http connection (see from the server)
*/
public class Headers {
private static final int SZ = 1024;
private Hashtable H = new Hashtable();
private String method   = null;
private String url      = null;
private String protocol = null;

	/**
	* Constructor for headers read the stream to the empty line. It don't construct
	* any buffered think over the stream. At the end, the next byte you read is the first
	* one in the body part of the http request. 
	**/
	public Headers(java.io.InputStream in) throws IOException {
		byte buff[] = new byte[SZ];
		int cnt = 0;
		int cr =0, lf = 0;
		boolean end = false;
		int c;
		BufferedReader r;

		while( !end ) {
			c = in.read();
			if( c != '\n' && c != '\r' ) {
				if( cnt >= buff.length ) {
					byte[] old = buff;
					buff = new byte[old.length * 2];
					System.arraycopy( old,old.length, buff, 0, old.length );
				}
				buff[cnt++] = (byte)c;
				cr = 0; lf = 0;
			}
			else {	// looking for \n\n, \r\r, \n\r\n\r or \r\n\r\n
				buff[cnt++] = (byte)c;

				if ( c == '\n' ) lf++;
				if ( c == '\r' ) cr++;

				if( lf==2  && cr==2 ) end = true;
				else if( lf==2  && cr==0 ) end = true;	
				else if( lf==0  && cr==2 ) end = true;	
			}
		}
		/* remove the empty line */
		cnt -= (lf+cr);

		r  = new BufferedReader( 
				new InputStreamReader( 
					new ByteArrayInputStream( buff, 0, cnt ), 
					Config.getEncoding()
				)
			);

		String l;
		StringToken tok;

		// look at the status line
		l = r.readLine();
		tok = new StringToken( l, " " );
		method = tok.nextToken();
		url    = tok.nextToken();
		protocol = tok.purgeToken();

		while( (l = r.readLine()) != null ) {
System.err.println( "[" + l + "]" );
			tok = new StringToken( l, ":" );
			H.put( tok.nextToken().trim().toLowerCase() , tok.purgeToken().trim() );
		}
	}

	/**
	* Build headers from the socket input
	*/
	public Headers(java.net.Socket s) throws java.io.IOException {
		this( s.getInputStream() );
	}

	/**
	* Ask for the header field with this name
	**/
	public String getField( String name ) {
		return (String)H.get( name );
	}

	/**
	* Ask for used request method (GET, POST ...)
	**/
	public String getMethod() {
		return method;
	}

	/**
	* Ask for requested URL
	**/
	public String getUrl() {
		return url;
	}

	/**
	* Ask for HTTP protocol version
	**/
	public String getProtocol() {
		return protocol;
	}

	public String toString() {
		String key;
		String s = "- [" + getMethod() + "] [" + getUrl() + "] [" + getProtocol() + "]\n-\n";

		for (Enumeration e = H.keys() ; e.hasMoreElements() ;) {
			key = (String)e.nextElement();
			s = s + "- [" + key + "] :	[" + getField( key ) + "]\n";
		}
		return s;
	}
}
