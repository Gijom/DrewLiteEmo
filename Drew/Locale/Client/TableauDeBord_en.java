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
 * File: TableauDeBord_en.java
 * Author:
 * Description:
 *
 * $Id: TableauDeBord_en.java,v 1.9 2007/02/28 13:04:30 collins Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class TableauDeBord_en extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Control panel"},
		{"ButtonApplet","Discuss"}, 
		{"LabelName", "Enter your name"},
		{"LabelModules", "Available modules:"},
		{"LabelChangeRoom", "Change room"},
		{"ButtonConnect", "Connect"},
		{"ButtonDeconnect", "Disconnect"},
		{"ButtonClear", "Clear"},
		{"ButtonQuit", "Quit"},
		{"ButtonSubject", "Display subject"},
		{"ButtonHelp", "help"},
		{"ButtonList", "Participants List"},
		{"msg001", "Please, enter your name"},
		{"msg002", "CONTACTING SERVER..."},
		{"msg003", "{0} has joined us in the {1}"},
		{"msg004", "-------------------------------"},
		{"msg005", "WARNING, YOU ARE DISCONNECTED"},
		// ROOMS
		{"Hall",    "Hall"},
		{"Salon",   "Living room"},
		{"Boudoir", "Boudoir"},
		{"Jardin",  "Garden"},
		{"Salle 1", "Room 1"},
		{"Salle 2", "Room 2"},
		{"Salle 3", "Room 3"},
		{"Salle 4", "Room 4"},
		{"Salle 5", "Room 5"},
		{"Salle 6", "Room 6"},
		{"Salle 7", "Room 7"},
		{"Salle 8", "Room 8"},
		{"Salle 9", "Room 9"},
		{"Salle 10", "Room 10"},
		{"Salle 11", "Room 11"},
		{"Salle 12", "Room 12"},
		{"Salle 13", "Room 13"},
		{"Salle 14", "Room 14"},
		{"Salle 15", "Room 15"},
		{"Salle 16", "Room 16"},
		{"Salle 17", "Room 17"},
		{"Salle 18", "Room 18"},
		{"Salle 19", "Room 19"},
		// END OF MATERIAL TO LOCALIZE
	};
}

