package hexkid;

import hexkid.condition.EnemyFiredCondition;
import hexkid.movement.DodgeMove;
import hexkid.movement.MeleeMove;
import hexkid.movement.Move;
import hexkid.stats.Enemy;
import hexkid.stats.EnemyStatus;
import hexkid.stats.Enemys;
import hexkid.targetting.PositionPredictor;
import hexkid.targetting.TargetManager;
import hexkid.util.Point;
import robocode.*;

import java.awt.*;

import static java.awt.Color.*;
import static java.lang.Math.PI;
import static hexkid.util.Physics.normalizeBearingRadians;

public class CarpeDiem extends AdvancedRobot {

    protected static final int CLOCKWISE = 1;
    protected static final int ANTICLOCKWISE = -1;

    protected static final int FORWARD = 1;
    protected static final int BACKWARD = -1;
    private static final double SWAP_FACTOR = 0.8;

    private byte radarDir = CLOCKWISE; // 1 => Clockwise, -1 => Anticlockwise
    private byte robotDir = FORWARD; // 1 => Forwards, -1 => Backwards

    private Enemys enemies = new Enemys();
    private Enemy selectedTarget = null;
    private Point predictedPosition = null;

    private TargetManager targetManager;
    private Move currentMove = null;
    private EnemyFiredCondition enemyFiredCondition=null;

    public Enemy getCurrentTarget(){
        return this.selectedTarget;
    }

    public Enemys getEnemies(){
        return enemies;
    }

    @Override
    public void onRobotDeath(RobotDeathEvent event) {
        super.onRobotDeath(event);
        enemies.remove(event.getName());
        if (selectedTarget != null && selectedTarget.name.equals(event.getName())) {
            selectedTarget = null;
        }
        if (getOthers() == 1){
            currentMove = new DodgeMove(this, enemies);
        }
    }

    @Override
    public void run() {

        this.enemies.clear();
        this.radarDir = 1;
        this.robotDir = 1;

        this.currentMove = new MeleeMove(this, this.enemies);
        if (this.getOthers()==1){
            this.currentMove = new DodgeMove(this, this.enemies);
        }

        targetManager = new TargetManager(getBattleFieldWidth(), getBattleFieldHeight(), getWidth());

        setColors(ORANGE, BLACK, LIGHT_GRAY);
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        addCustomEvent(new RadarTurnCompleteCondition(this));
        setTurnRadarRightRadians(2 * Math.PI * radarDir); // Perform a complete circle
        selectedTarget = null;

        setMaxVelocity(4.0);

        while (true) {

            Point myPosition = currentPosition();

            Enemy previousTarget = getCurrentTarget();
            selectedTarget = selectTarget(myPosition, selectedTarget);
            if (selectedTarget!= null && !selectedTarget.equals(previousTarget)){
                onSwitchedTarget(previousTarget, selectedTarget);
            }

            doMove(selectedTarget);

            if (selectedTarget != null) {

                double enemyDistance = myPosition.distance(selectedTarget.lastKnownPos()) - (this.getWidth() / 2);
                double bulletPower = calcBulletPower(enemyDistance);
                double bulletVelocity = Rules.getBulletSpeed(bulletPower);
                double bulletTime = enemyDistance / bulletVelocity;
                double coolingTime = getGunHeat() / getGunCoolingRate();
                double timeToImpact = coolingTime + bulletTime;

                // Predict where the target is likely to be when the bullet reaches them
                predictedPosition = predictFutureTargetPosition(selectedTarget, getTime() + timeToImpact, bulletVelocity);

                // TODO - Need to check if the distance to the estimate position is significantly different than distance to target.

                // demonstrate feature of debugging properties on RobotDialog
                if (predictedPosition != null) {
                    setDebugProperty("predictedPosition", selectedTarget.name + " at " + predictedPosition.x + " ," + predictedPosition.y);

                    // Point the gun at the predicted position.
                    aimGunAtPosition(predictedPosition);
                } else {
                    setDebugProperty("predictedPosition", "None");
                }

                if (canFire()) {
                    setFireBullet(bulletPower);
                }
            }
            execute(); // Perform all the Queued Actions
        }
    }

    private void onSwitchedTarget(Enemy previousTarget, Enemy newTarget) {
        if (enemyFiredCondition != null) {
            removeCustomEvent(enemyFiredCondition);
        }
        enemyFiredCondition = new EnemyFiredCondition(newTarget);
        addCustomEvent(enemyFiredCondition);
    }

