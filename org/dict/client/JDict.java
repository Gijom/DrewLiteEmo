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
 * File: JDict.java
 * Author:
 * Description:
 *
 * $Id: JDict.java,v 1.8 2003/11/24 10:54:31 serpaggi Exp $
 */

package org.dict.client;

import java.io.*;
import java.net.*;
import Drew.Serveur.Service;
import Drew.Serveur.Connexion;

/**
 * Insert the type's description here.
 * Creation date: (24.02.2002 16:33:09)
 * @author: 
 */
public class JDict implements Service {
	String host = "dict.org";
	int port = 2628;
	String db = "*";
	boolean match = false;

/**
 * JDict constructor comment.
 */
public JDict() {
	super();
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:47:00)
 * @return java.lang.String
 */
public java.lang.String getDb() {
	return db;
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @return java.lang.String
 */
public java.lang.String getHost() {
	return host;
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @return int
 */
public int getPort() {
	return port;
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @return boolean
 */
public boolean isMatch() {
	return match;
}

public String lookup(String word) {
	Socket s = null;
	PrintWriter out = null;
	BufferedReader in = null;
	StringWriter result = new StringWriter();

	try {
		s = new Socket(getHost(), getPort());
		out = new PrintWriter(s.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(s.getInputStream()));
		out.println("client \"Java DICT client\"");
		if (!isMatch()) {
			out.println("define "+getDb()+" "+word);
		} else {
			out.println("match "+getDb()+" prefix "+word);
		}
		out.println("quit");
		String fromServer;
		while ((fromServer = in.readLine()) != null) {
			result.write(fromServer); result.write("\n");
		}
		out.close();
		in.close();
		s.close();

	} catch (Exception e) {
		System.err.println(e);
	}

	return result.toString();
}

/**
* implement the Service interface
*/
public String doCmd(String cmd) {
	return lookup( cmd );
}

public String doCmd( String cmd, Connexion c ) {
/* can be used for getting curent user language
*  c.getLang()
*/
	return  doCmd( cmd );
}

public void setCmd( String cmd ) {
}

public static void main(String args[]) {
	if (args.length == 0) {
		System.out.println("Usage: java ...JDict [-h host] [-p port] [-d database] [-m] word");
		System.exit(0);
	}
	JDict c = new JDict();
	for (int i = 0; i < args.length-1; i++){
		if (args[i].equals("-h")) {
			c.setHost(args[++i]);
		} else if (args[i].equals("-p")) {
			try {
				c.setPort(Integer.parseInt(args[++i]));
			} catch (Throwable t) {}
		} else if (args[i].equals("-d")) {
			c.setDb(args[++i]);
		} else if (args[i].equals("-m")) {
			c.setMatch(true);
		}
	}
	System.out.println( c.lookup(args[args.length-1]) );
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:47:00)
 * @param newDb java.lang.String
 */
public void setDb(java.lang.String newDb) {
	db = newDb;
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @param newHost java.lang.String
 */

public void setHost(java.lang.String newHost) {
	host = newHost;
}

/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @param newMatch boolean
 */

public void setMatch(boolean newMatch) {
	match = newMatch;
}
/**
 * Insert the method's description here.
 * Creation date: (24.02.2002 16:42:15)
 * @param newPort int
 */

public void setPort(int newPort) {
	port = newPort;
}
}
