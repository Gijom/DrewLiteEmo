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
 * File: Grapheur_hu.java
 * Author:
 * Description:
 *
 * $Id: Grapheur_hu.java,v 1.9 2004/03/01 10:15:52 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Grapheur_hu extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Grafikus terület az érveléshez"},
		{"LabelList", "Résztvev\u0151k:"},	// o with ''
		{"getActive", "Résztvevõ"},
		{"getPassive", "Csak hallgató"},
		{"ButtonNewBox", "Új téglalap"},
		{"ButtonNewRel", "Új kapcsolat"},
		{"ButtonDelete", "Törlés"},
		{"ButtonMore", "További részletek..."},
		{"LabelPro", "Egyetértek"},
		{"LabelContra", "Nem értek egyet"},
		{"ButtonQuit", "Kilépés"},
		{"ButtonPrint", "iNyomtatvany"},
		{"ButtonEditUsers", "Felhasználók szerkesztése"},
		{"ButtonAutoLayout", "Automatic Layout"},
		//EDITUSER DIALOG 
		{"EUWindowTitle", "Jelenlegi résztvev\u0151k szerkesztése"},
		{"ButtonNew","Új"}, 
		{"EULabelList", "Elérhetõ felhasznalók:"},
		{"LabelEngaged", "Foglalt"},
		{"EUButtonEdit", "Szerkesztés"},
		{"ButtonClose", "Bezárás"},
		{"ButtonSave", "Mentés"},
		{"ButtonCancel", "Mégsem"},
		//MORE INFO DIALOG
		{"MILabelBox","Tartalom"},
		{"MILabelRelation","A kapcsolat típusa:"},
		{"MILabelComment","Megjegyzés"},
		{"MILabelPro","Résztvevõk, akik egyetértenek vele"},
		{"MILabelContra","Résztvevõk, akik nem értenek egyet vele"},
		{"LabelRelation0","Meghatározatlan [?]"},
		{"LabelRelation3","Támogatja [+]"},
		{"LabelRelation5","Ellene van [-]"},
		// ARGUMENT
		{"CommentPrompt","Akar többet mondani errõl ?"},
		{"NewArgument", "Új szövegdoboz"},
                // PRINT
                {"print.argument.name", "Érv : {0}" },
                {"print.argument.comment", "Kommentár : {0}" },
                {"print.boite.typebox", "Típus: Szövegdoboz" },
                {"print.fleche.type", "Típus: Kapcsolat {0} and {1} között." }, 
                {"print.fleche.nothing", "semmi" },
		// END OF MATERIAL TO LOCALIZE
	};
}

