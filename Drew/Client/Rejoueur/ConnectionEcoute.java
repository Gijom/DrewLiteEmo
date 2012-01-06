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
 * File: ConnectionEcoute.java
 * Author:
 * Description:
 *
 * $Id: ConnectionEcoute.java,v 1.19.2.1 2007/06/18 09:21:36 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.util.*;
import java.io.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;

/**
* L'unique fonction de ce thread est d'ecouter et de dechiffrer les messages du serveur
 * suivant le protocole de serveur Sioux et les formats de donnees des modules partages.
 * @see Drew.Client.WhiteBoard.WhiteBoard
 * @see Drew.Client.Chat.Chat_fenetre
 * @see Drew.Serveur.Sioux
 */
public class ConnectionEcoute extends Thread {

    /** Applet mere d'appel du rejoueur de Drew */
    public CentreDeConnection central_applet;

    /** Buffer de lecture sur la socket */
    private VeryBufferedReader entree = null;

    private PanelManager pn;

    private boolean checkmode = false;

    private boolean stopped = false;

    /**
	* Constructeur, la socket de connection est recuperee du CentreDeConnection en parametre
     * @param cdc CentreDeConnection du rejoueur
     * @see Communication#acces_serveur
     */
    public ConnectionEcoute(CentreDeConnection cdc) throws IOException {
	central_applet=cdc;

	InputStream is = central_applet.url.openStream();
	InputStreamReader isr;

	if( central_applet.encoding != null ) {
	    isr = new InputStreamReader(is, central_applet.encoding);
	}
	else {
	    isr = new InputStreamReader(is);
	}

	entree = new VeryBufferedReader(isr);
	pn = new PanelManager(this,entree);
    }

    public void setCheckMode(boolean b) {
	checkmode = b;
    }

    public void doCommand(long timestamp) {
    	try {
        	pn.goTo(timestamp);
    	}catch (IOException err) {
			
	    }
    }
    
    public void doCommand( String cmd ) {
	if(cmd.equals("PLAY")) {
	    pn.play();
	}
	else if(cmd.equals("STEP")) {
	    pn.step();
	}
	else if(cmd.equals("FFWD")) {
	    pn.ffwd();
	    //int delta = central_applet.trace_length - central_applet.current_event;
	    //pn.step(delta);
	}
	else if(cmd.equals("RWD")) {
	    pn.rwnd();
	}
	else if(cmd.equals("STOP")) {
	    pn.stop();
	}
	else {
	}
    }

    public void jump(int i) {
	pn.step(i);
    }

    public void backTo(int i) {
	pn.rwnd();
	pn.step(i);
    }

    public void check(){
	XMLTree t;
	Interaction event;
	stopped = false;
	boolean EOF = true;
	int num = 0;
	entree.run();
	while( !stopped ) {
	    try {
		t = entree.nextEvent();
		event = new Interaction(t);
		num++;

		String user = event.getName();	// current user
		String room = event.getRoom();	// current room
		String sujet;
		String lang;
		Object o = null;

		for (Enumeration e = event.elements() ; e.hasMoreElements() ;) {
		    try {
			o = e.nextElement();
			XMLTree op = (XMLTree)o;

			if( op.tag().equals("server") ) { // it's a control message
			    for (Enumeration ee = op.elements() ; ee.hasMoreElements() ;) {
				XMLTree cmd = (XMLTree)ee.nextElement();
				if (cmd.tag().equalsIgnoreCase("connection") ) {
				    String state = cmd.getAttributeValue("state","end");
				    if( state.equalsIgnoreCase( "new" )) {
					XMLTree x = null;
					x  = cmd.getByTagName("room");
					if( x != null ) {
					    room = x.getAttributeValue("where","Hall");
					}
					else room = "Unspecified room";

					x  = cmd.getByTagName( "identity" );
					if( x != null ) {
					    user = x.getAttributeValue( "user","unknown" );
					    //System.err.print( user + ", ");
					}
					else user = "Unspecified user";
					sujet = cmd.getByTagName( "subject" ).getText();
					lang  = cmd.getByTagName( "language" ).getAttributeValue("lang", "en");

					//central_applet.nom = user; // no reason to change name -- dyke
					central_applet.addUsedRoom(room);
					central_applet.addUser(user, room);
				    }

				}

				else if( cmd.tag().equalsIgnoreCase("room") ) {
				    // user change room
				    String roomdestination = cmd.getAttributeValue("where","Hall");
				    central_applet.addUsedRoom(roomdestination);
				    central_applet.addUser(user, room);
				    room = roomdestination;
				}

			    }
			}

			else if( op.tag().equals("time") ) {
			    // System.err.println( op.toString() );
			}
			else {
			    // it's messages for modules
				System.err.println("New module found in trace file "+op.tag());
			    central_applet.addUsedModule(op.tag());
			}
		    }
		    catch (NoSuchElementException err) {
		    }
		    catch ( ClassCastException eee) {
	    		System.err.println("Exception : "+ o.toString());
	    		eee.printStackTrace();
			
		    }
		}
	    }
	    catch(java.io.IOException e) {
		if ((e.getMessage() == null) || (!e.getMessage().equals("VeryBufferedReader EOF"))) {
		    EOF = false;
		    System.err.println( "IO error " + e.toString() );
		}
		stopped=true;
		continue;
	    }
	} // fin du while
	System.out.println("Trace checking ended");
	if (EOF) central_applet.updateParams(num);
    }

