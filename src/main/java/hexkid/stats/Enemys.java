package hexkid.stats;

import hexkid.util.Point;
import robocode.ScannedRobotEvent;

import java.util.HashMap;

/**
 * Created by Andy Bowes on 27/10/2016.
 */
public class Enemys extends HashMap<String, Enemy>{

    public void addSighting(Point currPos, double bearingRadians, ScannedRobotEvent event){
        String enemyName = event.getName();
        if (!containsKey(enemyName)) {
            put(enemyName, new Enemy(enemyName));
        }
        Enemy enemy = get(enemyName);
        enemy.addSighting(currPos, bearingRadians, event);
    }

    public Enemy findClosestEnemy(Point point) {
        double closestDistance = Double.MAX_VALUE;
        Enemy closest = null;
        for (Enemy e : this.values()) {
            double dist = point.distance(e.lastKnownPos());
            if (dist < closestDistance) {
                closestDistance = dist;
                closest = e;
            }
        }
        return closest;
    }

    public Enemy findStalestEnemy() {
        long oldest = Long.MAX_VALUE;
        Enemy oldestEnemy = null;
        for (Enemy e : this.values()) {
            if (e.latestSighting().turn < oldest) {
                oldest = e.latestSighting().turn;
                oldestEnemy = e;
            }
        }
        return oldestEnemy;
    }

}
