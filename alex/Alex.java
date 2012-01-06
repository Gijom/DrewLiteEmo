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

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import Drew.Client.Util.*;
import Drew.Client.Util.XML.Grapheur;
import Drew.Util.XMLmp.*;

//import Drew.Util.*;

/**
 * <p>Title: ALEX Templates</p>
 * <p>Description: To run the ALEX GUI and send message to Graph module</p>
 * <p>SCALE Project </p>
 * @author Laurie Hirsch
 *
 */

public class Alex
    extends Panel
    implements CooperativeModule {

  private String topicStatement = "0 GMO?" + '\n';

//each entered statement is given a sentence number for later reference
  private int sentenceNumber, turnNumber, opinionCount, advisorCount;
  boolean firstSentence = true;

//to store the number of moves befoer running the advisor
  static final int advisorDelay = 6;

//object to read templates from a text file and perform operations on the set of templates
  private ReadTemplates readTemplates;

//the ALEX panels
  private Panel upper = new Panel();
  private Panel selection = new Panel();
  private Panel edit = new Panel();
  private Panel feedBack = new Panel();
  private Panel completed = new Panel();

  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private GridBagLayout gridBagLayout = new GridBagLayout();
  private FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT, 0, 0);

  private GridLayout gridLayout = new GridLayout();

  private Label selectATemplateLabel = new Label("1  Select a Template ",
                                                 Label.LEFT);
  private Label AlexLabel = new Label("ALEX 1-2-3");
  private Label makeYourArgumentLabel = new Label("2  Make Your Argument                                                                                                       ");
  private Label argueLabel = new Label("Argument:");
  private Label opinionLabel = new Label("Opinion:");
  private Label exploreLabel = new Label("Explore:");
  private Label dialogueLabel = new Label("Comment:");
  private Label adviceLabel = new Label("Advice");

  private Button submitButton = new Button("3 Submit");

//template choice allows user to select thier template in upper panel
  private Choice argueChoice = new Choice();
  private Choice opinionChoice = new Choice();
  private Choice exploreChoice = new Choice();
  private Choice dialogueChoice = new Choice();

//in center panel users build their sentence.  They select references to other sentences by
//using choice components
  private Choice numberChoice1 = new Choice();
  private Choice numberChoice2 = new Choice();
  private Choice localNumberChoice1 = new Choice();
  private Choice localNumberChoice2 = new Choice();

//in the center panel users enter their text to make thier arguments
  private TextArea userTextArea1 = new TextArea("", 6, 60, 1);
  private TextArea adviceTextArea = new TextArea("", 9, 13,
                                                 TextArea.SCROLLBARS_NONE);

//in the bottom area the completed text is displayed
  private TextArea completeTextArea = new TextArea("", 13, 0,
      TextArea.SCROLLBARS_VERTICAL_ONLY);
  // private TextArea completeTextArea = new TextArea();

//text fields are used in the center panel to display the text of the templates
  private TextField templateTextField1 = new TextField("", 40);
  private TextField templateTextField2 = new TextField("", 30);
  private TextField templateTextField3 = new TextField("", 30);

//first character of a template indicates the screen type and order of components on screen.
  private char screenType;
  private String templateID, stringToSend;
  private char menuType;
  private NodeTree nodeTree; // = new NodeTree();
  private TemplateStore templateStore;
  private int graphx = 167, graphy = 32, tempSno = 0;

//******************************************************************************
//******************************************************************************
// acces to localized messages using drew configuration
    Drew.Util.Locale alexStrings;

//Construct the applet
  public Alex() {
  }

  Communication cdc;
  private String CODE = "alex";
  private String newline = System.getProperty("line.separator");

