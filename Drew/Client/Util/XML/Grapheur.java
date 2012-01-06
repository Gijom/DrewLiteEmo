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
 * Author: ph. Jaillon
 * Description: Just some usefull method to manipulate XML events
 *
 * $Id: Grapheur.java,v 1.3 2007/02/20 16:03:42 collins Exp $
 */

package Drew.Client.Util.XML;

import Drew.Client.Util.Config;
import Drew.Util.XMLmp.*;


public class Grapheur {

	
	/**
	* Create a text node with lang information from current config
	*/
	static private XMLTree text( String txt ) {

		return new XMLTree("text", 
			XMLTree.Attributes( "lang",Config.getLocale().getLanguage() ),
			XMLTree.Contents( txt)
		);
	}

	/**
	* The displayed content of a box or of an arrow
	*/
	static public XMLTree name( String txt ) {

		return new XMLTree("name", text( txt ) );
	}

	/**
	* The comment of a box or of an arrow
	*/
	static public XMLTree comment( String txt ) {

		return new XMLTree("comment", text( txt ) );
	}

	/**
	* The position of a box or of an arrow
	*/
	static public XMLTree position( int x, int y) {
		return new XMLTree("position",
			XMLTree.Attributes(
				"x", String.valueOf( x ),
				"y", String.valueOf( y )
			),
			XMLTree.Contents()
		);
	}

	/**
	* Ask for the creation of a box with the parameter id
	*/
	static public XMLTree box( String id ) {
		return new XMLTree( "argument" ,
				XMLTree.Attributes(
					"id", id ,
					"action", "create",
					"type", "box"
				),
				XMLTree.Contents()
		);
	}

	/**
	* Ask for the creation of an arrow with the parameter id
	*/
	static public XMLTree arrow( String id ) {
		return new XMLTree( "argument" ,
				XMLTree.Attributes(
					"id", id ,
					"action", "create",
					"type", "arrow"
				),
				XMLTree.Contents()
		);
	}

	/**
	* Ask for the creation of a relation between thes parameters id
	*/
	static public XMLTree relation( String from, String to ) {
		return new XMLTree("relation",
			XMLTree.Attributes(
				"action","create",
				"from",  from,
				"to",    to
			) ,
			XMLTree.Contents()
		);
	}

}

