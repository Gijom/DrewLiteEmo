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
 * File: Fleche.java
 * Author:
 * Description:
 *
 * $Id: Fleche.java,v 1.12.2.1 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;
import java.util.*;

/** Les arguments de type Fleche pour les justifications. Ce sont des arguments standards avec en plus la possibilit� d'avoir des ant�c�dents (contenus qui justifient) et des cons�quents (des contenus justifi�s). Ces contenus sont des arguments : donc potentiellement des boites ou des fleches !*/
public class Fleche extends Argument {

	private final static Font plainFont =
		new Font(Argument.plainFont.getName(), 
				Font.PLAIN, 
				Argument.plainFont.getSize()+2);

	private final static Font highlightFont = 
		new Font(Argument.plainFont.getName(), 
				Font.BOLD, 
				Argument.plainFont.getSize()+2);

	Font getHighlightFont(){ return highlightFont;}
	Font getPlainFont(){ return plainFont;}

	/** Constructeur de fleche. On ne construit que
	  des fleches vides par d�faut, que l'on doit sp�cifier par la suite.
	  <p> Fleche doit redefinir la m�thode abstraite paint.
	  */
	public Fleche(Argumentation argu, String id) {
		super(argu, id);
		setName(argu.papa.comment.getString("SymRelation0"));
		upstream = new Vector(1,1) ;
		downstream = new Vector(1,1) ;
	}

	/** obtenir la liste des ant�c�dents de la justification. 
	  @see #upstream
	  */
	Vector getAntecedent(){
		return upstream ;
	}

	/** Ajouter un ant�c�dent a la justification.
	  @see #upstream
	  */
	void addAntecedent(Argument a){
		if (!upstream.contains(a)) upstream.addElement(a) ;
	}

	/** Enlever un ant�c�dent a la justification.
	  @see #upstream
	  */
	void removeAntecedent(Argument a){
		if (upstream.contains(a)) upstream.removeElement(a) ;
	}


	/** obtenir la liste des downstreams de la justification. 
	  @see #downstream
	  */
	Vector getConsequent(){
		return downstream ;
	}

	/** d�finir le cons�quent de la justification. Unique pour l'instant.
	  @see #downstream
	  */
	void addConsequent(Argument a){
		if (!downstream.contains(a)) downstream.addElement(a) ;
	}

	/** Enlever un cons�quent a la justification.
	  @see #upstream
	  */
	void removeConsequent(Argument a){
		if (downstream.contains(a)) downstream.removeElement(a) ;
	}


	String printType(){
		// {"print.fleche.type", "Type : Relation between {0} and {1}." },
		String from = printArgumentList(getAntecedent());
		String to   = printArgumentList(getConsequent());

		return argumentation.papa.comment.format( "print.fleche.type", from, to );
	}
	
	String printArgumentList(Vector v) {
		// {"print.fleche.nothing", "nothing" },
		if (v.isEmpty()) return argumentation.papa.comment.getString("print.fleche.nothing");

		String s = "(";
		int nc = v.size();
		for (int i = 0 ; i< nc-1 ; i++) {
			s = s + argumentation.getComponentNumber((Component)v.elementAt(i)) + ", ";
		}
		s =  s + argumentation.getComponentNumber((Component) v.elementAt(nc-1)) + ")";
		return s;
	}

	/** Forcer le re-dessin des fleches, car Argumentation.update ne fait
	  pas les choses comme il faut.
	  */
	void dessinerLesFleches(Graphics g){
		dessinerLesFleches(g,1.);
	}

	void dessinerLesFleches(Graphics g, double c){
		Argument toto ;
		int i;
		g.setColor(Color.black);
		// Dessin des fleches avec pointe vers downstreams
		for (i = 0 ; i < downstream.size() ; i++) {
			toto = (Argument) downstream.elementAt(i);
			if (toto.isEnabled()) dessineFleche(g,this, toto, true, c);
		}

		// Dessin des fleches sans pointe venant des upstreams
		for (i = 0 ; i < upstream.size() ; i++) {
			toto = (Argument) upstream.elementAt(i);
			if (toto.isEnabled()) dessineFleche(g,this, toto, false, c);
		} 	
	}


