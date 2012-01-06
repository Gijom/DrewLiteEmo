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
 * <p>Title:  </p>
 * <p>Description: To store information on the argumentation and provide
 * algorithms for updating advice text</p>
 * <p>Company: SCALE Project</p>
 * @author Laurie Hirsch
 * @version 1.0
 */

public class NodeTree {

//to hold all the moves submitted.
  private Vector VNodes = new Vector();

//to store all currently active players
  private Hashtable playerHashTable = new Hashtable();

//template properties definitions
  private TemplateStore ts;

  private int reactive=0, proactive=0, inactive=0, mixed=0;

  public NodeTree(TemplateStore ts){
    VNodes.addElement(new Node());
    this.ts=ts;
  }

  void updatePlayerTable(){
    String player,playerType;

    for (Enumeration e = playerHashTable.keys() ; e.hasMoreElements() ;) {
      player= (String)e.nextElement();
      playerType=assignPlayerType(player);
      playerHashTable.put(player,playerType);
    }
  }


//add a node after a new player move
  public void addNode(Node node){
    Template t;

//get the template
    t=ts.getTemplate(node.templateID);

//get the previous node ID
    String predID,predSameID="-1";

//the last node entered before this one
    Node predNode;

//the last node entered by the same user
    Node predNodeSameID;

    predNode=(Node)VNodes.lastElement();
    predID=String.valueOf(VNodes.size()-1);

    int y=VNodes.size()-1;
    boolean found=false;
    while (y>=0 & y>=VNodes.size()-Alex.advisorDelay & !found){
      found=((Node)VNodes.elementAt(y)).player.equals(node.player);
      if (found){
        predNodeSameID=(Node)VNodes.elementAt(y);
        predSameID=String.valueOf(y);
      }
      y--;
    }

/*    0 Link0TypeArg	      If the node links to node 0 and is of the argument category
    1	Link0TypeNonArg	      If the node links to node 0 but is not of the argument category.
    2	LinkPredSameID	      If the node links to a preceding node with a same user ID.
    3	LinkPredDistinctID    If the node links to a preceding node with a distinct user ID.
    4	LinkNonPredSameID     If the node links to a non-preceding node with the same user ID.
    5	LinkNonPredDistinctID If the node links to a non-preceding node with a distinct user ID. */

    if ((node.linksTo1.equals("0"))&(t.argCategory.equals("arg")))
      node.setLinkCat(0);
    else if ((node.linksTo1.equals("0"))&!(t.argCategory.equals("arg")))
      node.setLinkCat(5);
    else if (node.linksTo1.equals(predSameID))
      node.setLinkCat(2);
    else if ((node.linksTo1.equals(predID))&!(predNode.player.equals(node.player)))
      node.setLinkCat(1);
    else if (!(node.linksTo1.equals(predID))&(predNode.player.equals(node.player)))
      node.setLinkCat(4);
    else if (!(node.linksTo1.equals(predID))&!(predNode.player.equals(node.player)))
      node.setLinkCat(3);

//update the player table (playername/player type - set to until defined
    if (!(playerHashTable.containsKey(node.player)))
      playerHashTable.put(node.player, "0");

//Add the node
    VNodes.addElement(node);
  }


//set the player type based on the type of links made by that player
//this is run for all players when updating advice
  public String assignPlayerType(String player){
    Node n;

//default player type is mixed
    String playerType="mixed";

    int startAt=0;

//to store count of link types for a player
    int link0TypeArg=0, link0TypeNonArg=0, linkPredSameID=0, linkPredDistinctID=0,
    linkNonPredSameID=0, linkNonPredDistinctID=0;


//only look at recent nodes
    startAt=VNodes.size()-Alex.advisorDelay;
    if (startAt<0) startAt=0;

    for (int x=startAt; x<VNodes.size(); x++){
      n=(Node)VNodes.elementAt(x);
      if (n.player.equals(player)){

        switch(n.linkCat){
          case 0 : link0TypeArg++;
            break;
          case 1 : link0TypeNonArg++;
            break;
          case 2 : linkPredSameID++;
            break;
          case 3 : linkPredDistinctID++;
            break;
          case 4 : linkNonPredSameID++;
            break;
          case 5 : linkNonPredDistinctID++;
            break;
          default: {};
        }
      }
    }

/*    Playertype	Condition
Inactive	if all statements/nodes are  either LinkPrecedDistinctID or  link0TypeArg. (no other cat nodes).
Proactive	If instances of LinkPrecedSameID and LinkNonPrededSameID and Link0TypeArg > instances of LinkNonPrecedDistinctID and LinkPrecedDistinctID node +1
Reactive	 instances of LinkNonPrecedDistinctID nodes > LinkPrecedSameID or LinkNonPrededSameID or  Link0TypeArg nodes +1
Mixed	If instances of LinkPrecedSameID and LinkNonPrededSameID and LinkNonPrecedDistinctID  and Link0TypeArg nodes  (+1,+0,-1)= instances of LinkNonPrecedDistinctID nodes
 */
    playerType="mixed";

    if (link0TypeNonArg==0 & linkPredSameID==0 & linkNonPredSameID==0 & linkNonPredDistinctID==0)
      playerType="inactive";

    else if((linkPredSameID + linkNonPredSameID) > (linkNonPredDistinctID +linkPredDistinctID + 1) )
      playerType="proactive";

    else if ((linkNonPredDistinctID> java.lang.Math.max(linkPredSameID,linkNonPredSameID)) |
             (linkNonPredDistinctID>link0TypeArg+1)) //(linkNonPredDistinctID> linkNonPredSameID)
      playerType="reactive";

    return playerType;
  }


