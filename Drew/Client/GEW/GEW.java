/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import Drew.Client.Util.Communication;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Util.XMLmp.XMLTree;
import Drew.Client.Util.Config;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java .awt.event.ComponentListener;
import java .awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.Timer;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.border.Border;

/**
 *
 * @author Guillaume Chanel
 */
public class GEW extends DefaultCooperativeModule implements ActionListener, ComponentListener {

    //constants
    static final String CODE = "GEW"; //Default code of the module
    static final String EMOTION_CODE = "Emotion";
    static final String MESSAGE_CODE = "InputMessage";
    static final String NOEMOTION_CODE = "NoEmotion";
    static final ArrayList<String> DEFAULT_EMOTIONS =  //Default list of emotions if the config file is bad
            new ArrayList<String>() {{
                add("Admiration"); add("Love"); add("Contentment"); add("Anger"); add("Hate");
                add("Contempt"); add("Interest"); add("Joy"); add("Pride"); add("Amusement"); add("Pleasure");
                add("Disgust"); add("Fear"); add("Disappointment"); add("Shame"); add("Regret"); add("Guilt");
                add("Sadness"); add("Compassion"); add("Relief");
            }};
    
    //A message is prompted every timeTimer (ms) to ask user for input
    Timer messageTimer;
    int timeTimer = 3000;
    boolean dispMessageBegin = false;
    
    //GEW
    JButton noEmotionButton;
    ArrayList<GEWLine> GEW; //The lines of buttons of the GEW (one line per emotion)
    ArrayList<String> emotions; //The labels (emotion) of each line
    int nbEmotions; //the number of emotions (should be equal to length of 'emotions' and 'GEW')
    int nbPointsPerScale = 5; //Number of buttons in each line
    
    //A line over two has a different color, this is represented by the two variables bellow
    Color firstLineColor = new Color(0.65f, 0.65f, 0.65f);
    Color otherLineColor = new Color(0.75f, 0.75f, 0.75f);

    Dimension maxSizeLabels = new Dimension(0,0); //biggest height and width of labels (depends on the emotion text)

    //Variable for DREW communication
    XMLTree eventToSend = null;
    XMLTree eventContent = null;
    
    public GEW() {
        super();
    }

    public GEW(Communication cdc) {
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
        //called after GEW()        
     
        ////////////////////////////
        //Get the GEW parameters given in the config file
        
        
        //The number of points in each GEW line (scale)
        String result = central_applet.getParameter("GEW.scalesPoints");
        if( result != null )
            nbPointsPerScale = Integer.parseInt(result);
        
        //The number of emotions in the GEW + the list of emotions
        emotions = new ArrayList<String>();
        result = central_applet.getParameter("GEW.nbEmotions");
        if( result != null )
        {
            //set the number of emotion and reset the lists of emotions
            nbEmotions = Integer.parseInt(result);
            emotions.clear();
        
            //Search for each emotion name, if one is not found just stop the process
            //and use the standard GEW
            for(int ie = 0; ie < nbEmotions; ie++) {
                result = central_applet.getParameter("GEW.emotion" + (ie+1));
                if(result != null)
                    emotions.add(result);
                else //Bad parameter create the standard GEW
                {
                    System.err.println("Missing emotion, loading the default emotion list...");
                    emotions.clear();
                    emotions.addAll(DEFAULT_EMOTIONS);                    
                    break;
                }
            }
        }
        else //nbEmotions not found in the config file
            emotions.addAll(DEFAULT_EMOTIONS);            
        nbEmotions = emotions.size();       
        
        //Set the correct timer for (defaults to zero as initialized, if value is
        //zero to timer is set), the time is given in seconds
        result = central_applet.getParameter("GEW.timeInput");
        if( result != null )
            timeTimer = Integer.parseInt(result) * 1000;
        
        //Check if the emotion annotation message should be displayed in the beginning
        result = central_applet.getParameter("GEW.dispMessageBegin");
        if(result != null )
            dispMessageBegin = result.equalsIgnoreCase("true");
        
        //Add a fake user
        addUser("nobody"); //That is to keep color consistency with the Grapheur
    }

    @Override
    public Dimension getPreferredSize()
    {
        return getMinimumSize();
    }

