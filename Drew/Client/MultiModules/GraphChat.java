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

package Drew.Client.MultiModules;

import java.awt.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;
import Drew.Client.Grapheur.*;
import Drew.Client.Chat.*;


/**
* Classe permettant d'avoir grapheur et chat  dans une fenetre
* Exemple d'utilisation:
* @see    Drew.Client.Util.Communication
* @see    Feu
*/
public class GraphChat extends Panel implements CooperativeModule {

  /**
  * code <EM>unique</EM> represntatnt les messages en provenance ou a destiniton de ce module
  * en l'occurence <CODE>yenapas</CODE>
  */
  static final String CODE = "yenapas";

  /**
  * Recuper l'applet-mere
  * @see Communication
  */
  public Communication central_applet;

  /**
  * les drewlets que l'on veut embarquer
  */
  private Chat_fenetre chat;
  private Grapheur     graph;

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public GraphChat()
  	{
  	}

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public GraphChat(Communication cdc)
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

		chat = new Chat_fenetre(cdc);
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

  	public void Xinit()
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
                c.weightx = 1.0;
		c.weighty = 1.0;

                // les modules
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1;
    		c.gridheight = 1;

		c.fill = GridBagConstraints.BOTH;

                gridbag.setConstraints(graph, c);
		add(graph); graph.init();

		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
    		c.gridheight = 1;

		gridbag.setConstraints(chat, c);
	 	add(chat); chat.init();
	}

	private void setGridBagConstraints( GridBagConstraints c, String where ) {
		if( where.equalsIgnoreCase("left") ) {
			c.gridx = 0; c.gridy = 0;
		}
		else if( where.equalsIgnoreCase("right") ) {
			c.gridx = 1; c.gridy = 0;
		}
		else if( where.equalsIgnoreCase("up") ) {
			c.gridx = 0; c.gridy = 0;
		}
		else if( where.equalsIgnoreCase("down") ) {
			c.gridx = 0; c.gridy = 1;
		}
		else {
			c.gridx = 0; c.gridy = 0;
		}
	}

  	public void init()
	{
        	// Mise en place des elements graphiques
        	//----------------------------------------

                // couleur de fond
                //setBackground(Color.darkGray );

                //Add Components to the Component.

		String dummy = null;
         	GridBagLayout gridbag = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();

                setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2);
                c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridwidth = 1;
    		c.gridheight = 1;

		c.gridx = 0;
		c.gridy = 0;
		dummy = central_applet.getParameter("multi.chat");
		if( dummy != null ) setGridBagConstraints(c,dummy);
		gridbag.setConstraints(chat, c);
		add(chat); chat.init();

		c.gridx = 1;
		c.gridy = 0;
		dummy = central_applet.getParameter("multi.graph");
		if( dummy != null ) setGridBagConstraints(c,dummy);
                gridbag.setConstraints(graph, c);
		add(graph); graph.init();
	}
  
	/**
        * module is started
        */
        public void start()
        {
		chat.start();
		graph.start();
        }

        /**
        * arret du module
        */
        public void stop()
	{
		chat.stop();
		graph.stop();
	}

        /**
        * effacer les donn�es du module
        */
        public void clear() 
        {
		chat.clear();
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
			chat.messageFilter(data) || graph.messageFilter(data) ;
        }

        /**
        * Delivrer les messages qui sont a destination de chacun des modules embarques
        */
        public void messageDeliver(String user, XMLTree data) 
	{
		if( chat.messageFilter(data) )  chat.messageDeliver(user, data);
		if(graph.messageFilter(data) ) graph.messageDeliver(user, data);
	}
}
