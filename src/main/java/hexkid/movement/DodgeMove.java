package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Physics;
import hexkid.util.Point;
import robocode.AdvancedRobot;
import robocode.Rules;

import java.awt.*;

import static java.lang.Math.PI;

public class DodgeMove extends AbstractMove {

    private static final double OPTIMUM_DISTANCE = 150.0;

    private final Point arenaCentre;
    private Point moveTowards = null;

    public DodgeMove(AdvancedRobot me, Enemys enemys) {
        super(me, enemys);
        arenaCentre = new Point(me.getBattleFieldWidth()/2, me.getBattleFieldHeight()/2);
    }

    @Override
    public void doMove(Point myPos, Enemy enemy) {
        me.setMaxVelocity(Rules.MAX_VELOCITY);
        if (enemy!= null){
            double bearingToEnemy = myPos.bearingTo(enemy.lastKnownPos());

            // If Robot is stationary then turn at 90 degrees to the enemy
            if ( me.getDistanceRemaining() < 5.0){
                me.setTurnRightRadians(Physics.normalizeBearingRadians(bearingToEnemy+(PI/2)-me.getHeadingRadians()));
            }
        }
    }

    @Override
    public void handleIncoming(Point myPos, Enemy enemy, double bulletPower) {
        super.handleIncoming(myPos, enemy, bulletPower);
        me.setMaxVelocity(Rules.MAX_VELOCITY);
        if ( me.getDistanceRemaining() > 0){
            me.setAhead(-1*me.getDistanceRemaining());
            return;
        }

        // Get 2 Points either side of bearing from Enemy & pick the one closest to the arena centre;
        Point enemyPos = enemy.lastKnownPos();
        double bearingFromEnemy = enemyPos.bearingTo(myPos);
        Point pos1 = enemyPos.translateRadians(bearingFromEnemy + (PI / 8), OPTIMUM_DISTANCE);
        Point pos2 = enemyPos.translateRadians(bearingFromEnemy - (PI / 8), OPTIMUM_DISTANCE);

        moveTowards = (arenaCentre.distance(pos1) < arenaCentre.distance(pos2)) ? pos1 : pos2;

        double turn = Physics.normalizeBearingRadians(myPos.bearingTo(moveTowards) - me.getHeadingRadians());
        moveOnBearing(turn, 80, PI/6);
    }

    @Override
    public void paint(Graphics2D g) {
        super.paint(g);
        drawPosition(g, moveTowards);
    }
}
