package Drew.Client.Util;

import java.awt.Color;
import java.awt.Panel;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import Drew.Util.XMLmp.XMLTree;
import java.util.EmptyStackException;

public abstract class DefaultCooperativeModule extends Panel implements
		CooperativeModule, TokenListener {
	
        private final static double base = 3.0 ;
	private final static float fixedValue = (float)0.8;
	private Map<String,Color> listOfUsers;
	//private TokenRing token;
	
	protected Communication central_applet;
	
	public DefaultCooperativeModule() {
		super();
	}
	
	public DefaultCooperativeModule(Communication cdc) {
		this();
		constructor(cdc);
	}

	public abstract void clear();

	public void constructor(Communication cdc) {
		central_applet=cdc;
		listOfUsers = new HashMap<String,Color>();

	}

	public void destroy() {
		
	}
	
	public final void sendServer(XMLTree data) {
		this.central_applet.envoiserveur(data);
	}
	
	public abstract String getCode();

	public abstract String getTitle();
	
	public String getUsername() {
		return isReplayer()?"":central_applet.nom;
	}
	
	protected void addUser(String user) { //GC: changed from private to pretected cause I want to add a fake user
		float t = makeTint(listOfUsers.size());
		Color c = new Color(Color.HSBtoRGB(t,fixedValue,fixedValue));
		listOfUsers.put(user,c);
	}
	
	// "" is a special username that is equivalent to getUsername()
	// use when you need to distinguish between incoming history and user
	// interface
	public Color getColor(String user) {
		if (user=="") {
			user = getUsername();
		}
		if (!listOfUsers.containsKey(user)) {
			addUser(user);
		} 
		return listOfUsers.get(user);
	}

	public void init() {
		// TODO Auto-generated method stub

	}
	
	public boolean isReplayer() {
		return central_applet.nom==null;
	}
	
	protected TokenRing token = null;
	public void setupTokenRing(boolean showTakeButton, boolean showFreeButton) {
		token = new TokenRing(central_applet,this.getCode(),this);
		token.init(showTakeButton, showFreeButton);
		if (isReplayer()) {
			token.setReplayer();
		}
	}

	public final void messageDeliver(String user, XMLTree data) {
		//System.err.println(user +" "+ data.getText());
		// createcolorlist
	    String player = data.getAttributeValue("player");
	    if( player == null) {
	    	player = user;
	    }
	    if (player=="unknown") {
	    	//player=this.getUsername();
                return; //GC: do not do anything in that case
	    }
		if(!listOfUsers.containsKey(player)) {
			addUser(player);
		}
	    
		if (token!=null && data.tag().equals(getCode())) {
			token.tokenMessageDeliver(user, data);
		}

		boolean fordrewlet=true;
		try {
			data.getByTagName(TokenRing.CODEModule);
			fordrewlet=false;
		} catch (Exception e) {}
		
	    if (fordrewlet) {
			// forward messages for drewlets if they are not generic messages
			moduleMessageDeliver(user,data);
		}

	}
	
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
		return t;
	}
	
	public abstract void moduleMessageDeliver(String user, XMLTree data);

	public final boolean messageFilter(XMLTree event) {
		return true;
	}

	public void start() {
		// TODO Auto-generated method stub

	}

	public void stop() {
		// TODO Auto-generated method stub

	}
	
	public void setTokenIsFree() {
		// TODO Auto-generated method stub
		
	}

	public void setTokenIsTaken() {
		// TODO Auto-generated method stub
		
	}

	public void setTokenIsYours() {
		// TODO Auto-generated method stub
		
	}

}
