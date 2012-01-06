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
 * File: MyAppletContext.java
 * Author:
 * Description:
 *
 * $Id: MyAppletContext.java,v 1.7 2007/02/20 16:03:41 collins Exp $
 */


package Drew.Client.App;

import java.applet.*;
import java.util.*;
import java.net.*;
import java.awt.*;
import java.io.* ;

class MyAppletContext implements AppletContext {

private Toolkit toolkit = null;

	MyAppletContext( Toolkit tk ) {
		toolkit = tk;
	}

	public AudioClip getAudioClip(URL url) {
		return null;
	}

	public Image getImage(URL url) {

		try {
			java.awt.image.ImageProducer producer =(java.awt.image.ImageProducer)url.getContent();
			return toolkit.createImage( producer );
		}
		catch(Exception e) {
			try {
				// try looking at resources in jar file if possible
				url = getClass().getClassLoader().getResource( url.getFile() );
				java.awt.image.ImageProducer producer =(java.awt.image.ImageProducer)url.getContent();
				return toolkit.createImage( producer );
			}
			catch(Exception ee) {
				System.err.println( "getImage " + ee );
				return null;
			}
		}
	}

	public Applet getApplet(String name) {
		return null;
	}

	public Enumeration getApplets() {
		return null;
	}

	public void showDocument(URL url) {
		showDocument( url, "_self" );
	}

	public void showDocument(URL url, String target) {
		System.out.println( "showDocument \"" + url.toString() + "\" in window " + target );
	}

	public void showStatus(String status) {
		System.out.println( status );
	}

	public void setStream(String key, InputStream stream) {
	}

	public InputStream getStream(String key) {
		return null ;
	}

	
	public Iterator getStreamKeys() {
		return null ;
	}
	
		
			
};
