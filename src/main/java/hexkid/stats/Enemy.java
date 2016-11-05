package hexkid.stats;

import hexkid.util.Point;
import robocode.Rules;
import robocode.ScannedRobotEvent;

import java.util.LinkedList;

public class Enemy {

    // Maximum number of sightings elements to keep
    private static final int MAX_HISTORY = 50;

    public final String name;
    public final LinkedList<EnemyStatus> sightings;

    public Enemy(String name) {
        this.name = name;
        sightings = new LinkedList<EnemyStatus>();
    }

    public void addSighting(Point currPos, double bearingRadians, ScannedRobotEvent e){
        Point targetPosition = currPos.translateRadians(bearingRadians,e.getDistance());
        EnemyStatus status = new EnemyStatus(e.getTime(),e.getVelocity(), e.getHeadingRadians(), e.getDistance(), e.getBearingRadians(), e.getEnergy(), targetPosition);
        addSighting(status);
    }

    public void addSighting(EnemyStatus status) {
        sightings.addFirst(status);
        while (sightings.size() > MAX_HISTORY){
            sightings.removeLast();
        }
    }

    public Point lastKnownPos(){
        return latestSighting().pos;
    }

    public EnemyStatus latestSighting(){
        return sightings.getFirst();
    }

    public boolean hasJustFired() {
        if (sightings.size() > 2){
            double energyDrop = sightings.get(1).energy - latestSighting().energy;
            return (energyDrop > 0 && energyDrop <= Rules.MAX_BULLET_POWER);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj != null && (obj instanceof Enemy)) ? name.equals(((Enemy)obj).name): false;
    }
}
