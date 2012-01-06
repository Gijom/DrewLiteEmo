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
 * File: Chat_fenetre.java
 * Author:
 * Description:
 *
 * $Id: Chat_fenetre.java,v 1.12.2.2 2007/10/31 14:22:57 collins Exp $
 */

package Drew.Client.Chat;

import java.awt.event.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.text.*;

import java.util.*;

import Drew.Client.Util.*;
//import Drew.Client.TableauDeBord.*;
import Drew.Util.XMLmp.*;

/**
 * Classe representant la fenetre de chat a l'ecran
 * Exemple d'utilisation:
 * <pre>
 *     Frame frame_chat = new Chat_fenetre(parent);
 *    frame_chat.show();
 * </pre>
 * @see    Drew.Client.Util.Communication
 */
public class Chat_fenetre extends DefaultCooperativeModule implements ActionListener, KeyListener {

    /**
     * code <EM>unique</EM> represntatnt les messages en provenance ou a destiniton de ce module
     * en l'occurence <CODE>cht</CODE>
     */
    static final String CODE = "chat";

    /**
     * Champ de saisie des messages de l'utilisateur
     */
    public TextField textField;

    /**
     * Champ d'affichage des messages du chat des autres utilisateurs 
     */
    public TextArea textArea;
    public JTextPane textPane;
    public JScrollPane scrollPane;
    private StyledDocument doc ;
    //private StyledDocument doc2;
    //private JEditorPane editorPane;
    private int testColor=0;

    /** Base de calcul des couleurs */
    private final static double base = 3.0 ;

    // list of users (information provided by the communication center
    Vector listOfUsers;

    /**
     * Chaine de caractere representant le retour a la ligne
     */
    public String newline;

    /**
     * The XML object used to send messages
     */
    private XMLTree msg   = null;
    private XMLTree event = null;
 
    private Duration d = new Duration();

    /**
     * Constructeur de la fenetre de chat: definition de l'environnement graphique
     * @param    cdc instance de l'applet de communication
     * @see Communication
     */
    public Chat_fenetre() 
    {
    }

    /**
     * Constructeur de la fenetre de chat: definition de l'environnement graphique
     * @param    cdc instance de l'applet de communication
     * @see Communication
     */
    public Chat_fenetre(Communication cdc) 
    {
	this();
	constructor(cdc);
    }

    /**
     * pour terminer la construction de la fenetre de chat,  Class.newInstance()
     * n'appelle que le contructeur par defaut de la classe.
     * @param    cdc instance de l'applet de communication
     * @see Communication
     * @see Drew.Client.Util.CooperativeModule
     * @see Drew.Client.TableauDeBord.Module
     */
    public void constructor(Communication cdc) {
	super.constructor(cdc);
	newline = System.getProperty("line.separator");

	// Create an XML object suitable for all messages
	msg = new XMLTree("text",XMLTree.Attributes("lang",Config.getLocale().getLanguage()),XMLTree.Contents());
	event = new XMLTree("chat", msg );

	System.err.println( "Chat created (" + this.hashCode() + ")" );
    }

    /**
     * Implement ActionListener
     * Envoi le texte saisi dans le chat a chaque retour charriot
     * @param    evt attend un retour chariot
     */
    public void actionPerformed(ActionEvent evt) {
	String text = textField.getText();

	msg.setContents(text);
	d.stamp();
	central_applet.envoiserveur(event, d);
	textField.setText("");
	d.clear();
    }


