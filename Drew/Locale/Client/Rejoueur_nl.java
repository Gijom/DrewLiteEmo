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
 * File: Rejoueur_nl.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_nl.java,v 1.9 2004/03/01 10:23:38 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_nl extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Replay control panel"},
		{"ButtonApplet","Naspelen"}, 

		{"LabelList", "Deelnemerslijst"},
		{"LabelEcouter", "Luisteren "},
		{"LabelEtat", "TOESTAND VERBINDING:"},
		{"LabelMsg", "BOODSCHAPPEN:"},
		{"LabelConnecter", "SVP, leg verbinding.				 "},
		{"LabelLigne", "Huidige lijn "},
		{"LabelSignification", "met deze regel wordt bedoeld:"},
		{"ButtonConnect", "Connect"},
		{"ButtonDeconnect", "Afbreken"},
		{"ButtonQuit", "Stop"},
		{"ButtonPLAY", "AFSPELEN"},
		{"ButtonSTEP", "STAP"},
		{"ButtonSTOP", "STOP"},
		{"ButtonRWD", "<<"},

		{"panel.filedialog", "selecteer uw tracefile"},
		{"panel.check", "controleer deze file"},
		{"panel.modules", "modules"},
		{"panel.locale", "taalinterface"},
		{"panel.layout", "layout van de module"},
		{"panel.all", "alles in een"},
		{"panel.horizontal", "dubbel horizontaal"},
		{"panel.vertical", "dubbel verticaal"},
		{"panel.separate", "afzonderlijk"},
		{"panel.print.scale", "diagram Print Scale"},
		{"panel.replay", "opnieuw afspelen"},
		{"panel.rooms", "kamers"},
		{"panel.participants", "deelnemers in de kamers"},

		{"msg000", ""},
		{"msg001", "afbreken..."},
		{"msg002", "verbonden"},
		{"msg003", "wacht even...."},
		{"msg004", "verzamelen en verwerken gegevens"},
		{"msg005", "Boodschap van de server: "},
		{"msg006", "{0} doe met ons mee"},
		{"msg007", "{0} stop"},
		{"msg008", "boodschap van de server: "},
		{"msg009", "{0} verlaat de ruimte {1} voor {2}"},
		{"msg010", " voor "},
		{"msg011", "{0} vraag deelnemerslijst"},
		{"msg012", "{0} gebruik een moduul {1}"},
		{"msg013", "boodschap niet bestemd voor deze ruimte"},
		{"msg014", "geen vertaling beschikbaar"},
		{"msg015", "verbinding verbroken"},
		{"msg016", "svp, maak verbinding."},
		{"msg017", "verbinding afgebroken door de server."}
		// END OF MATERIAL TO LOCALIZE
	};
}

