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
 * File: OutilDessinTrace.java
 * Author:
 * Description:
 *
 * $Id: OutilDessinTrace.java,v 1.3 2003/11/24 10:54:27 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* Definie une sous classe d'outils permettant de tracer des figures contigues et de taille variable 
* le long du chemin trace par la souris lors de deplacements bouton enfonce.
* Cette classe ne definit qu'un type de comportement; il faut la deriver et surcharger la fonction dessin pour l'utiliser.
*  @see Outil#dessin(Graphics g,Donnees donnees)
*/
public class OutilDessinTrace extends Outil {

	/**
   * construit un outil avec un bouton comportant une image de fond ou on pourra representer le dessin trace via l'outil
   * Le curseur de la souris est une croix.
   */
	OutilDessinTrace(Communication cdc, MenuOutils v_barre_menu, 
						String nom, String groupe, String nom_image, Dimension dimension) 
		{
		super(cdc,v_barre_menu,nom, groupe,nom_image,dimension);
		curseur=Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
		}
   /**
   * Le dessin trace par l'outil est appose sur la zone de dessin le long du chemin
   * trace par la souris lorsque l'on maintient le bouton enfonce.
   * Les donnees sont traitees et envoyees au serveur.
	* Les coordonnees du click de souris initial (nx,ny) sont ecrasees par les nouvelles coordonnees (lx,ly) 
   * du curseur de la souris (ses donnees sont sauvegardees dans le parametre "donnees").
   * @see WhiteBoard#traiteDonnees(Donnees donnees)
   * @see Communication#envoiserveur(String texte)
   * @see Donnees#compacteEnMessage()
   * @see Donnees
   */
	public void action_mouseDragged (Donnees donnees)
		{
		barre_menu.white_board.traiteDonnees(donnees);
		central_applet.envoiserveur(donnees.compacteEnMessage());
		donnees.nx=donnees.lx;
		donnees.ny=donnees.ly;
		}
	/**
	* lors de la selection de l'outil ce dernier est affecte au champs outil de donnees
	* @see Donnees#outil
	*/
	public void action_directe(Donnees donnees){
		donnees.outil=this;
		}
}
