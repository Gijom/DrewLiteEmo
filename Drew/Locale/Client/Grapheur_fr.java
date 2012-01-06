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
 * File: Grapheur_fr.java
 * Author:
 * Description:
 *
 * $Id: Grapheur_fr.java,v 1.13.2.1 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Grapheur_fr extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Espace graphique pour argumenter"},
		{"LabelList", "Participants en jeu :"},
		{"getActive", "Participer"},
		{"getPassive", "Juste \u00e9couter"},
		{"ButtonNewBox", "Nouvelle Bo\u00eete"},
		{"ButtonNewRel", "Nouvelle Fl\u00e8che"},
		{"ButtonDelete", "Supprimer"},
		{"ButtonMore", "Modifier..."},
		{"LabelPro", "Pour"},
		{"LabelContra", "Contre"},
		{"ButtonQuit", "Quitter"},
		{"ButtonPrint", "Imprimer"},
		{"ButtonEditUsers", "Liste des utilisateurs"},
		{"ButtonAutoLayout", "Placement automatique"},
		//EDIT USER DIALOG
		{"EUWindowTitle", "Gestion des Participants"},
		{"ButtonNew","Nouveau"}, 
		{"EULabelList","Liste des participants :"},
		{"LabelEngaged", "En jeu"},
		{"EUButtonEdit", "Modifier"},
		{"ButtonClose", "Fermer"},
		{"ButtonSave", "Enregistrer"},
		{"ButtonCancel", "Annuler"},
		//MORE INFO DIALOG
		{"MILabelBox","Contenu"},
		{"MILabelRelation","Type de fl\u00e8che :"},
		{"MILabelComment","Commentaire"},
		{"MILabelPro","Participants pour"},
		{"MILabelContra","Participants contre"},
		{"LabelRelation0","Ind\u00e9finie [?]"},
		{"LabelRelation1","Soutien [+++]"},
		{"LabelRelation2","Soutien [++]"},
		{"LabelRelation3","Soutien [+]"},
		{"LabelRelation4","Soutien [=]"},
		{"LabelRelation5","Attaque [-]"},
		{"LabelRelation6","Attaque [--]"},
		{"LabelRelation7","Attaque [---]"},
		{"SymRelationComposedOf","Est composé de"},
		{"LabelRelationComposedOf","Est composé de"},
		{"SymRelationIsA","Est un"},
		{"LabelRelationIsA","Est un"},
		{"SymRelationDesignates","Designe"},
		{"LabelRelationDesignates","Designe"},
		{"SymRelationDepends","Dépend de"},
		{"LabelRelationDepends","Dépend de"},
		// ARGUMENT
		{"CommentPrompt","Peux-tu en dire un peu plus ?"},
		{"NewArgument","Nouvelle bo\u00eete"},
                // PRINT
                {"print.argument.name", "Nom : {0}" },                         // {0} is name of the box/arrow
                {"print.argument.comment", "Commentaire : {0}" },                   // {0} is comment of the box/arrow
		{"print.argument.creator", "Cr\u00e9\u00e9e par : {0}" },
                {"print.boite.typebox", "Type : Bo\u00eete" },
                {"print.fleche.type", "Type : Relation entre {0} et {1}." }, // {0} or {1} are "(x,y,z)" or fleche.print.nothing 
                {"print.fleche.nothing", "rien" },
		{"print.users.nobody", "(personne)"},
		// END OF MATERIAL TO LOCALIZE
	};
}

