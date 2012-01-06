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
 * File: StringToken.java
 * Author:
 * Description:
 *
 * $Id: StringToken.java,v 1.6 2003/11/24 10:54:29 serpaggi Exp $
 */

package Drew.Util;

import java.util.*;


/** 
 * Classe heritant de StringTokenizer
 * 
 * @see StringTokenizer
 *
 */

public class StringToken extends StringTokenizer {


  	/**
   	* Variable permettant de recuperer le delimiteur
   	*/

  	private String Delimiteur;
  	private String data;

  	public StringToken(String str, String delim) {
      		super(str,delim);
      		Delimiteur = delim;
      		data = str;
    	}
  

  	public StringToken(String str, String delim, boolean returnTokens) 
    {
      		super(str,delim,returnTokens);
      		Delimiteur = delim;
    	}




  	/**
   	* purgeToken vide tous le token d'une chaine
   	*/

  	public String purgeToken(){
    		String finChaine = "";

    		if(super.hasMoreElements()) finChaine = super.nextToken();
    		for(;super.hasMoreElements();)
        		finChaine = finChaine+Delimiteur+super.nextToken();

		//add the delimiteur if data end withi, work if all delimiter is present
		if( data.endsWith(Delimiteur) ) finChaine = finChaine+Delimiteur;

		return finChaine;
	}

  	public  String nextToken(String delim){
    		Delimiteur = delim;
    		return super.nextToken(delim);
  	}

}