	/** dessine une fleche entre deux points */
	private void dessineFleche(Graphics g, Argument o, Argument d, boolean pointe){
		dessineFleche(g, o, d, pointe, 1.);
	}

	private void dessineFleche(Graphics g, Argument o, Argument d, boolean pointe, double c){
		int a = d.getCenterLocation().x - o.getCenterLocation().x ;
		int b = d.getCenterLocation().y - o.getCenterLocation().y ;

		int x1 = o.getLocation().x + o.getSize().width/2;
		int y1 = o.getLocation().y + o.getSize().height/2;
		int x2 = d.getLocation().x + d.getSize().width/2;
		int y2 = d.getLocation().y + d.getSize().height/2;

		Point p1 = getPoignee(x1,y1,o.getSize().width, 
				o.getSize().height, a, b);
		Point p2 = getPoignee(x2,y2,d.getSize().width, 
				d.getSize().height, -a, -b);	
		x1 = p1.x ; y1 = p1.y;
		x2 = p2.x ; y2 = p2.y;

		g.drawLine((int)(x1*c), (int)(y1*c), (int)(x2*c), (int)(y2*c));
		if (pointe == true) dessinePointe(g, x1, y1, x2, y2, c);
		else g.fillRect((int)((x2-2)*c), (int)((y2-2)*c), (int)(4*c), (int)(4*c));
	}

	/** Calcule a quel endroit de la bordure doit aboutir le trait de
	  la fleche */
	static Point getPoignee(int cx, int cy, int w, int h, int a, int b){
		double dw = (double) w;
		double dh = (double) h;
		double da = (double) a;
		double db = (double) b;
		Point p = new Point(cx, cy);

		if ((a != 0) && ((Math.abs(db/da) < Math.abs(dh/dw)))) {
			p.x +=  (int) (da*dw/2.0/Math.abs(da));
			p.y +=  (int) (db*dw/2.0/Math.abs(da));
		}
		else 
			if (b!=0) {
				p.x +=  (int) (da*dh/2.0/Math.abs(db));
				p.y +=  (int) (db*dh/2.0/Math.abs(db));
			}
		return p;
	}

	/** dessine la pointe de la fleche, selon l'angle du trait. attention, rep�re indirect !*/
	private void dessinePointe(Graphics g, int o_x, int o_y, int d_x, int d_y, double c){
		int radius = 10;
		int demi_secteur = 30;
		int a ;
		double angle;

		if (d_x == o_x) {
			if (d_y > o_y) {
				angle = Math.PI/2;
			} else {
				angle = -Math.PI/2;
			}
		} else {
			angle = Math.PI + Math.atan(((double) (d_y - o_y)) / ((double)(o_x - d_x)));
		}

		if (o_x > d_x) {
			angle += Math.PI;
		}

		if (angle < 0) {
			angle += 2*Math.PI;
		}

		a = (int) ((angle / Math.PI)*180);

		g.fillArc((int)((d_x - radius)*c) , (int)((d_y - radius)*c), (int)(c*2*radius), (int)(c*2*radius),  
				a - demi_secteur, 2*demi_secteur);
	}

	public Sticker getSticker() {
		return null ;
	}

	/** Retourne la forme que doit prendre la fleche en fonction de son etat. */
	int getForme(){
		int forme = 0;
		switch (getAgreement()) {
			case UNANIMITY: 
				forme = OVALE;
				break;
			case ABSTENTION: 
				forme = OVALE ;
				break;
			case CONFLICT: 
				forme = LOSANGE ;
				break;
		}
		return forme;
	}

	/** Calcule les dimensions du cadre de l'etiquette en fonction de
	 * ses largeur et hauteur minimales
	 * @param largeur la largeur du nom de l'argument
	 */
	Dimension getLabelSize(int largeur, int hauteur){
//		Dimension dim ;
//		if (hauteur > largeur) dim = new Dimension(hauteur, hauteur);
//		else dim = new Dimension(largeur, largeur);
//		return dim;
		return new Dimension(largeur, hauteur);
	}
	
    	public void autoResize(){
    	}
}
