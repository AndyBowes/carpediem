package hexkid.stats;

import hexkid.util.Point;
import org.junit.Before;
import org.junit.Test;
import robocode.ScannedRobotEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Andy Bowes on 27/10/2016.
 */
public class EnemysTest {

    public static final String ENEMY_1 = "enemy1";
    public static final String ENEMY_2 = "enemy2";
    public static final String ENEMY_3 = "enemy3";

    private Enemys enemys = new Enemys();

    @Before
    public void setup() {
        enemys.clear();

        Point currPos = new Point(45.0,75.0);

        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_1, 30.0, 90.0, 100.0, 60.0, 5.0, 10));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_1, 30.0, 90.0, 98.0, 60.0, 5.0, 11));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_1, 30.0, 90.0, 97.0, 60.0, 5.0, 12));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_1, 30.0, 90.0, 96.0, 60.0, 5.0, 13));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_1, 30.0, 90.0, 94.0, 60.0, 5.0, 14));

        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_2, 78.0, 180.0, 100.0, 45.0, 5.0, 10));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_2, 75.0, 180.0, 98.0, 46.0, 4.0, 11));
        enemys.addSighting(currPos, getHeading(), createScannedEvent(ENEMY_2, 74.0, 180.0, 97.0, 47.0, 3.0, 12));



    }

    private ScannedRobotEvent createScannedEvent(String name, double energy, double bearing, double distance, double heading, double velocity, long time) {
        ScannedRobotEvent evt = new ScannedRobotEvent(name, energy, bearing, distance, heading, velocity, false);
        evt.setTime(time);
        return evt;
    }

    @Test
    public void testAddEnemy() {

    }

    @Test
    public void testFindClosestEnemy() {
        Point currPos = new Point(0, 0);
        Enemy enemy = enemys.findClosestEnemy(currPos);
        assertNotNull(enemy);
        assertEquals("enemy1", enemy.name);
    }

    @Test
    public void testFindStalestEnemy() {

    }

}
