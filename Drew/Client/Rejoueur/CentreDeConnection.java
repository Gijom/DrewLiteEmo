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
 * File: CentreDeConnection.java
 * Author:
 * Description:
 *
 * $Id: CentreDeConnection.java,v 1.32.2.3 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.*;
import java.awt.*;
import java.awt.event.*;

import java.util.StringTokenizer;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;

import Drew.Client.Util.*;

/**
 * C'est la classe a appeler dans un fichier html (attention c'est une applet)
 * pour lancer le programme de rejoueur de Drew.
 * Elle gere la creation des modules partages (Chat,WhiteBoard,..)
 * permettant de visualiser les actions sur ces memes modules
 * a partir des traces (fichier) generees par un serveur GrandSioux
 * lors d'une session de discussion Drew.
 * L'utilisation de cette classe necessite le lancement d'un serveur Sioux ou d'un
 * serveur ayant le meme protocole de communication et la meme fonction que ce
 * dernier.
 *
 * Extension Quig pour avoir une interface plus interactive avec la personne qui
 * veut rejouer cela en local (sur son disque).
 *
 * @see   Drew.Serveur.GrandSioux
 * @see   Drew.Serveur.Sioux
 * @see   Drew.Client.Rejoueur.Rejoueur
 * @see   Drew.Client.WhiteBoard.WhiteBoard
 * @see   Drew.Client.Chat.Chat_fenetre
 */
public class CentreDeConnection extends Communication implements ActionListener, ItemListener {

	/** Classe a l'ecoute des messages du serveur */
	private ConnectionEcoute ecoute;

	/** nom du sujet de l'historique a rejouer */
	public String sujet;

	/** encodage utilisé pour le fichier de trace */
	public String encoding = null;

	/** URL representant le sujet de l'historique a rejouer */
	public URL url;

	/** Pour une ecoute selective: piece a ecouter */
	public String room;

	/** nom du server d'ou vient l'applet  */

	//public  String host;

	/** pour ecouter toutes les pieces, attribuer cette valeur constante a room */
	public static final String allrooms="toutes les pieces";

	/** module de controle et de commande du rejoueur */
	public Rejoueur frame_tab_de_bord;

	/**
	 * liste des personnes connectees par piece (geree dynamiquement),
	 * key = nom du participant,
	 * value = piece est situe le participant.
	 */
	public Hashtable listeParticipants= new Hashtable();

	/**
	 * liste des pieces (geree dynamiquement),
	 * key = nom de la piece,
	 * value = "" (pas utilise).
	 */
	public Hashtable listePiece= new Hashtable();

	/**
	 * Receptacle a modules, seront ajoute ici les modules reclamï¿½s dans les parametres
	 * de l'applet. La liste des modules utilisables est spï¿½cifiee dans Drew.Client.Util.Config
	 *
	 * @see Drew.Client.Util.Config
	 */
	CooperativeModuleList modules ;

	String twomodules = "chat,grapheur";
	String twomodulesorientation = "horizontal";
	String graphprintscale = "0.7";
	String locale;
	String defaultFileName=null;

	/**
	 * pour obtenir les messages "localises"
	 */
	Drew.Util.Locale comment;

	FileDialog fd;
	Vector usedMods;
	Vector users;
	Vector usedRooms;

	Button btnSelectFile ;
	TextField tfFile;
	Button btnCheckFile;
	Label lbModules;
	List listModules;
	Label lbLocale;
	TextField tfLocale;
	Label lbLayout;
	CheckboxGroup cbg;
	Checkbox cbMulti;
	Checkbox cbDoubleH;
	Checkbox cbDoubleV;
	Checkbox cbSingle;

	Label roomLabel;
	List roomList;
	Label userLabel;
	List userList;

	Button btnReplay;
	Label lbGraphPrintScale;
	TextField tfGraphPrintScale;

	boolean withSlider = false;
	private boolean filedialog = false;
	int trace_length;
	int current_event;

