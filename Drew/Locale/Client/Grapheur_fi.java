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
 * File: Grapheur_fi.java
 * Author:
 * Description:
 *
 * $Id: Grapheur_fi.java,v 1.11 2004/03/01 10:15:52 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Grapheur_fi extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Argumentoinnin graafinen tila"},
		{"LabelList", "Osallistujat :"},
		{"getActive", "Osallistu"},
		{"getPassive", "Seuraa ainoastaan"},
		{"ButtonNewBox", "Tee uusi laatikko"},
		{"ButtonNewRel", "Tee uusi ympyrä"},
		{"ButtonDelete", "Poista"},
		{"ButtonMore", "Muokkaa sisältöä..."},
		{"LabelPro", "Puolesta"},
		{"LabelContra", "Vastaan"},
		{"ButtonQuit", "Lopeta"},
		{"ButtonPrint", "Tulosta"},
		{"ButtonEditUsers", "Muokkaa osallistujaluetteloa"},
		{"ButtonAutoLayout", "Järjestä kaavio"},
		//EDIT USER DIALOG
		{"EUWindowTitle", "Muokkaa nykyisiä osallistujia"},
		{"ButtonNew","Uusi"}, 
		{"EULabelList","Saatavilla olevat osallistujat :"},
		{"LabelEngaged", "Varattu"},
		{"EUButtonEdit", "Muokkaa"},
		{"ButtonClose", "Sulje"},
		{"ButtonSave", "Tallenna"},
		{"ButtonCancel", "Peruuta"},
		//MORE INFO DIALOG
		{"MILabelBox","Sisältö"},
		{"MILabelRelation","Linkin tyyppi :"},
		{"MILabelComment","Alla voit kehitellä tai täydentää laatikkosi/ympyräsi sisältöä."},
		{"MILabelPro","Osallistujat puolesta"},
		{"MILabelContra","Osallistujat vastaan"},
		{"LabelRelation0","Määrittelemätön [?]"},
		{"LabelRelation1","Vahvistava [+++]"},
		{"LabelRelation2","Vahvistava [++]"},
		{"LabelRelation3","Vahvistava [+]"},
		{"LabelRelation4","Neutraali [=]"},
		{"LabelRelation5","Kriittinen [-]"},
		{"LabelRelation6","Kriittinen [--]"},
		{"LabelRelation7","Kriittinen [---]"},
		// ARGUMENT
		{"CommentPrompt","Haluatko kehitellä tai täydentää"},
		{"NewArgument","Uusi laatikko"},
		// PRINT
		{"print.argument.name", "Nimi : {0}" },
		{"print.argument.comment", "Kommentti : {0}" },
		{"print.boite.typebox", "Tyyppi: Laatikko" },
		{"print.fleche.type", "Tyyppi: Linkki {0} and {1}:n välillä" },
		{"print.fleche.nothing", "ei mitään" },
		// END OF MATERIAL TO LOCALIZE
	};
}

