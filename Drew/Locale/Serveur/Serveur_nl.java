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


package Drew.Locale.Serveur;

import java.util.*;

public class Serveur_nl extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"msg000", "VERBINDING TOT STAND GEBRACHT"},
		{"msg001", "{0} zegt tot ziens"},
		{"msg002", "{0} doet mee in {1}"}, 
		{"msg003", "{0} komt binnen in{1}"},
		{"msg004", "{0} verlaat de {1}"},
		{"msg005", "Server heeft geen verbinding meer met {0}"},
		{"msg006", "VERBINDING GEWEIGERD"},
		{"msg007", "Deelnemerslijst:\n"},
		// END OF MATERIAL TO LOCALIZE
	};
}
