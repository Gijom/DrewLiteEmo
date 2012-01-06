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
 * File: Argumentation.java
 * Author: Matthieu Quignard
 *
 * $Id: Argumentation.java,v 1.51.2.1 2007/07/06 15:46:22 collins Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import Drew.Util.XMLmp.*;

/**
Espace graphique pour la construction d'argumentaires.
 H�rite de la classe <b>Panel</b> � laquelle on ajoute une
 structure de donn�es pour l'enregistrement des objets de l'argumentation,
 la liste des protagonistes etc.
 */
public class Argumentation
extends Panel // On peut dessiner sur un Container et en plus ajouter des composants
implements MouseListener, MouseMotionListener, ActionListener, Runnable, KeyListener {

    private boolean debug = Drew.Client.Util.Config.getDebug() ;
    boolean automaticLayout = false;

    int nnodes = 0;
    Vector nodes;

    int nedges=0;
    Edge edges[];

    public final static int NOTHING_TO_CHOOSE = 0;
    public final static int CHOOSE_ANTECEDENT = 1;
    public final static int CHOOSE_CONSEQUENT = 2;

    private static int nbarg = 0;
    private static int seed = (int)(System.currentTimeMillis() % 1000);

    /** Liste (classe Vector) des bo�tes cr��es.  */
    private Vector propositions ;

    /** Liste (classe Vector) des fl�ches cr��es.  */
    private Vector relations ;

    /** Taille originelle de l'espace graphique.
	Cette dimension peut �tre modifi�e en redimensionnant la fen�tre.
	*/
    Dimension minSize; 	// Taille de la fenetre argumentation

    /** R�f�rence � la fen�tre parente : Grapheur (h�rite de <b>Frame</b>).
	@see Grapheur
	*/
    Grapheur papa ; 	// Fenetre parente

    /** Fenetre d'information sur l'argument courant */
    MoreInfo miDialog;

    /** Structure de liste (de classe Vector) stockant les utilisateurs enregistr�s.  */
    Vector protagonists ; 	// Liste des utilisateurs engag�s

    /** Position du dernier click pour dragging (MousePressed) */
    private Point firstClick = new Point(0,0);

    private boolean dragging = false;

    /** Tampon pour double-buffering pour un rendu plus souple des modifications graphiques */
    private Image tampon ;

    /** L'argument courant selectionne */
    private Argument currentArgument = null ;

    private XMLTree state;

    /** the target element for all interactions */
    private Argument currentTarget = null ;

    /** Flag pour savoir si on doit designer l'antecedent/consequent d'une fleche */
    private int chooseArg = 0;

    /** Variable qui va recevoir l'argument d�sign�. Indispensable pour g�r�r les
	ant�c�dents et cons�quents des fl�ches.*/
    private Argument chosenArg = null;

    /** Liste des composants que l'on doit relier par des fleches  */
    // 20011028 MQ modif pour gestion shiftclik
    //private Vector linkedArgs;
    Vector linkedArgs;

    /** The highest number already used to number boxes when used with Alex */
    private int lastNumber = 0 ;

    Point mousePosition;

    /**
	Constructeur de l'objet Argumentation, en lien avec sa fen�tre parente
     (Grapheur sous-classe de <b>Frame</b>).
     Le nouvel objet Argumentation est instanci� avec :
     <UL>
     <LI> last_id == 0 </LI>
     <LI> propositions : liste vide </LI>
     <LI> relations : liste vide </LI>
     <LI> protagonists : liste vide </LI>
     <LI> taille : 900 x 900 </LI>
     </UL>
     */
    public Argumentation(Grapheur parent, Dimension sz) {
	//System.out.println("*** Argumentation init start");
	propositions = new Vector(1,1) ;
	relations = new Vector(1,1) ;
	protagonists = new Vector(1,1) ;
	linkedArgs = new Vector(2) ;

	//mp 29/01/2002 taille augment�e pour v�rifier
 //que les scrollbars fonctionnent.

	//PJ 20030528, add config for graphic area size
 //minSize = new Dimension(900,900);
	minSize = sz;
	this.setSize(minSize);

	this.papa = parent;
	this.addMouseListener(this);
	addMouseMotionListener(this);
	this.addKeyListener(this);
	//this.addFocusListener(this);
	setBackground(new Color(240,240,240));
	this.setLayout(null);
	tampon = createImage(getSize().width, getSize().height);
    }

    //PJ 20011025 add getState
    public void clear() {
	propositions.removeAllElements();
	relations.removeAllElements();
	linkedArgs.removeAllElements();
	protagonists.removeAllElements();

	removeAll();
	repaint();
    }

    //PJ 20030515
    boolean isInteractive() {
	return papa.isInteractive();
    }

    public Dimension getPreferredSize() {
	return minSize;
    }

    public synchronized Dimension getMinimumSize(){
	return minSize;
    }

    /** Ajouter une bo�te dans le graphe.
	La bo�te doit avoir �t� instanci�e avant. Cette m�thode ne fait que lui attribuer
	un num�ro et l'enregistrer dans le graphe.*/
    private synchronized void addProposition(Boite b) {
	//System.out.println("*** Argumentation addProp start");

	if (!propositions.contains(b)) {
	    propositions.addElement(b);
	    this.add(b,0);
	    b.setArgumentation(this);
	    //repaint();
	}

	//System.out.println("*** Argumentation addProp stop");

    }

    /** Used with Alex interactions.
	*
	* Given a number (the number of the argument in Alex templates),
	* this function returns the <code>id</code> of the box in the grapher.
	*
	* @param n the number of the box in Alex templates.
	* @return the <code>id</code> of the box number <code>n</code>.
	* @see #id
	* @see #Boite
	* @see #Argument
	*/
    public String getPropositionIdByNumber(String n) throws ArgNotFoundException {
	int i ;
	Boite b ;

	for ( i = 0 ; i < propositions.size() ; i++ ) {
	    b = (Boite) propositions.elementAt(i) ;
	    if ( n.compareTo(b.getNumber()) == 0 ) {
		return b.getId() ;
	    }
	}

	System.out.println("ERROR: No such Argument (Number = " + n + ")") ;
	throw new ArgNotFoundException("Arg not found Number = " + n + ")") ;
    }


    /** Ajouter une fl�che dans le graphe.
	La fl�che doit avoir �t� instanci�e avant. Cette m�thode ne fait que lui attribuer
	un num�ro et l'enregistrer dans le graphe.*/
    private synchronized void addRelation(Fleche f) {
	if (!relations.contains(f)) {
	    relations.addElement(f);
	    this.add(f,0);
	    f.setArgumentation(this);
	}
    }


    /** Mise � jour de la liste des utilisateurs engag�s, appelee par Grapheur
	*/
    void updateProtagonists(){
	int i;
	User u;

	Vector v = papa.connectedUsers;

	//Initialisation, remise � z�ro des listes
	if (!protagonists.isEmpty()) protagonists.removeAllElements();

	//Remplissage des listes avec des utilisateurs engag�s
 //On prend tous les utilisateurs, engages ou non pour garder trace de leurs opinions
 //On ne prend pas le premier (0 = nobody)
	for (i=1 ; i< v.size() ; i++) {
	    u = (User) v.elementAt(i);
	    //if (u.getUserEngaged()) protagonists.addElement(u);
	    protagonists.addElement(u);
	}
    }

    /** Retourne la liste des utilisateurs engag�s.
	@see #protagonists
	*/
    public Vector getProtagonists() {
	return protagonists;
    }

    public User getCurrentUser(){
	return papa.getCurrentUser();
    }

    public String getRealUserName(){
	return papa.getRealUserName();
    }

    public Argument getCurrentTarget(){
	return currentTarget;
    }

    public void setCurrentTarget( Argument ca ){
	if (ca != currentTarget) {
	    if (currentTarget != null) {
		currentTarget.setTargetted(false);
		currentTarget.repaint();
	    }
	    if (ca != null) {
		ca.setTargetted(true);
		ca.repaint();
	    }
	    currentTarget = ca;
	}
    }

    public Argument getCurrentArgument(){
	return currentArgument;
    }

    /** Retourne la liste (Vector) des propositions (Boites) pr�sentes dans
	*  l'argumentation.
	*/
    public Vector getPropositions(){
	return propositions;
    }

    /** Retourne la liste (Vector) des relations (Fleches) pr�sentes dans
	*  l'argumentation.
	*/
    public Vector getRelations(){
	return relations;
    }

    /** Retourne l'argument s�lectionn�. NB : cela suppose bien sur
	qu'un seul argument ne peut etre selectionn� � la fois !!!  */
    public void setCurrentArgument(Argument ca) {
	//System.out.println("*** Argumentation setCurrArg start");

	if (ca != currentArgument) {
	    if (currentArgument != null) {
		currentArgument.setSelected(false);
		currentArgument.repaint();
	    }
	    if (ca != null) {
		ca.setSelected(true);
		ca.repaint();
	    }
	    this.currentArgument = ca;
	    papa.updatePanelArg();
	}
	//System.out.println("*** Argumentation setCurrArg stop");
    }

    /** redimensionner provoquer la mise a zero du tampon */
    public void invalidate(){
	super.invalidate();
	tampon = null;
    }


    /** Redessine l'espace graphique en prenant en compte l'�ventuel
	redimensionnement de la fen�tre.
	On dessine les bo�tes non-effac�es, puis les fl�ches.
	*/
    public void update(Graphics g) {
	paint(g);
    }

    public Image getTampon() {
	if (tampon == null) repaint();
	return tampon;
    }

    // Ajout MQ 20011028, getion de shiftclik
    /** Retourne le composant situ� � ces coordonn�es. Renvoie null sinon */
    public Argument getBoxAt(int x, int y) {
	Argument a = null;
	int ncomponents = getComponentCount();
	Component component[] = getComponents();

	for ( int i = 0  ; i < ncomponents ; i++) {
	    Component comp = component[i];
	    if (comp != null &&
	 comp.isVisible() == true &&
	 comp.getBounds().contains(x,y)) {
		a = (Argument) comp;
		break;
	    }
	}
	return a;
    }


    public int getComponentNumber(Component c) {
	if (c == null) return -1;

	Component component[] = getComponents();
	int ncomponents = getComponentCount();
	for (int i = 0  ; i < ncomponents ; i++) {
	    Component comp = component[i];
	    if (comp.equals(c)) return i;
	}
	return -1;
    }

    /** Reecriture d'update pour implementer le double-buffering
	*/
    public void paint(Graphics g) {
	if ( debug ) {
	    System.out.println("*** Argumentation update start");
	}

	int i;
	Dimension d = getSize();

	if (tampon == null) {
	    tampon = createImage(d.width, d.height);
	}

	Graphics tamponGraph = tampon.getGraphics();
	tamponGraph.setClip(0, 0, d.width, d.height);

	//Effacer l'image precedente
	tamponGraph.setColor(this.getBackground()); //On prend la couleur du fond
	tamponGraph.fillRect(0,0,d.width,d.height); //On efface tout
	tamponGraph.setColor(Color.gray); //On prend un crayon gris
	tamponGraph.draw3DRect(0,0,d.width,d.height,false); // le cadre

	int ncomponents = getComponentCount();
	Component component[] = getComponents();
	Rectangle clip = tamponGraph.getClipBounds();
	for ( i = ncomponents -1  ; i>= 0 ; i--) {
	    Component comp = component[i];
	    if (comp != null &&
	 //comp.getPeer() instanceof java.awt.peer.LightweightPeer &&
	 comp.isVisible() == true) {
		if (comp instanceof Fleche) {
		    Fleche f = (Fleche) comp;
		    f.dessinerLesFleches(tamponGraph);
		}

		Rectangle cr = comp.getBounds();
		if ((clip == null) || cr.intersects(clip)) {
		    Graphics cg = tamponGraph.create(cr.x, cr.y, cr.width, cr.height);
		    cg.setFont(comp.getFont());
		    try {
			comp.paint(cg);
		    } finally {
			cg.dispose();
		    }
		}
	    }
	}

	if ((dragging) && (mousePosition != null) && (currentMouseMode != MOVEMODE)) {
	    if (currentMouseMode == LINKMODE) tamponGraph.setColor(Color.blue);
	    else tamponGraph.setColor(Color.red);
	    tamponGraph.drawLine(currentArgument.getCenterLocation().x, currentArgument.getCenterLocation().y,
		mousePosition.x, mousePosition.y);
	}
	/*
	 ca arrive au d�but, la premiere fois ?
	 java.lang.NullPointerException: trying to call virtual method
	 at sun.awt.motif.X11Graphics.drawImage(X11Graphics.java:275)
	 at Drew.Client.Grapheur.Argumentation.paint(Argumentation.java:279)
	 at java.awt.Component.dispatchEventImpl(Component.java:1734)
	 */
	//affichage du tampon
	if ( g != null && tampon != null ) {
	    g.drawImage(tampon, 0, 0, null) ;

	    /*
	     papa.miniGraph.getGraphics().drawImage(
					     tampon,
					     0, 0, papa.miniGraph.getSize().width, papa.miniGraph.getSize().height,
					     0, 0, getSize().width, getSize().height,
					     null) ;

	     papa.miniGraph.setSize(getSize().width / 10, getSize().width / 10) ;
	     */

	    tamponGraph.dispose() ;
	}

	if ( debug ) {
	    System.out.println("*** Argumentation update stop");
	}
    }

    /**
	* Usefull for printing
     */
    public void directPaint(Graphics g) {
	directPaint(g, 1.);
    }

    public void directPaint(Graphics g, double scale) {
	double c = scale;
	if ( debug ) System.out.println("*** Argumentation directPaint start");

	int ncomponents = getComponentCount();
	Component component[] = getComponents();
	int margin = 50;

	if ( g instanceof PrintGraphics ) {
	    if (debug) System.out.println("CA commence");
	    Dimension d = ((PrintGraphics)g).getPrintJob().getPageDimension();
	    g.setColor(this.getBackground()); //On prend la couleur du fond
	    g.fillRect(0,0,d.width,d.height); //On efface tout

	    //print the username at the bottom line
	    int PAD = 20;
	    g.setColor(Color.black) ;
	    String u = getRealUserName();
	    if( u != null ) {
		Font plainFont = new Font("SansSerif", Font.PLAIN, 10); //was 12
		g.setFont( plainFont );
		g.drawString(u, PAD, d.height - PAD);
	    }

	    // remove "margin" from the both side of the printable destination
	    d = new Dimension( d.width - 2*margin, d.height-2*margin);
	    if( debug ) {
		g.setColor(Color.red);
		g.drawRect(0+margin,0+margin,d.width,d.height);
	    }

	    Rectangle zone = null;
	    for (int i =  ncomponents - 1 ; i >= 0 ; i--) {
		Component comp = component[i];
		if ( (comp != null) && (comp.isVisible()) )
		    if (zone == null) zone = comp.getBounds();
		    else zone = comp.getBounds().union(zone);
	    }
	    if (debug) System.out.println("Zone : "+zone);
	    if (debug) System.out.println("Dim : "+d);

	    if (zone != null) {
		int w = (int) (c * zone.width);
		if (w > d.width) c = (double)d.width/(double)zone.width;
		int h = (int) (c * zone.height);
		if (h > d.height) c = (double)d.height/(double)zone.height;
		if (debug) System.out.println("Scale = "+c);
	    }

	}
	g.translate( margin, margin );

	for (int i = ncomponents -1  ; i>= 0 ; i--) {
	    Component comp = component[i];
	    if (comp != null && comp.isVisible() == true) {
		if (comp instanceof Fleche) {
		    Fleche f = (Fleche) comp;
		    f.dessinerLesFleches(g,c);
		}

		Rectangle cr = comp.getBounds();
		Graphics cg = g.create( (int)(cr.x*c), (int)(cr.y*c),
			  (int)(cr.width*c), (int)(cr.height*c));

		Font f = new Font(
		    comp.getFont().getName(),
		    comp.getFont().getStyle(),
		    (int)(comp.getFont().getSize()*c)
		    );
		cg.setFont(f);

		try {
		    ((Argument)comp).paint(cg,c);
		} finally {
		    cg.dispose();
		}

		if ( g instanceof Graphics ) {
		    g.setFont(new Font("SansSerif", Font.PLAIN, (int) (10*c)));
		    g.drawString(Integer.toString(i),
		   (int) ( (cr.x + cr.width) *c),
		   (int) ( (cr.y + cr.height) *c));
		}
	    }
	}
	if ( debug ) System.out.println("*** Argumentation directPaint end");
    }

    public String printUsers(Vector v) {
	if (v.isEmpty()) return papa.comment.format("print.users.nobody");

	String s = "";
	for (int i=0 ; i < v.size(); i++) {
	    User u = (User) v.elementAt(i);
	    s = s.concat(u.getUserName());
	    if (i < v.size() -1) s = s.concat(", ");
	    else s = s.concat(".");
	}
	return s;
    }


    /**
	* Print Components details (id, name and comment) in a table form
     * MQ 3 nov 2003
     */
    public void printDetails(PrintJob pg) {
	int i;
	int l = 50; // Line index
	int c = 50; // Column index
	int newline = 50 ; //Forthcoming line index (next row)

	int ncomponents = getComponentCount();
	Component component[] = getComponents();
	Graphics g = pg.getGraphics();
	Font myfont = new Font("SansSerif", Font.PLAIN, 10);
	int hauteur = g.getFontMetrics().getHeight();

	for (i = 0; i < protagonists.size(); i++) {
	    l = newline;
	    User u = (User) protagonists.elementAt(i);
	    g.drawString(u.getUserName(), c, l);
	    newline += hauteur;
	}

	newline += 2*hauteur;

	Component comp; // Current component
		 //Print Boxes first
	for (i = 0 ; i < ncomponents ; ) {
	    l = newline;
	    comp = component[i];

	    if (comp != null && comp.isVisible() == true && (comp instanceof Boite)) {
		g.setFont(myfont);
		newline = ((Argument) comp).printDetails(g, i, l, c);
	    }

	    if (newline > 750) { //Ca depasse la page
		g.dispose();
		g = pg.getGraphics();
		newline = 50;
		// No increment : Details should be reprinted (may be not complete)
	    } else i++;
	}

	//Print Relations
	for (i = 0 ; i < ncomponents ; ) {
	    l = newline;
	    comp = component[i];

	    if (comp != null && comp.isVisible() == true && (comp instanceof Fleche)) {
		g.setFont(myfont);
		newline = ((Argument) comp).printDetails(g, i, l, c);
	    }

	    if (newline > 750) { //Ca depasse la page
		g.dispose();
		g = pg.getGraphics();
		newline = 50;
		// No increment : Details should be reprinted (may be not complete)
	    } else i++;
	}

	g.dispose();
    }


    /* Repaint all Components on a line between p1 and p2 */
    void repaintComponents(int p1x, int p1y, int p2x, int p2y) {

	int i;
	Dimension d = getSize();

	Graphics g = getGraphics();

	// Je fais des points de grosseur 10 autour de p1 et p2
	Rectangle p1 = new Rectangle(p1x - 5, p1y - 5, 10, 10);
	Rectangle p2 = new Rectangle(p2x - 5, p2y - 5, 10, 10);

	//Je calcule grosso-modo la zone sensible
	Rectangle cr2 = p1.union(p2);

	int ncomponents = getComponentCount();
	Component component[] = getComponents();
	Rectangle clip = g.getClipBounds();
	for ( i = ncomponents -1  ; i>= 0 ; i--) {
	    Component comp = component[i];
	    if (comp != null && comp.isVisible() == true) {
		Rectangle cr = comp.getBounds();

		//Je repeins les composants qui entrent dans cette zone
		if ((clip == null) || cr.intersects(clip))
		    if (cr.intersects(cr2)){
			Graphics cg = g.create(cr.x, cr.y, cr.width, cr.height);
			cg.setColor(this.getBackground()); //On prend la couleur du fond
			cg.fillRect(0,0,cr.width,cr.height);
			cg.setFont(comp.getFont());
			try {
			    comp.paint(cg);
			} finally {
			    cg.dispose();
			}
		    }
	    }
	}
    }


	public void actionPerformed(ActionEvent e) {
	    String cmd = e.getActionCommand();
	    performCommand(cmd);
	}

	// Florent Beauchamp : methode d'acces public, car besoin dans Graphe.java
	public String getUniqID() {
	    // ce n'est pas suffisant lorsque l'on reprend une session
	    nbarg++;
	    return getCurrentUser().getUserName() + "." + seed + "." + nbarg;
	}

	//PJ 20011026 gestion des id uniques
	private void rememberID( String id ) {
	}

	/** XS 26 09 2002
	    * Each box, when numbered, must have an unique number.
	    * Moreover, all the numbers have to be consecutive, so it
	    * may be easier to understand topics in Alex.
	    */
	private String getNextNumber() {
	    lastNumber ++ ;
	    return new Integer(lastNumber).toString() ;
	}

	/** Creer une nouvelle Boite */
	private void nouvelleBoite() {
	    String id = getUniqID();
	    //pj 20011026
     //nouvelleBoite(id);

	    //XML version pj 20021128
     //papa.avertir("nvboite",id);
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes("id", id, "action", "create", "type", "box"),
		   XMLTree.Contents()
		   )
	     );
	}

	private synchronized void nouvelleBoite(String id) {
	    Boite b = new Boite(this,id);
	    addProposition(b);
	    b.resize();
	    setCurrentArgument(b);
	}

	private synchronized void nouvelleBoite(String id, String number) {
	    Boite b = new Boite(this,id, number);
	    addProposition(b);
	    b.resize();
	    setCurrentArgument(b);
	}

	private void nouvelleFleche() throws ArgException {
	    String dest = "none"; //MQ 2002-11-11 , it used to be ""
	    String id = getUniqID();
	    //XML version pj 20021128
     //if (currentArgument != null) dest = currentArgument.getId();
     //else dest = "none";

	    //pj 20011026
     //nouvelleFleche(id,dest);

	    //XML version pj 20021128
     //papa.avertir("nvfleche",id,dest);
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes("id", id, "action", "create", "type", "arrow"),
		   XMLTree.Contents()
		   )
	     );
	}

	/** Creer une nouvelle Fleche  */

	private void nouvelleFleche(String id) throws ArgException {
	    nouvelleFleche( id, "none" );
	}

	private void nouvelleFleche(String id, String dest) throws ArgException {
	    Fleche f = new Fleche(this,id);
	    f.setName(papa.comment.getString("SymRelation0"));
	    f.resize();
	    if (!dest.equals("none")) {
		Argument destArg = getArgument(dest);
		f.addConsequent(destArg);
	    }
	    addRelation(f);
	    setCurrentArgument(f);
	}

	/** Supprimer l'argument courant */
	private void deleteCurrentArgument() throws ArgException {
	    if( currentArgument == null ) return;
	    String id = currentArgument.getId();
	    //XML version pj 20021128
     //papa.avertir("delete",id);
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes( "id", id, "action", "delete" ),
		   XMLTree.Contents()
		   )
	     );
	    //pj 20011026
     //deleteArgument(id);
	}

	/** Supprimer l'argument i */
	private void deleteArgument(String id) throws ArgException {
	    Argument a = getArgument(id);

	    //PJ, lors d'un rejouage, ya qq chose de null ???
	    if (a != null) {
		if ((currentArgument != null) && (currentArgument.equals(a))) {
		    setCurrentArgument(null) ;
		}
		a.remove();	//devient disabled;
		if (a instanceof Boite) propositions.removeElement(a);
		else relations.removeElement(a);
		remove(a);
		invalidate();
		repaint();
	    }
	}

	/** repeindre l'argument courant */
	void repaintCurrentArgument() {
	    //System.out.println("*** Argumentation repaintCurrArg start");

	    if (currentArgument != null) {
		currentArgument.repaint();
	    }
	    //System.out.println("*** Argumentation repaintCurrArg stop");

	}

	/** Tout Effacer */
	private void toutEffacer() {
	    //UTILISER Boite/Fleche.remove !!!
     //propositions.removeAllElements();
     //relations.removeAllElements();
	    setCurrentArgument(null);
	    repaint();
	}

	/** Modification de l'etat de l'argument selectionne pour
	    l'utilisateur u */
	void supportCurrentArgument(User u) {
	    //System.out.println("*** Argumentation suppCurrArg start");

	    //pj 20011026
     //currentArgument.support(u);
     //XML version pj 20021128
     //papa.avertir("supportArgument",currentArgument.getId(),"true");
	    if( currentArgument == null ) return;
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes( "id", currentArgument.getId(), "support", "true" ),
		   XMLTree.Contents()
		   )
	     );
	    //System.out.println("*** Argumentation suppCurrArg stop");

	}

	public void unsupportCurrentArgument(User u) {
	    //pj 20011026
     //currentArgument.unsupport(u);
     //XML version pj 20021128
     //papa.avertir("supportArgument",currentArgument.getId(),"false");
	    if( currentArgument == null ) return;
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes( "id", currentArgument.getId(), "support", "none" ),
		   XMLTree.Contents()
		   )
	     );
	}

	void challengeCurrentArgument(User u) {
	    //pj 20011026
     //currentArgument.challenge(u);
     //XML version pj 20021128
     //papa.avertir("challengeArgument",currentArgument.getId(),"true");
	    if( currentArgument == null ) return;
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes( "id", currentArgument.getId(), "support", "false" ),
		   XMLTree.Contents()
		   )
	     );
	}

	void unchallengeCurrentArgument(User u) {
	    //pj 20011026
     //currentArgument.unchallenge(u);
     //XML version pj 20021128
     //papa.avertir("challengeArgument",currentArgument.getId(),"false");
	    if( currentArgument == null ) return;
	    avertir(
	     new XMLTree( "argument",
		   XMLTree.Attributes( "id", currentArgument.getId(), "support", "none" ),
		   XMLTree.Contents()
		   )
	     );
	}

	/**
	    * XML version for event management
	 **/
	/*
	 <!ELEMENT grapher               - -     (argument|relation)+>
	 */
	void performCommand(String user, XMLTree data) {

	    final String NONE = "EMPTY";

	    try {
		/*
		 <!ELEMENT argument              - -     (name|comment|size|position)*>
		 <!ATTLIST argument
		 id              CDATA           #REQUIRED
		 action          (create,delete) ?
		 type            (box,arrow)     box
		 apparence       CDATA           ""
		 edit            (true1,false1)  true1
		 support         (true2,false2,none)  true2
		 >
		 */
		if(data.tag().compareTo("argument") == 0) {

		    String id = data.getAttributeValue("id");
		    String action = data.getAttributeValue("action", NONE);
		    String type = data.getAttributeValue("type", NONE);
		    String apparence = data.getAttributeValue("apparence", NONE);	// not used yet
		    String edit = data.getAttributeValue("edit", NONE);
		    String support = data.getAttributeValue("support", NONE);
		    String sticker = data.getAttributeValue("sticker", NONE);

		    try {
			getArgument(id);
		    }
		    catch( ArgNotFoundException ae ){
			// if it doesn't exist, we create it, we miss some things ???
			if( action.compareTo("create") != 0)  {
			    System.err.println( "Reference to never created Arg, create it id =" + id);
			}
			if( type.compareTo("arrow") == 0 ) {
			    nouvelleFleche(id);
			}
			else {	// assume it's a box
			    nouvelleBoite(id);
			}
		    }

		    if( sticker != NONE) {
			setSticker(id, sticker);
		    }

		    if( edit != NONE) {
			boolean state = false;
			if( edit.compareTo("true") == 0 ) state = true;
			editArgument(user, id, state);
		    }

		    if( support != NONE ) {
			if( support.compareTo("none") == 0 ) {
			    challengeArgument(user, id, false);
			    supportArgument(user, id, false);
			}
			else if( support.compareTo("true") == 0 ) {
			    challengeArgument(user, id, false);
			    supportArgument(user, id, true);
			}
			else {
			    challengeArgument(user, id, true);
			    supportArgument(user, id, false);
			}
		    }

		    for (Enumeration e = data.elements() ; e.hasMoreElements() ;) {
			Object m; XMLTree x;

			m = e.nextElement();
			if( !( m instanceof XMLTree ) ) continue;
			x = (XMLTree)m;

			if( x.tag().compareTo("name") == 0) {
			    /*
			    <!ELEMENT name                  - -     (text)>
			     */
			    renameArgument(user, id, x.getText());
			}
			else if(x.tag().compareTo("comment") ==0 ) {
			    /*
			     <!ELEMENT comment               - -     (text)>
			     */
			    commentArgument(user, id, x.getText());
			}
			else if(x.tag().compareTo("size") == 0) {
			    /*
			     <!ELEMENT size                  - -     EMPTY>
			     <!ATTLIST size
			     width           NUMBER  #REQUIRED
			     height          NUMBER  #REQUIRED
			     >
			     */
			    resizeArgument(id, x.getAttributeValue("width"), x.getAttributeValue("height"));
			}
			else if(x.tag().compareTo("position") == 0) {
			    /*
			     <!ELEMENT position              - -     EMPTY>
			     <!ATTLIST position
			     x               NUMBER  #REQUIRED
			     y               NUMBER  #REQUIRED
			     >
			     */
			    //System.err.println("x = " + x.getAttributeValue("x") + " ; y = " + x.getAttributeValue("y") ) ;

			    // C'est un peu dommage que le XMLTree s'apelle x ...
			    moveArgument(id,x.getAttributeValue("x"), x.getAttributeValue("y"));
			}
			else {
			    // unknow argument command ???
			}
		    }

		    // we don't test the creation state, it's naturaly made with the id test.
		    if( (action != NONE) && (action.compareTo("delete") == 0) ) {
			deleteArgument(id);
		    }

		}
		/*
		 <!ELEMENT relation                      - -     EMPTY>
		 <!ATTLIST relation
		 action          (create,delete)   #REQUIRED
		 from            CDATA   #REQUIRED
		 to              CDATA   #REQUIRED
		 >
		 */
		else if(data.tag().compareTo("relation") == 0) {
		    String action = data.getAttributeValue("action");
		    String from = data.getAttributeValue("from");
		    String to = data.getAttributeValue("to");

		    if( action.compareTo( "create" ) == 0 ) {
			setRelation(from,to);
		    }
		    else {	// assume it's delete
			unsetRelation(from,to);
		    }
		}
		else if(data.tag().compareTo("engaged") == 0) {
		    String status = data.getAttributeValue("status");
		    //// PJ 20030915
      //// papa.engagedUser(user, status);
      //// what appen if we don't use the engage value ??
		    papa.engagedUser(user, status);
		}
		else {
		    // unknown command ???
		}
	    }
	    catch(Exception e) {
		e.printStackTrace();
	    }
	    if (automaticLayout) restartLayout();
	}

	void performCommand(String cmd) {

	    //MP 8/8/2002 Placement automatique avec l'algorithme
     //if (cmd.equals("place")) graphe.spring();

	    if ( getProtagonists().contains(getCurrentUser()) ) {
		try {
		    if (cmd.equals("nvboite")) askMoreInfo(null, true, true); //nouvelleBoite();
		    else if (cmd.equals("nvfleche")) askMoreInfo(null, false, true); //nouvelleFleche();
		    else if (cmd.equals("moreInfo")) askMoreInfo(currentArgument, (currentArgument instanceof Boite), false);
		    else if (cmd.equals("delete")) deleteCurrentArgument();
		} catch( ArgException ex) {}
	    }/* else {
			System.err.println("You should participate if you want to be able to perform an action!") ;
	    }*/
	}

	void performCommand(String cmd, String id) {

	    //PJ 20011026 gestion des id uniques
	    rememberID( id );

	    try {
		if (cmd.equals("nvboite")) nouvelleBoite(id);
		else if (cmd.equals("clear")) toutEffacer();
		else if (cmd.equals("delete")) deleteArgument(id);
		//else if (cmd.equals("getsticker")) {/* get the sticker from argument 'id'. */;}
  //MP 8/8/2002 Placement automatique avec l'algorithme
  //else if (cmd.equals("place")) graphe.spring();

	    }
	    catch( ArgException ex) {
	    }
	}

	void performCommand(String origine, String cmd, String id, String arg) {

	    //PJ 20011026 gestion des id uniques
	    rememberID( id );

	    try {
		if (cmd.equals("nvboite")) nouvelleBoite(id);
		else if (cmd.equals("nvfleche")) nouvelleFleche(id,arg);
		else if (cmd.equals("setRelation")) setRelation(id,arg);
		else if (cmd.equals("unsetRelation")) unsetRelation(id,arg);
		else if (cmd.equals("nvboiteNb")) nouvelleBoite(id, arg) ; /* Use with Alex */
		      else {
			  boolean state = false ;
			  if (arg.equals("true")) state = true ;
			  if (cmd.equals("supportArgument")) supportArgument(origine, id, state);
			  else if (cmd.equals("challengeArgument")) challengeArgument(origine, id, state);
			  else if (cmd.equals("renameArgument")) renameArgument(origine, id, arg);
			  else if (cmd.equals("commentArgument")) commentArgument(origine, id, arg);
			  else if (cmd.equals("editArgument")) editArgument(origine, id, state);
		      }
	    } catch( ArgException ex) {}
	}