	/* Liste des modules elementaires pouvant etre combines ensemble dans des fenetres comme multi, 2modules etc.
	 * C'est une sous-liste de Config. Tous les mots de cette liste doivent correspondre exactement a un nom de module
	 * declare dans Drew.Client.Util.Config T[][]. Sinon gros bug.
	 */
	// not used anymore since we use the drew.client.util.config to list all availible modules
	//    static String MOD[]= { "chat", "texte", "whiteboard", "grapheur", "alex", "vote", "feutricolore" } ;

	public CentreDeConnection(){
		usedMods = new Vector();
		users = new Vector();
		usedRooms = new Vector();
	}

	// called to fill in the filename textfilled with a filename
	public CentreDeConnection(String fileName) {
		defaultFileName=fileName;
		usedMods = new Vector();
		users = new Vector();
		usedRooms = new Vector();
	}

	private void buildGUI() {
		Panel panel = new Panel(); //A panel to modulize interface
		if( filedialog == true ) { //Pj, don't works with explorer when using local applet
			//fd = new FileDialog(new Frame(), "Select your trace file", FileDialog.LOAD);
			fd = new FileDialog(new Frame(), comment.format("panel.filedialog"), FileDialog.LOAD);
		}

		if( filedialog == true ) {
			btnSelectFile = new Button(comment.format("panel.filedialog"));
			btnSelectFile.setActionCommand("loadfile");
			btnSelectFile.addActionListener(this);
		}

		tfFile = new TextField("",50);
		tfFile.setBackground(Color.white);

		btnCheckFile = new Button(comment.format("panel.check"));
		btnCheckFile.setActionCommand("checkfile");
		btnCheckFile.addActionListener(this);

		lbModules = new Label(comment.format("panel.modules"), Label.CENTER);
		listModules = new List(4,true); //3 lines with multiple selection
		listModules.setBackground(Color.white);
		listModules.addItemListener(this);

		lbLocale = new Label(comment.format("panel.locale"), Label.RIGHT);
		tfLocale = new TextField("",10);
		tfLocale.setBackground(Color.white);

		lbLayout = new Label(comment.format("panel.layout"), Label.CENTER);
		cbg = new CheckboxGroup();
		cbMulti = new Checkbox(comment.format("panel.all"), cbg, true);
		cbDoubleH = new Checkbox(comment.format("panel.horizontal"), cbg, false);
		cbDoubleV = new Checkbox(comment.format("panel.vertical"), cbg, false);
		cbSingle = new Checkbox(comment.format("panel.separate"), cbg, false);

		lbGraphPrintScale = new Label(comment.format("panel.print.scale"), Label.RIGHT);
		tfGraphPrintScale = new TextField("",10);
		tfGraphPrintScale.setBackground(Color.white);

		btnReplay = new Button(comment.format("panel.replay"));
		btnReplay.setActionCommand("go");
		btnReplay.addActionListener(this);

		roomLabel = new Label(comment.format("panel.rooms"),Label.CENTER);
		roomList = new List(4, false);
		roomList.addItemListener(this);
		userLabel = new Label (comment.format("panel.participants"), Label.CENTER);
		userList = new List(4, false);

		panel.setLayout(new GridBagLayout());
		panel.setBackground(Color.lightGray);
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(5,5,0,5);
		c.fill = GridBagConstraints.NONE;
		c.weightx = c.weighty = 0.0;
		c.gridy = 0;
		c.gridx = 1; c.anchor = GridBagConstraints.WEST; 	
		if( filedialog == true ) {
			panel.add(btnSelectFile,c);
		}

		c.gridy = 1;
		c.insets = new Insets(0,5,5,5);
		c.gridx = 0; c.anchor = GridBagConstraints.EAST;        panel.add(btnCheckFile,c);
		c.gridx = 1; c.anchor = GridBagConstraints.WEST;
		c.weightx = 1;
		c.gridwidth=3; c.fill = GridBagConstraints.HORIZONTAL;	panel.add(tfFile,c);

		c.gridy = 2; c.gridwidth= 1;
		c.insets = new Insets(5,5,0,5);
		c.gridx = 0; c.anchor = GridBagConstraints.CENTER;	panel.add(lbModules,c);
		c.gridx = 1; 						panel.add(lbLayout,c);
		c.gridx = 2; 						panel.add(roomLabel,c);
		c.gridx = 3; 						panel.add(userLabel,c);

		c.gridy = 3; c.gridheight = 4;
		c.insets = new Insets(0,5,0,5);
		c.fill = GridBagConstraints.BOTH;
		c.gridx = 0; c.anchor = GridBagConstraints.NORTH;       panel.add(listModules,c);
		c.gridx = 2; 						panel.add(roomList,c);
		c.gridx = 3; 						panel.add(userList,c);

		//Add an internediate panel
		Panel cbPanel = new Panel();
		cbPanel.setLayout( new GridBagLayout() );
		c.fill = GridBagConstraints.NONE;
		c.anchor = GridBagConstraints.WEST;
		c.gridheight = 1;
		c.gridy = 0; 
		c.gridx = 0; 					        cbPanel.add(cbMulti,c);
		c.gridy = 1;					        cbPanel.add(cbDoubleH,c);
		c.gridy = 2; 					        cbPanel.add(cbDoubleV,c);
		c.gridy = 3; 					        cbPanel.add(cbSingle,c);

		c.gridy = 3; 
		c.gridx = 1; 
		c.anchor = GridBagConstraints.EAST;
		panel.add( cbPanel, c);
		c.gridy = 7;
		c.insets = new Insets(15,5,5,5);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0; c.anchor = GridBagConstraints.EAST;        panel.add(lbLocale,c);
		c.gridx = 1; c.anchor = GridBagConstraints.WEST;        panel.add(tfLocale,c);
		c.gridx = 2; c.anchor = GridBagConstraints.EAST;        panel.add(lbGraphPrintScale,c);
		c.gridx = 3; c.anchor = GridBagConstraints.WEST;        panel.add(tfGraphPrintScale,c);

		c.gridy = 8; c.gridwidth = 4;
		c.gridx = 0; c.anchor = GridBagConstraints.CENTER;	panel.add(btnReplay,c);
		btnReplay.setEnabled(false);
		/*
	c.gridy = 9; c.weightx = 1 ; c.weighty = 0; c.gridwidth = 4 ; c.gridheight = 4;
	c.fill = GridBagConstraints.HORIZONTAL;
	panel.add(new Label(""),c);
	c.gridy = 13; c.weightx = 1 ; c.weighty = 1; c.gridheight = 1; c.gridwidth = 4;
	c.fill = GridBagConstraints.BOTH;
	panel.add(new Label(""),c);
		 */
		setLayout( new BorderLayout() );
		add( panel, BorderLayout.CENTER );

		frame_tab_de_bord = new Rejoueur("Visualisation d'un historique de session Drew", this);
		frame_tab_de_bord.setEnabled(false);
		add( frame_tab_de_bord, BorderLayout.SOUTH);

		//validate GUI in init.
	}

