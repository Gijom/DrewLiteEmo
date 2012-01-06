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
		{"WindowTitle", "Grafikus ter�let az �rvel�shez"},
		{"LabelList", "R�sztvev\u0151k:"},	// o with ''
		{"getActive", "R�sztvev�"},
		{"getPassive", "Csak hallgat�"},
		{"ButtonNewBox", "�j t�glalap"},
		{"ButtonNewRel", "�j kapcsolat"},
		{"ButtonDelete", "T�rl�s"},
		{"ButtonMore", "Tov�bbi r�szletek..."},
		{"LabelPro", "Egyet�rtek"},
		{"LabelContra", "Nem �rtek egyet"},
		{"ButtonQuit", "Kil�p�s"},
		{"ButtonPrint", "iNyomtatvany"},
		{"ButtonEditUsers", "Felhaszn�l�k szerkeszt�se"},
		{"ButtonAutoLayout", "Automatic Layout"},
		//EDITUSER DIALOG 
		{"EUWindowTitle", "Jelenlegi r�sztvev\u0151k szerkeszt�se"},
		{"ButtonNew","�j"}, 
		{"EULabelList", "El�rhet� felhasznal�k:"},
		{"LabelEngaged", "Foglalt"},
		{"EUButtonEdit", "Szerkeszt�s"},
		{"ButtonClose", "Bez�r�s"},
		{"ButtonSave", "Ment�s"},
		{"ButtonCancel", "M�gsem"},
		//MORE INFO DIALOG
		{"MILabelBox","Tartalom"},
		{"MILabelRelation","A kapcsolat t�pusa:"},
		{"MILabelComment","Megjegyz�s"},
		{"MILabelPro","R�sztvev�k, akik egyet�rtenek vele"},
		{"MILabelContra","R�sztvev�k, akik nem �rtenek egyet vele"},
		{"LabelRelation0","Meghat�rozatlan [?]"},
		{"LabelRelation3","T�mogatja [+]"},
		{"LabelRelation5","Ellene van [-]"},
		// ARGUMENT
		{"CommentPrompt","Akar t�bbet mondani err�l ?"},
		{"NewArgument", "�j sz�vegdoboz"},
                // PRINT
                {"print.argument.name", "�rv : {0}" },
                {"print.argument.comment", "Komment�r : {0}" },
                {"print.boite.typebox", "T�pus: Sz�vegdoboz" },
                {"print.fleche.type", "T�pus: Kapcsolat {0} and {1} k�z�tt." }, 
                {"print.fleche.nothing", "semmi" },
		// END OF MATERIAL TO LOCALIZE
	};
}

