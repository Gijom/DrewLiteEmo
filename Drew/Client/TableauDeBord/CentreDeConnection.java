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
 * File: CentreDeConnection.java
 * Author: Ph. Jaillon
 * Description:
 *
 * $Id: CentreDeConnection.java,v 1.28  2011/01/04 15:53:58  Guillaume Chanel (GC) Exp $
 *
 * $Log: CentreDeConnection.java,v $
 *
 * Revision 1.28  2011/01/04 15:53:58  Guillaume Chanel (GC)
 * take the client time into account
 *
 * Revision 1.27.2.1  2007/10/31 14:22:58  collins
 * tokentextboard, replayerstuff, better textboard
 *
 * Revision 1.27  2007/02/20 16:03:41  collins
 * This is a major update , updating drewLite, updating several drewlets (TextBoard, Chat), adding drewlets (Viewboard, SimpleAwareness, ImageViewer), and adding the TraceViewer (exports trace files to html/exel, splits drewLite traces, and replay)
 *
 * Revision 1.26  2006/04/28 08:35:29  girardot
 * Comments updating ; replacement of "show" by "setVisible"
 *
 * Revision 1.25  2004/01/22 08:37:03  jaillon
 * Some modifications in the replayer interface
 * (messages are in xml and more readable). 
 * Correct a bug in the player (now all threads end when destroy is called)
 *
 * Revision 1.24  2004/01/20 09:43:26  jaillon
 * Update locale initialisation
 *
 * Revision 1.23  2004/01/14 13:48:56  jaillon
 * Add error handling for embeded drewlet
 *
 */

package Drew.Client.TableauDeBord;

import java.net.*;
import java.io.*;
import java.applet.AppletContext;
import java.awt.*;
import java.awt.event.*;

import Drew.Util.XMLmp.*;
import Drew.Client.Util.*;

/**
*  Applet derived from class "communication", used
*  by drew in discussion mode
* @see    Drew.Client.Util.Communication
*/
public class CentreDeConnection extends Communication implements Runnable {

/**
* Room where the user is initially
* @see Drew.Client.TableauDeBord.TableauDeBord
*/
public  String room = "Hall";

/** 
* Name of the protocol 
*/
public String protocole = "http";

/** 
* Server where the applet comes from 
*/
public String host = "localhost";

/**
* Server need dynamic discovery
*/
private boolean DYNAMICLOOKUP = false;

/** 
* Name of the default subject
*/
public String sujet = "default";

/** 
* Documentation directory 
*/
public String laide = ".";

/** 
* Port on which the server is listening
*/
public int port_serveur = 80;

/**
* ancestore applet context
*/  
public  AppletContext context;

/**
* @see ConnectionEcoute
*/
ConnectionEcoute ecoute;

/**
* Module list
* @see CooperativeModule
*/
CooperativeModuleList modules;

//************ a faire disparaitre
/**
* Dashboard window
* @see TableauDeBord
*/
TableauDeBord frame_tab_de_bord = null;

/**
* access to the localization mechanism
*/
Drew.Util.Locale comment;

/** 
* The XML printer
*/
//PJ private XMLPrinter outpw;

/**
* The application main thread
*/
Thread mainThread = null;

	/**
	* Default constructor - initialize module container
	*/
	public CentreDeConnection() {
		super();
		modules = new CooperativeModuleList();
		Config.setcdc( this );
	}

	/**
	 * provide access to the control pannel
	 */
	public TableauDeBord controlPanel() {
		return frame_tab_de_bord;
	}

	protected void finalize() throws Throwable {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : finalize"); }
		super.finalize();
    	}

	// Overloaded applet methods

