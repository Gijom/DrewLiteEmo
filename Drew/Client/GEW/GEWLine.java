/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.JLabel;

/**
 * The GEWLine class works as a kind of container containing many GEWButtons and positioning them in line
 * in the current Form (). It also set the size of the buttons regularly between min and may button radius.
 * In addition to the buttons a line also contains a label placed at the end of the line.
 * 
 * This class also controls that only one button is active in a line by getting the ActionEvent of each button.
 * 
 * @author chanel
 */
public class GEWLine implements ActionListener {
    
    private ArrayList<GEWButton> GEWButtons;    //Smallest button is the first of the list  
    private JLabel emotion;                     //The lable of the emotion corresponding to this line
    private int minButtonRadius;                //The maximum size of a button in the line
    private int maxButtonRadius;                //The minimum size of a button in the line
    private Color buttonsColor;                 //The basic color of each button of the line
    
    /**
     * Construct an empty line
     */
    public GEWLine() {
        super();
    }
    
    /**
     * Constructs a line with nbButtons and a label emotion
     * @param nbButtons
     * @param emotion 
     */
    public GEWLine(int nbButtons, String emotion) {
        buttonsColor = null;
        createUI(nbButtons, emotion);
    }
    
    /**
     * Constructs a line with nbButtons, a lable emotion, the radius of the buttons range from
     * minButtonRadius to maxButtonRadius
     * @param nbButtons
     * @param emotion
     * @param minButtonRadius
     * @param maxButtonRadius 
     */
    public GEWLine(int nbButtons, String emotion, int minButtonRadius, int maxButtonRadius) {
        this(nbButtons, emotion);
        
        //Set the size of each button
        setButtonsRadiusRange(minButtonRadius, maxButtonRadius);
    }
    
    
    /**
     * Constructs a line with nbButtons, a lable emotion, the radius of the buttons range from
     * minButtonRadius to maxButtonRadius. The basic (at rest / unpressed) color of the buttons is set
     * to color
     * @param nbButtons
     * @param emotion
     * @param color
     * @param minButtonRadius
     * @param maxButtonRadius 
     */
    public GEWLine(int nbButtons, String emotion, Color color, int minButtonRadius, int maxButtonRadius) {
        this(nbButtons, emotion, minButtonRadius, maxButtonRadius);
        
        buttonsColor = color;
        createUI(nbButtons, emotion);
    }
    
    /**
     * Generate a line with nbButtons and a lable emotion, this method is called by the constructors
     * @param nbButtons
     * @param emotion 
     */
    private void createUI(int nbButtons, String emotion) {
        //Create the new vector of GEWButtons
        GEWButton newButton;
        GEWButtons = new ArrayList<GEWButton>(nbButtons);
        for(int ib = 0;ib < nbButtons;ib++) {
            if(buttonsColor != null)
                newButton = new GEWButton(buttonsColor);            
            else
                newButton = new GEWButton();  
            newButton.setParentLine(this);
            newButton.addActionListener(this);
            GEWButtons.add(newButton);            
        }
        
        //Create and resize the label
        this.emotion = new JLabel(emotion);
        //this.emotion.setBorder(BorderFactory.createLineBorder(Color.black)); //for labels use setinsets ?
        this.emotion.setSize(this.emotion.getPreferredSize());        
    }



    /**
     * Unclick all the buttons that have been pressed by the user coloredUser in the line
     * @param coloredUser 
     */
    public void reset(Color coloredUser) {
        GEWButton currButton;
        for(int ib=0;ib < GEWButtons.size();ib++) {
            currButton = GEWButtons.get(ib);
            if(currButton.isPressed(coloredUser))
                currButton.clickButton(coloredUser);
        }
    }
    

    /**
     * Return the number of the button pressed by the user coloredUser ranging from one to N where:
     *  - 1 is the smallest (first) button
     *  - N is the last biggest button
     *  - 0 if no buttons are pressed
     * @param coloredUser
     * @return a number from 0 (no button pressed) to 5 (button 5 pressed)
     */
    public int getScaleValue(Color coloredUser) {
        int ib;
        
        for(ib=0;ib < GEWButtons.size();ib++)
            if(GEWButtons.get(ib).isPressed(coloredUser))
                break;
        
        if(ib >= GEWButtons.size()) //No button clicked
            return 0;
        else
            return ib+1;        
    }
    
    
    /**
     * Set the scale value by pushing the correct buttons, for instance if scale value value is zero, no
     * button is pushed, if scale value is 3 the third button is pushed
     * This is done for the chosen coloredUser
     * @param value
     * @param coloredUser 
     * @see getScaleValue
     */
    public void setScaleValue(int value, Color coloredUser) {
        int idButton = value - 1;
        System.out.println("IdButton : " + idButton);
        GEWButton currButton;
        for(int ib=0;ib < GEWButtons.size();ib++) {
            currButton = GEWButtons.get(ib);
            if(ib == idButton)
            {
                if(!currButton.isPressed(coloredUser))
                    currButton.clickButton(coloredUser);
            }
            else
            {
                if (currButton.isPressed(coloredUser))
                    currButton.clickButton(coloredUser);
            }
        }
        
    }
    
