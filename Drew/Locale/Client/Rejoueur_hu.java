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
		{"WindowTitle", "Visszaj�tsz� vez�rl�pult"},
		{"ButtonApplet","Visszaj�tsz�s"}, 

		{"LabelList", "R�sztvev�k list�ja"},
		{"LabelEcouter", "Hallgat�k�nt"},
		{"LabelEtat", "KAPCSOL�D�SI �LLAPOT:"},
		{"LabelMsg", "T�J�KOZTAT� �ZENETEK:"},
		{"LabelConnecter", "K�rem v�rjon, kapcsolat l�trehoz�s.				 "},
		{"LabelLigne", "Aktu�lis sor"},
		{"LabelSignification", "Aktu�lis sor �rtelmez�se."},
		{"ButtonConnect", "Kapcsolat l�trehoz�sa"},
		{"ButtonDeconnect", "Kapcsolat bont�sa"},
		{"ButtonQuit", "Kil�p�s"},
		{"ButtonPLAY", "Lej�tsz�s"},
		{"ButtonSTEP", "L�ptet�s"},
		{"ButtonSTOP", "Le�ll�t�s"},
		{"ButtonRWD", "Visszateker�s"},

		{"panel.filedialog", "V�lassza ki a saj�t f�jlj�t!"},
		{"panel.check", "Ellenorizze ezt a f�jlt!"},
		{"panel.modules", "Modulok"},
		{"panel.locale", "A felhaszn�l�i fel�let nyelve"},
		{"panel.layout", "A modul szerkezete"},
		{"panel.all", "Egyben az eg�sz"},
		{"panel.horizontal", "V�zszintesen kett\u0151zve"},
		{"panel.vertical", "F�gg\u0151legesen kett\u0151zve"},
		{"panel.separate", "K�l�n"},
		{"panel.print.scale", "A nyomtatott gr�f m�rete"},
		{"panel.replay", "Visszaj�tsz�s"},
		{"panel.rooms", "Szob�k"},
		{"panel.participants", "Jelen vannak"},

		{"msg000", ""},
		{"msg001", "Kapcsolat bont�sa..."},
		{"msg002", "A kapcsolatot l�trehoztam"},
		{"msg003", "K�rem v�rjon!"},
		{"msg004", "A kapott adatok feldolgoz�sa"},
		{"msg005", "�zenet a szervert�l: "},
		{"msg006", "{0} csatlakozott hozz�nk"},
		{"msg007", "{0} kil�p"},
		{"msg008", "�zenet a szervert�l: "},
		{"msg009", "{0} �tment a(z) {1}-b�l/b�l a(z) {2}-ba/be"},
		{"msg010", " sz�m�ra "},				//delete
		{"msg011", "{0} m�r a r�sztvev�k k�z�tt van"},
		{"msg012", "{0} haszn�lja a modult {1}"},
		{"msg013", "�zenet egy m�sik helyis�g sz�m�ra"},
		{"msg014", "Nincs lehet�s�g �tmenni m�sik helyis�gbe"},
		{"msg015", "A kapcsolatot bontottam"},
		{"msg016", "El�sz�r kapcsol�djon a szerverhez!"},
		{"msg017", "A szerver a kapcsolatot bontotta."}
		// END OF MATERIAL TO LOCALIZE
	};
}