//CooperativeModule interface
  public void constructor(Communication cdc) {
    this.cdc = cdc;
  }

  //Initialize the applet
  public void init() {
    try {
      String dummy = cdc.getParameter("alex.topic");
      if (dummy != null) {
        topicStatement = "0 " + dummy + "\n";
      }
      alexStrings = new Drew.Util.Locale("alex.messages", Config.getLocale(cdc));
      adviceTextArea.setText(alexStrings.format("Advice1"));
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void start() {
  }

  public void stop() {
  }

  public void destroy() {
    stop();
  }

  public void clear() {
    completeTextArea.setText(topicStatement);
    sentenceNumber = 0;
    numberChoice1.removeAll();
    numberChoice1.add("0");
    numberChoice2.removeAll();
    numberChoice2.add("0");
  }

  public String getCode() {
    return CODE;
  }

  public String getTitle() {
    return "Alex 1-2-3";
  }

  /** put here XML implementation */
  public boolean messageFilter(Drew.Util.XMLmp.XMLTree event) {
    return true;
  }

  public void messageDeliver(java.lang.String user,
                             Drew.Util.XMLmp.XMLTree data) {
    if (getCode().equals(data.tag())) {
      messageDeliver(data.tag(), user, data.getText());
    }
  }

  public boolean messageFilter(String code, String origine) {
    return code.equals(getCode());
  }

  public void messageDeliver(String code, String who, String msg) {

    /**node holds the information of that move */
    Node node = new Node(who, msg);

    completeTextArea.setCaretPosition(999999);

//template 7 is the delete template -can't delete 0 node
    if (node.templateID.equals("7")) {
      if (!node.linksTo1.equals("0")) {
//      if (!node.linksTo1.equals(alexStrings.format("number_Prefix") + "0" +
//                                 alexStrings.format("number_Postfix"))){ // OR az/0 need to add check that links to belongs to user

        try {
          numberChoice1.remove(node.linksTo1);
          numberChoice2.remove(node.linksTo1);
        }
        catch (IllegalArgumentException e) {
          System.err.println("Cannot remove item " + node.linksTo1 +
                             " from choice");
        }
      }
    }

//Only increment sentence number for argue or explore templates
    if ( (node.menuType == 'A') | (node.menuType == 'E')) {
      sentenceNumber++;

//need to include number prefixes and postfixes for Hungarian = "" for other languages.
      numberChoice1.insert(String.valueOf(sentenceNumber), 0);
      numberChoice2.insert(String.valueOf(sentenceNumber), 0);

//      numberChoice1.insert(alexStrings.format("number_Prefix") +
//                                           String.valueOf(sentenceNumber) +
//                                           alexStrings.format("number_Postfix"),0);
//      numberChoice2.insert(alexStrings.format("number_Prefix") +
//                                           String.valueOf(sentenceNumber) +
//                                           alexStrings.format("number_Postfix"),0);
      completeTextArea.append(sentenceNumber + " <" + who + "> " +
                              node.completedString + "\n");

//add the node to the tree
      nodeTree.addNode(node);
    }
    else {
      completeTextArea.append(" <" + who + "> " + node.completedString + "\n");
    }

//update advice after specified number of turns
    turnNumber++;
    adviceTextArea.setText(alexStrings.format("Advice1"));
       if (node.menuType=='O')
         opinionCount++;
       opinionCount=opinionCount/2;
       advisorCount=sentenceNumber+opinionCount;
       if (advisorCount<4)
         adviceTextArea.setText(alexStrings.format("Advice1"));
       else
       if ((advisorCount%advisorDelay==0)|(advisorCount%advisorDelay==1)){
         nodeTree.updatePlayerTable();
//send personalised advice to each player
         adviceTextArea.setText(alexStrings.format(nodeTree.getQuestion(cdc.nom)));
       }
       else
         adviceTextArea.setText("");

  }

  /*** End CooperativeModule interface */

  public Dimension getPreferredSize() {
    return new Dimension(700, 480);
  }

  public Dimension getMinimumSize() {
    return new Dimension(550, 500);
  }

  private void jbInit() throws Exception {
//*****************************************************************************
//new objects to store templates from file
     templateStore = new TemplateStore();

// read the templates into the template store from the localised text files
    readTemplates = new ReadTemplates(cdc.getCodeBase(), templateStore,
                                      alexStrings.format("template_file"));

//new object to store play history and provide advice
    nodeTree = new NodeTree(templateStore);

//add templates to choice component
    templateStore.setTemplateChoice(argueChoice, templateStore.VArgue);
    templateStore.setTemplateChoice(exploreChoice, templateStore.VExplore);
    templateStore.setTemplateChoice(dialogueChoice, templateStore.VDialogue);
    templateStore.setTemplateChoice(opinionChoice, templateStore.VOpinion);

//initialse number Numberchoices with 0 - this is the sentence entered by the teacher (topic)
//Special prefix and postfix notation for Hungarian - other languages = ""
//    numberChoice1.insert(alexStrings.format("number_Prefix") + "0" +
//                         alexStrings.format("number_Postfix"), 0);
//    numberChoice2.insert(alexStrings.format("number_Prefix") + "0" +
//                         alexStrings.format("number_Postfix"), 0);
    numberChoice1.insert("0",0);
    numberChoice2.insert("0",0);

    /**
     * the layout for the ALEX screen
     */
    setLayout(borderLayout1);

    upper.setLayout(borderLayout2);

//selection panel*******************************************************************
    selection.setBackground(new Color(131, 144, 197));
    selection.setLayout(gridBagLayout);
    selection.setSize(20, 50);

    feedBack.setBackground(new Color(111, 137, 161));
    feedBack.setLayout(borderLayout3);
    this.setBackground(new Color(255, 211, 255));
    adviceTextArea.setRows(9);

    selectATemplateLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    AlexLabel.setFont(new java.awt.Font("Dialog", 2, 20));
    AlexLabel.setForeground(new Color(255, 79, 138));
    AlexLabel.setCursor(null);

    selectATemplateLabel.setText(alexStrings.format("select_template"));
    makeYourArgumentLabel.setText(alexStrings.format("make_Argument"));
    argueLabel.setText(alexStrings.format("argue_Label"));
    opinionLabel.setText(alexStrings.format("opinion_Label"));
    exploreLabel.setText(alexStrings.format("explore_Label"));
    dialogueLabel.setText(alexStrings.format("dialogue_Label"));
    submitButton.setForeground(new Color(255, 79, 138));
    submitButton.setLabel(alexStrings.format("submit_Label"));

    userTextArea1.setBackground(Color.white);
    adviceTextArea.setEditable(false);
    adviceTextArea.setFont(new java.awt.Font("Monospaced", 1, 12));
    adviceTextArea.setForeground(Color.white);
    adviceTextArea.setBackground(new Color(255, 79, 138));
    adviceTextArea.setColumns(13);

    GridBagConstraints c = new GridBagConstraints();
    c.anchor = GridBagConstraints.NORTHWEST;

    c.gridwidth = 2;
    c.gridheight = 2;
    c.gridx = 0;
    c.gridy = 0;

    completeTextArea.setBackground(new Color(231, 255, 255));
    selection.add(selectATemplateLabel, c);

    c.gridx = 2;
    c.weightx = 0;
    selection.add(AlexLabel, c);

    c.gridwidth = 1;
    c.gridheight = 1;
    c.gridx = 0;
    c.gridy = 2;
    selection.add(argueLabel, c);

    c.gridx = 1;
    c.gridy = 2;
    c.weightx = 1;
    selection.add(argueChoice, c);

    c.gridx = 0;
    c.gridy = 3;
    selection.add(exploreLabel, c);

    c.gridx = 1;
    c.gridy = 3;
    c.weightx = 1;
    selection.add(exploreChoice, c);

    c.gridx = 0;
    c.gridy = 4;
    selection.add(opinionLabel, c);

    c.gridx = 1;
    c.gridy = 4;
    c.weightx = 1;
    selection.add(opinionChoice, c);

    c.gridx = 0;
    c.gridy = 5;
    selection.add(dialogueLabel, c);

    c.gridx = 1;
    c.gridy = 5;
    c.weightx = 1;
    selection.add(dialogueChoice, c);

    feedBack.add(adviceTextArea);

//edit panel ****************************************************************

    edit.setBackground(new Color(125, 210, 223));
    edit.setFont(new java.awt.Font("Monospaced", 0, 12));
    edit.setLayout(flowLayout);

    userTextArea1.setBackground(Color.white);
    userTextArea1.setCursor(null);
    userTextArea1.setRows(3);
    userTextArea1.selectAll();

    completeTextArea.setColumns(4);
    completeTextArea.setEditable(false);
    completeTextArea.setFont(new java.awt.Font("Monospaced", 0, 12));
    completeTextArea.setText(topicStatement);

    templateTextField1.setBackground(SystemColor.inactiveCaptionBorder);
    templateTextField1.setEditable(false);
    templateTextField1.setFont(new java.awt.Font("Monospaced", 0, 12));
    templateTextField2.setBackground(SystemColor.inactiveCaptionBorder);
    templateTextField2.setColumns(50);
    templateTextField2.setEditable(false);
    templateTextField3.setBackground(SystemColor.activeCaptionBorder);
    templateTextField3.setColumns(50);
    templateTextField3.setEditable(false);

    makeYourArgumentLabel.setFont(new java.awt.Font("Monospaced", 1, 14));
    numberChoice1.setCursor(null);
    numberChoice1.setFont(new java.awt.Font("Monospaced", 0, 12));
    numberChoice2.setFont(new java.awt.Font("Monospaced", 0, 12));
    numberChoice2.setCursor(null);

    edit.add(makeYourArgumentLabel, null);

    upper.add(selection, BorderLayout.NORTH);
    upper.add(edit, BorderLayout.CENTER);
    upper.add(feedBack, BorderLayout.EAST);

    add(upper, BorderLayout.NORTH);

//completed panel ****************************************************************
    add(completed, BorderLayout.CENTER);
    completed.add(completeTextArea, null);
    gridLayout.setColumns(5);
    completed.setBackground(Color.gray);
    completed.setLayout(gridLayout);

//end panel initialisation*****************************************************

//Add listeners for choice objects**********************************************
    argueChoice.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        basicChoice_itemStateChanged(e);
      }
    });

    opinionChoice.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        questionChoice_itemStateChanged(e);
      }
    });

    exploreChoice.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        exploreChoice_itemStateChanged(e);
      }
    });

    dialogueChoice.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        dialogueChoice_itemStateChanged(e);
      }
    });

    numberChoice1.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        sentenceNumberChoice1_itemStateChanged(e);
      }
    });

    numberChoice2.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        sentenceNumberChoice2_itemStateChanged(e);
      }
    });
