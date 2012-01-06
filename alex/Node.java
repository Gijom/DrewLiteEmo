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
 * <p>Node: </p>
 * <p>Description: A place to hold information for one ALEX move </p>
 * <p>Project: SCALE</p>
 * @author Laurie Hirsch
 *
 */

public class Node {
   public String templateID;
   String player;
   String completedString;
   int linkCat;
   char menuType;
   String linksTo1="-1";
   String linksTo2;
   public String topicStatus="none";
   int linksToThisNodeCount = 0, nonSequentialLinkCount=0;

   public Node(){
     templateID="0";
     player="mainClaim";
     linksTo1="0";
   }

  public Node(String who, String msg){
    StringTokenizer tok  = new StringTokenizer(msg,"#");
    Node linksToNode;

    player=who;

    if (tok.hasMoreTokens())
      templateID = tok.nextToken();

    if (tok.hasMoreTokens())
      menuType=((String)tok.nextToken()).charAt(0);

    if (tok.hasMoreTokens())
      completedString = tok.nextToken();

    if (tok.hasMoreTokens())
      linksTo1=tok.nextToken();

    if (tok.hasMoreTokens())
      linksTo2=tok.nextToken();

    if (tok.hasMoreTokens())
      System.err.println("too many tokens sent Node");

//remove any number prefix
//    if (linksTo1.length()>1)
//      linksTo1=linksTo1.substring(0,1);

  // linksToNode=
  }

  public void setLinkCat(int linkCat){
    this.linkCat=linkCat;
  }

  public void incrementNonSeqLinkCount(){
    nonSequentialLinkCount++;
  }

  public String toString() {
    String s = "tempid is: "  + templateID + "menuType: " + menuType +"player is " + player +" linksTo1: " + linksTo1 ;
    return s;
  }
}