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
 * File: TextBoard.java
 * Author:
 * Description:
 *
 * $Id: TextBoard.java,v 1.15.2.2 2007/10/31 14:22:58 collins Exp $
 */

package Drew.Client.TextBoard;

import java.awt.*;
import java.lang.Math;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.text.*;
import javax.swing.event.*; 

import Drew.Client.TextBoard.EditHistory.Modif;
import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;


/**
 * @author dyke
 * 
 * The shared text editor enables simultaneous edition of plain text.
 * 
 * A timer calls textChange() every second. textChange() sends whole text and caret position to server if change==true
 * 
 * Incoming messages are diffed against current text and changes are applied as intelligently as possible
 * 
 * The document is an EditOwnerDocument - this attaches an "owner" to each modification so that we can tell whether
 * changes come from the user or from the network (via the diff algorithm).
 * 
 * EditOwnerDocuments also maintains a history of modifications and their owners, so that text can be styled in function 
 * of its author and the time at which the contribution was added.
 * 
 * In the current implementation, the text starts off in the colour of its contributor. 
 * In replayer mode, this color does not change. Otherwise, it darkens every delayD, 
 * making it black in around 3-5*delayD milliseconds.
 * 
 * The slider is an idea to parameter this delayD, but it's implications are too complicated
 * for the user and is not usable for the replayer (all messages arrive one after the other
 * with no information about relative time of production).
 *
 */
public class TextBoard extends DefaultCooperativeModule implements DocumentListener, CaretListener, ActionListener {//, SimpleNotifiable {

	/**
	 * Id for the events handeled by the drewlet: <CODE>textboard</CODE>
	 */
	static final String CODE  = "textboard";



	protected JTextPane E;
	private EditOwnerDocument D;
	//private JSlider changeSpeed;
	public long delayD = 1000;//(long)(60000*Math.tan(50*Math.PI/200));
	// = 60 secondes


	private int dot;
	private int mark;

	private boolean receivinghistory = true;


	private static final int DELAY = 1*1000;
	
	// used to use SimpleTimer, but using java classes is better
	// also, events are sent on the event-dispatching thread
	// is GUI hangs, check for deadlocks caused by bad use of 
	// synchronized
	private Timer networksendtimer;


	/**
	 * Default constructor for the drewlet (called by Class.newInstance() )
	 */
	public TextBoard()
	{
		super();
	}

