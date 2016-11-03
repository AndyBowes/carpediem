package hexkid.targetting;

import hexkid.stats.Enemy;
import hexkid.stats.EnemyStatus;
import hexkid.util.Point;

public final class CircularPredictor extends AbstractPredictor {

    public CircularPredictor(PredicatorProperties properties) {
        super(properties);
    }

    @Override
    public Point estimatePosition(Point myPos, Enemy enemy, double estimatedTimeOfImpact, double bulletVelocity) {
        EnemyStatus lastSighting = enemy.sightings.getFirst();
        double deltaHeading = 0;
        if (enemy.sightings.size()>1 && enemy.sightings.get(1).turn == lastSighting.turn -1){
            deltaHeading = lastSighting.heading - enemy.sightings.get(1).heading;
        }

        double heading = lastSighting.heading;
        double estimatedX = lastSighting.pos.x;
        double estimatedY = lastSighting.pos.y;
        int t = 0;
        // Assumes constant velocity & rate of heading change
        while (t*bulletVelocity < myPos.distance(estimatedX, estimatedY)){
            t++;
            estimatedX += lastSighting.velocity * Math.sin(heading);
            estimatedY += lastSighting.velocity * Math.cos(heading);
            heading += deltaHeading;
        }
        return getNormalisedPoint(estimatedX, estimatedY);
    }

}
