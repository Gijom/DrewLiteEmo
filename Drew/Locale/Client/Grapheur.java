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
 * File: Grapheur.java
 * Author:
 * Description:
 *
 * $Id: Grapheur.java,v 1.14.2.1 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Locale.Client;
 
import java.util.*;

public class Grapheur extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Graphical Space for Argumentation"},
		{"LabelList", "Engaged participants:"},
		{"getActive", "Participate"},
		{"getPassive", "Just Listen"},
		{"ButtonNewBox", "New Box"},
		{"ButtonNewRel", "New Relation"},
		{"ButtonDelete", "Delete"},
		{"ButtonMore", "More info..."},
		{"LabelPro", "Pro"},
		{"LabelContra", "Contra"},
		{"ButtonQuit", "Quit"},
		{"ButtonPrint", "Print"},
		{"ButtonEditUsers", "Edit users list"},
		{"ButtonAutoLayout", "Automatic Layout"},
		//EDIT USER DIALOG 
		{"EUWindowTitle", "Editing Current Participants"},
		{"ButtonNew","New"}, 
		{"EULabelList", "Available participants:"},
		{"LabelEngaged", "Engaged"},
		{"EUButtonEdit", "Edit"},
		{"ButtonClose", "Close"},
		{"ButtonSave", "Save"},
		{"ButtonCancel", "Cancel"},
		//MORE INFO DIALOG
		{"MILabelBox","Content"},
		{"MILabelRelation","Type of relation:"},
		{"MILabelComment","Comment"},
		{"MILabelPro","Participants Pro"},
		{"MILabelContra","Participants Contra"},
		{"LabelRelation0","Undefined [?]"},
		{"LabelRelation1","Support [+++]"},
		{"LabelRelation2","Support [++]"},
		{"LabelRelation3","Support [+]"},
		{"LabelRelation4","Indifferent [=]"},
		{"LabelRelation5","Attack [-]"},
		{"LabelRelation6","Attack [--]"},
		{"LabelRelation7","Attack [---]"},
		{"SymRelation0","?"},
		{"SymRelation1","+++"},
		{"SymRelation2","++"},
		{"SymRelation3","+"},
		{"SymRelation4","="},
		{"SymRelation5","-"},
		{"SymRelation6","--"},
		{"SymRelation7","---"},
		{"SymRelationComposedOf","Is part of"},
		{"LabelRelationComposedOf","Is part of"},
		{"SymRelationIsA","Is a"},
		{"LabelRelationIsA","Is a"},
		{"SymRelationDesignates","Designates"},
		{"LabelRelationDesignates","Designates"},
		{"SymRelationDepends","Depends on"},
		{"LabelRelationDepends","Depends on"},
		// ARGUMENT
		{"CommentPrompt","Could you say more about it?"},
		{"NewArgument", "New Box"},
                // PRINT
                {"print.argument.name", "Name : {0}" },                         // {0} is name of the box/arrow
                {"print.argument.comment", "Comment : {0}" },                   // {0} is comment of the box/arrow
		{"print.argument.creator", "Created by : {0}" },
		{"print.users.nobody", "nobody"},
                {"print.boite.typebox", "Type : Box" },
                {"print.fleche.type", "Type : Relation between {0} and {1}." }, // {0} or {1} are "(x,y,z)" or fleche.print.nothing 
                {"print.fleche.nothing", "nothing" },
		{"print.users.nobody", "(nobody)"},
		// END OF MATERIAL TO LOCALIZE
	};
}
