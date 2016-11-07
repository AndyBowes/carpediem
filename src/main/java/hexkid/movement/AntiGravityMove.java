package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.stats.EnemyStatus;
import hexkid.stats.Enemys;
import hexkid.util.Physics;
import hexkid.util.Point;
import robocode.AdvancedRobot;
import robocode.Rules;

import java.awt.*;

import static java.lang.Math.*;

public class AntiGravityMove extends AbstractMove {


    public static final int CORNER_FORCE_MULTIPLIER = 6000;
    public static final int WALL_FORCE_MULTIPLIER = 4000;
    public static final int BULLET_FORCE_MULTIPLIER = 500;
    public static final int ROBOT_FORCE_MULTIPLIER = 500;
    private double movedBearing;

    public AntiGravityMove(AdvancedRobot me, Enemys enemys) {
        super(me, enemys);
    }

    @Override
    public void doMove(Point myPos, Enemy enemy) {

        me.getTime();

        me.setMaxVelocity(Rules.MAX_VELOCITY);

        double forceX = 0;
        double forceY = 0;

        // Find AG Component from the walls
        forceX += WALL_FORCE_MULTIPLIER / pow(me.getX() - 15, 2);
        forceX -= WALL_FORCE_MULTIPLIER / pow(me.getBattleFieldWidth() - me.getX() - 15, 2);
        forceY += WALL_FORCE_MULTIPLIER / pow(me.getY() - 15, 2);
        forceY -= WALL_FORCE_MULTIPLIER / pow(me.getBattleFieldHeight() - me.getY() - 15, 2);

        // Iterate through the Enemys to Calculate the AG Component of each Enemy
        for (Enemy e : enemys.values()) {
            EnemyStatus latestSighting = e.latestSighting();
            final double distance = myPos.distance(latestSighting.pos);
            final double theta = latestSighting.pos.bearingTo(myPos);

            // Stay further from enemies with high energy
            final double botForce = sqrt(latestSighting.energy) * ROBOT_FORCE_MULTIPLIER / pow(distance,1.5);
            forceX += botForce * sin(theta);
            forceY += botForce * cos(theta);
        }

        // Try and stay about 200 from the closest Enemy
        Enemy closestEnemy = enemys.findClosestEnemy(myPos);
        if (closestEnemy != null) {
            EnemyStatus latestSighting = closestEnemy.latestSighting();
            final double distance = myPos.distance(latestSighting.pos);
            final double theta = latestSighting.pos.bearingTo(myPos);
            final double botForce = (200-distance) * ROBOT_FORCE_MULTIPLIER * 0.001;
            forceX += botForce * sin(theta);
            forceY += botForce * cos(theta);
        }

        // Avoid locations of previously recorded hits
        for (RecentHit bulletHit : recentHits){
            final double distance = myPos.distance(bulletHit.pos) + 1;
            final double theta = bulletHit.pos.bearingTo(myPos);
            final double botForce = BULLET_FORCE_MULTIPLIER/ pow(distance,1.25);
            forceX += botForce * sin(theta);
            forceY += botForce * cos(theta);
        }

        me.setDebugProperty("AG: forceX", String.valueOf(forceX));
        me.setDebugProperty("AG: forceY", String.valueOf(forceY));

        double bearing = atan2(forceX,forceY);
        me.setDebugProperty("AG: bearing", String.valueOf(toDegrees(bearing)));

        double turn = Physics.normalizeBearingRadians(bearing - me.getHeadingRadians());
        me.setDebugProperty("AG: turn", String.valueOf(toDegrees(turn)));
        moveOnBearing(turn, 32);

        movedBearing = bearing;
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        Point targetPoint = new Point(me.getX(), me.getY()).translateRadians(movedBearing, 64);
        drawPosition(g,targetPoint);
    }
}
