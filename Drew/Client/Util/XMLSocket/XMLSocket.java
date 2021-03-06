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
 * File: XMLSocket.java
 * $Author: serpaggi $
 * Description:
 *
 * $Id: XMLSocket.java,v 1.2 2003/11/24 10:54:26 serpaggi Exp $
 *
 * $Log: XMLSocket.java,v $
 * Revision 1.2  2003/11/24 10:54:26  serpaggi
 *  - LGPL header for all .java files
 *
 * Revision 1.1  2003/11/04 11:13:20  jaillon
 * Add a standalone, http and raw mode
 *
 */

package Drew.Client.Util.XMLSocket;

import Drew.Util.XMLmp.*;

public interface XMLSocket {
	public void write( XMLTree o ) throws java.io.IOException; 
	public XMLTree read() throws java.io.IOException;
	public void flush() throws java.io.IOException;
	public void close() throws java.io.IOException;
}
