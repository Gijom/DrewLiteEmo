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
 * A Label for Users. You click on it, you select the user behind
 *
 */

package Drew.Client.Grapheur;

import java.awt.*;
import java.awt.event.*;

public class UserLabel extends Label implements MouseListener {

	private User user;
	private String userName;
	private boolean colorLabel;
	private Grapheur parent;
	private boolean selected = false;

	private final static Font selectedFont = new Font("SansSerif", Font.BOLD, 10);
	private final static Font normalFont = new Font("SansSerif", Font.PLAIN, 10);
	private Color userColor;

	private int prefWidth = -1;

 	public UserLabel(Grapheur g, User u, boolean colored) {
		this(g,u,colored,-1);
	}

 	public UserLabel(Grapheur g, User u, boolean colored, int width) {
		super("", Label.LEFT);
		parent = g;
		user = u;
		userName = u.getUserName();
		colorLabel = colored;
		prefWidth = width;
		addMouseListener(this);
		userColor = new Color(user.getUserRGBColor());
		
		if (colorLabel) {
			setBackground(userColor);
			setForeground(getBackground());
			setText("mm");
		}
		else {
			setForeground(Color.black);
			setBackground(Color.white);
			setText(userName);
		}
		setFont(normalFont);
	}

	public void mouseEntered(MouseEvent e) {
	}

        public void mouseExited(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseClicked(MouseEvent e) {
		if (isEnabled()) {
			parent.setCurrentUser(user);
		}
        }

	public int getWidth() {
		return getSize().width;
	}

	public  Dimension getPreferredSize() {
		return getSize();
	}

	public  Dimension getSize() {
		Dimension d = super.getPreferredSize();
		if( prefWidth > 0 ) d = new Dimension( prefWidth, d.height );

		return d;
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		if (selected) {
			g.setColor(userColor);
			g.drawRect(0, 0, getSize().width-1, getSize().height-1);
		}
	}
		
	public void setSelected(boolean s) {
		selected = s;
		//String n=">";
		if (selected) { 
			setFont(selectedFont);
			//setText(n.concat(userName));
		}
		else {
			setFont(normalFont);
			//setText(userName);
		}
		repaint();
	}
}
