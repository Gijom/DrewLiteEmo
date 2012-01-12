/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 *
 * @author chanel
 */
public class GEWLine {
    
    ArrayList<GEWButton> GEWButtons;  //Smallest button is the first of the list  
    int minButtonRadius;
    int maxButtonRadius;
    
    public GEWLine() {
        super();
    }
    
    public GEWLine(int nbButtons) {

        //Create the new vector of GEWButtons
        GEWButtons = new ArrayList<GEWButton>(nbButtons);
        for(int ib = 0;ib < nbButtons;ib++) {
            GEWButtons.add(new GEWButton());
        }
    
    }
    
    public GEWLine(int nbButtons, int minButtonRadius, int maxButtonRadius) {
        this(nbButtons);
        
        //Set the size of each button
        setButtonsRadiusRange(minButtonRadius, maxButtonRadius);
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
    }
    
    public void addToComponent(Panel c)
    {
        for(int ib = 0; ib < GEWButtons.size(); ib++)
            c.add(GEWButtons.get(ib));
    }
    
    public ArrayList<GEWButton> getButtons() {
        return GEWButtons;
    }
    
    
}
