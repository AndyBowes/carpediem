package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Point;
import robocode.AdvancedRobot;

import java.awt.*;

public abstract class AbstractMove implements Move {

    protected final AdvancedRobot me;
    protected final Enemys enemys;

    protected AbstractMove(AdvancedRobot me, Enemys enemys) {
        this.me = me;
        this.enemys = enemys;
    }

    @Override
    public void handleIncoming(Point myPos, Enemy enemy, double bulletPower) {

    }

    @Override
    public void paint(Graphics2D g) {

    }
}
