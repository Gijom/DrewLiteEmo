package Drew.Client.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;

import Drew.Client.Util.Communication;
import Drew.Util.XMLmp.XMLTree;

public class TokenRing {

	/** Le code du module
	 *  Comme pour la Drewlet principal ce code est utilis� lors de la reception
	 *  des messages pour d�terminer si le message doit �tre trait� par le module
	 *  Ce code s'ajoute au code de la Drewlet
	 */
	static final String CODEModule = "Token";

	/** Centre de communication et CODE de la Drewlet
	 *  Ces deux param�tres proviennent de la Drewlet principale
	 */
	private Communication central_applet ;
	private String CODE;
	private TokenListener drewlet;


	/** Composant utilis� pour cr�er le panel de control
	 *  Ils permettent la prise du jeton ainsi que sa 
	 *  lib�ration. 
	 */
	private JPanel panelJeton;
	private JPanel panelButton;
	private JButton takeTokenButton;
	private JButton freeTokenButton;
	private JLabel jLabel1;

	/** Les dur�es des dif�rents timer du Token Schem
	 *  Ce token Schem comprant deux timer :
	 *  	- Un v�rifiant toutes des DELAY_UPDATE milliseconde qu'une modification � bien
	 *  	�t� effectu�. Si aucune modification n'a �t� effectu�e au bout de DELAY_TIMEOUT
	 *      milliseconde le jeton est alors lib�r�.
	 *      - Un v�rifiant que les personnes poss�dant le jeton sont bien toujours pr�sente
	 */
	private static final int DELAY_CHECKOWNER = 10000;
	private static final int DELAY_CHECKOWNERTIMEOUT = 5000;
	private static final int NB_SEC = 12;
	private static final int DELAY_TIMEOUT = NB_SEC*1000;
	private static final int DELAY_UPDATE = 500;
	protected Vector<String> userFifo;

	/** Timer servant � v�rifier que la personne ne garde pas le jeton infiniment
	 *  On v�rifit toues les DELAY_UPDATE,si la personne a effectuer une modif.
	 *  Si la perseonne n'a rien fait pendant un temps de DELAY_UPDATE
	 *  On rend le jeton
	 */
	private Timer t_owner;

	/** timer v�rifiant que le owner courant est toujours en ligne
	 *  au bout de DELAY_CHECKOWNER secondes sans nouvelles -> ping
	 *  au bout de DELAY_CHECKOWNERTIMEOUT secondes sans pong si pas de nouvelles -> expulsion
	 */
	private Timer t_exist_owner; 

	/** Les autres variables utiles au preogramme
	 *  - le boolean isOwner permet de savoir si la personne poss�de la jeton
	 *  - le long timeLastModif rend compte du temps de la derni�re modification effectu�
	 *  - le boolean pingWithoutPong permet de savoir si une personne a �t� ping�e sans succes
	 *  - la String nameOfPingWithoutPong permet de connaitre le nom de la personne ping�e sans succes
	 */
	private boolean isOwner;
	private long timeLastModif;
	private boolean pingWithoutPong;
	private String nameOfPingWithoutPong;
	private boolean isreplayer=false;

	/** Constructeur de la class TokenSchem
	 *  Il prend en param�tre le centre de communication de la drewlet
	 *  et son code pour les messages.
	 *  Une fois cr�er il ne faut pas oublier d'appler la m�thode init afin de 
	 *  l'initialiser toutes les vairiables et de lancer les diff�rents timer
	 */
	public TokenRing(Communication cdc, String code, TokenListener drewlet) {

		//Initialisation des variables cdc et code
		this.central_applet = cdc;
		this.drewlet=drewlet;
		this.CODE=code;
	}

	public void setReplayer() {
		isreplayer = true;
	}
	

