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

/**
 * File: ContradictionAddOn.java
 * Author: Xavier Serpaggi
 * Description: A beginning to make boxes have various shapes (themes).
 *
 * $Id: ContradictionAddOn.java,v 1.4 2003/11/24 10:54:24 serpaggi Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;


public class ContradictionAddOn
	extends Component {

	public final static int FACE = 0 ;
	public final static int THUNDER = 1 ;

	private Point pos ;
	private int r ;
	
	private Graphics g ;

	private int type ;

	public ContradictionAddOn(Graphics g, int x, int y, int r, int type)
	{
		pos = new Point(x,y) ;
		this.r = r ;
		this.g = g ;

		switch (type) {
			case FACE:
			case THUNDER:
				this.type = type ;
				break ;
			default:
				this.type = FACE ;
				break ;
		}
	}
	
	public ContradictionAddOn(Graphics g, Point p, int r, int type)
	{
		pos = new Point(p) ;
		this.r = r ;
		this.g = g ;

		switch (type) {
			case FACE:
			case THUNDER:
				this.type = type ;
				break ;
			default:
				this.type = FACE ;
				break ;
		}
	}
	
	public void draw()
	{
		switch (type) {
			case FACE:
				drawUnhappyFace() ;
				break ;
			case THUNDER:
				break ;
		}
	}

	/** draws an unhappy smiley
	 * @param g the graphic context
	 * @param x abscissa of the center of the circle
	 * @param y ordinate of the center of the circle
	 * @param r the radius of the circle
	 * @param c color of the background of the smiley
	 */
	private void drawUnhappyFace()
	{
		/* Drawing an oval is like drawing a box. How strange ! */
		int X, Y, R, d ;

		
		if ( r < 10 ) { r = 10 ; }
		else if ( r > 50 ) { r = 50 ; }

		X = pos.x - r ;
		Y = pos.y - r ;
		R = r + r ;

		/* The coloured face with only a border */
		g.setColor(Color.yellow) ;
		g.fillOval(X, Y, R, R) ;
		g.setColor(Color.black) ;
		g.drawOval(X, Y, R, R) ;
		
		/* the eyes */
		R = r / 7 ; /* X.S. : why ints ? */
		d = (int)(0.4 * (double)r) ;
		Y = pos.y - d ;
		X = pos.x - d ;
		g.fillOval(X-R, Y-R, 2*R, 2*R) ;
		X = pos.x + d ;
		g.fillOval(X-R, Y-R, 2*R, 2*R) ;
		
		/* the mouth */
		R = 3 * r / 4 ;
		X = pos.x - R ;
		Y = pos.y + (r - R) ;
		R = 2 * R ;
		g.drawArc(X, Y, R, R, 45, 90) ;
		Y = Y - 1 ;
		g.drawArc(X, Y, R, R, 45, 90) ;
	}
}


