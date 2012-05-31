/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.EmotionAwareness;

/**
 *
 * @author chanel
 */
import javax.swing.*;
import java .awt.event.ComponentListener;
import java .awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;


class ResizableTextField extends JTextField implements ComponentListener {  
    float iw, ih, sw, sh, weight;  
    boolean first;  
    public ResizableTextField(){  
        constructor();
    }  
    
    public ResizableTextField(int i){
        super(i);
        constructor();
    }
    
    public void constructor() {
        first = true;  
        weight = 1f;  
        addComponentListener(this);  
    }
    
    @Override
    public void componentResized(ComponentEvent e) {  
        if (first) {  
            iw = getWidth();  
            ih = getHeight();  
            first = false;  
        }  

        sw = (getWidth()/iw) * weight;  
        if(sw == 0f)  
              sw = 1f;  
        sh = (getHeight()/ih) * weight;  
        if(sh == 0f)  
              sh = 1f;  
        AffineTransform trans = AffineTransform.getScaleInstance(sw,sh);  
        setFont(getFont().deriveFont(trans));  
    }  
    @Override
    public void componentShown(ComponentEvent e) {}  
    @Override
    public void componentHidden(ComponentEvent e) {}  
    @Override
    public void componentMoved(ComponentEvent e) {}  
}  
