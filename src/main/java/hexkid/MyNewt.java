package hexkid;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Point;
import robocode.*;

import static hexkid.util.Physics.normalizeBearingRadians;

public class MyNewt extends AdvancedRobot {

    private Enemys enemies = new Enemys();
    private Enemy selectedTarget = null;
    private int dir = 1;

    @Override
    public void run() {
        enemies.clear();
        selectedTarget = null;
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
        setAhead(dir * 100);
        setTurnRight(90 - (180 * Math.random()));
        addCustomEvent(new MoveCompleteCondition(this));
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        double bearing = normalizeBearingRadians(getHeadingRadians() + event.getBearingRadians());
        final Point currPos = new Point(getX(), getY());
        enemies.addSighting(currPos, bearing, event);

        if (selectedTarget == null) {
            selectedTarget = enemies.findClosestEnemy(currPos);
        } else {
            final Enemy sightedEnemy = enemies.get(event.getName());
            if (currPos.distance(selectedTarget.lastKnownPos()) > currPos.distance(sightedEnemy.lastKnownPos())) {
                selectedTarget = sightedEnemy;
            }
        }

        if (selectedTarget != null) {
            setTurnGunRightRadians(normalizeBearingRadians(currPos.bearingTo(selectedTarget.lastKnownPos()) - getGunHeadingRadians()));
        }
        setTurnRadarLeftRadians(getRadarTurnRemainingRadians());

        if (selectedTarget != null && getGunHeat() == 0.0) {
            fire(3.0);
        }
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        enemies.remove(event.getName());
        if (selectedTarget != null && event.getName().equals(selectedTarget.name)) {
            selectedTarget = null;
        }
    }

    @Override
    public void onCustomEvent(CustomEvent event) {
        if (event.getCondition() instanceof MoveCompleteCondition) {
            setAhead(dir * (50 + Math.random() * 200));
            setTurnRight(90 - (180 * Math.random()));
            dir *= -1;
        }
    }
}
