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
 * File: Boite.java
 * Author:
 * Description:
 *
 * $Id: Boite.java,v 1.17 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*; 
import java.util.*;

/** Forme d'argument de type Boite pour les propositions. */
public class Boite 
	extends Argument {

	/** La fonte normale des etiquettes de Boites */
	private final static Font plainFont = 
		new Font(Argument.plainFont.getName(), 
				Font.PLAIN, 
				Argument.plainFont.getSize());
	

	/** la fonte pour mettre en �vidence les boites selectionnees */
	private final static Font highlightFont =
		new Font(Argument.plainFont.getName(), 
				Font.BOLD, 
				Argument.plainFont.getSize());

	/** Constructeur de boites. C'est pareil qu'un argument. Rien de plus. */
	public Boite(Argumentation argu, String id) {
		super(argu, id);
	}

	public Boite(Argumentation argu, String id, String number) {
		super(argu, id) ;

		setNumber(number) ;
	}

	Font getHighlightFont(){ return highlightFont;}
	Font getPlainFont(){ return plainFont;}

	public Sticker getSticker() {
		return gom ;
	}

	/** Retourne la forme que doit prendre la boite en fonction de son etat. */
	int getForme(){
		int forme = 0;
		switch (getAgreement()) {
			case UNANIMITY: 
				forme = RECTANGLE;
				break;
			case ABSTENTION: 
				forme = RECTANGLE ;
				break;
			case CONFLICT: 
				forme = ETOILE ;
				break;
		}
		return forme;
	}

	/** Calcule les dimensions du cadre de l'etiquette en fonction de
	 * ses largeur et hauteur minimales
	 * @param largeur la largeur du nom de l'argument
	 */
	Dimension getLabelSize(int largeur, int hauteur){
		return new Dimension(largeur, hauteur);
	}

	public void paint(Graphics g) {
		paint(g, 1.) ;
	}

	public void paint(Graphics g, double c) {

		super.paint(g,c) ;
		
		/* The little sticker to show the number of the argument
		*/
		if ( gom != null ) {
			gom.setLocation(0, 0) ;
			gom.update(g,c) ;
		}
	}

	String printType(){
		//{"print.boite.typebox", "Type : Box" },
		return argumentation.papa.comment.getString("print.boite.typebox");
	}


    /** Optimize the bounds of the box */
    public void autoResize(){
	FontMetrics fm = getFontMetrics(getPlainFont());
	int hauteurLigne = fm.getAscent() - fm.getDescent();

	int largeur = fm.stringWidth(getName()); //Longueur de la
						 //chaine sur une
						 //ligne
	int hauteur = 2*hauteurLigne;
	int margeVerticale = 7;
	int coeff = getCoeff(largeur+2*margeVerticale, hauteur+2*margeVerticale);	

	int nblignes =1;

	while (coeff != 2){
	    largeur = largeur*coeff/2;
	    nblignes = getLines(largeur);
	    hauteur = 2*hauteurLigne*nblignes;
	    coeff = getCoeff(largeur+2*margeVerticale, hauteur+2*margeVerticale);	
	}
	//System.out.println("Nb lignes = "+nblignes);    
	setSize(largeur+2*margeVerticale, hauteur+2*margeVerticale);
    }

    /** Calculates the number of lines shorter than <tt>largeurMax</tt> that are
     * necessary to display the label */  
    private int getLines(int largeurMax){
	FontMetrics fm = getFontMetrics(getPlainFont());
	String currentLine;
	String nameCopy = getName() ;
	Vector v = new Vector();
	int hauteur = fm.getAscent() - fm.getDescent();
	int largeur = fm.stringWidth(nameCopy);
	int l_c = largeur / nameCopy.length();		// mean size per char
	int nc = Math.max(1, largeurMax / l_c );	// nb of char per line, NOTE: no less than one ;-) 
	int i = 0, e = 0;
	int nameCopyLength = nameCopy.length() ;	// length of the whole comment
	int sp ;	// index of the last space char ins a substring
	int br ;	// index of a carriage return
	int nl ;	// number of lines to display
	int totalHeight ;	// the height of the text to display
	boolean tooLong = false ;
	
	
	// First, split where '\n' are
	while( i < nameCopyLength ) {
	    sp = nameCopy.indexOf('\n', i) ;
	    if ( sp == -1 ) {
		sp = nameCopyLength ;
	    }
	    String sub = nameCopy.substring(i, sp) ;
	    v.addElement(sub) ;
	    i = sp + 1 ;
	}
	
	// Then, split long paragraphs
	int vi = 0 ;

	while ( vi < v.size() ) {
	    String par = (String)v.elementAt(vi) ;
	    int parLength = par.length() ;
	    	    
	    v.removeElementAt(vi) ;
	    
	    i = 0 ;
	    while( i < parLength ) {
// 		String s = par.substring(i);
// 		int slen = fm.stringWidth(s);
// 		if (1.1*slen <= largeurMax){
		if ((nc+i)>= 1.1*parLength){
		    e = parLength ;
		    sp = 0 ;
		    //System.err.println(par + " fits") ;
		} else {	// Need to split. Search for the last space char in the substring.
		    e = i + nc - 1 ;
		    sp = par.lastIndexOf(' ', e) ;
		    //System.err.println(par + " splitted") ;
		}
		
		if ( sp > i ) {
		    e = sp ;
		}
		
		String sub = par.substring(i, e) ;
		v.insertElementAt(sub, vi) ;
		vi ++ ;
		if ((e > 0) && (e < parLength) && (par.charAt(e) == ' ')) i=e+1;
		else i=e;
	    }
	}
	
	nl = v.size();				// number of lines to display
	totalHeight = 2 * nl * hauteur ;	// total
	// height of
	// the lines
	
	return (nl);
    }

    private int getCoeff(int largeur, int hauteur){
	if (largeur > 4*hauteur) 
	    return 1;
	else if (hauteur > largeur)
	    return 3;
	else return 2;
    }
}