  	/**
	* Initialization of the applet ; creation of a button to run the application
	* Read applet parameters
  	*    <pre>
  	*        &lt;sujet&gt; = "..." subject numebr (mandatory)
  	*        &lt;server&gt; = "..." sioux server host 
  	*        &lt;port&gt; = "..." sioux server port
  	*        &lt;room&gt; = "..." initial room
  	*    </pre>    
  	* Get the execution context of the applet
  	*/
  	public void init() {

		//getting system properties isn't alowed to applets
		//System.getProperties().list( System.err );

		// Parameters about the web server where the applet comes from
		host = getCodeBase().getHost();
		protocole = getCodeBase().getProtocol();
		port_serveur = getCodeBase().getPort();

		// first, set correct locale 
		Config.setLocale(this);

		// parameters about the sioux server
		{
			String dummy;

			dummy = getParameter("port");
    			if( dummy == null ) {
				port = 32000;
			}
			else {
    				port = Integer.parseInt(dummy);
			}


			// Set the sioux server to the value provided by <param server="..">. If server value
			// isn't set or is 0.0.0.0, set server as localhost and DYNAMICLOOKUP as true
			// see connection for dynamic server discovery
			try {
				dummy = getParameter("server");
    				if( dummy == null || dummy.equals( "0.0.0.0") ) {
					// Set server as localhost 
					DYNAMICLOOKUP = true;
					server=InetAddress.getByName(host);
				}
				else {
	        			server=InetAddress.getByName(dummy);
				}
			}
			catch( UnknownHostException e ) {
				e.printStackTrace();
			}
	
    			// look for help file
			dummy = getParameter("help") == null ? getParameter("help") : getParameter("aide");
    			if( dummy == null ) {
				dummy = "";
			}
    			laide = getCodeBase().getFile() + dummy;

			dummy = getParameter("room");
    			if( dummy != null ) {
            			room = this.getParameter("room");
			}

			// get subject identification
			dummy = getParameter("sujet");
			if (dummy != null) {
				sujet = "trace.utf8";
			}
			else {
				String msg = "Drew Error : No value provided for sujet parameter";
				context.showStatus(msg); addErrorMessage(msg);
				System.err.println("CentreDeConnexion : no sujet parameter");
				sujet = "sujet-default.utf8";
			}

			dummy = getParameter("debug");
                        if( dummy != null ) {
                                Config.setDebug( Integer.parseInt(dummy) );
                        }

			dummy = getParameter("useHTTP");
                        if( dummy != null ) {
                                Config.setHTTPConnection( dummy );
                        }

			dummy = getParameter("standalone");
                        if( dummy != null ) {
                                Config.setStandAlone( dummy.equalsIgnoreCase("true") );
                        }

			dummy = getParameter("controlpanel");
                        if( dummy != null ) {
                                Config.setControlPanel( dummy.equalsIgnoreCase("true") );
                        }

			// change the "coche" parameter as interactive.drewlet parameter
 			dummy = getParameter("interactive.drewlet") == null ? getParameter("interactive.drewlet") : getParameter("coche");
                        if( dummy != null ) {
                                Config.setChooseDrewlet( dummy.equalsIgnoreCase("true") );
                        }

			// change the "piece" parameter as interactive.room parameter
			dummy = getParameter("interactive.room") == null ? getParameter("interactive.room") : getParameter("piece");
                        if( dummy != null ) {
                                Config.setChangeRoom( dummy.equalsIgnoreCase("true") );
                        }

			// to add a mark to each event
			dummy = getParameter("mark");
                        if( dummy != null ) {
                                Config.setMark( dummy );
                        }
		}

		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : init"); }

    		comment = new Drew.Util.Locale("Drew.Locale.Client.TableauDeBord",Config.getLocale());
    		context = this.getAppletContext();

		if( Config.getControlPanel() == true ) {
			frame_tab_de_bord = new TableauDeBord("Tableau de bord", this);
			//frame_tab_de_bord.pack();
			add(frame_tab_de_bord);
		}
		else {
			// put here code for inlined drewlet, the subject parameter is used again in start method
			String dummy = getParameter("userid");
    			if( dummy == null ) {
				String msg = "Drew Error : No value provided for userid parameter";
				context.showStatus(msg); addErrorMessage(msg);
				System.err.println("CentreDeConnexion : no userid parameter");
			}
			if( Config.getDebug() ) { System.err.println("CentreDeConnexion : userid = " + dummy); }

			dummy = getParameter("module");
			if( dummy != null ) {
				CooperativeModule m = Config.newInstance( dummy, (Communication)this, false ); // a module without window
				modules.add( dummy, m ); //modules.init();

				setLayout(new java.awt.GridLayout(1,0));
				add( (Panel)m );
				validate();
			}
		}
  	}