    @Override
    public synchronized Dimension getMinimumSize()
    {
	return  new Dimension(400, 400);
    }
    
    
    //Create all the UI components + action listeners
    @Override
    public void init() {
        super.init();
        
        //Leason to you own events (especially resize)
        addComponentListener(this);   
        
        //Set the sizes of the panel
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension gewSize = new Dimension(scrDim.width/2,scrDim.height/2);
        
        //If the GEW is a standalone panel than set its size to half the screen
        if(this.getParent() == null)
            this.setSize(gewSize);
        
        //No layout to position the wheel manually
        setLayout(null);
        
        //Create the "No emotion" central button for wheel reset
        Drew.Util.Locale comment = new Drew.Util.Locale( "Drew.Locale.Client.GEW", Config.getLocale() );
        noEmotionButton = new JButton(comment.getString("NoEmotionButton"));
        noEmotionButton.setBackground(firstLineColor.brighter());
        noEmotionButton.setActionCommand(NOEMOTION_CODE);
        noEmotionButton.addActionListener(this);
        this.add(noEmotionButton);
        
        //Create the Lines with the appropriate number of button per line
        GEW = new ArrayList<GEWLine>();
        for(int i=0;i < nbEmotions; i++) {
            GEWLine currLine = new GEWLine(nbPointsPerScale, emotions.get(i));
            GEW.add(currLine); //Each line contains a certain number of buttons / points
            currLine.addToGEW(this); //Add each line of buttons to the current panel
            
            //Find the maximum label size (vertical and horizontal)
            Dimension size = currLine.getEmotionLabel().getPreferredSize();
            if (size.height > maxSizeLabels.height)
                maxSizeLabels.height = size.height;
            if (size.width > maxSizeLabels.width)
                maxSizeLabels.width = size.width;
            
        }
        
        //Update the wheel (position and sized of wheel elements)
        updateWheel();
        
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
    }
    
    
    private void updateWheel()
    {
        //Determine several sizes with respect to the size of the Panel
        Dimension panelSize = getSize();
        panelSize.setSize(new Dimension(panelSize.width,panelSize.height)); //Quick and dirty
        double panelRadius = Math.min(panelSize.width - maxSizeLabels.width*2, panelSize.height  - maxSizeLabels.height*2) / 2; //Biggest drawable circle in the panel (include lable size for this circle)
        double centralRadius = panelRadius / 4; //Central hole to put the "no emotion" button
        Point2D panelCenter = new Point2D.Double( ((double) panelSize.width) / 2, ((double) panelSize.height) / 2); //Point at the center of the panel
        int maxButtonRadius = (int) Math.min(panelRadius*Math.PI / (2*nbEmotions),30); //The radius of the biggest button is 30 except if it is to big (two emotions buttons overlap)
        
        //Position the no emotion central button for wheel reset
        int noEmotionButtonSize = Math.max((int) (centralRadius * Math.sqrt(2) * 0.75) , 5);
        noEmotionButton.setSize(noEmotionButtonSize , noEmotionButtonSize);
        noEmotionButton.setLocation(PointUtil.toPoint(PointUtil.add(panelCenter, -(noEmotionButtonSize / 2))));
        
        //Position each button according to the number of emotions and their size + add them to the Panel
        //This is done line by line, which all have a different angle
        double angleStep = 2*Math.PI / GEW.size();
        double angle = angleStep / 2;
        boolean isFirstColor = true; //for the color of the line (one over two)
        for(int il=0;il < GEW.size(); il++)
        {
            //Get the currentLine
            GEWLine currentLine = GEW.get(il);
            
            //Set the buttons to the correct size
            currentLine.setButtonsRadiusRange(7, maxButtonRadius);
            
            //Set the button to the correct color
            if(isFirstColor)
                currentLine.setButtonsColor(firstLineColor, null);
            else
                currentLine.setButtonsColor(otherLineColor, null);
            
            //Each line is composed of a starting Point (small button) and endPoint (big Button)
            Point2D endPoint = new Point2D.Double((Math.cos(angle) * panelRadius),(-Math.sin(angle) * panelRadius));
            endPoint = PointUtil.add(endPoint, panelCenter);
            Point2D startPoint = new Point2D.Double((Math.cos(angle) * centralRadius),(-Math.sin(angle) * centralRadius));
            startPoint = PointUtil.add(startPoint, panelCenter);
            currentLine.setLinePosition(startPoint, endPoint);            
            
            //Increase the angle for a new line
            angle += angleStep;
            isFirstColor = !isFirstColor;
        }        
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
        // TODO Auto-generated method stub
        //System.out.println(data);
        
        //This is the moment to set the main user name color
        if(user.equals(getUsername())) {
            for(int il=0;il < GEW.size(); il++)
                GEW.get(il).setButtonsColor(null, getColor(user));
        }
        
        //If this is a GEW message
       	if( getCode().equals( data.tag() ) ) {

            //If the message is an Emotion message than buttons should be changed
            if(data.getAttributeValue("Type", "").equals(EMOTION_CODE)) {
                
                //Parse the data received
                XMLTree content = (XMLTree) data.contents().elt(0);
                //System.out.println("Message content: " + content.toString());
                int lineID = Integer.parseInt(content.getAttributeValue("LineID"));
                int value = Integer.parseInt(content.getText());

                //Get the corresponding lines and buttons
                GEWLine currLine = GEW.get(lineID);
            
                //Set the line at the proper scale value
                currLine.setScaleValue(value, getColor(user));
            }
        }
    }

  
    @Override
    public void componentHidden(ComponentEvent e)
    {
    }
    
