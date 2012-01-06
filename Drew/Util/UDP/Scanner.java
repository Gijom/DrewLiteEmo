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
 * File: Scanner.java
 * Author: Ph. Jaillon 
 * Description: UDP scanner for zeroconf clients
 *              
 * $Id: Scanner.java,v 1.1 2006/05/02 16:35:41 jaillon Exp $
 */             

package Drew.Util.UDP;
            
import java.util.*;
import java.io.*;
import java.net.*;
import Drew.Client.Util.*;

/**
 * This class provide a way to find a Drew server on the local network.
 * The used discovery method is sendenig is sending an UDP request to local broadcast address 
 * (255.255.255.255) and using the source address and source port of the answer as the parameter
 * to be used to contact the drew server. 
 * If there is no answer before timeout, it try to scan each address in the local network (as class C) 
 * because in some situation (using VPN for example), broadcast aren't fully sent. 
 * It verify the answer send by a responder by comparing its payload to the ZeroConf.responder.answer property
 * ("I am here" by default).
 *
 * @see Drew.Util.UDP.Responder
 */
public class Scanner {
        /** 
         * Port used by the server, Drew.raw.port by default, can be set using ZeroConf.responder.port property 
         */
        private static int DREW_PORT = Config.getProperty("ZeroConf.responder.port", Config.getRawPort());
        /** 
         * Local Port used, 49152 by default, can be set using ZeroConf.scanner.port property 
	 * but prefer to use dynamic and/or Private port as IANA specified (49152 through 65535)
         */
        private static int LOCAL_PORT = Config.getProperty("ZeroConf.scanner.port", 49152);
        /** 
         * Time out waiting responder, 10 ms by default, can be set using ZeroConf.scanner.timeout property 
         */
        private static int UDP_TIME_OUT = Config.getProperty("ZeroConf.scanner.timeout", 10);
        /** 
         * request sent to responder, can be modified by with ZeroConf.responder.request property 
         */
        private static String request = Config.getProperty("ZeroConf.responder.request","Where are you");
        /** 
         * answer sent by responder to clients, can be modified by with ZeroConf.responder.answer property 
         */
        private static String answer = Config.getProperty("ZeroConf.responder.answer","I am here");

	/**
	 * For test purpose only
	 */
	public static void main( String[] args ) {
		try {
			InetAddress server = findServer();
			System.out.println( "Found a server at " + server );
		}
		catch( IOException e ) {
			System.out.print( "Humm : " + e.getMessage() );
			e.printStackTrace();
		}
	}

	/**
	 * This method look for an UDP responder on the local network. First step is sending an UDP request
	 * to local broadcast address (255.255.255.255). If there is no answer, we scan each address because
	 * in situation like using VPN or firewalls, broadcast aren't fully sent. 
	 * It verify the answer send by responder which must be equal to the ZeroConf.responder.answer property
	 */
	public static InetAddress findServer()  throws SocketException, UnknownHostException, IOException {
		String str = null;
		byte[] msg = new byte[1024];
		DatagramPacket R = null, A = new DatagramPacket(msg,msg.length);
		DatagramSocket s = null;

		try {
			s = new DatagramSocket();
			R = new DatagramPacket( request.getBytes(), 0, request.length(), InetAddress.getByName( "255.255.255.255"),32000);
			s.send( R ) ;
			s.setSoTimeout( 150 );

			try {
				s.receive( A );
				str = new String( A.getData(), 0, A.getLength() );
				if( answer.equals( str ) ) {
					return A.getAddress();
				}
			}
			catch( SocketTimeoutException e ) { 
			}
			s.close();
		}
		catch( IOException ee ) {
		}

		System.err.println( "UDP broadcast lookup failed, try scanning method" );

		// We look at each network interface configured
		for( Enumeration e  = NetworkInterface.getNetworkInterfaces(); e.hasMoreElements(); ) {
			NetworkInterface i = (NetworkInterface)e.nextElement();
			// And now for each address assigned
			for( Enumeration ee = i.getInetAddresses(); ee.hasMoreElements(); ) {
				InetAddress a = (InetAddress)ee.nextElement();
				byte T[] = a.getAddress();

				// Just do it for IPV4 binded interfaces
				if( T.length != 4 ) continue;

				// choose an unused source port
			        boolean ok = false;
                        	while( ok==false ) {
                                	try {
						s = new DatagramSocket(LOCAL_PORT,a);
						s.setSoTimeout( UDP_TIME_OUT);
                                        	ok = true;
                                	}
                                	catch(java.net.BindException eee) {
                                        	LOCAL_PORT++;
                                	}
                        	}

				for( int b=1; b<255;b++) { // may be, we can have pb if we are not in a class C network, 
							   // or if netmask isn't 255.255.255.0
					T[3]=(byte)b;
		
					R = new DatagramPacket( request.getBytes(), 0, request.length(), InetAddress.getByAddress(T),DREW_PORT);
					s.send( R ) ;
					try {
						s.receive( A );
						str = new String( A.getData(), 0, A.getLength() );
						if( answer.equals( str ) ) {
							return A.getAddress();
						}
					}
					catch( SocketTimeoutException eeee ) { 
					}
				}
				s.close();
			}
		}
		throw new SocketException("Can't find responder");
	}
}
