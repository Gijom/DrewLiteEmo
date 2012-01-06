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
 * File: 
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: XML.java,v 1.3 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Serveur;

import java.util.Locale;
import Drew.Util.XMLmp.*;

/**
* somme usefull methods for messages manipulation
* Use theses methods with an XMLTree or a string as parameter
*/
class XML {

	//private final static Drew.Util.Locale defaultcomment = new Drew.Util.Locale("Drew.Locale.Serveur.Serveur",new Locale("en","xx","xx"));
	private String msg;
	private	Object p0 = null;
	private	Object p1 = null;

	XML( String msg ) {
		this.msg = msg;
	}

	XML( String msg, Object p0 ) {
		this.msg = msg; this.p0 = p0;
	}

	XML( String msg, Object p0, Object p1 ) {
		this.msg = msg; this.p0 = p0; this.p1 = p1;
	}

	public String toString() {
		//if( comment == null ) comment = defaultcomment;

		Drew.Util.Locale comment = CurrentConnection.getLocale();

		if( p0 != null ) {
			if( p1 != null) return comment.format(msg,p0.toString(),p1.toString());
			else return comment.format(msg,p0.toString());
		}
		else return comment.format(msg);
	}

	static public XML message( String msg ) {
		return new XML( msg );
	}

	static public XML message( String msg, Object p0 ) {
		return new XML( msg, p0);
	}

	static public XML message( String msg, Object p0, Object p1 ) {
		return new XML( msg, p0, p1);
	}
// ----------------------------------------------------------------
	static public XMLTree event( Object msg ) {
		return new XMLTree( "event", msg );
	}

	static public XMLTree control( Object msg ) {
		return XML.event( new XMLTree( "control",  msg ) );
	}
		
	static public XMLTree info( Object msg ) {
		return XML.control( new XMLTree( "info", msg ) );
	}
	/**
	* Embed a new Drew.Serveur.Date object in the XML tree.
	* The Date object is evaluated each time it is printed using it toString() method
	*/
        static public XMLTree time() {
		return new XMLTree( "time", new XMLTree( "date", new Drew.Util.Date() ) );
	}
}
