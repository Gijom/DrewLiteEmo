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
 * File: Feu.java
 * Author:
 * Description:
 *
 * $Id: Feu.java,v 1.6 2006/04/28 08:35:29 girardot Exp $
 */

package Drew.Client.FeuTricolore;

import java.awt.*;

class Feu
	extends Canvas {

	Color on, off, curr;
	// Dimension minSize;
	
	Feu (Color c)
	{
		super();
		int att = 2;
		on = c;
		off = new Color((int)(c.getRed()/att), (int)(c.getGreen()/att), (int)(c.getBlue()/att));
		curr = on;
		// background color set to gray
		setBackground(Color.darkGray);
	}

	void lightOn() {
		curr = on;
	}

	void lightOff() {
		curr = off;
	}

	public void paint (Graphics g) {
		Dimension sz = getSize();
		// Set current color
		g.setColor(curr);
		g.fillOval(1,1,sz.width-1,sz.height-1);
		g.drawOval(1,1,sz.width-1,sz.height-1);
	}
}
