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
 * File: 
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Sioux.java,v 1.17 2007/02/20 16:03:41 collins Exp $
 * 
 * * $Log: CentreDeConnection.java,v $
 *
 * Revision 1.17  2011/01/04 15:53:58  Guillaume Chanel (GC)
 * management of several time information in the time stamp
 *
 */

package Drew.Serveur;

import java.util.*;
import java.io.*;

import Drew.Util.XMLmp.*;
import Drew.Serveur.Control.*;

/**
* Thread managing connexion with a client
*/
public class Sioux extends Thread {

	/**
	* Current onnexions number
	*/
	public static int count;

	/**
	* Represente un client
	* @see Connexion
	*/
	public Connexion theConnection;

	/**
	* Table de hachage contenant les RoomServer par sujet
	* lors de la phase initiale de la connexion, le thread s'inscrit pour un sujet donne
	* @see SubjectServer
	*/
	public static SubjectServer subjects = new SubjectServer();

	/**
	* Acces au tracefile de trace (partage entre les participants au sujet)
	*/
	private TraceFile tracefile = null;

	/**
	* le gestionnaire de salles
	*/
	private RoomServer rooms = null;

	/**
	* A XML object for time stamping of events
	*/
	static private XMLTree stamp = XML.time();

	/**
	 * The connexion state ( initialized true/false )
         */
	private boolean initialized = false;

	/**
	* The value of the current user language
	*/
	//private String lang= "en";
	//private Drew.Util.Locale comment = new Drew.Util.Locale("Drew.Locale.Serveur.Serveur",new java.util.Locale(lang,"xx","xx"));;
	
	/**
	* Constructeur pour une nouvelle demande de connection
	* @param c Connection to use
	*/
	public Sioux(Connexion c) {
		theConnection = c;
		setDaemon(true);
	}
	  
	/**
	* Handle the control messages
	*/
	private void doServerCmd( XMLTree ctl ) throws ConnectionException, IOException {

		// We need to initialize a new connexion
		if( initialized == false ) {

			String user, room, sujet, lang;

			try {
				// assuming connexion state is "new"
				XMLTree cnx = ctl.getByTagName( "connection" );

				// user id
				user = cnx.getByTagName( "identity" ).getAttributeValue( "user","unknown" );
				theConnection.setName(user);
	
				// subject id
				sujet   = cnx.getByTagName( "subject" ).getText();
				theConnection.setSubject(sujet);

				// client used langage
				lang = cnx.getByTagName( "language" ).getAttributeValue("lang","en");
				//comment = new Drew.Util.Locale("Drew.Locale.Serveur.Serveur",new java.util.Locale(lang,"xx","xx"));
				theConnection.setLang( lang );

				if( Config.Debug(Config.MESSAGES) ) System.err.println( user + " " + sujet + " " + lang);

				// inscription au pres du SubjectServer pour le sujet courant,
				// on obtient le room server qui va bien. c'est la qu'il est initialise
				// pas ailleur et pas avant.
				Subject subject = subjects.add( sujet );
				rooms = subject.getRoomServer();
				tracefile = subject.getTraceFile();

				// Verifie si deux connections ne proviennent pas de la meme machine
				// ou si deux utilisateurs n'ont pas le meme nom

				room = cnx.getByTagName("room").getAttributeValue("where","Hall");
				theConnection.setRoom( room );
	
				if (rooms.verifie(theConnection) == true ) {
					throw new NoConnectedException();
				}
		    
				// inetAdess.toString() resolve the name for printing, may be slow
				// in some case when DNS server can't solve or can't connect.
				// in java/net/InetAdress.java, toString source is 
				// return getHostName() + "/" + getHostAddress();
				// Don't use the cached value, it can't be set a init time
				cnx.insert( new XMLTree( "host", theConnection.getInetAddress().getHostAddress() ) );

				initialized = true; 

				// DEMANDE DE CONNECTION ACCEPTEE
        			theConnection.averti( XML.message( "msg000") );
 				//<user>  nous a rejoint dans le  <room>
				rooms.sendMessage(room, XML.info( XML.message("msg002", user, room )));

				// Now, add the connection in the room
				rooms.add(room,theConnection);

				System.err.println("Start connection : "+ theConnection );
			}
			catch( NoSuchElementException e ) {
				// somme thing is wrong in the connexion initialisation
				if(Config.Debug(Config.STACKTRACE)) e.printStackTrace();
				throw new NoConnectedException();
			}
		}

		// Now connexion is initialised, look at all the others elements in the contol tag

		XMLTree m = null;
		String op;

		for (Enumeration e = ctl.elements() ; e.hasMoreElements() ;) {

			Object o = e.nextElement();
			if( !( o instanceof XMLTree ) ) continue;

			m = (XMLTree)o; op = m.tag();

			if(Config.Debug(Config.MESSAGES)) System.err.println("doServerCmd " + op + ", " + m );
			// end of connection
			if (op.equalsIgnoreCase("connection") ) {
				if( m.getAttributeValue("state","end").equals("end") ) {
					initialized = false; 
					throw new EndConnectionException();
				}
			}
			// request the particpant list
			else if (op.equalsIgnoreCase("list")) {
				rooms.getConnectionList(theConnection.getRoom()).participantList(theConnection);
			} 
			// Ask for changing room
			else if (op.equalsIgnoreCase("room")) {
				String oldRoom = theConnection.getRoom();
				String newRoom = m.getAttributeValue("where","Hall");

				// do nothing if stay in the same room
				if( oldRoom.equals( newRoom ) ) continue;

				// Suppress connexion from current room
				rooms.remove(oldRoom, theConnection);

				// <user> quite le <room>
				rooms.sendMessage(oldRoom, XML.info( XML.message( "msg004", theConnection.getName(), oldRoom )));
				// <user> entre dans le <room>
				rooms.sendMessage(newRoom, XML.info( XML.message("msg003", theConnection.getName(), newRoom)));

				// Add connexion in the new room
				theConnection.setRoom(newRoom);
				rooms.add(newRoom,theConnection);
			}
			// synchronisation request
			else if (op.equalsIgnoreCase("syn")) {
				tracefile.sendState( theConnection.getRoom(), theConnection , m);
			}
		}
	}

