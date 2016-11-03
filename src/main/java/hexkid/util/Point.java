package hexkid.util;

import java.awt.geom.Point2D;

import static java.lang.Math.PI;

public class Point extends Point2D.Double{

    public Point(double x, double y) {
        this.setLocation(x,y);
    }

    public double bearingTo(Point other){
        return absoluteBearing(other.x, other.y);
    }

    public double absoluteBearing(double x2, double y2) {
        double xo = x2-x;
        double yo = y2-y;
        double hyp = distance(x2, y2);
        double arcSin = Math.asin(xo / hyp);
        double bearing = 0;

        if (xo > 0 && yo > 0) { // both pos: lower-Left
            bearing = arcSin;
        } else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
            bearing = 2 * PI + arcSin; // arcsin is negative here, actuall 360 - ang
        } else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
            bearing = PI - arcSin;
        } else if (xo < 0 && yo < 0) { // both neg: upper-right
            bearing = PI - arcSin; // arcsin is negative here, actually 180 + ang
        }

        return bearing;
    }

    public Point translateDegrees(double heading, double distance){
        return translateRadians(Math.toRadians(heading),distance);
    }

    public Point translateRadians(double heading, double distance) {
        return new Point(x + Math.sin(heading) * distance, y + Math.cos(heading) * distance);
    }
}