void performCommand(String origine, String cmd, String id, String arg1, String arg2) {

    //PJ 20011026 gestion des id uniques
    rememberID( id );

    try {
	if (cmd.equals("moveArgument")) {
	    moveArgument(id, arg1, arg2);
	}

	//mp 28/2/2002 Gestion du redimensionnement de l'argument
	else if (cmd.equals("resizeArgument")) {
	    resizeArgument(id, arg1, arg2);
	} else {
	    System.out.println("Unknown command " + cmd);
	}
    } catch( ArgException ex) {}
}

private void setSticker(String argId, String value) throws ArgException {
    Argument a = getArgument(argId);
    a.setNumber( value );
}

private void supportArgument(String userName, String argId, boolean state) throws ArgException {
    User u = getUser(userName);
    Argument a = getArgument(argId);
    if (state) a.support(u);
    else a.unsupport(u);
    a.repaint();
    papa.updatePanelArg();
    updateArgMoreInfo(a);
}

private void challengeArgument(String userName, String argId, boolean state) throws ArgException {
    User u = getUser(userName);
    Argument a = getArgument(argId);
    if (state) a.challenge(u);
    else a.unchallenge(u);
    a.repaint();
    papa.updatePanelArg();
    updateArgMoreInfo(a);
}

private void renameArgument(String userName, String argId, String name) throws ArgException {
    User u = getUser(userName);
    Argument a = getArgument(argId);
    a.setName(name);
    a.resize();
    invalidate();
    repaint();
    updateArgMoreInfo(a);
}

