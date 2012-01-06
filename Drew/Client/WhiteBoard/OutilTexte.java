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
 * File: OutilTexte.java
 * Author:
 * Description:
 *
 * $Id: OutilTexte.java,v 1.5 2003/11/24 10:54:27 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;

/**
* Ecrit du texte dans la zone de dessin a la position specifiee par un click de souris
* indentifiant : "texte"
* @see Outil#nom
*/
public class OutilTexte extends Outil {

	/**
	* Construit un outil pour ecrire avec un bouton comportant une image de fond 
	* et un curseur de souris texte.
	*/
	OutilTexte(Communication cdc, MenuOutils v_barre_menu, 
			String groupe, String nom_image, Dimension dimension) 
	{
		super(cdc,v_barre_menu,"texte",groupe,nom_image,dimension);
		curseur=Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR);
	}

	/** 
	* Chaque lettre tapee est ecrite au fur et a mesure sur la zone de dessin en (nx,ny).
	* La valeur de donnees.nx est incremente a chaque frappe de la largeur de la lettre tapee.
	* Les donnees sont traitees et envoyees au serveur.
	* On appelle la methode locale poseCurseur pour visualiser la position de l'ecriture de 
	* la prochaine lettre.
	* @see #poseCurseur(Donnees donnees)
	* @see WhiteBoard#traiteDonnees(Donnees donnees)
	* @see Communication#envoiserveur
	* @see Donnees
	* @see Donnees#compacteEnMessage()
	*/
	public void action_keyTyped (Donnees donnees) {
		barre_menu.white_board.traiteDonnees(donnees);
		central_applet.envoiserveur(donnees.compacteEnMessage());
		donnees.lx+= donnees.zone_dessin.g_ecriture.getFontMetrics().stringWidth(donnees.texte);
	}

	/**
	* A chaque click de souris, on appose un curseur pour 
	* visualiser la position de la prochaine lettre tapee.
	* @see #poseCurseur(Donnees donnees)
	*/
	public void action_mouseClicked (Donnees donnees)
	{
		//donnees.zone_dessin.g_ecriture.drawImage(donnees.zone_dessin.sauvegarde,0,0,null);
		//donnees.zone_dessin.repaint();
	//	poseCurseur(donnees);
	}

   	/**
   	* lors de la selection de l'outil ce dernier est affecte au champs outil de donnees
   	* @see Donnees#outil
   	*/
	public void action_directe(Donnees donnees)
	{
		donnees.outil=this;
	}

	/**
	* trace le caractere de donnees.caractere a la position (donnees.nx,donnees.ny)
	*/
	public void dessin(Graphics g,Donnees donnees)
	{
		g.setColor(donnees.couleur);

		//PJ 20021203
		//char [] caract= new char [1];
		//caract[0]=donnees.caractere;
		//g.drawChars(caract,0,1,donnees.nx,donnees.ny);
		
		g.drawString(donnees.texte,donnees.lx,donnees.ly);
	}

	/**
	* Trace un trait temporaire horizontal de la largeur d'une lettre a la position 
	* d'ecriture de la prochaine lettre tapee.
	*/
	public void poseCurseur(Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_ecriture;
		g.setColor(donnees.couleur);
		g.drawLine(donnees.nx,donnees.ny,donnees.nx+g.getFontMetrics().charWidth('A'),donnees.ny);
		donnees.zone_dessin.repaint();
	}
}
