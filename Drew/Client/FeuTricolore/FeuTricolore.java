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
 * File: FeuTricolore.java
 * Author:
 * Description:
 *
 * $Id: FeuTricolore.java,v 1.13 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.FeuTricolore;

import java.util.*;
import java.awt.event.*;
import java.awt.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;


/**
* A traffic light class provided as drewlet programming exemple
* @see    Drew.Client.Util.Communication
* @see	  Drew.Client.Util.CooperativeModule
* @see    Feu
*/
public class FeuTricolore extends Panel implements MouseListener, CooperativeModule {

  	/**
 	* We use the XML tree  <code>&lt;event>&lt;trafficlight>&lt;red/>&lt;trafficlight>&lt;event></code>
	
	<code>
		<event>
			<trafficlight>
				<red/>
			</trafficlight>
		</event>
	</code>
	
	*/
	static final String CODE = "trafficlight";

	/**
	* the green and red light
	*/
	private Feu red, green;

	/**
	* red and green constants
	*/
	final static String RED    = "red";
	final static String GREEN  = "green";

	/** XML tree for red and green ligth */
	private XMLTree XMLred   = new XMLTree( CODE, new XMLTree( RED   ));
	private XMLTree XMLgreen = new XMLTree( CODE, new XMLTree( GREEN ));

	/**
	* Saving main applet for communication purpose
	* @see Communication
	*/
	private Communication central_applet;

	/**
	* Default constructor
	*/
	public FeuTricolore()
	{
		//super("Feu R/V");
	}

	/**
	* Constructor 
	* @param    cdc the Communication instance
	* @see Communication
	*/
	public FeuTricolore(Communication cdc)
	{
		this();
		constructor(cdc);
	}

	/**
	* Finishing the initialisation of the drewlet,  Class.newInstance()
	* call only the default constructor of the class.
	* @param    cdc the Communication instance
	* @see Communication
	* @see Drew.Client.Util.CooperativeModule
	* @see Drew.Client.TableauDeBord.Module
	*/
	public void constructor(Communication cdc) 
	{
		central_applet=cdc;

		red = new Feu( Color.red );
		green  = new Feu( Color.green );
	}

	/**
	* MouseListener interface implementation
	*/
	public void mouseClicked(MouseEvent e) 
	{
		if( e.getComponent() == red ) {
			// changeColor( RED );
			central_applet.envoiserveur( XMLred );
		}
		else {
			// changeColor( GREEN );
			central_applet.envoiserveur( XMLgreen );
		}
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}

	/**
	* trafficlight get a color as parameter and switch it. 
	* @param   color the new lighted color RED, ou GREEN 
	*/
	private void changeColor( String color ) 
	{
		if( color.equals( RED ) ) {
			red.lightOn();
			green.lightOff();
		}
		else {
			green.lightOn();
			red.lightOff();
		}
		red.repaint();
		green.repaint();
	}

	/** overload Component methods */
	public Dimension getPreferredSize()
	{
		return new Dimension(120,240);
	}

	/** implement CooperativeModule interface */
	public String getTitle() {
		return "Traffic light";
	}

	public void init() {
		// GUI settings
		//----------------------------------------
		// background color
		setBackground(Color.darkGray);
		//Add Components to the Frame.
		GridLayout grid = new GridLayout(2,1);
		setLayout(grid);
		add(red);
		add(green);
		// events management
		red.addMouseListener(this);
		green.addMouseListener(this);
	}
  
	/**
	* module is started
	*/
	public void start()
	{
	}

	/**
	* stop drewlet
	*/
	public void stop()
	{
		/*
		if(isShowing()) {
        		dispose();
		}
		*/
	}

	/**
	* destroy drewlet
	*/
	public void destroy() 
	{
		/*
		stop();
		*/
	}
  
	/**
	* clear data content
	*/
	public void clear() {
	}

	/**
	* Rend la chaine de caractere identifiant le module
	*/
	public String getCode() {
		return CODE;
	}

	/**
	* Accepte les messages qui sont uniquement pour lui code == CODE
	*/
	public boolean messageFilter(XMLTree m) {
		return true;
	}

	/**
	* get messages with tag equal to <var>CODE</var>
	*/
	public void messageDeliver(String user, XMLTree data) {
		if( data.tag().equals( CODE ) ) {
			Object m;
			for (Enumeration e = data.elements() ; e.hasMoreElements() ;) {
				m = e.nextElement();
				if(!( m instanceof XMLTree )) continue;
					changeColor(((XMLTree)m).tag());
				}
		}
	}
}
