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
 * File: Rejoueur.java
 * Author:
 * Description:
 *
 * $Id: Rejoueur.java,v 1.15 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.text.*;
import java.awt.*;
import java.awt.event.*;

import Drew.Client.Util.*;

/**
* Centre de controle du rejoueur.
* (ATTENTION pour lancer un rejoueur, voir Drew.Client.Rejoueur.CentreDeConnection)
* Cette classe est l'IHM du rejoueur, elle permet d'envoyer des commandes au serveur et de visualiser
* les actions menees par le rejoueur.
* @see Drew.Client.Rejoueur.CentreDeConnection
*/
public class Rejoueur
	extends Panel
	implements ActionListener, ItemListener, AdjustmentListener
{
	/** Applet de lancement du rejoueur */
	public CentreDeConnection central_applet;
	
	/** zone ou est affichee la liste des participants de la session rejouee */
  	public TextArea zone_info;
  	/** nombre de lignes de la zone de texte zone_info */
  	public int nb_lignes_info;
  	/** zone ou sont affiches les messages de fonctionnement du rejoueur */
  	public Label activite;
  	/** Choix de la piece a ecouter pour la session en cours (liste geree dynamiquement) */
  	public Choice piece_a_ecouter;
  	/** Zone ou est affichee la ligne brute recue du serveur */
  	public TextField ligne_en_cours;
  	/** Zone ou est affichee une traduction succinte de la ligne brute recue du serveur */
  	public TextField traduction;
  	/** Bouton de controle pour quitter le Rejoueur */
  	//public Button Bquitter;

  	/** Bouton de controle pour joueur en continu la session demandee */
  	public Button Bplay;
  	/** Bouton de controle pour joueur la session demandee en pas a pas */
  	public Button Bstep;
  	/** Bouton de controle pour arreter la lecture en continue de la session */
  	public Button Bstop;
  	/** Bouton de controle pour revenir au debut de la session a rejouer */
  	public Button Brewind;
  	/** Bouton de controle pour lancer la lecture en avance rapide */
  	public Button Bffwd;

	/** All the usefull buttons in a single panel */
	public RemoteCtrl Magneto ;

	/** Slider */
	public Scrollbar slider;

  	/** Saut de ligne pour le systeme */
  	private String newline;

	Drew.Util.Locale Comment() {
		return central_applet.comment;
	}

  	/**
  	* Constructeur du tableau de commande, mise en place de tous les elements graphiques et
  	* prise en charge des evenements sur ces elements par l'applet.
  	* @param titre_fenet titre de la fenetre ou sont places tous les elements graphiques
  	* (les boutons de controle et les zones d'affichage.
  	* @param cdc applet de lancement du rejoueur
  	*/
  	Rejoueur(String titre_fenet,CentreDeConnection cdc)
  	{
		// Recupere l'aplet de CentreDeConnection
		central_applet = cdc;
		Drew.Util.Locale comment = Comment();
	
		// environnement graphique et positionnement
		// sur la grille
		Label titre_zone_info= new Label(comment.format("LabelList"));
		nb_lignes_info=5;
		zone_info= new TextArea("",nb_lignes_info,10,TextArea.SCROLLBARS_BOTH);
		zone_info.setEditable(false);
		Label titre_piece_a_ecouter= new Label(comment.format("LabelEcouter"));
		piece_a_ecouter=new Choice();
		piece_a_ecouter.addItem(central_applet.allrooms);
		Label titre_activite= new Label(comment.format("LabelMsg"));
		activite= new Label(comment.format("LabelConnecter"));
		Label titre_ligne_en_cours= new Label(comment.format("LabelLigne"));
		ligne_en_cours=new TextField() ;
		ligne_en_cours.setEditable(false);
		Label titre_traduction= new Label(comment.format("LabelSignification"));
		traduction=new TextField() ;
		traduction.setEditable(false);
/** see RemoteCtrl.java
		Bplay = new Button(comment.format("ButtonPLAY"));
		Bstep = new Button(comment.format("ButtonSTEP"));
		Bstop = new Button(comment.format("ButtonSTOP"));
		Brewind = new Button(comment.format("ButtonRWD"));
		Bffwd = new Button("FFWD");
*/	
		newline = System.getProperty("line.separator");
	
		GridBagLayout gridBag = new GridBagLayout();
		setLayout(gridBag);
		GridBagConstraints gbc = new GridBagConstraints();

/** Je retire tout. Je ne conserve que le magneto et les lignes traitees	
		// Zone d'information
		gbc.anchor = GridBagConstraints.NORTHWEST ;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.gridheight = 1;
		gridBag.setConstraints(titre_zone_info, gbc);
		add(titre_zone_info);

		gbc.gridy = 2;
		gbc.gridheight = 1;
		gridBag.setConstraints(zone_info, gbc);
		add(zone_info);
		
		// messages
		gbc.gridx = 3;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gridBag.setConstraints(titre_activite, gbc);
		//add(titre_activite);

		gbc.gridx = 3;
		gbc.gridwidth = 2;
		gridBag.setConstraints(activite, gbc);
		//add(activite);
	
		// piece a ecouter
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gridBag.setConstraints(titre_piece_a_ecouter, gbc);
		add(titre_piece_a_ecouter);
		gbc.gridx = 3;
		gbc.gridwidth = 2;
		gridBag.setConstraints(piece_a_ecouter, gbc);
		add(piece_a_ecouter);
*/
/** see Remote.java
		// Boutons RWD STOP PLAY STEP FFWD
		// A panel for the most common buttons
		Magneto = new Panel() ;
		Magneto.setLayout(new GridLayout()) ;
		Magneto.add(Brewind);
		Magneto.add(Bstop);
		Magneto.add(Bplay);
		Magneto.add(Bstep);
		Magneto.add(Bffwd);

*/
		Magneto = new RemoteCtrl( this );
		slider = new Scrollbar(Scrollbar.HORIZONTAL,0,1,0,cdc.trace_length);
		int block = cdc.trace_length / 20;
		if (block < 1) block = 1;
		slider.setBlockIncrement(block);
		if (cdc.withSlider) slider.addAdjustmentListener(this);

		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1;
		gbc.weighty = 1;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.insets = new Insets(1,1,1,1);

		gbc.gridx = 0;
		gbc.gridy = 0;
		add(traduction,gbc);
	
		// ecriture de la ligne courante
		gbc.gridy = 1;
		add(ligne_en_cours,gbc);
	
		gbc.gridy = 2;
		add(Magneto,gbc) ;

		gbc.gridy = 3;
		if (cdc.withSlider) add(slider,gbc);
		else add(new Label(""));

		validate();
	
		// Initialisation des objets graphiques
/** see RemoteCtrl.java 
		Bplay.addActionListener(this);
		Bplay.setActionCommand("PLAY");
		Bstop.addActionListener(this);
		Bstop.setActionCommand("STOP");
		Brewind.addActionListener(this);
		Brewind.setActionCommand("RWD");
		Bstep.addActionListener(this);
		Bstep.setActionCommand("STEP");
		Bffwd.addActionListener(this);
		Bffwd.setActionCommand("FFWD");
*/
		piece_a_ecouter.addItemListener(this);
		active_etat_connecte();
  	}

	void updateSliders(int n) {
		if (slider.getValue() != n) slider.setValue(n);
		/*
		 * dans un monde ideal, il faudrait disposer un slider dans chaque magneto
		 * suivi par le Rejoueur comme AdjustmentListener. Le mvt d'un slider devrait
		 * etre alors repercute vers les autres.
		 */
	}

	/** Gestion des evenements sur le slide */
	public void adjustmentValueChanged(AdjustmentEvent e) {
		Adjustable a = e.getAdjustable();
		if (a.equals(slider)) {
			int type = e.getAdjustmentType();
			int value = e.getValue();
			if (value > slider.getMaximum()) value = slider.getMaximum();
			if (value < slider.getMinimum()) value = slider.getMinimum();
 
			int delta = value - central_applet.current_event;
			if (delta > 0) {
				central_applet.jump(delta);
			}
			else if (delta < 0) {
				central_applet.backTo(value);
			}
		}
	}

  	/**
  	* Choix d'une piece a ecoute d'apres la selection dans une liste deroulante
  	*/
  	public void itemStateChanged(ItemEvent evt)
	{
		central_applet.room=((Choice)evt.getItemSelectable()).getSelectedItem() ;
	}

  	/**
  	* Declenchement de toutes les actions liees aux boutons de controle
  	*/
  	public void actionPerformed(ActionEvent evt)
  	{
		String command = evt.getActionCommand();

		
/*
		if(command.equals("Connecter")) { 
			etablir_connection();
			command = "RWD";
		}
		else if(command.equals("Deconnecter")) {
			 coupe_connection();
		}
		else if(command.equals("Quitter")) {
			central_applet.quitter();
		}
		else if (command.equals("PLAY")) { 
		    //central_applet.envoiserveur("PLAY");
		}
		else if (command.equals("STOP")) { 
		    //central_applet.envoiserveur("STOP");
		}
		else if (command.equals("STEP")) { 
		    //central_applet.envoiserveur("STEP");
		}
		else if (command.equals("RWD")) { 
			efface_affichage();
			//central_applet.envoiserveur("RWD");
		}
		else {
		    //central_applet.envoiserveur(command);
		}
*/

		//mp 21/3/2002 Pour que le bouton Rewind efface bien tous les champs quand il se repositionne
		// en debut de trace
		if (command.equals("RWD")) { 
			efface_affichage();
		}
		
		central_applet.doCommand( command );
		central_applet.updateSliders();
	}

  	/**
  	* Demande au CentreDeConnection d'etablir la connection avec le serveur
  	* @see #central_applet
  	* @see CentreDeConnection#connection(String nom)
  	public void etablir_connection()
  	{
		active_etat_connecte();
		central_applet.connection("Visualisation");
  	}
  	*/

  	/**
  	* Demande au CentreDeConnection de se deconnecter du serveur
  	* @see #central_applet
  	* @see CentreDeConnection#deconnection()
  	public void coupe_connection()
  	{
		active_etat_deconnecte();
		central_applet.deconnection();
		efface_affichage();
  	}
  	*/

  	/**
  	* Change l'etat active/desactive des boutons de controle ce qui permet de restreindre
  	* ou d'adapter les actions disponibles pour l'etat deconnecte.
  	*/
  	public void active_etat_deconnecte()
  	{
		//Bconnecter.setEnabled(true);
		//Bdeconnecter.setEnabled(false);
		Magneto.setEnabled( false );
	/*
		Bplay.setEnabled(false);
		Bstop.setEnabled(false);
		Bstep.setEnabled(false);
		Brewind.setEnabled(false);
		Bffwd.setEnabled(false);
*/
		//Bquitter.setEnabled(true);
  	}

  	/**
  	* Change l'etat active/desactive des boutons de controle ce qui permet de restreindre
  	* ou d'adapter les actions disponibles pour l'etat connecte.
  	*/
  	public void active_etat_connecte()
  	{
		//Bconnecter.setEnabled(false);
		//Bdeconnecter.setEnabled(true);
		//Bquitter.setEnabled(false);
		Magneto.setEnabled( true );
/*
		Bplay.setEnabled(true);
		Bstop.setEnabled(true);
		Bstep.setEnabled(true);
		Brewind.setEnabled(true);
		Bffwd.setEnabled(true);
*/
  	}

  	/**
  	* Efface une zone de texte de type TextArea
  	* @param zone zone de texte a effacer
  	*/
  	private void effacer (TextArea zone)
	{
	        zone.setText("");
		//mp Pour que ca fonctionne avec la MRJ d'Apple.

		//zone.replaceRange("",zone.getColumns()*zone.getRows());
		//zone.setCaretPosition(0);
	}

  	/**
  	* Remet a "zero" la liste des pieces disponibles et des participants
  	* @see CentreDeConnection#initPieceEtParticipants()
  	*/
	private void resetListePiece()
	{
		piece_a_ecouter.removeAll();
		piece_a_ecouter.addItem("toutes les pieces");
		central_applet.initPieceEtParticipants();
	}

  	/**
  	* reinitialise toutes les zones d'information et de selection du rejoueur
  	*/
  	private void efface_affichage()
	{
		mis_a_jour("direct","");
		mis_a_jour("traduction","");
		//effacer(zone_info);
		mis_a_jour("info_efface","");
		resetListePiece();
	}

  	/**
  	* affiche du texte ou realise une action dans la zone d'information specifiee.
  	* codes supportes:
  	* <PRE>
  	*     code               zone                      action
  	*     ----               ----                      ------
  	*     direct             ligne_en_cours            affiche du texte 
  	*     traduction         traduction                affiche du texte
  	*     info               zone_info                 affiche du texte
  	*     connection         etatDeLaConnection        affiche du texte
  	*     activite           activite                  affiche du texte
  	*     info_efface        zone_info                 efface la zone de texte
  	*
  	* </PRE>
  	* @param code zone
  	* @param texte texte a afficher
  	*/
  	public void mis_a_jour(String code, String texte)
  	{
		mis_a_jour(code, null, texte);
	}

  	public void mis_a_jour(String code,java.util.Date date, String texte) 
  	{
		if (code.equals("direct"))
			//PJ 20020124
         		//ligne_en_cours.append(newline+texte);
         		ligne_en_cours.setText(texte);
		else if (code.equals("traduction"))
			//PJ 20020124
         		//traduction.append(newline+texte);
			if(date != null) {
				String str = DateFormat.getDateTimeInstance(DateFormat.LONG,DateFormat.LONG,Config.getLocale()).format(date);
         			traduction.setText(date + " : " + texte);
			}
			else {
         			traduction.setText(texte);
			}
		else if (code.equals("info"))
			zone_info.append(texte+newline);
		else if (code.equals("info_efface"))
			effacer(zone_info);
		/*
		else if (code.equals("connection"))
			etatDeLaConnection.setText(texte);
		*/
		else if (code.equals("activite"))
			activite.setText(texte);
  	}
}



