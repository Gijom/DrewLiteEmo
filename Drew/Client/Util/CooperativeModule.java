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
 * File: CooperativeModule.java
 * Author:
 * Description:
 *
 * $Id: CooperativeModule.java,v 1.10 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Util;

import Drew.Util.XMLmp.*;
//import Drew.Client.TableauDeBord.*;
/**
* Interface que doit implementer tout module tel que
* le white board, le chat ...
* 
*/

public interface CooperativeModule {

	/** 
	* Initialisation du module, pour palier l'appel du constructeur sans
	* parametre de Class.newInstance()  
	*/
	public void constructor(Communication cdc);

	/** 
	* Initialisation du module 
	*/
	public void init();

	/** 
	* Le titre que le module veut avoir, par exemple pour sa fenetre
	*/
	public String getTitle();

	/** 
	* arret du module 
	*/
	public void stop();

	/** 
	* warm drewlet network is up
	*/
	public void start();

	/** 
	* effacement du contenu du module 
	*/
	public void clear();

	/** 
	* destruction du module 
	*/
	public void destroy();

	/**
	* Rend la chaine de caractere identifiant le module 
	*/
	public String getCode();

	/**
	* le module indique s'il est destinataire ou non du message identifier par <CODE>code</CODE>
	*/
	public boolean messageFilter(XMLTree event);

	/**
	* traitement des messages : 2 solutions, le message est deja coupe en rondelles, et on ne
	* le delivre qu'aux modules qui ont le bon filtre , ou on le donne a tout le monde, chacun
	* en faisant ce qu'il veut. 
	*/
	public void messageDeliver(String user, XMLTree data);
};
