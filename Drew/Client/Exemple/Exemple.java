package Drew.Client.Exemple;

import Drew.Client.Util.Communication;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Util.XMLmp.XMLTree;

public class Exemple extends DefaultCooperativeModule {

	public Exemple() {
		super();
	}

	public Exemple(Communication cdc) {
		this();
		this.constructor(cdc);
	}

	@Override
	public void clear() {
		// TODO R�initialiser les variables n�cessaires
		// sachant que toute la trace sera envoy�e par la suite

	}
	
	@Override
	public void constructor(Communication cdc) {
		super.constructor(cdc);
		// TODO tout ce qui se passerait normalement dans un constructeur.
		// la DREWlet est initialis�e par foo = class.newInstance(); foo.constructor(cdc);
	}
	
	@Override
	public void init() {
		super.init();
		// TODO Auto-generated method stub

	}

	@Override
	public String getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void moduleMessageDeliver(String user, XMLTree data) {
		// TODO Auto-generated method stub

	}
	
	
	

}
