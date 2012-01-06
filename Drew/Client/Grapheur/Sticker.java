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
 * File: Sticker.java
 * Author:
 * Description:
 *
 * $Id: Sticker.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Grapheur ;

import java.awt.* ;

public class Sticker
	extends Component
{
	private static Font stickerFont = new Font("SansSerif", Font.BOLD, 10);
	private static double last_scale = 1.0;
	private String number ;
	private Argument arg ;
	
	// add this the the getLocation().y to obtain the baseline of the text
	private int x_offset ;
	private int y_offset ;


	
	public Sticker(Argument arg, Point pos)
	{
		Graphics g = arg.getArgumentation().getGraphics() ;

		
		this.arg = arg ;
		
		updateNumber(arg.getNumber());

		setLocation(pos) ;
	}


	public void setNumber(int n)
	{
		number = new Integer(n).toString() ;
	}

	public void setNumber(String n)
	{
		number = n ;
	}

	public void updateNumber(int n)
	{
		updateNumber(new Integer(n).toString()) ;
	}

	public void updateNumber(String n)
	{
		int max ;
		FontMetrics fm ;
		Graphics g = arg.getArgumentation().getGraphics() ;

		
		setNumber(n) ;
		
		g.setFont(stickerFont) ;
		fm = g.getFontMetrics() ;

		max = (int)(Math.max(fm.stringWidth(number), fm.getHeight())) + 4 ;
		setSize(new Dimension(max, max)) ;

		// We're dealing with numbers so we suppose there are no 'descent' part.
		x_offset = 1 + (max - fm.stringWidth(number)) / 2 ;
		y_offset = (fm.getAscent() + max) / 2 - 1 ;
	}

	public String getNumber()
	{
		return number ;
	}

	public void update(Graphics g, double c)
	{
		paint(g, c);	
	}

	public void update(Graphics g)
	{
		paint(g, 1.0);	
	}

	public void paint(Graphics g) {
		paint( g, 1.0 );
	}

	public void paint(Graphics g, double c)
	{
		//setLocation(arg.getLocation().x - getSize().width / 2, arg.getLocation().y + arg.getSize().height - getSize().height / 2) ;

		int x = (int)(getLocation().x * c);
		int y = (int)(getLocation().y * c);
		int w = (int)(getSize().width * c);
		int h = (int)(getSize().height * c);
		int d = (int)(3 * c);

		g.setColor(Color.lightGray) ;
		//g.fillOval(getLocation().x + 3, getLocation().y + 3, getSize().width, getSize().height) ;
		g.fillOval(x + d, y + d, w, h) ;
		g.setColor(new Color(255,100,100)) ;
		g.fillOval(x, y, w, h) ;

		g.setColor(Color.black) ;
		g.drawOval(x, y, w, h) ;
		
		g.setColor(Color.white) ;
		if( last_scale != c ) {
			last_scale = c;
			stickerFont = new Font("SansSerif", Font.BOLD, (int)(10*c)) ;
		}
		g.setFont(stickerFont) ;
		//g.drawString(number, getLocation().x + getSize().width / 3, getLocation().y + offset) ;
		g.drawString(number, x + (int)(x_offset*c), y + (int)(y_offset*c) ) ;
	}
}
/* vim:tabstop=8 nowrap:
*/