  public String playerTypeQuestion(String me){

    String myPlayerType, question="QA";

    countOtherPlayerTypes(me);

    myPlayerType=assignPlayerType(me);

    if (myPlayerType.equals("reactive")& proactive>0)
      question="QA";
    else if (myPlayerType.equals("proactive")& ((inactive>0)|(reactive>0)|(mixed>0)))
      question="QC";
    else if (myPlayerType.equals("inactive")& (reactive>0))
      question="QC";
    else if (myPlayerType.equals("mixed")& (reactive>0))
      question="QC";
    else if (myPlayerType.equals("inactive")& (reactive>0))
      question="QD";
    else if (myPlayerType.equals("reactive")& (mixed>0))
      question="QD";
    else if (myPlayerType.equals("inactive")& ((proactive>0)|(mixed>0)))
      question="QE";
    else if (myPlayerType.equals("mixed")& ((inactive>0)|(proactive>0)))
      question="QF";

    return question;
  }

  private void countOtherPlayerTypes(String me) {
    String player, playerType;
    reactive=0; proactive=0; inactive=0; mixed=0;

    for (Enumeration e = playerHashTable.keys() ; e.hasMoreElements() ;) {
      player= (String)e.nextElement();
      if (!player.equals(me)){
        playerType=(String)playerHashTable.get(player);
        if (playerType.equals("reactive"))
          reactive++;
        if (playerType.equals("proactive"))
          proactive++;
        if (playerType.equals("inactive"))
          inactive++;
        if (playerType.equals("mixed"))
          mixed++;
      }
    }
  }


  public String getQuestion(String me){
//QA is the default question type
    String question="QA", myPlayerType;
    int totalTopics=0,myTopics=0, startPoint=0;
    Node n;
    int linksTo;

    setTopics();
    countOtherPlayerTypes(me);

    if (VNodes.size()<Alex.advisorDelay)
      startPoint=0;
    else
      startPoint=VNodes.size() - Alex.advisorDelay;

    for (int x=startPoint; x<VNodes.size(); x++){
      n=(Node)VNodes.elementAt(x);
      if (n.topicStatus.equals("Topic")){
        totalTopics++;
        if (n.player.equals(me))
          myTopics++;
      }
    }

    if (totalTopics==0)
      return playerTypeQuestion(me);

    myPlayerType=assignPlayerType(me);

    if (myPlayerType.equals("reactive")& proactive>0)
      return "QA";
    if (myPlayerType.equals("inactive")& proactive>0)
      return "QE";
    if (myPlayerType.equals("proactive")& (inactive>0|reactive>0))
      return "QC";
    if (myPlayerType.equals("inactive")& reactive>0)
      return "QC";
    if (myPlayerType.equals("reactive")& inactive>0)
      return "QD";

    if (totalTopics>=2 & myTopics==2)
      return "QE";
    if (totalTopics>=3 & myTopics==1)
      return "QD";
    if (totalTopics==1 & myTopics ==1)
      return "QB";
    if (totalTopics==4 & myTopics==2)
      return "QC";
    if (totalTopics==1 & myTopics ==1)
      return "QG";

    return question;
  }


  public void setTopics(){

    Node n;
    int linksTo;
    Template t;

//start at 1 - not interested in main claim node 0
    for (int x=1; x<VNodes.size(); x++){
      n=(Node)VNodes.elementAt(x);
      linksTo=Integer.parseInt(n.linksTo1);
      if (linksTo!=(x-1))
        ((Node)VNodes.elementAt(linksTo)).incrementNonSeqLinkCount();
    }

    //If the node is an argument and is referred to at least once by any player non sequentially OR
//  if the node is of the type argument that refers to a 0 category
    for (int x=1; x<VNodes.size(); x++){
      n=(Node)VNodes.elementAt(x);
      t=ts.getTemplate(n.templateID);
      if (t.argCategory.equals("arg")& n.nonSequentialLinkCount>0)
        n.topicStatus="Topic";
    }
  }
}