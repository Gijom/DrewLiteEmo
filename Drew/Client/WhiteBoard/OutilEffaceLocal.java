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
 * File: OutilEffaceLocal.java
 * Author:
 * Description:
 *
 * $Id: OutilEffaceLocal.java,v 1.4 2003/11/24 10:54:27 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Client.Util.*;


/**
* Gomme
*/
public class OutilEffaceLocal extends OutilDessinDirect {

	// initialisation
	//----------------
	OutilEffaceLocal(Communication cdc, MenuOutils v_barre_menu, 
						  String groupe, String nom_image, Dimension dimension) 
		{
		super(cdc,v_barre_menu,"effacelocal",groupe,nom_image,dimension);
		}
	/**
	* efface un carre centre en (lx,ly) de rayon egal a OutilDessinDirect.rayon
	* @see OutilDessinDirect#rayon
	*/
	public void dessin(Graphics g,Donnees donnees)
		{
		int diametre=rayon+rayon;
		g.setColor( donnees.zone_dessin.getBackground() );
		g.fillRect(donnees.lx-rayon,donnees.ly-rayon, diametre,diametre);
		}
}
