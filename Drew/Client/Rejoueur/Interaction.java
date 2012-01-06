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
 * File: Interaction.java
 * Author:
 * Description:
 *
 * $Id: Interaction.java,v 1.9 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.util.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;

/**
*/
public class Interaction {

private XMLTree event;
private java.util.Date    date;
	
   	Interaction(XMLTree event) {
		this.event = event;

		try {
			XMLTree t = event.getByTagName("time");
			//event.remove( t );	// suppress the time stamping from the event
			date = new java.util.Date( Long.parseLong( t.getByTagName("date").getText() ));
		}
		catch( NoSuchElementException e ) {
			if( Config.getDebug() ) { System.err.println( "Bad event format " + event.toString() ); }
		}
		catch( NumberFormatException e ) {
			if( Config.getDebug() ) { System.err.println( "Bad Date format " + event.toString() ); }
		}
	}

	public String toString() {
		//return  DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Config.getLocale()).format(date) + ":" + event.toString();
		return  event.toString();
	}

	java.util.Date getDate() {
		return date;
	}

	String getName() {
		return event.getAttributeValue("user", "unknown");
	}

	String getRoom() {
		return event.getAttributeValue("room", "unknown");
	}

	XMLTree getXMLTree() {
		return event;
	}

	Enumeration elements() {
		return event.elements();
	}
}
