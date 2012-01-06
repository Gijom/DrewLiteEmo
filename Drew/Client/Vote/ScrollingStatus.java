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
 * File: ScrollingStatus.java
 * Author: Xavier Serpaggi
 * Description: A scrolling bar chart.
 * 
 * $Id: ScrollingStatus.java,v 1.10 2003/12/10 09:16:54 jaillon Exp $
 */

package Drew.Client.Vote;

import java.awt.*;

class ScrollingStatus
	extends Canvas
	implements Runnable {

	private final static int width = 400 ;
	private final static int height = 300 ;
	private final static int BWIDTH = 300 ;
	private final static int BHEIGHT = 50 ;

	private float		value ;
	private int		updateTime,
				wait ;
	private boolean		initDone ;
	private Image		buff1 = null,
				buff2 = null ,
				screen = null;
	private Graphics	gBuff1 = null,
				gBuff2 = null ,
				gScreen = null;
	private Dimension 	dScreen = null;

	private Thread		scrollThread = null ;
	private Color		bg = null ;
		
	private Drew.Util.Locale locale ;

	
	public ScrollingStatus(int wait, Drew.Util.Locale l) {
		super() ;
			
		if ( wait < 1 ) {
			this.wait = 1 ;
		} else {
			this.wait = wait ;
		}
		updateTime = this.wait * 1000 ;
		
		initDone = false ;

		locale = l ;
	}

	public ScrollingStatus(Drew.Util.Locale l) {
		this(5, l) ;
	}

	
	private boolean initBuffers() {
		if ( !initDone ) {
			bg = getParent().getBackground() ;
			setBackground(bg) ;

			// a buffer for 5mn if update each second
			buff1  = createImage(BWIDTH, BHEIGHT) ;
			buff2  = createImage(BWIDTH, BHEIGHT) ;
			screen = createImage(width, height) ;
			dScreen = new Dimension(width, height);

			if ( buff1 != null && buff2 != null ) {
				gBuff1 = buff1.getGraphics() ;
				gBuff2 = buff2.getGraphics() ;
				gScreen = screen.getGraphics() ;
			
				gBuff1.setClip(0, 0, BWIDTH, BHEIGHT) ;
				gBuff2.setClip(0, 0, BWIDTH, BHEIGHT) ;
				gScreen.setClip(0, 0, width, height) ;

				initDone = true ;
			} else {
				System.err.println("Buffer not created!") ;
			}
		}

		return initDone ;
	}

	private void scroll() {
		// Erase the destination buffer ...
		gBuff2.setColor(bg) ;
		gBuff2.fillRect(0, 0, BWIDTH, BHEIGHT) ;
		gBuff2.setColor(Color.blue) ;
		// ... shift-copy the first buffer in the second one, ...
		gBuff2.drawImage(buff1, -1, 0, null) ;

		// ... add the last value-bar, ...
		if ( value >= 0f ) {
			gBuff2.setColor(Color.blue) ;
			gBuff2.fillRect(
				BWIDTH - 1, 0,
				1, (int)(value * BHEIGHT)) ;
			gBuff2.setColor(Color.red) ;
			gBuff2.fillRect(
				BWIDTH - 1, (int)(value * BHEIGHT),
				1, BHEIGHT - (int)(value * BHEIGHT)) ;
		}
		// ... then copy it back to the first buffer.
		gBuff1.drawImage(buff2, 0, 0, null) ;
	}
	
	public boolean isDoubleBuffered() {
		return true;
	}

	public void start() {
		if ( scrollThread == null ) {
			scrollThread = new Thread(this, "Scrolling Status") ;
			scrollThread.start() ;
		}
	}
	
	public void run() {
		Thread myThread = Thread.currentThread() ;
		myThread.setPriority(Thread.MIN_PRIORITY);

		while ( scrollThread == myThread ) {
			try {
				Thread.sleep(updateTime) ;
				if ( scrollThread != null 
				&&   initBuffers() == true ) {
					scroll() ;
					repaint() ;
				}
			} catch( InterruptedException e ) {}
		}

	}
	
	public void stop () {
		scrollThread = null ;
	}

	
	/**
	 * Sets the value of the last segment.
	 * @param val a value between 0 and 1
	 */
	void setValue(float val) {
		if ( val > 1f ) {
			val = 1f ;
		}
		value = val ;
		repaint() ;
	}

/*
	public Dimension getPreferredSize() {
		return new Dimension(width, height) ;
	}
*/
	public void paint(Graphics g) {
		if ( initBuffers() == true ) {
			Dimension d = getSize();
			if( (d.width != dScreen.width ) || (d.height !=  dScreen.height)) {
				gScreen.dispose();
				screen = createImage(getSize().width, getSize().height) ;
                                gScreen = screen.getGraphics() ;
				dScreen = d;
			}
			// invert top down the image
			gScreen.drawImage( buff2,
				0, 0, getSize().width, getSize().height,
				0, BHEIGHT, BWIDTH, 0,
				null) ;
			gScreen.setColor(Color.white) ;
			gScreen.drawString( locale.getString("History"),
				(getSize().width - gScreen.getFontMetrics().stringWidth(locale.getString("History"))) / 2,
				getSize().height / 2 + 5) ;

			g.drawImage(screen,0,0,null);
		}
	}
}
