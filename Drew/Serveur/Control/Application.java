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
 * File: Application.java
 * Author:
 * Description:
 *
 * $Id: Application.java,v 1.7 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Serveur.Control;

import java.util.*;
import Drew.Serveur.*;

/**
 * class for manage a control connection to the GrandSioux server
 */
public class Application extends Thread {
private Thread waiter = null;

private Hashtable sioux = new Hashtable();
private Vector    servers = new Vector();

	public Application() {
		waiter = this;
	}

	public void end() {
	Thread temp = waiter;
		waiter = null;
		temp.interrupt();
		System.exit(0);
	}

	/** register a server, to collect informations later */
	public void add( Infos o) {
		servers.addElement(o);
	}

	public Enumeration servers() {
		return servers.elements();
	}

	public void add( Sioux o) {
		sioux.put( o.getName(), o);
	}

	public void remove( Sioux o) {
		sioux.remove( o.getName() );
	}

	public void remove( String k) {
		sioux.remove( k );
	}

	public Sioux get( String k) {
		return (Sioux)sioux.get( k );
	}

	public Enumeration sioux() {
		return sioux.keys();
	}

	public void run() {
System.out.println( "app control started" );
		while (waiter != null) {
			try {
				waiter.sleep( 1000 * 60);
			} 
			catch (InterruptedException e){
			}
		}
System.out.println( "app control ended" );
	}
}
