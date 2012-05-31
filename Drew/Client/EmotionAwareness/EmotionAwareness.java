/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.EmotionAwareness;

import Drew.Client.Util.Communication;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Util.XMLmp.XMLTree;
import Drew.Client.Util.Config;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Timer;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.Box;

/**
 *
 * @author Guillaume Chanel
 */
public class EmotionAwareness extends DefaultCooperativeModule implements ActionListener, FocusListener {

    //constants    
    static final String CODE = "EmotionAwareness"; //Default code of the module
    static final int NB_TIME_EMOTIONS = 3; //Number of past + current emotions to display
    static final Color SELF_COLOR = Color.green; //Color for the fields concerning self emotions
    static final Color OTHER_COLOR = Color.blue; //Color for the fields concerning others emotions
    static final Color SELF_TEXT_COLOR = Color.black; //Color for the fields concerning self emotions
    static final Color OTHER_TEXT_COLOR = Color.white; //Color for the fields concerning others emotions
    
    static final String BUTTON_PRESS_ACTION = "ButtonPress";
    static final String FIELD_VALIDATED_ACTION = "FieldValidated";
    static final String REFRESH_BUTTON_ACTION = "Refresh";
    static final String MESSAGE_CODE = "InputMessage";
    static final String EMOTION_CODE = "Emotion";    
    static final String STARTED_CODE = "ModuleStarted";    
    static final String NOEMOTION_CODE = "NoEmotion"; //should not be an empty string
    static final int MAX_EMOTIONS = 10; //Maximum number of emotions (for either positive or negative emotions)
    static final ArrayList<String> DEFAULT_POSITIVE_EMOTIONS =  //Default list of emotions if the config file is bad
            new ArrayList<String>() {{
                add("Admiration"); add("Love"); add("Contentment");  add("Interest"); add("Joy"); add("Pride");
                add("Amusement"); add("Pleasure"); add("Compassion"); add("Relief");
            }};
    static final ArrayList<String> DEFAULT_NEGATIVE_EMOTIONS =  //Default list of emotions if the config file is bad
            new ArrayList<String>() {{
                add("Anger"); add("Hate"); add("Contempt");add("Disgust"); add("Fear"); add("Disappointment");
                add("Shame"); add("Regret"); add("Guilt"); add("Sadness"); 
            }};
    
    //A message is prompted every timeTimer (ms) to ask user for input
    Timer messageTimer;
    int timeTimer = 3000;
    boolean dispMessageBegin = false;
    
    //Variable for DREW communication
    XMLTree eventToSend = null;
    XMLTree eventContent = null;

    //EmotionAwareness GUI
    int nbPosEmotions;
    ArrayList<String> posEmotions;
    int nbNegEmotions;
    ArrayList<String> negEmotions;
    ArrayList<String> stringSelfEmo = new ArrayList<String>();
    ArrayList<ResizableTextField> textFieldsSelfEmo = new ArrayList<ResizableTextField>();
    ArrayList<String> stringOthersEmo = new ArrayList<String>();
    ArrayList<ResizableTextField> textFieldsOthersEmo = new ArrayList<ResizableTextField>();
    String lastEmotionEntered = "";
    boolean emotionValidated = false;
    
    
    public EmotionAwareness() {
        super();
    }

    public EmotionAwareness(Communication cdc) {
        this();
        this.constructor(cdc);
       
    }
    
    @Override
    public void clear() {
        // TODO R�initialiser les variables n�cessaires
        // sachant que toute la trace sera envoy�e par la suite

    }