	/** Applet Initialisation. Read applet parameters.
	 */
	public void init() {
		Config.setLocale(this);
		comment = new Drew.Util.Locale("Drew.Locale.Client.Rejoueur", Config.getLocale());

		String dummy = getParameter("debug");
		if( dummy != null ) Config.setDebug( Integer.parseInt(dummy) );
		dummy = getParameter("slider");     
		if( dummy != null ) withSlider = dummy.equalsIgnoreCase("true");
		dummy = getParameter("filedialog");     
		if( dummy != null ) filedialog = dummy.equalsIgnoreCase("true");

		buildGUI();

		//Update Fields with applet parameters
		tfFile.setText(getParameter("trace"));
		tfLocale.setText(getParameter("locale"));
		tfGraphPrintScale.setText(graphprintscale);


		//Update module lists
		updateModuleList();

		validate();

		// drewLite insert
		// update filename textfield if a filename was specified
		if(defaultFileName != null) {
			tfFile.setText(defaultFileName);
			this.checkTraceFile();
		}
		// TODO : we hide interface for tatiana replay, but we still want the replayer interface for 'normal' drew usage
	}

	public void resetEvent() {
		current_event = 0;
		updateSliders();
	}

	public void nextEvent() {
		current_event++;
	}

	public void updateSliders() {
		if( Config.getDebug() ) System.err.println("Update Slider position to "+current_event);
		if (frame_tab_de_bord != null) frame_tab_de_bord.updateSliders(current_event);
	}

