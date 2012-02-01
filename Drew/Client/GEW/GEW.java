/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import Drew.Client.Util.Communication;
import Drew.Client.Util.CooperativeModule;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Client.Util.TokenRing;
import Drew.Util.XMLmp.XMLTree;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
        
/**
 *
 * @author Guillaume Chanel
 */
public class GEW extends DefaultCooperativeModule implements ActionListener, ComponentListener {

    //UI objects definition
    static boolean matchGraheurColor = true;
    static final String CODE = "GEW";
    static final ArrayList<String> DEFAULT_EMOTIONS = 
            new ArrayList<String>() {{
                add("Admiration"); add("Love"); add("Contentment"); add("Anger"); add("Hate");
                add("Contempt"); add("Interest"); add("Joy"); add("Pride"); add("Amusement"); add("Pleasure");
                add("Disgust"); add("Fear"); add("Disappointment"); add("Shame"); add("Regret"); add("Guilt");
                add("Sadness"); add("Compassion"); add("Relief");
            }};

    ArrayList<GEWLine> GEW;
    ArrayList<String> emotions;
    int nbEmotions;
    int nbPointsPerScale = 5;
    Dimension maxSizeLabels = new Dimension(0,0);
    XMLTree eventToSend = null;
    XMLTree eventContent = null;

    Color firstLineColor = new Color(0.65f, 0.65f, 0.65f); 
    Color otherLineColor = new Color(0.75f, 0.75f, 0.75f);
    
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
        
        //Add a fake user
        addUser("nobody"); //That is to keep color consistency with the Grapheur
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
    }
    
    
    private void updateWheel()
    {
        //Determine several sizes with respect to the size of the Panel
        Dimension panelSize = getPreferredSize();
        panelSize.setSize(new Dimension(panelSize.width,panelSize.height)); //Quick and dirty
        double panelRadius = Math.min(panelSize.width - maxSizeLabels.width*2, panelSize.height  - maxSizeLabels.height*2) / 2; //Biggest drawable circle in the panel (include lable size for this circle)
        double centralRadius = panelRadius / 4; //Central hole to put the "no emotion" button
        Point2D panelCenter = new Point2D.Double( ((double) panelSize.width) / 2, ((double) panelSize.height) / 2); //Point at the center of the panel
        int maxButtonRadius = (int) Math.min(panelRadius*Math.PI / (2*nbEmotions),30); //The radius of the biggest button is 30 except if it is to big (two emotions buttons overlap)
        
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
        
       	if( getCode().equals( data.tag() ) ) {
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
    
    @Override
    public void actionPerformed(ActionEvent ev) {
                
        //Get the components associated with the event
        GEWButton button = (GEWButton) ev.getSource();
        GEWLine line = button.getParentLine();
        int lineID = GEW.indexOf(line);
        int buttonID = line.getButtons().indexOf(button);
        Color mainUserColor = getColor(getUsername());
     
        //Send the event corresponding to the change of state
        eventContent = new XMLTree(line.getEmotionLabel().getText(), line.getScaleValue(mainUserColor));
        eventContent.setAttribute("LineID", lineID);
        eventContent.setAttribute("ButtonID", buttonID);
        eventToSend = new XMLTree( CODE, eventContent);
        sendServer(eventToSend);
    }
}