	public void start() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : start"); }

		if( mainThread == null ){
			mainThread = new Thread( this, "the drew app");
			mainThread.start();
		}

		if( Config.getControlPanel() == false ) {
			String id = this.getParameter("userid");
			if( Config.getDebug() ) { System.err.println("CentreDeConnexion : connection started for " + id); }
			try {
				connection( id );
				validate();
			} 
			catch( IOException e ) { 
				context.showStatus( e.toString() ); 
				addErrorMessage(e );
				displayErrorMessage();
			}
		}
	}

	public void stop() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : stop"); }
		//destroy();
	}

  	public void destroy() {
		if( Config.getDebug() ) { System.err.println("CentreDeConnexion : destroy"); }
		System.err.println("ok1");
		quitterModules();
		System.err.println("ok2");
		// close error window 
		if( errFrame != null ) { errFrame.dispose(); errFrame=null; }
		mainThread=null;
		quitter();
		// peut-etre la mettre synchronised et faire un notify pour tout finir
	}

/** TextArea to display error messages to the user */
private StringWriter errString = new StringWriter();
private PrintWriter errStream = new PrintWriter( errString );

	void errorOccur( Exception e ) {
		if( frame_tab_de_bord == null ) {
			addErrorMessage( e);
			displayErrorMessage();
		}
		else {
			//frame_tab_de_bord.messageDeliver( e.toString() );
			System.err.println( e.toString() );
		}
	}

	private void addErrorMessage( Exception e ) {
		e.printStackTrace( errStream);
	}

	private void addErrorMessage( String msg ) {
		errStream.println( msg );
	}

	private Frame errFrame = null;
	private TextArea errArea = null;

	private void displayErrorMessage() {
		if( mainThread == null ) return;

		if( errArea == null ) {
			errArea = new TextArea("", 10, 30, TextArea.SCROLLBARS_VERTICAL_ONLY);
			errArea.setEditable(false);
		}
		errArea.setText(errString.toString());

		if( errFrame == null ) {
			errFrame = new Frame( "Drew errors" );
			errFrame.setLayout( new BorderLayout() );
			errFrame.add( errArea, BorderLayout.CENTER );
			errFrame.pack();
			errFrame.validate();
			errFrame.addWindowListener( 
				new WindowAdapter() { 
					public void windowClosing(WindowEvent e) {
						if( (errFrame != null) && errFrame.isShowing() ) {
							errFrame.dispose();
							errFrame = null;
						}
					}
				}
			);
			// errFrame.show();  // Deprecated => setVisible
			errFrame.setVisible(true);
		}
	}

  	/**
  	* Connection to the GrandSioux server
	* Send a message to the dashbord and the GrandSioux server
	* Create a thread to listen to the GrandSioux server
  	* @param texte is a string to transmit the user's name to the server
  	*/ 
  	public void connection(String texte) throws IOException {
		// User click the connect button, now it's time to find a server if not in standalone mode
		if( (DYNAMICLOOKUP == true) && (Config.getStandAlone()==false) ) {
			try {
				server =  Drew.Util.UDP.Scanner.findServer();
				System.err.println( "Drew server found at " + server );
			}	
			catch(IOException e) {
			}
		}

    		try {
        		nom = texte;    
        		if((nom == null) || nom.equals("")) {
            			// Please indicate your name
            			if( frame_tab_de_bord != null) {
					frame_tab_de_bord.messageDeliver(comment.format("msg001"));
            				reconnection();
				}
				else {
					String msg = comment.format("msg001");
					context.showStatus(msg); addErrorMessage(msg);
					throw new IOException( "No user name specified" );
					}
        		}
        		else {
					// starting access to the server
					// send expected informations
					// message "contacting server"
					if( frame_tab_de_bord != null) {
						frame_tab_de_bord.messageDeliver(comment.format("msg002"));
				}
				else {
					String msg = comment.format("msg002");
					context.showStatus(msg);
				}
	
				if( Config.getStandAlone() ) {
					System.out.println( "CentreDeConnection NullSocket");
            				acces_serveur = new Drew.Client.Util.XMLSocket.NullSocket( this );
				}
				else if(  Config.getHTTPConnection() != null ) {
					String id = Config.getHTTPConnection() + "?user="+nom+
							"&host="+InetAddress.getLocalHost().getHostAddress()+
							"&cookie="+Long.toHexString((long)(Math.random()*10000000.));
					System.out.println( "CentreDeConnection HTTPSocket : " + server.getHostAddress() + ":" + port + id);
            				acces_serveur = new Drew.Client.Util.XMLSocket.HTTPSocket(new
					                       URL("http",server.getHostAddress(),port,id));
				}
				else {
					System.out.println( "CentreDeConnection connect : " + server + " : " + port);
					acces_serveur = new Drew.Client.Util.XMLSocket.Socket(server, port);
				}
	
				ecoute = new ConnectionEcoute(this);
				ecoute.start();
				System.err.println( "New connection for " + nom + "," +  room + "," + sujet);
				send2server( 
					new XMLTree( "server",
						new XMLTree( "connection",
							    XMLTree.Attributes("state", "new"), 
								XMLTree.Contents(
									new XMLTree( "identity" ).setAttribute("user", nom),
									new XMLTree( "room" ).setAttribute("where", room),
									new XMLTree( "type" ).setAttribute("event", "room"),
									new XMLTree( "subject", sujet ),
									new XMLTree( "language").setAttribute("lang",Config.getLocale().getLanguage() )
							)
						)
					)
				);

				//PJ 05232003 module can now do network

				modules.init(); 
				getState();
				modules.start();
        		}
    		}
    		catch (Exception e) { 
			System.err.println("Connection error: "+e); 
			addErrorMessage("Unable to connect to " + server.getHostAddress() + ":" + port );
			if( Config.getDebug()) e.printStackTrace();
    			reconnection();
			throw new IOException( e.getMessage() );
    		}
  	}

	public synchronized void run() {
		System.out.println("CentreDeConnexion : run start");
		while( mainThread != null ) { // la condition qui fait qu'on quitte
			try {
				System.out.println("CentreDeConnexion : wait");
				wait();
			}
			catch( InterruptedException e ) {
				System.out.println("CentreDeConnexion : wait interupted");
			}
		}	
		System.out.println("CentreDeConnexion : run end");
		// PJ 20011102 (le soir a jouangrand)
		//ecoute.abordConnexion();
		//PJ 21/01/2004 quitterModules();
		System.out.println("CentreDeConnexion : quit");
	}

  	/**
  	* Suppress all modules 
	* Ask GrandSioux to close the connection
  	*/
	public void quitterModules() {
	
		modules.destroy();

		try {
			send2server(
				new XMLTree( "server",
					new XMLTree( "connection" ).setAttribute( "state", "end" )
				)
			);

			// code pour la deconexion explicite
			if( acces_serveur != null ) acces_serveur.close();
			ecoute.abordConnexion();	// le thread ferme sa socket et va s'arreter
    		}
			catch (IOException e) { 
			}
    		catch (NullPointerException e) { 
				// le serveur est par terre 
				if( Config.getDebug() ) {
        			System.err.println("quitterModules: "+e);
					e.printStackTrace();
			}
		}        

		// PJ (10/2001) Bentrer.setEnabled(true);
  	}
                  
  	public synchronized void quitter() {
		// Check when not null
		if( frame_tab_de_bord != null ) frame_tab_de_bord.quitter();
		// PJ (10/2001) Bentrer.setEnabled(true);

		notify();
	}

  	/**
  	* Re-initialize the dashboard and other modules
  	* in case of a deconnection
  	*/
  	public void reconnection () {

		modules.stop();

		if( frame_tab_de_bord != null) {
				frame_tab_de_bord.messageDeliver(comment.format("msg004")); 
				// ATTENTION VOUS ETES DECONNECTE
				frame_tab_de_bord.messageDeliver(comment.format("msg005"));
				frame_tab_de_bord.messageDeliver(comment.format("msg004"));
				frame_tab_de_bord.Bdeconnecter.setEnabled(false);
				frame_tab_de_bord.Bconnecter.setEnabled(true);
				frame_tab_de_bord.Bliste.setEnabled(false);
		}

		try {
			if(  acces_serveur != null ) acces_serveur.close(); //definitively close connection
		}
		catch( IOException e ) {}
	}

  	/**
  	* Send a message to the GrandSioux server
  	* @param event message to be sent
  	*/
	private void send2server( XMLTree event, Duration d ) throws IOException {
		XMLTree x = new XMLTree( "event-request" );
		x.setContents( event );

		if( Config.getMark() != null ) { 
			x.setAttribute( "mark", Config.getMark() ); 
		}
		if( d != null) {
			//x.insert( new XMLTree("time", new XMLTree("duration", d) ) );
                        //GC: the line above was replaced by the following lines to add the
                        //time (the client time) information of the end of the event
                        XMLTree timeTree = new XMLTree("time");
                        timeTree.add(new XMLTree("clientdate", d.getStampTime()));
                        timeTree.add(new XMLTree("duration", d));
                        x.insert(timeTree);                        
		}
                //else
                //        x.insert( new XMLTree("time", new XMLTree("clientdate", 321) ) );

		if( Config.getDebug() ) {
			x.delimiters( 2 );
			System.err.println("CentreDeConnection.send2server " + x.toString() );
		}
                
		if( acces_serveur == null ) throw new IOException( "Lost connection with server" );
		acces_serveur.write( x );	
		acces_serveur.flush();
	}

	private void send2server( XMLTree event ) throws IOException {
		send2server( event, null );
	}

	public void envoiserveur (XMLTree event, Duration d) {
		try {
			send2server( event, d );
		}
		catch( IOException e) {
			String msg = "Drew Error : lost connection with server";
			context.showStatus( msg );
			System.err.println("CentreDeConnection.envoiserveur : " + e.toString());
			//reconnection();
			if( Config.getControlPanel() == false ) {
				addErrorMessage( msg ); addErrorMessage( e );
				displayErrorMessage();
			}
		}
	}

	public void envoiserveur (XMLTree event) {
		envoiserveur( event, null );
	}

	/**
	* This drewlet ask for a synchro 
	*/
	public void getState() {
		if( Config.getStandAlone() == false ) modules.clear();
		try {
			send2server( 
				new XMLTree( "server",
					new XMLTree( "syn" )
				)
			);
		}
		catch( IOException e ) {
			reconnection ();
		}
	}

	/**
	* The communication thread uses this method to send messages from the server
	* to the different modules and the dashboard.
	*/
	/*
	public void messageDeliver(String code, String origine, String data) {
		if( frame_tab_de_bord != null) frame_tab_de_bord.messageDeliver(code, origine, data);
		modules.messageDeliver(code, origine, data);
	}
	*/
	/** XML version */
	public void messageDeliver(String user, XMLTree data) {
		if( frame_tab_de_bord != null) frame_tab_de_bord.messageDeliver(user, data);
		modules.messageDeliver(user,data);
	}

  	/**
  	* Get the applet html page and displays it in the browser
  	*/
  	public void sujet() {
    		URL url = this.getDocumentBase();
    		context.showDocument(url,"drew_main_window");
  	}

  	/**
  	* Get the on line html help page and displays it in the browser
  	*/
  	public void aide() {
    		try {
        		URL url = new URL(protocole, host, port_serveur,laide);

			envoiserveur(
				new XMLTree( "server",
					new XMLTree( "help" ).setAttribute("url",url.toString())
				)
			);

        		//PJ (10/2001) context.showDocument(url);
        		context.showDocument(url, "drew_main_window");
    		}
    		catch(MalformedURLException e) {
        		System.err.println("URL : "+e);
    		}
  	}
}