	/* Read applet parameters and put the required modules into the module List
	 * called at init();
	 */
	void updateModuleList() {
		System.err.println("CDC updateModuleList");
		boolean state ;
		String param;

		listModules.removeAll();

		// added by steven
		// old version didn't update MOD with list of modules specified in drew/client/util/config.java
		// (only added module specified on command line
		for( int i=0 ; i<Config.listLength() ; i++ ) {
			listModules.add(Config.getName(i),i);
		}

		// the follwing is not used any more since we fetch module list in drew.client.util.config
		// Ajouter tous les modules sp\xe9cifies dans les parametres
//		for( int i=0; i< MOD.length; i++ ) {
//		param = this.getParameter(MOD[i]);
//		if (param != null) state = param.equalsIgnoreCase("true");
//		else state = false;
//		listModules.add(MOD[i], i);
//		if (state == true) listModules.select(i);
//		}

		//Par defaut, si aucun module n'est parametre, on selectionne le chat
//		if ((MOD.length > 0) && (listModules.getSelectedIndex() == -1 )) {
//		System.out.println("No module selected : choosing chat");
//		listModules.select(0);
//		}

		validate();
		updateLayoutList();
	}

	void updateModuleList(Vector v){
		System.err.println("CDC updateModuleList with vector");
		boolean state ;
		String param;

		listModules.removeAll();
		// added by steven
		// old version didn't update MOD with list of modules specified in drew/client/util/config.java
		// (only added module specified on command line
		for( int i=0 ; i<Config.listLength() ; i++ ) {
			if (v.isEmpty()) state = false;
			else state = v.contains(Config.getName(i));
			listModules.add(Config.getName(i),i);
			System.err.println("listModules adding "+Config.getName(i)+" pos "+i);
			if (state == true) {
				System.err.println("Item in List! selecting it");
				//listModules.select(i);
				for(int j = 0 ; j< listModules.getItemCount() ; j++){
					if( listModules.getItem(j).equals(Config.getName(i)) ) {
						listModules.select(j);
						System.err.println("Selecting item "+i+" "+ listModules.getItem(j));
					}
				}
			}
		}

		// the follwing is not used any more since we fetch module list in drew.client.util.config
		// Ajouter tous les modules sp\xe9cifies dans les parametres
//		for( int i=0; i< MOD.length; i++ ) {
//		if (v.isEmpty()) state = false;
//		else state = v.contains(MOD[i]);
//		listModules.add(MOD[i], i);
//		if (state == true) listModules.select(i);
//		}

		//Par defaut, si aucun module n'est parametre, on selectionne le chat
//		if ((MOD.length > 0) && (listModules.getSelectedItems().length < 1  )) {
//		System.out.println("No module selected : choosing chat");
//		listModules.select(0);
//		} else System.out.println("Modules selected");

		validate();
		updateLayoutList();
	}

	void updateRoomList(Vector v){
		roomList.removeAll();
		userList.removeAll();
		// Ajouter tous les modules sp\xe9cifies dans les parametres
		for( int i=1; i< v.size(); i+=2) {
			String r = (String) v.elementAt(i);
			roomList.add(r);
		}
	}

	void updateUserList(String r) {
		room = r;
		userList.removeAll();
		int roomid = usedRooms.indexOf(r);
		if (roomid >= 0) {
			Vector localUsers = (Vector) usedRooms.elementAt(roomid + 1);
			if (!localUsers.isEmpty()) 
				for (int i=0; i < localUsers.size(); i++) {
					String s = (String) localUsers.elementAt(i);
					userList.add(s);
				}
		} else { //Liste de utilisateurs dans toutes les pieces
			Vector allUsers = (Vector) usedRooms.elementAt(0);
			if (!allUsers.isEmpty())
				for (int i=0; i < allUsers.size(); i++) {
					String s = (String) allUsers.elementAt(i);
					userList.add(s);
				}

		}
	}

