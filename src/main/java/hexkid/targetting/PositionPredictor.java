package hexkid.targetting;

import hexkid.stats.Enemy;
import hexkid.util.Point;

/**
 * Created by Andy Bowes on 30/10/2016.
 */
public interface PositionPredictor {

    public Point estimatePosition(Point myPos, Enemy enemy, double estimatedTimeOfImpact, double bulletVelocity);
}
