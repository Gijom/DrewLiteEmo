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
 * File: ViewBoard.java
 * Author:
 * Description:
 *
 * $Id: ViewBoard.java,v 1.2 2007/02/27 15:49:04 collins Exp $
 */

package Drew.Client.ViewBoard;

import java.awt.event.*;
import java.awt.*;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.BoxLayout;



import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

/**
 * Class	for a very simple shared text editor.
 * The removeTextListener and addTextListener method are managed asynchronously by
 * java and can't be used to differentiate text event from users or from setText method.
 * We use a boolean flag for this purpose and text event are send to network at time
 * intervals to minimize bandwith and event frequence. Some time it produce bugs ;-)
 */
public class ViewBoard extends Panel implements CooperativeModule {

	private static final long serialVersionUID = -7893996579552142378L;

	/**
	 * Id for the events handeled by the drewlet: <CODE>viewboard</CODE>
	 */
	static final String CODE  = "viewboard";

	/**
	 * The parent applet
	 * @see Communication
	 */
	public Communication central_applet;

	/**
	 * TextArea
	 */
	private TextArea T;
	private Label lg, ld;
	private javax.swing.JButton endTurnButton;
	private javax.swing.JButton numberButton;
	private JPanel southPanel,buttonPanel;
	private int ROWS = 20, COLS = 60;


	private static final int MSG_UPDATE = 1;
	private static final int MSG_ASKPROP = 2;
	private static final int MSG_RMVPROP = 3;
	private static final int MSG_RMVELSE = 4;
	private static final int MSG_PING = 5;
	private static final int MSG_PONG = 6;
	private static final int DELAY_UPDATE = 1000;
	private static final int DELAY_CHECKOWNER = 5000;
	private static final int DELAY_CHECKOWNERTIMEOUT = 10000;
	private static final int DELAY_TIMEOUT = 5000;
	private Vector userFifo;
	private Timer t_owner;
	private Timer t_exist_owner; // timer v�rifiant que le owner courant est toujours en ligne.
	// au bout de 10 secondes sans nouvelles -> ping
	// au bout de 5 secondes sans pong si pas de nouvelles -> expulsion
	private boolean waitingForOwning;
	private boolean isOwner;
	private boolean modifiedText;
	private long timeLastModif;
	private boolean pingWithoutPong;
	private String nameOfPingWithoutPong;

	/**
	 *  Default constructor for the drewlet (called by Class.newInstance() )
	 */
	public ViewBoard() {
		super();
		T = new TextArea("",ROWS,COLS,TextArea.SCROLLBARS_VERTICAL_ONLY);
	}

