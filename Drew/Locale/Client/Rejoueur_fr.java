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
 * File: Rejoueur_fr.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur_fr.java,v 1.9 2005/07/21 12:44:34 quignard Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Rejoueur_fr extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Rejoueur"},
		{"ButtonApplet","Rejouer"}, 

		{"LabelList", "Liste des Participants"},
		{"LabelEcouter", "Ecouter dans "},
		{"LabelEtat", "ETAT DE LA CONNECTION :"},
		{"LabelMsg", "MESSAGES D'INFORMATION :"},
		{"LabelConnecter", "Connectez-vous."},
		{"LabelLigne", "Ligne en cours de traitement"},
		{"LabelSignification", "Signification de la ligne trait\u00e9e."},
		{"ButtonConnect", "Connecter"},
		{"ButtonDeconnect", "D\u00e9connecter"},
		{"ButtonQuit", "quitter"},
		{"ButtonPLAY", "PLAY"},
		{"ButtonSTEP", "STEP"},
		{"ButtonSTOP", "STOP"},
		{"ButtonRWD", "RWD"},

		{"panel.filedialog", "S\u00e9lectionnez votre fichier de trace"},
		{"panel.check", "Tester cette trace"},
		{"panel.modules", "Modules"},
		{"panel.locale", "Langue de l'interface"},
		{"panel.layout", "Pr\u00e9sentation des modules"},
		{"panel.all", "Tout en un"},
		{"panel.horizontal", "2-Horizontalement"},
		{"panel.vertical", "2-Verticalement"},
		{"panel.separate", "S\u00e9par\u00e9s"},
		{"panel.print.scale", "Echelle d'impression du graphe"},
		{"panel.replay", "Rejouer"},
		{"panel.rooms", "Pi\u00e8ces"},
		{"panel.participants", "Participants pr\u00e9sents"},

		{"msg000", ""},
		{"msg001", "D\u00e9connection en cours..."},
		{"msg002", "connect\u00e9"},
		{"msg003", "patientez"},
		{"msg004", "r\u00e9ception et traitement des donn\u00e9es"},
		{"msg005", "Message originaire du serveur : "},
		{"msg006", "{0} vient de se connecter"},
		{"msg007", "{0} quitte le d\u00e9bat"},
		{"msg008", "message transmis par le serveur : "},
		{"msg009", "{0} passe de la salle {1} \u00e0 {2}"},
		{"msg010", " \u00e0 "},						// a supprimer
		{"msg011", "{0} demande la liste des participants"},
		{"msg012", "{0} utilise le module {1}"},
		{"msg013", "Pas de traitement, concerne une autre pi\u00e8ce"},
		{"msg014", "Pas de traduction disponible"},
		{"msg015", "D\u00e9connect\u00e9"},
		{"msg016", "Connectez vous."},
		{"msg017", "Le serveur vous a d\u00e9connect\u00e9."}
		// END OF MATERIAL TO LOCALIZE
	};
}

