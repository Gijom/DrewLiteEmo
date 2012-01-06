/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Panel;
import java.util.ArrayList;

/**
 *
 * @author chanel
 */
public class GEWLine extends Panel {
    
    ArrayList<GEWButton> GEWButtons;    
    
    public GEWLine() {
        super();
    }
    
    public GEWLine(int nbButtons, float sizeMin, float sizeMax) {
        super();       
            
        //Create the new vector of GEWButtons
        GEWButtons = new ArrayList<GEWButton>();
        for(int ib = 0;ib < nbButtons;ib++) {
            GEWButton newButton = new GEWButton();
            int buttonSize = (int) Math.floor(sizeMin + ib*(sizeMax-sizeMin)/(nbButtons-1));
            newButton.setSize(buttonSize, buttonSize);
            GEWButtons.add(newButton);
        }
    }
}
