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
 * File: BoutonOutil.java
 * Author:
 * Description:
 *
 * $Id: BoutonOutil.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;

/**
* la Classe BoutonOutil fournit un bouton graphique. A la difference de la classe standard java.awt.Button, BoutonOutil ne permet pas d'afficher du texte mais propose au choix une couleur de fond unie ou l'affichage d'une image de fond automatiquement redimensionnee a la taille du bouton. 
* Le bouton est de plus entoure de rebords donnant l'illusion de relief pour les etats enfonce/releve.
*/
public class BoutonOutil extends Canvas {

	/** Couleur de fond du bouton */
	public Color couleur_fond;
	/** Image redimensionnee et placee dans le bouton */
	public Image image;
	/**
	* Fonction associee au bouton
	* @see Outil
	*/
	public Outil fonction;
	/** Largeur du bouton */
	//private final int w;
	private int w;
	/** Hauteur du bouton */
	//private final int h;
	private int h;
	/** dimensions du bouton */
	//private final Dimension size;
	private Dimension size;
	/** Etat du bouton (false=non enfonce, true=enfonce) */
	private boolean releve;

	/**
	* Construit un bouton avec une image (si image=null le bouton a un fond blanc)
	* @param fonction Outil associe au bouton
	* @param image image de fond du bouton 
	* @param initial_size dimension du bouton
	*/
	BoutonOutil(Outil fonction, Image image, Dimension initial_size)
	{
		this.fonction=fonction;
		size=initial_size;
		h=size.height;
		w=size.width;
		releve=true;
		couleur_fond=Color.white; // couleur par default si l'image ne se charge pas
		setBackground(couleur_fond);
		if (image!=null) this.image=image.getScaledInstance(w-6,h-6,Image.SCALE_FAST);
		else this.image=null;
	}

	/**
	* Construit un bouton avec un fond de couleur
	* @param fonction Outil associe au bouton
	* @param couleur couleur de fond du bouton
	* @param initial_size dimension du bouton
	*/
	BoutonOutil(Outil fonction, Color couleur, Dimension initial_size)
	{
		this.fonction=fonction;
		size=initial_size;
		h=size.height;
		w=size.width;
		releve=true;
		couleur_fond=couleur;
		setBackground(couleur_fond);
		image=null;
	}

	/** 
	* Donne la taille d'affichage optimale du bouton.
	* Elle est egale a la taille minimale de l'objet.
	* @return cette taille sous forme d'objet Dimension.
	*/
	public Dimension getPreferredSize() {
		return getMinimumSize();
		}

	/** 
	* Donne la taille minimale du bouton.
	* @return cette taille sous forme d'objet Dimension.
	*/
	public Dimension getMinimumSize() {
		return size;
		}

	/**
	* fonction d'affichage du bouton.
	* Application de l'image en fond et ajout de la bordure.
	* @param environnement graphique de l'objet
	*/
	public void paint(Graphics g) { 
		if (image!=null)
			{
			g.drawImage(image, 3, 3, this);
			}
		g.setColor(Color.gray);
		g.draw3DRect(0, 0, getSize().width - 1 , getSize().height - 1,releve);
		g.draw3DRect(1, 1, getSize().width - 3 , getSize().height - 2,releve);
		g.draw3DRect(2, 2, getSize().width - 5 , getSize().height - 2,releve);
	}

	/**
	* active le bouton (change la bordure graphique)
	*/
	public void activer()
		{ releve=false; repaint();}
	/**
	* desactive le bouton (change la bordure graphique)
	*/
	public void desactiver()
		{ releve=true; repaint();}
}
