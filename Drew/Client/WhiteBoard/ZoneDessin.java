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
 * File: ZoneDessin.java
 * Author:
 * Description:
 *
 * $Id: ZoneDessin.java,v 1.7 2006/10/11 10:57:23 jaillon Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;

/**
* La classe ZoneDessin est un Canvas ameliore permettant de dessiner sur un fond blanc.
* Deux images virtuelles servent de buffer d'ecriture et de sauvegarde de la zone de dessin.
* Aucune ecriture ne doit etre realisee directement dans l'environnement graphique de ZoneDessin sous peine de perte d'information.
* Il est imperatif de passer par l'intermediaire du buffer d'ecriture. Le dessin peut etre sauvegarde puis rappele a tout moment par des fonctions publiques.
*/
public class ZoneDessin extends Canvas {

	/** largeur de la zone de dessin */
	private int w;
	/** hauteur de la zone de dessin */
	private int h;
	/** Dimensions de la zone de dessin */
	private Dimension size;

	// on dessine dans un buffer d'ecriture permettant de gerer les evenements sur la fenetre : la fenetre ne s'efface pas toute seule
	/** Buffer d'ecriture */
	public Image ecriture = null;
	/** Buffer de sauvegarde */
	public Image sauvegarde = null;
	/** Contexte graphique permettant de dessiner dans le buffer d'ecriture */
	public Graphics g_ecriture = null;
	/** Contexte graphique permettant de dessiner dans le buffer de sauvegarde */
	public Graphics g_sauvegarde = null;

	/** Sauvegarde de la taille des buffers utilisés */
	private Dimension buff_sz = new Dimension(0,0);

	/**
	* construit une zone de dessin de hauteur et de largeur determinee
	* @param initialWidth largeur initiale
	* @param initialHeight hauteur initiale
	*/
	ZoneDessin(int initialWidth, int initialHeight) 
	{
		w = initialWidth;
		h = initialHeight;
		size=new Dimension(w,h);
		setSize(size);
		// couleur de fond
		//setBackground(Color.white);
		setBackground(new Color(240,240,240));
	}

	// surcharge des methodes d'affichage/rafraichissement
	//----------------------------------------------------
	/**
	* initilisation, redimentionnement des buffers images.
	* recopie du buffer d'ecriture sur l'affichage ecran
	*/
	public void paint (Graphics g)
	{
		if( !buff_sz.equals(getSize()) ) {
			Image temp;
			Graphics g_temp;
			Dimension sz = getSize();
			
			buff_sz = new Dimension(Math.max(sz.width, buff_sz.width),Math.max(sz.height,buff_sz.height) );

			temp = createImage( buff_sz.width, buff_sz.height);
			g_temp = temp.getGraphics();

			// clear the image
			g_temp.setColor( getBackground() );
                        g_temp.fillRect(0,0,buff_sz.width,buff_sz.height);
			if( ecriture != null ) {
				g_temp.drawImage(ecriture,0,0,getBackground(),null);	
				g_ecriture.dispose();
			}
			ecriture = temp; g_ecriture = g_temp;
        
			temp = createImage ( buff_sz.width, buff_sz.height);
			g_temp = temp.getGraphics();

			// clear the image
			g_temp.setColor( getBackground() );
                        g_temp.fillRect(0,0,buff_sz.width,buff_sz.height);
			if( sauvegarde != null ) {
				g_temp.drawImage(sauvegarde,0,0,getBackground(),null); 
				g_sauvegarde.dispose();
			}
			sauvegarde = temp;  g_sauvegarde = g_temp;
		}
		g.drawImage(ecriture,0,0,null);
	}

	/**
	* appelle paint seulement (plus rapide que la fonction par defaut)
	*/
	public void update(Graphics g) 
	{
		 paint(g); 
	}

	/**
	* copie le buffer d'ecriture dans la sauvegarde
	*/
	public void sauvegarde_image()
	{
		 g_sauvegarde.drawImage(ecriture,0,0,null); 
	}
	
	/**
	* efface les buffers d'ecriture et de sauvegarde et appelle repaint().
	*/
	public void efface() 
	{
		if( g_sauvegarde != null && g_ecriture != null ) {
			//PJ 20021203 on sun, the screen is black ????
			//g_sauvegarde.clearRect(0,0,getSize().width,getSize().height);
			//g_ecriture.clearRect(0,0,getSize().width,getSize().height);

			g_sauvegarde.setColor( getBackground() );
			g_sauvegarde.fillRect(0,0,getSize().width,getSize().height);
			g_ecriture.setColor( getBackground() );
			g_ecriture.fillRect(0,0,getSize().width,getSize().height);
			repaint();
		}
	}

	// problemes de dimensionnement
	//-----------------------------
	/**
	* rend la taille optimale d'affichage
	*/
	public Dimension getPreferredSize() 
	{
		return getMinimumSize(); 
	}
	/**
	* rend la taille maximale d'affichage
	*/
	public Dimension getMaximumSize() 
	{
		return getMinimumSize();
	}
	/**
	* rend la taille minimale d'affichage
	*/
	public Dimension getMinimumSize() 
	{
		return size;
	}

        protected void finalize() throws Throwable {
		ecriture = null;
		sauvegarde = null;
		g_ecriture = null;
		g_sauvegarde = null;
                //System.err.println( "Zone_dessin finalised (" + this.hashCode() + ")" );
        }
}
