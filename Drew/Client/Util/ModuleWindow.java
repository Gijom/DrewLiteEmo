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
 * File: ModuleWindow.java
 * Author:
 * Description:
 *
 * $Id: ModuleWindow.java,v 1.12.2.1 2007/10/31 14:22:58 collins Exp $
 */

package Drew.Client.Util;

import java.awt.*;
import java.awt.event.*;
import Drew.Util.XMLmp.*;

/**
* Interface que doit implementer tout module tel que
* le white board, le chat ...
* 
*/

public class ModuleWindow implements CooperativeModule {

private Frame 		  frame;
private CooperativeModule module;
private Panel		  ctrl = null;
private String            name = null;

	class ModuleWindowAdapter extends WindowAdapter {
		public void windowClosing(WindowEvent e) {
                	stop();
			// we need to deselect the module in the list
			Config.selectModule( name, false);
        	}
	}

	/**
	* tout est pret, le module a ete correctement initialise, on le met
	* de cote, on va pouvoir creer sa fenetre.
	*/
	public ModuleWindow( CooperativeModule m ){
		module = m;
	}

	public ModuleWindow( CooperativeModule m, Panel ctrl, String name ){
		module = m;
		this.ctrl = ctrl;
		this.name = name;
	}

	/** 
	* Initialisation du module, pour palier a l'appel du constructeur sans
	* parametre de Class.newInstance()  
	*/
	public void constructor(Communication cdc)
	{
	}
	
	/**
	* Titre de la fenetre, on c'en sert pas, mais il le faut quand meme
	*/
        public String getTitle() 
	{
		return "Dummy";
	}


	/** 
	* Initialisation du module 
	*/
	public void init()
	{
	Panel m = (Panel)module;

	// ctrl is the remote command for the replay mode. Add one in each window
		if( m != null ) {
			frame = new Frame(module.getTitle());
			frame.setLayout( new BorderLayout() );
			module.init();

			frame.add( m, BorderLayout.CENTER );
			if( ctrl != null ) frame.add( ctrl, BorderLayout.SOUTH);

			frame.validate();
			frame.addWindowListener( new ModuleWindowAdapter() );

			if( Config.getDebug() ) {
				System.err.println("Debug module : " + module.toString() );
			}

			frame.setSize( m.getPreferredSize() );
			frame.pack();
			// frame.show();
			frame.setVisible(true);
		}
		else {
			System.err.println("Error : ModuleWindow.init null module");
		}	
	}

	/**
        * module is started
        */
        public void start()
        {
		if( module != null ) module.start();
        }

	/** 
	* arret du module 
	*/
	public void stop()
	{
		if( (frame != null) && frame.isShowing() ) frame.dispose();
		if( module != null ) module.destroy();
		module = null;
	}

	/** 
	* destruction du module 
	*/
	public void destroy()
	{
		if( module != null ) module.stop();
		stop();
	}

	/** 
	* Effacement des donn�es  du module 
	*/
	public void clear()
	{
		if( module != null ) module.clear();
	}

	/**
	* Rend la chaine de caractere identifiant le module 
	*/
	public String getCode()
	{
		if( module != null ) {
			return module.getCode(); 
		}
		else {
			return null;
		}
	}

	/**
	* le module indique s'il est destinataire ou non du message identifier par <CODE>code</CODE>
	*/
	public boolean messageFilter(XMLTree event)
	{
		if( module != null ) {
			return module.messageFilter( event ); 
		}
		else {
			return false;
		}
	}

	/**
	* traitement des messages : 2 solutions, le message est deja coupe en rondelles, et on ne
	* le delivre qu'aux modules qui ont le bon filtre , ou on le donne a tout le monde, chacun
	* en faisant ce qu'il veut. 
	*/
	public void messageDeliver(String user,XMLTree data)
	{
		if( module != null ) {
			module.messageDeliver( user,data );
		}
	}
	
	public void cacher() {
		frame.setVisible(false);
	}
	
}
