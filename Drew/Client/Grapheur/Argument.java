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
 * File: Argument.java
 * Author: Matthieu Quignard
 *
 * $Id: Argument.java,v 1.44.2.1 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;
import java.util.*;
/**
 * Classe g�n�rique des objets de l'argumentation. Un argument est
 * d�finit par un id, un label et une explication. Il poss�de �galement
 * la liste de ses supporters et de ses d�tracteurs. <p> C'est �galement
 * un objet graphique, pouvant �tre visible ou non, s�lectionn� ou non,
 * et poss�dant des coordonn�es et une taille.<p> Cette classe g�n�rique
 * g�re �galement les �v�nements souris qui ont lieu dessus. Toutes les
 * sous-classes auront donc le m�me comportement � la souris. On ne d�l�gue
 * aux sous-classes que ce qui les distingue : leur apparance principalement.
 */
public abstract class Argument
	extends Component {



	/** Params for automated Layout :
		<ul>
		<li>dx, dy : direction in which the element will be moved</li>
		<li>fixed : boolean to pin the element on the background</li>
		</ul>
	*/
	double dx,dy;
	boolean fixed;

	/** Constantes pour les �tats-types des arguments */
	public final static int POSITIVE_ORIENTATION	= 0 ;
	public final static int NEUTRAL_ORIENTATION 	= 1 ;
	public final static int NEGATIVE_ORIENTATION 	= 2 ;

	public final static int UNANIMITY 	= 0 ;
	public final static int ABSTENTION 	= 1 ;
	public final static int CONFLICT 	= 2 ;

	/** Constantes pour les formes possibles des arguments */
	final static int RECTANGLE	= 0 ;
	final static int TRAPEZE 	= 1 ;
	final static int ETOILE 	= 2 ;
	final static int OVALE 		= 3 ;
	final static int LOSANGE 	= 4 ;

	private static int theme ; // Choose between the different types of boxes
	private static int style ; // Choose the way boxes shrink
	
	/** Background Colors for Arguments */
	final static int selectedColor = 0xc0f0c0 ;	//Soft Green
	final static int linkedColor = 0xc8c8ff; 	//Soft Blue
	final static int unlinkedColor = 0xffc8c8; 	//Soft Red
	final static int unselectedColor = 0xffffff; 	//White

	int backgroundColor = unselectedColor;

	/** Une fonte pour tous les arguments */
	static Font plainFont = new Font("SansSerif", Font.PLAIN, 12); //was 12

	/** In an A --&gt; B --&gt; C relation, when you're on B, all the A elements. 
	 * In the case of an arrow it is called an Antecedent.
	 */
	protected Vector upstream ;

	/** In an A --&gt; B --&gt; C relation, when you're on B, all the C elements.
	 * In the case of an arrow it is called a Consequent.
	 */
	protected Vector downstream ;
	
	/** l'id de l'argument : Nom du createur + num d'index pour unicit�*/
	private String id;

	/** un champ de texte plus �tendue pour fournir un compl�ment d'information 
	*/
	private String comment;

	//mp 28/2/2002 TextArea
	/** composant permettant de faire des boites redimensionnables */
	// TextArea area;

	/** le graphe d'arguments auquel l'argument est reli� */
	Argumentation argumentation ;

	/** La liste des participants "pour" */
	private Vector pros;

	/** La liste des participants "contre" */
	private Vector cons;

	/** Type de conflit sur l'argument : conflit, consensus, unanimit� */
	private int conflict;

	/** Padding value for label drawing */
	private int PAD = 10 ;

	/** Si l'argument est s�lectionn� */
	private boolean selected = false;

	private boolean targetted = false;

	/** Si l'argument doit �tre redimensionn� */
	private static boolean resized = false;

	/** l'argument a ete redimensionnee, on en tient compte */
	private boolean isresized = false;

	/** temps �coul� pour double-clic */
	static private long t0=0;

	/** L'utilisateur qui est en train de modifier les champs */
	private User editeur;

	/** A tooltip attached to the argument */
	private Tips tTip = null ;

	/** The number of the argument (if it exists)
	 * This is usefull for Alex.
	 */
	private String number = null ;

	/** A little sticker where the string `number' above
	 * is displayed.
	 */
	protected Sticker gom = null ;

	/** to find out the longest line after wrapping.
	 */
	private int longestLine = 0 ;

	boolean valid = true ;


	/** Constructeur d'argument, appele par Boite ou Fleche.
	 * On cree un nouvel argument, rattache a une argumentation et 
	 * portant l'id voulu.
	 */
	public Argument(Argumentation argu, String id){

		this.id = id ;
		this.argumentation = argu ;

		setComment(argu.papa.comment.getString("CommentPrompt"));
		setEditeur(null);
		selected = true ;
		cons = new Vector(1,1);
		pros = new Vector(1,1);

		// Matthieu Quignard
		// Automatic placement of arguments in the Panel
		ScrollPane s = (ScrollPane) argu.getParent();
		Point focus = s.getScrollPosition();
		Dimension fenetre = s.getSize();
		// setLocation(100,100);
		setLocation(focus.x + fenetre.width/2, focus.y + fenetre.height/2);
		
		setEnabled(true);
		setVisible(true);
		//setName(id);
		super.setName(id); // to identify boxes
		setName(argu.papa.comment.getString("NewArgument"));

		//this.addMouseMotionListener(this);
		//this.addMouseListener(this);
	}

	public String toString() {
		return "Argument["+ id + "," + this.getName() + "," + getSize()  + ", " + getLocation() + "]";
	}

	/** Returns a Point which represents the center of the component relative to its parent. */
	public Point getCenterLocation() {
		return new Point(getLocation().x + getSize().width / 2, getLocation().y + getSize().height / 2) ;
	}

	/** Returns a Point which represents the center of the component relative to its coordinate system. */
	public Point getCenter() {
		return new Point(getSize().width / 2, getSize().height / 2) ;
	}

	/** Returns a Point which represents the center of the component in the screen space. */
	public Point getCenterOnScreen() {
		return new Point(getLocationOnScreen().x + getSize().width / 2, getLocationOnScreen().y + getSize().height / 2) ;
	}

	/** Set the location of a component relative to its center. */
	public void setCenterLocation(int x, int y) {
		setLocation(x - getSize().width / 2, y - getSize().height / 2) ;
	}

	/** Set the location of a component relative to its center. */
	public void setCenterLocation(Point p) {
		setCenterLocation(p.x, p.y) ;
	}

	
	/** returns true if argument is selected, false otherwise.
	  @see #selected
	  */
	boolean getSelected() {
		return selected ;
	}

	/** D�finir l'�tat s�lectionn� ou non de l'argument 
	  @see #selected
	  */
	void setSelected(boolean b) {
		selected = b ;
	}

	void setTargetted(boolean b) {
		targetted = b ;
	}

	/** obtenir son id 
	  @see #id
	  */
	String getId() {
		return id;
	}

	/** d�finir son id 
	  @see #id 
	  */
	void setId(String i) {
		id = i ;
	}

	String getCreator() {
	    String s = "???";
	    int i = id.indexOf(".");
	    if (i >= 0) s = id.substring(0,i);
	    return s;
	}

	
	void resize(){
		Rectangle r = getPreferredBounds();
		setBounds(r) ;
		repaint();
	}

	/** Indique � quelle argumentation cet argument est reli� 
	  @see #argumentation
	  */
	Argumentation getArgumentation() {
		return this.argumentation;
	}

	/** Pour sp�cifier � quelle argumentation cet argument est reli� 
	  @see #argumentation
	  */
	void setArgumentation(Argumentation a) {
		this.argumentation = a ;
	}

	/** Retourne la liste des utilisateurs "pour"
	  @see #pros
	  */
	public Vector getPros() {
		return pros;
	}

	/** Retourne la liste des utilisateurs "contre"
	  @see #cons
	  */
	public Vector getCons() {
		return cons;
	}

	/** Retourne le nombre d'utilisateurs "pour"
	  @see #pros
	  */
	public int getNbPros() {
		return pros.size();
		/*
		int i ;
		User u;
		return pros.size();
		int count = 0;
		if (!pros.isEmpty())
			for (i = 0 ; i < pros.size() ; i++){
				u = (User) pros.elementAt(i);
				if (u.getUserEngaged()) count++;
			}
		return count ;
		*/
	}

	/** Retourne le nombre d'utilisateurs "contre"
	  @see #cons
	  */
	public int getNbCons() {
		return cons.size();
		/*
		int i ;
		User u;
		int count = 0;
		if (!cons.isEmpty())
			for (i = 0 ; i < cons.size() ; i++){
				u = (User) cons.elementAt(i);        
				if (u.getUserEngaged()) count++;
			}
		return count ;
		*/
	}

	/** Retourne le nombre d'utilisateurs engages ni "pour" ni "contre"
	*/
	public int getNbNonCommitted() {
		int n = argumentation.protagonists.size();
		n -= getNbPros() ;
		n -= getNbCons() ;
		return n ;
	}

	/** Donner les infos suppl�mentaires sur l'argument.
	  @see #comment
	  */    
	public void setComment(String s) {
		comment = s ;
	}

	/** Obtenir les infos suppl�mentaires sur l'argument.
	  @see #comment
	  */
	public String getComment() {
		return comment ;
	}

	/** Retirer l'argument = le rendre inactif
	*/
	void remove() {
		setEnabled(false) ;
	}

	/** Reint�grer l'argument = le re-rendre actif
	*/
	void re_add() {
		setEnabled(true) ;
	}

	/**
	  M�thode pour ajouter un utilisateur u dans la liste <b>pros</b>
	  des supporteurs de l'argument. L'utilisateur sera de facto
	  retire de la liste des detracteurs (le cas echeant). 
	  */
	void support(User u) {
		if (!pros.contains(u)) pros.addElement(u);
		while (cons.contains(u)) cons.removeElement(u);
		isSupporter(u);
		updateConflict();
		resize();
	}

	/**
	  M�thode pour retirer un utilisateur u de la liste <b>pros</b>
	  des supporteurs de l'argument.
	  */
	void unsupport(User u) {
		while (pros.contains(u)) pros.removeElement(u);
		isSupporter(u);
		updateConflict();
		resize();
	}

	/**
	  M�thode pour ajouter un utilisateur u dans la liste <b>cons</b>
	  des d�tracteurs de l'argument. Si l'utilisateur figurait parmi
	  les supporteurs, il sera egalement retire de la liste <b>pros</b>.
	  */
	void challenge(User u) {
		if (!cons.contains(u)) {
			cons.addElement(u);
		}
		while (pros.contains(u)) {
			pros.removeElement(u);
		}
		isChallenger(u);
		updateConflict();
		resize();
	}

	/**
	  M�thode pour retirer un utilisateur u de la liste <b>cons</b> des
	  d�tracteurs de l'argument.  
	  */
	void unchallenge(User u) {
		while (cons.contains(u)) {
			cons.removeElement(u);
		}
		isChallenger(u);
		updateConflict();
		resize();
	}

	/** Teste si l'utilisateur u est un supporter de la
	  proposition. La m�thode retourne true si u figure dans pros, 
	  false sinon. */
	public boolean isSupporter(User u){
		if (pros.contains(u)) {
			return true;
		}
		else {
			return false;
		}	
	}

	/** Teste si l'utilisateur u est un challenger de la
	  proposition. La m�thode retourne true si u figure dans cons,
	  false sinon.
	  */
	public boolean isChallenger(User u){
		if (cons.contains(u)) {
			//System.out.println(u.getUserName() + " est un challenger");
			return true;
		}
		else {
			//System.out.println(u.getUserName() + " n'est pas un challenger");
			return false;
		}	
	}   

	/** Connaitre l'utilisateur qui est en train d'editer les champs */
	User getEditeur(){
		return editeur;
	}

	/** Mise a jour de l'utilisateur qui est en train d'editer les champs */
	void setEditeur(User u){
		this.editeur = u ;
	}

	/** Retourne la situation de conflit autour de la proposition en
	  �valuant les positions des diff�rents utilisateurs engag�s */
	void updateConflict(){
		conflict = 0 ;
		if (getNbPros() > 0) conflict+=4 ;
		if (getNbCons() > 0) conflict+=2 ;
		if (getNbNonCommitted() > 0) conflict+=1 ;
	}

	/** Evalue l'orientation g�n�rale des positions autour de l'argument.
	  Fonctionnalit� possible non exploit�e car un tel rendu surcharge
	  la compr�hension des codes de couleur, de forme etc.
	  */
	public int getOrientation(){
		if (conflict >= 4) return POSITIVE_ORIENTATION;
		else if (conflict >=2 ) return NEGATIVE_ORIENTATION;
		else return NEUTRAL_ORIENTATION;
	}

	/** renvoie le type d'accord autour de l'argument : conflit (deux positions
	  antagonistes, abstension (pas de conflit mais existence 
	  d'abstensionnistes) ou unanimit�.
	  */
	public int getAgreement(){
		if (conflict >= 6) return CONFLICT; // Pro AND Cons
		else 
			if ((conflict == 3) || (conflict == 5) ) 
				return ABSTENTION; // (Pro XOR Cons) AND non committed
			else return UNANIMITY; // (Pro XOR Cons) AND all committed
	}


	public void startToolTip(){
	    tTip = new Tips(argumentation, this) ;
	    tTip.start() ;	
	}


	public void stopToolTip(){
	    if ( tTip != null ) {
		tTip.stop() ;
		tTip = null ;
	    }
	}
	
	public String getNumber() {
		return number ;
	}

	public void setNumber(String n) {
		if ( n == null ) {
			return ;
		}
		
		/* X.S. 24 09 2002
		 * If I correctly understand what's behind Java, this
		 * is correct as the garbage collecting mecanism will
		 * take care of the memory which is perhaps already
		 * allocated.
		 */
		number = new String(n) ;
		
		if ( gom == null ) {
			gom = new Sticker(this, getLocation()) ;
		} else {
			getSticker().updateNumber(number) ;
		}
	}



	// METHODES GRAPHIQUES

	/** dessine un rectangle dans un certain cadre, avec un d�calage vers le centre, dans une couleur donn�e.
	  @param g l'espace graphique
	  @param x l'abscisse du cadre
	  @param y l'ordonn�e du coin haut/gauche du cadre
	  @param width la largeur du cadre (vers la droite)
	  @param height la hauteur du cadre (vers le bas)
	  @param offset le nb de pixel de d�calage vers le centre du cadre, pour permettre d'emboiter les rectangles
	  @param c la couleur du cadre
	  */
	private void dessineRectangle(Graphics g, int x, int y, int width, int height, int offset, int c, boolean bg){
		g.setColor(new Color(c));

		if( bg ) {
			g.fillRect(x + offset, y + offset, width - 2*offset, height - 2*offset);
		}
		else {
			g.drawRect(x + offset, y + offset, width - 2*offset, height - 2*offset);
		}
	}

	/** dessine une ellipse dans un certain cadre, avec un d�calage vers le centre, dans une couleur donn�e.
	  @param g l'espace graphique
	  @param x l'abscisse du cadre;

	  @param y l'ordonn�e du coin haut/gauche du cadre
	  @param width la largeur du cadre (vers la droite)
	  @param height la hauteur du cadre (vers le bas)
	  @param offset le nb de pixel de d�calage vers le centre du cadre, pour permettre d'emboiter les ellipses
	  @param c la couleur du cadre
	  */
	private void dessineOvale(Graphics g, int x, int y, int width, int height, int offset, int c, boolean bg){
		g.setColor(new Color(c));

		if( bg ) {
			g.fillOval(x + offset, y + offset, width - 2*offset, height - 2*offset);
		}
		else {
			g.drawOval(x + offset, y + offset, width - 2*offset, height - 2*offset);
		}
	}

	/** dessine un trapeze dans un certain cadre, avec un d�calage vers le centre, dans une couleur donn�e.
	  @param g l'espace graphique
	  @param x l'abscisse du cadre
	  @param y l'ordonn�e du coin haut/gauche du cadre
	  @param width la largeur du cadre (vers la droite)
	  @param height la hauteur du cadre (vers le bas)
	  @param offset le nb de pixel de d�calage vers le centre du cadre, pour permettre d'emboiter les trap�zes
	  @param c la couleur du cadre
	  */
	private void dessineTrapeze(Graphics g, int x, int y, int width, int
			height, int offset, int c, boolean bg){
		int numpoints = 4;
		int[] xpoints = new int[numpoints+1];
		int[] ypoints = new int[numpoints+1];
		xpoints[0] = x + offset           ; ypoints[0] = y + height - offset;
		xpoints[1] = x + height/2 + offset ; ypoints[1] = y + offset ;
		xpoints[2] = x + width - height/2 - offset ; ypoints[2] = y + offset  ;
		xpoints[3] = x + width - offset           ; ypoints[3] = y + height - offset ;
		xpoints[4] = x + offset                   ; ypoints[4] = y + height - offset ;

		g.setColor(new Color(c));
		if(bg) {
			g.fillPolygon(xpoints, ypoints, numpoints+1);
		}
		else {
			g.drawPolygon(xpoints, ypoints, numpoints+1);
		}
	}

	/** dessine une etoile dans un certain cadre, avec un d�calage vers le centre, dans une couleur donn�e.
	  @param g l'espace graphique
	  @param x l'abscisse du cadre
	  @param y l'ordonn�e du coin haut/gauche du cadre
	  @param width la largeur du cadre (vers la droite)
	  @param height la hauteur du cadre (vers le bas)
	  @param offset le nb de pixel de d�calage vers le centre du cadre, pour permettre d'emboiter les �toiles
	  @param c la couleur du cadre
	  */
	private void dessineEtoile(Graphics g, int x, int y, int width, int height, int offset, int c, boolean bg)
	{
		int numpoints = 8;
		int[] xpoints = new int[numpoints];
		int[] ypoints = new int[numpoints];
		int radius, topRightOffset, w, h, r ;
	
		
		radius = ((width > height) ? height : width) / 7 ;
		if ( radius < 10 ) { radius = 10 ; }
		else if ( radius > 30 ) { radius = 30 ; }

		/* X.S. 16 07 2002
		 * To be used with the smiley (not exactly perfect, but...)
		topRightOffset = offset + radius ;
		*/
		topRightOffset = offset ;
		w = width - offset - topRightOffset ;
		h = height - offset - topRightOffset ;
		r = radius / 2 ;

		xpoints[0] = x + offset ;		ypoints[0] = y + topRightOffset ;
		xpoints[1] = xpoints[0] + r ;		ypoints[1] = ypoints[0] + h / 2 ;
		xpoints[2] = xpoints[0] ;		ypoints[2] = ypoints[0] + h ;
		xpoints[3] = xpoints[0] + w / 2 ;	ypoints[3] = ypoints[2] - r ;
		xpoints[4] = xpoints[0] + w ;		ypoints[4] = ypoints[2] ;
		xpoints[5] = xpoints[4] - r ;		ypoints[5] = ypoints[1] ;
		xpoints[6] = xpoints[4] ;		ypoints[6] = ypoints[0] ;
		xpoints[7] = xpoints[3] ;		ypoints[7] = ypoints[0] + r ;

		g.setColor(new Color(c));
		if( bg ) {
			g.fillPolygon(xpoints, ypoints, numpoints);
		}	
		else {
			g.drawPolygon(xpoints, ypoints, numpoints);
		}

		// drawUnhappyFace(g, xpoints[6], ypoints[6], radius);
	}   

	/** dessine un losange dans un certain cadre, avec un d�calage vers le centre, dans une couleur donn�e.
	  @param g l'espace graphique
	  @param x l'abscisse du cadre
	  @param y l'ordonn�e du coin haut/gauche du cadre
	  @param width la largeur du cadre (vers la droite)
	  @param height la hauteur du cadre (vers le bas)
	  @param offset le nb de pixel de d�calage vers le centre du cadre, pour permettre d'emboiter les losanges
	  @param c la couleur du cadre
	  */
	private void dessineLosange(Graphics g, int x, int y, int width, int height, int offset, int c, boolean bg)
	{
		int numpoints = 4;
		int[] xpoints = new int[numpoints+1];
		int[] ypoints = new int[numpoints+1];


		xpoints[0] = x + offset			; ypoints[0] = y + height/2	;
		xpoints[1] = x + width/2		; ypoints[1] = y + offset	;
		xpoints[2] = x + width - offset         ; ypoints[2] = y + height/2		;
		xpoints[3] = x + width/2		; ypoints[3] = y + height - offset	;
		xpoints[4] = x + offset			; ypoints[4] = y + height/2	;

		g.setColor(new Color(c));

		if( bg ) {
			g.fillPolygon(xpoints, ypoints, numpoints+1);
		}
		else {
			g.drawPolygon(xpoints, ypoints, numpoints+1);
		}

		// drawUnhappyFace(g, xpoints[2], ypoints[1], ((width > height) ? height : width) / 6);
	}

	
	/** draws an unhappy smiley
	 * @param g the graphic context
	 * @param x abscissa of the center of the circle
	 * @param y ordinate of the center of the circle
	 * @param r the radius of the circle
	 * @param c color of the background of the smiley
	 */
	private void drawUnhappyFace(Graphics g, int x, int y, int r)
	{
		/* Drawing an oval is like drawing a box. How strange ! */
		int X, Y, R, d ;

		
		if ( r < 10 ) { r = 10 ; }
		else if ( r > 50 ) { r = 50 ; }

		X = x - r ;
		Y = y - r ;
		R = r + r ;

		/* The coloured face with only a border */
		g.setColor(Color.yellow) ;
		g.fillOval(X, Y, R, R) ;
		g.setColor(Color.black) ;
		g.drawOval(X, Y, R, R) ;
		
		/* the eyes */
		R = r / 7 ; /* X.S. : why ints ? */
		d = (int)(0.4 * (double)r) ;
		Y = y - d ;
		X = x - d ;
		g.fillOval(X-R, Y-R, 2*R, 2*R) ;
		X = x + d ;
		g.fillOval(X-R, Y-R, 2*R, 2*R) ;
		
		/* the mouth */
		R = 3 * r / 4 ;
		X = x - R ;
		Y = y + (r - R) ;
		R = 2 * R ;
		g.drawArc(X, Y, R, R, 45, 90) ;
		Y = Y - 1 ;
		g.drawArc(X, Y, R, R, 45, 90) ;
	}


	/** dessiner un contour pour l'argument */
	private void dessineContour(Graphics g, int forme, int x, int y, int width, int height, int offset, int c, boolean bg){
		if ( bg )  {
			if (targetted || argumentation.linkedArgs.contains(this)) {
				if (argumentation.getMouseMode() == argumentation.LINKMODE) c = linkedColor;
				else if (argumentation.getMouseMode() == argumentation.UNLINKMODE) c = unlinkedColor; 
			}
			else if (selected) c = selectedColor;
		}

		switch (forme) {
			case RECTANGLE: 
				// Utilise pour un argument sans contradictions
				dessineRectangle(g,x,y,width,height,offset,c,bg);
				break;
			case OVALE: 
				System.err.println("Largeur, hauteur = "+width + " "+height);
				// Utilise pour une liaison sans contradiction
				dessineOvale(g,x,y,width,height,offset,c,bg);
				break;
			case ETOILE: 
				// Utilise pour un argument avec contradiction
				dessineEtoile(g,x,y,width,height,offset,c,bg);
				break;
			case LOSANGE: 
				// Utilise pour une liaison avec contradiction
				dessineLosange(g,x,y,width,height,offset,c,bg);
				break;
			case TRAPEZE: 
				// Pas utilise a priori...
				dessineTrapeze(g,x,y,width,height,offset,c,bg);
				break;
		}
	}

	/** dessiner tous les contours de l'argument */
	private void dessineContours(Graphics g, int forme, int x, int y, int width, int height){
		User u;
		Vector liste;
		int index;
		int offset = -1;

		// fill the box
		dessineContour(g,forme,x,y,width,height,0,0x00ffffff, true);

		liste = pros;
		if (!liste.isEmpty())
			for (index = liste.size() - 1; index >= 0 ; index--) {
				u = (User) liste.elementAt(index);
			//	if (u.getUserEngaged()) {
					offset +=2;
					dessineContour(g,forme,x,y,width,height,offset,u.getUserRGBColor(), false);
			//	}
			}

		liste = cons;
		if (!liste.isEmpty())
			for (index = liste.size() - 1; index >= 0 ; index--) {
				u = (User) liste.elementAt(index);
			//	if (u.getUserEngaged()) {
					offset +=2;
					dessineContour(g,forme,x,y,width,height,offset,u.getUserRGBColor(), false);

			//	}
			}    
		
		if (offset < 0) {
			offset = 1;
			dessineContour(g,forme,x,y,width,height,offset,Color.gray.getRGB(), false);
		}
	}

	abstract String printType();

	public int printDetails(Graphics g, int num, int line, int col) {
                String s;
                FontMetrics fm = g.getFontMetrics( g.getFont() );
                int hauteur = fm.getHeight();                  
		int l = line;
		int nl;

                //Print ID
                s = Integer.toString(num);
                g.drawString(s, col, l);                    

                //Print Type
                s = printType();            
                g.drawString(s, col + 20, l);

		l += hauteur + fm.getLeading();;
                //Print Name
		// {"print.argument.name", "Name : {0}" },
                s = argumentation.papa.comment.format( "print.argument.name", getName() );
                nl = printString(g, l, col+20, 400, s);

		l += nl*hauteur + fm.getLeading();
                //Print Comment
		// {"print.argument.comment", "Comment : {0}" },
                s = argumentation.papa.comment.format( "print.argument.comment", comment );
		nl = printString(g, l, col+20, 400, s);

		l += nl*hauteur + fm.getLeading();
                //Print Creator's Name
                s = argumentation.papa.comment.format( "print.argument.creator", getCreator());
                nl = printString(g, l, col+20, 400, s);

		l += nl*hauteur + fm.getLeading();
                //Print For
                s = argumentation.papa.comment.format( "MILabelPro" );
		s = s.concat(" : ");
		s = s.concat(argumentation.printUsers(getPros()));
                nl = printString(g, l, col+20, 400, s);

		l += nl*hauteur + fm.getLeading();
                //Print Against
                s = argumentation.papa.comment.format( "MILabelContra" );
		s = s.concat(" : ");
		s = s.concat(argumentation.printUsers(getCons()));
                nl = printString(g, l, col+20, 400, s);

		return l + nl*hauteur + hauteur;
        }

	/** Print a given text (String s) on a Graphics context (g) starting at a given point (col c, line l)
	 *  within a specified horizontal space (length)
	 *  Returns the number of lines into which the text has been broken down.
	 */
	public static int printString(Graphics g, int line, int col, int length, String s) {
                FontMetrics fm = g.getFontMetrics( g.getFont() );
                String nameCopy = "Ceci est une merveilleuse nouvelle boite avec plein des trucs tres long."; 
                Vector v = new Vector();
                int hauteur = fm.getHeight();
                int largeur = fm.stringWidth(nameCopy);
                float l_c = (float)largeur / nameCopy.length(); // mean size per char
                int nc = Math.max(1,(int)(length / l_c )); // nb of char per line, NOTE: no less than one ;-)
                int nameCopyLength;     // length of the whole comment
                int sp ;        // index of the last space char ins a substring
                int br ;        // index of a carriage return
                int nl ;        // number of lines to display

                try {
                        StringTokenizer st = new StringTokenizer(s,"\n");
                        while (st.hasMoreTokens()) {
                                nameCopy = st.nextToken();
                                nameCopyLength = nameCopy.length();

                                int i = 0, e = 0;
                                while(i < nameCopyLength) {
                                        if ( (i + nc) >= nameCopyLength ) {     // Here the whole sub-sentence fits on one line
                                                e = nameCopyLength ;
                                                sp = 0 ;
                                        } else {        // Need to split. Search for the last space char in the substring.
                                                e = i + nc ;
                                                sp = nameCopy.lastIndexOf(' ', e) ;
                                        }

                                        if ( sp > i ) {
                                                e = sp ;
                                        }

                                        if( nameCopy.charAt(i) == ' ' ) i++;
                                        String sub = nameCopy.substring(i, e) ;
                                        v.addElement(sub) ;
                                        i = e;
                                }
                        }
                }
                catch( NoSuchElementException eee ) {
                }

                nl = v.size();                          // number of lines to display

		int l = line;
                for( int i = 0 ; i < nl ; i++ ) {
			g.drawString((String)v.elementAt(i), col, l);
			l += hauteur;
                }

		return nl;
	}

	/** retrieves the Sticker attached to this argument.
	 * This only works for boxes.
	 */
	abstract Sticker getSticker() ;

	/** Retourne la forme de l'argument selon... */
	abstract int getForme();

	/** Calcule les dimensions du cadre de l'etiquette
	 * @param largeur la largeur du nom de l'argument
	 * @param hauteur la hauteur du nom de l'argument
	 */
	abstract Dimension getLabelSize(int largeur, int hauteur);

	/** Les arguments ont besoin de pouvoir �tre mis en �vidence par une
	  police particuli�re. Celle-ci peut �tre diff�rente selon les 
	  sous-classes. 
	  */
	abstract Font getHighlightFont();

	/** La police standard peut �tre diff�rente selon les sous-classes.
	  Par exemple, une taille un peu plus grande pour les fleches */
	abstract Font getPlainFont();

	private void dessineLabel(Graphics g, String name, int cx, int cy){
		dessineLabel(g,name,cx,cy,1.);
	}

	/** Dessine le label dans la fonte specifique a Boite/Fleche */
	private void dessineLabel(Graphics g, String name, int cx, int cy, double c){
		g.setColor(Color.black) ;
		Font f ;
		if (selected) f = getHighlightFont();
		else f = getPlainFont();
		g.setFont( new Font( f.getName(), f.getStyle(), (int) (f.getSize()*c) ));

		FontMetrics fm = g.getFontMetrics( g.getFont() );
		String currentLine;
		String nameCopy = "Ceci est une merveilleuse nouvelle boite avec plein des trucs tres long."; //"Jumping brown lazy fox." ; // just to calculate the char width
		Vector v = new Vector();
		int hauteur = fm.getAscent() - fm.getDescent();
		int largeur = fm.stringWidth(nameCopy);
		float l_c = (float)largeur / nameCopy.length();	// mean size per char
		int nc = Math.max(1,(int)((getSize().width-15)*c / l_c )); // nb of char per line, NOTE: no less than one ;-) 
		int nameCopyLength;	// length of the whole comment
		int sp ;	// index of the last space char ins a substring
		int br ;	// index of a carriage return
		int nl ;	// number of lines to display
		int totalHeight ;	// the height of the text to display
		boolean tooLong = false ;
		boolean centered = true ;
		int largeurUtile ;
		int hauteurUtile ;

		if (getAgreement()>0) {
			largeurUtile=(int) ((getSize().width  - 30 )*c);
			hauteurUtile=(int) ((getSize().height - 10 )*c);
		} else {
                        largeurUtile=(int) ((getSize().width  - 15 )*c);
                        hauteurUtile=(int) ((getSize().height - 5  )*c);
		}

//System.err.println("DessineLabel : l_c : " + l_c + ", nc : " + nc + ", sz " + getSize().width + ", strw " + largeur);

		
 		try {
			StringTokenizer st = new StringTokenizer(name,"\n");
			while (st.hasMoreTokens()) {
				nameCopy = st.nextToken();
				nameCopyLength = nameCopy.length();
//System.err.println("DessineLabel : " + nameCopy + " length " + nameCopyLength);

				int i = 0, e = 0;
				while (i < nameCopyLength) {
					if (nameCopy.charAt(i) == ' ' ) i++;
					String sub = nameCopy.substring(i, nameCopyLength );
					//if ( (i + nc) >= nameCopyLength ) {	// Here the whole sub-sentence fits on one line
						/* MQ Le test peut etre plus exact */
					if (fm.stringWidth(sub) < largeurUtile ) {
						e = nameCopyLength ;
						sp = 0 ;
						// 0 <= i <= e = length
					} else {	// Need to split. Search for the last space char in the substring.
						//On raccourcit la fenetre de recherche a droite de i
						e = i + nc ; 
						if (e > nameCopyLength) e = nameCopyLength;
						sp = nameCopy.lastIndexOf(' ', e) ; // sp = -1 ou une position < nameCopyLength
						if (sp > i) e = sp;
						//else on garde la valeur de e
						centered = false;
					}

					sub = nameCopy.substring(i, e) ;
					v.addElement(sub) ;
					i = e;
				}
			}
		}
		catch( NoSuchElementException eee ) {
		}

		nl = v.size();				// number of lines to display
		totalHeight = nl * 2 * hauteur ;	// total height of the lines
		
		if ( totalHeight > hauteurUtile ) {
			// Too long, so begin just under the top of the box
			cy = (int) (3 * hauteur / 2./c) ;
			tooLong = true ;
		} else {
			// Not too long, so center the text vertically
			cy = cy - (int) (( totalHeight -3*hauteur) / 2. / c ) ;
		}
		
		longestLine = cx ;
		for( int i = 0 ; i < nl ; i++ ) {
			int stringWidth ;
			
			currentLine = (String)v.elementAt(i);
			stringWidth = fm.stringWidth(currentLine) ;
			// System.err.println(currentLine + " : string = " + stringWidth + " longest = " + longestLine) ;
			/* J'enleve ce qui suit, car cela ne sert a rien. Utile pour autoresize 
			if ( stringWidth > longestLine ) {
				longestLine = stringWidth ;
			}
			*/
			if ( centered ) {
				g.drawString(currentLine, (int)(cx*c - stringWidth/2.), (int)(cy*c));
			}
			else {
				g.drawString(currentLine, 
					(int)(cx*c - largeurUtile/2.), 
					(int)(cy*c)
				); //bad, add padding
			}
			cy += (2*hauteur);
		}

		/**
		 * If the text is too long to fit in the box, draw a little
		 * arrow on the right down corner of the argument to notify
		 * the user.
		 */
		if ( this instanceof Boite ) {
			if ( tooLong ) {
				Polygon more = new Polygon() ;

				more.addPoint(0, 0) ;
				more.addPoint((int)(c*7), 0) ;
				more.addPoint((int)(c*7), (int)(c*7)) ;
				more.addPoint(0, (int)(c*7)) ;

				more.translate((int)((getSize().width - 7)*c) , (int)((getSize().height - 7)*c)) ;
				g.setColor(Color.red) ;
				g.fillPolygon(more) ;
				g.setColor(Color.black) ;
			}

			// Auto resizing does not work pretty well at that time...
			// So sad. I'll try to improve it.
			//setSize(longestLine, getSize().height) ;
		}
	}

	/** Calcule la taille optimale du composant en fonction des
	 * dimensions de l'etiquette et de sa forme.
	 */
	private Rectangle getPreferredBounds(){
		Rectangle rect = new Rectangle(getBounds());
		Dimension dim = getPreferredSize();

		rect.x = getLocation().x + (rect.width  - dim.width )/2;
		rect.y = getLocation().y + (rect.height - dim.height)/2;
		rect.width = dim.width;
		rect.height = dim.height;
		return rect;
	}

	boolean getResized() {
		return isresized;
	}

	void setResized( boolean val ) {
		isresized = val;
	}

	public Dimension getPreferredSize(){

		if( getResized() == true ) return getSize();

		Dimension d = new Dimension(10,10);
		int offset = 2*(getNbPros()+getNbCons()) -1 ;
		if (offset < 0) offset = 1;

		Font f = getPlainFont();

		if (f != null) {
			int forme = getForme();
			FontMetrics fm = getFontMetrics(f);
			d.height = 2*(fm.getAscent() + fm.getDescent());
			if (forme == 2) d.height /= 2;
			//int str_w = fm.stringWidth(getName());
			int str_l = getName().length();
			if( str_l == 0 ) str_l = 1;
			int str_w = fm.stringWidth("+-?") * str_l / 3;
			int c_w = Math.max(1,str_w)/str_l;
			d.width = str_w + Math.max(PAD,d.height/2)  + 2*c_w ; //see dessineLabel

// boite 18/9 ??

			if( d.width > 5*d.height ) {
				double k = Math.sqrt( d.width / (2*d.height) );
				d.height = (int)(k * d.height) ;
				d.width  = (int)(d.width / k);
			}

			Dimension dim = new Dimension(getLabelSize(d.width, d.height));

			// On ajuste en fonction du nombre de traits a dessiner autour
			// Si c'est des etoiles, c'est plus complique
			if (forme == 2) {
				//rect.width  += rect.height + 4*offset;
				//rect.height += rect.height + 4*offset;
				dim.width  += dim.height/2 + 3*offset;
				dim.height += dim.height/2 + 3*offset;
			}
			else {
				dim.width  += 2*offset;
				dim.height += 2*offset;
			}
			return dim;
		} 
		else return d;
	}

	public Dimension getMinimumSize(){
		return new Dimension(10,10);
	}

	/** Dessiner l'argument c'est pareil que le mettre � jour */
	public void update(Graphics g) {
		paint(g);	
	}

	public void paint(Graphics g) {
		paint(g, 1.);
	}

	public void paint(Graphics g, double c) {
		// On prend les dimensions, on ajuste puis on dessine dans un tampon.
		// On dessine enfin le tampon dans la fenetre

		// On prend les dimensions en fonction des attributs de l'argument
		Dimension dim = getSize();

		// On dessine : le fond, le contour puis le nom

		// Le fond est rectangulaire a cause du point sous la souris qui peut aller dans les coins.
		//g.setColor(getBackground());
		//g.fillRect(0,0,(int)(dim.width*c), (int)(dim.height*c));
		// Les contours
		dessineContours(g, getForme(), 0, 0, (int)(dim.width*c), (int)(dim.height*c));

		//Dessin du label au centre du cadre

		Point center = new Point(dim.width/2, dim.height/2);
		g.setColor(Color.black);
		dessineLabel(g, getName(), center.x, center.y, c);

		//drawPointer(g);
	}

    /** automatic resize the argument box/node */
    public abstract void autoResize();
}

/* vim:tabstop=8 nowrap:
*/
