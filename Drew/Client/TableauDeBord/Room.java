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
 * File: Room.java
 * Author:
 * Description:
 *
 * $Id: Room.java,v 1.12.2.1 2007/10/31 14:22:58 collins Exp $
 */

package Drew.Client.TableauDeBord;

import java.util.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import Drew.Util.XMLmp.*;

/**
* Display a pulldown list with accessibles room
* @see TableauDeBord
*/
public class Room extends Applet implements ItemListener {
    	/**
    	* Label for the list "change room"
    	*/
    	Label L = new Label("Changer de piece");

    	/**
    	* The awt Choice component
    	*/
    	Choice x = new Choice();

    	/**
    	* hashtable to translate the local room name to the "network" one
    	*/
    	Hashtable irooms = new Hashtable();

    	/**
    	* get the main applet
    	*/
    	CentreDeConnection central_applet;

    	/**
    	* Constructor
    	* @param cdc the main applet
    	* @see CentreDeConnection
    	*/
    	public Room(CentreDeConnection cdc) {
        	central_applet = cdc;
		L = new Label(cdc.comment.format("LabelChangeRoom"));
    	}
        
    	/**
    	* add a localised item (room) to the List and update the hastable 
	* used for translation
    	*/
    	private void AddIRoom(String n ) {
		String i = central_applet.comment.format(n);

		x.addItem( i ); irooms.put(i, n);
    	}

	/**
	* Initialize a localized room menu and set the initial room
	* to its parameter.
	* Set the room to Hall if there is problems
	*/
    	public void initx(String rm) {
		String IroomName = null;
	
        	add(L);
        	add(x);
	
		/** PJ Add international name to rooms, these values are the exchanged values */
        	AddIRoom("Hall");
        	AddIRoom("Salon");
        	AddIRoom("Boudoir");
        	AddIRoom("Jardin");
        	AddIRoom("Salle 1");
        	AddIRoom("Salle 2");
        	AddIRoom("Salle 3");
        	AddIRoom("Salle 4");
        	AddIRoom("Salle 5");
        	AddIRoom("Salle 6");
        	AddIRoom("Salle 7");
        	AddIRoom("Salle 8");
        	AddIRoom("Salle 9");
        	AddIRoom("Salle 10");
        	AddIRoom("Salle 11");
        	AddIRoom("Salle 12");
        	AddIRoom("Salle 13");
        	AddIRoom("Salle 14");
        	AddIRoom("Salle 15");
        	AddIRoom("Salle 16");
        	AddIRoom("Salle 17");
        	AddIRoom("Salle 18");
        	AddIRoom("Salle 19");

        	x.addItemListener(this);

        	if( rm == null ) {
			rm = "Hall";
		}
	
		try {
			IroomName = central_applet.comment.format(rm);
		}
		catch( MissingResourceException e ) {
			System.err.println("Can't translate room name " + rm);
			IroomName = central_applet.comment.format("Hall");
		}
			   
		x.select( (String)irooms.get(IroomName) );
    	}

    	/**
    	* What to do if user change room.
    	* @see CentreDeConnection
    	*/
    	public void itemStateChanged(ItemEvent evt) {
		String theroom = (String)irooms.get(evt.getItem());
	
        	central_applet.envoiserveur( new XMLTree( "server", new XMLTree( "room" ).setAttribute("where",theroom)));
		central_applet.getState();
        	central_applet.room = theroom;
    	}    

    	/**
    	* Return the current room
    	* @return    the selected room
    	*/
    	public String piece_choisie() {
        	return (String)irooms.get( x.getSelectedItem() );
    	}
}