	void updateLayoutList(){
		String selectedMods[] = listModules.getSelectedItems();
		int n = selectedMods.length;

		if (n == 0) btnReplay.setEnabled(false);
		else {
			btnReplay.setEnabled(true);
			if (n == 1) cbSingle.setState(true);
			else if (n == 2) {
				if (selectedMods[0].equals("chat")) cbDoubleV.setState(true);
				else cbDoubleH.setState(true);
			} else {
				boolean b = false;
				int i = 0;
				while ((b == false) && (i < selectedMods.length)) {
					if ((selectedMods[i].equals("alex")) || (selectedMods[i].equals("vote")))
						b = true;
					else i++;
				}
				if (b) cbSingle.setState(true);
				else cbMulti.setState(true);
			}
		}
	}

	/* Load and read the trace file. If this is correct, the module list is updated
	 * and we set the most convenient layout. The Replay Button is Enabled.
	 * Called when btnCheckFile is pressed
	 */
	public void checkTraceFile() {
		ConnectionEcoute ec;
		try {
			nom = null;
			sujet = tfFile.getText();
			url = new URL( getCodeBase(), sujet );

			usedMods.removeAllElements();
			users.removeAllElements();
			usedRooms.removeAllElements();
			//Liste globale des participants
			usedRooms.addElement(new Vector());

			ec = new ConnectionEcoute(this);
			System.err.println("CentreDeConnection ecoute started");
			ec.check();

		} catch (Exception ex) {
			System.err.println("Exception : "+ ex.getMessage());
			ex.printStackTrace();
			tfFile.setForeground(Color.red);
			tfFile.setText(ex.toString());
			btnReplay.setEnabled(false);
			closeFrameTabDeBord();
		}
	}

	/* called at the end of trace checking process at EOF */
	void updateParams(int n) {
		trace_length = n;
		current_event = 0;
		updateRoomList(usedRooms);
		updateModuleList(usedMods);
		btnReplay.setEnabled(true);
	}

	void addUsedModule(String s) {
		if ( (usedMods.isEmpty()) || (!usedMods.contains(s)) ) usedMods.addElement(s);
	}

	void addUsedRoom(String s) {
		if (!usedRooms.contains(s)) {
			usedRooms.addElement(s);
			usedRooms.addElement(new Vector()); //Additional place for list of local users
		}
	}

	void addUser(String u, String r) {
		int roomid;
		roomid = usedRooms.indexOf(r);
		if (roomid >= 0) {
			//Ajout dans la liste globale des utilisateurs
			Vector allUsers = (Vector) usedRooms.elementAt(0);
			if ( (allUsers.isEmpty()) || (!allUsers.contains(u)) )
				allUsers.addElement(u);

			//Ajout dans la liste locale des utilisateurs presents dans cette piece
			Vector localUsers = (Vector) usedRooms.elementAt(roomid+1);
			if ( (localUsers.isEmpty()) || (!localUsers.contains(u)) ) 
				localUsers.addElement(u);
		} else {
			System.out.println("This room is unknown : "+r);
			System.out.println("User "+u+" cannot be added");
		}
	}

	void closeFrameTabDeBord() {
		if (frame_tab_de_bord != null) {
			frame_tab_de_bord.setVisible(false);
			remove(frame_tab_de_bord);
			frame_tab_de_bord = null;
		}
	}
	java.util.Locale tryLocale(String loc) {
		StringTokenizer st = new StringTokenizer(loc,"_");
		String l = null,c = "xx",v = "xx";
		l = st.nextToken();
		if(st.hasMoreTokens()) c = st.nextToken();
		if(st.hasMoreTokens()) v = st.nextToken();
		else return new java.util.Locale(l,c);
		return new java.util.Locale(l,c,v);
	}

