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
 * File: OutilOvalePlein.java
 * Author:
 * Description:
 *
 * $Id: OutilOvalePlein.java,v 1.3 2003/11/24 10:54:27 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* permet de dessiner des ellipses remplies dimensionnables par drag&drop
* indentifiant: "ovaleplein"
*/
public class OutilOvalePlein extends OutilDessinAjuste {

	// initialisation
	//----------------
	OutilOvalePlein(Communication cdc, MenuOutils v_barre_menu, 
						 String groupe, String nom_image, Dimension dimension) 
		{
		super(cdc,v_barre_menu,"ovaleplein",groupe,nom_image,dimension);
		}
   /**
   * trace une ellipse pleine circonscrite dans le rectangle de coins superieur droit et inferieur gauche (nx,ny) et (lx,ly).
   * la couleur du trace est definie par le champs couleur de donnees
   * @param g environnement graphique ou est realisee l'ecriture
	* @see Donnees
   */
	public void dessin(Graphics g,Donnees donnees)
		{
		g.setColor(donnees.couleur);
		g.fillOval(Math.min(donnees.lx,donnees.nx),Math.min(donnees.ly,donnees.ny),
		           Math.abs(donnees.lx-donnees.nx),Math.abs(donnees.ly-donnees.ny));
		}
}
