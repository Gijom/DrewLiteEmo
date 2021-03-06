package Drew.Client.ImageViewer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;

import Drew.Client.Util.*;
import Drew.Util.XMLmp.*;
public class ImageViewer extends Panel implements CooperativeModule
{
    static final String CODE = "imageviewer";
    public Communication central_applet;

    SpeedControl speedControl;
    ImagePanel imagePanel;
    //    JLabel nameLabel;
    Vector images;
    Vector imageNames;
    
    public void init() {
	images = loadImages();
        imagePanel = new ImagePanel(images,imageNames,getCode());
        speedControl = new SpeedControl(imagePanel,central_applet);
	//nameLabel = new JLabel("");

	setLayout(new BorderLayout());
	add(speedControl.getAnimationPanel(),BorderLayout.NORTH);
	add(imagePanel,BorderLayout.CENTER);
	//	add(nameLabel,BorderLayout.SOUTH);
	
	// 	System.out.println("maxw="+imagePanel.biggestWidth()+" maxh="+imagePanel.biggestHeight());
	// 	setSize(imagePanel.biggestWidth(),imagePanel.biggestHeight());
    }

    public Dimension getPreferredSize()
    {
// 	if(imagePanel !=null) {
// 	    if( imagePanel.getImageWidth() != 0 && imagePanel.getImageHeight() != 0 ) {
// 		System.out.println("preferred size :"+imagePanel.getImageWidth()+","+imagePanel.getImageWidth());
// 		return new Dimension(imagePanel.getImageWidth(),imagePanel.getImageWidth());
// 	    }
// 	}
	return new Dimension(500,500);
    }

    public void fitToSize(int a , int b) {
	System.out.println("resizing to:"+a+"x"+b);
	this.setSize(a,b);
    }
 

    public ImageViewer()
    {
    }

    public ImageViewer(Communication cdc)
    {
	this();
	constructor(cdc);
    }

    public void constructor(Communication cdc) {
	central_applet=cdc;

	System.err.println( "ImageViewer tool created (" + this.hashCode() + ")" );
    }

    // loads images from directory and build a vector of vectors (pairs actually:(imageName,BufferedImage) )
    private Vector loadImages()
    {
	images = new Vector();
	imageNames = new Vector();

	// set image directory (current directory + /images
	String dirName = System.getProperty("user.dir")+"/images";
	System.out.println("dir name:"+dirName);
	File dir = new File(dirName);
	
	// get image directory contents
	String []dirList = dir.list();
	System.out.println("dir length:"+dirList.length);
	for( int k=0; k<dirList.length; k++) {
	    System.out.println("image found:"+dirList[k]);
	}

	// load all images in directory and build vectors
        //BufferedImage[] images = new BufferedImage[dirList.length];
        // load all image names
	for( int j=0 ; j< dirList.length; j++ ) {
	    imageNames.add(new String(dirList[j]));
	}

	// sort image name alphabetically
	Collections.sort(imageNames);

	// load image file to BufferdImage vector
	for(int j = 0; j < imageNames.size(); j++)
            try
            {
                URL url = (new File(dirName+"/"+imageNames.elementAt(j))).toURL();
		Image im = ImageIO.read(url);
                images.add(im);
		//images[j] = ImageIO.read(url);
		//boolean test ;
		//while ( !(test = Toolkit.getDefaultToolkit().prepareImage(images[j] , -1 , -1 , null)) ) {}
                 
            }
            catch(MalformedURLException mue)
            {
                System.err.println("url: " + mue.getMessage());
            }
            catch(IOException ioe)
            {
                System.err.println("read: " + ioe.getMessage());
            }

        return images;
    }
 
    public static void main(String[] args)
    {
        new ImageViewer();
    }

    public String getTitle() {
	return "Image Viewer";
    }

    public String getCode() 
    {
	return CODE;
    }
    public void start()
    {
    }

    public void stop()
    {
    }

    public void destroy() 
    {
	stop();
    }
  
    public void clear() {
    }

    public boolean messageFilter(XMLTree event) {
	return true;
    }

    public void messageDeliver(String user, XMLTree data) {
	if( getCode().equals( data.tag() ) ) {
	    System.err.println("msg recieved");
	    Object m;
	    for (Enumeration e = data.elements() ; e.hasMoreElements() ;) {
		m = e.nextElement();
		if( !( m instanceof XMLTree ) ) continue;
		XMLTree t = (XMLTree)m;
		String action = t.getAttributeValue("action");
		String imageName = t.getText();
		System.err.println("action: "+action+"\n"+
				   "text:   "+t.getText()+"\n"+
				   "current:"+imagePanel.getCurrentImageName());
		// if we recieve a 'switch pictures' message and name of picture is not the picture already loaded
		// the 2nd test is to avoid switching several pictures as each client signals it's change all at the same time after the default delay
		
		//speedControl.setNameLabel(imageName);

		if( action.equals("switching pictures")) {
		    System.err.println("I should change to picture:"+imageName);
		    speedControl.animator.interrupt();
		    imagePanel.advance(1);
		} else if( action.equals("loadNextImage")) {
		    System.err.println("I should load next picture:"+imageName);
		    speedControl.animator.interrupt();
		    imagePanel.advance(1);
		} else if( action.equals("loadPreviousImage")) {
		    System.err.println("I should load previous picture:"+imageName);
		    speedControl.animator.interrupt();
		    imagePanel.advance(-1);		    
		}
		
	    }

	    

	    //String action = data.getAttributeValue("imageViewerAction");
	    //System.err.println("text:"+data.getText()+" action:"+action);
	    //XMLTree xmlt = data.getText();
	    //System.err.println("attribut action:"+xmlt.getAttributeValue("action"));
	}
    }


}
 
