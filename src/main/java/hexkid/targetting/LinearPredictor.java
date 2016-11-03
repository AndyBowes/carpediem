package hexkid.targetting;

import hexkid.stats.Enemy;
import hexkid.stats.EnemyStatus;
import hexkid.util.Point;

public class LinearPredictor extends AbstractPredictor {
    public LinearPredictor(PredicatorProperties properties) {
        super(properties);
    }

    @Override
    public Point estimatePosition(Point myPos, Enemy enemy, double estimatedTimeOfImpact, double bulletVelocity) {
        EnemyStatus lastSighting = enemy.sightings.getFirst();
        double estimatedX = lastSighting.pos.x;
        double estimatedY = lastSighting.pos.y;
        double xStep = lastSighting.velocity * Math.sin(lastSighting.heading);
        double yStep = lastSighting.velocity * Math.cos(lastSighting.heading);
        int t = 0;

        while (t*bulletVelocity < myPos.distance(estimatedX,estimatedY)){
            estimatedX += xStep;
            estimatedY += yStep;
            t += 1;
        }
        return getNormalisedPoint(estimatedX, estimatedY);
    }
}
