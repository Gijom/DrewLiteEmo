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
 * File: Vote_en.java
 * Author:
 * Description:
 *
 * $Id: Vote_en.java,v 1.9 2004/02/04 18:45:17 jaillon Exp $
 */

package Drew.Locale.Client;

import java.util.*;

public class Vote_en extends ListResourceBundle {
	public Object[][] getContents() {
		return contents;
	}

	static final Object[][] contents = {
		// LOCALIZE THIS
		{"WindowTitle", "Ballot Box"},
		{"PressVote", "Vote"},
		{"RemoveVote", "Cancel"},
		{"Vote", "Vote"},
		{"Expressed", "Expressed"},
		{"FirstVote", "votes for the first time"},
		{"History", "History"},
		{"Welcome", "Expressed"}
		// END OF MATERIAL TO LOCALIZE
	};
}

