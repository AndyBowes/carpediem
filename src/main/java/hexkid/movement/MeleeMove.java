package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Point;
import robocode.AdvancedRobot;

public class MeleeMove extends AbstractMove {

    public MeleeMove(AdvancedRobot me, Enemys enemys) {
        super(me, enemys);
    }

    @Override
    public void doMove(Point myPos, Enemy enemy) {
        me.setMaxVelocity(4.0);
        me.setAhead(32.0);
        me.setTurnLeft(20.0);
    }
}
