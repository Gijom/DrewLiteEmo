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
 * File: MenuOutils.java
 * Author:
 * Description:
 *
 * $Id: MenuOutils.java,v 1.4 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;
import Drew.Client.Util.*;


/**
* Barre d'outil inclue dans une boite de dialogue.
* Trois groupes d'outils sont implementes: les outils a action directe (effacement de toute la zone), les outils de dessin et les outils de selection de couleur.
*/
//public class MenuOutils extends Dialog implements MouseListener {
public class MenuOutils extends Container implements MouseListener {

	/** fenetre appelante */
	public WhiteBoard white_board;
	/** applet mere */
	private Communication central_applet;

	// tous les outils:
	// trois groupes: action immediate (action_*)
	//                dessin (dessin_*)
	//                selection de couleur (col_*)
	//
	/** identifiant du groupe d'outils a action immediate */
	public final String groupe_action="action";
	private Outil action_efface_tout;
	/** identifiant du groupe d'outils de dessin */
	public final String groupe_dessin="dessin";
	private Outil dessin_efface_local;
	private Outil dessin_texte;
	private Outil dessin_pointeur;
	private Outil dessin_droite;
	private Outil dessin_libre;
	private Outil dessin_rectangle_vide;
	private Outil dessin_rectangle_plein;
	private Outil dessin_ovale_vide;
	private Outil dessin_ovale_plein;
	/** identifiant du groupe d'outils de selection de couleur */
	public final String groupe_couleur="col";
	private Outil col_noir;
	private Outil col_blanc;
	private Outil col_rouge;
	private Outil col_jaune;
	private Outil col_vert;
	private Outil col_bleu;

	/** le bouton active dans un groupe de fonctionalite */
	private Hashtable bouton_active = new Hashtable();
	/** Ensemble d'outils du groupe groupe_dessin */
	public Hashtable outils_dessin = new Hashtable();