    @Override
    public void constructor(Communication cdc) {
        super.constructor(cdc);
     
       
        ////////////////////////////
        //Get the EmotionAwareness parameters given in the config file
        
        //The number of positive emotions in the EmotionAwareness + the list of positive emotions
        posEmotions = new ArrayList<String>();
        String result = central_applet.getParameter("EmotionAwareness.nbPosEmotions");
        if( result != null )
        {
            //set the number of positive emotion (forbiden to have more than MAX_EMOTIONS)
            nbPosEmotions = Integer.parseInt(result);            
            if(nbPosEmotions > MAX_EMOTIONS) {
                System.err.println("Too many positive emotions (max." + MAX_EMOTIONS + "), loading the default emotion list...");
                posEmotions.clear();
                posEmotions.addAll(DEFAULT_POSITIVE_EMOTIONS);                    
            }
            else {
                //Search for each emotion name, if one is not found just stop the process
                //and use the standard Emotion list
                for(int ie = 0; ie < nbPosEmotions; ie++) {
                    result = central_applet.getParameter("EmotionAwareness.posEmotion" + (ie+1));
                    if(result != null)
                        posEmotions.add(result);
                    else //Bad parameter create the standard EmotionAwareness
                    {
                        System.err.println("Missing positive emotion, loading the default emotion list...");
                        posEmotions.clear();
                        posEmotions.addAll(DEFAULT_POSITIVE_EMOTIONS);                    
                        break;
                    }
                }
            }
        }
        else //nbEmotions not found in the config file
            posEmotions.addAll(DEFAULT_POSITIVE_EMOTIONS);            
        nbPosEmotions = posEmotions.size();      

        //The number of negative emotions in the EmotionAwareness + the list of negative emotions
        negEmotions = new ArrayList<String>();
        result = central_applet.getParameter("EmotionAwareness.nbNegEmotions");
        if( result != null )
        {
            //set the number of emotion (forbiden to have more than MAX_EMOTIONS)
            nbNegEmotions = Integer.parseInt(result);
            if(nbPosEmotions > MAX_EMOTIONS) {
                System.err.println("Too many positive emotions (max." + MAX_EMOTIONS + "), loading the default emotion list...");
                posEmotions.clear();
                posEmotions.addAll(DEFAULT_POSITIVE_EMOTIONS);                    
            }
            else {

                //Search for each emotion name, if one is not found just stop the process
                //and use the standard Emotion list
                for(int ie = 0; ie < nbNegEmotions; ie++) {
                    result = central_applet.getParameter("EmotionAwareness.negEmotion" + (ie+1));
                    if(result != null)
                        negEmotions.add(result);
                    else //Bad parameter create the standard EmotionAwareness
                    {
                        System.err.println("Missing negative emotion, loading the default emotion list...");
                        negEmotions.clear();
                        negEmotions.addAll(DEFAULT_NEGATIVE_EMOTIONS);                    
                        break;
                    }
                }
            }
        }
        else //nbEmotions not found in the config file
            negEmotions.addAll(DEFAULT_NEGATIVE_EMOTIONS);            
        nbNegEmotions = negEmotions.size();      
       
        
        //Set the correct timer for (defaults to zero as initialized, if value is
        //zero to timer is set), the time is given in seconds
        result = central_applet.getParameter("EmotionAwareness.timeInput");
        if( result != null )
            timeTimer = Integer.parseInt(result) * 1000;
        
        //Check if the emotion annotation message should be displayed in the beginning
        result = central_applet.getParameter("EmotionAwareness.dispMessageBegin");
        if(result != null )
            dispMessageBegin = result.equalsIgnoreCase("true");
        
        //Add a fake user
        addUser("nobody"); //That is to keep color consistency with the Grapheur
        
        ////////////////////////////
        // Initialize the emotions strings with the proper number of empty strings
        for(int i=0; i<NB_TIME_EMOTIONS; i++) {
            stringSelfEmo.add("");
            stringOthersEmo.add("");
        }
            

    }

    @Override
    public Dimension getPreferredSize()
    {
        return super.getPreferredSize();
    }

