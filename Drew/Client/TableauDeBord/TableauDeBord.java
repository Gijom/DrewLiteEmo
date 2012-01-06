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
 * File: TableauDeBord.java
 * Author:
 * Description:
 *
 * Changes made by Mathieu Ponsonnet (MP), Philippe Jaillon (PJ)
 * $Id: TableauDeBord.java,v 1.15 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.TableauDeBord;

import java.io.*;
import java.awt.*;
import java.awt.event.*;

import Drew.Util.XMLmp.*;
import Drew.Client.Util.Config;

/**
* Dashboard displaying
*/
// The dashboard is the applet
//public class TableauDeBord extends Frame implements ActionListener {
public class TableauDeBord extends Panel implements ActionListener {
  /**
  * Get mother applet
  */
  public CentreDeConnection central_applet;

  /**
  * User's name
  */
  public String nom;

  /**
  * Label for the area for entering the user's name
  */
  public Label L;

  /**
  * Area for entering the user's name
  */
  public TextField TF;

  /**
  * Button for connection to the server
  */
  public Button Bconnecter;

  /**
  * Button for disconnecting from the server
  */
  public Button Bdeconnecter;

  /**
  * Button for clearing user's name
  */
  public Button Beffacer;

  /**
  * Button for displaying the subject in the browser
  */
  public Button Bsujet;

  /**
  * Button for displayong help in the browser
  */
  public Button Baide;

  /**
  * Button to display people present in the room
  */
  public Button Bliste;

  /**
  * Pop-up menu to change room
  * @see Room
  */
  public Room liste_room;

  /**
  * Text area for displaying messages in the dashboard
  */
  public TextArea textArea;

  /**
  * Newline as a character string
  */
  public String newline;

  //MP 6/9/2002
  /**
  * Label with DREW version number
  */
  public Label version = new Label("DREW version 1.2 (XML)");
  
  /**
  * Check boxes to activate or disactivate drewlets
  * @see Module
  */
  public Module coche_module;

  private static final String CODE = "control";

