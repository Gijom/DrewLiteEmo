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
 * File: OutilCouleur.java
 * Author:
 * Description:
 *
 * $Id: OutilCouleur.java,v 1.3 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* Outil de selection d'une couleur.
* Identifiant: "couleur"
* @see Outil#nom
*/
public class OutilCouleur extends Outil {

	/** couleur a selectionner */
	private Color couleur;

	/** constructeur de l'outil. Le bouton de controle est un bouton de couleur egale a la couleur a selectionner.*/
	OutilCouleur(Communication cdc, MenuOutils v_barre_menu, 
					String groupe, Color couleur, Dimension dimension) 
		{
		super(cdc,v_barre_menu,"couleur",groupe,couleur,dimension);
		this.couleur=couleur;
		}

	/**
	* attribue la couleur selectionnee au champs couleur de la variable donnees.
	*/
	public void action_directe(Donnees donnees){
		donnees.couleur=this.couleur;
		}
}