	/** Methode utiliser pour initialiser les variable, GUI, et lancer les timer
	 *  Il faut en effet initialiser les diff�rentes variables et lancer le timer
	 *  pingant les utilisateur. L'autre timer n'est naturellemnt d�mar� que lorsque
	 *  l'on obtient le jeton et arr�ter lorsque l'on a plus le jeton
	 */
	public void init(boolean showTakeButton, boolean showFreeButton) {
		
		//Creation des boutons du jeton
		takeTokenButton = new JButton();
		takeTokenButton.setText("Prendre le jeton");
		takeTokenButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				takeTokenButtonActionPerformed(evt);
			}
		});
		freeTokenButton = new JButton();
		freeTokenButton.setText("Lib\u00e9rer le jeton");
		freeTokenButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				freeTokenButtonActionPerformed(evt);
			}
		});

		jLabel1 = new JLabel();


		//Creation du pannel regroupant les boutons du jetons
		panelJeton = new JPanel();
		panelButton = new JPanel();
		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(Color.BLUE);
		panelJeton.setLayout(new BorderLayout());
		jLabel1.setText("Le jeton est libre                                ");
		//panelInfo.add(jLabel1);
		//panelButton.setLayout(new java.awt.GridLayout(1, 2));
		if (showTakeButton) {
			panelButton.add(takeTokenButton);
		}

		if (showFreeButton) {
			panelButton.add(freeTokenButton);
		}
		panelJeton.add(panelButton,BorderLayout.WEST);
		panelJeton.add(jLabel1,BorderLayout.CENTER);

		isOwner = false;
		timeLastModif = 0;
		pingWithoutPong = false;
		userFifo = new Vector<String>();

		//Cr�ation du timer servant � la lib�ration auto du jeton
		t_owner = new Timer(DELAY_UPDATE, new ActionListener () {
			public void actionPerformed(ActionEvent arg0) {
				// Si la personne n'a effectu�e aucune modification durant un certain temps (temps > DELAY_TIMEOUT)
				if (isKeepTokenAlive() == false) { 
					if(Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_TIMEOUT) {
						// On envoie un message signalant la lib�ration du jeton
						central_applet.envoiserveur( 
								new XMLTree( CODE , 
										new XMLTree( CODEModule, 
												XMLTree.Attributes("type","RELEASE_LOCK"),
												XMLTree.Contents( "" )
										)
								)
						);
					}
				}
				// Si la personne a effectu�e une modification on enregsitre l'heure actuelle.
				else
				{
					timeLastModif = Calendar.getInstance().getTimeInMillis();
				}
			}
		});

		//Cr�ation du timer servant � pinger les utilisateurs.
		t_exist_owner = new Timer(DELAY_CHECKOWNER, new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// if at least one user is wainting in line for lock
				if (userFifo.size() > 0) {
					// if last active user is timed out
//					System.err.println("time last modif "+  timeLastModif);
//					System.err.println("ping without pong "+  pingWithoutPong);
					if (pingWithoutPong && Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_CHECKOWNERTIMEOUT) {
						pingWithoutPong = false;
						// send message to server to update trace file with forced release of viewboard
						central_applet.envoiserveur( 
								new XMLTree( CODE , 
										new XMLTree( CODEModule, 
												XMLTree.Attributes("type","FORCE_RELEASE"),
												XMLTree.Contents(nameOfPingWithoutPong)
										)
								)
						);
					}

					// if last active user is timed out send ping
					if (Calendar.getInstance().getTimeInMillis() - timeLastModif > DELAY_CHECKOWNERTIMEOUT) {
						pingWithoutPong = true;
						nameOfPingWithoutPong = (String) userFifo.elementAt(0);
						// send message to server to update trace file with user that's going to be ping-ed
						central_applet.envoiserveur( 
								new XMLTree( CODE , 
										new XMLTree( CODEModule, 
												XMLTree.Attributes("type","PING"),
												XMLTree.Contents( ""+(String)userFifo.elementAt(0))
										)
								)
						);
					}
				}
			}
		});

		updateState(true);
		// send out ping to see if user is still connected
		t_exist_owner.start();
	}

	/** M�thode specifiant ce que l'on fait quand on obitent le jeton
	 *  Cette m�thode commence par appeler la m�thode abstarite gotTokenAction()
	 *  qui permet de personnalis� le token schem dans la class impl�mentant ce module
	 */
	private void becomeOwner() {
		if (!isreplayer) {
			t_owner.start();
			isOwner = true;
			timeLastModif = Calendar.getInstance().getTimeInMillis();
		}
	}

	/** M�thode specifiant ce que l'on fait quand on relache le jeton
	 *  Cette m�thode commence par appeler la m�thode abstarite freeTokenAction()
	 *  qui permet de personnalis� le token schem dans la class impl�mentant ce module
	 */
	private void becomeNonOwner() {
		t_owner.stop();
		isOwner = false;
	}

	/** methode charge de mettre a jour la liste d'attente pour le jeton
	 *  cette m�thode est appel�e a la reception des messages
	 */
	private void updatePrintFifo() {
		Iterator<String> it = userFifo.iterator();
		StringWriter sw = new StringWriter();
		if (it.hasNext()) {
			sw.write(it.next()+" a le jeton.");
		} else {
			sw.write("Le jeton est libre");
		}
		if (it.hasNext()) {
			sw.write(" En attente: ");
		}
		while (it.hasNext()) {
			sw.write(" >> "+it.next());
		}
		jLabel1.setText(sw.toString());
		System.err.println(sw.toString());
		getGUIControl().repaint();
	}

	/** M�thode appel� par le action listener du bouton de lib�ration du jeton
	 *  Il sert a d�finir ce que l'on fait quand l'utilisateur lib�re le jeotn explicitement
	 */
	private void freeTokenButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (tokenrequested) {
			tokenrequested=false;
			central_applet.envoiserveur( 
					new XMLTree( CODE , 
							new XMLTree( CODEModule, 
									XMLTree.Attributes("type","RELEASE_LOCK"),
									XMLTree.Contents("")
							)
					)
			);
			setButtonsEnable(true, false);
		}
	}

	/** M�thode appel� par le action listener du bouton de prise du jeton
	 *  Il sert a d�finir ce que l'on fait quand l'utilisateur demande le jeotn.
	 */
	private void takeTokenButtonActionPerformed(java.awt.event.ActionEvent evt) {
		if (!tokenrequested) {
			tokenrequested=true;
			XMLTree msg = new XMLTree( CODE, 
					new XMLTree( CODEModule, 
							XMLTree.Attributes("type","LOCK_ASKED_FOR"),
							XMLTree.Contents( "" )
					)
			);
			central_applet.envoiserveur(  msg );
			setButtonsEnable(false, true);
		}
	}

	/** M�thode charg�e de tait� les message
	 *  Cette m�thode ne traite que les messages du module TokenSchem
	 */
	public void tokenMessageDeliver (String user, XMLTree data) {

		String type = null;
		try {
			XMLTree action = data.getByTagName(CODEModule);
			type = action.getAttributeValue("type");
		}
		catch (Exception e){}

		if(type != null) {

			//Message signalant la lib�ration du jeton
			if( type.equals("RELEASE_LOCK") ) {

				//On enleve de la liste celui qui vient de lib�r�r le jeton
				userFifo.remove(user);

				if(isOwner && user.equals(central_applet.nom)) {
					updateState(true);
				} else {
					updateState(false);
				}




			}	    

			//Message siganlant la demande du jeton
			else if (type.equals("LOCK_ASKED_FOR") && !userFifo.contains(user)) {
				// add user to wait line
				userFifo.add(user);
				updateState(false);
			}

			//Message forcant la lib�ration du jeton
			else if (type.equals("FORCE_RELEASE")) {

				// remove user from wait line as designed in message
				userFifo.remove(data.getText());
				// release lock if user to remove from wait line was ourself
				if (data.getText().equals(central_applet.nom)) {
					updateState(true);
				} else {
					updateState(false);
				}
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
							new XMLTree( CODE , 
									new XMLTree( CODEModule, 
											XMLTree.Attributes("type","PONG"),
											XMLTree.Contents("")
									)
							)
					);
			}
			else if ( type.equals("DISABLE_NUMBER_BUTTON")) {
				System.err.println("neverused????");
				takeTokenButton.setEnabled(false);
			}
		}	
	}

	/** M�thode permettant de savoir si on a le jeton
	 *  Elle retourne le boolean isOwner
	 */
	public boolean hasTheToken() {
		return isOwner;
	}

	/** M�thode permettant d'obtenir le panneau avec les
	 *  boutons prendre et lib�rer le jeton
	 */
	public JPanel getGUIControl() {
		return panelJeton;
	}

	/** M�thode permettant d'activ� ou de d�sactiv� les boutons 
	 *  de prise et de lib�ration du jeton
	 */
	public void setButtonsEnable(boolean takeButton, boolean freeButton) {
		takeTokenButton.setEnabled(!isreplayer && takeButton);
		freeTokenButton.setEnabled(!isreplayer && freeButton);
	}

	//TODO que doit on faire qaund on sait que quelqu'un � le jeton 
	private void updateState(boolean allowrequesttoken) {

		if (userFifo.size()>0) {
			if (userFifo.elementAt(0).equals(central_applet.nom)) {
				becomeOwner();
				drewlet.setTokenIsYours();
			} else {
				becomeNonOwner();
				drewlet.setTokenIsTaken();
			}
		} else {
			becomeNonOwner();
			drewlet.setTokenIsFree();
		}
		if (allowrequesttoken) {
			setButtonsEnable(true, false);
			tokenrequested=false;
		}
		updatePrintFifo();
	}

	private boolean keeptoken=false;
	private boolean isKeepTokenAlive () {
		boolean ret=keeptoken;
		keeptoken=false;
		return ret;
	}

	public void requestKeepTokenAlive() {
		keeptoken = true;
	}

	private boolean tokenrequested;
	public void requestToken() {
		takeTokenButtonActionPerformed(null);
	}

	public void releaseToken() {
		freeTokenButtonActionPerformed(null);
	}

}

