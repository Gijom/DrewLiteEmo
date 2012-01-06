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
 * File: OutilDessinDirect.java
 * Author:
 * Description:
 *
 * $Id: OutilDessinDirect.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* Definie une sous classe d'outils permettant de tracer des figures de dimension 
* fixe a chaque click de souris.  Une image du trace est attachee au curseur de 
* la souris et permet de posiionner la figure a tracer de facon precise avant de 
* la tamponner sur la zone de dessin.
* Cette classe ne definit qu'un type de comportement; il faut la deriver et 
* surcharger la fonction dessin pour l'utiliser.
* @see Outil#dessin(Graphics g,Donnees donnees)
*/
public class OutilDessinDirect extends Outil {

	/**
	* Rayon de la figure a tracer quelle qu'elle soit.
	* fixee a 10 pixels par default 
	*/
	int rayon=10;

	/**
	* construit un outil avec un bouton comportant une image de fond ou on 
	* pourra representer le dessin trace via l'outil
	* Le curseur de la souris est une main.
	*/
	OutilDessinDirect(Communication cdc, MenuOutils v_barre_menu, 
				String nom, String groupe, String nom_image, Dimension dimension) 
	{
		super(cdc,v_barre_menu,nom, groupe,nom_image,dimension);
		curseur=Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	}

	/**
	* On rentre sur la zone de dessin, on sauvegarde l'ecran.
	*/
	public void action_mouseEntered(Donnees donnees)
	{
		super.action_mouseEntered(donnees);

		Graphics g = donnees.zone_dessin.g_sauvegarde;
		g.drawImage(donnees.zone_dessin.ecriture,0,0,null);
	}

	/**
	* On sort de la zone de dessin, on restitue la sauvegarde de l'ecran.
	*/
	public void action_mouseExited(Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_ecriture;
		g.drawImage(donnees.zone_dessin.sauvegarde,0,0,null);
		donnees.zone_dessin.repaint();

		super.action_mouseExited(donnees);
	}

	/**
	* Bouger la souris sans appuyer sur le bouton permet de positionner le dessin 
	* avant de le tracer definitivement.
	* Le buffer d'ecriture de la zone de dessin est ecrase par le buffer de  sauvegarde 
	* avant de redessiner sur ce buffer d'ecriture a la nouvelle position de la souris.
	* le dessin liee a l'outil est defini dans la fonction de dessin.
	* @see Outil#dessin(Graphics g,Donnees donnees)
	*/
	public void action_mouseMoved(Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_ecriture;
		g.drawImage(donnees.zone_dessin.sauvegarde,0,0,null);
		dessin(g,donnees);
		donnees.zone_dessin.repaint();
	}

	/**
	* Le dessin trace par l'outil est appose sur la zone de dessin lors d'un clic de souris
	* Les donnees sont traitees et envoyees au serveur. l'image est deja la, on la sauvegarde
	* @see WhiteBoard#traiteDonnees(Donnees donnees)
	* @see Communication#envoiserveur(String texte)
	* @see Donnees#compacteEnMessage()
	*/
	public void action_mousePressed (Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_sauvegarde;
		g.drawImage(donnees.zone_dessin.ecriture,0,0,null);
		central_applet.envoiserveur(donnees.compacteEnMessage());
	}

	/**
	* Le dessin trace par l'outil est appose sur la zone de dessin le long du chemin
	* trace par la souris lorsque l'on maintient le bouton enfonce.
	* Les donnees sont traitees et envoyees au serveur.
	* @see WhiteBoard#traiteDonnees(Donnees donnees)
	* @see Communication#envoiserveur(String texte)
	* @see Donnees#compacteEnMessage()
	*/
	public void action_mouseDragged (Donnees donnees)
	{
		// traitedonnees ecrit dans le buffer ecriture
		barre_menu.white_board.traiteDonnees(donnees);
		central_applet.envoiserveur(donnees.compacteEnMessage());
	}

	/**
	* On relache le bouton de la souris, on sauvegarde l'image affichée
	*/
        public void action_mouseReleased(Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_sauvegarde;
		g.drawImage(donnees.zone_dessin.ecriture,0,0,null);
	}
	
	/**
	* lors de la selection de l'outil ce dernier est affecte au champs outil de donnees
	* @see Donnees#outil
	*/
	public void action_directe(Donnees donnees)
	{
		donnees.outil=this;
	}
}
