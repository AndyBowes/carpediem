package hexkid.movement;

import hexkid.util.Point;

public class RecentHit {
    public final long time;
    public final String name;
    public final Point pos;

    protected RecentHit(long time, String name, Point pos) {
        this.time = time;
        this.name = name;
        this.pos = pos;
    }
}

