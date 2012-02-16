/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Iterator;

/**
 * GEWButton - a class that produces a lightweight round GEW button.
 *
 * This buttons work in two ways: either standalone more or less as classical buttons or with several users
 * Users are all defined by a color so that a color = a user. For this reason users are called coloredUsers
 * The button has three states: 
 *  - unpressed: with a specific color (button "at rest")
 *  - inpress: being pressed by the local user 
 *  - pressed: the button is pressed by at least one coloredUser
 * 
 * When a button is pressed is changes its color depending on the coloredUsers that have pressed it.
 * In the following a main users is considered to be the one using the mouse: the button will automatically
 * associate a specific color when it is pressed by the local user (i.e. the mouse user). This color is named
 * mainColor or colorMainUser. Other users can also use the button with the interface especially the
 * clickButton method.
 *
 * When the main user click the button, an action ActionEvent.ACTION_PERFORMED is raised.
 * 
 * When the button is pressed by several coloredUsers their colors are combined to form the button color.
 * 
 */
public class GEWButton extends Component {

  public static final Color DEFAULT_COLOR = Color.LIGHT_GRAY;    
  public static final int BORDER_TICKNESS = 2;
  public static enum ButtonState {PRESSED, UNPRESSED, INPRESS};
  public static String ID = "GEWButton";
  
  private ActionListener actionListener;        // Post action events to listeners
  private String label;                         // The Button's text
  private boolean inpress;                      // The button is pressed by the main user but is not released yet
  private Color colorUnpressed;                 // color of the button when not pressed
  private Color colorMainUser;                  // color of the button when pressed by the main user (mouse click)
  private HashSet<Color> coloredUsersPressed;   // contains the colors of the users who pressed the button (including main user)
  private BufferedImage offscreen = null;       //Image used for the double-buffering display of the button
  private GEWLine parentLine = null;            // A pointer to the parent line if it exists
  
  /**
   * Constructs a RoundButton with no label.
   */
  public GEWButton() {
      this(null, DEFAULT_COLOR);
  }

  /**
   * Constructs a GEWButton with the specified label.
   * @param label the label of the button
   */
  public GEWButton(String label) {
      this(label, DEFAULT_COLOR);
  }
  /**
   * Construct a  GEWButton with a label and a specific color
   * @param label the label of the button
   * @param color the color of the button when unpressed, the mainUser color is automatically set to color.darker()
   */
  public GEWButton(String label, Color color) {
        this.coloredUsersPressed = new HashSet<Color>();
        this.inpress = false;
        this.label = label;
        this.colorUnpressed = color;
        this.colorMainUser = color.darker();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
  }
  /**
   * Construct a GEWButton with a specific color
   * @param color the color of the button when unpressed, the mainUser color is automatically set to color.darker()
   */
  public GEWButton(Color color) {
        this(null, color);
  }

  /**
   * This function associate the button to its GEWLine if needed
   * @param parentLine the parent line containing the button
   */
  public void setParentLine(GEWLine parentLine) {
        this.parentLine = parentLine;
  }

  /**
   * This function return the GEWLine to which the button belongs
   * @return GEWLine the parent line containing the button
   */
  public GEWLine getParentLine() {
        return parentLine;
  }

  /**
   * Returns the state of the button (UNPRESSED, PRESSED (by at leat one user), INPRESS)
   * This function is different from isPressed which return a boolean
   * @return ButtonState (UNPRESSED, PRESSED, INPRESS)
   */
  private ButtonState getState() {
        if(!inpress) {
            if(coloredUsersPressed.isEmpty())
                return ButtonState.UNPRESSED;
            else
                return ButtonState.PRESSED;
        }
        else
            return ButtonState.INPRESS;
  }
  
  /**
   * Returns if the button is pressed by at least one user
   * @return boolean (true: the button is pressed, false: not pressed)
   */
  public boolean isPressed() {
        if(getState() == ButtonState.PRESSED)
            return true;
        else
            return false;
  }

  /**
   * Returns if the button is pressed by the specified coloredUser
   * @param ColoredUser is the button pressed by ColoredUser ?
   * @return boolean (true: the button is pressed by ColoredUser, false: the button is not pressed by Colored User)
   */
  public boolean isPressed(Color ColoredUser) {
        if(coloredUsersPressed.contains(ColoredUser))
            return true;
        else
            return false;
  }  
  
