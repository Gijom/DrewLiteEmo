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
 * File: OutilDessinAjuste.java
 * Author:
 * Description:
 *
 * $Id: OutilDessinAjuste.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* Definie une sous classe d'outils permettant de tracer des figures de dimension variable
* en les etirants prealablement a une taille voulue.
* Cette classe ne definit qu'un type de comportement; il faut la deriver et surcharger la 
* fonction dessin pour l'utiliser.
* @see Outil#dessin(Graphics g,Donnees donnees)
*/
public class OutilDessinAjuste extends Outil {

	/**
	* Le constructeur attribue a ce type d'outil un curseur en forme de croix.
	* Le bouton assigne a cet outil comportant une image de fond ou on pourra 
	* representer le dessin trace via l'outil
	*/
	OutilDessinAjuste(Communication cdc, MenuOutils v_barre_menu, 
				String nom, String groupe, String nom_image, Dimension dimension) 
	{
		super(cdc,v_barre_menu,nom,groupe,nom_image,dimension);
		curseur=Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
	}

	/**
	* On est entree dans la zone de dessin et on clic pour commnecer ca figure
	* on sauvegarde d'abord l'image originale.
	*/
	public void action_mousePressed(Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_sauvegarde;
		g.drawImage(donnees.zone_dessin.ecriture,0,0,null);
		g = donnees.zone_dessin.g_ecriture;
		dessin(g,donnees);
		donnees.zone_dessin.repaint();
	}

	/**
	* Bouger la souris en maintenant le bouton enfonce permet de dimensionner le dessin a tracer.
	* Le buffer d'ecriture de la zone de dessin est ecrase par le buffer de  sauvegarde avant de 
	* redessiner sur ce buffer d'ecriture a la nouvelle position de la souris.
	* le dessin liee a l'outil est defini dans la fonction de dessin.
	* @see Outil#dessin(Graphics g,Donnees donnees)
	*/
	public void action_mouseDragged (Donnees donnees)
	{
		Graphics g = donnees.zone_dessin.g_ecriture;
		g.drawImage(donnees.zone_dessin.sauvegarde,0,0,null);
		dessin(g,donnees);
		donnees.zone_dessin.repaint();
	}

	/**
	* Lorsque l'utilisateur relache le bouton de la souris dans la zone de dessin, il 
	* valide la dimension de son trace
	* qui devient definitif. Les donnees sont traitees et envoyees au serveur.
	* @see WhiteBoard#traiteDonnees(Donnees donnees)
	* @see Communication#envoiserveur(String texte)
	* @see Donnees#compacteEnMessage()
	*/
	public void action_mouseReleased (Donnees donnees)
	{
		barre_menu.white_board.traiteDonnees(donnees);
		central_applet.envoiserveur(donnees.compacteEnMessage());
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