	/**
	 * Initialisation de l'applet avec creation et affichage du bouton d'appel des elements
	 * graphiques (modules partages Chat, WhiteBoard,... et centre de commande frame_tab_de_bord).
	 * Initialise les listes de participants et de pieces disponibles.
	 * @see #Bentrer
	 * @see #initPieceEtParticipants()
	 */
	public void go() {
		java.util.Locale l = null;
		if (tfLocale.getText() != null && 
				!tfLocale.getText().equals("")) l = tryLocale(tfLocale.getText());
		else l = this.getLocale();
		Config.setLocale(l);
		comment = new Drew.Util.Locale("Drew.Locale.Client.Rejoueur", l);

		if (Double.valueOf( tfGraphPrintScale.getText() ) != null)
			graphprintscale = tfGraphPrintScale.getText() ;



		/*
	GridBagConstraints c = new GridBagConstraints();
	c.gridx = 0; c.gridy = 9; 
	c.gridwidth = 4; c.gridheight = 4; 
	c.weightx = 1; c.weighty = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.NORTH;
	c.insets = new Insets(5,5,5,5);
		 */
		// PJ, to solve GUI bad display
		frame_tab_de_bord.setEnabled(true);
		/*
	if (frame_tab_de_bord != null) remove(frame_tab_de_bord);
	frame_tab_de_bord = new Rejoueur("Visualisation d'un historique de session Drew", this);
	add( frame_tab_de_bord, BorderLayout.SOUTH);
	//add( frame_tab_de_bord, c);
	validateTree();
		 */

		CooperativeModule m;
		modules = new CooperativeModuleList();

		//Construction des modules : selon le layout choisi et les modules selectionnes
		// Ajouter tous les modules selectionnes dans la liste des modules
		
		// degbu for tatiana
		System.out.println("cbMulti state: "+cbMulti.getState() );
		System.out.println("cbSingle state: "+cbSingle.getState() );
		// prefered 1window per drewlet for tatiana
		cbSingle.setState(true);
		
		if (cbMulti.getState()) {
			int j = Config.getIndex("multi");
			m = Config.newInstance(j, this, true, frame_tab_de_bord.Magneto.Clone());
			modules.add(Config.getComment(j), m );

		} else if (cbSingle.getState()) {
			for( int i=0; i< listModules.getItemCount(); i++ ) {
				if (listModules.isIndexSelected(i)) {
					String modname = listModules.getItem(i);
					int j = Config.getIndex(modname);
//					m = Config.newInstance(j, this, true,frame_tab_de_bord.Magneto.Clone());
					// no need for drew remote control in tatiana
					// so 4th argument is null
					m = Config.newInstance(j, this, true,null);
					modules.add(Config.getComment(j), m );
				}
			}
		} else {//2 modules.
			int j = Config.getIndex("2modules");
			String selectedMods[] = listModules.getSelectedItems();
			if (selectedMods.length == 2) {
				twomodules = selectedMods[0].concat(",");
				twomodules = twomodules.concat(selectedMods[1]);
			}
			twomodulesorientation = "horizontal";
			if (cbDoubleV.getState()) twomodulesorientation = "vertical";
			m = Config.newInstance(j, this, true, frame_tab_de_bord.Magneto.Clone()) ;
			modules.add(Config.getComment(j), m );
		}

		modules.init();

		//PJ 20020730 on met directement l'url du sujet, qui s'appele maintenant trace
		sujet = tfFile.getText();

		String dummy = getParameter("encoding");
		if( dummy != null ) {
			encoding = dummy;
		}

		initPieceEtParticipants();

		//On se connecte tout de suite
		connection(null);
	}