  /**
   * Simulate a click on the button by the user coloredUser. The call to this function does
   * not trigger any ActionEvent
   * @param coloredUser 
   */
  public void clickButton(Color coloredUser) { //TODO
        if(coloredUsersPressed.contains(coloredUser))
            coloredUsersPressed.remove(coloredUser);
        else
            coloredUsersPressed.add(coloredUser);
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

  /**
   * Return the color in which the button is currently painted
   * @return Color, the color in which the button is currently painted
   */
  public Color getColor() {
      
      switch(getState()) {
          case INPRESS:
              return combineColors().darker();
          case PRESSED:
              return combineColors(); //TODO
          case UNPRESSED:
              return colorUnpressed;
      }
      return null;
  }
 
  /**
   * gets the mainUser color (i.e. the color of mouse user)
   * @return Color, the mainUser color
   */
  public Color getMainUserColor() {
     return colorMainUser;
  }

  public Color getUnpressedColor() {
     return colorUnpressed;
  }
  
  /**
   * Get the list of all users that have pressed the button
   * @return a HasSet of ColoredUsers that have pressed the button
   */
  public HashSet<Color> getColoredUsers() {
     return coloredUsersPressed;
  }
  
  /**
   * Set the button colors at rest (unpressed) and when pressed by the main User
   * @param color color when unpressed
   * @param userColor color of the main user
   */
  public void setColor(Color color, Color userColor) {
      this.colorUnpressed = color;
      this.colorMainUser = userColor;
      invalidate();
      repaint();
  }
  
  public void setMainUserColor(Color color) {
      colorMainUser = color;
  }

  public void setUnpressedColor(Color color) {
      colorUnpressed = color;
  }
  
  /**
   * Combine the colors of the different users to form a unique color that will
   * be displayed as the button color
   * @return the combined color or the unpressed color if no users has pressed the button
   */
  private Color combineColors() {
      
      Iterator<Color> it = coloredUsersPressed.iterator();
      int nbColors = coloredUsersPressed.size(); //number of coloredUsers
      
      //If there is at least one user combine the colors
      if (nbColors > 0) {
        Color currentColor;
        int r = 0, g = 0, b = 0; //RGB components
        while (it.hasNext())
        {
                //Add the color together
                currentColor = it.next();
                r += currentColor.getRed();
                g += currentColor.getGreen();
                b += currentColor.getBlue();
        } 
        //return new Color(r / nbColors, g / nbColors, b / nbColors);      
        return new Color(Math.min(r, 255), Math.min(g, 255), Math.min(b, 255));
      }
      else //No user have pressed the button
        return colorUnpressed;  
  }
          
  /**
   * paints the RoundButton
   */
  
  @Override
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
      Color currentColor = getColor(); //the current color of the button (if pressed or not)   
      
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
  @Override
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
  @Override
  public Dimension getMinimumSize() {
      return new Dimension(0, 0);
  }

  /**
   * Sets the radius of the button (actually change the squared size of the component)
   * @param r the radius
   */
  public void setRadius(double r) {
      int diameter = (int) Math.round(2*r);
      setSize(new Dimension(diameter,diameter));
  }
  
  /**
   * Returns the radius of the button (has computed from the rectangle shape)
   * @return the radius
   */
  public double getRadius() {        
      return Math.min(getSize().width, getSize().height) / 2;      
  }

  /**
   * Move the round button to the given center
   * @param center a point where to move the center of the button
   */  
  public void setCenter(Point center) {
      double r = getRadius();
      Dimension d = getSize();              
      this.setBounds((int) Math.round(center.getX() - r), (int) Math.round(center.getY() - r), d.width, d.height);
  }

  /**
   * returns the center of the round button
   * @return the center of the button
   */
  public Point getCenter() {
      Rectangle rec = getBounds();
      double r = getRadius();
      Point center = new Point((int) Math.round(rec.x + r), (int) Math.round(rec.y + r));
      return center;
  }
  
  
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
   @Override
   public boolean contains(int x, int y) {
       int mx = getSize().width/2;
       int my = getSize().height/2;
       return (((mx-x)*(mx-x) + (my-y)*(my-y)) <= mx*mx);
   }
   
   /**
    * Set the button to the proper state and distribution Action events
    */
   @Override
   public void processMouseEvent(MouseEvent e) {
       Graphics g;
       switch(e.getID()) {
          case MouseEvent.MOUSE_PRESSED:
	    // If the left-mouse button is used remove or add the main user to
            // the list of users that pressed the button
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {                
                if(coloredUsersPressed.contains(colorMainUser)) //main user already clicked, remove it
                {
                    coloredUsersPressed.remove(colorMainUser);
                    inpress = true;
                }
                else
                {
                    coloredUsersPressed.add(colorMainUser); //main user have not clicked, add it to the list of users
                    inpress = true;
                }
                repaint();
            }
	    break;
          case MouseEvent.MOUSE_RELEASED:
            //If the button was pressed than release the Action event
              if(getState() == ButtonState.INPRESS) {                
                //Not anymore in press
                inpress = false;
                repaint();
                
                //Send this action
                if(actionListener != null) {               
                    actionListener.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ID));
                }
            }
	    break;

          case MouseEvent.MOUSE_ENTERED:
	    break;
              
          case MouseEvent.MOUSE_EXITED:
            // If a mouse button is still pressed cancel the event (remove or re-add the main user)
            if ((e.getModifiers() & InputEvent.BUTTON1_MASK) == InputEvent.BUTTON1_MASK) {
                if(getState() == ButtonState.INPRESS) {
                    if(coloredUsersPressed.contains(colorMainUser))
                    {
                        coloredUsersPressed.remove(colorMainUser);
                        inpress = false;
                    }
                    else
                    {
                        coloredUsersPressed.add(colorMainUser);
                        inpress = false;
                    }
                    repaint();
                }
            }
	    break;
       }
       super.processMouseEvent(e);
   }
   
}
