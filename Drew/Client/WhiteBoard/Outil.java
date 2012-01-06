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
 * File: Outil.java
 * Author:
 * Description:
 *
 * $Id: Outil.java,v 1.4 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;

/**
* La classe Outil defini un outil de dessin pour une application de type whiteboard.
* Elle est obligatoirement reliee a une applet mere et a une classe de type Container
* permettant l'affichage du bouton declenchant la fonctionnalite de l'outil et gerant
* les evenements de souris sur ce bouton.
*/
public class Outil {

	/** applet mere */
	//protected final Communication central_applet;
	protected Communication central_applet;
	/** barre d'outil d'affichage et centre de controle du bouton de declanchement de l'outil */
	protected MenuOutils barre_menu;
	/** Bouton d'appel de l'outil */
	//public final BoutonOutil bouton;
	public BoutonOutil bouton;
	/** appartenance de l'outil a un groupe bien determine */
	public String groupe;
	/** chaine de caractere identifiant l'outil */
	public String nom;
	/** curseur personnalise associe a l'outil dans la zone de dessin */
	protected Cursor curseur;

	/**
	* Constructeur pour un outil beneficiant d'un bouton de couleur
	*/
	Outil(Communication cdc, MenuOutils v_barre_menu, 
			String v_nom, String v_groupe, 
			Color couleur, Dimension dimension) 
	{
		central_applet= cdc;
		barre_menu = v_barre_menu;
		groupe=v_groupe;
		nom=v_nom;
		curseur=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		bouton=new BoutonOutil(this, couleur,dimension);
		// active le bouton et l'affiche dans la barre d'outil;
		this.bouton.addMouseListener(barre_menu);
		barre_menu.add(this.bouton);
	}

	/**
	* Constructeur pour un outil beneficiant d'un bouton avec image de fond
	*/
	Outil(Communication cdc, MenuOutils v_barre_menu, 
			String v_nom, String v_groupe, 
			String nom_image, Dimension dimension )
	{
		Image image_bouton;
		central_applet= cdc;
		barre_menu = v_barre_menu;
		groupe=v_groupe;
		nom=v_nom;
		curseur=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		if (nom_image!=null) {
			//System.err.println( "Outil : load image " + central_applet.getCodeBase() + "Image/"+nom_image );
			image_bouton = central_applet.getImage(central_applet.getCodeBase(),"Image/"+nom_image);
		}
		else image_bouton=null;

		bouton=new BoutonOutil(this,image_bouton,dimension);
		// active le bouton et l'affiche dans la barre d'outil;
		this.bouton.addMouseListener(barre_menu);
		barre_menu.add(this.bouton);
	}

	/**
	* Fonction a surcharger pour definir la fonction de dessin de l'outil: tracage de 
	* figure geometrique ...
	* Toute ecriture doit etre realisee dans l'environnement graphique g.
	*/
	public void dessin(Graphics g,Donnees donnees) {}

// actions vide par default qui correspondent a ce qui ce passe pour les evenements correspondants sur la zone de dessin
//-----------------------------------------------------------------------------------------------------------
	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de 
	* la pression d'une touche clavier dans la zone de dessin.
	*/
	public void action_keyPressed(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors du 
	* relachement d'une touche clavier dans la zone de dessin.
	*/
	public void action_keyReleased(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de 
	* la frappe d'une touche clavier dans la zone de dessin.
	*/
	public void action_keyTyped(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors du 
	* suivi des mouvements de la souris, bouton relache dans la zone de dessin.
	*/
	public void action_mouseMoved(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors du suivi 
	* des mouvements de la souris, bouton enfonce dans la zone de dessin.
	*/
	public void action_mouseDragged (Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de l'entree 
	* du curseur souris dans la zone de dessin. Par default la fonction change le 
	* curseur de la souris en curseur personnalise de l'outil.
	*/
	public void action_mouseEntered(Donnees donnees)
	{
		donnees.zone_dessin.setCursor(curseur); 
	}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de la 
	* sortie du curseur souris dans la zone de dessin.
	* Par default la fonction retablie le curseur par defaut de la souris et 
	* rafraichie la zone de dessin pour enlever tous les traces temporaires remmanantes .
	*/
	public void action_mouseExited(Donnees donnees)
	{
		donnees.zone_dessin.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR)); 
	}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors du 
	* relachement d'un bouton de souris dans la zone de dessin.
	*/
	public void action_mouseReleased(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors d'un click de 
	* souris dans la zone de dessin.
	*/
	public void action_mouseClicked(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de la 
	* pression d'un bouton souris dans la zone de dessin.
	*/
	public void action_mousePressed(Donnees donnees){}

	/**
	* Fonction a surcharger pour definir le comportement de l'outil lors de sa 
	* selection dans la barre de menu
	*/
	public void action_directe(Donnees donnees){}

}
