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
 * File: CooperativeModuleList.java
 * Author:
 * Description:
 *
 * $Id: CooperativeModuleList.java,v 1.7.2.1 2007/10/31 14:22:58 collins Exp $
 */

package Drew.Client.Util;

import java.util.*;
import Drew.Util.XMLmp.*;

/**
 * Class qui est chargee de gerer les differents modules qui serviront aux
 * interactions. Les elements de cette liste (enfin une hashtable) sont des
 * CooperativeModule dont tous les modules interactifs sont senses heriter.
 */
public class CooperativeModuleList {

static int DEFAULTSIZE = 10;

private Hashtable hash;

	public CooperativeModuleList() {
		hash = new Hashtable(DEFAULTSIZE);
	}

	public boolean containsKey( String name ) {
		return hash.containsKey(name);
	}

	public void add( String name, CooperativeModule m) {
		hash.put(name, m);
	}

	public CooperativeModule remove( String name) {
		return (CooperativeModule)hash.remove(name);
	}

	public void init() {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.init();
		}
	}       

	public void start() {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.start();
		}
	}       

	public void stop() {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.stop();
		}
	}       

	public void destroy() {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.destroy();
		}
		hash.clear();
	}       

	public void clear() {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.clear();
		}
	}       

	public void messageDeliver(String origine, XMLTree data) {
	CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			m.messageDeliver( origine, data);
		}
	}

	public void cacher() {
		System.err.println("essayer de cacher");
		CooperativeModule m;

		for (Enumeration e = hash.elements() ; e.hasMoreElements() ;) {
			m = (CooperativeModule)e.nextElement(); 
			if (m instanceof ModuleWindow) {
				ModuleWindow p = (ModuleWindow) m;
				p.cacher();
			}
		}
		
	}       
};
