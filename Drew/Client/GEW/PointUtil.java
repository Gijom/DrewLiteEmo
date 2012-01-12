/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Drew.Client.GEW;

import java.awt.Point;
import java.awt.geom.Point2D;

/**
 *
 * @author chanel
 */
public class PointUtil {
    static Point2D add(Point2D p1, Point2D p2) {
        return new Point2D.Double(p1.getX() + p2.getX(), p1.getY() + p2.getY());
    }
    
    static Point2D add(Point2D p1, double value) {
        return new Point2D.Double(p1.getX() + value, p1.getY() + value);
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
}