  /**
  * Constructeur du tableau de bord: definition de l'aspect graphique.
  * Lecture des parametres de l'applet-mere dans la page html:
  * changement de piece et controle des modules autorises ou non.
  * @param titre_fenet titre de la fenetre du tableau de bord
  * @param cdc recupere l'applet-mere
  */
  TableauDeBord(String titre_fenet,CentreDeConnection cdc)
  {
    //super(titre_fenet);
    super();

    // Get the CentreDeConnection applet
    central_applet = cdc;
    Drew.Util.Locale comment = cdc.comment;

    //PJ (10/2001) setTitle(comment.format("WindowTitle")); 

    // Graphic placements on the grid
    L = new Label(comment.format("LabelName"));
    TF = new TextField("", 15);
    Bconnecter = new Button(comment.format("ButtonConnect"));
    Bdeconnecter = new Button(comment.format("ButtonDeconnect"));
    Beffacer = new Button(comment.format("ButtonClear"));
    Bsujet = new Button(comment.format("ButtonSubject"));
    Baide = new Button(comment.format("ButtonHelp"));
    Bliste = new Button(comment.format("ButtonList"));
    liste_room = new Room(central_applet);
    //liste_room.initx(central_applet.getParameter("room"));
    liste_room.initx(Config.getInitialRoom());
    textArea = new TextArea("", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
    textArea.setEditable(false);
    coche_module = new Module(central_applet);
    coche_module.init();

    newline = System.getProperty("line.separator");
    GridBagLayout gridBag = new GridBagLayout();
    //PJ 20030130
    setFont(new Font("Helvetica", Font.PLAIN, 12));
    //setBackground(Color.white);
    //setForeground(Color.black);
    setLayout(gridBag);
    GridBagConstraints gbc = new GridBagConstraints();

    // Label
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gridBag.setConstraints(L, gbc);
    add(L);

    // Area for entering user's name
    gbc.gridx = 1;
    gridBag.setConstraints(TF, gbc);
    add(TF);

    // Button for connection
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    gbc.gridy = 1;
    gridBag.setConstraints(Bconnecter, gbc);
    add(Bconnecter);
    
    // Button for disconnection
    gbc.gridx = 1;
    gbc.gridy = 1;
    gridBag.setConstraints(Bdeconnecter, gbc);
    add(Bdeconnecter);

    // Area for room list
    gbc.gridwidth = 1;
    gbc.gridheight = 1;
    gbc.gridx = 2;
    gbc.gridy = 1;
    gridBag.setConstraints(liste_room, gbc);
    add(liste_room);

    // Button to get participants list
    gbc.gridwidth = 1;
    gbc.gridx = 3;
    gridBag.setConstraints(Bliste, gbc);
    add(Bliste);

    // Button to display help in the browser
    gbc.gridwidth = 1;
    gbc.gridx = 4;
    gridBag.setConstraints(Baide, gbc);
    add(Baide);

    // Messages display area
    gbc.gridwidth = 3;
    gbc.gridheight = 5;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.gridx = 0;
    gbc.gridy = 2;
    gridBag.setConstraints(textArea, gbc);
    add(textArea);

    // Labels of the modules
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gbc.gridx = 3;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    Label t = new Label(central_applet.comment.getString("LabelModules"));  // Available Modules
    gridBag.setConstraints(t, gbc);
    add(t);

    // Zone de case a cocher pour les modules
    gbc.gridwidth = 2;
    gbc.gridheight = 4;
    gbc.gridx = 3;
    gbc.gridy = 3;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.anchor = GridBagConstraints.NORTHEAST;
    gridBag.setConstraints(coche_module, gbc);
    add(coche_module);

    // MP 6/9/2002
    // Label of version number
    gbc.gridx = 3;
    gbc.gridy = 7;
    gbc.gridwidth = 2;
    gbc.gridheight = 1;
    gridBag.setConstraints(version, gbc);
    add(version);
    version.setFont(new Font("Helvetica", Font.PLAIN, 10));
    
    // Initialisation of graphic objects
    Bconnecter.addActionListener(this);
    Bconnecter.setActionCommand("Connecter");
    Bdeconnecter.addActionListener(this);
    Bdeconnecter.setActionCommand("Deconnecter");
    Bdeconnecter.setEnabled(false);
    Beffacer.addActionListener(this);
    Beffacer.setActionCommand("Effacer");
    Bsujet.setActionCommand("Sujet");
    Bsujet.addActionListener(this);
    Baide.setActionCommand("Aide");
    Baide.addActionListener(this);
    Bliste.setActionCommand("listing");
    Bliste.addActionListener(this);
    Bliste.setEnabled(false);

    // Lecture des parametres de l'applet dans la page html
	// Getting parameters from the applet in the html page
    // coche_module.setEnabled(central_applet.getParameter("coche").equals("true"));
    // liste_room.setEnabled(central_applet.getParameter("piece").equals("true"));

    coche_module.setEnabled(Config.canChooseDrewlet());
    liste_room.setEnabled(Config.canChangeRoom());
  }

  	/**
  	* Management of interactions with the dashboard
  	* @param evt evenement
  	* @see CentreDeConnection
  	*/
  	public void actionPerformed(ActionEvent evt)
  	{
    		String command = evt.getActionCommand();

    		if(command.equals("Connecter")) {
        		etablir_connection();
			}
    		else if(command.equals("Effacer")) {
        		effacer_texte();
			}
    		else if(command.equals("Deconnecter")) {
        		coupe_connection();
			}
    		else if(command.equals("Quitter")) {
        		central_applet.quitter();
    		}
    		else if (command.equals("Sujet")) {
        		central_applet.sujet();
    		}
    		else if (command.equals("Aide")) {
        		central_applet.aide();
    		}
    		else if (command.equals("listing")) {
        		central_applet.envoiserveur(new XMLTree("server", new XMLTree( "list") ));
		}
  	}

  	/**
  	* Modifie la validite des boutons du tableau de bord.
  	* Demande l'initialisation des modules coches.
  	* Recupere le nom de l'utilisateur, positionne la piece choisie par l'utilisateur
  	* et demande la connection au serveur GranSioux via l'applet-mere.
  	* @see CentreDeConnection
  	*/
  	public void etablir_connection()
  	{
		try {
    			coche_module.demarrer();
    			nom = TF.getText();
    			central_applet.room = liste_room.piece_choisie();
	
    			Bconnecter.setEnabled(false);
    			Bdeconnecter.setEnabled(true);
    			Bliste.setEnabled(true);

    			central_applet.connection(nom);
		}
		catch( IOException e ) {};
  	}

  	/**
  	* Efface la zone de saisie du nom de l'utilisateur
  	*/
  	public void effacer_texte()
  	{
    		TF.setText("");
  	}

  	/**
  	* Demande l'arret de la connection a l'applet-mere.
  	* @see CentreDeConnection
  	*/
  	public void coupe_connection()
  	{
    		Bconnecter.setEnabled(true);
    		Bdeconnecter.setEnabled(false);
    		Bliste.setEnabled(false);
    		central_applet.quitterModules();
  	}

  	/**
  	* Met a jour la zone de texte du tableau de bord
  	* @param texte message a afficher dans le tableau de bord
  	*/
  	public void mis_a_jour(String texte)
  	{
         	//textArea.insert(texte+newline,textArea.getCaretPosition());
         	textArea.setCaretPosition(999999);
         	textArea.append(texte+newline);
  	}

  	public void messageDeliver(String texte) {
		mis_a_jour( texte );
  	}
	
  	public void messageDeliver(String code, String origine, String texte) {
		if( code.equals( CODE ) )  mis_a_jour( "<" + origine + "> " + texte );
  	}

    public void messageDeliver(String user, XMLTree data) {
		if( data.tag().equals( CODE ) ) {
                        mis_a_jour( data.getText() );
                }
  	}

  	void quitter() {
        	//PJ (10/2001) dispose();
        	//central_applet.Bentrer.setEnabled(true);
  	}

	public void selectModule(String name, boolean state) {
		coche_module.select(name,state);
	}
}
