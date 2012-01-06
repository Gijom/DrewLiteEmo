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
 * File: MoreInfo.java
 * Author: Matthieu Quignard
 *
 * $Id: MoreInfo.java,v 1.28.2.1 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;
import java.awt.List; // confusion java.awt.List et java.util.List
import java.awt.event.*;
import java.util.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

/**
  Fenetre d'edition du contenu des boites/fleches, des commentaires
  et de visualisation des participants pour et contre. 
  */
public class MoreInfo
	extends Dialog
	implements ActionListener, ItemListener {
	
	/**
	 * La fenÔøΩtre ÔøΩ laquelle ce Dialog est rattachÔøΩ. 
	 * En effet, ce Dialog bloque temporairement toute action dans la 
	 * fenÔøΩtre parente...
	 */
	private Grapheur parent;

	private Drew.Util.Locale comment ;
	
	private Map<String,String> relationtext;

	private CardLayout cl = new CardLayout() ;

	/** Panneau contenus Argument (CardLayout)*/
	private Panel pContent;

	/** Panneau contenus Boites*/
	private Panel pContentB;

	/** Panneau contenus Fleches*/
	private Panel pContentF;

	/** Panneau General */
	private Panel pInfo;

	/** Panel boutons */
	private Panel pBoutons;

	/** Etiquette figurant le titre de la zone de contenu de la boite
	*/
	private Label labelBoite;

	/** Etiquette figurant le titre de la zone de contenu de la fleche
	*/
	private Label labelFleche;

	/** Etiquette figurant le titre de la zone de commentaire
	*/
	private Label labelComment;

	/** Etiquette figurant le titre de la liste des pour
	*/
	private Label labelPro;

	/** Etiquette figurant le titre de la liste des contre
	*/
	private Label labelContra;

	/** Liste affichant les utilisateurs pro
	*/    
	private List listPro;

	/** Liste affichant les utilisateurs contra
	*/    
	private List listContra;

	/** Champ de texte pour le contenu de la boite
	*/        
	private TextArea textBoite;

	/** menu dÔøΩroulant pour le choix du type de fleche
	*/     
	private Choice menuFleche;

	/** texte de  la fleche (fait que le texte de la flèche n'est plus un menu)
	*/     
	private TextField textFleche;
	public static boolean FlecheAsText = false;


	/** Champ de texte pour le commentaire
	*/        
	private TextArea textComment;

	/** menu dÔøΩroulant pour le choix du type de fleche
	*/     
	//private Choice choixFleche;

	/** Bouton Save pour enregistrer les changements 
	  @see #saveEntry()
	  */        
	private Button saveBouton;

	/** Bouton Cancel pour annuler les changements et/ou la crÔøΩation
	  @see #cancelEntry()
	  */    
	private Button cancelBouton;

	/** Bouton Close pour fermer le Dialog
	*/    
	private Button closeBouton;

	private Label labelOpinion;
	private Checkbox cbPro;
	private Checkbox cbContra;

	/** l'argument dont il est question */
	Argument currentArg;

	/** l'utilisateur qui a declenche l'ouverture de cette fenetre */
	User currentUser = null;

	/** Statut de l'utilisateur : a-t-il le droit de donner son avis */
	boolean itsMe = false;

	/** l'ID du dernier argument cree */
	String currentID;

	/** l'etat de l'argument sous la forme d'un XMLTree-argument */
	private XMLTree state;

	private boolean isaBox;

	private boolean createNewArg;

	/** Constructeur du dialog box.
	  Charge le constructeur generique de Dialog avec des parametres
	  fixes.
	  @param f la fenÔøΩtre parente (Grapheur), paralysÔøΩe durant toute la
	  visualisation de la boite.
	  */
	public MoreInfo(Grapheur f) {
		/* PJ 05-2001 */
		super( f.getWindowParent(),"En savoir plus...",true);
		this.parent = (Grapheur) f;
		this.comment = parent.comment;

		relationtext = new HashMap<String,String>();
		
		if ("conceptgraph".equals(System.getProperty("drew.graph.mode"))) {
			relationtext.put(comment.getString("LabelRelationIsA"),
					comment.getString("SymRelationIsA"));	
			relationtext.put(comment.getString("LabelRelationComposedOf"),
					comment.getString("SymRelationComposedOf"));
			relationtext.put(comment.getString("LabelRelationDepends"),
					comment.getString("SymRelationDepends"));	
			relationtext.put(comment.getString("LabelRelationDesignates"),
					comment.getString("SymRelationDesignates"));			
		} else {
			relationtext.put(comment.getString("LabelRelation0"),
					comment.getString("SymRelation0"));
			relationtext.put(comment.getString("LabelRelation3"),
					comment.getString("SymRelation3"));
			relationtext.put(comment.getString("LabelRelation5"),
					comment.getString("SymRelation5"));
//			menuFleche.add(comment.getString("LabelRelation0"));	// ?
//			menuFleche.add(comment.getString("LabelRelation3"));	// +
//			menuFleche.add(comment.getString("LabelRelation5"));
		}

		
		
		setTitle(comment.getString("ButtonMore"));
		setBackground(parent.getBackground());

		GridBagLayout gridbag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();

		pContent = new Panel(cl);

		/* First card of the CardLayout: boxes */
		pContentB = new Panel(new BorderLayout(0,0));
		labelBoite = new Label("1. " + comment.getString("MILabelBox"));
		pContentB.add("North", labelBoite);
		textBoite = new TextArea("", 4, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
		textBoite.setBackground(new Color(Argument.selectedColor)) ;	// Same background as a box
		textBoite.setForeground(Color.black) ;	// Same color as a selected box
		pContentB.add("South", textBoite);
		pContent.add(pContentB, "boite");

		/* Second card of the CardLayout: arrows */
		pContentF = new Panel(new FlowLayout(FlowLayout.LEFT));
		labelFleche = new Label("1. " + comment.getString("MILabelRelation"));
		pContentF.add(labelFleche);

		//PJ for marije 20041117
		textFleche = new TextField(comment.getString("SymRelation0"), 20); 
		menuFleche = new Choice(); 

		/** PJ 20030202
		menuFleche.add(comment.getString("LabelRelation0"));
		menuFleche.add(comment.getString("LabelRelation1"));
		menuFleche.add(comment.getString("LabelRelation2"));
		menuFleche.add(comment.getString("LabelRelation3"));
		menuFleche.add(comment.getString("LabelRelation4"));
		menuFleche.add(comment.getString("LabelRelation5"));
		menuFleche.add(comment.getString("LabelRelation6"));
		menuFleche.add(comment.getString("LabelRelation7"));
		*/
		// Miika ask just for + ? and -, no more
//		menuFleche.add(comment.getString("LabelRelation0"));	// ?
//		menuFleche.add(comment.getString("LabelRelation3"));	// +
//		menuFleche.add(comment.getString("LabelRelation5"));	// -
		
		// the dropdown of comments is the keys in relationtext -- gdyke
		for (String s : relationtext.keySet()) {
			menuFleche.add(s);
		}
		
		//PJ for marije 20041117
		if( FlecheAsText == true )
			pContentF.add(textFleche);
		else 
			pContentF.add(menuFleche);

		pContent.add(pContentF,"fleche");

		/* The first vertical part: content of box or arrows */
		pInfo = new Panel(gridbag);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 2;
		c.insets = new Insets(0,5,5,5);
		gridbag.setConstraints(pContent,c);
		pInfo.add(pContent);

		/* The second vertical part: info about the argument */
		c.weightx = 0.5;
		c.gridy = 2;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.insets = new Insets(5,5,0,5);
		labelComment = new Label("2. " + comment.getString("MILabelComment"));
		gridbag.setConstraints(labelComment,c);
		pInfo.add(labelComment);

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 3;
		c.gridheight = 3;
		c.insets = new Insets(0,5,0,5);
		textComment = new TextArea("", 3, 40, TextArea.SCROLLBARS_VERTICAL_ONLY);
		textComment.setBackground(Drew.Client.Grapheur.Tips.background) ;
		gridbag.setConstraints(textComment,c);
		pInfo.add(textComment);

		/* Third vertical part: the list of participants pro and contra */
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 6;
		c.insets = new Insets(10,5,0,5);
		//labelPro = new Label(comment.getString("MILabelPro"), Label.CENTER);
		cbPro = new Checkbox(comment.getString("LabelPro"),true);
		cbPro.addItemListener(this);
		gridbag.setConstraints(cbPro,c);
		pInfo.add(cbPro);
		cbPro.setVisible(!parent.hideAttitudes);

		c.gridx = 2;
		c.gridy = 6;
		c.insets = new Insets(10,5,0,5);
		//labelContra = new Label(comment.getString("MILabelContra"), Label.CENTER);
		cbContra = new Checkbox(comment.getString("LabelContra"),false);
		cbContra.addItemListener(this);
		gridbag.setConstraints(cbContra,c);
		pInfo.add(cbContra);
		cbContra.setVisible(!parent.hideAttitudes);

		//Listes :
		//3 items visibles par dÔøΩfauts, non redimensionable.	
		c.gridx = 0;
		c.gridy = 7;
		c.insets = new Insets(0,5,0,5);
		listPro = new List(4,false);
		listPro.setBackground(parent.argu.getBackground());
		gridbag.setConstraints(listPro,c);
		pInfo.add(listPro);
		listPro.setVisible(!parent.hideAttitudes);

		c.gridx = 2;
		c.gridy = 7;
		listContra = new List(4,false);
		listContra.setBackground(parent.argu.getBackground());
		gridbag.setConstraints(listContra,c);
		pInfo.add(listContra);
		listContra.setVisible(!parent.hideAttitudes);
		
		Button btnResize = new Button("Resize");
		btnResize.addActionListener(this);
		btnResize.setActionCommand("resize");

		/* Fourth vertical part: buttons to save or quit */
		pBoutons = new Panel(new GridLayout(1,2));

		cancelBouton = new Button(comment.getString("ButtonCancel"));
		cancelBouton.addActionListener(this);
		cancelBouton.setActionCommand("Cancel");
		pBoutons.add(cancelBouton);

		saveBouton = new Button(comment.getString("ButtonSave"));
		saveBouton.addActionListener(this);
		saveBouton.setActionCommand("Save");
		pBoutons.add(saveBouton);

		/* pj-05-2001, fait chier d'appuyer sur deux boutons, on enregistre et on ferme
		   closeBouton = new Button(comment.getString("ButtonClose"));
		   closeBouton.addActionListener(this);
		   closeBouton.setActionCommand("Close");
		   pBoutons.add(closeBouton);
		   */

		this.setLayout(new BorderLayout(5,5));
		add(pInfo,"North");
		//add(btnResize,"Center");
		add(pBoutons,"South");
		validate();
		pack();
	}
/* PJ 20040301 size is never good
	public Dimension getPreferredSize() {
                return new Dimension(350, 360);   
        }

	
	public Dimension getMinimumSize() {     
                return new Dimension(250, 360);
        }
*/
	Argument getCurrentArgument() {
		return currentArg;
	}

	/** Action du bouton Cancel : Annuler l'ÔøΩdition de l'utilisateur
	  courant.
	  */
	private void cancelEntry() {
		parent.argu.setStateFromMoreInfo(null);
	}

	/** Action du bouton Save : enregistrer les nouveaux attributs de l'argument.
	  */
	private void saveEntry() {
		String lang = Config.getLocale().getLanguage();
		state = null;

		if (createNewArg) {
		//Il faut creer un nouvel argument
			if (currentArg == null) currentID = parent.argu.getUniqID();
			else currentID = currentArg.getId();

			String opinion = "true";
			String argType;

			if (isaBox)  argType = "box";
			else argType = "arrow";

			if (cbPro.getState()) opinion = "true";
			else if (cbContra.getState()) opinion = "false";
			else opinion = "none";

			state = new XMLTree( "argument" ,
                                        XMLTree.Attributes("id", currentID, "action", "create", "type", argType, "support", opinion ),
                                        XMLTree.Contents(
                                                new XMLTree("name",
                                                        new XMLTree("text",
                                                                XMLTree.Attributes( "lang",lang ),
                                                                XMLTree.Contents(updateArgumentName())
                                                                )
                                                        ),
                                                new XMLTree("comment",
                                                        new XMLTree("text",
                                                                XMLTree.Attributes( "lang",lang ),
                                                                XMLTree.Contents(textComment.getText())
                                                                )
                                                        )
                                                )
                              );
		} else {
		//On modifie l'argument courant	
		String opinionType = "none";
		String opinion = "";

		//On regarde si l'utilisateur a change d'opinion
		if (cbPro.getState() && !currentArg.isSupporter(currentUser)) {
			//u devient Supporter
			opinionType = "support";
			opinion = "true";
		}
		else if (cbContra.getState() && !currentArg.isChallenger(currentUser)) {
			//u devient Challenger
			opinionType = "support";
                        opinion = "false";
		}
		else if (!cbContra.getState() && currentArg.isChallenger(currentUser)) {
			//u n'est plus Challenger mais n'est pas devenu supporter
			opinionType = "support";
                        opinion = "none";
		}
		else if (!cbPro.getState() && currentArg.isSupporter(currentUser)) {
                        //u n'est plus Supporter mais n'est pas devenu challenger
			opinionType = "support";
                        opinion = "none";
                }

		currentID = currentArg.getId();

		if (opinionType == "none") //Il n'a pas change d'opinion
			state = new XMLTree( "argument" , 
				XMLTree.Attributes( "id", currentID ),
				XMLTree.Contents(
					new XMLTree("name",
						new XMLTree("text", 
							XMLTree.Attributes( "lang",lang ),
							XMLTree.Contents(updateArgumentName()) 
							)
						),
					new XMLTree("comment", 
						new XMLTree("text", 
							XMLTree.Attributes( "lang",lang ),
							XMLTree.Contents(textComment.getText())
							) 
						)
					)
			      );
		else // Il a change d'opinion
			state = new XMLTree( "argument" ,
                                        XMLTree.Attributes( "id", currentID, opinionType, opinion ),
                                        XMLTree.Contents(
                                                new XMLTree("name",
                                                        new XMLTree("text",
                                                                XMLTree.Attributes( "lang",lang ),
                                                                XMLTree.Contents(updateArgumentName())
                                                                )
                                                        ),
                                                new XMLTree("comment",
                                                        new XMLTree("text",
                                                                XMLTree.Attributes( "lang",lang ),
                                                                XMLTree.Contents(textComment.getText())
                                                                )
                                                        )
                                                )
                              );
		} // Fin du test
		parent.argu.setStateFromMoreInfo(state);
	}

	String updateArgumentName() {
		String s = "NO NAME ??";
		if (isaBox) s = textBoite.getText();
		else {
			// PJ for marije 20041117
			if( FlecheAsText == false ) {
				s = relationtext.get(menuFleche.getSelectedItem());
			}
			else s = textFleche.getText();
		}
		return s;
	}

	/** Mise a jour des listes des pour et des contre
	*/
	private void updateLists(){
		int i;
		User u,v;
		Vector liste;

		if (listPro.getItemCount() > 0) listPro.removeAll();
		if (listContra.getItemCount() > 0) listContra.removeAll();

		u = currentUser;
		if (u != null) {			
			if (cbPro.getState()) listPro.add(u.getUserName());
			if (cbContra.getState()) listContra.add(u.getUserName());
		}  

		if (currentArg != null) {
			liste = currentArg.getPros();
			for (i=0 ; i<liste.size() ; i++){
				v = (User) liste.elementAt(i);   
				if (!v.equals(u)) listPro.add(v.getUserName());
			}

			liste = currentArg.getCons();
			for (i=0 ; i<liste.size() ; i++){
				v = (User) liste.elementAt(i);   
				if (!v.equals(u)) listContra.add(v.getUserName());
			}
			validate();
		}
	}

	XMLTree getArgState(Argument a) {
		if ((a != null) && (a.getId() == currentID)) return state;
		else return null;
	}

	/** Gestion des actions faites sur les boutons.
	*/
	public void actionPerformed(ActionEvent event) {
		if (event.getActionCommand() == "Save") {
			saveEntry();
			setVisible(false);
		}
		else if (event.getActionCommand() == "Cancel") {
			cancelEntry();
			setVisible(false);
		}		
		else if (event.getActionCommand() == "resize") {
			if (currentArg != null) currentArg.autoResize();
		}
		else if (event.getActionCommand() == "Close") {
			setVisible(false);
		}
	}

	public void itemStateChanged(ItemEvent e) {
                Component choice = (Component) e.getItemSelectable();

                if (choice.equals(cbPro)) {
			if (e.getStateChange() == e.SELECTED) cbContra.setState(false);
			updateLists();
		} else if (choice.equals(cbContra)) {         
                        if (e.getStateChange() == e.SELECTED) cbPro.setState(false);
                        updateLists();
                }
	}
		

	/** Met a jour la description de la boite/fleche quand celle-ci a change alors
	  * que la fenetre MoreInfo etait ouverte */
	void update(Argument a) {
		if (a == null) return;
		boolean isaBox = (a instanceof Boite);

		if (!isaBox) setMenuFleche(a.getName());
                else textBoite.setText(a.getName());
                
		textComment.setText(a.getComment());
                
                updateLists();
	}

	/** Met a jour la description de la boite/fleche en prenant les valeurs par
	 *  defaut quand il s'agit d'un nouvel argument, ou les valeurs actuelles sinon
	 *  Cette methode est appelee exclusivement par l'utisateur courant au lancement
	 *  de MoreInfo. Les mises a jour provenant d'autres joueurs se font par update(Argument a).
	 *  Cette methode met a jour l'attribut global 'currentUser' et 'itsMe'.
	 */
	void update(Argument a, boolean isaBox, boolean editable, boolean createNew){
		this.currentArg = a;
		this.isaBox = isaBox;
		state = null;
		this.createNewArg = createNew;

		currentUser = parent.getCurrentUser();
		itsMe = parent.getItsMe();

		cbPro.setVisible( ( !parent.hideAttitudes ) && (currentUser != null));
		cbContra.setVisible( ( !parent.hideAttitudes ) && (currentUser !=null));

		if (createNewArg) {// Someone is creating a box/node. Set default values
			textBoite.setText(comment.getString("NewArgument"));
			menuFleche.select(comment.getString("LabelRelation0"));
			textFleche.setText(comment.getString("SymRelation0"));
			textComment.setText(comment.getString("CommentPrompt"));
			cbPro.setState(true);
			cbContra.setState(false);
			if (isaBox) cl.show(pContent, "boite");
	                else cl.show(pContent, "fleche");
		}
		else {
			if (!isaBox) setMenuFleche(a.getName());
			else textBoite.setText(a.getName());
			
			textComment.setText(a.getComment());
			if (currentUser != null) {
				cbPro.setState(currentArg.isSupporter(currentUser));
				cbContra.setState(currentArg.isChallenger(currentUser));
			}
		
		}
		if (isaBox) cl.show(pContent, "boite");
		else cl.show(pContent, "fleche");

		updateLists();
		affichage(editable);
		validate();
		pack();
	}

	private void affichage(boolean editable){
		// Si c'est moi qui ai ouvert la fenetre le premier, je peux modifier
		menuFleche.setEnabled(editable);
		textFleche.setEnabled(editable);
		textBoite.setEditable(editable);
		textComment.setEditable(editable);

		//Si c'est moi, je peux donner mon avis
		cbPro.setEnabled(itsMe);
		cbContra.setEnabled(itsMe);
		saveBouton.setEnabled(itsMe);
	}

	//0 c'est par defaut !
	private void setMenuFleche(String sym){
		if (sym.equals(comment.getString("SymRelation1")))
			menuFleche.select(comment.getString("LabelRelation1"));
		else if (sym.equals(comment.getString("SymRelation2")))
			menuFleche.select(comment.getString("LabelRelation2"));
		else if (sym.equals(comment.getString("SymRelation3")))
			menuFleche.select(comment.getString("LabelRelation3"));
		else if (sym.equals(comment.getString("SymRelation4")))
			menuFleche.select(comment.getString("LabelRelation4"));
		else if (sym.equals(comment.getString("SymRelation5")))
			menuFleche.select(comment.getString("LabelRelation5"));
		else if (sym.equals(comment.getString("SymRelation6")))
			menuFleche.select(comment.getString("LabelRelation6"));
		else if (sym.equals(comment.getString("SymRelation7")))
			menuFleche.select(comment.getString("LabelRelation7"));
		else 
			menuFleche.select(comment.getString("LabelRelation0"));
	
		//PJ for marije 20041117
		textFleche.setText( sym );
	}
}