private void commentArgument(String userName, String argId, String comment) throws ArgException {
    User u = getUser(userName);
    Argument a = getArgument(argId);
    a.setComment(comment);
    a.repaint();
    updateArgMoreInfo(a);
}

private void editArgument(String userName, String argId, boolean state) throws ArgException {
    User u = getUser(userName);
    Argument a = getArgument(argId);
    if (state) a.setEditeur(u);
    else a.setEditeur(null);
}

// Florent Beauchamp : Besoin d'acceder a la methode dans Graphe.java, donc public
public void moveArgument(String id, String sx, String sy)  throws ArgException {
    int x = Integer.parseInt(sx);
    int y = Integer.parseInt(sy);
    Argument a = getArgument(id);
    if (a != null) {
	a.setCenterLocation(x,y);
	invalidate();
	repaint();
    } else {
	System.out.println("Can't move Argument "+id+ " to [ x="+sx+" ; y="+sy+ " ]");
    }
}

//mp 28/2/2002 Gestion du redimensionnement de l'argument
private void resizeArgument(String id, String sx, String sy) throws ArgException {
    int x = Integer.parseInt(sx);
    int y = Integer.parseInt(sy);
    Argument a = getArgument(id);
    if (a != null) {
	a.setSize(x,y);
	a.setResized(true);
	invalidate();
	repaint();
    } else {
	System.out.println("Can't resize Argument "+id+" with [x="+sx+"; y="+sy+"]");
    }
}

