package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Physics;
import hexkid.util.Point;
import robocode.AdvancedRobot;
import robocode.HitByBulletEvent;

import java.awt.*;
import java.util.LinkedList;

import static java.lang.Math.PI;
import static java.lang.Math.abs;

public abstract class AbstractMove implements Move {

    protected final AdvancedRobot me;
    protected final Enemys enemys;

    protected static LinkedList<RecentHit> recentHits = new LinkedList<RecentHit>();

    protected AbstractMove(AdvancedRobot me, Enemys enemys) {
        this.me = me;
        this.enemys = enemys;
    }

    @Override
    public void handleIncoming(Point myPos, Enemy enemy, double bulletPower) {

    }

    protected double moveOnBearing(double bearing, int distance) {
        return moveOnBearing(bearing, distance, PI * 2);
    }

    protected double moveOnBearing(double bearing, int distance, double maxTurn) {
        double dir = 1;
        if (abs(bearing) > PI / 2) {
            dir = -1;
            bearing = Physics.normalizeBearingRadians(bearing + PI);
        }
        if (abs(bearing) > maxTurn) {
            bearing = Math.copySign(maxTurn, bearing);
        }
        me.setAhead(dir * distance);
        me.setTurnRightRadians(bearing);
        return bearing;
    }

    @Override
    public void recordHitByBullet(Point point, HitByBulletEvent event) {
        recentHits.add(new RecentHit(event.getTime(), event.getName(), point));
    }

    @Override
    public void removeOldHits(long time){
        while (recentHits.size() > 0 && recentHits.getFirst().time < time){
            recentHits.removeFirst();
        }
    }

    @Override
    public void paint(Graphics2D g) {

    }

    protected void drawPosition(Graphics2D g, Point pos) {
        g.setColor(Color.YELLOW);
        if (pos != null) {
            int x = (new Double(pos.x)).intValue();
            int y = (new Double(pos.y)).intValue();
            g.drawOval(x - 6, y - 6, 12, 12);
            g.drawLine(x - 12, y, x + 12, y);
            g.drawLine(x, y - 12, x, y + 12);
        }
    }

}
