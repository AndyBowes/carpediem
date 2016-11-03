package hexkid.util;

import static java.lang.Math.PI;

public class Physics {

    public static double normalizeBearing(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public static double normalizeBearingRadians(double angle) {
        while (angle > PI ) angle -= 2 * PI;
        while (angle < -PI) angle += 2 * PI;
        return angle;
    }

}
