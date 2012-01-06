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
 * File: PanelManager.java
 * Author:
 * Description:
 *
 * $Id: PanelManager.java,v 1.10.2.2 2007/06/29 08:50:47 collins Exp $
 */

package Drew.Client.Rejoueur;

import java.io.*;
import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;
import java.util.*;


class PanelManager {
private VeryBufferedReader in;
private ConnectionEcoute th;
private List<XMLTree> drewTrace;
private List<Long> drewEvents;
private int position;
private int target;

private int     P  = 0;     // how many line to read ( 0 stop, 1 step, -1 all )
private boolean RT = true;  // true for Realtime, false for as fast as possible

private long prev = Long.MAX_VALUE; // pour le premier "step"
private long curr;
private long delta = 0;

	PanelManager(ConnectionEcoute th,VeryBufferedReader vbr) {
		in = vbr;
		drewTrace=new LinkedList<XMLTree>();
		drewEvents=new Vector<Long>();
		//no more stupid very buffered reader (or maybe not that stupid... I can't tell)

		fillTraceList();
		this.position=-1;
		this.target=-1;
		this.th = th;
		System.err.println("panel manager created, length of drewEvents: " + drewEvents.size());
	}
	
	private void fillTraceList() {
		try {
			in.seek(0);
			while(true) {
				XMLTree e = in.nextEvent();
				drewEvents.add(new Long(getEventTime(e)));
				drewTrace.add(e);
			}
		} catch (IOException ioe) {
			// in is empty
		}
	}
//	private Interaction i = null;

	synchronized Interaction read() throws IOException {
	XMLTree event;
	long n;
	
		while ( position >= target ) {
			try {
				if( Config.getDebug(8) ) {
					System.err.println( "IRead wait");
					}
				wait();
			}
			catch( InterruptedException e ) {
			}
		}


		// reading the next line in the file and store the current event date
    	position++;
    	event = drewTrace.get(position);
    	System.err.println("Drew replayer: sending to event "+position);

//		
  	    Interaction i = new Interaction( event );
//		curr = i.getDate().getTime();
//		P--;
//
//		// store date for prev event and compute time between events
//		n = curr - prev; 
//		if( Config.getDebug(8) ) { System.err.println( "IRead " + curr + "," + prev + "|" + n + " : " + i.toString() ); }
//		prev = curr;	
//
//		if( RT == true ) {
//			try {
//				if( n > delta ) wait( n - delta );
//			}
//			catch( InterruptedException e ) {
//			}
//		}
		return i;
	}
	
	private long getEventTime(XMLTree event) {
		Interaction i = new Interaction (event);
	    return i.getDate().getTime();
	}

	// should be called to position the drewlets at a specific timestamp
	synchronized void goTo(long timestamp) throws IOException{
		System.err.println("DREW replayer: goto "+timestamp);
		// mark where we are 
		//in.mark(2048);
		
		// get next event timestamp (curr)

		//XMLTree event = in.nextEvent();
		
		if (drewTrace.isEmpty()) {
			fillTraceList();
		}
		
		if (target>=0 && getEventTime(drewTrace.get(target))>timestamp) {
			// need to rewind
			rwnd();
		}
		
		// set the target to be the last of the set of events at timestamp
		int offset=0;
		while ((target+offset+1<drewTrace.size()) && 
				    (getEventTime(drewTrace.get(target+offset+1))<=timestamp)) {	
			offset++;
		}
		setTarget(target+offset);
		
		// check if its the event we are looking for
//		if(curr == timestamp) {
//			// it is, so replace the buffer 1 event befor
//			//in.reset();
//			// then play the event
//			System.err.println("PanelManager : stepping 1 (normal next event)");
//			step(1);
//		} else { // not the event we are looking for
//			// if event in the futur
//			if( curr < timestamp ) {
//				System.err.println("PanelManager : event still in futur-> Recursive call");
//				//in.reset();
//				//step(1);
//				// recursive call, if event is in the futur, by advancing one by one we will find it
//				goTo(timestamp);
//			} else { // event is in the past
//				System.err.println("PanelManager : event in the past-> rewinding");
//				// rewind buffer
//				// count events until we find the one looking for
//				// rewind buffer
//				// then play the counted number of events
//				// (thats what the play function does
//				goToInThePast(timestamp);
//			}
		//}		
	}
	
	//	 should be called to position the drewlets at a specific timestamp IN THE PAST
	synchronized void goToInThePast(long timestamp) throws IOException {
		// to count number of events to play
		int numberOfEvents = 0;
		
		// rewind
		in.seek(0);

		XMLTree event = in.nextEvent();
		Interaction i = new Interaction (event);
		long curr = i.getDate().getTime();
		
		while( curr != timestamp ) {
			event = in.nextEvent();
			i = new Interaction(event);
			curr = i.getDate().getTime();
			numberOfEvents ++;
		}	
		
		// replace at beginning of list of events
		in.seek(0);

		if( Config.getDebug(9) ) System.err.println( "PLAY " + P + "/" + RT + " " +timestamp);
		
		step(numberOfEvents);
	}

	
	/**
	* Real time play
	*/
	synchronized void play() {
		P = -1; RT = true;
		notifyAll();
		if( Config.getDebug(9) ) System.err.println( "PLAY " + P + "/" + RT );
	}

	synchronized void stop() {
		P = 0; RT = false;
		notifyAll();
		if( Config.getDebug(9) ) System.err.println( "STOP " + P + "/" + RT );
	}

	synchronized void step(int i) {
                P = i; RT = false;
                notifyAll();
                if( Config.getDebug(9) ) System.err.println( "STEP " + P + "/" + RT );
        }

	synchronized void step() {
		P = 1; RT = false;
		notifyAll();
		if( Config.getDebug(9) ) System.err.println( "STEP " + P + "/" + RT );
	}
	
	synchronized void setTarget(int value) {
		if (target==value) return; //do nothing
		target=value;
		notifyAll();
	}

	synchronized void rwnd() {
		P = 0; RT = false;
		target=-1;
		// reset file reading and clear drewlet content
		try {
			in.seek(0);
			position=-1;
			th.central_applet.modules.clear();
		}
		catch(IOException e) {}
		notifyAll();
		if( Config.getDebug(9) ) System.err.println( "RWND " + P + "/" + RT );
	}

	synchronized void rwnd_silent() {
                P = 0; RT = false;
                // reset file reading 
                try {
                        in.seek(0);
                }
                catch(IOException e) {}
                notifyAll();
                if( Config.getDebug(9) ) System.err.println( "RWND " + P + "/" + RT );
        }

	/**
	* Play as fast as possible
	*/
	synchronized void ffwd() {
		P = -1; RT = false;
		notifyAll();
		if( Config.getDebug(9) ) System.err.println( "FFWD " + P + "/" + RT );
	}

	boolean stepWise() {
		if (RT) return false;
		else return true;
	}

	public List<Long> getEvents() {
		if (drewEvents.isEmpty()) {
			in.waitReady();
			fillTraceList();
		}
		return drewEvents;
	}
}
