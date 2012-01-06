/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import Drew.Client.Util.Communication;
import Drew.Client.Util.DefaultCooperativeModule;
import Drew.Util.XMLmp.XMLTree;
import java.awt.event.*;
import java.awt.Button;
import java.awt.Panel;
import java.awt.*;
        
/**
 *
 * @author Guillaume Chanel
 */
public class GEW extends DefaultCooperativeModule implements ActionListener, MouseListener {

    //UI objects definition
    GEWButton myButton;
    GEWLine myLine;
    Panel tst;

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
        // TODO tout ce qui se passerait normalement dans un constructeur.
        // la DREWlet est initialis�e par foo = class.newInstance(); foo.constructor(cdc);
    }

    //Create all the UI components + action listeners
    @Override
    public void init() {
        super.init();

        
        setLayout(null);
        
        myButton = new GEWButton("Push me !");
        myButton.addMouseListener(this);
        this.add(myButton);

        
        Insets insets = this.getInsets();
        Dimension size = myButton.getPreferredSize();
        myButton.setBounds(100 + insets.left, 500 + insets.top, size.width, size.height);
        this.setSize(size);
        
        
        myLine = new GEWLine(4, 100, 400);
        this.add(myLine);
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
    public void actionPerformed(ActionEvent ev) {
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
