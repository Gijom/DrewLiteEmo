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
 * File: Responder.java
 * Author: Ph. Jaillon
 * Description: UDP responder for zeroconf clients
 *
 * $Id: Responder.java,v 1.2 2006/10/19 14:44:58 jaillon Exp $
 */

package Drew.Util.UDP;

import java.io.*;
import java.net.*;
import Drew.Serveur.*;

/**
 * This class implements an UDP responder for an automatic discovery of Drew server.
 * Idea is : a client looking for a server send an UDP broadcast packet to the local network
 * The responder send back to the client an answer to the client. Client must used the peer
 * address to conctact the server on the same port but with TCP. In this way, you can 
 * also discover a server on WAN (not only the local network) always it is behind a NAT
 * getway (we don't make the error sending IP address and port in data payload of the
 * answer like FTP).
 * This class uses ZeroConf.responder, ZeroConf.responder.port and ZeroConf.responder.answer
 * properties for its configuration. Default are :
 *      false ZeroConf.responder (disable by default)
 *	Drew.raw.port for ZeroConf.responder.port
 *	and "I am here" for ZeroConf.responder.answer
 *
 * @see Drew.Util.UDP.Lookup
 */
public class Responder extends Thread implements Infos {
	/** 
	 * Port used by the server, Drew.raw.port by default, can be set using ZeroConf.responder.port property 
	 */
	private int port         = Config.getProperty("ZeroConf.responder.port", Config.getRawPort());
	/** 
	 * answer sent to clients, can be modified by with ZeroConf.responder.answer property 
	 */
	private String answer    = Config.getProperty("ZeroConf.responder.answer","I am here");
	private DatagramSocket s = null;

	public Responder() {
		setDaemon( true );	
	}

	/**
	 * The demon responder (running forever). Just send an answer to the peer when it receive a request.
         */
	public void run() {
		byte[] request = new byte[1024];
		DatagramPacket A = null, R = new DatagramPacket(request,request.length);

		try {
			s = new DatagramSocket(port);
			System.err.println("UDP responder started on port " + port );

			while( true ) {
				s.receive( R );
				//A = new DatagramPacket( answer.getBytes(), 0, answer.length(), R.getSocketAddress() );
				A = new DatagramPacket( answer.getBytes(), answer.length(), R.getAddress(), R.getPort() );
				s.send( A ) ;
			}	
		}
		catch( IOException e ) {
			e.printStackTrace();
		}
	}

	/** 
	 * Implement Infos interface for control status 
	 * @see Drew.Server.Infos
	 */
	public String infos() {
                if( isAlive() ) {
                        return "Listening configuration request on UDP port " + port;
                }
                else {
                        return "UDP Responder is dead";
                }
        }
}