	/**
	 * Constructor for the drewlet
	 * @param    cdc communication applet part
	 * @see Communication
	 */
	public ViewBoard(Communication cdc) {
		this();
		constructor(cdc);
	}
	/**
	 * To finish the drewlet initialisation,because  Class.newInstance()
	 * call only the default class constructor.
	 * @param    cdc reference to the communication part of the drewlet
	 * @see Communication
	 * @see Drew.Client.Util.CooperativeModule
	 * @see Drew.Client.TableauDeBord.Module
	 */
	public void constructor(Communication cdc) {
		central_applet=cdc;
		pingWithoutPong = false;
		userFifo = new Vector ();
		t_owner = new Timer(DELAY_UPDATE, new ActionListener () {
			public void actionPerformed(ActionEvent arg0) {
				// if active personne hasn't wrote for some time (time > DELAY_TIMEOUT)
				if (Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_TIMEOUT) {
					// send message RELEASE_LOCK
					central_applet.envoiserveur( 
							new XMLTree( "viewboard" , 
									new XMLTree( "viewboardAction", 
											XMLTree.Attributes("type","RELEASE_LOCK"),
											XMLTree.Contents( "" )
									)
							)
					);
					// send message to include in trace file
//					central_applet.envoiserveur( 
//					new XMLTree( "viewboard" , 
//					new XMLTree( "viewboardAction", 
//					XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//					XMLTree.Contents( "releases lock explicitly" )
//					)
//					)
//					);
					// release lock on viewboard
					becomeNonOwner();
				}

				// if text has been modified
				if (modifiedText) {
					XMLTree tmpMsg = new XMLTree("viewboard",
							new XMLTree("viewboardAction",
									XMLTree.Attributes("type", "UPDATE"),
									XMLTree.Contents("")
							)
					);
					tmpMsg.add(new XMLTree("text",
							XMLTree.Attributes("lang",	Config.getLocale().getLanguage()),
							XMLTree.Contents(T.getText())
					));
					// send message to server to update trace file with modified text
					central_applet.envoiserveur(tmpMsg);
//					central_applet.envoiserveur(
//					new XMLTree( "viewboard" ,
//					new XMLTree( "text",
//					XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//					XMLTree.Contents( T.getText() )
//					)
//					)
//					);
					modifiedText = false;
				}
			}
		});

		t_exist_owner = new Timer(DELAY_CHECKOWNER, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// if at least one user is wainting in line for lock on viewboard
				if (userFifo.size() > 0) {
					// if last active user is timed out
					if (pingWithoutPong && Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_CHECKOWNERTIMEOUT) {
						pingWithoutPong = false;
						// send message to server to update trace file with forced release of viewboard
						central_applet.envoiserveur( 
								new XMLTree( "viewboard" , 
										new XMLTree( "viewboardAction", 
												XMLTree.Attributes("type","FORCE_RELEASE"),
												XMLTree.Contents("")
										)
								)
						);
//						central_applet.envoiserveur( 
//						new XMLTree( "viewboard" , 
//						new XMLTree( "viewboardAction", 
//						XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//						XMLTree.Contents( "is not writing anymore" )
//						)
//						)
//						);
					}

					// if last active user is timed out send ping
					if (Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_CHECKOWNERTIMEOUT) {
						pingWithoutPong = true;
						nameOfPingWithoutPong = (String) userFifo.elementAt(0);
						// send message to server to update trace file with user that's going to be ping-ed
						central_applet.envoiserveur( 
								new XMLTree( "viewboard" , 
										new XMLTree( "viewboardAction", 
												XMLTree.Attributes("type","PING"),
												XMLTree.Contents( ""+(String)userFifo.elementAt(0))
										)
								)
						);
					}
				}
			}
		});

		// send out ping to see if user is still connected
		t_exist_owner.start();
	}



	public void init() {

		this.setLayout(new BorderLayout());
		//this.setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));
		add(T, BorderLayout.CENTER);
		southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());
		add(southPanel, BorderLayout.SOUTH);

		southPanel.setLayout(new BoxLayout(southPanel,javax.swing.BoxLayout.Y_AXIS));

		lg = new Label();
		updatePrintFifo();
		ld = new Label("Vous ne pouvez pas modifier le texte");
		southPanel.add(lg);
		southPanel.add(ld);

		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BorderLayout());
		southPanel.add(buttonPanel);

		endTurnButton = new javax.swing.JButton();
		endTurnButton.setText("Rendre la main");
		endTurnButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				endTurnButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(endTurnButton, BorderLayout.EAST);

		numberButton = new javax.swing.JButton();
		numberButton.setText("Num�roter lignes");

		numberButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				numberButtonActionPerformed(evt);
			}
		});

		buttonPanel.add(numberButton, BorderLayout.WEST);

		isOwner = false;
		waitingForOwning = false;
		modifiedText = false;
		timeLastModif = 0;

		// textarea management
		T.setEditable(false);
		T.addTextListener(new TextListener() {
			public void textValueChanged(TextEvent arg0) {
				if (isOwner) {
					timeLastModif = Calendar.getInstance().getTimeInMillis();
					modifiedText = true;
				}
			}
		});
		T.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				// if double clic
				// should probably add "if(owner)" to release text board only if we are the active user
				// but maybe not necessary , really need to look into that
				if(e.getClickCount() == 2) {
					central_applet.envoiserveur( 
							new XMLTree( "viewboard" , 
									new XMLTree( "viewboardAction", 
											XMLTree.Attributes("type","RELEASE_LOCK"),
											XMLTree.Contents("")
									)
							)
					);
//					central_applet.envoiserveur( 
//					new XMLTree( "viewboard" , 
//					new XMLTree( "viewboardAction", 
//					XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//					XMLTree.Contents( "has stopped writing" )
//					)
//					)
//					);
					// release lock on viewboard
					becomeNonOwner();

				} 
				// else if we are not owner and not yet in line
				else if (!isOwner && !waitingForOwning) {
					// send message to server to update our 'in-line' status

					XMLTree tmpMsg = new XMLTree("viewboard");
					tmpMsg.add(new XMLTree("viewboardAction",
							XMLTree.Attributes("type","LOCK_ASKED_FOR"),
							XMLTree.Contents("")
					)
					);
					central_applet.envoiserveur(tmpMsg);

//					central_applet.envoiserveur( 
//					new XMLTree( "viewboard" , 
//					new XMLTree( "viewboardAction", 
//					XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//					XMLTree.Contents("asked acces to viewboard" )
//					)
//					)
//					);
					// set in line
					waitingForOwning = true;
				}

				if (isOwner) {
					timeLastModif = Calendar.getInstance().getTimeInMillis();
				}
			}


			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}

		});
		becomeNonOwner();
	}

	/**
	 * get messages with tag equal to <var>CODE</var>
	 */
	public void messageDeliver(String user, XMLTree data) {
		if( data.tag().equals( CODE ) ) {
			System.err.println("data.getText()= "+data.getText());
			
			XMLTree action = data.getByTagName("viewboardAction");
			
			String type = action.getAttributeValue("type");
			if(type != null) {
				System.err.println("attribute type=  "+type);
				// MSG_RMVPROP
				if( type.equals("RELEASE_LOCK") ) {
					//if (message.startsWith(""+MSG_RMVPROP)) {
					//remove user from wait line
					userFifo.remove(user);
					// release lock if active user
					if (user.equals(central_applet.nom)) becomeNonOwner();
					// if first user in line aquire lock
					if (userFifo.size()>0 && userFifo.elementAt(0).equals(central_applet.nom)) {
						becomeOwner();
					}
					// update wait line
					updatePrintFifo();
				}	    
				// MSG_ASKPROP
				else if (type.equals("LOCK_ASKED_FOR")) {
					// add user to wait line
					userFifo.add(user);
					// if we are the first user (nobody wainting in line when we requested the lock)
					if (userFifo.size()>0 && userFifo.elementAt(0).equals(central_applet.nom)) {
						// aquire lock
						becomeOwner();
					}
					// update wait line
					updatePrintFifo();
				}
				// MSG_UPDATE
				else if (type.equals("UPDATE")) {
					// if not active user
					// problem here with the replay : if a person connects with the same login as 'owner' : then nothing is replayed, and the user won't get current status...
					if (!isOwner) {
						// strip message of 'update' code (MSG_UPDATE)
						//String message2 = message.replaceFirst(""+MSG_UPDATE, "");
						// update viewboard with new content
						T.setText(data.getText());
						timeLastModif = Calendar.getInstance().getTimeInMillis();
					}
				}
				// MSG_RMVELSE
				else if (type.equals("FORCE_RELEASE")) {
					// strip message of code (MSG_RMVELSE)
					//String message2 = message.replaceFirst(""+MSG_RMVELSE, "");
					// remove user from wait line as designed in message
					userFifo.remove(data.getText());
					// release lock if user to remove from wait line was ourself
					if (data.getText().equals(central_applet.nom)) {
						becomeNonOwner();
					}
					// if we are next in line : aquire lock
					if (userFifo.size()>0 && userFifo.elementAt(0).equals(central_applet.nom)) {
						becomeOwner();
					}
					// update wait line
					updatePrintFifo();
				}
				// MSG_PONG
				else if (type.equals("PONG")) {
					if (pingWithoutPong && nameOfPingWithoutPong.equals(user)) pingWithoutPong = false;
				}
				// MSG_PING
				else if (type.equals("PING")) {
					// if ping was for ourself send answer (MSG_PONG)
					if (data.getText().equals(central_applet.nom) && isOwner)
						central_applet.envoiserveur( 
								new XMLTree( "viewboard" , 
										new XMLTree( "viewboardAction", 
												XMLTree.Attributes("type","PONG"),
												XMLTree.Contents("")
										)
								)
						);
				}
				else if ( type.equals("DISABLE_NUMBER_BUTTON")) {
					numberButton.setEnabled(false);
				}
				//else JOptionPane.showMessageDialog(null, "Message inconnu : "+data.getText(), "Message inconnu", JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	private void becomeOwner() {
		T.setEditable(true);
		t_owner.start();
		isOwner = true;
		modifiedText = false;
		waitingForOwning = false;
		timeLastModif = Calendar.getInstance().getTimeInMillis();
		ld.setText("Vous pouvez modifier le texte");
	}

	private void becomeNonOwner() {
		T.setEditable(false);
		t_owner.stop();
		isOwner = false;
		modifiedText = false;
		waitingForOwning = false;
		ld.setText("Cliquer dans la zone de texte pour demander l'acces au texte.");
	}

	private void updatePrintFifo() {
		Iterator it = userFifo.iterator();
		StringWriter sw = new StringWriter();
		if (it.hasNext()) sw.write(it.next()+" a la main. En attente: "); else sw.write("Personne �crit.");
		while (it.hasNext()) sw.write(" > "+it.next());
		lg.setText(sw.toString());
	}

	/* overload Component methods*/
	public Dimension getPreferredSize() {
		return new Dimension(500,500);
	}
	/* implement CooperativeModule interface */
	public String getTitle() {
		return "View Board";
	}
	/**
	 * start drewlet
	 */
	public void start() {
		t_exist_owner.start();
	}
	/**
	 * Stop drewlet
	 */
	public void stop() {
		t_owner.stop();
		t_exist_owner.stop();
	}
	/**
	 * Destroy drewlet
	 */
	public void destroy() {
		stop();
	}
	/**
	 * Clear all data in the drewlet
	 */
	public void clear() {
		T.setText("");
//		envoi au serveur
	}
	/**
	 * return id string for the drewlet
	 */
	public String getCode() {
		return CODE;
	}
	/**
	 * filter for usefull messages 
	 */
	public boolean messageFilter(XMLTree data) {
		return true;
	}

	private void endTurnButtonActionPerformed(java.awt.event.ActionEvent evt) {
		central_applet.envoiserveur( 
				new XMLTree( "viewboard" , 
						new XMLTree( "viewboardAction", 
								XMLTree.Attributes("type","LOCK_RELEASED"),
								XMLTree.Contents("")
						)
				)
		);
//		central_applet.envoiserveur( 
//		new XMLTree( "viewboard" , 
//		new XMLTree( "viewboardAction", 
//		XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//		XMLTree.Contents( "Releases lock explicitly" )
//		)
//		)
//		);
		// release lock on viewboard
		becomeNonOwner();

	}

	private void numberButtonActionPerformed(java.awt.event.ActionEvent evt) {
		int n=1;
		StringBuffer oldText = new StringBuffer( T.getText());
		StringBuffer newText = new StringBuffer();
		int max = oldText.length();
		int i;
		newText.append("1 ");
		for(i=0 ; i<max ; i++) {
			if( oldText.charAt(i) == '\n' ) {
				n++;
				newText.append("\n"+n+" ");
			} else {
				newText.append(oldText.charAt(i));
			}
		}

//		central_applet.envoiserveur( 
//		new XMLTree( "viewboard" , 
//		new XMLTree( "viewboardAction", 
//		XMLTree.Attributes("type","NUMBER_LINES"),
//		XMLTree.Contents( "" )
//		)
//		)
//		);
//		central_applet.envoiserveur( 
//		new XMLTree( "viewboard" , 
//		new XMLTree( "viewboardInternalAction", 
//		XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
//		XMLTree.Contents( "DISABLE_NUMBER_BUTTON" )
//		)
//		)
//		);
		


		central_applet.envoiserveur(	new XMLTree("viewboard" , 
				new XMLTree("viewboardAction", 
						XMLTree.Attributes("type","DISABLE_NUMBER_BUTTON"),
						XMLTree.Contents("")
				)
		)	
		);
		
		XMLTree tmpMsg = new XMLTree("viewboard" , 
				new XMLTree("viewboardAction", 
						XMLTree.Attributes("type","UPDATE"),
						XMLTree.Contents("")
				)
		);
		tmpMsg.add(new XMLTree("text",
				XMLTree.Attributes("lang",Config.getLocale().getLanguage()),
				XMLTree.Contents( ""+newText.toString() )
		));
		central_applet.envoiserveur(tmpMsg);

		
		// release lock on viewboard
		becomeNonOwner();
	}



}
