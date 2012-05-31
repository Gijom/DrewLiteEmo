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

//import java.net.*;
//import java.io.*;
import java.util.*;
//import java.awt.event.*;
import java.awt.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;


/**
* Classe permettant d'avoir grapheur et chat  dans une fenetre
* Exemple d'utilisation:
* it use the following parameters
*	multi.modules with a comma separated list of volume name (as defined in Drew.Client.Util.Config)
* 	multi.orientation with horizontal and verical as value
* @see    Drew.Client.Util.Communication
* @see    Feu
*/
public class TwoModules extends Panel implements CooperativeModule {

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
  private CooperativeModule modA;
  private CooperativeModule modB;

  private boolean horizontal;
  private double ratioModA;
  
  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public TwoModules() {
  	}

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public TwoModules(Communication cdc)
  	{
        	this();
		constructor(cdc);
  	}

  	/**
	 *
  	 */
	public void constructor(Communication cdc) {
        	central_applet=cdc;

		String modules = central_applet.getParameter("multi.modules");
		if( modules == null ) modules = "grapheur,alex";

		try {
			// we use just the two first elements of the list
			StringTokenizer st = new StringTokenizer(modules,",;:");

			modA = Config.newInstance(st.nextToken() , cdc, false );
			modB = Config.newInstance( st.nextToken(), cdc, false );
		}
		catch( NoSuchElementException e ) {
			System.err.println("TwoModules.init : bad modules list " + modules );
			modA = Config.newInstance( "grapheur",cdc, false );
			modB = Config.newInstance( "alex",cdc, false );
		}
               
                // chose modules orientation (vertical or horizontal)
                horizontal = true;
		String dummy = central_applet.getParameter("multi.orientation");		
		if( dummy != null) {
                    if( dummy.equalsIgnoreCase("vertical") )
                        horizontal = false;
                }
                
                // Ratio of the first module
		dummy = central_applet.getParameter("multi.ratioFirstModule");		
                try
                {
                    ratioModA = Float.parseFloat(dummy);
                    if (ratioModA >= 1)
                        ratioModA = 0.5;
                }
                catch(Exception e) //null string, or not float format
                {
                    ratioModA = 0.5;
                }
	}

  	/** overload the Component method */
	public Dimension getPreferredSize() {
		return new Dimension(800,600);
	}

  	/** CooperativeModule interface */
        public String getTitle() {
                return "Drew modules";
        }

  	public void init()
	{
        	// set up graphical interface
                // and add modules to the Component.
         	GridBagLayout gridbag = new GridBagLayout();
                GridBagConstraints c = new GridBagConstraints();

                setLayout(gridbag);
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets( 2, 2, 2, 2);
                c.weightx = ratioModA;
                c.weighty = ratioModA;
                c.gridx = 0;
                c.gridy = 0;
                gridbag.setConstraints((Panel)modA, c);
                
                c.weightx = 1-ratioModA;
                c.weighty = 1-ratioModA;
		if( horizontal ) {
                        c.gridx = 1; //GridBagConstraints.REMAINDER;
			gridbag.setConstraints((Panel)modB, c);
		}
		else {
                        c.gridy = 1; //GridBagConstraints.REMAINDER;
			gridbag.setConstraints((Panel)modB, c);
		}
		add((Panel)modA); modA.init();
		add((Panel)modB); modB.init();
        }

	/**
        * modules are started
        */
        public void start() {
		modA.start();
		modB.start();
        }

        /**
        * stop modules
        */
        public void stop() {
		modA.stop();
		modB.stop();
	}

        /**
        * clear data from modules
        */
        public void clear() {
		modA.clear();
		modB.clear();
        }

        /**
        * destroy modules
        */
        public void destroy() {
		stop();
	}
  
        /**
        * Return string id fo this module
        */
        public String getCode() {
		return CODE;
	}

        /**
        * Accept messages for each embeded modules
        */
        public boolean messageFilter(XMLTree data) {
                return 
			modA.messageFilter(data) || modB.messageFilter(data) ;
        }

        /**
        * Deliver messages for each embeded modules
        */
        public void messageDeliver(String user, XMLTree data) {
		if( modA.messageFilter(data) ) modA.messageDeliver(user, data);
		if( modB.messageFilter(data) ) modB.messageDeliver(user, data);
	}
}