    @Override
    public void componentResized(ComponentEvent e)
    {
        updateWheel();
    }

    @Override
    public void componentShown(ComponentEvent e)
    {
    }

    @Override
    public void componentMoved(ComponentEvent e)
    {
    }
    
    private void sendMessageEmotion(String emotion, int value, int lineID, int buttonID)
    {
        eventContent = new XMLTree(emotion, value);
        eventContent.setAttribute("LineID", lineID);
        eventContent.setAttribute("ButtonID", buttonID);
        eventToSend = new XMLTree( CODE, eventContent);
        eventToSend.setAttribute("Type", EMOTION_CODE);
        sendServer(eventToSend);
    }
        
    @Override
    public void actionPerformed(ActionEvent ev) {
                
        //If the event comes from a GEW button        
        String action = ev.getActionCommand();
        if(action != null) {
            if(action.equals("GEWButton"))
            {
                //Get the components associated with the event
                GEWButton button = (GEWButton) ev.getSource();
                GEWLine line = button.getParentLine();
                int lineID = GEW.indexOf(line);
                int buttonID = line.getButtons().indexOf(button);
                Color mainUserColor = getColor(getUsername());
     
                //Send the event corresponding to the change of state
                sendMessageEmotion(line.getEmotionLabel().getText(), line.getScaleValue(mainUserColor), lineID, buttonID);
            
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
                Drew.Util.Locale comment = new Drew.Util.Locale( "Drew.Locale.Client.GEW", Config.getLocale() );
                JOptionPane.showMessageDialog(null,comment.getString(MESSAGE_CODE));
                messageTimer.start();
            }
            else if(action.equals(NOEMOTION_CODE)) {
                //Get the main user color
                Color mainUserColor = getColor(getUsername());
                
                //Reset all lines:
                //For each line check the scale value for main user and send
                //a new value of 0 only if the current value is not 0
                for(int lineID=0;lineID < GEW.size();lineID++) {
                    GEWLine currLine = GEW.get(lineID);
                    int currentValue = currLine.getScaleValue(mainUserColor);                    
                    if(currentValue != 0) {
                        //Not very nice but well (ButtonID is not very well computed...)
                        sendMessageEmotion(currLine.getEmotionLabel().getText(), 0, lineID, currentValue-1);
                        
                        //Cancel the timer and restart it to check for next delay between emotional information
                        if (timeTimer != 0)
                        {                
                            messageTimer.stop();
                            messageTimer.start();
                        }                        
                    }
                }
            }
                
        }
    }


    @Override
    public void stop() {
        //Just stop the timer
        if(timeTimer != 0)
            messageTimer.stop();
    }
    
    @Override
    public void destroy() {
        stop(); //the component should be stoped
    }
}
