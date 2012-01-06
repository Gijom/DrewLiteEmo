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
 * File: Dict.java
 * Author:
 * Description:
 *
 * $Id: Dict.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Dict;

import java.awt.event.*;
import java.awt.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;



/**
* Classe implementant l'acc�s au dictionnaire
*/
public class Dict extends Panel
                  implements ActionListener, CooperativeModule {

/**
* We use the XML tree  <code>&lt;event>&lt;dict>dictionary request as string&lt;dict>&lt;event></code>
*/
  static final String CODE = "dict";

  /**
  * Recuper l'applet-mere
  * @see Communication
  */
  public static Communication central_applet;

  /**
  * Champs
  */
    private TextArea  input;
    private int ROWSIN = 10;
    private int COLSIN = 100;
    private TextArea  output;
    private int ROWSOUT = 20;
    private int COLSOUT = 100;

    private Panel schPanel;
    private Panel buttons;
    private Button eval;
    private Button clear;


    public int maxtxtlng = 16000;

  	/**
  	* Constructeur de la fenetre 
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public Dict()
  	{
        	super();
                
  	}

  	/**
  	* Constructeur de la fenetre
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public Dict(Communication cdc)
  	{
        	this();
		constructor(cdc);
  	}

  	/**
  	* pour terminer la construction de la fenetre Class.newInstance()
	* n'appelle que le contructeur par defaut de la classe.
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
	* @see Drew.Client.Util.CooperativeModule
	* @see Drew.Client.TableauDeBord.Module
  	*/
	public void constructor(Communication cdc) 
	{
        	central_applet=cdc;

	}

        public static void send(String msg)
        {
            central_applet.envoiserveur( new XMLTree( CODE, msg) );
        }


        /* surcharge des methodes de Component */

        public Dimension getPreferredSize()
        {
                return schPanel.getPreferredSize();
        }


  	/* implantation de l'interface CooperativeModule */

        public String getTitle() {
                return "Dict Access";
        }

  	public void init()
	{
        	// Mise en place des elements graphiques
        	//----------------------------------------

        setLayout (new BorderLayout());

        schPanel = new Panel ();
        schPanel.setLayout (new BorderLayout());
        schPanel.setBackground(Color.white);

        add (BorderLayout.CENTER, schPanel);

        output = new TextArea ("",ROWSOUT,COLSOUT,
                TextArea.SCROLLBARS_VERTICAL_ONLY);
        output.setEditable (false);
        schPanel.add (BorderLayout.NORTH, output);
        output.setBackground(Color.white);

        input = new TextArea ("",ROWSIN,COLSIN,
                TextArea.SCROLLBARS_VERTICAL_ONLY);
        schPanel.add (BorderLayout.CENTER, input);

        buttons = new Panel();
        buttons.setLayout (new FlowLayout());
        buttons.setBackground(Color.white);
        add (BorderLayout.SOUTH, buttons);

        eval = new Button ("Find");
        buttons.add (eval);

        clear = new Button ("Clear");
        buttons.add (clear);

        eval.addActionListener(this);
        eval.setActionCommand("eval");
        clear.addActionListener(this);
        clear.setActionCommand("clear");

        {
            //System.err.println("J'initialise scheme");
            // String msg = Sch.schinit(1);
            // System.err.println("Second initialisation de scheme");
            //String msg = Sch.sinit();
            //Sch.sinit();
             System.err.println("Fin de l'initialisation");
            // output.append(msg+"\n");
            // appel de TestOps
            // TestOps.dcl();
        }

	}
  
    public void addText(String str) {
        // Insertion de la chaine en sortie
        output.append(str);
        // Se termine-t-elle par "\n" ?
        if (! str.endsWith("\n"))
            output.append("\n");
        // On positionne � la fin
        output.setCaretPosition(0x3ffffff);
        int pos = output.getCaretPosition();
        if (pos > maxtxtlng)
        {
              output.replaceRange("(truncated)\n...", 0, pos-maxtxtlng);
              output.setCaretPosition(0x3ffffff);
        }
    }



   /**
   *   Gestion des interactions
   */
   public void actionPerformed(ActionEvent evt)
   {
       String cmd = evt.getActionCommand();

       if (cmd.equals("eval"))
       {
        // Read commands
        String str = input.getText();
        // Add commands to transcript
        addText(str);

        // Send the string
        //central_applet.envoiserveur(CODE + "~" + str);
        send( str);
        // Put output in output text

        // Reset input
        input.setText ("");
        // Ask for focus - doesn't work
        input.requestFocus();


       }
       else
       if (cmd.equals("clear"))
       {
        output.setText ("");

       }
   }


	/**
        * module is started
        */
        public void start()
        {
        }

        /**
        * arret du module
        */
        public void stop()
	{
/*
		if(isShowing()) {
        		dispose();
    		}
*/
	}

        /**
        * destruction du module
        */
        public void destroy() 
	{
		stop();
	}
  
        /**
        * effacement des donn�es du module
        */
        public void clear() {
        }


        /**
        * Rend la chaine de caractere identifiant le module
        */
        public String getCode() 
	{
		return CODE;
	}

        /**
        * Accepte les messages qui sont uniquement pour lui code == CODE
        */
        public boolean messageFilter(String code, String origine)
        {
                return code.equals( getCode() );
        }

        public void messageDeliver(String code, String origine, String msg) 
	{
		// display definition 
                addText(msg);
	}
	
	public boolean messageFilter(XMLTree m) {
                return true;
        }

	/**
        * get messages with tag equal to <var>CODE</var>
        */
        public void messageDeliver(String user, XMLTree data) {

                if( data.tag().equals( CODE ) ) {
			addText( data.getText());
                }
        }

}