//XML pj 20021128
//void avertir(String cmd, String id, String arg1, String arg2){
//	papa.avertir(cmd, id, arg1, arg2);
//}

void avertir( XMLTree cmd ){
    papa.avertir(cmd);
}

void avertir( XMLTree cmd1, XMLTree cmd2 ){
    papa.avertir(cmd1,cmd2);
}

void avertir( XMLTree cmd1, XMLTree cmd2, XMLTree cmd3 ){
    papa.avertir(cmd1,cmd2,cmd3);
}


/** Recherche l'argument de tel id */
private synchronized Argument getArgument(String id) throws ArgNotFoundException {
    int i ;
    Argument a = null ;

    for (i = 0 ; i < propositions.size() ; i++) {
	Boite b = (Boite) propositions.elementAt(i) ;
	//System.err.println( "known arg : [" + b.getId() + "]/[" + id +"]" );
	if (b.getId().equals(id)) a = b ;
    }
    if (a == null)
	for (i = 0 ; i < relations.size() ; i++) {
	    Fleche b = (Fleche) relations.elementAt(i) ;
	    if (b.getId().equals(id)) a = b ;
	}
	    if (a == null) {
		//System.out.println("ERROR : No such Argument (ID = " + id + ")");
		throw new ArgNotFoundException( "Arg not found ID = " + id);
	    }
	    return a;
}

