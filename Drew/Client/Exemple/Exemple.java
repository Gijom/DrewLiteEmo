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
		// TODO Réinitialiser les variables nécessaires
		// sachant que toute la trace sera envoyée par la suite

	}
	
	@Override
	public void constructor(Communication cdc) {
		super.constructor(cdc);
		// TODO tout ce qui se passerait normalement dans un constructeur.
		// la DREWlet est initialisée par foo = class.newInstance(); foo.constructor(cdc);
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
