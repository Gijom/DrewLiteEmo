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
		{"LabelConnecter", "Yhdist�."},
		{"LabelLigne", "Nykyinen linja"},
		{"LabelSignification", "Nykyisen rivin merkitys"},
		{"ButtonConnect", "Yhdist�"},
		{"ButtonDeconnect", "Ei yhteytt�"},
		{"ButtonQuit", "Poistu"},
		{"ButtonPLAY", "K�ynnist�"},
		{"ButtonSTEP", "STEP"},
		{"ButtonSTOP", "Seis"},
		{"ButtonRWD", "Taakse"},

		{"panel.filedialog", "Valitse j�lkitiedosto (trace file)" },
		{"panel.check",  "K�yt� t�t� tiedostoa" },
		{"panel.modules", "Ty�kalut" },
		{"panel.locale",  "K�ytt�liittym�n kieli" },
		{"panel.layout",  "Ty�kalujen n�ytt�minen" },
		{"panel.all",  "Kaikki yhdess�" },
		{"panel.horizontal",  "Kaksi rinnakkain" },
		{"panel.vertical",  "Kaksi p��llekk�in" },
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
		{"msg011", "{0} pyyt�� osallistujaluetteloa"},
		{"msg012", "{0} k�ytt�� ty�kalua {1}"},
		{"msg013", "viesti ei ole tarkoitettu t�h�n huoneeseen"},
		{"msg014", "Ei k��nn�st� saatavilla"},
		{"msg015", "Ei yhteytt�"},
		{"msg016", "Yhdist�"},
		{"msg017", "Palvelin katkaisi yhteyden"}
		// END OF MATERIAL TO LOCALIZE
	};
}

