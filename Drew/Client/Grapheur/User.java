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
 * File: User.java
 * Author:
 * Description:
 *
 * $Id: User.java,v 1.8 2003/12/01 13:48:32 quignard Exp $
 */

package Drew.Client.Grapheur;

import java.awt.*;

/** Classe des Utilisateurs du Grapheur. La classe User contient les informations sur
    les utilisateurs : comme leur nom, leur numéro d'enregistrement, leur couleur etc.
*/
public class User {
    /** Base de calcul des couleurs */
    private final static double base = 3.0 ;

    /** La saturation est un parametre de classe pour la couleur des utilisateurs. Elle est donc la meme pour tous. */
    private static float saturation = (float) 1.0 ;

    /** La clarte est un parametre de classe, invariable pour tous les utilisateurs. */
    private static float clarte = (float) 0.8 ;

    /** Le nom de l'utilisateur */
    private String nom ;	     

    /** Le rang de l'utilisateur (determine sa couleur) */
	private int rang; 
	
    /** La teinte de la couleur de l'utilisateur. On utilise le codage HSB (teinte, saturation [gris/couleur], clarte [sombre/clair]) : on ne fait varier que la teinte, la saturation et la clarte sont fixes.
	@see #getTeinte
    */
    private float teinte;	//Teinte de la couleur
	
    /** Vaut <b>false</b> si l'utilisateur a été retiré de l'argumentation.
    */
    private boolean engaged ;	

	/** the Label in the Grapheur List */
	private UserLabel myLabel;
    
    /** Construit un nouvel utilisateur avec des valeurs par défaut :
	<UL>
	<LI>nom = "toto" </LI>
	<LI>teinte = 0.0 </LI>
	<LI>engaged = true</LI>
	</UL>
    */
    public User(){
	this("toto",0,true);;
    }
    
    /** Construit un nouvel utilisateur en spécifiant son nom.
    */
    public User(String s){
	this(s,0,true);
    }

    /** Construit un nouvel utilisateur en spécifiant son nom et son rang.
	L'utilisateur est engagé par défaut.
    */
    public User(String s, int c){
	this(s, c, true);
    }

    /** Construit un nouvel utilisateur en spécifiant son nom, son rang 
     * et son engagement.
    */
    public User(String s, int c, boolean engaged){
	this.nom = s ;
	this.rang = c;
	this.teinte = getTeinte(c);
	this.engaged = engaged ;
    }    

    /** Obtenir le nom d'un utilisateur */
    public String getUserName() {
	return(nom);
    }
    
    /** Retourne la couleur en RGB que porte l'utilisateur. On
     * prend donc la teinte (variable), la saturation et la clarte 
     * qui sont fixes.
     */
    public int getUserRGBColor(){
	return Color.HSBtoRGB(teinte, saturation, clarte);
    }

    /** Connaître le statut d'un utilisateur (en jeu ou non)*/
    public boolean getUserEngaged() {
	    return(engaged);
    }
    
    /** Définir le nom d'un utilisateur */
    public void setUserName(String s) {
	nom = s;
    }

    public int getNumber() {
        return rang;
    }
    
    /** Définir le statut d'un utilisateur (le retirer du jeu ou non)*/
    public void setUserEngaged(boolean b) {
	engaged = b;
    }   

	public void setUserLabel(UserLabel l){
		myLabel = l;
	}

	public UserLabel getUserLabel(){
		return myLabel;
	}
    /** Calcule la teinte selon le rang i (0..255). On se repartit sur toute
     * la gamme des teintes : 0, 0.5, 0.25, 0.75, 0.125, etc.
    */
    public float getTeinte(int i){
	float t ;
	if (i == 0) t = (float) 0.0;
	else {
	     double di, msb, puiss, reste;
	     di = (double) i;

	     puiss = Math.floor(Math.log(di)/Math.log(base));
	     puiss = Math.pow(base, puiss);
	     msb = Math.floor(di/puiss);
	     reste = di - msb*puiss;

	     t = getTeinte((int) reste) + (float) (msb / base / puiss);
	}
	return t ;
    }

    /** Attribue une teinte particulière à l'utilisateur */
    public void getTeinte(float t){
	this.teinte = t;
    }
}
