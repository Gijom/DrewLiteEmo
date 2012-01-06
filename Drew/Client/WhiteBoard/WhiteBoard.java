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
 * File: WhiteBoard.java
 * Author:
 * Description:
 *
 * $Id: WhiteBoard.java,v 1.13 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.WhiteBoard;

import java.lang.Character;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

/**
* La classe WhiteBoard est un module permettant de dessiner. Il est 
* imperativement rattache a une applet de type Communication.
* Cet utilitaire est compose d'une zone de dessin et d'une boite a outils. 
* La classe WhiteBoard sert d'interface entre ces deux entitees:
* elle gere les evenements souris et clavier ayant lieu sur la zone de dessin. 
* Pour chaque type d'evenement elle appelle la methode d'action correspondante 
* de l'outil selectionne dans la barre d'outil.
* Les donnees (coordonnees, outil utilise, couleur ... ) sont transmises de la 
* zone de dessin et de la barre d'outil vers les methodes de l'outil selectionne 
* par un champs de type Donnees.
* Le traitement des messages provenant de WhiteBoard distants est aussi traite 
* par la methode mis_a_jour de cette classe.
* @see Outil
* @see Donnees
* @see #mis_a_jour
*/
public class WhiteBoard extends Panel
			implements CooperativeModule, MouseListener, MouseMotionListener, KeyListener {

/** Applet "mere" */
private Communication central_applet;

/** Zone de dessin */
private ZoneDessin zone_dessin;

/** fenetre dependente contenant les outils/parametres de dessin */
public MenuOutils outils;

/** 
* position de la souris dans la zone de dessin, couleur activees 
* et autres donnees necessaires aux outils
*/
public Donnees donnees;

private Drew.Util.Locale comment;

	public WhiteBoard() 
	{
	}

	/**
	* Constructeur du module "Tableau blanc"
	* @param cdc Applet "mere" 
	*/
	public WhiteBoard(Communication cdc) 
	{
		this();
		constructor(cdc);
        }

        /**
        * pour terminer la construction de la fenetre du wb,  Class.newInstance()
        * n'appelle que le contructeur par defaut de la classe.
        * @param    cdc instance de l'applet de communication
        * @see Communication
        * @see Drew.Client.Util.CooperativeModule
        * @see Drew.Client.TableauDeBord.Module
        */
        public void constructor(Communication cdc) 
	{
                central_applet=cdc;
		comment = new Drew.Util.Locale("Drew.Locale.Client.WhiteBoard",Config.getLocale() );
                System.err.println( "Whiteboard created (" + this.hashCode() + ")" );
        }


// methodes de l'interface MouseListener: gestion des clics de souris et deplacements
//---------------------------------------
	/**
	* Gere l'evenement de pression d'un bouton souris dans la zone de dessin.
	* les coordonnees x,y du pointeur de souris sont transmises aux champs nx et ny de donnees.
	* la methode action_mousePressed de l'outil actif est appelee.
	* @see Outil#action_mousePressed(Donnees donnees)
	* @see #donnees
	*/
	public void mousePressed(MouseEvent e){
		donnees.nx=e.getX();
		donnees.ny=e.getY();
		outils.get_bouton_active(outils.groupe_dessin).action_mousePressed(donnees);
	}

	/**
	* Gere l'evenement de click de souris dans la zone de dessin.
	* les coordonnees x,y du click sont transmises aux champs nx et ny de donnees.
	* la methode action_mouseClicked de l'outil actif est appelee.
	* @see Outil#action_mouseClicked(Donnees donnees)
	* @see #donnees
	*/
	public void mouseClicked(MouseEvent e)
	{
		donnees.nx=e.getX();
		donnees.ny=e.getY();
		outils.get_bouton_active(outils.groupe_dessin).action_mouseClicked(donnees);
	}

	/**
	* Gere l'evenement de relachement d'un bouton de souris dans la zone de dessin.
	* les coordonnees x,y du pointeur de souris sont transmises aux champs lx et ly de donnees.
	* la methode action_mouseReleased de l'outil actif est appelee.
	* @see Outil#action_mouseReleased(Donnees donnees)
	* @see #donnees
	*/
	public void mouseReleased(MouseEvent e) 
	{
		donnees.lx=e.getX();
		donnees.ly=e.getY();
		outils.get_bouton_active(outils.groupe_dessin).action_mouseReleased(donnees);
	}

	/**
	* Gere l'evenement d'entree du curseur souris dans la zone de dessin.
	* la methode action_mouseEntered de l'outil actif est appelee.
	* @see Outil#action_mouseEntered(Donnees donnees)
	*/
	public void mouseEntered(MouseEvent e)
	{
		outils.get_bouton_active(outils.groupe_dessin).action_mouseEntered(donnees);
	}

	/**
	* Gere l'evenement de sortie du curseur souris de la zone de dessin.
	* la methode action_mouseExited de l'outil actif est appelee.
	* @see Outil#action_mouseExited(Donnees donnees)
	*/
	public void mouseExited(MouseEvent e)
	{
		outils.get_bouton_active(outils.groupe_dessin).action_mouseExited(donnees);
	}


// methodes de l'interface MouseMotionListener: gestion des deplacements bouton enfonce/releve de la souris
	
	/**
	* Gere les mouvements de la souris bouton enfonce dans la zone de dessin.
	* les coordonnees x,y du pointeur de souris sont transmises aux champs lx et ly de donnees.
	* la methode action_mouseDragged de l'outil actif est appelee.
	* @see Outil#action_mouseDragged(Donnees donnees)
	* @see #donnees
	*/
	public void mouseDragged (MouseEvent e) 
	{
		donnees.lx=e.getX();
		donnees.ly=e.getY();
		outils.get_bouton_active(outils.groupe_dessin).action_mouseDragged(donnees);
	}

	/**
	* Gere les mouvements de la souris bouton releve dans la zone de dessin.
	* les coordonnees x,y du pointeur de souris sont transmises aux champs lx et ly de donnees.
	* la methode action_mouseMoved de l'outil actif est appelee.
	* @see Outil#action_mouseMoved(Donnees donnees)
	* @see #donnees
	*/
	public void mouseMoved(MouseEvent e)
	{
		donnees.lx=e.getX();
		donnees.ly=e.getY();
		outils.get_bouton_active(outils.groupe_dessin).action_mouseMoved(donnees);
	}

// methodes de l'interface KeyListener: gestion des frappes claviers pour l'entree de texte
//--------------------------------------------
	/**
	* Gere la pression d'une touche clavier dans la zone de dessin.
	* La lettre tapee est transmise au champs caractere de donnee.
	* la methode action_keyPressed de l'outil actif est appelee.
	* @see Outil#action_keyPressed(Donnees donnees)
	* @see #donnees
	*/
	public void keyPressed(KeyEvent e) 
	{
	 
		char touche = e.getKeyChar();

		if(Character.isWhitespace(touche)) touche = ' ';
		//PJ 20021203
		//donnees.caractere = touche;
		donnees.texte = String.valueOf( touche );
		outils.get_bouton_active(outils.groupe_dessin).action_keyTyped(donnees);
	}

	/**
	* Gere le relachement d'une touche clavier dans la zone de dessin.
	* La lettre tapee est transmise au champs caractere de donnee.
	* la methode action_keyReleased de l'outil actif est appelee.
	* @see Outil#action_keyReleased(Donnees donnees)
	* @see #donnees
	*/
	public void keyReleased(KeyEvent e) 
	{
		outils.get_bouton_active(outils.groupe_dessin).action_keyReleased(donnees);
	}

	/**
	* Gere la frappe d'une touche clavier dans la zone de dessin.
	* La lettre tapee est transmise au champs caractere de donnee.
	* la methode action_keyTyped de l'outil actif est appelee.
	* @see Outil#action_keyTyped(Donnees donnees)
	* @see #donnees
	*/
	public void keyTyped(KeyEvent e) 
	{
            	char touche = e.getKeyChar();

            	if(Character.isWhitespace(touche)) touche = ' ';
		//PJ 20021203
            	//donnees.caractere = touche;
		donnees.texte = String.valueOf( touche );
            	outils.get_bouton_active(outils.groupe_dessin).action_keyPressed(donnees);
	}

// Affichage des dessins des autres clients connectes
//----------------------------------------------------
	/**
	* Affichage des dessins des autres clients connectes.
	* Separe l'entete du message transmis via le Communication des instructions 
	* de dessin du module WhiteBoard distant.
	* La fonction dechiffre les donnees du WhiteBoard distant et effectue la mise 
	* a jour correspondante de sa propre zone de dessin.
	* @param texte message envoye par le serveur et transmis au WhiteBoard par le Communication.
	* @see Donnees#Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin,String chaine)
	* @see #traiteDonnees(Donnees donnees)
	*/
	//public void mis_a_jour(String origine, String texte)
	//XML pj 2002
	public void mis_a_jour(String origine, XMLTree msg) {
		//PJ 20011026
		//if(!central_applet.nom.equals(origine)) {

		//we are dealing with a svg content, loop for the content
		Object o;
		for (Enumeration e = msg.elements() ; e.hasMoreElements() ;) {
			o = e.nextElement();   
			if( !( o instanceof XMLTree ) ) continue;

			// o is a line, an ellipse, a rect or a text
//System.err.println( "mis_a_jour " + o.toString() );
			Donnees donnees_recues = new Donnees(outils.outils_dessin,zone_dessin,(XMLTree)o);
			traiteDonnees(donnees_recues);
		}

		//}
/* **********************************
		StringTokenizer tok;
		String origine;
		try {
			tok = new StringTokenizer(texte,"<>");
			origine = tok.nextToken();
			if(!central_applet.nom.equals(origine)) {
				Donnees donnees_recues = new Donnees(outils.outils_dessin,zone_dessin,tok.nextToken());
				traiteDonnees(donnees_recues);
			}
		}
		catch(NumberFormatException e) {	// Message invalide
			System.err.println("maj erreur : "+e);
		}
		catch(Exception Erreur) { 
			System.err.println("maj erreur : "+Erreur); 
		}
**************************************/
	}

	/**
	* Fonction utilisee pour ecrire sur la zone de dessin. Elle est 
	* synchronisee pour permettre le partage entre les acces suite a des
	* ordres locaux et les acces de mise a jour des ordres provenant de whiteboard distants.
	*/
	public synchronized void traiteDonnees(Donnees data)
	{
		if ( (data.zone_dessin.g_ecriture!=null) && (data.outil!=null)) {
			data.outil.dessin(data.zone_dessin.g_ecriture,data);
			data.zone_dessin.repaint();
		}
	}


// surcharge des methodes de Component 

        public Dimension getPreferredSize()
        {
                return new Dimension(500,500);
        }

// implantation de l'interface CooperativeModule 

        public String getTitle() {
                return "Whiteboard";
        }

        public void init() 
	{
		setBackground(Color.lightGray);

                GridBagLayout gridBag = new GridBagLayout();
                setLayout(gridBag);
                GridBagConstraints c = new GridBagConstraints();

                // zone  pour les outils
		outils= new MenuOutils(this,central_applet,"Outils");

		c.anchor = GridBagConstraints.NORTH;
                c.fill = GridBagConstraints.NONE;
                gridBag.setConstraints(outils, c);
		add(outils);

                // zone d'affichage du Dessin
		zone_dessin=new ZoneDessin(900,900);
		
		//mp 29/01/2002 ajout d'un ScrollPane
		ScrollPane pane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		//pane.setSize(new Dimension(350, 350));
		pane.add(zone_dessin);
		c.gridwidth = GridBagConstraints.REMAINDER;
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 1.0;
                c.weighty = 1.0;
                gridBag.setConstraints(pane, c);
		gridBag.setConstraints(zone_dessin, c);
		add(pane);

		// toutes les actions sur la zone de dessin sont gerees ici
		zone_dessin.addMouseListener(this);
		zone_dessin.addMouseMotionListener(this);
		zone_dessin.addKeyListener(this);

		// donnees necessaires pour les outils
		donnees= new Donnees(outils.outils_dessin,zone_dessin);
		donnees.zone_dessin.repaint();
		// initialisation des outils
		outils.get_bouton_active(outils.groupe_dessin).action_directe(donnees);
		outils.get_bouton_active(outils.groupe_couleur).action_directe(donnees);
	}

	/**
        * module is started
        */
        public void start()
        {
        }

        /**
        * arret du module
        */
        public void stop()
        {
        }

        /**
        * destruction du module
        */
        public void destroy()
        {
		stop();
        }

        /**
        * effacer les donn�es du module
        */
        public void clear()
        {
		donnees.zone_dessin.efface();
        }

        /**
        * Rend la chaine de caractere identifiant le module
        */
        public String getCode()
        {
                return Donnees.CODE;
        }

        /**
        * Accepte les messages qui sont uniquement pour lui code == CODE
        */
        public boolean messageFilter(String code, String origine)
        {
                return code.equals( getCode() );
        }

        public void messageDeliver(String code, String origine, String msg)
        {
		//XML
                //mis_a_jour( origine, msg );
        }

        /**
        * Accepte les messages qui sont uniquement pour lui code == CODE
        */
        public boolean messageFilter(XMLTree data)
        {
                return true;
        }

        public void messageDeliver(String user, XMLTree data) {
		//it's an wb event, loop for svg data
		if( getCode().equals( data.tag() ) ) {
                        Object o;
                        for (Enumeration e = data.elements() ; e.hasMoreElements() ;) {
                                o = e.nextElement();
                                if( !( o instanceof XMLTree ) ) continue;
				mis_a_jour( user, (XMLTree)o ); // o is a svg event 
                        }
		}
        }

        protected void finalize() throws Throwable {
		central_applet = null;
		zone_dessin = null;
		outils = null;
		donnees = null;
                //System.err.println( "Whiteboard finalised (" + this.hashCode() + ")" );
        }
}