/** Recherche l'utilisateur de tel id */
private User getUser(String nom){
    int i ;
    User a = null ;

    for (i = 0 ; i < protagonists.size() ; i++) {
	User b = (User) protagonists.elementAt(i) ;
	if (b.getUserName().equals(nom)) a = b ;
    }
    if (a == null) {
	System.out.println("ERROR : No such User (name = " + nom + ")");
	//pj, ca peut etre dans le cas d'un evenement a traiter lorsque l'on rejoue
 // alors que le user n'est pas engage, alors on l'engage de force

	a = papa.getUser(nom);
	a.setUserEngaged(true);
	updateProtagonists();
	//pj
    }
    return a;
}

/** Visualisation des infos cach�es : nom, commentaire, liste
*  des pour et des contre.
*  Les champs Nom et Commentaire ne sont modifiables que
*  (1) si personne n'est en train de les editer (getEditeur = null)
*  (2) si on s'est donne la main (itsMe)
*
*  PJ 09/2001 rale, le bouton more info devrait etre disable si qq est
*  est deja en train de modifier ca ailleur.
*
*/
private void callMoreInfo(Argument a, boolean isaBox, boolean createNewArg){
    // there is a bug in the win2k implementation, the modal dialogs are not realy
    // modal, you can use the GUI of the underlaying window. It's bad and have some
    // concequenses : if you click 2 times on the modify button, you lock your own
    // box or arrow.

    boolean itsMe = papa.getItsMe();

    // it doesn't work on linux ppc with gnome2 ???
    //if (miDialog == null) miDialog = new MoreInfo(papa);
    if( miDialog != null ) miDialog.dispose();
    miDialog = new MoreInfo(papa);

    if ( itsMe && (a == null) ) {
	miDialog.update(a,isaBox,true, createNewArg);        //champs modifiables, mais argument null
	miDialog.setVisible(true);
    }
    else if (a != null) {
	if (itsMe && (a.getEditeur() == null)) {
	    a.setEditeur(getCurrentUser());
	    if (createNewArg == false) avertir(
					new XMLTree( "argument",
		  XMLTree.Attributes( "id", a.getId(),"edit", "true" ),
		  XMLTree.Contents()
		  )
					);
	}

	if (itsMe && (a.getEditeur() == getCurrentUser())) {
	    miDialog.update(a, isaBox, true, createNewArg);
	    miDialog.setVisible(true);
	    if (createNewArg == false) avertir(
					new XMLTree( "argument",
		  XMLTree.Attributes( "id", a.getId(),"edit", "false" ),
		  XMLTree.Contents()
		  )
					);
	}
	else {// a.getEditeur() != getCurrentUser() : MoreInfo avec Champs non modifiables
	    miDialog.update(a, isaBox, false, createNewArg);
	    miDialog.setVisible(true);
	}
    }
    else return; // (a == null && !itsMe)
    papa.repaint();
}

