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
 * File: OutilPointeur.java
 * Author:
 * Description:
 *
 * $Id: OutilPointeur.java,v 1.5 2003/11/24 10:54:27 serpaggi Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;


/**
* trace un pointeur avec le nom du dessinateur a la position specifiee par un click de souris.
* Permet a un utilisateur de "signer" ses dessins ou d'attirer l'attention des autres connectes sur une partie de la zone de dessin.
* indentifiant : "pointeur"
* @see Outil#nom
*/
public class OutilPointeur extends OutilDessinDirect {

	/** Definition de la forme du pointeur */
	private Polygon forme;
	/** Largeur du pointeur */
	private int w;
	/** hauteur du pointeur */
	private int h; 
	// 

   /**
   * Construit un outil pour pointer avec un bouton comportant une image de fond
   */
	OutilPointeur(Communication cdc, MenuOutils v_barre_menu, 
						String groupe, String nom_image, Dimension dimension) 
		{
		super(cdc,v_barre_menu,"pointeur",groupe,nom_image,dimension);
		}
   /**
   * lors de la selection de l'outil ce dernier est affecte au champs outil de donnees, et
	* Le nom du dessinateur est stocke dans le champs texte de donnees.
   * @see Donnees#outil
   * @see Donnees#texte
   */
	public void action_directe(Donnees donnees){
		donnees.outil=this;
		donnees.texte=central_applet.nom;
		}
	/**
	* Recherche la taille (w,h) du pointeur a partir du nom de dessinateur a inscrire a l'interieur
	* Appelle la definition de la forme du pointeur et le trace en (lx,ly)
	* @see #w
	* @see #h
	* @see #formepointeur(int w, int h, int wchar)
	* @see Donnees
	*/
	public void dessin(Graphics g,Donnees donnees)
		{
		// recherche de la taille
		FontMetrics fm = donnees.zone_dessin.getFontMetrics(donnees.zone_dessin.getFont());
		int htexte=fm.getHeight();
		int wtexte=fm.stringWidth(donnees.texte);
		int wchar=fm.charWidth('A');
		h=2*htexte; 
		w=wtexte+3*wchar;
		// definition de la croix
		forme=formepointeur(w,h,wchar);
		g.setColor(donnees.couleur);
		forme.translate(donnees.lx-2*wchar,donnees.ly-2*h/3);
		g.drawPolygon(forme);
		// texte dans la fleche
		g.drawString(donnees.texte,donnees.lx,donnees.ly);
		}

	/** 
	* definition du polygone donnant la forme graphique du pointeur 
	* @param w largeur du dessin
	* @param h hauteur du dessin
	* @param wchar largeur d'une lettre de la police de caractere selectionne
	* @return La forme a tracer.
	*/
	private Polygon formepointeur(int w, int h, int wchar)
		{
		int xpoints[]=new int[8];
		int ypoints[]=new int[8];
		xpoints[0]=0; ypoints[0]=h/2;
		xpoints[1]=3*wchar; ypoints[1]=0;
		xpoints[2]=3*wchar; ypoints[2]=h/4;
		xpoints[3]=w; ypoints[3]=h/4;
		xpoints[4]=w; ypoints[4]=3*h/4;
		xpoints[5]=3*wchar; ypoints[5]=3*h/4;
		xpoints[6]=3*wchar; ypoints[6]=h;
		xpoints[7]=0; ypoints[7]=h/2;
		return new Polygon(xpoints,ypoints,7);
		}

	XMLTree Shape(Donnees donnees) {
		FontMetrics fm = donnees.zone_dessin.getFontMetrics(donnees.zone_dessin.getFont());
		int htexte=fm.getHeight();
		int wtexte=fm.stringWidth(donnees.texte);
		int wchar=fm.charWidth('A');

		int h=2*htexte; 
		int w=wtexte+3*wchar;

		int sz = 8;
		int xpoints[]=new int[sz];
		int ypoints[]=new int[sz];

		int dx = donnees.lx-2*wchar, dy = donnees.ly-2*h/3;

		xpoints[0]=0; ypoints[0]=h/2;
		xpoints[1]=3*wchar; ypoints[1]=0;
		xpoints[2]=3*wchar; ypoints[2]=h/4;
		xpoints[3]=w; ypoints[3]=h/4;
		xpoints[4]=w; ypoints[4]=3*h/4;
		xpoints[5]=3*wchar; ypoints[5]=3*h/4;
		xpoints[6]=3*wchar; ypoints[6]=h;
		xpoints[7]=0; ypoints[7]=h/2;

		XMLTree svg = new XMLTree( "svg" );
		for( int i=0; i<sz; i++ ) {
			svg.add(
				new XMLTree( "line",
					XMLTree.Attributes(
						"x1", String.valueOf( dx + xpoints[i] ),
						"y1", String.valueOf( dy + ypoints[i] ),
						"x2", String.valueOf( dx + xpoints[(i+1)%sz] ),
						"y2", String.valueOf( dy + ypoints[(i+1)%sz] ),

						"stroke",  Donnees.colorEncode(donnees.couleur),
						"stroke-width", "1"
					),
					XMLTree.Contents()
				)
			);
		}
		return svg;
	}
}
