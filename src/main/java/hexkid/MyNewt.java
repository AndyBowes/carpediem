package hexkid;

import hexkid.stats.Enemy;
import hexkid.stats.Enemys;
import hexkid.util.Point;
import robocode.AdvancedRobot;
import robocode.ScannedRobotEvent;

public class MyNewt extends AdvancedRobot {

    private Enemys enemies = new Enemys();
    private Enemy selectedTarget = null;
    private Point predictedPosition = null;

    @Override
    public void run() {
        super.run();
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        super.onScannedRobot(event);
    }


}
