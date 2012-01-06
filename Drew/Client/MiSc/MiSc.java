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
 * File: MiSc.java
 * Author: Jean-Jacques Girardot
 * Description:
 *
 * $Id: MiSc.java,v 1.11 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.MiSc;

import java.awt.event.*;
import java.awt.*;
import java.util.*;
import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

import Sch.*;

/**
*  This class implements an interactive Scheme interpreter
*/
public class MiSc extends Panel
                  implements ActionListener, CooperativeModule {

  /**
  * The code <CODE>misc</CODE> represents messages aimed at this module, of
  * issued by this module.
  */
  static final String CODE = "misc";

    public static final String CATCHEVENTS = "catch";
    public static final String DROPEVENTS = "drop";

    private static XMLTree XMLcatchevents = 
	        new XMLTree(CODE, new XMLTree(CATCHEVENTS));
    private static XMLTree XMLdropevents = 
	        new XMLTree(CODE, new XMLTree(DROPEVENTS));

  /**
  * Get a connection to the central applet
  * @see Communication
  */
  public static Communication central_applet;

  /**
  * Fields
  */
    private TextArea  input;
    private int ROWSIN = 8;
    private int COLSIN = 60;
    private TextArea  output;
    private int ROWSOUT = 12;
    private int COLSOUT = 60;

    private Panel schPanel;
    private Panel buttons;
    private Button eval;
    private Button clear;

    private boolean catchevents = false;

    public int maxtxtlng = 16000;

    public static void catchevents(boolean catchthem) {
        if (catchthem)
            central_applet.envoiserveur(XMLcatchevents);
        else
            central_applet.envoiserveur(XMLdropevents);
    }

    public static void sendevent(XMLTree event) {
        if (event != null)
            central_applet.envoiserveur(event);
    }

    /**
    * Drewlet constructor
    * @see Communication
    */
    public MiSc()
    {
          super();
    }

    /**
    * Drewlet constructor
    * @param    cdc instance of the communication applet
    * @see Communication
    */
    public MiSc(Communication cdc)
    {
        this();
        constructor(cdc);
    }

    /**
    * To finish constructing the window, Class.newInstance()
    * calls only the default class constructor.
    * @param    cdc instance of the communication applet
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
        central_applet.envoiserveur(msg);
    }


    /* Redefines the methods of Component */

    public Dimension getPreferredSize()
    {
        return schPanel.getPreferredSize();
    }


    /* Implementation of the interface CooperativeModule */

    public String getTitle() {
        return "MiSc Interpreter";
    }

    public void init()
    {
        // Graphic components
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

        eval = new Button ("Eval");
        buttons.add (eval);

        clear = new Button ("Clear");
        buttons.add (clear);

        eval.addActionListener(this);
        eval.setActionCommand("eval");
        clear.addActionListener(this);
        clear.setActionCommand("clear");

        {
            //System.err.println("Initializing scheme");
            String msg = Sch.schinit(1);
            //String msg = Sch.sinit();
            //Sch.sinit();
			System.err.println("End of initialisation");
            output.append(msg+"\n");
            // appel de TestOps
            TestOps.dcl();
        }

    }

    public void addText(String str) {
        // Insert string in the output
        output.append(str);
        // Add a "\n" if necessary
        if (! str.endsWith("\n"))
            output.append("\n");
        // Position at the end
        output.setCaretPosition(0x3ffffff);
        int pos = output.getCaretPosition();
        if (pos > maxtxtlng)
        {
              output.replaceRange(";(truncated)\n", 0, pos-maxtxtlng);
              output.setCaretPosition(0x3ffffff);
        }
    }



   /**
   *   Management of interactions
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

          // Load the string in interpreter
          Sch.load(str);
          // Loop on code
          Sch.loop();
          // Get outputs
          str = Sch.result();
          // Put output in output text
          addText(str);

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
    * stoping the module
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
    * Destruction of the module
    */
    public void destroy()
    {
        stop();
    }

    /**
    * Clear module data
    */
    public void clear()
    {
    }


    /**
    * Return module identification
    */
    public String getCode()
    {
        return CODE;
    }

    /**
    * Filter messages
    */
    public boolean messageFilter(XMLTree msg)
    {
        return true;
    }

    /**
    * Filter messages
    */
    public boolean messageFilter(String code, String origine)
    {
        return true;
    }


    public void messageDeliver(String code, XMLTree msg)
    {
        // Message sent to Misc
        if (msg.tag().equals(CODE)) {
            Enumeration e = msg.elements();
            if (e.hasMoreElements()) {
                Object obj = e.nextElement();
                if (obj instanceof XMLTree) {
                    XMLTree tree = (XMLTree)obj;
                    /**
                     * Should we capture events ?
                     */
                    if (tree.tag().equals(CATCHEVENTS))
                        catchevents = true;
                    else if (tree.tag().equals(DROPEVENTS))
                        catchevents = false;
                }
            }
        }
        // Display captured messages
        else if ((output != null) && (catchevents)) {
            // Capturde messages 
            output.append("; Event catched: ");
            output.append(msg.toString() + "\n");
        }
    }

    public void messageDeliver(String code, String origine, String msg)
    {
         // Nothing done for now
    }
}
