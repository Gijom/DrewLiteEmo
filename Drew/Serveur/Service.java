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
 * File: Service.java
 * Author: Ph.jaillon
 * Description:
 *
 * $Id: Service.java,v 1.4 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Serveur;

/**
* Interface for server modules like dictionary
*/
public interface Service {

	/**
	* The commande to be done
	*/
	public String doCmd( String cmd );

        /**
        * The command to be done
        */
        public String doCmd( String cmd, Connexion c );

        /**
        * The command to be done
        */
        public void setCmd( String cmd );
}