    /**
     * Set the color of the buttons by giving the rest (unpressed) button color
     * and the main user color (when pressed by the mouse user)
     * @param color the resting color of the button
     * @param userColor the mainUser button color 
     */
    public void setButtonsColor(Color color, Color userColor)
    {
        buttonsColor = color;
        for(int ib = 0;ib < GEWButtons.size();ib++)
            GEWButtons.get(ib).setColor(color, userColor);
    }
    
    /**
     * Set the minimum and maxumum radius of the buttons, all buttons radius will be linearly scaled in this range
     * @param minButtonRadius
     * @param maxButtonRadius 
     */
    public void setButtonsRadiusRange(int minButtonRadius, int maxButtonRadius)
    {
        if(minButtonRadius > maxButtonRadius)
            minButtonRadius = maxButtonRadius;
        
        this.minButtonRadius = minButtonRadius;
        this.maxButtonRadius = maxButtonRadius;
        
        setButtonsRadius();
    }            
    
    /**
     * Actually sets the radius of the buttons to have a linear trend between minButtonRadius and maxButtonRadius
     */
    private void setButtonsRadius() {
        
        int nbButtons = GEWButtons.size();
        double step = ((double) (maxButtonRadius - minButtonRadius)) / (nbButtons - 1);
        double buttonRadius = minButtonRadius;
        for(int ib = 0; ib < GEWButtons.size(); ib++) {            
            GEWButtons.get(ib).setRadius(buttonRadius);
            buttonRadius += step;
        }
    }
    
    /**
     * Set the position of the line by giving the beginning and end point
     * The first point give the edge of the smallest button, the second
     * point the edge of the biggest button
     * 
     * TODO the label is added after the end of p2, this had to be corrected later
     * 
     * @param p1 beginning point
     * @param p2 end point
     */
    public void setLinePosition(Point2D p1, Point2D p2) {
        double lineLength = p1.distance(p2);
        
        //Compute the unit vector giving the direction of the line
        Point2D vector = PointUtil.divide(PointUtil.substract(p2, p1), lineLength);
        
        //Compute the sum of the buttons diameter
        double buttonsLength = 0;
        for(int ib = 0; ib < GEWButtons.size(); ib++)
            buttonsLength += GEWButtons.get(ib).getRadius() * 2;

        //Compute the space (or overlap if space is negative) beetween buttons
        double interSpace = (lineLength - buttonsLength) / GEWButtons.size();
        
        //Place each button
        Point2D center = p1;
        for(int ib = 0; ib < GEWButtons.size(); ib++) {
            GEWButton currentButton = GEWButtons.get(ib);
            center = PointUtil.add(center, PointUtil.multiply(vector, currentButton.getRadius()));
            currentButton.setCenter(PointUtil.toPoint(center));        
            center = PointUtil.add(center, PointUtil.multiply(vector, currentButton.getRadius()));
            center = PointUtil.add(center, PointUtil.multiply(vector, interSpace));
        }
        
        //Place the label: depending on the orientation of the line the label is placed
        //differently (one of the four corner is used for position)
        //Not too general: missing EAST, WEST, NORTH, SOUTH
        switch(PointUtil.getOrientationUI(vector)) {
            case NORTH_EAST: //Bottom left corner
                center = PointUtil.addY(center, -emotion.getHeight());
                break;
            case NORTH_WEST: //Bottom right corner
                center = PointUtil.addX(center, -emotion.getWidth());
                center = PointUtil.addY(center, -emotion.getHeight());
                break;
            case SOUTH_EAST: //Upper left corner
                break;
            case SOUTH_WEST: //Upper right corner
                center = PointUtil.addX(center, -emotion.getWidth());
                break;
        }
        emotion.setLocation(PointUtil.toPoint(center));
        
    }
    
    
    /**
     * Add the line to the GEW panel c
     * This is a not a nice function but is useful to add the different
     * listeners and component to the panel
     * @param c 
     */
    public void addToGEW(GEW c) //c'est pas joli joli
    {
        for(int ib = 0; ib < GEWButtons.size(); ib++) {
            GEWButton currButton = GEWButtons.get(ib);
            c.add(currButton);
            currButton.addActionListener(c);
        }
        c.add(emotion);
    }

    /**
     * Get the list of buttons in the line
     * @return 
     */
    public ArrayList<GEWButton> getButtons() {
        return GEWButtons;
    }
    
    /**
     * Get the label of the line
     * @return 
     */
    public JLabel getEmotionLabel() {
        return emotion;
    }
    
    /**
     * Set the label of the line
     * @param label 
     */
    public void setEmotionLabel(JLabel label) {
        emotion = label;
    }
        
    /*
     * Check if the label (emotion) of a line has a given name
     */
    public boolean isNameEqual(String lineName) {
        return lineName.equalsIgnoreCase(this.emotion.getText());
    }
    
    /**
     * When a button is pressed, this event handler ensure that all the other buttons
     * are unpressed
     * @param ev 
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        
        GEWButton currentButton;
        for(int ib=0;ib < GEWButtons.size();ib++) {
            currentButton = GEWButtons.get(ib);
            Color mainColor = currentButton.getMainUserColor();
            if(currentButton.isPressed(mainColor) & (currentButton != ev.getSource()))
                currentButton.clickButton(mainColor);
        }        
    }
    
}
