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
 * File: Locale.java
 * Author:
 * Description:
 *
 * $Id: Locale.java,v 1.4 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Util;

import java.util.*;
import java.text.*;


/** 
 * Classe encapsulant l'usage de l'internationalisation et melangeant l'usage des 
 * ResourceBundle et des MessageFormat.
 * Le contructeur a la meme semantique que celui de ResourceBundle. Les methodes "format"
 * sont declinees avec 1, 2 ou 3 parametre qui sont des cas courant d'utilisation.
 * 
 * @see java.util.ResourceBundle
 * @see java.util.Locale
 * @see java.text.MessageFormat
 *
 */

public class Locale {

  /**
   * Acces aux messages dans la langue determinee par la localisation
   */
   ResourceBundle comment = null;

	/**
	* Constructeur "a la" ResourceBundle
	* @param name : Nom de la classe de ressource
	* @param locale : description de la localisation
	*/
	public Locale(String name, java.util.Locale locale) {
		comment = ResourceBundle.getBundle(name,locale);
	}	

	public ResourceBundle getBundle() {
		return comment;
	}

	/**
	* Comme avec ResourceBundle, par soucis de compatibilite, mais pluto utiliser format(msg).
	*/
	public String getString(String str) {
		return comment.getString(str);
	}

	/**
	* Comme avec MessageFormat.
	*/
	public String format(String msg, Object[] params ) {
		return MessageFormat.format(comment.getString(msg),params);
	}

	/**
	* Comme avec MessageFormat, mais sans parametre de type Object (tableau vide).
	*/
	public String format(String msg) {
		return comment.getString(msg);
	}

	/**
	* Comme avec MessageFormat, mais avec 1 Object en parametre.
	*/
	public String format(String msg, Object param ) {
	Object[] params = { param };
		return format(msg, params);
	}

	/**
	* Comme avec MessageFormat, mais avec 2 Object en parametre.
	*/
	public String format(String msg, Object a, Object b ) {
	Object[] params = { a,b };
		return format(msg, params);
	}

	/**
	* Comme avec MessageFormat, mais avec 3 Object en parametre.
	*/
	public String format(String msg, Object a, Object b, Object c ) {
	Object[] params = { a,b,c };
		return format(msg, params);
	}
}
