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
 * File: InputStream.java
 * Author:
 * Description:
 *
 * $Id: InputStream.java,v 1.4 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Serveur.HTTP;

import java.io.*;

class InputStream extends java.io.InputStream {
private Session s;

	public InputStream(Session s) {
		this.s = s;
	}

	public int available() throws IOException {
		return s.available();
	}

	public void close() throws IOException {
		s.close();
	}

 	public int read() throws IOException {
		return s.read();
	}

 	public int read(byte[] b) throws IOException {
		return s.read(b);
	}

 	public int read(byte[] b, int off, int len) throws IOException {
		return s.read(b, off, len);
	}
}