	/**
	* Main loop for this new connection
	*/
	public void run() {

		XMLTree m = null;
		XMLTree endMsg = null;

		// add this new thread to the control application
		Config.getApp().add( this );

		count++;
		System.out.println("New Sioux started. Nb. users : "+ count);
		try {
			InputStream is = theConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is, Config.getEncoding());

			XMLParser parser = new XMLParser( isr );
			XMLTree event = null;

			while ( true ) { 

				event = parser.parse(0);
			
				if(event == XMLParser.PEOF) throw new EndConnectionException();
				if(event == XMLParser.PERROR) continue;

				event.delimiters(2);
				if( !event.tag().equalsIgnoreCase( "event-request" ) ) {
					System.err.println( "Warning - strange event : " + event.toString() );
					continue;
				}

				//transform event-request to event, add user and room attributes
				XMLTree new_event = new XMLTree( "event", 
					XMLTree.Attributes(
						"user", theConnection.getName(),
						"room", theConnection.getRoom()
					),
					XMLTree.Contents() 
					//event.contents() 

				);

				//look at event attribute, and copy it 
				for( Enumeration e = event.attributes(); e.hasMoreElements(); ) {
					Pair o = (Pair)e.nextElement();
					new_event.setAttribute( o);
				}
				//event = new_event;


				if(Config.Debug(Config.MESSAGES)) System.err.println( "process event "  + event.toString() );
				if(Config.Debug(Config.MESSAGES)) System.err.println( "new event "  + new_event.toString() );

				// create a new time stamper
				stamp = XML.time();

				// For each part of the event
				for (Enumeration e = event.elements() ; e.hasMoreElements() ;) {

                        		Object o = e.nextElement();
                        		if( !( o instanceof XMLTree ) ) continue; 

					m = (XMLTree)o;
					String op = m.tag();

					try {
		
						if( op.equalsIgnoreCase("time") )  {
							//stamp.add( new XMLTree( m.contents() ) );
                                                        //GC: the line above was replaced by the for loop bellow to deal with the cases
                                                        //where there is more than one element / content (the XMLTree constructor just keep
                                                        //the first element / content)
                                                        for (Enumeration en = m.elements() ; en.hasMoreElements() ;) {
                                                            stamp.add( en.nextElement() );
                                                        }
							event.remove( m );
						}
						else if ( op.equalsIgnoreCase("server") )  {
							doServerCmd( m ); 
						}
						// For any extensions, just send answer back to the user himself
						else if ( ExternalService.exist( "user",op ) ) {
							System.out.println( "External service (user) " + op );
							String cmd = m.getText();
 							String result = ExternalService.get("user",op).doCmd(cmd); 

							m = new XMLTree( op );
							m.add( result );
							m = XML.event( m );
							m.setAttribute("user", theConnection.getName());
							theConnection.sendMessage( m );
						}
						// For any extensions, send answer to any body connected in the room
						else if ( ExternalService.exist( "room",op ) ) {
							String cmd = m.getText();

							System.out.println( "External service (room) " + op  + " ["+cmd+"]");

							Service s = ExternalService.get( "room",op );
							s.setCmd( cmd );
							
							m = XML.event( new XMLTree( op, s ) );
							m.setAttribute("user", theConnection.getName());
							rooms.sendMessage(theConnection.getRoom(), m );
						}
						// for all others kind of event, send it back to each people in the room
						else {
							new_event.setContents( m );
							rooms.sendMessage(theConnection.getRoom(), new_event );
						}
					}
					catch(IOException eee) {
						System.err.println("Error - Tracefile problem : "+eee);
						if(Config.Debug(Config.STACKTRACE)) eee.printStackTrace();
					} 
					catch(NullPointerException ee) {
						System.err.println("Error - Unreadable message : "+ m.toString());
						if(Config.Debug(Config.STACKTRACE)) ee.printStackTrace();
					}
				}
	
				// Time stamp the event and save it in the tracefile
				//event.setAttribute("user", theConnection.getName());
				//new_event.setContents( new XMLTree( event.contents() ) );
				//new_event.setContents( store.contents() );
				if(Config.Debug(Config.MESSAGES)) System.err.println( "store1 event "  + event.toString() );
				event.setTagName( "event" );
				if(Config.Debug(Config.MESSAGES)) System.err.println( "store2 event "  + event.toString() );
				event.insert( stamp );
				event.setAttribute("user", theConnection.getName());
				event.setAttribute("room", theConnection.getRoom());

				tracefile.enregistre( event );   
			}
		}
		catch(EndConnectionException e) {
			//System.out.println( "Connection : " + theConnection );
			System.err.println("End connection   : "+ theConnection );
			if(Config.Debug(Config.STACKTRACE)) e.printStackTrace();
			// <user> n'est plus des notres
			endMsg = XML.info( XML.message("msg001", theConnection.getName()));
		} 
		catch(NoConnectedException e) {
			if(Config.Debug(Config.STACKTRACE)) e.printStackTrace();
			System.err.println("Error - Not connected client : "+ e);
			// DEMANDE DE CONNECTION REJETEE
			theConnection.averti(XML.message("msg006"));
			endMsg = null;
		} 
		catch (Throwable e) {        
			if(Config.Debug(Config.STACKTRACE)) e.printStackTrace();
			System.err.println("lost connexion with client : "+e);
			// Perte de connection avec <user>
			endMsg = XML.info( XML.message("msg005", theConnection.getName()));
		}
		finally {
			//suppress entry from control app
			Config.getApp().remove( this );

			//on termine la connexion
			try {
				rooms.remove(theConnection.getRoom(),theConnection);

				if(endMsg != null ) {
					rooms.sendMessage(theConnection.getRoom(), endMsg );
					endMsg.insert( stamp );
					tracefile.enregistre( endMsg );
				}	
				subjects.remove( theConnection.getSubject() );
				theConnection.close();
			}
			catch (Exception ee) { }       

			count--;
			System.out.println("one Sioux ended. Nb. users : "+ count);
		}
	}

	/**
	* methods for extern control
	* @see ControlConnection
	*/
	public void end() {
		interrupt();
	}

	public String info() {
		return theConnection.toString();
	}
}

