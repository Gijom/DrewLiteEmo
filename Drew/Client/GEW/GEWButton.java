/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * RoundButton - a class that produces a lightweight button.
 *
 * Lightweight components can have "transparent" areas, meaning that
 * you can see the background of the container behind these areas.
 *
 */
public class GEWButton extends Component {

  public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;    
  public static final int BORDER_TICKNESS = 2;
  
  private ActionListener actionListener;        // Post action events to listeners
  private String label;                         // The Button's text
  private boolean pressed = false;              // true if the button is detented.
  private Color colorUnpressed;                          // color of the button when not pressed
  private Color colorPressed;                   // color of the button when pressed
  private BufferedImage offscreen = null;       //Image used for the double-buffering display of the button
  private GEWLine parentLine = null;
    /**
   * Constructs a RoundButton with no label.
   */
  public GEWButton() {
      this(null, DEFAULT_COLOR);
  }

  /**
   * Constructs a RoundButton with the specified label.
   * @param label the label of the button
   */
  public GEWButton(String label) {
      this(label, DEFAULT_COLOR);
  }

  public GEWButton(String label, Color color) {
        this.label = label;
        this.colorUnpressed = color;
        this.colorPressed = color.darker();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
  }

  public GEWButton(Color color) {
        this(null, color);
  }

  public void setParentLine(GEWLine parentLine) {
        this.parentLine = parentLine;
  }

  public GEWLine getParentLine() {
        return parentLine;
  }

  public boolean isPressed() {
        return pressed;
  }
  
  public void clickButton() {
        pressed = !pressed;
        invalidate();
        repaint();
  }
  
  /**
   * gets the label
   * @see setLabel
   */
  public String getLabel() {
      return label;
  }
  
  /**
   * sets the label
   * @see getLabel
   */
  public void setLabel(String label) {
      this.label = label;
      invalidate();
      repaint();
  }

 
  public Color getColor() {
      if(pressed)
        return colorPressed;
      else
        return colorUnpressed;
  }
 
  
  public Color getColorPressed() {
     return colorPressed;
  }

  public Color getColorUnpressed() {
      return colorUnpressed;
  }
 
  
  public void setColor(Color color) {
      this.colorUnpressed = color;
      this.colorPressed = color.darker();
      invalidate();
      repaint();
  }
          
  public void setColorPressed(Color color) {
      this.colorPressed = color;
      if(pressed) {
        invalidate();
        repaint();
      }
  }
  
  public void setColorUnpressed(Color color) {
      this.colorUnpressed = color;
      if(!pressed) {          
        invalidate();
        repaint();
      }
  }
    
  /**
   * paints the RoundButton
   */
  public void paint(Graphics g) {


      //Determine the Size of the round Button
      int s = Math.min(getSize().width, getSize().height);    

      //In the end it seems it is needed cause button size might have changed
      GraphicsConfiguration gc = getGraphicsConfiguration();
      offscreen = gc.createCompatibleImage(s, s, Transparency.BITMASK);
      
      
      //Get the 2D graphic drawing
      Graphics2D g2D = (Graphics2D) offscreen.getGraphics();
      g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      g2D.setStroke(new BasicStroke(BORDER_TICKNESS));  //Pen width
              
      //Redefine the size to account for the tickness of the border
      s = s - BORDER_TICKNESS + 1;
      
      //Create the button arc / circle
      Arc2D arc = new Arc2D.Double(0, 0, s, s, 0, 360, Arc2D.CHORD);
      
      //Make a darker button if pressed a normal color button otherwise
      Color currentColor; //the current color of the button (if pressed or not)      
      if(pressed) {
          currentColor = colorPressed;
      } else {
	  currentColor = colorUnpressed;
      }      
      
      // paint the interior of the button
      g2D.setColor(currentColor);
      g2D.fill(arc);
      
      // Paint the perimeter of the button with a gradient for 3D effect
      GradientPaint brighttodark = new GradientPaint(0, 0, currentColor.brighter(), s, s, currentColor.darker());
      g2D.setPaint(brighttodark);
      g2D.draw(arc);
      
      // draw the label centered in the button      
      if(label != null) {
          Font f = getFont();
          if (f!= null)
          {
            FontMetrics fm = getFontMetrics(getFont());
            g2D.setColor(Color.BLACK);
            g2D.drawString(label, s/2 - fm.stringWidth(label)/2, s/2 + fm.getMaxDescent());
          }
      }
      
      //Show the whole image (double buffering)
      g.drawImage(offscreen,0,0,this);
      g2D.dispose();
  }
  
  /**
   * The preferred size of the button. 
   */
  public Dimension getPreferredSize() {
      Font f = getFont();
      if(f != null) {
	  FontMetrics fm = getFontMetrics(getFont());
	  int max = Math.max(fm.stringWidth(label) + 40, fm.getHeight() + 40);
	  return new Dimension(max, max);
      } else {
	  return new Dimension(100, 100);
      }
  }
  
  /**
   * The minimum size of the button. 
   */
  public Dimension getMinimumSize() {
      return new Dimension(0, 0);
  }

  public void setRadius(double r) {
      int diameter = (int) Math.round(2*r);
      setSize(new Dimension(diameter,diameter));
  }
  
  public double getRadius() {        
      return Math.min(getSize().width, getSize().height) / 2;      
  }
  
  public void setCenter(Point center) {
      double r = getRadius();
      Dimension d = getSize();              
      this.setBounds((int) Math.round(center.getX() - r), (int) Math.round(center.getY() - r), d.width, d.height);
      //!!!!!!!!!!!!!!!!!!!!!!!!! are you sure you should do -r ?
  }
  
  public Point getCenter() {
      Rectangle rec = getBounds();
      double r = getRadius();
      Point center = new Point((int) Math.round(rec.x + r), (int) Math.round(rec.y + r));
      return center;
  }
  
  
  //myButton.setBounds(100 + insets.left, 500 + insets.top, size.width, size.height);
  
  /**
   * Adds the specified action listener to receive action events
   * from this button.
   * @param listener the action listener
   */
   public void addActionListener(ActionListener listener) {
       actionListener = AWTEventMulticaster.add(actionListener, listener);
       enableEvents(AWTEvent.MOUSE_EVENT_MASK);
   }
 
   /**
    * Removes the specified action listener so it no longer receives
    * action events from this button.
    * @param listener the action listener
    */
   public void removeActionListener(ActionListener listener) {
       actionListener = AWTEventMulticaster.remove(actionListener, listener);
   }

  /**
   * Determine if click was inside round button.
   */
   public boolean contains(int x, int y) {
       int mx = getSize().width/2;
       int my = getSize().height/2;
       return (((mx-x)*(mx-x) + (my-y)*(my-y)) <= mx*mx);
   }
   
   /**
    * Paints the button and distribute an action event to all listeners.
    */
   public void processMouseEvent(MouseEvent e) {
       Graphics g;
       switch(e.getID()) {
          case MouseEvent.MOUSE_PRESSED:
	    // render myself inverted only if the left clic is used
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                pressed = !pressed;
                repaint();
            }
	    break;
          case MouseEvent.MOUSE_RELEASED:
	    if(actionListener != null) {
	       actionListener.actionPerformed(new ActionEvent(
		   this, ActionEvent.ACTION_PERFORMED, label));
	    }
	    break;
          case MouseEvent.MOUSE_ENTERED:

	    break;
          case MouseEvent.MOUSE_EXITED:
            // If a mouse button is still pressed cancel the event
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                pressed = !pressed;
                repaint();
            }
	    break;
       }
       super.processMouseEvent(e);
   }
   
}
