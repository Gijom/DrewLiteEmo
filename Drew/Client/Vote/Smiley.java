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
 * File: Smiley.java
 * Author: Xavier Serpaggi
 * Description: Displays a happy or an unhappy smiley
 * 
 * $Id: Smiley.java,v 1.2 2003/11/24 10:54:26 serpaggi Exp $
 */

package Drew.Client.Vote;

import java.awt.*;

class Smiley
	extends Canvas {

	private boolean happy ;

	
	Smiley(boolean state) {
		super() ;
		happy = state ;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(32, 32) ;
	}

	public void paint(Graphics g) {
		int w, h ;
		
		w = getSize().width ;
		h = getSize().height ;

		setBackground(getParent().getBackground()) ;

		g.setColor(Color.yellow) ;
		g.fillOval(2, 2, w-4, h-4) ;
		g.setColor(Color.black) ;
		g.drawOval(2, 2, w-4, h-4) ;
		if ( happy ) {
			g.drawArc(w/4, h/3, w/2, h/2, 190, 160) ;
			g.drawArc(w/4, h/3+1, w/2, h/2, 190, 160) ;
		} else {
			g.drawArc(w/4, 2*h/3, w/2, h/2, 25, 130) ;
			g.drawArc(w/4, 2*h/3-1, w/2, h/2, 25, 130) ;
		}
		g.fillOval(w/4, h/4, w/5, h/5) ;
		g.fillOval(6*w/10, h/4, w/5, h/5) ;
	}
}