	/**
	* constructeur de la boite d'outil.
	* @param dw white_board auquel appartient la barre d'outil
	* @param cdc applet "mere" de type Communication
	* @param titre_fenet titre de la boite de dialogue contenant les outils
	*/
	MenuOutils(Container dw, Communication cdc, String titre_fenet) {

		//super(dw,titre_fenet,false);
		super();
		white_board = (WhiteBoard) dw;
		central_applet= cdc;

		// Mise en place des boutons de menu graphique
		//---------------------------------------------
		// les boutons sont positionnes sur une grille permettant de mettre des marges
		GridLayout grid= new GridLayout(8,4,5,5);
		setLayout(grid);

		// couleur de fond 
		setBackground(Color.gray);
		// dimension des boutons
		Dimension dimension_bouton=new Dimension(36,36);

		// outils a action directe (groupe groupe_action)
		action_efface_tout=new OutilEffaceTout(central_applet,this,groupe_action,"effacetout.gif",dimension_bouton);
		// outil du groupe selectionne au demarage
		bouton_active.put(groupe_action,action_efface_tout);
		change_activation(action_efface_tout);

		// outils de dessin (groupe groupe_dessin)
		dessin_efface_local=new OutilEffaceLocal(central_applet,this,groupe_dessin,"gomme.gif",dimension_bouton);
		outils_dessin.put(dessin_efface_local.nom,dessin_efface_local);
		dessin_pointeur=new OutilPointeur(central_applet,this,groupe_dessin,"pointeur.gif",dimension_bouton);
		outils_dessin.put(dessin_pointeur.nom,dessin_pointeur);
		dessin_texte=new OutilTexte(central_applet,this,groupe_dessin,"texte.gif",dimension_bouton);
		outils_dessin.put(dessin_texte.nom,dessin_texte);
		dessin_droite=new OutilDroite(central_applet,this,groupe_dessin,"droite.gif",dimension_bouton);
		outils_dessin.put(dessin_droite.nom,dessin_droite);
		dessin_libre=new OutilDessinLibre(central_applet,this,groupe_dessin,"dessinlibre.gif",dimension_bouton);
		outils_dessin.put(dessin_libre.nom,dessin_libre);
		dessin_ovale_vide=new OutilOvaleVide(central_applet,this,groupe_dessin,"ovalevide.gif",dimension_bouton);
		outils_dessin.put(dessin_ovale_vide.nom,dessin_ovale_vide);
		dessin_rectangle_vide=new OutilRectangleVide(central_applet,this,groupe_dessin,"rectanglevide.gif",dimension_bouton);
		outils_dessin.put(dessin_rectangle_vide.nom,dessin_rectangle_vide);
		dessin_ovale_plein=new OutilOvalePlein(central_applet,this,groupe_dessin,"ovaleplein.gif",dimension_bouton);
		outils_dessin.put(dessin_ovale_plein.nom,dessin_ovale_plein);
		dessin_rectangle_plein=new OutilRectanglePlein(central_applet,this,groupe_dessin,"rectangleplein.gif",dimension_bouton);
		outils_dessin.put(dessin_rectangle_plein.nom,dessin_rectangle_plein);
		// outil du groupe selectionne au demarage
		bouton_active.put(groupe_dessin,dessin_libre);
		change_activation(dessin_libre);

		// boutons de choix de couleur (groupe groupe_couleur)
		col_noir=new OutilCouleur(central_applet,this,groupe_couleur,Color.black,dimension_bouton);
		col_blanc=new OutilCouleur(central_applet,this,groupe_couleur,Color.white,dimension_bouton);
		col_rouge=new OutilCouleur(central_applet,this,groupe_couleur,Color.red,dimension_bouton);
		col_jaune=new OutilCouleur(central_applet,this,groupe_couleur,Color.yellow,dimension_bouton);
		col_vert=new OutilCouleur(central_applet,this,groupe_couleur,Color.green,dimension_bouton);
		col_bleu=new OutilCouleur(central_applet,this,groupe_couleur,Color.blue,dimension_bouton);
		// outil du groupe selectionne au demarage
		bouton_active.put(groupe_couleur,col_noir);
		change_activation(col_noir);

		//Initialize this dialog to its preferred size.
		//--pack();
		//--this.setResizable(false);
	}

   /** 
	* Affichage graphique de la selection d'un outil et mis a jour de l'outil active pour son groupe
	* @see #bouton_active
	* @param a_activer outil a activer
	*/
	private void change_activation( Outil a_activer)
		{
			((Outil)bouton_active.get(a_activer.groupe)).bouton.desactiver();
			a_activer.bouton.activer();
		 	bouton_active.put(a_activer.groupe,a_activer);
		}

	/** 
	* recuperation externe de l'outil selectionne pour un groupe d'outils donne
	* @param groupe groupe dont on cherche l'outil actif
	*/
	public Outil get_bouton_active(String groupe)
		{ return ((Outil)bouton_active.get(groupe)); }

	/**
	* Gestion de la selection d'un outil.
	* Visualisation et changement de l'outil active par l'appel a change_activation(Outil a_activer)
	* Appel a la fonction action_directe(Donnees donnees) de l'outil selectionne
	* @see #change_activation( Outil a_activer)
	* @see Outil#action_directe(Donnees donnees)
	*/
   public void mouseClicked(MouseEvent e){
			BoutonOutil source = (BoutonOutil) e.getSource();
			change_activation(source.fonction);
			source.fonction.action_directe(white_board.donnees);
	}
	/**
	* fonction vide
	*/
   public void mouseReleased(MouseEvent e){}
	/**
	* fonction vide
	*/
   public void mousePressed(MouseEvent e){}
	/**
	* fonction vide
	*/
   public void mouseEntered(MouseEvent e){}
	/**
	* fonction vide
	*/
   public void mouseExited(MouseEvent e){}
	/**
	* fonction vide
	*/
}
