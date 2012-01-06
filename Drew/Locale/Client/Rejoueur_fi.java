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
 * File: Rejoueur_fi.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_fi.java,v 1.9 2004/03/01 10:23:38 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_fi extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Toistaminen"},
		{"ButtonApplet","Toista"}, 

		{"LabelList", "Osallistujaluettelo"},
		{"LabelEcouter", "Kuuntele "},
		{"LabelEtat", "YHTEYDEN TILA:"},
		{"LabelMsg", "INFORMAATIOVIESTIT:"},
		{"LabelConnecter", "Yhdistä."},
		{"LabelLigne", "Nykyinen linja"},
		{"LabelSignification", "Nykyisen rivin merkitys"},
		{"ButtonConnect", "Yhdistä"},
		{"ButtonDeconnect", "Ei yhteyttä"},
		{"ButtonQuit", "Poistu"},
		{"ButtonPLAY", "Käynnistä"},
		{"ButtonSTEP", "STEP"},
		{"ButtonSTOP", "Seis"},
		{"ButtonRWD", "Taakse"},

		{"panel.filedialog", "Valitse jälkitiedosto (trace file)" },
		{"panel.check",  "Käytä tätä tiedostoa" },
		{"panel.modules", "Työkalut" },
		{"panel.locale",  "Käyttöliittymän kieli" },
		{"panel.layout",  "Työkalujen näyttäminen" },
		{"panel.all",  "Kaikki yhdessä" },
		{"panel.horizontal",  "Kaksi rinnakkain" },
		{"panel.vertical",  "Kaksi päällekkäin" },
		{"panel.separate",  "Jokainen erikseen" },
		{"panel.print.scale", "Kaavion koko tulostettaessa" },
		{"panel.replay",  "Toista" },
		{"panel.rooms",  "Huoneet" },
		{"panel.participants", "Osallistujat huoneissa" },

		{"msg000", ""},
		{"msg001", "Yhteys katkeaa..."},
		{"msg002", "Yhteys"},
		{"msg003", "Odota"},
		{"msg004", "getting an processing data"},
		{"msg005", "Viesti palvelimelta"},
		{"msg006", "{0} liity"},
		{"msg007", "{0} lopeta"},
		{"msg008", "Viesti palvelimelta :"},
		{"msg009", "{0} Poistu huoneesta {1} ? {2}"},
		{"msg010", " for "},
		{"msg011", "{0} pyytää osallistujaluetteloa"},
		{"msg012", "{0} käyttää työkalua {1}"},
		{"msg013", "viesti ei ole tarkoitettu tähän huoneeseen"},
		{"msg014", "Ei käännöstä saatavilla"},
		{"msg015", "Ei yhteyttä"},
		{"msg016", "Yhdistä"},
		{"msg017", "Palvelin katkaisi yhteyden"}
		// END OF MATERIAL TO LOCALIZE
	};
}

