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
 * File: Grapheur_nl.java
 * Author:
 * Description:
 *
 * $Id: Grapheur_nl.java,v 1.10 2004/03/01 10:15:52 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Grapheur_nl extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Grafische Ruimte voor Argumentatie"},
		{"LabelList", "Deelnemers:"},
		{"getActive", "Ik wil meedoen"},
		{"getPassive", "Ik wil alleen toekijken"},
		{"ButtonNewBox", "Nieuw blokje"},
		{"ButtonNewRel", "Nieuwe verbinding"},
		{"ButtonDelete", "Verwijderen"},
		{"ButtonMore", "Aanpassen..."},
		{"LabelPro", "Voor"},
		{"LabelContra", "Tegen"},
		{"ButtonQuit", "Stop"},
		{"ButtonPrint", "Afdrukken"},
		{"ButtonEditUsers", "Toevoegen aan lijst van gebruikers"},
		{"ButtonAutoLayout", "Automatic Layout"},
		//EDITUSER DIALOG 
		{"EUWindowTitle", "Bewerken huidige deelnemerslijst"},
		{"ButtonNew","Nieuw"}, 
		{"EULabelList", "Beschikbare deelnemers:"},
		{"LabelEngaged", "Bezet"},
		{"EUButtonEdit", "Bewerken"},
		{"ButtonClose", "Sluiten"},
		{"ButtonSave", "Opslaan"},
		{"ButtonCancel", "Annuleren"},
		//MORE INFO DIALOG
		{"MILabelBox","Titel"},
		{"MILabelRelation","Soort relatie:"},
		{"MILabelComment","Uitleg"},
		{"MILabelPro","Deelnemers v¢¢r"},
		{"MILabelContra","Deelnemers Tegen"},
		{"LabelRelation0","Niet gedefinieerd [?]"},
		{"LabelRelation1","Ondersteuning [+++]"},
		{"LabelRelation2","Ondersteuning [++]"},
		{"LabelRelation3","Ondersteuning [+]"},
		{"LabelRelation4","Hetzelfde [=]"},
		{"LabelRelation5","Aanval [-]"},
		{"LabelRelation6","Aanval [--]"},
		{"LabelRelation7","Aanval [---]"},
		// ARGUMENT
		{"CommentPrompt","Kun je er iets meer over zeggen?"},
		{"NewArgument", "Nieuw blokje"},
		// PRINT
		{"print.argument.name", "Naam : {0}" },
		{"print.argument.comment", "Opmerking : {0}" },
		{"print.boite.typebox", "Type: BLOKJE" },
		{"print.fleche.type", "Type: Relatie(s) tussen {0} en {1}." },
		{"print.fleche.nothing", "geen" },
		// END OF MATERIAL TO LOCALIZE
	};
}