    private void aimGunAtPosition(Point predictedPosition) {
        double bearingRadians = currentPosition().bearingTo(predictedPosition);
        setDebugProperty("targetBearing", selectedTarget.name + " : " + bearingRadians);
        setTurnGunRightRadians(normalizeBearingRadians(bearingRadians - getGunHeadingRadians()));
    }

    private Point predictFutureTargetPosition(Enemy target, double timeToImpact, double bulletVelocity) {
        PositionPredictor positionPredictor = targetManager.getPositionPredictor(target);
        setDebugProperty("predictedPredictor", positionPredictor.getClass().getName());
        Point predictedPosition = positionPredictor.estimatePosition(currentPosition(), target, timeToImpact, bulletVelocity);
        return predictedPosition;
    }

    public Point currentPosition() {
        return new Point(this.getX(), this.getY());
    }

    private void doMove(Enemy selectedTarget) {
        // Delegate to Current Movement Mode
        this.currentMove.doMove(currentPosition(), selectedTarget);
    }

    private Enemy selectTarget(Point myPosition, Enemy currentTarget) {
        Enemy selectedTarget;
        Enemy closestEnemy = this.enemies.findClosestEnemy(currentPosition());
        if (currentTarget == null || closestEnemy.equals(currentTarget)) {
            selectedTarget = closestEnemy;
        } else {
            selectedTarget = currentTarget;
            if (this.getGunHeat() > 0.5){
                // If there is another target which is significantly closer then switch targets
                if (myPosition.distance(selectedTarget.lastKnownPos()) * SWAP_FACTOR > myPosition.distance(closestEnemy.lastKnownPos())) {
                    selectedTarget = closestEnemy;
                }
            }
        }
        return selectedTarget;
    }

    private boolean canFire() {
        return (getGunHeat() == 0.0) && Math.abs(getGunTurnRemaining()) < 10.0d;
    }

    private double calcBulletPower(double targetDistance) {
        return Math.min(this.getEnergy() / 4, Rules.MAX_BULLET_POWER);
    }

    @Override
    public void onCustomEvent(CustomEvent event) {
        if (event.getCondition() instanceof RadarTurnCompleteCondition) {
            onRadarScanComplete(event);
        } else if ( event.getCondition() instanceof EnemyFiredCondition){
            onIncomingBullet(event, ((EnemyFiredCondition)event.getCondition()).enemy);
        }
    }

    public void onRadarScanComplete(CustomEvent event) {
        radarDir *= -1;
        setTurnRadarRightRadians(2 * PI * radarDir);
    }

    public void onIncomingBullet(CustomEvent event, Enemy enemy){
        double bulletPower = Rules.MAX_BULLET_POWER; // TODO - Determine energy of Enemies Bullet
        currentMove.handleIncoming(currentPosition(), enemy, bulletPower);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent event) {
        double bearing = normalizeBearingRadians(getHeadingRadians() + event.getBearingRadians());
        enemies.addSighting(currentPosition(), bearing, event);

        // If there is only 1 other robot then keep focus on that robot.
        if (getOthers() == 1) {
            double radarSweep = getHeadingRadians() - getRadarHeadingRadians() + event.getBearingRadians();
            radarDir *= -1;
            setTurnRadarRightRadians(radarSweep);
        }
    }

    @Override
    public void onWin(WinEvent event) {
        for (int i=0; i<25; i++){
            turnRight(18);
            turnLeft(18);
        }
    }

    @Override
    public void onPaint(Graphics2D g) {
        super.onPaint(g);

        if (currentMove!=null){
            currentMove.paint(g);
        }

        // Show predicted position
        g.setColor(Color.red);
        if (predictedPosition != null) {
            int x = (new Double(predictedPosition.x)).intValue();
            int y = (new Double(predictedPosition.y)).intValue();
            g.drawOval(x - 6, y - 6, 12, 12);
            g.drawLine(x - 12, y, x + 12, y);
            g.drawLine(x, y - 12, x, y + 12);
        }

        // Draw History positions
        if (this.selectedTarget != null) {
            g.setColor(Color.green);
            for (EnemyStatus es : this.selectedTarget.sightings) {
                g.fillOval((new Double(es.pos.x)).intValue(), (new Double(es.pos.y)).intValue(), 10, 10);
            }
        }
    }

}
