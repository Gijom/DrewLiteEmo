/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import Drew.Client.Util.Communication;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Util.XMLmp.XMLTree;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
        
/**
 *
 * @author Guillaume Chanel
 */
public class GEW extends DefaultCooperativeModule implements ActionListener, MouseListener, ComponentListener {

    //UI objects definition
    ArrayList<GEWLine> GEW;
    int nbEmotions = 16;
    int nbPointsPerScale = 5;
    Button test;
    
    
    public GEW() {
        super();
        //Called first
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
        
        
    }

    //Create all the UI components + action listeners
    @Override
    public void init() {
        super.init();
        
        //Leason to you own events (especially resize)
        addComponentListener(this);        
        
        //Set the sizes of the panel
        Dimension scrDim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setMinimumSize(new Dimension(scrDim.width/2,scrDim.height/2));
        this.setMaximumSize(new Dimension(scrDim.width/2,scrDim.height/2));
        
        //No layout to position the wheel manually
        setLayout(null);
        
        //Create the Lines with the appropriate number of button per line
        GEW = new ArrayList<GEWLine>();
        for(int i=0;i < nbEmotions; i++)
            GEW.add(new GEWLine(nbPointsPerScale)); //Each line contains a certain number of buttons / points
        
        //Update the wheel
        updateWheel();
    }
    
    private void updateWheel()
    {
        //Determine several sizes with respect to the size of the Panel
        Dimension panelSize = getPreferredSize();
        System.out.println(panelSize.toString());
        panelSize.setSize(new Dimension(panelSize.width,panelSize.height)); //Quick and dirty to deal with panel borders
        double panelRadius = Math.min(panelSize.width, panelSize.height) / 2; //Biggest drawable circle in the panel
        double centralRadius = panelRadius / 5; //Central hole to put the "no emotion" button
        Point2D panelCenter = new Point2D.Double( ((double) panelSize.width) / 2, ((double) panelSize.height) / 2); //Point at the center of the panel
        int maxButtonRadius = (int) Math.min(panelRadius*Math.PI / (2*nbEmotions),30); //The radius of the biggest button is 30 except if it is to big (two emotions buttons overlap)

        
        //Position each button according to the number of emotions and their size + add them to the Panel
        //This is done line by line, which all have a different angle
        double angleStep = 2*Math.PI / GEW.size();
        double angle = angleStep / 2;
        for(int il=0;il < GEW.size(); il++)
        {
            //Get the currentLine
            GEWLine currentLine = GEW.get(il);
            
            //Set the buttons to the correct size
            currentLine.setButtonsRadiusRange(10, maxButtonRadius);
            
            //Each line is composed of a starting Point (small button) and endPoint (big Button)
            Point2D endPoint = new Point2D.Double((Math.cos(angle) * panelRadius),(Math.sin(angle) * panelRadius));
            endPoint = PointUtil.add(endPoint, panelCenter);
            Point2D startPoint = new Point2D.Double((Math.cos(angle) * centralRadius),(Math.sin(angle) * centralRadius));
            startPoint = PointUtil.add(startPoint, panelCenter);
            currentLine.setLinePosition(startPoint, endPoint);            
            
            //Add all the buttons in the line to the Panel
            currentLine.addToComponent(this);
            
            //Increase the angle for a new line
            angle += angleStep;
        }        
    }

    @Override
    public String getCode() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void moduleMessageDeliver(String user, XMLTree data) {
        // TODO Auto-generated method stub

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
        System.out.println("Action Performed !");
    }
    
    @Override
    public void mouseClicked(MouseEvent ev) {
        System.out.println("Button clicked !");        
    }
    
    @Override
    public void mouseExited(MouseEvent ev) {
    }
    
    @Override
    public void mouseEntered(MouseEvent ev) {
        System.out.println("mouse entered");
    }

    @Override
    public void mouseReleased(MouseEvent ev) {
    }

    @Override
    public void mousePressed(MouseEvent ev) {
    }    
}
