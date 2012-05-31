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
 * File: player.java
 * Author:
 * Description:
 *
 * $Id: player.java,v 1.7.2.1 2007/10/31 14:22:59 collins Exp $
 */


package Drew.Client.App;

import java.applet.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

public class player implements WindowListener {

Applet app;
public Frame frame;
public boolean issubprogram = false;

	public void windowActivated(WindowEvent e)  {}
	public void windowClosed(WindowEvent e) {} 
	public void windowClosing(WindowEvent e) {
		app.stop(); 
		frame.dispose(); 
		app.destroy();
        	System.exit(0);
	} 
	public void windowDeactivated(WindowEvent e) {} 
	public void windowDeiconified(WindowEvent e) {} 
	public void windowIconified(WindowEvent e) {} 
	public void windowOpened(WindowEvent e) {} 

	public player () {
	}

	public void set( Applet a) {
		app = a;
                frame = new Frame(app.getClass().getName() + " window");
                app.setStub( new MyAppletStub( frame.getToolkit()) );
		frame.addWindowListener(this);
	}
	
	public void start() {
                app.init();	
		frame.add( app );
		frame.pack();
		// frame.show(); // Deprecated in 1.5 => setVisible
                frame.setVisible(true);
                app.start();	
	}

	public static void main(String args[]) {
	player app = new player();

		try {
			//System.getProperties().load( new FileInputStream("play.properties"));
			// System.getProperties().load( app.getClass().getResourceAsStream("play.properties"));

			// Using -D swich in java command add the specified property to the System properties
			// we want overide properties defined in properties file in such way
			Properties defaultProperties = new Properties();
			// in this case, java look at the app class package (the directory where is Application.class)
			//defaultProperties.load( app.getClass().getResourceAsStream("play.properties"));

			// When using the class loader, it looks at the root of the jar archive
			try {
				defaultProperties.load( new FileInputStream("client.properties"));
			} catch (RuntimeException e) {
				defaultProperties.load( app.getClass().getClassLoader().getResourceAsStream("client.properties"));
			}

			for( Enumeration e = defaultProperties.propertyNames(); e.hasMoreElements() ;) {
				// Add the property only if not defined on command line
				String key = (String) e.nextElement();
				if(System.getProperties().getProperty(key) == null) {
					System.getProperties().setProperty( key,defaultProperties.getProperty(key) );
				}
			}
		}
		catch( IOException e ){
			System.err.println( "Can'read play.properties" );
		}

		System.getProperties().list( System.err );

		String mode = System.getProperty( "player.mode", "play" ); // 2 modes : play and replay
		if( mode.equals( "play" ) ) {
			app.set( new Drew.Client.TableauDeBord.CentreDeConnection() );
		}
		else { // assume we are in replay mode
			app.set( new Drew.Client.Rejoueur.CentreDeConnection() );
		}
		app.start();
	}
};