	public void start() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : start"); }
	}

	public void stop() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : stop"); }
	}

	public void destroy() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : destroy"); }

		if (ecoute != null) ecoute.end();
		quitter();
	}

	/* Surcharge de Applet.getParameter(String s) pour pouvoir modifier certains parametres
	 * sans toucher au code de l'applet.
	 */
	public String getParameter(String s){
		if (s  == null) return s;
		else if (s.equals("multi.modules")) return twomodules;
		else if (s.equals("multi.orientation")) return twomodulesorientation;
		else if (s.equals("graph.autolayout")) return "true";
		else if (s.equals("graph.print")) return "true";
		else if (s.equals("locale")) {
			if (locale == null) locale = super.getParameter(s);
			if (locale == null) locale = Config.getLocale().getCountry();
			if (locale == null) locale = "en";
			return locale;
		}
		else return super.getParameter(s);
	}

	/**
	 * Fonction appelee lorsqu'on actionne le bouton Bentrer: lancement des modules partages
	 * Chat, WhiteBoard,... et du centre de commande frame_tab_de_bord.
	 * Le bouton Bentrer est desactive.
	 * @see #Bentrer
	 */
	public void actionPerformed(ActionEvent evt) {
		String cmd = evt.getActionCommand();
		if 	(cmd.equals("loadfile")) 	loadfile();
		else if (cmd.equals("checkfile")) 	checkTraceFile();
		else if (cmd.equals("go"))              go();
		else System.out.println("Replay/CDC : unknown command "+cmd);
	}

	public void itemStateChanged(ItemEvent evt) {
		ItemSelectable is = evt.getItemSelectable();
		if (is.equals(listModules)) updateLayoutList();
		else if (is.equals(roomList)) updateUserList(roomList.getSelectedItem());
	}

	/** Open a FileDialog and enable to choose a local trace file to read
	 **/
	public void loadfile() {
		if (fd == null) fd = new FileDialog(new Frame(), "Please choose a file:", FileDialog.LOAD);
		fd.setDirectory(getCodeBase().getFile());
		// fd.show();
		fd.setVisible(true);

		String selectedItem = fd.getFile();
		if (selectedItem != null) {
			File ffile = new File( fd.getDirectory() + File.separator + fd.getFile());
			String protocol = "file:";
			tfFile.setText(protocol.concat(ffile.getAbsolutePath()));
		}
	}

	//---------------------------------------------------------
	// methodes relatives au relations avec le serveur
	//---------------------------------------------------------

	/**
	 * Contacte le serveur et initialise le thread a l'ecoute des messages de ce dernier.
	 * @see #ecoute
	 */
	public void connection(String texte) {
		try {
			nom = texte;
			frame_tab_de_bord.mis_a_jour("Connection","Contact du serveur...");

			//PJ 20020103, le sujet est specifier relativement au serveur
			//PJ 200208730 ou tout ce qui se trouve dans la chaine sujet.

			// url = new URL( getCodeBase().getProtocol(), getCodeBase().getHost(), getCodeBase().getPort(), sujet );
			url = new URL( getCodeBase(), sujet );

			System.err.println( "Load URL : " + url.toString() );
			frame_tab_de_bord.mis_a_jour("Connection","loading " + url.toString());
			ecoute = new ConnectionEcoute(this);
			ecoute.start();

			System.err.println("CentreDeConnection ecoute started");
		}
		catch(MalformedURLException e) {
			System.err.println("Malformed URL : "+e);
		}
		catch (IOException  e) {
			System.err.println("Connection au serveur:"+e);
		}
	}

	/**
	 * Wrapper pour la gestion des commandes depuis le paneau de ctrl
	 */
	public void doCommand( String cmd ) {
		if (cmd.compareTo("RWD") == 0) {
			resetEvent();
		}
		ecoute.doCommand( cmd );
	}

	// doCommand for remote control goto function
	public void doCommand(long timestamp) {
		ecoute.doCommand(timestamp);
	}
	
	public void jump(int i) {
		ecoute.jump(i);
	}

	public void backTo(int i) {
		current_event = 0;
		ecoute.backTo(i);
	}

	/**
	 * Appelee lors d'une deconnection volontaire ou involontaire,
	 * cette methode met le tableau de commande (frame_tab_de_bord) en etat deconnecte.
	 * @see Rejoueur#active_etat_deconnecte()
	 */
	public void reconnection()
	{
		frame_tab_de_bord.active_etat_deconnecte();
	}

	public void deconnection() {
		frame_tab_de_bord.mis_a_jour("Connection", comment.getString("msg001") );
	}

	public void quitter() {
		// si c'est null, alors c'est qu'on etait pas connectï¿½
		if(frame_tab_de_bord != null ) {
			deconnection();
			if(modules != null) modules.destroy();
		}
	}

	//---------------------------------------------------------
	//Manipulation des listes de connection
	//---------------------------------------------------------

	/**
	 * Efface les listes des participants et des pieces disponibles,
	 * puis ajoute allrooms a la liste des pieces disponibles.
	 * @see #allrooms
	 * @see #listeParticipants
	 * @see #listePiece
	 */
	public void initPieceEtParticipants()
	{
		/*
	listeParticipants.clear();
	listePiece.clear();
	listePiece.put(allrooms,"");
		 */
		if (roomList.getSelectedIndex() < 0) room=allrooms;
		else room = roomList.getSelectedItem();
	}

	/**
	 * Affiche la liste des participants dans la zone "info" du tableau de commande
	 * @see Rejoueur#mis_a_jour(String code, String texte)
	 */
	public void afficheliste()
	{
		String texte;
		String clef;
		Enumeration enumer;

		frame_tab_de_bord.mis_a_jour("info_efface","");
		for(enumer = listeParticipants.keys(); enumer.hasMoreElements(); ) {
			clef=(String)enumer.nextElement();
			texte =clef+" ("+listeParticipants.get(clef)+")";
			frame_tab_de_bord.mis_a_jour("info",texte);
		}
	}

	/**
	 * Change l'affectation de piece d'un participant dans la liste des participants
	 * et affiche la nouvelle liste resultante
	 * @param personne nom du participant
	 * @param piece nom de la piece destination
	 * @see #listeParticipants
	 * @see #afficheliste()
	 */
	public void changepiece(String personne, String piece)
	{
		listeParticipants.put(personne,piece);
		afficheliste();
	}

	/**
	 * Enleve une personne de la liste des participants
	 * et affiche la nouvelle liste resultante
	 * @param personne nom du participant
	 * @see #listeParticipants
	 * @see #afficheliste()
	 */
	public void enlevepersonne(String personne)
	{
		listeParticipants.remove(personne);
		afficheliste();
	}

	/**
	 * Ajoute une nouvelle personne de la liste des participants
	 * et affiche la nouvelle liste resultante
	 * @param personne nom du participant
	 * @param piece nom de la piece ou se situe le participant
	 * @see #listeParticipants
	 * @see #afficheliste()
	 */
	public void ajoutpersonne(String personne, String piece)
	{
		listeParticipants.put(personne,piece);
		afficheliste();
	}

	/**
	 * Ajoute une piece dans la liste des pieces disponibles a l'ecoute
	 * et met a jour la selection de ces pieces dans le tableau de commande.
	 * CONDITION : piece n'est ajoutee que si elle n'est pas deja dans la liste
	 * @param piece nom de la piece a ajouter
	 * @see #listePiece
	 * @see Rejoueur#piece_a_ecouter
	 */
	public void ajoutpiece(String piece)
	{
		if (!listePiece.containsKey(piece)) {
			listePiece.put(piece,"");
			frame_tab_de_bord.piece_a_ecouter.addItem(piece);
		}
	}

	/**
	 * Enleve une piece de la liste des pieces disponibles a l'ecoute
	 * et met a jour la selection de ces pieces dans le tableau de commande.
	 * CONDITION : la methode verifie que piece est dans la liste et que plus personne n'y ai
	 * (avec la liste des participants/piece) avant de l'enlever de la liste.
	 * @param piece nom de la piece a enlever
	 * @see #listePiece
	 * @see #listeParticipants
	 * @see Rejoueur#piece_a_ecouter
	 */
	public void enlevepiece(String piece)
	{
		// On ne peut supprimer une piece que si il n'y a plus personne dans celle-ci
		if ((listePiece.containsKey(piece))&&(!listeParticipants.contains(piece))) {
			frame_tab_de_bord.piece_a_ecouter.remove(piece);
			listePiece.remove(piece);
		}
	}
	
	public java.util.List<Long> getEvents() {
		return ecoute.getEvents();
		
	}

	public void cacher() {
		// TODO Auto-generated method stub
		if(frame_tab_de_bord != null ) {
			deconnection();
			if(modules != null) modules.cacher();
		}
	}
}