void setStateFromMoreInfo(XMLTree t) {
    state = t;
}

/** Open MoreInfo Dialog and update the edited argument
*/
void askMoreInfo(Argument a, boolean isaBox, boolean createNew) {
    state = null;
    callMoreInfo(a, isaBox, createNew);
    if (state != null) avertir (state);
}

/** repercuter les changements dans la fenetre MoreInfo
* si celle-ci est ouverte sur cet argument
*/
private void updateArgMoreInfo(Argument a){
    if ((miDialog != null) &&
	(miDialog.isVisible()) &&
	(miDialog.getCurrentArgument().equals(a)))
	miDialog.update(a);
}

void addLinkedArgument(Argument a, boolean removeMe){
    if (!linkedArgs.contains(a)) {
	linkedArgs.addElement(a);
    }

    if (linkedArgs.size() == 2) {
	try {
	    Argument o = (Argument) linkedArgs.firstElement();
	    Argument d = (Argument) linkedArgs.lastElement();
	    if (removeMe) {
		unsetRelation(o,d,true); // true pour avertir papa
	    } else {
		setRelation(o,d,true);	// true pour avertir papa
	    }
	    repaint();
	} catch( ArgException ex ) {
	    System.err.println("Not enough arguments...") ;
	}

	linkedArgs.removeAllElements();
    }
}

private void setRelation(Argument o, Argument d, boolean avertir){
    if (o instanceof Fleche) {
	Fleche f = (Fleche) o;
	//pj 20011026
	if (avertir) {
	    //XML pj 20021128
     //papa.avertir("setRelation",o.getId(), d.getId());
	    avertir(
	     new XMLTree("relation",
		  XMLTree.Attributes(
		       "action","create",
		       "from",o.getId(),
		       "to",d.getId()
		       ),
		  XMLTree.Contents()
		  )
	     );
	}
	else f.addConsequent(d);
    }
    else if (d instanceof Fleche) {
	Fleche f = (Fleche) d;
	//pj 20011026
	if (avertir) {
	    //XML pj 20021128
     //papa.avertir("setRelation",o.getId(), d.getId());
	    avertir(
	     new XMLTree("relation",
		  XMLTree.Attributes(
		       "action","create",
		       "from",o.getId(),
		       "to",d.getId()
		       ),
		  XMLTree.Contents()
		  )
	     );
	}
	else f.addAntecedent(o);
    } else {	// lien entre deux boites, appele seulement par shiftclik
	     //creer EN LOCAL une fleche entre les deux boites pour materialiser l'action
      //appeler moreInfo(null, false)
      //creer VIA SERVEUR la fleche complete
	int x, y ;
	x = (o.getCenterLocation().x + d.getCenterLocation().x ) / 2 ;
	y = (o.getCenterLocation().y + d.getCenterLocation().y ) / 2 ;
	if (avertir) {
	    String id = getUniqID();

	    Fleche f = new Fleche(this,id);
		f.setCenterLocation(x,y);
	    f.setName(papa.comment.getString("SymRelation0"));
	    f.resize();
	    addRelation(f);
	    setCurrentArgument(f);
	    f.support(papa.getCurrentUser());//Par defaut

		setRelation(o, f, false);
		setRelation(f, d, false);
		callMoreInfo(f, false, true); //fleche a creer
		XMLTree tree1 = state;

		//On supprime le faux noeud
		    try { deleteArgument(id); }
		    catch (ArgException e) { System.err.println("Trying to delete unknown argument id ="+id);}

		if (tree1 == null) {
			//On a appuye sur Cancel
		    return; //Ca s'arrete la
		}
		//Sinon on poursuit en creant la relation
		tree1 = tree1.add( new XMLTree( "position",
                                		XMLTree.Attributes(
						     "x", String.valueOf((int)x),
						     "y", String.valueOf((int)y)
						     ),
				  XMLTree.Contents()
				  )
		     );

		XMLTree tree2 = new XMLTree("relation",
			      XMLTree.Attributes(
			    "action","create",
			    "from",  o.getId(),
			    "to",   id
			    ) ,
			      XMLTree.Contents()
			      );

		XMLTree tree3 = new XMLTree("relation",
			      XMLTree.Attributes(
			    "action","create",
			    "from", id,
			    "to", d.getId()
			    ) ,
			      XMLTree.Contents()
			      );

		avertir(tree1, tree2, tree3);
	}
    }
}

private void setRelation(String o, String d) throws ArgException {
    Argument orig = getArgument(o);
    Argument dest = getArgument(d);
    setRelation(orig, dest, false);
    repaint();
}

private void unsetRelation(Argument o, Argument d, boolean avertir) throws ArgException {
    if (avertir) {
	//XML pj 20021128
 //papa.avertir("unsetRelation",o.getId(), d.getId());
	avertir(
	 new XMLTree( "relation",
	       XMLTree.Attributes(
			   "action", "delete",
			   "from",   o.getId(),
			   "to",     d.getId()
			   ),
	       XMLTree.Contents()
	       )
	 );
    } else {
	if (o instanceof Fleche) {
	    Fleche f = (Fleche) o;
	    f.removeConsequent(d);
	}
	if (d instanceof Fleche) {
	    Fleche f = (Fleche) d;
	    f.removeAntecedent(o);
	}
    }
}

private void unsetRelation(String o, String d) throws ArgException {
    Argument orig = getArgument(o);
    Argument dest = getArgument(d);
    unsetRelation(orig, dest, false);
    repaint();
}

Thread relaxer;
/** tentative d'arrangement automatique du graphe avec algo
* graphLayout de sun */
public void startLayout(){
    nnodes = 0;
    nedges = 0;
    nodes = new Vector(1,1);
    edges = new Edge[500];

    Argument a;

    for (int icomp = 0; icomp < propositions.size(); icomp++) {
	a = (Argument) propositions.elementAt(icomp);
	if (a.isVisible()) {
	    makeNode(a);
	    //a.autoResize();
	}
    }

    for (int icomp = 0; icomp < relations.size(); icomp++) {
	Fleche f = (Fleche) relations.elementAt(icomp);
	if (f.isVisible()) {
	    makeNode(f);
	    Vector antec = f.getAntecedent();
	    Vector conseq = f.getConsequent();
	    for (int c = 0 ; c < conseq.size(); c++){
		Argument x = (Argument) conseq.elementAt(c);
		if (x.isEnabled()) makeEdge(f,x);
	    }
	    for (int c = 0 ; c < antec.size(); c++){
		Argument x = (Argument) antec.elementAt(c);
		if (x.isEnabled()) makeEdge(f,x);
	    }
	}
    }

    relaxer = new Thread(this);
    relaxer.start();
}

void stopLayout(){
    relaxer.interrupt();
    invalidate();
    repaint();
}

void restartLayout(){
    stopLayout();
    startLayout();
}

void makeNode(Argument a){
    nodes.addElement(a);
    a.dx = 0.0;
    a.dy = 0.0;
    nnodes++;
}

void makeEdge(Argument a, Argument b){
    edges[nedges] = new Edge();
    edges[nedges].from = a;
    edges[nedges].to = b;
    edges[nedges].len = 0.0;
    nedges++;
}

public void run() {
    Thread me = Thread.currentThread();
    while (relaxer == me) {
	relax();
	try {
	    Thread.sleep(100);
	} catch (InterruptedException e) {
	    break;
	}
    }
}

class Edge {
    Argument from;
    Argument to;
    double len;
}



