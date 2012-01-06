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
import java.awt.Choice;

/**
 * to store date from a delimited (~) text file
 * Assume: element 0 is the template ID
 *   element 1 is the screen type
 *   element 2 is the menu type
 *   element 3 is graph type
 *   element 4 is argument category
 *   element 5 is agree/disagree
 *
 *   element 5, 6 and 7 are the template text
 */


public class TemplateStore {
  Hashtable templateHashTable = new Hashtable();
  Vector VArgue = new Vector();
  Vector VOpinion = new Vector();
  Vector VExplore = new Vector();
  Vector VDialogue = new Vector();

  public TemplateStore() {
  }


  public void addTemplates(String s){

    Template t = new Template(s);
    templateHashTable.put(t.templateID, t);

    switch (t.menuType) {
      case 'A' :
        VArgue.addElement(t);
        break;
      case 'O' :
        VOpinion.addElement(t);
        break;
      case 'E' :
        VExplore.addElement(t);
        break;
      case 'D' :
        VDialogue.addElement(t);
        break;
      default :
        System.err.println ("Error in template store Vector assignment menu type: " +
                            t.menuType + " String is s: " + s);
    }
    //System.out.println("s is " + s);
  }

  public Template getTemplate(String templateID) {
   Template t=(Template)templateHashTable.get(templateID);
   if (t==null)
           System.err.println("not found templatid:" + templateID);
   return t;
  }


  public void setTemplateChoice(Choice choice, Vector v){
    String s;
    Template t;

    for (int x=0; x<v.size(); x++){

      t=(Template)v.elementAt(x);
      s="";
      s=t.textPart1;

      if (!(t.textPart2==null))
        s=s + "..."+ t.textPart2;
      if (!(t.textPart3==null))
        s=s + "..."+ t.textPart3;

      choice.addItem(s);
    }
  }

  public void setTemplateChoice(Choice choice, char menuType) {
    String choiceString="";
    Template t;
      for (Enumeration e = templateHashTable.elements(); e.hasMoreElements() ;) {
        t=(Template)e.nextElement();
        if (menuType==t.menuType){
          choiceString=t.textPart1;
          if (!(t.textPart2==null))
            choiceString=choiceString + "..."+ t.textPart2;
          if (!(t.textPart3==null))
           choiceString=choiceString + "..."+ t.textPart3;
         choice.addItem(choiceString);}
      }
  }
}