    /**
	* Lancement du thread.
     * Demande au serveur d'ouvrir un fichier distant puis se met a l'ecoute des messages
     * du serveur.
     * Le thread s'arrete lorsque le serveur ferme la connection.
     */
    public void run() {
	Interaction  event;

	Drew.Util.Locale comment = central_applet.comment;

	// Affichage pour l'utilisateur de l'Etat connecte
	central_applet.frame_tab_de_bord.mis_a_jour("connection",comment.format("msg002")); //connecte.

	// Envoi du nom de fichier a lire
	central_applet.frame_tab_de_bord.mis_a_jour("activite",comment.format("msg003") + central_applet.url.toString() ); //Patientez...

	// lecture ligne a ligne et affichage
	while( !stopped ) {

	    try {
		event = pn.read();
		/***
	    }
		catch(java.io.IOException e) {
		    System.err.println( "IO error " + e.toString() );
		    stopped=true;
		    pn.stop();
		    continue;
		}
		***/

		String user = event.getName();	// current user
		String room = event.getRoom();	// current room
		java.util.Date date = event.getDate(); // current date
		String sujet;
		String lang;

		central_applet.nextEvent();
		
		{
		    StringWriter sw = new StringWriter();
		    XMLPrinter pw = new XMLPrinter( sw );
		    pw.print(event.getXMLTree()); pw.flush();
		    //central_applet.frame_tab_de_bord.mis_a_jour("direct",event.toString());
		    central_applet.frame_tab_de_bord.mis_a_jour("direct",sw.toString());
		}

		for (Enumeration e = event.elements() ; e.hasMoreElements() ;) {
		    try {
			XMLTree op = (XMLTree)e.nextElement();
			if( op.tag().equals("server") ) { // it's a control message
				     //System.err.println( "Yee, it's serveur event");
			    for (Enumeration ee = op.elements() ; ee.hasMoreElements() ;) {
				XMLTree cmd = (XMLTree)ee.nextElement();
				if (cmd.tag().equalsIgnoreCase("connection") ) {
				    //System.err.println( "	it's a connection");
				    String state = cmd.getAttributeValue("state","end");
				    if( state.equalsIgnoreCase( "new" )) {
					//System.err.print( "	state is new ");
     // user join the session
					XMLTree x = null;
					x  = cmd.getByTagName("room");
					if( x != null ) {
					    room = x.getAttributeValue("where","Hall");
					    //System.err.print( room + ", ");
					}
					else room = "Unspecified room";

					x  = cmd.getByTagName( "identity" );
					if( x != null ) {
					    user = x.getAttributeValue( "user","unknown" );
					    //System.err.print( user + ", ");
					}
					else user = "Unspecified user";
					//System.err.println();
					sujet = cmd.getByTagName( "subject" ).getText();
					lang  = cmd.getByTagName( "language" ).getAttributeValue("lang", "en");

					central_applet.frame_tab_de_bord.mis_a_jour(
						 "traduction",
						 date,
						 comment.format("msg006", user)+ " ["+room+", "+sujet+", "+lang+"]");
					//System.err.println( "Set the curent username to " + user);
					//central_applet.nom = user; // no reason to change own name
					central_applet.ajoutpersonne(user,room);
					central_applet.ajoutpiece(room);

				    }
				    else {
					// user quit the session
					central_applet.frame_tab_de_bord.mis_a_jour(
						 "traduction",
						 date,
						 comment.format("msg007", user));
					central_applet.enlevepersonne(user);
					central_applet.enlevepiece(room);
				    }
				}
				else if( cmd.tag().equalsIgnoreCase("list") ) {
				    // user ask for the participant list
				    if (!checkmode) central_applet.frame_tab_de_bord.mis_a_jour(
								    "traduction",
								    date,
								    comment.format("msg011", user));
				}
				else if( cmd.tag().equalsIgnoreCase("room") ) {
				    // user change room
				    String roomdestination = cmd.getAttributeValue("where","Hall");
				    central_applet.frame_tab_de_bord.mis_a_jour("traduction",
						    date,
						    comment.format("msg009",user,room,roomdestination));
				    central_applet.changepiece(user,roomdestination);
				    central_applet.enlevepiece(room);
				    central_applet.ajoutpiece(roomdestination);

				    room = roomdestination;
				}
				else if( cmd.tag().equalsIgnoreCase("syn") ) {
				    // user ask for a synchronisation
				}
				else {
				    // ????
				}
			    }
			}
			else if( op.tag().equals("control") ) {
			    // it's informations ...
       //System.err.println( "It's control message,  " + event.toString() );
			    central_applet.frame_tab_de_bord.mis_a_jour( "traduction",date, op.getText() );
			}
			else if( op.tag().equals("time") ) {
			    // System.err.println( op.toString() );
			}
			else {
			    // it's messages for modules
       //central_applet.modules.DeliverMessage( user, op );
			    central_applet.modules.messageDeliver( user, op );
			    central_applet.frame_tab_de_bord.mis_a_jour(
						   "traduction", date, comment.format("msg012",user,op.tag()));
			}
		    }
		    catch (NoSuchElementException err) {
			central_applet.frame_tab_de_bord.mis_a_jour("traduction",date, comment.format("msg014"));
		    }
		}
	}
	    catch(java.io.IOException e) {
		pn.stop();
	    }
    } // fin du while
}

public void end() {
    stopped = true;
    pn.stop();
}

public List<Long> getEvents() {
	return pn.getEvents();
}
}
