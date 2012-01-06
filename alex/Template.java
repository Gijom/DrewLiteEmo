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

package alex;

import java.util.*;

/**
 *
 * <p>Title: </p>
 * <p>Description: Information about a template </p>
 * <p>SCALE Project</p>
 * @author Laurie Hirsch
 *
 */
public class Template {

  String templateID;

//describes how to layout components on screen
  char screenType;

//which screen menu to add the template text
  char menuType;

  String graphType;

  String argCategory;

  String graphSymbol;

  String proContra="n";

//the template text.  part2 and 3 can be blank
  String textPart1, textPart2, textPart3;


  public Template() {
  }

  public Template(String s){

    StringTokenizer tok  = new StringTokenizer(s,"~");

//    System.out.println("in template s: " + s);

    try {
      if (tok.hasMoreTokens())
        templateID = tok.nextToken();

      if (tok.hasMoreTokens())
        screenType=tok.nextToken().charAt(0);

      if (tok.hasMoreTokens())
        menuType=tok.nextToken().charAt(0);

  //    if (tok.hasMoreTokens())
  //      graphType = tok.nextToken();

      if (tok.hasMoreTokens())
        argCategory = tok.nextToken();

      if (tok.hasMoreTokens())
        graphSymbol = tok.nextToken();

      if (tok.hasMoreTokens())
        proContra = tok.nextToken();

      if (tok.hasMoreTokens())
        textPart1 = tok.nextToken();

      if (tok.hasMoreTokens())
        textPart2 = tok.nextToken();

      if (tok.hasMoreTokens())
        textPart3 = tok.nextToken();

      if (tok.hasMoreTokens())
        System.err.println("too many tokens sent to message deliver from template tokeniser");
    }

    catch (NoSuchElementException e) {
	    System.err.println("invalid template string to template tokeniser " + e.getMessage());
    }
  }
}