    @Override
    public synchronized Dimension getMinimumSize()
    {
	return  super.getMinimumSize();
    }
    
    
    //Create all the UI components + action listeners
    @Override
    public void init() {
        super.init();
        
        //Create the drew locale system for translation to create components with the correct names
        Drew.Util.Locale language = new Drew.Util.Locale( "Drew.Locale.Client.EmotionAwareness", Config.getLocale() );              
        
        //Create the two panels for the display of emotions and add in a horizontal panel
        JPanel selfEmoDisplayPanel = createEmotionTextBag(language.getString("SelfEmotionsTitle"), textFieldsSelfEmo, SELF_COLOR, SELF_TEXT_COLOR, true); //TODO change by a locale
        JPanel othersEmoDisplayPanel = createEmotionTextBag(language.getString("OthersEmotionsTitle"), textFieldsOthersEmo, OTHER_COLOR, OTHER_TEXT_COLOR, false); //TODO change by a locale
        JPanel emoDisplayPanel = new JPanel();
        emoDisplayPanel.setLayout(new BoxLayout(emoDisplayPanel, BoxLayout.PAGE_AXIS));
        emoDisplayPanel.add(selfEmoDisplayPanel);
        emoDisplayPanel.add(othersEmoDisplayPanel);
        
        
        //Create the two button panels and add then vertically to a single one
        JPanel posEmotionsPanel = createButtonBag(posEmotions);
        posEmotionsPanel.setBorder(BorderFactory.createTitledBorder(language.getString("PosButtonsTitle")));
        JPanel negEmotionsPanel = createButtonBag(negEmotions);
        negEmotionsPanel.setBorder(BorderFactory.createTitledBorder(language.getString("NegButtonsTitle")));
        JPanel emoButtonsPanel = new JPanel();
        emoButtonsPanel.setLayout(new BoxLayout(emoButtonsPanel, BoxLayout.LINE_AXIS));
        emoButtonsPanel.add(Box.createVerticalGlue());
        emoButtonsPanel.add(posEmotionsPanel);
        emoButtonsPanel.add(Box.createVerticalGlue());
        emoButtonsPanel.add(negEmotionsPanel);
        emoButtonsPanel.add(Box.createVerticalGlue());
        
        //Add all the stuff to the Drew module and set the Layout correctly
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.add(emoDisplayPanel);
        this.add(emoButtonsPanel);
        
        //Finally add the refresh button at the end
        JButton refreshButton = new JButton(language.getString("RefreshButtonTitle"));
        refreshButton.setActionCommand(REFRESH_BUTTON_ACTION);
        refreshButton.addActionListener(this);
        refreshButton.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
        refreshButton.setAlignmentX(CENTER_ALIGNMENT);
        this.add(refreshButton);
        
    }

