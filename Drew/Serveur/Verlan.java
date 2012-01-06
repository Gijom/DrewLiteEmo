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
 * File: Verlan.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Verlan.java,v 1.4 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Serveur;

import java.util.StringTokenizer;

/**
* default server module, used if find any 
*/
class Verlan implements Service {

private String cmd = null;

	/**
	* The command to be done
	*/
	public String doCmd( String cmd ) {
		StringTokenizer st = new StringTokenizer(cmd);
		StringBuffer sb = new StringBuffer( cmd.length() );
	
           	while (st.hasMoreTokens()) {
			StringBuffer x = new StringBuffer(st.nextToken() );
               		sb.append( x.reverse().toString() );
			sb.append( ' ' );
           	}

             	return sb.toString();
	}

	/**
	* The command to be done
	*/
	public String doCmd( String cmd, Connexion c ) {
             	return doCmd( cmd ) + "(using " + c.getLang() + ")";
	}

	public void setCmd( String cmd ) {
		this.cmd = cmd;
	}

	public String toString() {
		return doCmd( cmd );
	}
}