    /**
     * Implement KeyListener for evaluate time needs to write sentence in chat
     */
    public void keyTyped(KeyEvent e) {
	if( d.isCleared() ) d.init();
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    /**
     * @param    texte texte affiche dans le chat
     * @see    Drew.Client.Util.Communication#envoiserveur(String texte)
     */
    public void mis_a_jour(final String texte,final String player) {
	//textArea.insert(texte+newline,textArea.getCaretPosition());
	//textArea.setCaretPosition(999999);
	//textArea.append(texte+newline);
	//textArea.repaint(10);

	    //byte[] db = texte.getBytes("UNICODE");
	    //String str = new String(db);
    	
    	SimpleAttributeSet a = new SimpleAttributeSet();
		Color couleur= getColor(player);
        StyleConstants.setForeground(a,couleur);
        final SimpleAttributeSet b = a;
        
        
		SwingUtilities.invokeLater(new Runnable () {
				                public void run() {
				                	try {
				                	doc.insertString(doc.getLength(), texte+newline, b);
				                	} catch (BadLocationException ble) {
				                	    System.err.println("Couldn't insert text in TextPane");
				                	}
				                } 
				            });
	    //doc.insertString(0, texte+newline, doc.getStyle(player) );
// 	    if(testColor == 0) {
// 		doc.insertString(doc.getLength(), texte+newline, doc.getStyle("Red") );
// 		testColor = 1;
// 	    } else {
// 		doc.insertString(doc.getLength(), texte+newline, doc.getStyle("Blue") );
// 		testColor=0;
// 	    }


    }

    /* surcharge des methodes de Component */

    public Dimension getPreferredSize()
    {
	return new Dimension(600,300);
    }


    // implantation de l'interface CooperativeModule 

    public String getTitle() {
	return "Chat Area";
    }

    public void init() 
    {

	// Mise en place des elements graphiques
	//----------------------------------------
	textField = new TextField(20);
	textArea = new TextArea("", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
	textArea.setEditable(false);
	textPane = new JTextPane();
	textPane.setEditable(false);
	scrollPane = new JScrollPane();
	//textPane.setContentType("text/html; charset=UTF-8");
	doc = textPane.getStyledDocument();
	addStylesToDocument(doc);

	//editorPane = new JEditorPAne();
	//doc2 = editorPane.getStyledDocument();


	//Add Components to the Frame.
	GridBagLayout gridBag = new GridBagLayout();
	setLayout(gridBag);
	GridBagConstraints c = new GridBagConstraints();
	c.gridwidth = GridBagConstraints.REMAINDER;
	
	
	c.fill = GridBagConstraints.HORIZONTAL;
	gridBag.setConstraints(textField, c);

	// zone d'affichage du texte
	c.fill = GridBagConstraints.BOTH;
	c.weightx = 1;
	c.weighty = 1;
	//gridBag.setConstraints(textArea, c);
	//add(textArea);
	
	// zone d'affichage du texte
	//textPane.setBackground(Color.lightGray);
	scrollPane.getViewport().setView(textPane);
	//gridBag.setConstraints(textPane, c);
	gridBag.setConstraints(scrollPane, c);
	add(scrollPane);

	// zone d'entree du texte
	add(textField);

	// zone d'affichage du texte
	//gridBag.setConstraints(editorPane, c);
	//add(editorPane);

	// gestion des evenements
	textField.addActionListener(this);
	textField.addKeyListener(this);

	listOfUsers = new Vector();

    }
  
    /**
     * effacement des données du module
     */
    public void clear() {
    	try {
    		doc.remove(0,doc.getLength());
    	} catch (BadLocationException ble) {
    		System.err.println("clear didn't work in chat");
    	}
    }

    /**
     * return as String the uniq identifier for the drewlet
     */
    public String getCode() 
    {
	return CODE;
    }


    public void moduleMessageDeliver(String user, XMLTree data) {
	String speaker;
	float fixedValue2 = 1;
	float fixedValue = (float)0.8;
// 	System.out.println("data tag:"+data.tag());
// 	if( data.tag().equals("control") ) {
// 	    System.out.println("text:"+data.getText() );
// 	    System.out.println("player:"+data.getAttributeValue("player"));
// 	    Enumeration e  = data.elements();
// 	    while( e.hasMoreElements() ) {
// 		System.out.println("attrib:"+e.nextElement().toString());
// 	    }
// 	}
	if( getCode().equals( data.tag() ) ) {
		String player = data.getAttributeValue("player");
		if( player == null) {
			player = user;
		}
//	    if( ! isKnownUser(player) ) {
//		listOfUsers.add(player);
//		Style s = doc.addStyle(player,null);
//		float t = makeTint(listOfUsers.size()-1);
//		Color c = new Color(Color.HSBtoRGB(t,fixedValue,fixedValue));
//		System.err.println("bllllllllllllllllllllllllllll "+ player + " " + c);
//		StyleConstants.setForeground(s, c );
//	    }

// 	    if( data.getText().equals("bold") ) {
// 		Style s = doc.getStyle(player);
// 		StyleConstants.setBold(s,true);
// 	    } else if( data.getText().equals("nobold") )  {
// 		Style s = doc.getStyle(player);
// 		StyleConstants.setBold(s,false);
// 	    } else
	    
	    mis_a_jour( "<" + user + "> " + data.getText() , player );
	    
	    // 	    if( data.getText().equals("testCouleur") ) {
// 		for(int i=0 ; i<100 ; i++) {
// 		    Style s = doc.addStyle("couleur"+i,null);
// 		    StyleConstants.setForeground(s,new Color(Color.HSBtoRGB(makeTint(i),fixedValue,fixedValue)));
// 		    mis_a_jour("Couleur:"+i+"[normal] Voici une plus longue phrase pour se faire une meilleure id���e de la couleur","couleur"+i);
// 		}
// 	    }
	    
	}
    }

    protected void finalize() throws Throwable {
	System.err.println( "Chat finalised (" + this.hashCode() + ")" );
    }


    protected void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
	    getStyle(StyleContext.DEFAULT_STYLE);
	
	// Makes text red
	Style red = doc.addStyle("Red", null);
	StyleConstants.setForeground(red, Color.red);

	Style blue = doc.addStyle("Blue", null);
	StyleConstants.setForeground(blue, Color.blue);

	//styles[i] = doc.addStyle("",null);

        
// 	Style regular = doc.addStyle("regular", def);
//         StyleConstants.setFontFamily(def, "SansSerif");

//         Style s = doc.addStyle("italic", regular);
//         StyleConstants.setItalic(s, true);

//         s = doc.addStyle("bold", regular);
//         StyleConstants.setBold(s, true);

//         s = doc.addStyle("small", regular);
//         StyleConstants.setFontSize(s, 10);

//         s = doc.addStyle("large", regular);
//         StyleConstants.setFontSize(s, 16);

        
    }

    /*
     * from an int representing user rank, build a tint (color for his text)
     */
    public float makeTint(int i){
	float t ;
	if (i == 0) t = (float) 0.0;
	else {
	     double di, msb, puiss, reste;
	     di = (double) i;

	     puiss = Math.floor(Math.log(di)/Math.log(base));
	     puiss = Math.pow(base, puiss);
	     msb = Math.floor(di/puiss);
	     reste = di - msb*puiss;

	     t = makeTint((int) reste) + (float) (msb / base / puiss);
	}
	return t ;
    }




}

    
