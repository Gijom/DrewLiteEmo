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
 * File: MultiModules.java
 * Author:
 * Description:
 *
 * $Id: MultiModules.java,v 1.9 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.MultiModules;

import java.awt.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;
import Drew.Client.FeuTricolore.*;
import Drew.Client.WhiteBoard.*;
import Drew.Client.Grapheur.*;
import Drew.Client.TextBoard.*;
import Drew.Client.Chat.*;


/**
* Classe permettant d'avoir pleins de modules dans une fenetre
* Exemple d'utilisation:
* @see    Drew.Client.Util.Communication
* @see    Feu
*/
public class MultiModules extends Panel implements CooperativeModule {

  /**
  * code <EM>unique</EM> represntatnt les messages en provenance ou a destiniton de ce module
  * en l'occurence <CODE>yenapas</CODE>
  */
  static final String CODE = "notspecified";

  /**
  * Recuper l'applet-mere
  * @see Communication
  */
  public Communication central_applet;

  /**
  * les drewlets que l'on veut embarquer
  */
  private FeuTricolore feu;
  private Chat_fenetre chat;
  private WhiteBoard   wb;
  private TextBoard    tb;
  private Grapheur     graph;

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public MultiModules()
  	{
  	}

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public MultiModules(Communication cdc)
  	{
        	this();
		constructor(cdc);
  	}

  	/**
  	* pour terminer la construction du module  ,  Class.newInstance()
	* n'appelle que le contructeur par defaut de la classe.
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
	* @see Drew.Client.Util.CooperativeModule
	* @see Drew.Client.TableauDeBord.Module
  	*/
	public void constructor(Communication cdc) 
	{
        	central_applet=cdc;

		feu  = new FeuTricolore(cdc);
		chat = new Chat_fenetre(cdc);
		wb   = new WhiteBoard(cdc);
		tb   = new TextBoard(cdc);
		graph= new Grapheur(cdc);
	}

  	/* surcharge des methodes de Component */

	public Dimension getPreferredSize() 
	{
		return new Dimension(800,600);
	}

  	/* implantation de l'interface CooperativeModule */

        public String getTitle() {
                return "Drew modules";
        }

  	public void init()
	{
        	// Mise en place des elements graphiques
        	//----------------------------------------

                // couleur de fond
                //setBackground(Color.darkGray );

                //Add Components to the Component.
         	GridBagLayout gridbag = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();

                setLayout(gridbag);

		c.insets = new Insets( 3, 3, 3, 3);
                // les modules
                c.weightx = 0.5;
		c.weighty = 1.0;
		c.fill = GridBagConstraints.BOTH;
		gridbag.setConstraints(feu, c);
	 	add(feu); feu.init();

                c.weightx = 1.0;
		gridbag.setConstraints(chat, c);
	 	add(chat); chat.init();

		c.gridwidth = GridBagConstraints.REMAINDER;
                gridbag.setConstraints(tb, c);
		add(tb); tb.init();

		c.gridwidth = GridBagConstraints.RELATIVE;
		c.gridheight = 2;
                gridbag.setConstraints(wb, c);
		add(wb); wb.init();

		c.gridwidth = GridBagConstraints.REMAINDER;
                gridbag.setConstraints(graph, c);
		add(graph); graph.init();
	}
  
	/**
        * module is started
        */
        public void start()
        {
		feu.start();
		chat.start();
		wb.start();
		tb.start();
		graph.start();
        }

        /**
        * arret du module
        */
        public void stop()
	{
		feu.stop();
		chat.stop();
		wb.stop();
		tb.stop();
		graph.stop();
	}

        /**
        * effacer les donn�es du module
        */
        public void clear() 
        {
		feu.clear();
		chat.clear();
		wb.clear();
		tb.clear();
		graph.clear();
        }

        /**
        * destruction du module
        */
        public void destroy() 
	{
		stop();
	}
  
        /**
        * Rend la chaine de caractere identifiant le module
        */
        public String getCode() 
	{
		return CODE;
	}

        /**
        * Accepter les messages qui sont a destination de chacun des modules embarques
        */
        public boolean messageFilter(XMLTree data)
        {
                return 
			chat.messageFilter(data) || wb.messageFilter(data) || 
			tb.messageFilter(data) || graph.messageFilter(data) ||
			feu.messageFilter(data);
        }

        /**
        * Delivrer les messages qui sont a destination de chacun des modules embarques
        */
        public void messageDeliver(String user, XMLTree data) 
	{
		if(  feu.messageFilter(data) )   feu.messageDeliver(user, data);
		if( chat.messageFilter(data) )  chat.messageDeliver(user, data);
		if(   wb.messageFilter(data) )    wb.messageDeliver(user, data);
		if(   tb.messageFilter(data) )    tb.messageDeliver(user, data);
		if(graph.messageFilter(data) ) graph.messageDeliver(user, data);
	}
}
