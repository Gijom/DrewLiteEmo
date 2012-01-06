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
 * File: Rejoueur_en.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_en.java,v 1.6 2004/02/04 18:45:17 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_en extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Replay control panel"},
		{"ButtonApplet","Replay"}, 

		{"LabelList", "Participants list"},
		{"LabelEcouter", "Listen "},
		{"LabelEtat", "CONNECTION STATE:"},
		{"LabelMsg", "INFORMATION MESSAGES:"},
		{"LabelConnecter", "Please, connect.				 "},
		{"LabelLigne", "Current line"},
		{"LabelSignification", "Current line meaning."},
		{"ButtonConnect", "Connect"},
		{"ButtonDeconnect", "Disconnect"},
		{"ButtonQuit", "Quit"},
		{"ButtonPLAY", "PLAY"},
		{"ButtonSTEP", "STEP"},
		{"ButtonSTOP", "STOP"},
		{"ButtonRWD", "RWD"},

		{"panel.filedialog", "Select your trace file"},
		{"panel.check", "Check this file"},
		{"panel.modules", "Modules"},
		{"panel.locale", "Interface Language"},
		{"panel.layout", "Module Layout"},
		{"panel.all", "All-in-One"},
		{"panel.horizontal", "Double-Horizontally"},
		{"panel.vertical", "Double-Vertically"},
		{"panel.separate", "Separate"},
		{"panel.print.scale", "Graph Print Scale"},
		{"panel.replay", "Replay"},
		{"panel.rooms", "Rooms"},
		{"panel.participants", "Participants in rooms"},

		{"msg000", ""},
		{"msg001", "Disconnecting..."},
		{"msg002", "Connected"},
		{"msg003", "Please, wait."},
		{"msg004", "Getting and processing data"},
		{"msg005", "Message from server: "},
		{"msg006", "{0} join us"},
		{"msg007", "{0} quit"},
		{"msg008", "Message from server: "},
		{"msg009", "{0} quit room {1} for {2}"},
		{"msg010", " for "},					//delete
		{"msg011", "{0} require participants list"},
		{"msg012", "{0} use {1} module"},
		{"msg013", "Message not for this room"},
		{"msg014", "No translation available"},
		{"msg015", "Disconnected"},
		{"msg016", "Please, connect."},
		{"msg017", "Connection closed by server."}
		// END OF MATERIAL TO LOCALIZE
	};
}

