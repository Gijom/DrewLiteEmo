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
 * File: Grapheur.java
 * Author: Matthieu Quignard
 * Description: A tool to draw argumentation graphs over the internet.
 *  This is the main class.
 *
 * $Id: Grapheur.java,v 1.59.2.2 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Grapheur;

import java.util.*;
import java.awt.*;
import java.awt.event.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

/** Fenï¿½tre (Frame) permettant le dessin de graphes argumentatifs.
  @author Matthieu Quignard
  @version Java 1.1 (avec Listener)
  */
/* suppression du WindowListener, nous heritons maintenant de Panel
 * a la place de Frame
 */
public class Grapheur 
	extends Panel 
	implements ActionListener, WindowListener, ItemListener, CooperativeModule {

	private boolean debug = false ;
    	private boolean standalone=false;
    	private String  standaloneUsers=null;
    boolean hideAttitudes = false; //MQ : mode pour jouer sans les boutons pour/contre

	/** Size of the graphic area */
	private int graphWidth = 900;
	private int graphHeight = 900;

	/** Chaine identifiant le type de module pour tri des messages */
	static final String CODE = "grapheur";

    	/** Police de l'interface */
    	Font police = new Font("SansSerif", Font.PLAIN, 10);


	Communication central_applet = null;

	/** Locale attribute made available to other classes of this package : AppletLance, EditUserDialog */
	Drew.Util.Locale comment;

	/** Dimension de la fenï¿½tre initialisï¿½e ï¿½ -1 -1 pour forcer le
	  redimensionnement.
	  */
	Dimension minSize = new Dimension(-1,-1);

	/** Panneau gauche de la fenï¿½tre affichant la liste des participants
	  et certaines commandes de manipulation graphique */
	Panel commandes ;

	/** Espace graphique pour le dessin d'argumentaires.*/
	Argumentation argu ;

	/** A mini representation opf the grapher
	 * to give an overview when the graph does not fit
	 * in the window.
	 */
	Canvas miniGraph ;

	ScrollPane scp;

	Panel userPane;

	/** Bouton pour changer de mode Actif/Passif */
	ImageCheckbox btnToggleMode;
	
	/** Bouton pour editer les utilisateurs (mode grapheur standalone) */
	ImageButton btnEditUsers;

	/** Bouton pour quitter */
	Button btnQuit;

	/** Bouton pour crï¿½er une nouvelle boï¿½te */
	ImageButton nvBoite;

	/** Bouton pour crï¿½er une nouvelle flï¿½che*/
	ImageButton nvFleche;

	/** Bouton pour dire qu'on soutient l'argument courant */
	Checkbox btnArgPro;

	/** Bouton pour dire qu'on n'est pas d'accord avec l'argument courant */
	Checkbox btnArgContra;

	/** Bouton pour demander plus d'info sur l'argument. */
	Button btnArgInfo;

	/** Bouton pour supprimer l'argument courant */
	ImageButton btnArgDelete;

	/** Bouton pour appeler l'algorithme de placement automatique */
	//MP 8/8/2002
	// Button btnPlace;

	/** Panel avec l'ï¿½tat courant de l'argument pour l'utilisateur courant */
	Panel panel;

	private User currentUser;

    	String realUserName;

	/** entier mï¿½morisant l'utilisateur courant. C'est pour savoir si on a changï¿½ et qu'on doit rafraichir les panneaux */
	int oldIndex = -2 ;

	/** Liste des utilisateurs connectï¿½s (information provenant du centre de connexion*/
	Vector connectedUsers;

    	/** Case ï¿½ cocher pour ajuster automatiquement le graphe */
    	ImageCheckbox btnLayout;

    	/** Bouton pour imprimer */
    	ImageButton btnPrint;

	Image imageActif, imageInactif;

	/**
	* Can we print the graph ?
	* remember the graph.print property/parameter 
	*/
	private boolean isPrintable = false;
	private double  print_scale = 0.7;

	/** 
	* The grapher is interactif by default, but for alex, it's usefull to be not modifeid by users
	* remember the graph.interactive property/parameter 
	*/
	private boolean interactive = true;

	/** 
	* To disable the participate button and engage the user automaticly
	*/
	private boolean enableParticipate = true;
	private boolean needParticipate = false;
	private boolean editUsers = false;

	/** largeur souhaitee pour la fenetre d'impression. Si differente
        * de la fenetre d'argumentation, il y aura une mise a echelle 
	*/
    	int lPrint = 556;

    private boolean enableAutoLayout = false;

	public Grapheur(){
		/* PJ 05-2001
		   super("Argumentation Grapher");
		   */
		super();
		//PJ 20030515
		//comment = new Drew.Util.Locale("Drew.Locale.Client.Grapheur", java.util.Locale.FRENCH);
		//central_applet = new Communication();
		//central_applet.nom = "moi";
	}

	public Grapheur(Communication cdc){
		this();
		constructor(cdc);
	}

	/**  PJ 05-2001, on va tenter de trouver dans les parents du grapher@
	 **  un container qui serait une fenetre
	 */
	public Frame getWindowParent() 
	{
		Container c = this;

		// On va tester 10 fois, pour voir. on evite une boucle inf si yavait pas de fenetre
		for( int i=0; i<10; i++) {
			c = c.getParent();
			if( c instanceof Frame ) return (Frame)c;
		}
		return null;
	}

	public Dimension getPreferredSize() {
		return new Dimension(500,500);
	}

	public synchronized Dimension getMinimumSize(){
		return  new Dimension(500,500);
	}

	/** Reconstruit la liste (graphique et logique) des utilisateurs engagï¿½s 
	  aprï¿½s modification de la variable <b>protagonists</b> interne ï¿½ l'objet Argumentation.
	  <p> Cette mï¿½thode redï¿½finit currentUser, argu.protagonists.
	  @see Argumentation#getProtagonists
	  @see User#getUserEngaged
	  */
	private void refaireListeUsers() {
		int i;
		String name ;
		int index = -1 ;
		Vector v;

		userPane.removeAll();
		argu.updateProtagonists();
		v = argu.getProtagonists();
		UserLabel l;
		User u;

		Dimension dim = scp.getSize();
		userPane.setLayout(new GridBagLayout());
		//userPane.setSize(dim.width - 5, 300); 
		//userPane.setSize(-1,-1);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.insets = new Insets(1,1,1,1);
		c.anchor = GridBagConstraints.NORTHWEST;
		c.gridwidth = 1; c.gridheight = 1;

		for (i=0 ; i< v.size() ; i++) {
			c.gridx = 0; c.gridy = i;
			c.weightx = 0.0 ; c.weighty = 0.0;
			u = (User) v.elementAt(i);
			// add dim as parameter
			l = new UserLabel(this, u, true, 20);
			u.setUserLabel(l);
			l.setEnabled(true);
			l.setVisible(true);
			userPane.add(l,c);

			c.gridx = 1; // c.gridy = i;
			c.weightx = 0.0 ;
			//if (i == v.size() - 1) c.weighty = 1.0;
			//else c.weighty = 0.0;
			l = new UserLabel(this, u, false, 50);
			l.setEnabled(true);
                        l.setVisible(true);
			userPane.add(l,c);
			u.setUserLabel(l);	
			if (u.equals(currentUser)) {
				l.setSelected(true);
				index = i;
			}
		}
		if (v.size() < 4) {
			c.weighty = 1.0; c.gridwidth=2;
			c.gridx = 0; c.gridy = v.size();
			userPane.add(new Label(""),c);
		}

		validate();
	}

	/** Methode retournant l'utilisateur courant.*/
	User getCurrentUser(){
		return currentUser;
	}

	void setCurrentUser(User u){
		if (currentUser != null) {
			//deselectionner
			currentUser.getUserLabel().setSelected(false);
		}
		if (u != null) {
			u.getUserLabel().setSelected(true);
			currentUser = u;
		} else currentUser =null; 

		updatePanelArg();
	}
	
	/* Default selection of the current User 
	 * Me if (interactive) and engaged
	 * else the first engaged participant
	 * else simply the first participant
	 */
	void resetCurrentUser() {
		User u = null;

		if (isInteractive()) {
			// si je ne suis pas engage, je ne serai pas selectionne. 
			u = getUser(central_applet.nom);
			if ((u != null) && (u.getUserEngaged() == false))  u = null;
		}

		//Si je ne suis pas moi-meme selectionne, on prend le premier gus qui le soit.
		Vector protag = argu.getProtagonists();
		for (int i=0 ; ((u == null) && (i < protag.size())) ; i++) {
			u = (User) protag.elementAt(i);
			if (u.getUserEngaged() == false) u = null;
		}

		//Si tout le monde est parti, on prend le premier de la liste
		if ((u == null) && (protag.size() > 0)) u = (User) protag.elementAt(0);

		setCurrentUser(u);	
	}


	/** Methode indiquant si on a la main :
	 *  Le CurrentUser doit avoir notre nom (central_applet.nom) sauf en mode standalone
	 *  Il doit etre engage (cad present)
	 */
	boolean getItsMe(){
		if (currentUser == null) return false ;
		else if (currentUser.getUserEngaged() == false) return false;
		else if (standalone) return true;
		else return currentUser.getUserName().equals(central_applet.nom);
	}

	// GESTION DE L'AFFICHAGE

	/** Actionne l'affichage ou la disparition des infos relatives
	  ï¿½ la position de l'utilisateur courant par rapport ï¿½ l'argument
	  courant
	  */
	private void affichePanelArg(boolean state){
	    
		btnArgPro.setVisible( !hideAttitudes && state );
		btnArgContra.setVisible( !hideAttitudes && state);
		btnArgInfo.setVisible(state);
		btnArgDelete.setVisible(state);
	}

	private void enablePanelArg(boolean state){
		btnArgPro.setEnabled(state);
		btnArgContra.setEnabled(state);
		btnArgInfo.setEnabled(state);
		btnArgDelete.setEnabled(state);
	}   

	/** Affiche ou non la possibilitï¿½ d'ajouter de nouvelles boites
	  ou fleches. Dï¿½pend en fait de l'existence d'un utilisateur courant
	  */
	private void afficheNouveau(boolean state){
		nvBoite.setEnabled(state);
		nvFleche.setEnabled(state);
	}

	private void enableNouveau(boolean state){
		nvBoite.setEnabled(state);
		nvFleche.setEnabled(state);
	}

	/** mise ï¿½ jour des infos relatives
	  ï¿½ la position de l'utilisateur courant par rapport ï¿½ l'argument
	  courant
	  */
	/* 20021128: MQ modified by XS */
	void updatePanelArg() {
		Argument ca = argu.getCurrentArgument();
		User cu = this.currentUser;

		enableNouveau(false);
		affichePanelArg(false);
		btnArgPro.setState(false);
		btnArgContra.setState(false);
		enablePanelArg(false);

		if (cu != null) {
			if (ca != null) {
				btnArgPro.setState(ca.isSupporter(cu));
				btnArgContra.setState(ca.isChallenger(cu));
			}

			// PJ 20030915
			// We enable the panel and the new delete button if
			// we are in standalone or in interactive mode
			if (getItsMe()) {
				enableNouveau(true);
				enablePanelArg(true);
			} else {
				enableNouveau(false);
				enablePanelArg(false);
				argu.setMouseMode(Argumentation.MOVEMODE);
			}
		}
		affichePanelArg(true);
	}	    

	// GESTION DES EVENEMENTS

	/** Gestion des ï¿½vï¿½nements produits sur les boutons gï¿½rï¿½s par
	  le Grapheur : Editer et Quitter. Les autres sont enregistrï¿½s
	  auprï¿½s de Argumentation.
	  */ 
	public void actionPerformed(ActionEvent e) {
		String commande=e.getActionCommand();
		if (commande == "getPassive") { 
			avertir( new XMLTree( "engaged").setAttribute("status", "false") );
		}
		else if (commande == "getActive"){
			avertir( new XMLTree( "engaged").setAttribute("status", "true") );
		}
		//PJ 20030515
		else if (commande == "print"){
			imprimer();
		}
		else if (commande == "quit"){
			stop();
		}
		else if (commande == "editUsers") {
			editerUsers();
		}
	}

	/** Gestion des ï¿½vï¿½nements produits sur la fenï¿½tre */

	public void windowActivated(WindowEvent e) {;}
	public void windowClosed(WindowEvent e) {;}
	public void windowClosing(WindowEvent e) {
		stop();
	}
	public void windowDeactivated(WindowEvent e) {;}
	public void windowDeiconified(WindowEvent e) {;}
	public void windowIconified(WindowEvent e) {;}
	public void windowOpened(WindowEvent e) {;}

	/** Gestion des ï¿½venements sur la liste des protagonistes et 
	  les boites ï¿½ cocher "Pour" et "Contre".
	  Un changement de sï¿½lection occasionne le changement de
	  l'utilisateur courant et provoque pas mal de mises
	  ï¿½ jour. Le changement d'attitude sur l'argument ï¿½galement
	  @see Argumentation#repaintCurrentArgument
	  */
	public void itemStateChanged(ItemEvent e) {
		Component choice = (Component) e.getItemSelectable();

		if (choice.equals(btnArgPro)) {
			
			if (e.getStateChange() == e.SELECTED) {
				argu.supportCurrentArgument(currentUser);
				btnArgContra.setState(false);
			}
			else argu.unsupportCurrentArgument(currentUser);
		}
		else if (choice.equals(btnArgContra)) {
			if (e.getStateChange() == e.SELECTED) {
				argu.challengeCurrentArgument(currentUser);
				btnArgPro.setState(false);
			}
			else argu.unchallengeCurrentArgument(currentUser);
		}
		else if (choice.equals(btnLayout)) {
		    if (e.getStateChange() == e.SELECTED){
			argu.startLayout();
			argu.automaticLayout = true;
		    }
		    else {
			argu.automaticLayout = false;
			argu.stopLayout();
		    }
		}
		else if (choice.equals(btnToggleMode)) {
                    if (e.getStateChange() == e.SELECTED){
			avertir( new XMLTree( "engaged").setAttribute("status", "true") );
                    }
                    else {
			avertir( new XMLTree( "engaged").setAttribute("status", "false") );
                    }
                }

	}

	// Implï¿½mentation du Cooperative Module

	public void constructor(Communication cdc) {
		central_applet = cdc;
		comment = new Drew.Util.Locale( "Drew.Locale.Client.Grapheur",
				Config.getLocale() );

		//PJ 20030515
		String dummy = cdc.getParameter("graph.interactive");
		if( dummy != null ) {
			interactive = dummy.equalsIgnoreCase("true");
		}

		dummy = cdc.getParameter("graph.print");
		if( dummy != null ) {
			isPrintable = dummy.equalsIgnoreCase("true");
		}

		dummy = cdc.getParameter("graph.print.scale");
		if( dummy != null ) {
			//print_scale = Double.parseDouble(dummy); //parseDouble is not in jdk1.1.8
			print_scale = Double.valueOf(dummy).doubleValue();
		}

		dummy = cdc.getParameter("graph.autolayout");
		if( dummy != null ) {
			enableAutoLayout = dummy.equalsIgnoreCase("true");
		}

		dummy = central_applet.getParameter("graph.participate");
		if( dummy != null ) {
			needParticipate = dummy.equalsIgnoreCase("true"); 
			enableParticipate = false;	// hide participate button
		}

		dummy = cdc.getParameter("graph.standalone");
		if( dummy != null ) {
			standalone = dummy.equalsIgnoreCase("true");
			if(standalone == true ) {
				needParticipate = false;
				enableParticipate = false;	// hide participate button

				standaloneUsers = cdc.getParameter("graph.standalone.users");
			}
		}

		dummy = cdc.getParameter("graph.editusers");
		if (( dummy != null) && (dummy.equalsIgnoreCase("true"))) {
			standalone = true;
			needParticipate = false;
			enableParticipate = true;
			editUsers = true ;
		}
			
		dummy = cdc.getParameter("graph.width");
		if( dummy != null ) {
			graphWidth = Integer.parseInt(dummy);
		}

		dummy = cdc.getParameter("graph.height");
		if( dummy != null ) {
			graphHeight = Integer.parseInt(dummy);
		}

		dummy = cdc.getParameter("graph.arrow.edit");
		if (( dummy != null) && (dummy.equalsIgnoreCase("true"))) {
			MoreInfo.FlecheAsText = true;
		}
		dummy = cdc.getParameter("graph.attitudes.hide");
		if (( dummy != null) && (dummy.equalsIgnoreCase("true"))) {
		    this.hideAttitudes = true ;
		}
	}

	//PJ 05-2001
	public String getTitle() {
		return comment.getString("WindowTitle");
	}

	/** return interaction mode */
	boolean isInteractive() {
		return interactive;
	}

	/** Dï¿½marrage du module */
	public void init(){
		/** Mise sur ï¿½coute de cette fenï¿½tre. Elle s'ï¿½coute elle-mï¿½me
		  puisqu'elle implï¿½mente la fonctionnalitï¿½ d'une interface
		  WindowListener. Elle doit comporter toutes les mï¿½thodes de 
		  gestions d'ï¿½vï¿½nements WindowEvent.
		  */
		
		argu = new Argumentation(this, new Dimension(graphWidth, graphHeight));
		setBackground(Color.lightGray);
		realUserName=central_applet.nom;

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

                // la fenetre grapheur
                c.fill = GridBagConstraints.BOTH ;  
                c.insets = new Insets(5,5,5,5);
                c.gridx = 0 ; c.gridy = 0 ; c.gridwidth = 8 ; c.gridheight = 1 ;
                c.weightx = 1.0 ;
                c.weighty = 1.0 ;
                //mp 29/01/2002 ajout d'un ScrollPane contenant l'objet Argumentation
                ScrollPane pane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
                pane.add(argu);
                add(pane,c);

		c.fill = GridBagConstraints.NONE ;
		c.insets = new Insets(0,5,5,5);
		c.anchor = GridBagConstraints.NORTH ;
		c.gridx = 0 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		c.fill = GridBagConstraints.BOTH;
		//scp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scp = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
		scp.setSize(100,50);
		Adjustable bar = scp.getVAdjustable();
		bar.setUnitIncrement(10);
		bar.setBlockIncrement(20);	
		add(scp, c);

		userPane = new Panel();
		userPane.setBackground(Color.white);
		scp.add(userPane);

		c.fill = GridBagConstraints.NONE ;
		c.insets = new Insets(0,5,0,38);
		c.anchor = GridBagConstraints.NORTHWEST ;
                c.gridx = 1 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		
		imageActif = central_applet.getImage(
                                central_applet.getCodeBase(),
                                "Image/greenlightActiveTransp.gif");
                imageInactif = central_applet.getImage(
                                        central_applet.getCodeBase(),
                                        "Image/greenlightInactiveTransp.gif");

		if (editUsers) {
			imageActif = central_applet.getImage(
					central_applet.getCodeBase(),
					"Image/peopleTransp.gif");
			btnEditUsers = new ImageButton(this, imageActif, imageActif, 
								comment.getString("ButtonEditUsers"));
			btnEditUsers.setActionCommand("editUsers");
			add(btnEditUsers,c);
			btnEditUsers.setEnabled(true);
			btnEditUsers.addActionListener(this);
			btnToggleMode = new ImageCheckbox(this, imageActif, imageInactif,
                                                                comment.getString("getActive"));
		} else {
			btnToggleMode = new ImageCheckbox(this, imageActif, imageInactif, 
								comment.getString("getActive"));
			btnToggleMode.setSelected(false);
			btnToggleMode.addItemListener(this);	

			if( enableParticipate == true ) {
				add(btnToggleMode,c);
				btnToggleMode.setEnabled(true);
				if(isInteractive() == false) btnToggleMode.setEnabled(false);
			}
			else {
				add(new Label("",Label.CENTER),c);
			}
		}

		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0,5,0,5);
		c.anchor = GridBagConstraints.NORTHWEST ;
                c.gridx = 3 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		//nvBoite = new Button(comment.getString("ButtonNewBox"));
		imageActif = central_applet.getImage(
				central_applet.getCodeBase(),
				"Image/newboxActifTransp.gif");
		imageInactif = central_applet.getImage(
					central_applet.getCodeBase(),
					"Image/newboxInactifTransp.gif");
		nvBoite = new ImageButton(this,imageActif, imageInactif, comment.getString("ButtonNewBox"));
		nvBoite.setFont(police);
		nvBoite.setActionCommand("nvboite");
		nvBoite.addActionListener(argu);
		add(nvBoite,c);

		c.gridx = 4 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		//nvFleche = new Button(comment.getString("ButtonNewRel"));
		imageActif = central_applet.getImage(
                                central_applet.getCodeBase(),
                                "Image/newnodeActifTransp.gif");
                imageInactif = central_applet.getImage(
                                        central_applet.getCodeBase(),
                                        "Image/newnodeInactifTransp.gif");
                nvFleche = new ImageButton(this,imageActif, imageInactif, comment.getString("ButtonNewRel"));
		nvFleche.setFont(police);
		nvFleche.setActionCommand("nvfleche");
		nvFleche.addActionListener(argu);
		//add(nvFleche,c); // gdyke 07.2007 pas de crŽation de nouvelles boites

		c.fill = GridBagConstraints.HORIZONTAL ;
		c.anchor = GridBagConstraints.SOUTHWEST ;
                c.gridx = 2 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 1 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		btnArgPro = new Checkbox(comment.getString("LabelPro"));
		btnArgPro.setFont(police);
		btnArgPro.addItemListener(this);
		add(btnArgPro,c);
		btnArgPro.setVisible( !hideAttitudes ) ;

		c.anchor = GridBagConstraints.NORTHWEST ;
                c.gridx = 2 ; c.gridy = 2 ; c.gridwidth = 1 ; c.gridheight = 1 ;
		c.weightx = 0.0 ; c.weighty = 0.0 ;
		btnArgContra = new Checkbox(comment.getString("LabelContra"));
		btnArgContra.setFont(police);
		btnArgContra.addItemListener(this);
		add(btnArgContra,c);
		btnArgContra.setVisible( !hideAttitudes );

		// XXX A TEST XXX
		/*
		 * c.insets = new Insets(0,20,0,20) ;
		 * rulerTest = new Ruler(0,2) ;
		 * rulerTest.addAdjustmentListener(this) ;
		 * add(rulerTest) ;
		 */

		// The "Moreinfo" button
		//c.fill = GridBagConstraints.HORIZONTAL ;
		//c.insets = new Insets(5,20,0,20);
		c.anchor = GridBagConstraints.NORTHWEST ;
		c.gridx = 0 ; c.gridy = 6 ;
		c.gridwidth = 1 ; c.gridheight = 1 ;
		c.weightx = 0.0 ;
		c.weighty = 0.0 ;
		btnArgInfo = new Button(comment.getString("ButtonMore"));
		btnArgInfo.setFont(police);
		btnArgInfo.setActionCommand("moreInfo");
		btnArgInfo.addActionListener(argu);
		//add(btnArgInfo,c);

		c.fill = GridBagConstraints.NONE ;
		c.insets = new Insets(0,5,0,5);
		c.anchor = GridBagConstraints.NORTHWEST ;
                c.gridx = 5 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ; c.weightx = 1.0 ; c.weighty = 0.0 ;
		//btnArgDelete = new Button(comment.getString("ButtonDelete"));
		imageActif = central_applet.getImage(
                                central_applet.getCodeBase(),
                                "Image/deleteActifTransp.gif");
                imageInactif = central_applet.getImage(
                                        central_applet.getCodeBase(),
                                        "Image/deleteInactifTransp.gif");
                btnArgDelete = new ImageButton(this,imageActif, imageInactif, comment.getString("ButtonDelete"));
		btnArgDelete.setFont(police);
		btnArgDelete.setActionCommand("delete");
		btnArgDelete.addActionListener(argu);
		add(btnArgDelete,c);

		c.insets = new Insets(0,5,0,5) ;
		//btnLayout = new Checkbox("Automatic Layout");
		imageActif = central_applet.getImage(
                                central_applet.getCodeBase(),
                                "Image/layoutActifTransp.gif");
                imageInactif = central_applet.getImage(
                                        central_applet.getCodeBase(),
                                        "Image/layoutInactifTransp.gif");
//PJ quig never thinks european
                //btnLayout = new ImageCheckbox(this, imageActif, imageInactif, "Automatic Layout");
                btnLayout = new ImageCheckbox(this, imageActif, imageInactif,comment.getString( "ButtonAutoLayout" ) );
		//btnLayout.setActionCommand("doLayout");
		btnLayout.setFont(police);
		btnLayout.addItemListener(this);
		c.anchor = GridBagConstraints.NORTHEAST;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 6 ; c.gridy = 1 ; c.weightx = 0.0 ; c.weighty = 0.0; c.gridwidth =1; c.gridheight = 2;
		//c.insets = new Insets(5,20,0,20);
		if (enableAutoLayout) add(btnLayout,c);


	//PJ 20030515 add quig print
		c.fill = GridBagConstraints.NONE ;
        	c.insets = new Insets(0,5,0,5);
        	c.anchor = GridBagConstraints.NORTHEAST ;
		c.gridx = 7 ; c.gridy = 1 ; c.gridwidth = 1 ; c.gridheight = 2 ; c.weightx = 0.0 ; c.weighty = 0.0 ;
		//btnPrint = new Button(comment.getString("ButtonPrint"));
		imageActif = central_applet.getImage(
                                central_applet.getCodeBase(),
                                "Image/printActifTransp.gif");
                imageInactif = central_applet.getImage(
                                        central_applet.getCodeBase(),
                                        "Image/printInactifTransp.gif");
                btnPrint = new ImageButton(this,imageActif, imageActif, comment.getString("ButtonPrint"));
		btnPrint.setFont(police);
        	btnPrint.setActionCommand("print");
        	btnPrint.addActionListener(this);
        	if (isPrintable == true) add(btnPrint,c);

		oldIndex = -2;
		connectedUsers = new Vector();
		currentUser = null;
		addUser("nobody",false);
		// Need to be a parameter


		if(standalone == true ) {
			if( standaloneUsers != null ) {
				try {
					StringTokenizer st = new StringTokenizer(standaloneUsers,",;:");
     					while (st.hasMoreTokens()) {
						addUser(st.nextToken(),true);
     					}
				}
				catch( NoSuchElementException e ) {}
			}
			else {
				addUser("Jules",true);
				addUser("Julie",true);
			}
		}
		validate();
	}

	public void start(){
		//Normalement, comme tout module qui se respecte, il commence par lancer 
		//un "syn" au serveur pour se synchroniser. 
		//Cele se passe dans Drew.Client.TableauDeBord.CentreDeConnexion

		refaireListeUsers();

		if (needParticipate == true) {
			//Bad code copied from actionPerformed(), see below
			avertir( new XMLTree( "engaged").setAttribute("status", "true") );
		} else resetCurrentUser();
	}

	/** Arret du module */
	public void stop(){
		if (getUser(central_applet.nom) != null) {
			//pj 20011026
			//removeUser(central_applet.nom);
			//XML pj 20021128
			//avertir("engagedUser", "false");
			if (!standalone) avertir( new XMLTree( "engaged").setAttribute("status", "false"));
		}

		/* PJ 05-2001
		   if (isShowing()) {
		   dispose();
		   }
		   */
	}

	/** Mort du module */
	public void destroy(){
		stop();
	}

	/**
	 * effacer les donnï¿½es du module
	 */
	public void clear()
	{
		argu.clear();
		refaireListeUsers();
	}

	/** Identification du module */
	public String getCode(){
		return CODE;
	}

	/**
	 * le module indique s'il est destinataire ou non du message 
	 * identifier par <CODE>code</CODE>
	 */
	public boolean messageFilter(XMLTree data)
	{
		return true;
	}

	/**
	 * Perform command on event
	 */
	public void messageDeliver(String user, XMLTree data){
		if( getCode().equals( data.tag() ) ) {
			Object m;

			//PJ
			String player = data.getAttributeValue("player");
			if (player == null) {
				player = user;
			}
			else if( standalone ) {
				user = player;
			}

			// if user doesn't exist, we add it
			if (getUser(user) == null) addUser(user,!standalone);

			for (Enumeration e = data.elements() ; e.hasMoreElements() ;) {
				m = e.nextElement();
				if( !( m instanceof XMLTree ) ) continue;
				argu.performCommand( user, (XMLTree)m );
			}
		}
	}

	public void avertir(XMLTree cmd){
	    	XMLTree x;
	    	if (standalone) {
			x = new XMLTree( CODE , 
					XMLTree.Attributes("player",getCurrentUser().getUserName()) , 
					XMLTree.Contents()
				);
		}
	    	else {
			x = new XMLTree ( CODE );
		}
	    	x.add( cmd );
	    	central_applet.envoiserveur( x );
	}

	public void avertir(XMLTree cmd1, XMLTree cmd2){
		XMLTree cmd;
	    	if (standalone) {
			cmd = new XMLTree( CODE , 
					XMLTree.Attributes("player",getCurrentUser().getUserName()) , 
					XMLTree.Contents()
				);
		}
	    	else {
			cmd = new XMLTree( CODE );
		}
		cmd.add( cmd1 ); cmd.add( cmd2 ); 
		central_applet.envoiserveur( cmd );
	}

	public void avertir(XMLTree cmd1, XMLTree cmd2, XMLTree cmd3){
		XMLTree cmd;
		if (standalone) {
	    		cmd = new XMLTree( CODE , 
					XMLTree.Attributes("player",getCurrentUser().getUserName()) , 
					XMLTree.Contents()
				);
		}
		else {
			cmd = new XMLTree( CODE );
		}
		cmd.add( cmd1 ); cmd.add( cmd2 ); cmd.add( cmd3 );
		central_applet.envoiserveur( cmd );
	}

	public String getRealUserName() {
		return central_applet.nom;
	}

	/** methode pour savoir si tel utilisateur est deja enregistre
	 * dans le debat. Renvoie l'utilisateur ou null selon le
	 * cas. Methode publique pour permettre a d'autres modules (dont
	 * le tableau de bord) d'introduire des participants.
	 */
	public User getUser(String name){
		User u;
		int i;
		//PJ 20031025
		if( connectedUsers == null || name==null ) return null;
		for (i = 0; i< connectedUsers.size() ; i++){
			u = (User) connectedUsers.elementAt(i);
			if (u.getUserName()!=null && u.getUserName().compareTo(name) == 0) return u;
		}
		return null;
	}

	EditUsersDialog dialog;

	void editerUsers(){
        	if (dialog == null) dialog = new EditUsersDialog(this);
        	dialog.setEnabled(true);       
        	dialog.setVisible(true);
        	refaireListeUsers();
        	argu.repaint();
	}


	/** Methode permettant a quiconque d'ajouter de nouveaux
	 * participants */
	public void addUser(String name, boolean engaged){
		User u = getUser(name);
		if (u == null) {
			int rang = 0;
			if (!connectedUsers.isEmpty()) {
				User last = (User) connectedUsers.lastElement();
				rang = last.getNumber() +1;
			}
			u = new User(name, rang, engaged);
			connectedUsers.addElement(u);
		}
		else u.setUserEngaged(engaged);

		//Appel necessaire avant setCurrentUser pour que le UserLabel soit cree 
		refaireListeUsers();
	}

	/** Methode permettant a quiconque de retirer des participants */
	public void removeUser(String name){
		User u = getUser(name);
		if (u != null) {
			if (editUsers) {
				if (u.equals(currentUser)) setCurrentUser(null);
				connectedUsers.removeElement(u);	
			} 
			else u.setUserEngaged(false);

			refaireListeUsers();
		}
	}

	// need to be public, used in Argumentation.performCommand( XMLTree ) 
	public void engagedUser(String name, String state){
		boolean engaged = (state.compareTo("true") == 0);
		User u = getUser(name);

		if (u == null) {
			addUser(name, engaged);
			u = getUser(name);
		}
		else if (u.getUserEngaged() != engaged) {
			u.setUserEngaged(engaged);
		}

		if (u.equals(currentUser) && !(engaged)) {
			setCurrentUser(null);
		}

		if (isInteractive() && name.equals(central_applet.nom)) {
			if (engaged) {
				setCurrentUser(u);

				btnToggleMode.setTip(comment.getString("getPassive"));
				btnToggleMode.setSelected(true);
				btnToggleMode.repaint();
			} else {
				btnToggleMode.setTip(comment.getString("getActive"));
				btnToggleMode.setSelected(false);
				btnToggleMode.repaint();
			}
		}

		if (currentUser == null) resetCurrentUser();
	}

	public void imprimer(){
        	/* creer une fenetre */
        	/*
		PrintWindow printWindow = new PrintWindow(argu,lPrint);
        	printWindow.imprimer();
		*/

		PrintJob p;     
        	try {                   
            		p = getToolkit().getPrintJob(this.getWindowParent(), "Print", new java.util.Properties());
            		if (p != null) {            
                		Graphics pg = p.getGraphics();
                		if (pg != null) {
					System.out.println("Start printing process...");
					argu.directPaint(pg, print_scale);
                    			pg.dispose();
				}
				else System.out.println("No page to print");
				System.out.println("Printing details...");
				argu.printDetails(p);
                		p.end();
                		System.out.println("Printing process ended");
            		}
		} catch (Exception e) {
            		System.out.println("Unable to print : " + e);
            		p = null;
        	}

    	}
}