	/**
	 * Constructor for the drewlet
	 * @param    cdc communication applet part
	 * @see Communication
	 */
	public TextBoard(Communication cdc)
	{
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
	public void constructor(Communication cdc) 
	{
		super.constructor(cdc);
		networksendtimer = new Timer(DELAY,this);
		networksendtimer.start();

	}

	private boolean change = false;

	/*
	 * implement DocumentListener interface
	 * call textUpdate to find out whether we propagate this change to the network.
	 */

	public void insertUpdate(DocumentEvent e) {
		textUpdate(e);
	}

	public void removeUpdate(DocumentEvent e) {
		textUpdate(e);
	}

	public void changedUpdate(DocumentEvent e) {
		// do nothing
	}

	/*
	 * Implement CaretListener, update dot and mark.
	 */
	public void caretUpdate(CaretEvent e) {
		if (!isReplayer()) {
			dot = e.getDot();
			mark = e.getMark();
		}
	}

	/*
	 * Called when the Document has been modified
	 */
	public void textUpdate(DocumentEvent e) {	

		// network update if the owner of the event is not local user
		boolean net = false;
		try {
			net = !((EditOwnerDocumentEvent) e).getOwner().equals("");
		} catch (ClassCastException cce) {
			System.err.println("Textboard: document event isn't an editownerdocumentevent.");
		}

		if (net) {
			eM("Network change not worth propagating");
		} else { 
			eM("got a change worth propagating" + e.getType().toString());
			change = true;
			receivinghistory = false;
		}
	}
 
	public void actionPerformed(ActionEvent arg0) {
		this.textChange();
	}

	/**
	 * Send data to the network if change==true. This method is called by the timer
	 * each second
	 */
	private void textChange() {

		if ( change == false || isReplayer()) {
			return;
		}

		change = false;
		String txt = getString();

		eM("sending text to server with caret at " + dot);
		eM(txt);
		eM("---");

		central_applet.envoiserveur( 
				new XMLTree( getCode() , 
						new XMLTree( "text", 
								XMLTree.Attributes("lang",Config.getLocale().getLanguage(),"caret",""+dot),
								XMLTree.Contents( txt )
						)
				)
		);
	}

	/* overload Component methods*/

	public Dimension getPreferredSize()
	{
		return new Dimension(600, 300); 
	}

	/* implement CooperativeModule interface */


	public String getTitle() {
		return "Textboard";
	}



	public void init() {
		super.init();
		E = new JTextPane();
		D = new EditOwnerDocument();

		E.setDocument(D);


		JScrollPane scrollPane = new JScrollPane(E,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);


//		//Add Components to the Frame.
		
		setLayout(new BorderLayout());


//		JPanel sliderPanel = new JPanel();
//		sliderPanel.setLayout(new BorderLayout());
//		sliderPanel.setSize(new Dimension(80, 80));
//		changeSpeed = new javax.swing.JSlider(JSlider.VERTICAL,0,100,50);
//		sliderPanel.add(changeSpeed , BorderLayout.CENTER);
//
//
//
//		add(sliderPanel);


		add(scrollPane, BorderLayout.CENTER);


		// editable if we are not in replayer mode
		E.setEditable(!isReplayer());

		D.addDocumentListener(this);
		E.addCaretListener(this);


//		changeSpeed.addChangeListener(new javax.swing.event.ChangeListener() {
//			public void stateChanged(javax.swing.event.ChangeEvent e) {
//				//delayD = (int)(Math.tan((double)changeSpeed.getValue()/1000000.0)*1000);
//				//le delayD va de 0secondes à infinite
//				delayD = (long)(60000*Math.tan((double)changeSpeed.getValue()*Math.PI/200));
//				System.out.println("delay :" + delayD/1000);
//			}
//		});
	}


	/**
	 * start drewlet
	 */
	public void start()
	{
	}

	/**
	 * Stop drewlet
	 */
	public void stop()
	{
//		try {
//    		D.stop();
//    		networksendtimer.stop(); // stop the timer
//		//networksendtimer.end(); // stop the timer
//		} catch (Exception e) {
//			
//		}
	}

	/**
	 * Destroy drewlet
	 */
	public void destroy() 
	{

	}

	/**
	 * Clear all data in the drewlet
	 */
	public void clear() 
	{
		removeString(0,D.getLength(),"");
		receivinghistory = true;
		change = false;
	}

	/**
	 * return id string for the drewlet
	 */
	public String getCode() 
	{
		return CODE;
	}

	private boolean debugmode = true;
	private void eM(String s) {
		if (debugmode) System.out.println(s);
	}

	/**
	 * get messages with tag equal to <var>CODE</var>
	 */
	public synchronized void moduleMessageDeliver(String user, XMLTree data) {
		// only use messages where tag equals code and where user is not self (except where we are receiving history 
		// after disconnect)
		//System.err.println("gotmessage"+data.getByTagName("textBoardAction"));
		if (data.tag().equals( getCode() ) && (!user.equals(central_applet.nom) || receivinghistory)) {
			eM("got incoming message from " + user);
			concileIncomingText(data, user);
		}
	}

	private void concileIncomingText(XMLTree data,String user) {
		int datacaret;
		try {
			datacaret = Integer.parseInt(data.getByTagName("text").getAttributeValue("caret")); 
		} catch (Exception e) {
			System.err.println("Textboard: Old trace format, data caret is set at zero");
			datacaret=0;
		}


		class DiffRunnable implements Runnable {

			private TextBoard t;
			private String s;
			private int c;
			private String util; 


			DiffRunnable(TextBoard t,String s, int c,String util) {
				this.s=s;
				this.c=c;
				this.t=t;
				this.util=util; 
			}

			public void run() {
				t.diff(s,c,util);	
			}
		};

		Runnable doDiff = new DiffRunnable(this,data.getText(),datacaret,user);

		try {
			SwingUtilities.invokeAndWait(doDiff);
		} catch (InterruptedException ie) {

		} catch (java.lang.reflect.InvocationTargetException ite) {
			ite.printStackTrace();
		}


	}


	/*
	 * Called by anyone except user interface who wants to modify the document
	 */
	public void insertString(final int i, final String s, String user) {
		try {
			D.insertString(i,s,user);			
		} catch (BadLocationException ble) {
			eM("Badlocationexception, insert at " + s);
		}  
	}

	public void removeString(final int f, final int t, String user) {
		try {
			D.remove(f,t,user);
		} catch (BadLocationException ble) {
			eM("Badlocationexception, remove from " + f 
					+ " to " + t);
		}  
	}

	/*
	 * Called by anyone except user interface who wants to read the document
	 */
	public String getString() {

		String s = "";
		try {
			s = D.getText(0,D.getLength());
		} catch (BadLocationException ble) {
			eM("badlocation exception getting whole text");
		}
		return s;
	}


	public void diff(String s2, int caret2, String user) {

		String s1 = getString();
		int caret1 = dot;

		//eM("merging my message with caret at "+caret1);
		//eM(s1);
		//eM("with their message with caret at "+caret2);
		//eM(s2);


		char[] c1 = s1.toCharArray();
		char[] c2 = s2.toCharArray();
		int l1=c1.length;
		int l2=c2.length;
		
		//invert the arrays so that the diff matches last possible occurence
		//this is for cases where I compare abbbbc and abbbc
		
		for (int i=0;i<c1.length/2; i++) {
			char temp = c1[i];
			c1[i]=c1[c1.length-i-1];
			c1[c1.length-i-1]=temp;
		}
		
		for (int i=0;i<c2.length/2; i++) {
			char temp = c2[i];
			c2[i]=c2[c2.length-i-1];
			c2[c2.length-i-1]=temp;
		}
		
		int max = l1+l2;
		NegIntVector V = new NegIntVector();
		V.setDefault(-1);
		Vector<NegIntVector> vhist = new Vector<NegIntVector>(30,50);

		// run a diff algorithm
		V.set(1,0);
		int distance=max;
		boolean stop=false;
		for (int dist=0; dist<=max ;dist++) {
			for (int diag=-dist; diag<=dist; diag+=2) {
				int x = (diag==-dist || (diag!=dist && V.get(diag-1)<V.get(diag+1)))?
						V.get(diag+1): // vertical move
							V.get(diag-1)+1; // horizontal move

						int y = x - diag;
						while (x<c1.length && y<c2.length && c1[x]==c2[y]) {
							x++;
							y++;
						}
						V.set(diag,x);
						if (x>=c1.length && y>=c2.length) {
							eM("shortest edit script: " +dist);
							distance=dist;
							stop=true;
							break;
						}
			}

			//	    for (int i=0;i<V.length;i++) {
			//	vhist[dist][i]=V[i];
			//}
			vhist.add(V.copy());

			if (stop) {
				break;
			}
		}


		/* Create maps between characters in l1 and characters in l2
		 * map1.get(char in l1) returns the l2 "equivalent" ie. the same char or 
		 * the char corresponding to the previous match (or -1 if no previous match)
		 * 
		 * 0:a - 0:a 
		 * 1:b - 1:b
		 * 2:c - 2:c
		 * 
		 * 0:a - -1:
		 * 1:b - 0:b
		 * 2:c - 1:c
		 * 
		 * 0:a - -1
		 * 1:b / 
		 * 2:c - 0:c
		 * 
		 * 0:a - 0:a
		 * 1:b / 
		 * 2:c / 
		 * 3:d - 1:d
		 * 
		 * 0:a - -1:
		 * 1:b - 0:b
		 * 2:c   1:e
		 * 3:d         // 2 and 3 are mapped to 0. 1 is mapped to 1
		 * 4:g - 3:g   
		 * 
		 */

		/*
		 * map1.get() takes a parameter within s1
		 * map2.get() takes a parameter within s2
		 * 
		 * by convention <name>1 always refers to something within s1
		 *               <name>2 always refers to something within s2
		 *               corresponding names mean something (sometimes)
		 * 
		 */


		IntVector map1 = new IntVector(l1,5);
		IntVector map2 = new IntVector(l2,5);

		map1.setDefault(-1);
		map2.setDefault(-1);

		int y = l2;
		int x = l1;
		for (int dist=distance;dist>0;dist--) {
			int diag=x-y;
			V = vhist.get(dist-1);
			//System.out.println("x:"+ x +" y:"+y);//+" x on next diag:"+V[pval(diag+1)]+" x on prev diag:"+V[pval(diag-1)]);
			while(V.get(diag+1)<x && V.get(diag-1)<x-1) {
				x--;
				y--;
				//	System.out.println("x:"+x +" matches y:"+y);
				//map1.set(x,y);
				//map2.set(y,x);
				//			invert the maps back to original letter order
				map1.set(c1.length-1-x, c2.length-1-y);
				map2.set(c2.length-1-y, c1.length-1-x);
			}
			if (V.get(diag+1)==x) {
				y--;
			} else {
				x--;
			}
		}
		while (x>0) {
			x--;
			y--;
			//map1.set(x,y);
			//map2.set(y,x);
			//			invert the maps back to original letter order
			map1.set(c1.length-1-x, c2.length-1-y);
			map2.set(c2.length-1-y, c1.length-1-x);
		}

		
	
		//map unmapped values.
		int lasttarget=-1;
		for (int i=0;i<c1.length;i++) {
			if (map1.get(i)<0) {
				map1.set(i,lasttarget);
			} else {
				lasttarget=map1.get(i);
			}
		}

		lasttarget=-1;
		for (int i=0;i<c2.length;i++) {
			if (map2.get(i)<0) {
				map2.set(i,lasttarget);
			} else {
				lasttarget=map2.get(i);
			}
		}

		//eM("map1.print");
		//map1.print();
		//eM("map2.print");
		//map2.print();

		// find char in s1 which has match and is immediately before caret1 (map2 of map1)
		// find char in s1 which corresponds to caret2 and has match (by def)
		// note that caret at pos p means it is *before* char at pos p and therefore has just produced
		// char at pos p-1.
		int mymatch1 = map2.get(map1.get(caret1-1));
		int theirmatch1 = map2.get(caret2-1);

		/*
		 * Apply inserts and deletes - lots of copy-paste code in these cases, but refactoring renders code incomprehensible
		 */

		if (!E.isFocusOwner() || receivinghistory) { // apply all inserts and deletes if not focus owner
			eM("applying all inserts and deletes");
			int current2 = s2.length()-1;
			int current1 = s1.length()-1;
			while (current1 >= 0 || current2>=0) {
				if (current1 != map2.get(current2)) {
					String deletion="";
					int start1 = map2.get(current2)+1;
					int end1 = current1+1;
					try {
						deletion = s1.substring(start1,end1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("nonfocus delete: " + deletion);
					removeString(start1,end1-start1,user);
					current1 = map2.get(current2);
				} 
				if (current2!= map1.get(current1)) {
					String insertion="";
					int start2 = map1.get(current1) + 1;
					int end2 = current2+1;
					try {
						insertion = s2.substring(start2,end2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("nonfocus insert: " + insertion);
					insertString(current1+1,insertion,user);
					current2 = map1.get(current1);
				}
				current1--;
				current2--;
			} 
		} else if (mymatch1 < theirmatch1) { 
			// my caret is before their caret. Afford me one set of deletions
			// and then apply all modifications between my potential deletion
			// and the end of the text.
			
			
			eM("my caret is before their caret");

			// go to end of potential suppression (affected by caret1)
			int endofsupp2 = map1.get(caret1-1);
			do {
				endofsupp2++;
			} while (endofsupp2<s2.length() && map2.get(endofsupp2)<caret1);

			// do all modifications from end of document to endofsupp2 
			int current2 = s2.length()-1;
			int current1 = s1.length()-1;
			while (current2 >= endofsupp2) {
				if (current1 != map2.get(current2)) {
					String deletion="";
					int start1 = map2.get(current2) + 1;
					int end1 = current1+1;
					try {
						deletion = s1.substring(start1,end1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("mycaret first delete: " + deletion);
					removeString(start1,end1-start1,user);
					current1 = map2.get(current2);
				} 
				if (current2!= map1.get(current1)) {
					String insertion="";
					int start2 = map1.get(current1) + 1;
					int end2 = current2+1;
					try {
						insertion = s2.substring(start2,end2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("mycaret first insert: " + insertion);
					insertString(current1+1,insertion,user);
					current2 = map1.get(current1);
				}
				current1--;
				current2--;
			}
		} else if (mymatch1 > theirmatch1) { // my caret is after their caret. Apply all modifications between 
			// beginning of text and their caret, affording them one potential
			// suppression
			eM("my caret is after their caret");
			// go to end of potential suppression (effected by caret2)
			int endofsupp1 = map2.get(caret2-1);
			do {
				endofsupp1++;
			} while (endofsupp1<s1.length() && map1.get(endofsupp1)<caret2);

			// do all modifications from endofsupp1+delta1 and caret2+delta2
			// to beginning of document
			int current2 = caret2-1;
			int current1 = endofsupp1-1;
			while (current1 >= 0) {
				if (current1 != map2.get(current2)) {
					String deletion="";
					int start1 = map2.get(current2) + 1;
					int end1 = current1+1;
					try {
						deletion = s1.substring(start1,end1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("theircaret first delete: " + deletion);
					removeString(start1,end1-start1,user);
					current1 = map2.get(current2);
				} 
				if (current2!= map1.get(current1)) {
					String insertion="";
					int start2 = map1.get(current1) + 1;
					int end2 = current2+1;
					try {
						insertion = s2.substring(start2,end2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("theircaret first insert: " + insertion);
					insertString(current1+1,insertion,user);
					current2 = map1.get(current1);
				}
				current1--;
				current2--;
			}
		} else { // carets are at same position - do all modifications
			eM("applying all inserts and deletes carets at same position");
			int current2 = s2.length()-1;
			int current1 = s1.length()-1;
			while (current1 >= 0 || current2>=0) {
				if (current1 != map2.get(current2)) {
					String deletion="";
					int start1 = map2.get(current2)+1;
					int end1 = current1+1;
					try {
						deletion = s1.substring(start1,end1);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("same caret delete: " + deletion);
					removeString(start1,end1-start1,user);
					current1 = map2.get(current2);
				} 
				if (current2!= map1.get(current1)) {
					String insertion="";
					int start2 = map1.get(current1) + 1;
					int end2 = current2+1;
					try {
						insertion = s2.substring(start2,end2);
					} catch (Exception e) {
						e.printStackTrace();
					}
					eM("same caret insert: " + insertion);
					insertString(current1+1,insertion,user);
					current2 = map1.get(current1);
				}
				current1--;
				current2--;
			} 
		}

//		commented out june 2007
//		int match1 = map2.get(map1.get(caret1-1));
//		int match2 = map2.get(caret2-1);


//		//find next matching char after caret2. this is where the inserts and removes end. might be bad as alphabet is limited :/ people might independantly write the same letter in different words...
//		int last2 = caret2-1;
//		do {
//		last2++;
//		} while (last2<s2.length() && map2.get(last2)==map2.get(caret2-1));

//		int last1 = match2;
//		do {
//		last1++;
//		} while (last1<s1.length() && map1.get(last1)==map1.get(match2));

//		String insertion ="";
//		String deletion="";
//		eM(""+match2);
//		eM(""+last1);
//		eM(""+last2);

//		try {
//		insertion = s2.substring(map1.get(match2)+1,last2);
//		deletion = s1.substring(match2+1,last1);
//		} catch (Exception e) {
//		e.printStackTrace();
//		}

//		eM("insert: " + insertion);
//		eM("delete: " + deletion);

//		if (match1!=match2 || !E.isFocusOwner()) {
//		removeString(match2+1,last1-match2-1);
//		insertString(match2+1,insertion,user);
//		} else { // match1 == match2 -> no remove
//		if (match1+1 >= c1.length) {
//		insertString(match1+1,insertion,user);
//		} else if (map1.get(match2)+1 >= c2.length) {
//		removeString(match2+1,last1-match2-1);
//		} else if (c1[match1+1]>c2[map1.get(match1)+1]) {
//		// insert after equivalent
//		insertString(last1, insertion,user);
//		} else { 
//		// insert before equivalent
//		insertString(match1+1, insertion,user);
//		}
//		}
	}

	// intvector classes allow creation of a vector with a default value.
	// this allows me to set numbers 1 and 3 and have 2 return - it also means
	// that all will return, even those out of range.
	private class IntVector {

		protected Vector<Integer> v;
		protected int defaultval = 0;

		IntVector() {
			this(30, 50);
		}

		IntVector(int initialcapacity, int incrementby) {
			this.v = new Vector<Integer>(initialcapacity, incrementby);
		}

		public void setDefault(int i) {
			this.defaultval = i;
		} 

		public void set(int pos, int elem) {
			if (pos<v.size()) {
				v.set(pos,new Integer(elem));
			} else {
				while (pos>v.size()) {
					v.add(null);
				}
				v.add(pos, new Integer(elem));
			}
		}

		public int get(int pos) {
			int retvalue = this.defaultval;
			try {
				retvalue = v.get(pos).intValue();
			} catch (Exception e) {
				// didn't work, return default instead.
				//	System.out.println("arraymighthavebeenoutofbounds:" + pos);
			}
			return retvalue;
		}

		public int length() {
			return this.v.size();
		}

		public void print() {
			for (int i=0; i<this.length(); i++) {
				System.out.println("" + i + ": " + this.get(i));
			}
		}


	}

	class NegIntVector extends IntVector {

		private int pval(int n) {
			return (n<0)?-2*n-1:2*n;
		}

		//inverse of pval
		private int nval(int p) {
			return (p%2==0)?p/2:-(p+1)/2;
		}

		public NegIntVector() {
			super();
		}

		public NegIntVector(int initialcapacity, int incrementby) {
			super(initialcapacity, incrementby);
		}

		public void set(int pos, int elem) {
			super.set(this.pval(pos), elem);
		}

		public int get(int pos) {
			return super.get(this.pval(pos));
		}

		public void print() {
			int size = this.length()-1;
			int min = (size%2==0)?-size/2:-(size+1)/2;
			int max = -min;
			for (int i=min; i<=max; i++) {
				System.out.println("" + i + ": " + this.get(i));
			}
		}

		public NegIntVector copy() {
			NegIntVector clone = new NegIntVector();
			clone.setDefault(this.defaultval);
			for (int i=0; i<super.v.size(); i++) {
				if (super.v.get(i)!=null) {
					clone.set(nval(i),super.get(i));
				}
			}
			return clone;
		}

	}

	public class EditOwnerDocument extends DefaultStyledDocument implements ActionListener { //on utilise un HTMLDocument

		// owner lasts the duration of a change
		private String owner;
		private EditHistory history;
		private Timer updateAwarenessTimer;
//		!!!! there is no garantee that this variable points something within 
//		one of the history's list.
		private Modif firstnonblack = null; 
		
		EditOwnerDocument() {
			super();
			history = new EditHistory();
			updateAwarenessTimer = new Timer(1000, this);
			if (!isReplayer()) {
				updateAwarenessTimer.start();				
			}
		}

		// intercept user interface insert
		public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
			insertString(offset,str,"");
		}

		// insert with owner attached
		public void insertString(int offs, String str, String owner) throws BadLocationException {
			writeLock();
			try {
				SimpleAttributeSet a = new SimpleAttributeSet();
				Color couleur= getColor(owner);
				eM("color " + couleur);
				StyleConstants.setForeground(a,couleur);
				this.owner=owner;
				// donner le bon attribute set.
				super.insertString(offs,str,a);
				history.insertModif(offs, str.length(), owner);
				if (firstnonblack == null) {
					firstnonblack = history.lastbydate;
				}
				//history.print();
			} finally {
				this.owner=null;
				writeUnlock();
			}
		}

		public void remove(int offs, int len) throws BadLocationException {
			remove(offs,len,"");
		}

		public void remove(int offs, int len, String owner) throws BadLocationException {
			writeLock();
			try {
				this.owner=owner;
				super.remove(offs,len);
				while (firstnonblack!=null && firstnonblack.offs>=offs && 
						firstnonblack.length+firstnonblack.offs<=len+offs) {
					firstnonblack=firstnonblack.nextbydate;
				}
				history.removeModif(offs, len);
				history.check();
			} finally {
				this.owner=null;
				writeUnlock();
			}
		}	

		protected void fireUpdate(DocumentEvent e) {
			DocumentEvent.EventType type = e.getType();
			if (type == DocumentEvent.EventType.REMOVE) {
				super.fireRemoveUpdate(new EditOwnerDocumentEvent(e,this.owner));
			} else if (type == DocumentEvent.EventType.INSERT) {
				super.fireInsertUpdate(new EditOwnerDocumentEvent(e,this.owner));
			} else { 
				super.fireChangedUpdate(new EditOwnerDocumentEvent(e,this.owner));
			}
		}

		protected void fireRemoveUpdate(DocumentEvent e) {
			this.fireUpdate(e);
		}

		protected void fireInsertUpdate(DocumentEvent e) {
			this.fireUpdate(e);
		}

		protected void fireChangedUpdate(DocumentEvent e) {
			this.fireUpdate(e);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			long time = System.currentTimeMillis();
//			if (firstnonblack==null) { useful for debugging
//				history.printByDate();
//				history.print();
//				System.exit(0);
//			} else {
//				firstnonblack.print();
//			}
			for (Modif mod = firstnonblack; mod!=null; mod=mod.nextbydate) {
				SimpleAttributeSet a = new SimpleAttributeSet();
				Color color = getColor(mod.owner);
				long delta = time - mod.createTime;
				
				if (delta > delayD) {
					color = color.darker();
					delta = delta/7;
				}	
				if (delta > delayD) {
					color = color.darker();
					delta = delta/2;
				}	
				if (delta > delayD) {
					color = color.darker();
					delta = delta/2;
				}	
				if (delta > delayD) {
					color = Color.BLACK;
						firstnonblack = mod.nextbydate;
				}	
				StyleConstants.setForeground(a,color);
				this.setCharacterAttributes(mod.offs, mod.length, a, true);
			}
		}
		
		public void stop() {
			updateAwarenessTimer.stop();
			//history.reset();
			System.err.println("document stopped");
		}
	}


	class EditOwnerDocumentEvent implements DocumentEvent {

		DocumentEvent e;
		String owner;

		EditOwnerDocumentEvent(DocumentEvent e, String owner) {
			this.e = e;
			this.owner = owner;
		}


		public DocumentEvent.ElementChange getChange(Element elem) {
			return e.getChange(elem);
		}

		public Document getDocument() {
			return e.getDocument();
		}

		public int getLength() {
			return e.getLength();
		}

		public int getOffset() {
			return e.getOffset();
		}


		public DocumentEvent.EventType getType() {
			return e.getType();
		} 


		public String getOwner() {
			return this.owner;
		}

	}

}