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
 * File: Tips.java
 * Author: Xavier Serpaggi
 * Description: A basic tooltip class.
 *
 * $Id: Tips.java,v 1.13 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Grapheur ;

import java.awt.* ;
import java.util.*;
import java.lang.Thread ;

public class Tips
	extends Component
	implements Runnable
{
	private int popup_delay = 400 ;	// delay (milliseconds) before the toolip appears
	private Point tipPos ;		// location of the tooltip
	private Dimension tipSize ;	// size of the tooltip
	private int arg_width ;
	
	public final static Color background = new Color(255, 255, 180) ;
	
	/** Une fonte pour tous les arguments */
	private static Font plainFont = new Font("SansSerif", Font.PLAIN, 10);

	/** The text of the tip (that is, the comment of the argument).
	*/
	private String comment;

	/** le graphe d'arguments auquel l'argument est reli� */
	//private Argumentation argumentation ;
	private Container parent;
	private Component comp;


	private Thread tipThread = null ;
	private boolean drawn = false ;
	
	public Tips(Argumentation argu, Argument arg) {
		this(argu, arg, arg.getComment());
	}

	public Tips(Container cont, Component comp, String s) {
		parent = cont ;
		this.comp = comp;
		
		setComment(s);

		tipSize = new Dimension(comp.getSize()) ;
		tipSize.width = (tipSize.width<100)?100:tipSize.width ;
		setSize(tipSize) ;
		arg_width = comp.getSize().width ;

		// The tooltip is under the argument. This may cause problems near borders
		// We also move it to the left when needed, so it is centered under the argument.
		tipPos = new Point() ;
		tipPos.x = comp.getLocation().x ;
		tipPos.y = comp.getLocation().y + tipSize.height ;
		setLocation(tipPos);
		
		//System.err.print("Tip : where ("+tipPos.x+","+tipPos.y+")\n") ;
		//System.err.print("Tip : size ("+tipSize.width+","+tipSize.height+")\n") ;
	}

	public void start()
	{
		if ( tipThread == null ) {
			tipThread = new Thread(this, "Comment") ;
			tipThread.start() ;
		}
	}

	
	// Required by the Runnable interface
	public void run()
	{
		Thread myThread = Thread.currentThread() ;


		while ( tipThread == myThread ) {
			try {
				Thread.sleep(popup_delay) ;
				if ( tipThread != null && drawn == false ) {
					this.paint(parent.getGraphics()) ;
					setEnabled(true);
					setVisible(true);
					drawn = true ;
				}
			} catch( InterruptedException e ) {}
		}
	}

	public void stop()
	{
		setVisible(false) ;
		setEnabled(false);
		tipThread = null ;
		parent.repaint() ;
	}

	public void setDelay(int delay)
	{
		popup_delay = delay ;
	}

	public int getDelay()
	{
		return popup_delay ;
	}

	public boolean isActive()
	{
		return ( tipThread != null ) ;
	}

	/** Donner les infos suppl�mentaires sur l'argument.
	  @see #comment
	  */    
	public void setComment(String s)
	{
		comment = s ;
	}

	/** Obtenir les infos suppl�mentaires sur l'argument.
	  @see #comment
	  */
	public String getComment()
	{
		return comment ;
	}

	private int getLongestWordLength(String s)
	{
		int l = 0, max = 0 ;


		for ( int p = 0 ; p < s.length() ; p++ ) {
			if ( s.charAt(p) == ' ' ) {
				max = Math.max(max, l) ;
				l = 0 ;
			} else {
				l ++ ;
			}
		}

		return max ;
	}

	private String getLongestWord(String s)
	{
		int l = 0, max = 0 ;
		int begin = 0, end=s.length() ;

		
		for ( int p = 0 ; p < s.length() ; p++ ) {
			if ( s.charAt(p) == ' ' ) {
				if ( l > max ) {
					max = l ;
					begin = p - l ;
					end = p ;
				}
				l = 0 ;
			} else {
				l ++ ;
			}
		}

		return s.substring(begin, end) ;
	}
	
	/** Compute the Dimensions of the label
	 * @param w width of arg name
	 * @param h height of arg name
	 */
	Dimension getLabelSize(int w, int h)
	{
		return new Dimension(w, h) ;
	}

	public void update(Graphics g)
	{
		paint(g);	
	}

	public void paint(Graphics g)
	{
		int hauteur, largeur, cx, cy ;
		int l_c, nc, i, e, nl, sp, cl ;
		int longestLine = 0 ;
		int padding = 14 ; // Total padding (left + right)
		String longestWord ;
		String currentLine ;
		String commentCopy = comment.replace('\n', ' ') ;
		FontMetrics fm ;
		Vector v ;

		
		g.setFont(plainFont) ;
		fm = g.getFontMetrics() ;

		hauteur = fm.getAscent() - fm.getDescent() ;
		largeur = fm.stringWidth(commentCopy) ;

		if (!(comp instanceof Argument)) {
			setSize(largeur+6, fm.getHeight()+6);
			tipSize = getSize();
			tipPos.x = comp.getLocation().x -10 ;
			if (tipPos.x < 0) tipPos.x =1 ;
			else tipPos.x = Math.min(tipPos.x, parent.getSize().width - tipSize.width-3);

			//tipPos.y -= 0.5 * tipSize.height ;
			setLocation(tipPos) ;

			g.setColor(Color.lightGray) ;
                	g.fillRect(tipPos.x + 3, tipPos.y + 3, tipSize.width, tipSize.height) ;
                	g.setColor(background) ;
                	g.fillRect(tipPos.x, tipPos.y, tipSize.width, tipSize.height) ;
                	g.setColor(Color.black) ;
                	g.drawRect(tipPos.x, tipPos.y, tipSize.width, tipSize.height) ;
			g.drawString(comment, tipPos.x + 3, tipPos.y + 3 + fm.getAscent());
			return;
		}

		v = new Vector() ;
		if ( largeur > tipSize.width - padding ) {
			// faut faire des choses, on calcule la taille moyenne par caractere
			l_c = largeur / commentCopy.length() ;

			// The longest word of the commentCopy string should not be split
			longestWord = getLongestWord(commentCopy) ;
			largeur = fm.stringWidth(longestWord) ;
			// nb de caractere par lignes, attention, pas moins de 1 ;-)
			if ( largeur > tipSize.width - 15 ) {
				nc = getLongestWordLength(commentCopy) + 2 ;
				tipSize.width = nc * l_c + 15 ;
			} else {
				nc = Math.max(1, (tipSize.width - padding) / l_c - 2) ;
			}
			i = 0 ;
			e = 0 ;
			cl = commentCopy.length() ;
			while ( i < cl ) {
				if ( (i + nc) >= cl ) {
					e = cl ;
					sp = 0 ;
				} else {
					e = i + nc ;
					sp = commentCopy.lastIndexOf(' ', e) ;
				}
				
				if ( sp > i ) { e = sp ; }

				v.addElement( commentCopy.substring(i, e) ) ;
				i = e ;
			}
		} else {
			v.addElement( commentCopy ) ;
		}

		nl = v.size() ;
		tipSize.height = 2 * (nl + 1)  * hauteur ;
		setSize( tipSize ) ;
		
		tipPos.x = tipPos.x - (tipSize.width - arg_width) / 2 ;

		// Some people prefer the tooltip encroaching on the box !
		tipPos.x += 0.1 * tipSize.width ;
		tipPos.y -= 0.1 * tipSize.height ;

		setLocation(tipPos) ;

		cx = tipPos.x + tipSize.width / 2 ;
		cy = tipPos.y + tipSize.height / 2 ;

		g.setColor(Color.lightGray) ;
		g.fillRect(tipPos.x + 3, tipPos.y + 3, tipSize.width, tipSize.height) ;
		g.setColor(background) ;
		g.fillRect(tipPos.x, tipPos.y, tipSize.width, tipSize.height) ;
		g.setColor(Color.black) ;
		g.drawRect(tipPos.x, tipPos.y, tipSize.width, tipSize.height) ;

		cy = cy + (3 * hauteur / 2) - (nl * hauteur) ;
		for ( i = 0 ; i < nl ; i++ ) {
			currentLine = (String)v.elementAt(i) ;
			// g.drawString(currentLine, cx - fm.stringWidth(currentLine) / 2, cy) ;
			g.drawString(currentLine, tipPos.x + 2, cy) ;
			cy += (2 * hauteur) ;
		}
	}
}
/* vim:tabstop=8 nowrap:
*/
