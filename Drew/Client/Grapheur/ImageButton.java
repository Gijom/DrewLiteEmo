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

package Drew.Client.Grapheur;

import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.Vector;

/**
 * Un bouton avec une image actif/inactif et toolTip
 */
//public class ImageButton extends Container implements MouseListener
public class ImageButton extends Canvas implements MouseListener {
	private Image imageActif, imageInactif ;

	private final static Color UL_OUT = new Color( 0xC0, 0xC0, 0xC0 );
	private final static Color UL_IN  = new Color( 0xEE, 0xEE, 0xEE );
	private final static Color LR_IN  = new Color( 0x80, 0x80, 0x80 );
	private final static Color LR_OUT = new Color( 0x00, 0x00, 0x00 );

	protected boolean pressed;

	private boolean wasPressed;

	private ActionListener aListener;

	private String command;
	private String tip;
	private Tips myTip = null;
	private Container parent;

	private final static Dimension size = new Dimension(33,33);

	public ImageButton(Container parent, Image actif, String tip) {
		this(parent, actif, actif, tip);
	}

	public ImageButton(Container parent, Image actif, Image inactif, String tip) {
                setImageActif(actif);
                setImageInactif(inactif);
		this.tip = tip;
		this.parent = parent;
                setSize( size );
                // image.getWidth(null) + 4, image.getHeight(null) + 4 ));
                addMouseListener(this);
		myTip = null;
                setEnabled( true );
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String s) {
		tip = s;
	}

	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		//System.err.println(" imageUpdate called ("+tip+"): " + infoflags + " " 
		//+ WIDTH  + " " +HEIGHT + " " + PROPERTIES + " " + SOMEBITS + " " + FRAMEBITS +" "+ ALLBITS   );

		if( ( infoflags & ALLBITS) != 0 ) {
			repaint();
			return false;
		}
		return true;
	}

	public void update(Graphics g) {

		if( isPressed() && isEnabled() ) drawLoweredBorder(g);
		else drawRaisedBorder(g); 

		Image tampon = getImageInactif();
		if( isEnabled() || tampon == null )
			tampon = getImageActif();

		//Si l'image du bouton n'est pas disponible, on s'arrete la.
		if( (checkImage(tampon, null ) & ALLBITS) == 0 )  return;

		Dimension totalSize = getSize();

		try {
			int imageHeight = tampon.getHeight( null ) / 2;
			int imageWidth = tampon.getWidth( null ) / 2;

			int halfHeight = totalSize.height / 2;
			int halfWidth = totalSize.width / 2;

			g.drawImage( tampon, halfWidth - imageWidth + 1, halfHeight - imageHeight + 1, null );
		} catch (NullPointerException e) {
//			we can see there is no image
		}

	}

	public void paint(Graphics g) {
		update(g);
	}

	private void drawRaisedBorder(Graphics g) {
		int w = getPreferredSize().width;
		int h = getPreferredSize().height;

		g.setColor( UL_OUT );
		g.drawLine( 0, 0, 0, h );			// All the way down
		g.drawLine( 0, 0, w - 1, 0 );	// Nearly all the way right

		g.setColor( UL_IN );
		g.drawLine( 1, 1, 1, h - 1 );	// Nearly all the way down
		g.drawLine( 1, 1, w - 2, 1 );	// Almost all the way right

		g.setColor( LR_IN );
		g.drawLine( 2, h - 1, w - 1, h - 1 );	// Nearly all the way right
		g.drawLine( w - 1, 1, w - 1, h - 1 );	// Almost all the way down

		g.setColor( LR_OUT );
		g.drawLine( 1, h, w, h );	// All the way right
		g.drawLine( w, 0, w, h );	// All the way down
	}

	private void drawLoweredBorder( Graphics g ) {
		int w = getPreferredSize().width;
		int h = getPreferredSize().height;

		g.setColor( LR_OUT );
		g.drawLine( 0, 0, 0, h );			// All the way down
		g.drawLine( 0, 0, w - 1, 0 );	// Nearly all the way right

		g.setColor( LR_IN );
		g.drawLine( 1, 1, 1, h - 1 );	// Nearly all the way down
		g.drawLine( 1, 1, w - 2, 1 );	// Almost all the way right

		g.setColor( UL_IN );
		g.drawLine( 2, h - 1, w - 1, h - 1 );	// Nearly all the way right
		g.drawLine( w - 1, 1, w - 1, h - 1 );	// Almost all the way down

		g.setColor( UL_OUT );
		g.drawLine( 1, h, w, h );	// All the way right
		g.drawLine( w, 0, w, h );	// All the way down
	}

	public void mouseClicked(MouseEvent e) {
		if( isEnabled() && (aListener != null)) {
			//On construit un evenement et on le transmet au listener
			ActionEvent action = new ActionEvent(
				this, ActionEvent.ACTION_PERFORMED, command, e.getModifiers() );
			aListener.actionPerformed(action);
		}
	}

	public void mousePressed( MouseEvent me ) {
		wasPressed = true;
		setPressed(true);
		repaint();
	}

	public void mouseReleased( MouseEvent me ) {
	// On relache la souris sur le bouton
		wasPressed = false;
		setPressed(false);
		repaint();
	}

	public void mouseExited( MouseEvent me ) {
		if( isPressed() ) {
			setPressed( false );
			wasPressed = true;
			repaint();
		}
			if (myTip != null) {
				myTip.stop();
				myTip = null;
			}
	}

	public void mouseEntered( MouseEvent me ) {
		if( wasPressed ) {
			setPressed( true );
			wasPressed = false;
			repaint();
		}
			myTip = new Tips(parent, this, tip);
			myTip.start();	
	}

	public void addActionListener(ActionListener a) {
		aListener = a;
	}

	public void setActionCommand(String s) {
		command = s;
	}

	public Dimension getPreferredSize() { 
		return size; 
	}

	public Dimension getMinimumSize() { 
		return getPreferredSize(); 
	}

	protected Image getImageActif() { 
		return imageActif; 
	}

	protected void setImageActif(Image i) { 
		this.imageActif = i; 
		prepareImage( i,this );
	}

        protected Image getImageInactif() { 
                return imageInactif;  
        }

        protected void setImageInactif(Image i) { 
                this.imageInactif = i;  
		prepareImage( i,this );
        }

	public void setEnabled( boolean enable ) {
		if( enable != isEnabled() ) {
			super.setEnabled(enable);
			repaint();
		}
	}

	protected boolean isPressed() { 
		return pressed; 
	}

	private void setPressed( boolean b ) { 
		pressed = b; 
	}
}

