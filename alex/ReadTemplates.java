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
 * File: ReadTemplates.java
 * Author:
 * Description:
 *
 * $Id: ReadTemplates.java,v 1.9 2007/02/20 16:03:41 collins Exp $
 */

package alex;

import java.net.*;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import Drew.Client.Util.*;

/**
 * <p>Title: ReadTemplates</p>
 * <p> to read templates from a local text file into the template store
 * Templates delimited with #</p>
 * <p>SCALE Project </p>
 * @author Laurie Hirsch
 */



class ReadTemplates {

  private Vector VOneSentence= new Vector();

  public ReadTemplates(URL tURL,  TemplateStore templateStore, String templateFile) {

    try {
      URL textURL = new URL(tURL, "alex/" + templateFile);
      Reader r = new BufferedReader(new InputStreamReader(textURL.openStream(), Config.getEncoding()));
      StreamTokenizer tokens = new StreamTokenizer(r);

     //valid characters in template file
      tokens.wordChars(34,122);

      //# char is used to delimit text in templates file
      tokens.quoteChar(35);

      String templateID,s;

      //add templates to the template store
      while (tokens.nextToken() != tokens.TT_EOF)
         templateStore.addTemplates((String)tokens.sval);
    }

    catch (MalformedURLException e) {
      System.err.println("Bad URL in read templates: " + e.getMessage());
    }

     catch (IOException IOe) {
      System.err.println("IO error in read templates  " + IOe.getMessage() );
    }

    catch (Exception e) {
      System.err.println("file error in reading word file ReadTemplates " + e.getMessage() );
    }
  }
}
