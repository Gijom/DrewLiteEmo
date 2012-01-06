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
 * File: Donnees.java
 * Author:
 * Description:
 *
 * $Id: Donnees.java,v 1.7 2007/02/20 16:03:41 collins Exp $
 */

package Drew.Client.WhiteBoard;

import java.awt.*;
import java.util.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;

/**
* Cette classe definie par ces champs les donnees necessaires a tous 
* les outils de dessin disponibles pour l'ensemble de l'application WhiteBoard.
* La mise sous forme de message des donnees locales pour les rediriger vers le serveur ainsi que le
* le dechiffrement des messages distants sont effectues dans cette classe par des methodes publiques.
*/
public class Donnees {

	/**
	* Le code utilise par le whiteboard pour envoyer et recevoir ses messages
	*/
	static final String CODE = "whiteboard";

	/** zone de dessin ou l'on desire ecrire */
	public ZoneDessin  zone_dessin;

	/** outil actif */
	public Outil outil;

	/** couleur active */
	public Color couleur;

	/** caractere en cours */
	//public char caractere;

	/** texte en cours */
	public String texte;

	/** 
	* coordonnee x de la souris dans la zone de dessin : correspond a 
	* un relachement de bouton de souris ou a un mouvement en cours
	*/
	public int lx;

	/** 
	* coordonnee y de la souris dans la zone de dessin : correspond a un
	* relachement de bouton de souris ou a un mouvement en cours
	*/
	public int ly;

	/** 
	* coordonnee x de la souris dans la zone de dessin : correspond a un click
	* de bouton de souris ou a un debut de mouvement
	*/
	public int nx;

	/** 
	* coordonnee y de la souris dans la zone de dessin : correspond a un click
	* de bouton de souris ou a un debut de mouvement
	*/
	public int ny;

	/** Table de tous les outils de dessin disponibles : la clef 
	* de hachage est le nom identifiant chaque outil 
	*  Cette table est cree lors de la creation d'une instance de la classe MenuOutils
	*  @see Outil#nom
	*  @see MenuOutils
	*/
	//private final Hashtable outils_disponibles;
	private Hashtable outils_disponibles;

	/**
	* Constructeur pour les outils n'utilisant pas les entrees clavier
	* Les champs caractere et texte sont initialises a un espace
	*/
	Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin,Color couleur, int lx,int ly,int nx,int ny) 
	{
		this.outils_disponibles=outils_disponibles;
		this.zone_dessin=zone_dessin;
		this.couleur=couleur;
		this.lx=lx;
		this.ly=ly;
		this.nx=nx;
		this.ny=ny;
		//caractere=' ';
		texte=" ";
	}

	/**
	* Constructeur pour les outils utilisant les entrees clavier
	* le champ texte est initialise a un espace, lx=ly=0
	*/
/* PJ 20021203
	Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin,Color couleur, int nx,int ny, char caractere) 
	{
		this.outils_disponibles=outils_disponibles;
		this.zone_dessin=zone_dessin;
		this.couleur=couleur;
		this.nx=nx;
		this.ny=ny;
		this.caractere=caractere;
		lx=ly=0;
		texte=" ";
	}
*/
	/**
	* Contructeur de base
	* Champs couleur initialise a la couleur noire, tous les autres non-definis a 0 ou espace suivant leur type
	* NB : tous les champs de donnees etant publics, les evenements lies a la zone de dessin peuvent modifier sans aucune restriction tout ou partie de ces champs. Les donnees primordiales sont :
	* @param outils_disponibles la liste des outils disponibles.
	* @param zone_dessin la zone de dessin ou l'on va agir.
	*/
	Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin) 
	{
		this.outils_disponibles=outils_disponibles;
		this.zone_dessin=zone_dessin;
		couleur=Color.black;
		//caractere=' ';
		texte=" ";
		lx=ly=nx=ny=0;
	}

	/**
	* Constructeur remplissant tous les champs de donnees a partir d'un message distant
	* @param chaine message distant epure traitable par la methode decompacteDeMessage()
	* @see #decompacteDeMessage
	*/
	//XML pj 20021202
	//Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin,String chaine) 
	Donnees(Hashtable outils_disponibles, ZoneDessin zone_dessin,XMLTree msg) 
	{
		this(outils_disponibles, zone_dessin);
		//this.outils_disponibles=outils_disponibles;
		//this.zone_dessin=zone_dessin;

		decompacteDeMessage(msg);
	}

	/**
	* "compactage" des donnees au format texte pour envoi au serveur
	* Format des donnees: "wbd~outil.nom:couleur.getRGB():lx:ly:nx:ny:caractere:texte:"
	* @see Color#getRGB()
	*/
	public String toString() {
		return (CODE+"~"+outil.nom+":"+couleur.getRGB()+":"+lx+":"+ly+":"+nx+":"+ny+":"+texte+":");
	}
