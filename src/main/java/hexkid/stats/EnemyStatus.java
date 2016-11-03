package hexkid.stats;

import hexkid.util.Point;

public class EnemyStatus {

    public final long turn;
    public final double velocity;
    public final double heading;
    public final double distance;
    public final double bearing;
    public final Point pos;

    public EnemyStatus(long turn, double velocity, double heading, double distance, double bearing, Point targetPosition) {
        this.turn = turn;
        this.velocity = velocity;
        this.heading = heading;
        this.distance = distance;
        this.bearing = bearing;
        this.pos = targetPosition;
    }
}
