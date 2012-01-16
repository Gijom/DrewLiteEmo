/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author chanel
 */
public class PointUtil {
    
    public static enum Orientation {NORTH_WEST, NORTH_EAST, SOUTH_WEST, SOUTH_EAST};
    
    static Point2D add(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }
    
    static Point2D add(Point2D p1, double value) {
        return new Point2D.Double(p1.getX() + value, p1.getY() + value);
    }
    
    static Point2D addX(Point2D p1, double value) {
        return new Point2D.Double(p1.getX() + value, p1.getY());
    }

    static Point2D addY(Point2D p1, double value) {
        return new Point2D.Double(p1.getX(), p1.getY() + value);
    }    
    
    static Point2D add(Point2D p1, Dimension dim) {
        return new Point2D.Double(p1.getX() + dim.width, p1.getY() + dim.height);
    }


    static Point2D substract(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() - p2.getX(), p1.getY() - p2.getY());
    }

    static Point2D divide(Point2D p, double value) {
        return new Point2D.Double(p.getX() / value, p.getY() / value);
    }

    static Point2D multiply(Point2D p, double value) {
        return new Point2D.Double(p.getX() * value, p.getY() * value);
    }
    
    static Point2D normalize(Point2D p1) {
        return divide(p1, p1.distance(new Point2D.Double(0,0)));
    }
    
    static Point toPoint(Point2D p) {
        return new Point((int) p.getX(),(int) p.getY());
    }
    
    //Return the orientation according to the UI reference space (Y pointing down)
    static Orientation getOrientationUI(Point2D p) {
        
        if(p.getX() > 0)
            if(p.getY() > 0)
                return Orientation.SOUTH_EAST;
            else
                return Orientation.NORTH_EAST;
        else
            if(p.getY() > 0)
                return Orientation.SOUTH_WEST;
            else
                return Orientation.NORTH_WEST;
    }
}