/*
	public String compacteEnMessage() 
	{
		return (CODE+"~"+outil.nom+":"+couleur.getRGB()+":"+lx+":"+ly+":"+nx+":"+ny+":"+caractere+":"+texte+":");
	}
*/
	//XML pj 20021202
	public XMLTree compacteEnMessage() {
	// dessinlibre, droite, effacelocal, effacetout, ovaleplein, ovalevide, pointeur, rectangleplein, rectanglevide, texte
		XMLTree data = null;

		if( outil.nom.equals( "droite" ) || outil.nom.equals( "dessinlibre" ) ) {
			// <line x1="" y1="" x2="" y2="" stroke="" stroke-width="" />
			data = new XMLTree( "line",
				XMLTree.Attributes(
					"x1", String.valueOf( lx ),
					"y1", String.valueOf( ly ),
					"x2", String.valueOf( nx ),
					"y2", String.valueOf( ny ),
					"stroke", colorEncode(couleur),
					"stroke-width", "1"
				),
				XMLTree.Contents()
			);
		}
		else if( outil.nom.equals( "ovaleplein" )  || outil.nom.equals( "ovalevide" ) ) {
			// <ellipse x="" y="" rx="" ry="" fill="" stroke="" stroke-width="" />
			data = new XMLTree( "ellipse",
				XMLTree.Attributes(
					"x", String.valueOf( lx ),
					"y", String.valueOf( ly ),
					"rx", String.valueOf( nx ),
					"ry", String.valueOf( ny ),
					"stroke", colorEncode(couleur),
					"stroke-width", "1"
				),
				XMLTree.Contents()
			);

			if( outil.nom.equals( "ovaleplein" ) ) data.setAttribute( "fill", colorEncode(couleur) );
		}
		else if( outil.nom.equals( "rectangleplein" )  || outil.nom.equals( "rectanglevide" ) ) {
			// <rect x="" y="" width="" height="" fill="" stroke="" stroke-width="" />
			data = new XMLTree( "rect",
				XMLTree.Attributes(
					"x", String.valueOf( lx ),
					"y", String.valueOf( ly ),
					"height", String.valueOf( nx ),
					"width", String.valueOf( ny ),
					"stroke", colorEncode(couleur),
					"stroke-width", "1"
				),
				XMLTree.Contents()
			);

			if( outil.nom.equals( "rectangleplein" ) ) data.setAttribute( "fill", colorEncode(couleur) );
		}
		else if( outil.nom.equals( "effacelocal" ) ) {
			// <clear x="" y="" radius="" />
			data = new XMLTree( "clear",
				XMLTree.Attributes(
					"x", String.valueOf( lx ),
					"y", String.valueOf( ly ),
					"radius", String.valueOf( ((OutilDessinDirect)outil).rayon ) 
				),
				XMLTree.Contents()
			);

  		}
		else if( outil.nom.equals( "effacetout" ) ) {
			// <clear/>
			data = new XMLTree( "clear" );
		}
		else if( outil.nom.equals( "texte" ) || outil.nom.equals( "pointeur" )) {
			// <text x="" y="" width="" height="" fill="" stroke="" stroke-width="" />
			data = new XMLTree( "text",
				XMLTree.Attributes(
					"x", String.valueOf( lx ),
					"y", String.valueOf( ly ),
					"lang", Config.getLocale().getLanguage(),
					"fill", colorEncode(couleur)
				),
				XMLTree.Contents( texte )
			);
			
			if( !outil.nom.equals( "texte" ) ) {
				XMLTree shape = ((OutilPointeur)outil).Shape(this);
				shape.insert( data );
				return new XMLTree( CODE, XMLTree.Attributes(), XMLTree.Contents( shape ));
			}
		}
		else {
			System.err.println( "Unknown tool : " +  outil.nom );
		}
		return new XMLTree( CODE, new XMLTree( "svg" , XMLTree.Attributes(), XMLTree.Contents( data )));
	}

	/**
	* "decompactage" des donnees envoyees par le serveur et epurees dans WhiteBoard.mis_a_jour
	* Format des donnees: "outil.nom:couleur.getRGB():lx:ly:nx:ny:caractere:texte:"
	* @see Color#getRGB()
	* @see WhiteBoard#mis_a_jour
	*/
	public void decompacteDeMessage(String message) {
		StringTokenizer tok;

		try {
			tok = new StringTokenizer(message,":");
			String nom_outil=tok.nextToken();
			outil=(Outil) outils_disponibles.get(nom_outil) ;
			couleur=new Color(Integer.parseInt(tok.nextToken()));
			lx=Integer.parseInt(tok.nextToken());
			ly=Integer.parseInt(tok.nextToken());
			nx=Integer.parseInt(tok.nextToken());
			ny=Integer.parseInt(tok.nextToken());
			//caractere=tok.nextToken().charAt(0);
			texte=tok.nextToken();
		}
		catch (NoSuchElementException e) {
			System.err.println("message recu indechiffrable : "+message);
			outil=null;
		}
	}

	static String colorEncode(Color c) {
		String n = "000000" + Integer.toHexString(c.getRGB() & 0x00ffffff);
		return n.substring(n.length() - 6);
	}

	Color colorDecode( String c ) throws NumberFormatException {
		try {
			// c reprsent a integer
			return Color.decode( c );
		}
		catch (NumberFormatException e) {
			// test if c is an hex string
			try {
				return new Color( Integer.parseInt( c, 16 ) );
			}
			catch( NumberFormatException ee) {
				System.err.println( "colorDecode, trying to decode as hex " + c );
				return new Color( 0 );
			}
		}
	}

	//XML pj 20021202
	public void decompacteDeMessage(XMLTree svg)  {
	// dessinlibre, droite, effacelocal, effacetout, ovaleplein, ovalevide, pointeur, rectangleplein, rectanglevide, texte
		if( svg.tag().equals( "ellipse" ) ) {
			lx=Integer.parseInt(svg.getAttributeValue("x", "0"));
			ly=Integer.parseInt(svg.getAttributeValue("y", "0"));
			nx=Integer.parseInt(svg.getAttributeValue("rx", "0"));
			ny=Integer.parseInt(svg.getAttributeValue("ry", "0"));

			couleur= colorDecode( svg.getAttributeValue( "stroke", "0" ));
			if( svg.getAttributeValue( "fill" )  == null ) {
				outil=(Outil) outils_disponibles.get("ovalevide") ;
			}
			else {
				outil=(Outil) outils_disponibles.get("ovaleplein") ;
				couleur= colorDecode( svg.getAttributeValue( "fill", "0" ));
			}
		}
		else if( svg.tag().equals( "rect" ) ) {
			lx=Integer.parseInt(svg.getAttributeValue("x", "0"));
			ly=Integer.parseInt(svg.getAttributeValue("y", "0"));
			nx=Integer.parseInt(svg.getAttributeValue("height","0"));
			ny=Integer.parseInt(svg.getAttributeValue("width", "0"));

			couleur= colorDecode( svg.getAttributeValue( "stroke", "0" ));
			if( svg.getAttributeValue( "fill" )  == null ) {
				outil=(Outil) outils_disponibles.get("rectanglevide") ;
			}
			else {
				outil=(Outil) outils_disponibles.get("rectangleplein") ;
				couleur= colorDecode( svg.getAttributeValue( "fill", "0" ));
			}
		}
		else if( svg.tag().equals( "line" ) ) {
			lx=Integer.parseInt(svg.getAttributeValue("x1", "0"));
			ly=Integer.parseInt(svg.getAttributeValue("y1", "0"));
			nx=Integer.parseInt(svg.getAttributeValue("x2", "0"));
			ny=Integer.parseInt(svg.getAttributeValue("y2", "0"));

			outil=(Outil) outils_disponibles.get("droite") ;
			couleur= colorDecode( svg.getAttributeValue( "stroke", "0" ));
		}
		else if( svg.tag().equals( "text" ) ) {
			lx=Integer.parseInt(svg.getAttributeValue("x", "0"));
			ly=Integer.parseInt(svg.getAttributeValue("y", "0"));
			couleur= colorDecode( svg.getAttributeValue( "fill", "0" ));
			outil=(Outil) outils_disponibles.get("texte") ;
			texte = svg.getText();
		}
		else if( svg.tag().equals( "clear" ) ) {
			int radius=Integer.parseInt(svg.getAttributeValue("radius", "-1"));

			if( radius == -1 ) {
				outil=(Outil) outils_disponibles.get("effacetout");
			}
			else {
				lx=Integer.parseInt(svg.getAttributeValue("x", "0"));
				ly=Integer.parseInt(svg.getAttributeValue("y", "0"));
				outil=(Outil) outils_disponibles.get("effacelocal");
			}
		}
	}
}
