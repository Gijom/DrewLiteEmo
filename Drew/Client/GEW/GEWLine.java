/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 *
 * @author chanel
 */
public class GEWLine implements MouseListener {
    
    ArrayList<GEWButton> GEWButtons;    //Smallest button is the first of the list  
    JLabel emotion;                      //The name of the emotion corresponding to this line
    int minButtonRadius;                //The maximum size of a button in the wheel
    int maxButtonRadius;                //The minimum size of a button in the wheel
    Color buttonsColor;
    public GEWLine() {
        super();
    }
    
    public GEWLine(int nbButtons, String emotion) {
        buttonsColor = null;
        createUI(nbButtons, emotion);
    }
    
    public GEWLine(int nbButtons, String emotion, int minButtonRadius, int maxButtonRadius) {
        this(nbButtons, emotion);
        
        //Set the size of each button
        setButtonsRadiusRange(minButtonRadius, maxButtonRadius);
    }
    
    public GEWLine(int nbButtons, String emotion, Color color, int minButtonRadius, int maxButtonRadius) {
        this(nbButtons, emotion, minButtonRadius, maxButtonRadius);
        
        buttonsColor = color;
        createUI(nbButtons, emotion);
    }
    
  
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
            newButton.addMouseListener(this);
            GEWButtons.add(newButton);            
        }
        
        //Create and resize the label
        this.emotion = new JLabel(emotion);
        //this.emotion.setBorder(BorderFactory.createLineBorder(Color.black)); //for labels use setinsets ?
        this.emotion.setSize(this.emotion.getPreferredSize());        
    }

    public int getScaleValue() {
        int ib;
        
        for(ib=0;ib < GEWButtons.size();ib++)
            if(GEWButtons.get(ib).isPressed())
                break;
        
        if(ib >= GEWButtons.size()) //No button clicked
            return 0;
        else
            return ib+1;        
    }

    
    public void setButtonsColor(Color color)
    {
        buttonsColor = color;
        for(int ib = 0;ib < GEWButtons.size();ib++)
            GEWButtons.get(ib).setColor(color);
    }
    
    public void setButtonsRadiusRange(int minButtonRadius, int maxButtonRadius)
    {
        if(minButtonRadius > maxButtonRadius)
            minButtonRadius = maxButtonRadius;
        
        this.minButtonRadius = minButtonRadius;
        this.maxButtonRadius = maxButtonRadius;
        
        setButtonsRadius();
    }            
    
    private void setButtonsRadius() {
        
        int nbButtons = GEWButtons.size();
        double step = ((double) (maxButtonRadius - minButtonRadius)) / (nbButtons - 1);
        double buttonRadius = minButtonRadius;
        for(int ib = 0; ib < GEWButtons.size(); ib++) {            
            GEWButtons.get(ib).setRadius(buttonRadius);
            buttonRadius += step;
        }
    }
    
    /*
     * The first point give the adge of the smallest button, the second
     * point the edge of the biggest button
     */
    public void setLinePosition(Point2D p1, Point2D p2) {
        double lineLength = p1.distance(p2);
        Point2D vector = PointUtil.divide(PointUtil.substract(p2, p1), lineLength);
//        System.out.println("P1: " + p1.toString());
//        System.out.println("P2: " + p2.toString());
//        System.out.println("Vector: " + vector.toString());
//        System.out.println("---------------");
        
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
    
    public void addToComponent(GEW c) //c'est pas joli joli
    {
        for(int ib = 0; ib < GEWButtons.size(); ib++) {
            GEWButton currButton = GEWButtons.get(ib);
            c.add(currButton);
            currButton.addMouseListener(c);
        }
        c.add(emotion);
        
        
    }
    
    public ArrayList<GEWButton> getButtons() {
        return GEWButtons;
    }
    
    public JLabel getEmotionLabel() {
        return emotion;
    }
    
    public void setEmotionLabel(String label) {
        emotion.setText(label);
    }
        
    public boolean isNameEqual(String lineName) {
        return lineName.equalsIgnoreCase(this.emotion.getText());
    }
    
    
    @Override
    public void mouseClicked(MouseEvent ev) {
 /*       //check that Only one button is pressed by line, choose the last button pressed
        GEWButton currentButton;
        for(int ib=0;ib < GEWButtons.size();ib++) {
            currentButton = GEWButtons.get(ib);
            if(currentButton.isPressed() & (currentButton != ev.getComponent()))
                currentButton.clickButton();
        }
*/
    }
    
    @Override
    public void mouseExited(MouseEvent ev) {
    }
    
    @Override
    public void mouseEntered(MouseEvent ev) {
        //System.out.println("mouse entered");
    }

    @Override
    public void mouseReleased(MouseEvent ev) {
    }

    @Override
    public void mousePressed(MouseEvent ev) {
    } 
    
    
}
