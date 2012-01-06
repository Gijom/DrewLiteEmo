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
 * File: Rejoueur_hu.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_hu.java,v 1.9 2004/03/03 09:10:26 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_hu extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Visszajátszó vezérlõpult"},
		{"ButtonApplet","Visszajátszás"}, 

		{"LabelList", "Résztvevõk listája"},
		{"LabelEcouter", "Hallgatóként"},
		{"LabelEtat", "KAPCSOLÓDÁSI ÁLLAPOT:"},
		{"LabelMsg", "TÁJÉKOZTATÓ ÜZENETEK:"},
		{"LabelConnecter", "Kérem várjon, kapcsolat létrehozás.				 "},
		{"LabelLigne", "Aktuális sor"},
		{"LabelSignification", "Aktuális sor értelmezése."},
		{"ButtonConnect", "Kapcsolat létrehozása"},
		{"ButtonDeconnect", "Kapcsolat bontása"},
		{"ButtonQuit", "Kilépés"},
		{"ButtonPLAY", "Lejátszás"},
		{"ButtonSTEP", "Léptetés"},
		{"ButtonSTOP", "Leállítás"},
		{"ButtonRWD", "Visszatekerés"},

		{"panel.filedialog", "Válassza ki a saját fájlját!"},
		{"panel.check", "Ellenorizze ezt a fájlt!"},
		{"panel.modules", "Modulok"},
		{"panel.locale", "A felhasználói felület nyelve"},
		{"panel.layout", "A modul szerkezete"},
		{"panel.all", "Egyben az egész"},
		{"panel.horizontal", "Vízszintesen kett\u0151zve"},
		{"panel.vertical", "Függ\u0151legesen kett\u0151zve"},
		{"panel.separate", "Külön"},
		{"panel.print.scale", "A nyomtatott gráf mérete"},
		{"panel.replay", "Visszajátszás"},
		{"panel.rooms", "Szobák"},
		{"panel.participants", "Jelen vannak"},

		{"msg000", ""},
		{"msg001", "Kapcsolat bontása..."},
		{"msg002", "A kapcsolatot létrehoztam"},
		{"msg003", "Kérem várjon!"},
		{"msg004", "A kapott adatok feldolgozása"},
		{"msg005", "Üzenet a szervertõl: "},
		{"msg006", "{0} csatlakozott hozzánk"},
		{"msg007", "{0} kilép"},
		{"msg008", "Üzenet a szervertõl: "},
		{"msg009", "{0} átment a(z) {1}-bõl/ból a(z) {2}-ba/be"},
		{"msg010", " számára "},				//delete
		{"msg011", "{0} már a résztvevõk között van"},
		{"msg012", "{0} használja a modult {1}"},
		{"msg013", "Üzenet egy másik helyiség számára"},
		{"msg014", "Nincs lehetõség átmenni másik helyiségbe"},
		{"msg015", "A kapcsolatot bontottam"},
		{"msg016", "Elõször kapcsolódjon a szerverhez!"},
		{"msg017", "A szerver a kapcsolatot bontotta."}
		// END OF MATERIAL TO LOCALIZE
	};
}