synchronized void relax() {
    /* Les liens se resserrent */
    double f = 0.022 ;
    /* les centres se repoussent */
    double g = 2.82;

    //System.out.println("Coeff de resserrement : "+f);

    Argument n1;
    Argument n2;
    Point p1;
    Point p2;
    Dimension s1, s2;

    for (int i = 0 ; i < nedges ; i++) {
	Edge e = edges[i];
	p1 = e.from.getCenterLocation();
	p2 = e.to.getCenterLocation();

	double vx = (double) (p2.x - p1.x);
	double vy = (double) (p2.y - p1.y);
	double dx = f * vx;
	double dy = f * vy;

	e.to.dx -= dx;
	e.to.dy -= dy;
	e.from.dx += dx;
	e.from.dy += dy;
    }

    /* Les boites et les noeuds se repoussent entre eux */
    for (int i = 0 ; i < nnodes ; i++) {
	n1 = (Argument) nodes.elementAt(i);
	p1 = n1.getCenterLocation();
	double dx, dy;

	s1 = n1.getSize();

	for (int j = i+1 ; j < nnodes ; j++) {
	    n2 = (Argument) nodes.elementAt(j);
	    p2 = n2.getCenterLocation();
	    s2 = n2.getSize();

	    int vx = p2.x - p1.x;
	    int vy = p2.y - p1.y;
		/*
	    if ( ! n1.getBounds().intersects(n2.getBounds())) {
		Point q1 = Fleche.getPoignee(p1.x, p1.y, s1.width, s1.height, vx, vy);
		Point q2 = Fleche.getPoignee(p2.x, p2.y, s2.width, s2.height, -vx, -vy);

		vx = q2.x - q1.x;
		vy = q2.y - q1.y;
	    }
	*/

	    double len2 = Math.sqrt(vx * vx + vy * vy);
	    double diag2 = Math.sqrt( s2.width * s2.width + s2.height * s2.height) + Math.sqrt(s1.width * s1.width + s1.height * s1.height ) ;
	    //if (len == 0) {
	    if (len2 == 0) {
		dx = Math.random();
		dy = Math.random();
		//} else if (len < 100) {
		} else if ( len2 < 0.7* diag2 ) {
		    //dx = 100* vx / len /len;
		    //dy = 100*vy / len /len;
		    dx = g * vx / len2;
		    dy = g * vy / len2;
		} else dx = dy = 0.0;

	    n1.dx -= dx;
	    n1.dy -= dy;
	    n2.dx += dx;
	    n2.dy += dy;
	    }
	}

    /* Repositionnement asymptotique des boites */
    Dimension d = getSize(); //Dimension de la zone
    double x, y;	//Nouvelles positions
    for (int i = 0 ; i < nnodes ; i++) {
	n1 = (Argument) nodes.elementAt(i);
	p1 = n1.getCenterLocation();
	s1 = n1.getSize();

	//Nouvelles positions a prori
 //On corrige pour ne pas que cela deborde de la zone d
	x = p1.x + Math.max(-5, Math.min(5, n1.dx));
	y = p1.y + Math.max(-5, Math.min(5, n1.dy));

	if (x < s1.width/2) {//Bord de gauche
	    x = s1.width/2 + 1 ;
	} else if (x > d.width - s1.width/2) {//Bord de droite
	    x = d.width - s1.width/2 - 1 ;
	}
	if (y < s1.height/2) {//Bord du haut
	    y = s1.height/2 + 1 ;
	} else if (y > d.height - s1.height/2) {//Bord du bas
	    y = d.height - s1.height/2 - 1 ;
	}

	//Dimension dim = n1.getSize();
	int abscisse = (int) Math.round(x);
	int ordonnee = (int) Math.round(y);
	n1.setCenterLocation(abscisse, ordonnee); //WORKS but not synchronized with server...

	/*  SYNCHRONISATION
	    try {
		avertir(new XMLTree( "argument" ,
		       XMLTree.Attributes( "id", n1.getId()),
		       XMLTree.Contents( new XMLTree("position",
				       XMLTree.Attributes(
			      "x",String.valueOf(abscisse),
			      "y", String.valueOf(ordonnee)),
				       XMLTree.Contents())
			   )
		       )
	  );
	    } catch (Exception e){System.err.println(e);}
	*/
	n1.dx /= 2;
	n1.dy /= 2;
    }
    //invalidate();
    //repaint();
    }

public void keyTyped(KeyEvent e) {
}

public void keyPressed(KeyEvent e) {
    if (papa.getItsMe() == false) return;

    if (e.isShiftDown()) setMouseMode(LINKMODE);
    else if (e.isControlDown()) setMouseMode(UNLINKMODE);
    
    if (currentMouseMode != MOVEMODE) {
	Argument a = getBoxAt(mousePosition.x, mousePosition.y);
	if (a != null) {
	    setCurrentTarget(a) ;
	    a.stopToolTip();
	    resetCursor();
	    a.repaint();
	}
    }
}

public void keyReleased(KeyEvent e) {
    	if (papa.getItsMe() == false) return;

	if (currentMouseMode != MOVEMODE) {
		setMouseMode(MOVEMODE);
		resetCursor();
	}

    	if (dragging) {
		dragging = false;
		repaint();
	}

	if ((mousePosition != null) && (getBoxAt(mousePosition.x, mousePosition.y) != null)) {
		setCurrentTarget(null);
		resetCursor();
		Argument a = getBoxAt(mousePosition.x, mousePosition.y);
		a.repaint();
	}
}


/* Nouvelle gestion du deplacement */
private Point oldLocation;
private boolean resized;

final static int MOVEMODE       = 0;
final static int LINKMODE       = 1;
final static int UNLINKMODE     = 2;
final static int DELETEMODE     = 3;
long t0, t1;

int currentMouseMode;
Argument underMouseArgument;

int getMouseMode() {
    return currentMouseMode;
}

void setMouseMode(int m) {
    currentMouseMode = m;
}

void resetCursor() {
    if (currentMouseMode == MOVEMODE) {
	if (resized) setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
	else setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
    }
    else if (currentMouseMode == LINKMODE) setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    else if (currentMouseMode == UNLINKMODE) setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
}

// Gestion de la souris dans l'espace graphique


public void mouseEntered(MouseEvent e) {
    mousePosition = e.getPoint();
    setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    if (e.isShiftDown()) setMouseMode(LINKMODE) ;
    else if (e.isControlDown()) setMouseMode(UNLINKMODE) ;
    else setMouseMode(MOVEMODE) ;
    //requestFocus(); -- gdyke don't make the grapher window be focused because of mouseover
}

public void mouseExited(MouseEvent e) {
}


public void mouseMoved(MouseEvent e) {
    mousePosition = e.getPoint();
    Component c = getComponentAt(mousePosition);
    if ((c != null) && (c instanceof Argument)) {
	Argument a = (Argument) c;
	if (a.equals(underMouseArgument)) argumentMoved(a, e);
	else {
	    if (underMouseArgument != null) argumentExited(underMouseArgument, e);
	    argumentEntered(a,e);
	}
    } else if (underMouseArgument != null) argumentExited(underMouseArgument, e);	
}

public void mouseClicked(MouseEvent e) {
    mousePosition = e.getPoint();
    Component c = getComponentAt(mousePosition);
    if ((c != null) && (c instanceof Argument)) {
	Argument a = (Argument) c;
	argumentClicked(a, e);
    } else setCurrentArgument(null);	
}

public void mousePressed(MouseEvent e) {
    mousePosition = e.getPoint();
    Component c = getComponentAt(mousePosition);
    if ((c != null) && (c instanceof Argument)) {
	Argument a = (Argument) c;
	argumentPressed(a, e);
    }
}

public void mouseReleased(MouseEvent e) {
    mousePosition = e.getPoint();
    Component c = getComponentAt(mousePosition);
    if ((c != null) && (c instanceof Argument)) {
	Argument a = (Argument) c;
	argumentReleased(a, e);
    }
    if (dragging) {
	dragging = false;
	repaint();
    }
}

public void mouseDragged(MouseEvent e) {
    mousePosition = e.getPoint();
    if (underMouseArgument != null) argumentDragged(underMouseArgument, e);
}