    @Override
    public void start()    {
        //Create the timer with the proper time
        //Lunch the message Timer: start counting the time up to the moment an emotion will be entered
        if(timeTimer != 0)
        {
            messageTimer = new Timer(timeTimer, this);        
            messageTimer.setRepeats(false);
            messageTimer.setActionCommand(MESSAGE_CODE);
            messageTimer.start();
            if(dispMessageBegin) //Simulate event to display the first message immediately
                this.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, messageTimer.getActionCommand()));
        }
        
        //Send a message to notify that the module has started
        eventToSend = new XMLTree( getCode());
        eventToSend.setAttribute("Type", STARTED_CODE);
        sendServer(eventToSend);

    }
    
    private JPanel createEmotionTextBag(String panelTitle, ArrayList<ResizableTextField> createdFields, Color backColor, Color textColor, boolean editable) {
        //Create the panel of self-reported emotions (containing mainly three TextFields)
        JPanel emotionPanel = new JPanel();
        emotionPanel.setLayout(new BoxLayout(emotionPanel, BoxLayout.PAGE_AXIS));
        emotionPanel.setBorder(BorderFactory.createTitledBorder(panelTitle));
        for(int i=0; i<NB_TIME_EMOTIONS; i++) {
            ResizableTextField newField = new ResizableTextField(15); //TODO: change to maximum length of emotion            
            newField.setFont(new Font(newField.getFont().getFontName(), Font.BOLD,newField.getFont().getSize()));
            if(i == 0) {
                newField.setBackground(backColor);                
                newField.setEditable(editable);
                if(editable) {
                    newField.addFocusListener(this);
                    newField.setActionCommand(FIELD_VALIDATED_ACTION);
                    newField.addActionListener(this);                    
                }
            }
            else {
                newField.setBackground(backColor.darker());
                newField.setEditable(false);
            }
            newField.setForeground(textColor);
            newField.setHorizontalAlignment(ResizableTextField.CENTER);
            newField.setMinimumSize(newField.getPreferredSize());
            newField.setMaximumSize(new Dimension(Short.MAX_VALUE*2, Short.MAX_VALUE*2));
            createdFields.add(newField);
            emotionPanel.add(newField);            
        }
        return emotionPanel;
    }
    
    private JPanel createButtonBag(ArrayList<String> buttonsTitles) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.PAGE_AXIS));
        for(int i=0;i < buttonsTitles.size();i++) {
            JButton newButton = new JButton(buttonsTitles.get(i));
            newButton.addActionListener(this);
            newButton.setActionCommand(BUTTON_PRESS_ACTION);
            newButton.setMaximumSize(new Dimension(Short.MAX_VALUE, Short.MAX_VALUE));
            buttonsPanel.add(newButton);            
        }
        return buttonsPanel;
    }
    
    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return CODE;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return CODE;
    }
    
    @Override
    public void moduleMessageDeliver(String user, XMLTree data) {
        
        if(Config.getDebug())
            System.out.println("Message received " + data);
        
        //If this is an EmotionAwareness message
       	if( getCode().equals( data.tag() ) ) {

            //If the message is an Emotion message than buttons should be changed
            if(data.getAttributeValue("Type", "").equals(EMOTION_CODE)) {

                //Parse the data received
                String newEmotion = (String) data. contents().elt(0);
                //If the message comes from the user than change the self-emotion
                if(user.equals(getUsername())) {                
                    //Change the emotion
                    inputEmotion(newEmotion, stringSelfEmo);
                    updateEmotionTextFields(stringSelfEmo, textFieldsSelfEmo);
                    
                    //remove the focus of the text field if it has it (if put somewhere else there is word repretition ?)
                    if(textFieldsSelfEmo.get(0).hasFocus())
                        textFieldsSelfEmo.get(0).transferFocus();                        
                }
                else //if the message does not come from the user change the others-emotion
                {
                    inputEmotion(newEmotion, stringOthersEmo);
                    updateEmotionTextFields(stringOthersEmo, textFieldsOthersEmo);
                }

            }
        }
     }

  
    private void sendMessageEmotion(String emotion)
    {
        eventToSend = new XMLTree( getCode(), emotion);
        eventToSend.setAttribute("Type", EMOTION_CODE);
        sendServer(eventToSend);
    }
        
    @Override
    public void actionPerformed(ActionEvent ev) {

        String action = ev.getActionCommand();
        
        if(Config.getDebug())
            System.out.println("Received action: " + action);       
        
        
        if(action != null) {
            if(action.equals(FIELD_VALIDATED_ACTION)) //An emotion has been entered in the field
            {
                //Get the components associated with the event  
                ResizableTextField currentTextField = ((ResizableTextField) ev.getSource());
                String newEmotion = currentTextField.getText();
                
                //Send the message to server (server does not like empty strings)
                if(newEmotion.equals(""))
                    newEmotion = NOEMOTION_CODE;
                sendMessageEmotion(newEmotion);
                        
                //remove the focus to show validation (colored background - that does not work here: it kinf of doubles the text...)
                //currentTextField.transferFocus();
               
                //Cancel the timer and restart it to check for next delay between emotional information
                if (timeTimer != 0)
                {                
                    messageTimer.stop();
                    messageTimer.start();
                }   
            }
            else if(action.equals(MESSAGE_CODE)) //Timer
            {        
                //stop the timer (not really needed), output the message, and retart for a new run
                messageTimer.stop();

                //Generate a message to report for the message event
                eventToSend = new XMLTree( CODE );
                eventToSend.setAttribute("Type", MESSAGE_CODE);
                sendServer(eventToSend);
            
                //Generate the message
                Drew.Util.Locale comment = new Drew.Util.Locale( "Drew.Locale.Client.EmotionAwareness", Config.getLocale() );
                JOptionPane.showMessageDialog(null,comment.getString(MESSAGE_CODE));
                messageTimer.start();
            }
            else if(action.equals(BUTTON_PRESS_ACTION)) { //An emotion button has be pressed
                
                //Just send an emotion message with the name of the button that was pressed
                String buttonName = ((JButton) ev.getSource()).getText();
                if(buttonName != null)
                    sendMessageEmotion(buttonName);
                
                //Cancel the timer and restart it to check for next delay between emotional information
                if (timeTimer != 0)
                {                
                    messageTimer.stop();
                    messageTimer.start();
                }                        
            }
            else if(action.equals(REFRESH_BUTTON_ACTION)) { //The refresh button has been pressed
                
                //Just send an emotion message with the no emotion code
                sendMessageEmotion(NOEMOTION_CODE);
                
                //Cancel the timer and restart it to check for next delay between emotional information
                if (timeTimer != 0)
                {                
                    messageTimer.stop();
                    messageTimer.start();
                }                        
            }
                
        }
  
 
    }

    private void inputEmotion(String newEmotion, ArrayList<String> stringEmotions) {
        //If the first emotion is empty, do not move down just replace the empty emotion
        if(!stringEmotions.get(0).equals("")) {
            //Move the emotions down for all emotion except the first one
            if(NB_TIME_EMOTIONS > 1) {            
                for(int i=stringEmotions.size()-1;i > 0;i--) {
                    String precEmotion = stringEmotions.get(i-1);
                    if(precEmotion != null)
                        stringEmotions.set(i, precEmotion);
                }            
            }
        }
        
        //Set the first emotion (if it is a no emotion code then just clear the first emotion)
        if(newEmotion.equals(NOEMOTION_CODE))
            stringEmotions.set(0, "");
        else
            stringEmotions.set(0, newEmotion);
        
    }
    
    private void updateEmotionTextFields(ArrayList<String> emotionList, ArrayList<ResizableTextField> fieldList){
        int sizeLists = emotionList.size();
        if(sizeLists != fieldList.size())
            System.err.println("Error: the field list and the string list should have the same size");
        for(int i=0; i < sizeLists; i++)
            fieldList.get(i).setText(emotionList.get(i));
    }            
    
    @Override
    public void stop() {
        //Just stop the timer
        if(timeTimer != 0 && messageTimer != null)
            messageTimer.stop();
    }
    
    @Override
    public void destroy() {
        stop(); //the component should be stoped
    }
    
    //This function is called when the focus is gained by a editable
    //ResizableTextField (the one to enter his/her own emotion)
    @Override
    public void focusGained(FocusEvent fe) {
        
        //Clear the text and set the background white
        ResizableTextField currentField = ((ResizableTextField) fe.getComponent());
        currentField.setBackground(Color.white);
        currentField.setForeground(Color.black);
        currentField.setText("");
    }

    //This function is called when the focus is lost by the first editable
    //ResizableTextField (the one to enter his/her own emotion)
    @Override
    public void focusLost(FocusEvent fe) {
        //Undo the text entry and put the last emotion if needed
        ResizableTextField currentField = ((ResizableTextField) fe.getComponent());
        if(!currentField.getText().equals(stringSelfEmo.get(0)))
            currentField.setText(stringSelfEmo.get(0));
        currentField.setBackground(SELF_COLOR);
        currentField.setForeground(SELF_TEXT_COLOR);
    }
}
