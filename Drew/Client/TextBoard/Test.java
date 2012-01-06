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
 * File: Test.java
 * Author:
 * Description:
 *
 * $Id: Test.java,v 1.6 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.TextBoard;

import java.awt.event.*;
import java.awt.*;
 

class Test extends Frame implements TextListener,KeyListener {

  static final String CODE = "text";

  private TextArea T;
  private int ROWS = 15, COLS = 30;

  private char lcar;
  private int l;

  private StringBuffer TXT;

  	/**
  	* Constructeur de la fenetre de chat: definition de l'environnement graphique
  	* @param    cdc instance de l'applet de communication
  	* @see Communication
  	*/
  	public Test()
  	{
        	super("Text Editor");
		T = new TextArea("",ROWS,COLS,TextArea.SCROLLBARS_VERTICAL_ONLY);
		TXT = new StringBuffer(1000);
  	}

char RET = '\n';
char NRET = 172;

        public String netToDrew( String str ) {
                return str.replace(NRET, RET);
        }

        public String drewToNet( String str ) {
                return str.replace(RET, NRET);
        }

	public void textValueChanged(TextEvent e)
	{
		String msg, tmpTXT;
		String txt = T.getText();
		int delta =  txt.length() - l; l += delta ;
		int caret = T.getCaretPosition();
		int a, b;

		if( delta > 0 ) {
			msg = txt.substring( caret - delta, caret);
			a = b = caret - delta; 
		}
		else {
			if( (int)lcar == 65288 ) {
				msg = "";
				a = caret; b = caret - delta ; 
			}
			else {
				msg = String.valueOf(lcar);
				a = caret-1; b = caret - delta ; 
			}
		}

		System.out.println( "Replace from " + a + " to " + b + " by \"" + msg + "\" set caret to " + caret);

		// X.S. 03 07 2002: StringBuffer.replace() does not exist in 1.1 API
 		//TXT = TXT.replace(a, b, msg) ;

		// X.S. 03 07 2002: This replacement does not ensure StringBuffer length,
		// but may work pretty well.
		tmpTXT = new String(TXT.toString()) ;
		TXT = new StringBuffer(tmpTXT.substring(0,a-1) + msg + tmpTXT.substring(b)) ;

		System.out.println( netToDrew(drewToNet( TXT.toString()) ) );
	}

  	public void init()
	{
        	// Mise en place des elements graphiques
        	//----------------------------------------

                // couleur de fond
                setBackground(Color.darkGray );

                //Add Components to the Frame.
                GridBagLayout gridBag = new GridBagLayout();
                setLayout(gridBag);
                GridBagConstraints c = new GridBagConstraints();
                c.gridwidth = GridBagConstraints.REMAINDER;

                // les feux
                c.fill = GridBagConstraints.BOTH;
                c.weightx = 1.0;
                c.weighty = 1.0;

                gridBag.setConstraints(T, c);
                add(T);

        	// gestion des evenements
        	T.addTextListener(this);
		T.addKeyListener(this);
		T.setEditable( true );

		// on redimensionne et on affiche. (a rendre accessible dans la config)
    		setSize(T.getPreferredSize());
    		// show();
		setVisible(true);
		pack();
	}
  
//**********************************$
	public void keyPressed(KeyEvent e) 
	{
		lcar = e.getKeyChar();
	//	System.out.println( "keyPressed : " + (int)lcar + ", " + T.getCaretPosition() );
	}
  	public void keyReleased(KeyEvent e) {}
  	public void keyTyped(KeyEvent e) {}

	static public void main( String args[] ) 
	{
		Test T = new Test();
		T.init();
	}
}