class ImagePanel extends JPanel
{
    Vector images;
    Vector imageNames;
    int index;
    int maxW = 0;
    int maxH = 0;
    String code;

    public ImagePanel(Vector images,Vector imageNames,String code)
    {
        this.images = images;
	this.imageNames = imageNames;
	this.code = code;
        index = 0;
	//setMaxSizeValues();
    }
  
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        int w = getWidth();
        int h = getHeight();
	BufferedImage image = (BufferedImage)images.elementAt(index);
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int x = (w - imageWidth)/2;
        int y = (h - imageHeight)/2;
	g.drawImage(image, x, y, this);
	
     }
 
    public void advance(int i)
    {
        index = (index + i) % images.size();
	if(index <0) index+=images.size();
        System.err.println("new index value:"+index);
	//iv.fitToSize(images[index].getWidth(),images[index].getHeight());
	repaint();
    }

    public String getCurrentImageName() {
	return (String)imageNames.elementAt(index);
    }

    public String getNextImageName() {
	int i = (index+1) % images.size();
	return (String)imageNames.elementAt(i);
    }  
    public String getPreviousImageName() {
	int i = index - 1;
	if(i <0) i+=images.size();
	return (String)imageNames.elementAt(i);
    }

    public String getCode() {
	return code;
    }

//     void setMaxSizeValues() {
// 	for( int i=0 ; i<images.length ; i++ ) {
// 	    if( images[i].getWidth() > maxW ) {
// 		maxW = images[i].getWidth();
// 	    }
// 	    if( images[i].getHeight() > maxH ) {
// 		maxH = images[i].getHeight();
// 	    }
// 	}
//     }
    
//     public int getImageWidth() {
// 	return images[index].getWidth();
//     }

//     public int getImageHeight() {
// 	return images[index].getHeight();
//     }

    public int biggestWidth() {
	return maxW;
    }

    public int biggestHeight() {
	return maxH;
    }

}
 
class SpeedControl
{
    ImagePanel imagePanel;
    Thread animator;
    boolean keepAdvancing;
    Communication c;	
    //JLabel nameLabel;

    // default delay : 5 minutes
    int delay = 1000 * 60 * 5;
    int s = 0; // sleep number
    int t = 0; // thread number
 
    public SpeedControl(ImagePanel imPanel)
    {
        imagePanel = imPanel;
	start();
    }  
    
    public SpeedControl(ImagePanel imPanel,Communication cdc)
    {
	c = cdc;
        imagePanel = imPanel;
        //nextImage(); // always started with picture number 2 (index 1) :(
	start();
    }
 
    public void setNameLabel(String s) {
	//nameLabel.setText(s);
    }

    private Runnable runner = new Runnable()
    {
        public void run()
        {
	    boolean endOfDelay;
	    t++;
	    s=0;
            while(keepAdvancing)
            {
		s++;
		endOfDelay = false;
                try
                {
		    System.err.println("thread "+t+" sleep "+s+" sleeping :"+delay+" milliseconds");
                    Thread.sleep(delay);
		    endOfDelay = true;
                }
                catch(InterruptedException ie)
                {
                    System.err.println("thread "+t+" sleep "+s+" interrupt: " + ie.getMessage());
		}
		if( endOfDelay ) {
		    //imagePanel.advance(1);
		    c.envoiserveur( 
				   new XMLTree( imagePanel.getCode() , 
						new XMLTree( "imageViewerAction", 
							     XMLTree.Attributes("action","switching pictures"),
							     XMLTree.Contents( imagePanel.getNextImageName() )
							     )
						)
				   );
		}
	    }
	}
    };

    public void start() {
	if(animator == null) {
	    System.err.println("START t "+t+" s "+s);
            keepAdvancing = true;
            animator = new Thread(runner);
	    animator.start();
	}
    }

    private void stop()
    {
	if( keepAdvancing ) {
	    System.err.println("STOP t "+t+" s "+s);
            keepAdvancing = false;
            animator.interrupt();
            animator = null;
	}
    }

    private void nextImage()
    {
	//stop();
	//imagePanel.advance(1);
	//start();
	System.out.println("next image : "+imagePanel.getNextImageName());
	c.envoiserveur( 
		       new XMLTree( imagePanel.getCode() , 
				    new XMLTree( "imageViewerAction", 
						 XMLTree.Attributes("action","loadNextImage"),
						 XMLTree.Contents( imagePanel.getNextImageName() )
						 )
				    )
		       );
    }
 
    private void previousImage()
    {
	//stop();
	//imagePanel.advance(-1);
	//start();
	System.out.println("previous image : "+imagePanel.getCurrentImageName());
	c.envoiserveur( 
		       new XMLTree( imagePanel.getCode() , 
				    new XMLTree( "imageViewerAction", 
						 XMLTree.Attributes("action","loadPreviousImage"),
						 XMLTree.Contents( imagePanel.getPreviousImageName() )
						 )
				    )
		       );
    }
 
    public void setDelay(int delay)
    {
        this.delay = delay;
    }
 
    public JPanel getAnimationPanel()
    {
        final JButton
            nextImage = new JButton("Next image"),
            previousImage  = new JButton("Previous image");
        ActionListener l = new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                JButton button = (JButton)e.getSource();
                if(button == nextImage)
                    nextImage();
                if(button == previousImage)
                    previousImage();
            } 
        };
        nextImage.addActionListener(l);
        previousImage.addActionListener(l);
	//nameLabel = new JLabel("");
        JPanel panel = new JPanel();
        panel.add(previousImage);
	//panel.add(nameLabel);
        panel.add(nextImage);
        return panel;
    }



}