//MQ 20011028 interaction shiftclik
public void argumentDragged(Argument a, MouseEvent e) {
    //System.err.println( "Argument.mouseDragged " + this ) ;

    mousePosition = e.getPoint();
    dragging = true;

    // PJ 20030515
    if( isInteractive() == false ) return;

    if (!a.valid) {//Out of the drawing zone
	return ;
    }

    if (firstClick == null) {
	firstClick = a.getCenter();
    }

    // mp 11/2/2002 Redimension de l'argument.
    // Le texte doit aussi �tre redimensionn�.
    if ((resized == true) && (currentMouseMode == MOVEMODE)) {
	Rectangle b1,b2;
	Rectangle limits;

	limits = new Rectangle(0,0,getSize().width,getSize().height) ;
	if ( limits.contains(mousePosition) == false ) {
	    resized = false ;
	    a.valid = false ;
	    return ;
	}


	b1 = a.getBounds();

	//PJ, il faut pas utiliser getX() sur les rectangles, c'est pas dans la jvm 1.1.8
	a.setBounds(b1.x, b1.y,
	   Math.max( 10, b1.width  + mousePosition.x - firstClick.x - b1.x),
	   Math.max( 10, b1.height + mousePosition.y - firstClick.y - b1.y) );
	firstClick = new Point(mousePosition.x - b1.x, mousePosition.y - b1.y);
	invalidate();
	repaint();
	b2 = a.getBounds();
    }
    // XS 26 07 2002 only engaged user can create or remove links
    // Drag = create a link -- draw a blue line
    // Drag = remove a link -- draw a red line
    else if ( (currentMouseMode == LINKMODE) || (currentMouseMode == UNLINKMODE)
	      &&  getProtagonists().contains(getCurrentUser())) {

	Graphics g=getGraphics();
	if (currentMouseMode == LINKMODE) g.setColor(Color.blue);
	else g.setColor(Color.red);

	Argument ar = getBoxAt(e.getX(), e.getY());
	setCurrentTarget( ar );

	oldLocation = a.getLocation();
	repaint();
	firstClick = new Point(mousePosition.x - oldLocation.x, mousePosition.y - oldLocation.y);
	
    } else if (currentMouseMode == MOVEMODE) { // Drag = Deplacement
	Rectangle b1,b2;

	Rectangle limits;
	limits = new Rectangle(0,0,getSize().width,getSize().height) ;

	if ( limits.contains(mousePosition) == false ) {
	    resized = false ;
	    a.valid = false ;
	    return ;
	}

	b1 = a.getBounds();
	a.setLocation(mousePosition.x - firstClick.x, mousePosition.y - firstClick.y);
	invalidate();
	repaint();
	b2 = a.getBounds();
    }
}


/** On ne fait rien quand on ne fait que bouger la souris */
public void argumentMoved(Argument a, MouseEvent e) {
    mousePosition = e.getPoint();

    Rectangle rect = a.getBounds();
    Rectangle redim = new Rectangle(rect.x + rect.width-7,
				    rect.y + rect.height-7,
				    7,
				    7);

    if (redim.contains(mousePosition) && (currentMouseMode == MOVEMODE)) {
	resized = true;

    } else {
	resized = false;
    }
    resetCursor();
}

/** Un click selectionne l'argument et d�selectionne le pr�c�dent s'il existe. */
public void argumentClicked(Argument a, MouseEvent e) {

    long t1 = e.getWhen();

    if ((t1-t0) < 500) { // c'est un double click
	setCurrentArgument(a);
	performCommand("moreInfo");
	return;
    }
    t0 = t1;

    setCurrentArgument(a);
    setCurrentTarget(null);
}

/** On m�morise l'endroit dans l'argument o� on clicke pour produire
un d�placement plus agr�able */
//MQ 20011028 interaction shiftclik
public void argumentPressed(Argument a, MouseEvent e) {
    // PJ 20030515
    if( isInteractive() == false ) return;

    oldLocation = a.getLocation();

    setCurrentArgument(a);
    setCurrentTarget(null);

    //mp 11/2/2002 boites redimensionnables
    if ((resized == true) && (currentMouseMode == MOVEMODE))
	firstClick = new Point(mousePosition.x - oldLocation.x, mousePosition.y - oldLocation.y);
    else {
	// XS 26 07 2002 only engaged user can create or remove links
	/** PJ 20030602 suppress the shift and control clik mode i*/
	if ( (currentMouseMode == LINKMODE) || (currentMouseMode == UNLINKMODE)
      &&  getProtagonists().contains(getCurrentUser())) {
	    addLinkedArgument(a,false);
	    repaint();
	    firstClick = a.getCenter() ;
	} else {
	    // XS 27022003 : ca sert a quoi ??
	    firstClick = new Point(mousePosition.x - oldLocation.x, mousePosition.y - oldLocation.y);
	}
    }
}


/** au relachement de la souris on avertit le serveur de la position de la boite */
//MQ 20011028 interaction shiftclik
public void argumentReleased(Argument a, MouseEvent e) {

    // PJ 20030515
    if(isInteractive() == false ) return;


    //mp 11/2/2002 Boites redimensionnables
    if ((resized == true)  && (currentMouseMode == MOVEMODE)) {
	resized = false;
	resetCursor();
	a.setResized( true ); // on conserve le fait que l'on a ete redemensionne
		     //XML pj 20021128
	avertir(
		       new XMLTree( "argument" ,
		      XMLTree.Attributes( "id", a.getId() ),
		      XMLTree.Contents(
			 new XMLTree("size",
		XMLTree.Attributes(
		     "width",String.valueOf(a.getSize().width),
		     "height",String.valueOf(a.getSize().height)
		     ),
		XMLTree.Contents()
		)
			 )
		      )
		       );
    }
    else if ((currentMouseMode == LINKMODE)
	     &&  getProtagonists().contains(getCurrentUser())) {
	if( (getCurrentArgument() != null ) && (getCurrentTarget() != null ) ) {
		dragging = false;
		repaint();
	    addLinkedArgument(getCurrentArgument(),false);
	    addLinkedArgument(getCurrentTarget(),false);
	}
	dragging = false;
	linkedArgs.removeAllElements();
	repaint();
    }
    else if ((currentMouseMode == UNLINKMODE)
	     &&  getProtagonists().contains(getCurrentUser())) {
	if( (getCurrentArgument() != null ) && (getCurrentTarget() != null ) ) {
	    // link current and target
		dragging = false;
		repaint();
	    addLinkedArgument(getCurrentArgument(),true);
	    addLinkedArgument(getCurrentTarget(),true);
	}
	dragging = false;
	linkedArgs.removeAllElements();
	repaint();
    }
    else if (!a.getLocation().equals(oldLocation)) {
	// XMP pj 20021128
 //argumentation.avertir("moveArgument", id, ""+getCenterLocation().x, ""+getCenterLocation().y);
	avertir(
		       new XMLTree( "argument" ,
		      XMLTree.Attributes( "id", a.getId()),
		      XMLTree.Contents(
			 new XMLTree("position",
		XMLTree.Attributes(
		     "x",String.valueOf(a.getCenterLocation().x),
		     "y", String.valueOf(a.getCenterLocation().y)
		     ),
		XMLTree.Contents()
		)
			 )
		      )
		       );
    }

    if (!a.valid ) {
	a.valid = true ;
    }
    //System.err.println( "Argument.mouseReleased " + this ) ;
}

/** On change la forme du curseur quand on survole un argument */
public void argumentEntered(Argument a, MouseEvent e) {
    underMouseArgument = a;
    
    mousePosition = e.getPoint();
    requestFocus();
    if( isInteractive() == true )  {

	//mp 5/2/2002 curseur pour arguments redimensionnables
	Rectangle rect = a.getBounds();
	Rectangle redim = new Rectangle(rect.width-4, rect.height-4, rect.width, rect.height);

	if (redim.contains(e.getPoint()) && (currentMouseMode == MOVEMODE)) {
	    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
	    resized = true;
	    return;
	}
    }
    resized = false;
    resetCursor();

    if ( ( (currentMouseMode == LINKMODE) || (currentMouseMode == UNLINKMODE) )
	 &&  getProtagonists().contains(getCurrentUser()) ) {
	setCurrentTarget(a);
	repaint();
    } else a.startToolTip();
}


public void argumentExited(Argument a, MouseEvent e) {
    underMouseArgument = null;
    
    if (a != null) a.stopToolTip();
    
    if (currentMouseMode != MOVEMODE) {
	setCurrentTarget( null );
    }
    mousePosition = null;
}

}
