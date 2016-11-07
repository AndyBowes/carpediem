package hexkid.movement;

import hexkid.stats.Enemy;
import hexkid.util.Point;
import robocode.HitByBulletEvent;

import java.awt.*;

public interface Move {

    public void doMove(Point myPos, Enemy enemy);

    public void handleIncoming(Point myPos, Enemy enemy, double bulletPower);

    void removeOldHits(long time);

    public void paint(Graphics2D g);

    void recordHitByBullet(Point point, HitByBulletEvent event);
}
