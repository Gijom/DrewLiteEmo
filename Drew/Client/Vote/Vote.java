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
 * File: Vote.java
 * Author: Xavier Serpaggi
 * Description: Provides a means to express an opinion and to record it.
 * 
 * $Id: Vote.java,v 1.17 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.Vote;

import java.awt.event.*;
import java.awt.*;
import java.util.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;


/**
 * Classe representant une boite a voter
 * Exemple d'utilisation:
 * @see    Drew.Client.Util.Communication
 * @see    Status
 */
public class Vote
	extends Panel
	implements ActionListener, CooperativeModule {

	/**
	 * code <EM>unique</EM> representant les messages en provenance ou a destiniton
	 * de ce module en l'occurence <CODE>vote</CODE>.
	 */
	static final String 		CODE = "vote" ;

	private static final int 	steps = 5 ; // number of opinions you can choose from
	
	private Button			doIt,
					dontDoIt ;
	private Scrollbar		opinion ;
	private Status			status,
					voteFor ;
	private ScrollingStatus		history ;
	private Label			expressedLabel ;
	private Label			valueLabel ;

	private Hashtable		users ;

	/*
	 * The number of votes expressed and the total value of
	 * these votes.
	 * Thanks to these two variables we can implement an
	 * incremental process for the computation of the mean
	 * value.
	 */
	private int			expressed ;
	private int			total ;
	private float			poll,
					normPoll ;

	/** Locale attribute made available to other classes of this package : AppletLance, EditUserDialog */
	private Drew.Util.Locale	locale ;

	/**
	 * Recuper l'applet-mere
	 * @see Communication
	 */
	public Communication		central_applet;

	/**
	 * Window constructor.
	 * Nothing has to be done here. Everything must be written in
	 * the "constructor()" method behind.
	 * @see Communication
	 */
	public Vote() {}

	/**
	 * Window constructor.
	 * Nothing has to be done here. Everything must be written in
	 * the "constructor()" method behind.
	 * @param    cdc a communication class instance.
	 * @see Communication
	 */
	public Vote(Communication cdc) {
		this();
		constructor(cdc);
	}

	/**
	 * Everything is done in this method.
	 * It puts an end to the setup of the window and what's needed
	 * aroud to have everything work fine.
	 * @param    cdc a communication class instance.
	 * @see Communication
	 * @see Drew.Client.Util.CooperativeModule
	 * @see Drew.Client.TableauDeBord.Module
	 */
	public void constructor(Communication cdc) {
		central_applet = cdc ;
		locale = new Drew.Util.Locale( "Drew.Locale.Client.Vote", Config.getLocale()) ;

		total = 0 ;
		expressed = 0 ;
		poll = 0f ;
		normPoll = 0f ;

		doIt = new Button(locale.getString("PressVote")) ;
		doIt.setActionCommand("PressVote") ;
		doIt.addActionListener(this) ;
		
		dontDoIt = new Button(locale.getString("RemoveVote")) ;
		dontDoIt.setActionCommand("RemoveVote") ;
		dontDoIt.addActionListener(this) ;
		
		opinion = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, steps) ;
		opinion.setValue(steps/2) ;

		/*
		System.err.println("Min = " + opinion.getMinimum() + " " +
				   "Max = " + opinion.getMaximum() ) ;
		*/

		status = new Status(0) ;
		voteFor = new Status((float)(opinion.getValue())/(steps - 1)) ;
		expressedLabel = new Label(locale.getString("Expressed") + " 0", Label.LEFT) ;
		valueLabel = new Label("#", Label.CENTER) ;

		history = new ScrollingStatus(1, locale) ;
		history.setValue(-1) ;
		history.start() ;

		opinion.addAdjustmentListener(new AdjustmentListener(){
			public void adjustmentValueChanged(AdjustmentEvent e) {
				voteFor.setValue((float)(opinion.getValue())/(steps - 1)) ;
			}
		}) ;

		users = new Hashtable() ;
	}

	public String getLocalizedString(String s) {
		return locale.getString(s) ;
	}

	/**
	 * This method updates everything depending on the message reveived
	 * from the server.
	 * If toggles the little label that indicates if the user playing
	 * has expressed a vote or not depending on the local user name
	 * and the name returned by the server.
	 * 
	 * @param user the user expressing a vote.
	 * @param val the value of the vote (-1 means the user wants to remove his vote).
	 * @return nothing is returned by this function.
	 * 
	 * @see Communication
	 * @see Drew.Client.Util.CooperativeModule
	 * @see Drew.Client.TableauDeBord.Module
	 */
	private void updatePoll(String user, Integer val) {
		
		// System.err.println("user = " + user + " nom = " + central_applet.nom) ;
		
		if ( val.intValue() < 0 ) {
			if ( users.remove(user) != null
			&&   user.equals(central_applet.nom) ) {
				// valueLabel.setText("0") ;
				valueLabel.setBackground(this.getBackground()) ;
			}
		} else {
			if ( users.put(user, val) == null ) {
				// System.out.println(user + " " + locale.getString("FirstVote") + ".") ;
			} 
			
			if ( user.equals(central_applet.nom) ) {
				// valueLabel.setText("1") ;
				valueLabel.setBackground(Color.green) ;
			}
		}
		
		total = 0 ;
		expressed = users.size() ;
		
		Enumeration e = users.elements() ;
		while ( e.hasMoreElements() ) {
			total += ((Integer)e.nextElement()).intValue() ;
		}
		
		poll = (float)total / expressed ;
		normPoll = poll / ( steps - 1 ) ;

		if ( expressed == 0 ) {
			history.setValue(-1f) ;
		} else {
			history.setValue(normPoll) ;
		}
		setStatus(normPoll) ;
		expressedLabel.setText(locale.getString("Expressed") + " " + expressed) ;
	}

	/* Methodes de l'interface ActionListener */
	/*----------------------------------------*/
	public void actionPerformed(ActionEvent e) {
		String commande = e.getActionCommand() ;
		
		if ( commande == "PressVote" ) {
			//central_applet.envoiserveur(CODE + "~" + opinion.getValue() ) ;
			central_applet.envoiserveur( new XMLTree(CODE, String.valueOf( opinion.getValue() ) ) );
		} else if ( commande == "RemoveVote" ) {
			//central_applet.envoiserveur(CODE + "~-1" ) ;
			central_applet.envoiserveur( new XMLTree(CODE, "-1" ) );
		}
	}


	/* Methodes de l'interface MouseListener */
	/*---------------------------------------*/
	/*
	public void mouseClicked(MouseEvent e) {
		central_applet.envoiserveur(CODE + "~" + opinion.getValue() );
	}

	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	*/
	
	/* surcharge des methodes de Component */
	/*-------------------------------------*/
	public Dimension getPreferredSize() {
		return new Dimension(300, 200);
	}

	public Dimension getMinimumSize() {
		return new Dimension(300, 200) ;
	}


	/* implantation de l'interface CooperativeModule */
	/*-----------------------------------------------*/
	public String getTitle() {
		return locale.getString("WindowTitle");
	}

	public void init() {
		GridBagConstraints c = new GridBagConstraints();
		Panel ligneScroll = new Panel(new BorderLayout()) ;;
		Panel ligneInfo = new Panel(new BorderLayout()) ;;

		// Add Components to the Frame.
		setLayout(new GridBagLayout()) ;
		c.gridy = 0 ;

		// One line
		c.gridx = 0 ;
		c.gridwidth = 4 ;
		c.weighty = 0.44 ;
		c.weightx = 1.0 ;
		c.fill = GridBagConstraints.BOTH ;
		c.anchor = GridBagConstraints.EAST ;
		c.insets = new Insets(5,5,5,5) ;
		add(history, c) ;
		c.gridy ++ ;

		// One line
		c.gridx = 0 ;
		c.weighty = 0.07 ;
		c.fill = GridBagConstraints.BOTH ;
		c.anchor = GridBagConstraints.NORTHWEST ;
		c.insets = new Insets(0,5,0,5) ;
		add(status, c) ;
		c.gridy ++ ;

		// One line
		c.gridx = 0 ;
		c.gridwidth = 4 ;
		c.anchor = GridBagConstraints.CENTER ;
		c.insets = new Insets(0,5,0,5) ;
		//ligneScroll.add(new Label("-", Label.RIGHT), BorderLayout.WEST) ;
		ligneScroll.add(new Smiley(false), BorderLayout.WEST) ;
		ligneScroll.add(opinion, BorderLayout.CENTER) ;
		ligneScroll.add(new Smiley(true), BorderLayout.EAST) ;
		//ligneScroll.add(new Label("+", Label.LEFT), BorderLayout.EAST) ;
	
		add(ligneScroll, c) ;
		c.gridy ++ ;
		
		// One line
		c.gridx = 0 ;
		c.fill = GridBagConstraints.BOTH ;
		c.anchor = GridBagConstraints.NORTHWEST ;
		c.insets = new Insets(0,5,0,5) ;
		add(voteFor, c) ;
		c.gridy ++ ;

		// One line
		c.gridx = 0 ;
		c.gridwidth = 2 ;
		c.weighty = 0.2 ;
		c.fill = GridBagConstraints.NONE ;
		c.anchor = GridBagConstraints.WEST ;
		c.insets = new Insets(5,5,0,0) ;
		add(doIt, c) ;
		c.gridx = 2 ;
		c.anchor = GridBagConstraints.EAST ;
		c.insets = new Insets(5,0,0,5) ;
		add(dontDoIt, c) ;
		c.gridy ++ ;

		// One line
		c.gridx = 0 ;
		c.gridwidth = 4 ;
		c.weighty = 0.15 ;
		c.fill = GridBagConstraints.BOTH ;
		c.anchor = GridBagConstraints.CENTER ;
		c.insets = new Insets(0,5,0,5) ;
		ligneInfo.add(expressedLabel, BorderLayout.WEST) ;
		ligneInfo.add(valueLabel,BorderLayout.EAST) ;
		add(ligneInfo, c) ;
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
	public void stop() {
		history.stop() ;
	}

	/**
	 * destruction du module
	 */
	public void destroy() {
		history.stop() ;
	}

	/**
	 * effacement des donn�es du module
	 */
	public void clear() {
		expressed = 0 ;
		total = 0 ;
		poll = 0f ;
		normPoll = 0f ;
		opinion.setValue(steps/2) ;

		repaint() ;
	}

	/**
	 * Rend la chaine de caractere identifiant le module
	 */
	public String getCode() {
		return CODE;
	}

	/**
	 * Accepte les messages qui sont uniquement pour lui code == CODE
	 */
	public boolean messageFilter(String code, String origine) {
		return code.equals( getCode() );
	}

	public void messageDeliver(String code, String origine, String msg) {
		try {
			updatePoll(origine, new Integer(msg) );
		}
		catch(  NumberFormatException e ) {
			System.err.println( "Vote.messageDeliver : Can't convert message to int " + e );
		}

	}

	/**
        * XML version
        * We use event <code>&lt;event>&lt;vote>&lt;5/>&lt;vote>&lt;event></code>
        */
	public boolean messageFilter(XMLTree m) {
                return true;
        }

        public void messageDeliver(String user, XMLTree data) {

                if( data.tag().equals( CODE ) ) {
			try {
				updatePoll(user, new Integer(data.getText()) );
			}
			catch(  NumberFormatException e ) {
				System.err.println( "Vote.messageDeliver : Can't convert message to int " + e );
			}
                }
        }
	
	/* Control of the status part */
	/*----------------------------*/
	/**
	 * Sets the position of the blue/red bar to the mean
	 * value of the polls.
	 * @param s the mean value between 0 and 100.
	 */
	public void setStatus(float s) {
		status.setValue(s) ;
	}
}