//end add listeners for choices*************************************************

    submitButton.setFont(new java.awt.Font("Monospaced", 3, 14));
    submitButton.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
        submitButton_actionPerformed(e);
      }
    });
  }

//set center panel according to choice
  void basicChoice_itemStateChanged(ItemEvent e) {
    setCenterPanel(argueChoice.getSelectedIndex(), templateStore.VArgue);
  }

  void questionChoice_itemStateChanged(ItemEvent e) {
    setCenterPanel(opinionChoice.getSelectedIndex(), templateStore.VOpinion);
  }

  void exploreChoice_itemStateChanged(ItemEvent e) {
    setCenterPanel(exploreChoice.getSelectedIndex(), templateStore.VExplore);
  }

  void dialogueChoice_itemStateChanged(ItemEvent e) {
    setCenterPanel(dialogueChoice.getSelectedIndex(), templateStore.VDialogue);
  }

  void setCenterPanel(int choiceIndex, Vector v) {
//copy number to choice to local number choice to avoid updating whilst editing
    localNumberChoice1.removeAll();
    localNumberChoice2.removeAll();

    for (int y = 0; y < numberChoice1.getItemCount(); y++) {
      localNumberChoice1.addItem(numberChoice1.getItem(y));
    }
    for (int y = 0; y < numberChoice2.getItemCount(); y++) {
      localNumberChoice2.addItem(numberChoice2.getItem(y));
    }

    Template t = (Template) v.elementAt(choiceIndex); //tempChoice1.removePropertyChangeListener();
    templateID = t.templateID;
    screenType = t.screenType;
    templateTextField1.setText(t.textPart1);
    templateTextField2.setText(t.textPart2);
    templateTextField3.setText(t.textPart3);
    templateTextField1.setColumns(t.textPart1.length());
    if (! (t.textPart2 == null))
      templateTextField2.setColumns(t.textPart2.length());
    if (! (t.textPart3 == null))
      templateTextField3.setColumns(t.textPart3.length());
    menuType = t.menuType;
    edit.removeAll();
    edit.add(makeYourArgumentLabel);

//TemplateType indicates order of components
    switch (screenType) {
      case 'a': {
        edit.add(templateTextField1, null);
        edit.add(localNumberChoice1, null);
        edit.add(templateTextField2, null);
        edit.add(userTextArea1);
        localNumberChoice1.requestFocus();
        break;
      }
      case 'b': {
        edit.add(templateTextField1, null);
        edit.add(localNumberChoice1, null);
        edit.add(templateTextField2, null);
        edit.add(userTextArea1);
        edit.add(templateTextField3, null);
        localNumberChoice1.requestFocus();
        break;
      }
      case 'c': {
        edit.add(templateTextField1, null);
        edit.add(localNumberChoice1, null);
        edit.add(templateTextField2, null);
        edit.add(localNumberChoice2, null);
        edit.add(templateTextField3, null);
        edit.add(userTextArea1);
        localNumberChoice1.requestFocus();
        break;
      }
      case 'd': {
        edit.add(templateTextField1, null);
        break;
      }
      case 'e': {
        edit.add(templateTextField1, null);
        edit.add(localNumberChoice1, null);
        localNumberChoice1.requestFocus();
        break;
      }
      case 'f': {
        edit.add(templateTextField1, null);
        edit.add(localNumberChoice1, null);
        edit.add(templateTextField2, null);
        localNumberChoice1.requestFocus();
        break;
      }
      case 'g': {
        edit.add(localNumberChoice1, null);
        edit.add(templateTextField1, null);
        edit.add(userTextArea1);
        localNumberChoice1.requestFocus();
        break;
      }

      default: {
        System.err.println("Error in screen type - check templates file ");
      }
    }

//on entering the first sentence the user can only select sentence 0 so focus can go straight to text
    if (firstSentence) {
      userTextArea1.requestFocus();
      firstSentence = false;
    }

//submit button required in all cases
    edit.add(submitButton);
    edit.validate();
  }

  void submitButton_actionPerformed(ActionEvent e) {

//completed string is built according to the template type i.e. components on center panel.
    boolean userEnteredText = true;
    stringToSend = " ";

    switch (screenType) {
      case 'a': {
        userEnteredText = containsText(userTextArea1.getText());
        if (userEnteredText) {
          stringToSend = templateTextField1.getText() + " " +
              localNumberChoice1.getSelectedItem() +
//              alexStrings.format("number_Postfix") + // " " +
              templateTextField2.getText() + " " +
              userTextArea1.getText();
        }
        break;
      }
      case 'b': {
        userEnteredText = containsText(userTextArea1.getText());
        if (userEnteredText) {
          stringToSend =
              templateTextField1.getText() + " "
              + localNumberChoice1.getSelectedItem()
//              alexStrings.format("number_Postfix") //+//" "
              + templateTextField2.getText() + " "
              + userTextArea1.getText() + " "
              + templateTextField3.getText();
        }
        break;
      }
      case 'c': {
        userEnteredText = containsText(userTextArea1.getText());
        if (userEnteredText) {
          stringToSend =
              templateTextField1.getText() + " "
              + localNumberChoice1.getSelectedItem() //" "
//              + alexStrings.format("number_Postfix")
              + templateTextField2.getText() + " "
              + localNumberChoice2.getSelectedItem() //+ " "
//              + alexStrings.format("number_Postfix")
              + templateTextField3.getText() + " "
              + userTextArea1.getText();
        }
        break;
      }
      case 'd': {
        stringToSend = templateTextField1.getText();
        break;
      }
      case 'e': {
        stringToSend = templateTextField1.getText() + " "
            + localNumberChoice1.getSelectedItem();

        break;
      }
      case 'f': {
        if (userEnteredText) {
          stringToSend =
              templateTextField1.getText() + " "
              + localNumberChoice1.getSelectedItem()
//              + alexStrings.format("number_Postfix") //+" "
              + templateTextField2.getText();
        }
        break;
      }
      case 'g': {
        userEnteredText = containsText(userTextArea1.getText());
        if (userEnteredText) {
          stringToSend =
              localNumberChoice1.getSelectedItem() // + " "
//              + alexStrings.format("number_Postfix")
              + templateTextField1.getText() + " "
              + userTextArea1.getText();
        }
        break;
      }
    } //end switch

//   if (!userEnteredText)
//      cdc.showStatus(alexStrings.format("invalid_Status"));
//   else {
//       cdc.showStatus(alexStrings.format("OKStatus"));

//******************************************************************************
     if (userEnteredText) {

       if (sentenceNumber == 0)
         initGrapher();

       if (menuType == 'A' | menuType == 'E')

         //             updateGrapher( localNumberChoice1.getSelectedItem(), userTextArea1.getText(), stringToSend, sentenceNumber);
         updateGrapher(localNumberChoice1.getSelectedItem(),
                       userTextArea1.getText(),
                       stringToSend, sentenceNumber, menuType);

       if (screenType == 'c') {
         cdc.envoiserveur(
             new XMLTree(CODE,
                         "#" + templateID + "#" + menuType + "#" + stringToSend +
                         "#" +
                         localNumberChoice1.getSelectedItem() + "#" +
                         localNumberChoice2.getSelectedItem()
                         )
             );
       }
       else {
         cdc.envoiserveur(
             new XMLTree(CODE,
                         "#" + templateID + "#" + menuType + "#" + stringToSend +
                         "#" +
                         localNumberChoice1.getSelectedItem()
                         )
             );
       }

//set fixed are to blank after the sentence has been submitted
       templateTextField1.setText("");
       templateTextField2.setText("");
       templateTextField3.setText("");
       userTextArea1.setText("");

       edit.removeAll();
       edit.add(makeYourArgumentLabel);
       //    cdc.showStatus(alexStrings.format("OKStatus"));
     }
  }

  //#### How to send event to the grapher ###############################################

  private static final String GRAPHCODE = "grapheur";

  void envoiGrapher(XMLTree cmd) {
    XMLTree event = new XMLTree(GRAPHCODE);

    event.add(cmd);
    cdc.envoiserveur(event);
  }

  void envoiGrapher(XMLTree cmd1, XMLTree cmd2) {
    XMLTree event = new XMLTree(GRAPHCODE);

    event.add(cmd1);
    event.add(cmd2);
    cdc.envoiserveur(event);
  }

  void envoiGrapher(XMLTree cmd1, XMLTree cmd2, XMLTree cmd3) {
    XMLTree event = new XMLTree(GRAPHCODE);

    event.add(cmd1);
    event.add(cmd2);
    event.add(cmd3);
    cdc.envoiserveur(event);
  }

  void envoiGrapher(XMLTree cmd1, XMLTree cmd2, XMLTree cmd3, XMLTree cmd4) {
    XMLTree event = new XMLTree(GRAPHCODE);

    event.add(cmd1);
    event.add(cmd2);
    event.add(cmd3);
    event.add(cmd4);
    cdc.envoiserveur(event);
  }

  void initGrapher() {

    String id = "0";
    XMLTree e;
    e = Grapheur.box(id);
    e.add(Grapheur.name("    " + topicStatement));
    e.add(Grapheur.comment(topicStatement));
    e.add(Grapheur.position(graphx, graphy));
    e.setAttribute("sticker", id);
    envoiGrapher(e);
  }

  void updateGrapher(String linksTo, String userText, String stringToSend,
                     int sentenceNumber, char menuType) {

//void updateGrapher(String linksTo, String userText, String stringToSend, int sentenceNumber) {
    String contentString = "";

    tempSno = sentenceNumber;
//hungarian links contain number prefix and postfix so extract digits (can be one or two).
    if (linksTo.length() > 3)
      linksTo = getDigits(linksTo);
    if (menuType == 'E')
      contentString = alexStrings.format("explore_Label");
    else if (menuType == 'A')
      contentString = alexStrings.format("argue_Label");
    else
      contentString = " ";
//  if (userText.length()>1)
//     contentString = extractContentString(userText);
//  else
//    contentString = extractContentString(stringToSend);

    Template t = templateStore.getTemplate(templateID);
    sentenceNumber++;
    String Snumber = String.valueOf(sentenceNumber);
    String arrowid = "arrow" + Snumber;

    XMLTree box, arrow, r1, r2;

    box = Grapheur.box(Snumber);
    box.add(Grapheur.name("    " + contentString));
    box.add(Grapheur.comment(stringToSend));
    box.setAttribute("sticker", Snumber);
    //     box.setAttribute(")

    arrow = Grapheur.arrow(arrowid);
    arrow.add(Grapheur.name(t.graphSymbol));
    arrow.add(Grapheur.comment(userText));

    r1 = Grapheur.relation(arrowid, linksTo);
    r2 = Grapheur.relation(Snumber, arrowid);

    XMLTree linksToBox = Grapheur.box(linksTo);

    if (t.proContra.equals("pro"))
      linksToBox.setAttribute("support", "true");

    if (t.proContra.equals("contra"))
      linksToBox.setAttribute("support", "false");

    envoiGrapher(linksToBox);

    graphx = 167;

//following code used to place boxes on graph - move to top after reaching the base of the graph
    int rows = 12;  //the number of rows of boxes on the grapheur.
    if (sentenceNumber > rows) {
      graphx += 324;
      tempSno -= rows;
    }
    if (sentenceNumber > rows * 2) {
      graphx += 240;
      tempSno -= rows;
    }

//we have run out of space.  Keep making new boxes in the same place.
    if (sentenceNumber>rows*3){
      graphx=400;
      tempSno=4;
    }

    graphy = (tempSno * 70) + 95;

    box.add(Grapheur.position(graphx, graphy));

    if (sentenceNumber % 2 == 0)
      graphx += 140;
    else
      graphx -= 140;

    arrow.add(Grapheur.position(graphx, graphy));
    envoiGrapher(box, arrow, r1, r2);
  }

  /**
   * to extract a string to use as the content of a grapheur box
   * @param userText
   * @param stringToSend
   * @return the truncated string to be used as box content
   */

  private String extractContentString(String theText) {

    String contentString = new String(" ");
    String s = "";
    StringTokenizer tok = new StringTokenizer(theText);
    int elementCount = 0;
    boolean tooLong = false;

    while (tok.hasMoreTokens() & elementCount <= 4 & !tooLong) {
      s = s + tok.nextElement() + " ";
      elementCount++;
      tooLong = s.length() > 40;
    }
    if (s.length() > 53)
      s = s.substring(0, 53);

    return s;
  }

  private int ARROWID = 1;
  String arrowId() {
    ARROWID++;
    return "arrow" + String.valueOf(ARROWID);
  }

//to extract digits from the linksTO string for Hungarian version where numbers have
//prefix and postfix
  private String getDigits(String linksTo) {
    String s = "";
    int x = 0;
    while (!Character.isDigit(linksTo.charAt(x)))
      x++;
    while (Character.isDigit(linksTo.charAt(x))) {
      s = s + linksTo.substring(x, x + 1);
      x++;
    }
    return s;
  }

  boolean containsText(String text) {

    for (int x = 0; x < text.length(); x++)
      if (!Character.isWhitespace(text.charAt(x)))
        return true;
    return false;
  }

  void sentenceNumberChoice1_itemStateChanged(ItemEvent e) {
    //if type c then we have another sentencenumberChoice to make;
    if (screenType == 'c')
      numberChoice2.requestFocus();
    else
      userTextArea1.requestFocus();
  }

  void sentenceNumberChoice2_itemStateChanged(ItemEvent e) {
    userTextArea1.requestFocus();
  }
}