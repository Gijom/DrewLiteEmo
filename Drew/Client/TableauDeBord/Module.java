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
 * File: Module.java
 * Author:
 * Description:
 *
 * $Id: Module.java,v 1.13 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.TableauDeBord;

import java.applet.Applet;
import java.awt.event.*;

import Drew.Client.Util.*;
//import Drew.Client.TableauDeBord.*;

/**
 * Affichage des cases a cocher representant les differents modules disponibles
 * (chat, whiteboard, video-jj-conference...)
 * @see TableauDeBord
 */
public class Module extends Applet implements ItemListener
{
	/**
	 * Vecteur de cases a cocher pour le chat, le whiteboard ...
	 */
	java.awt.List list;

	/**
	 * taille par defaut du vecteur de cases a cocher
	 */
	final int DEFAULTSIZE = 10;

	/**
	 * Recupere l'applet-mere
	 */
	CentreDeConnection central_applet;

	/**
	 * Constructeur: Initialisation des cases a cocher
	 * avec lecture des parametres dans la page html
	 */
	public Module(CentreDeConnection cdc) {
		central_applet = cdc;
		list = new java.awt.List( 7, true );

		// Initialisation des cases avec lecture des parametres dans la page html
		for( int i=0; i< Config.listLength(); i++ ) {
			list.add( Config.getComment(i), i);
			String param = cdc.getParameter(Config.getName(i));
			if( (param != null) && param.equals("true") ) { 
				list.select(i);
			}
		}
	}

	/**
	 * La methode qui permet la creation d'un module a partir du nom de la case a cocher.
	 * si ca marche, ca sera merveilleux, pour rajouter un module il suffira juste de 
	 * rajouter une ligne dans le tableau T{ <applet parameter> <checkbox name> <class> }
	 * de la classe Drew.Client.Util.Config
	 * Il faudrait peut-etre rajouter une petite execption dans le cas ou l'on aurait des
	 * merdes.
	 */
	private CooperativeModule newCooperativeModule( String name , CentreDeConnection cdc) {
		// A ameliorer, si on a des milliers de modules, c'est en n2
		for( int i=0; i< Config.listLength(); i++ ) {
			if( name.equals(Config.getComment(i)) ) {
				return Config.newInstance( i, cdc );
			}
		}
		System.err.println( "Module.newCooperativeModule : " + name + " est inconnu, je rend null");
		return null;
	}

	/**
	 * Initialisation du module
	 */ 
	public void init()
	{
		add(list);
		list.addItemListener(this);
	}

	/**
	 * Gestion des changements d'etat des cases a cocher
	 * avec verification de l'etat de la connection au serveur GrandSioux
	 * @param evt evenement
	 * @see CentreDeConnection
	 */
	public void itemStateChanged(ItemEvent evt)
	{
		CooperativeModule m ;
		int index = ((Integer)(evt.getItem())).intValue();
		String name = list.getItem(index);

		if(central_applet.acces_serveur == null) return;

		// Trouver qq chose qui permette de savoir si l'etat du truc a chang�
		if( evt.getStateChange() == ItemEvent.SELECTED ) {
			// For OSX/JAVA 1.4, we verify if the module isn't loaded yet
			if(central_applet.modules.containsKey(name) == true) return;
			m = newCooperativeModule(name, central_applet);
			central_applet.modules.add( name, m );

			m.init(); //m.start();
			
			// a optimiser plus tard en fonction du drewlet charge
			// SYN
			central_applet.getState(); 

			//MQ : start after the SYN
			m.start();

			//central_applet.getState(m.getCode()); 
			System.err.println("Drewlet " + name + " loaded");
		}
		else { 
			if( (m = central_applet.modules.remove( name )) != null ) {
				m.stop(); m = null;
				// to see if it works
				System.runFinalization();
			}
		}
	}

	/**
	 * Initialisation des modules coches lors d'une connection au serveur GrandSioux
	 * @see CentreDeConnection
	 */
	public void demarrer() {
		CooperativeModule m;

		for (int i=0; i<list.getItemCount(); i++) {
			if( list.isIndexSelected(i) ) {
				m = newCooperativeModule(list.getItem(i), central_applet);
				central_applet.modules.add(list.getItem(i), m );
			}
		}
	}

	public void select( String name, boolean state) {
		if( isEnabled() == false ) return; //don't disable element if list isn't enable
                for( int i=0; i< list.getItemCount(); i++ ) {
			if( name.equals( list.getItem(i) ) ) {
                                if(state == true) list.select(i); else list.deselect(i);
                        }
                }
	}
}
