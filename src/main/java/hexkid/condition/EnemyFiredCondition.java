package hexkid.condition;

import hexkid.stats.Enemy;
import robocode.Condition;

public class EnemyFiredCondition extends Condition {

    public final Enemy enemy;

    public EnemyFiredCondition(Enemy enemy) {
        super("Incoming:" + enemy.name);
        this.enemy = enemy;
    }

    @Override
    public boolean test() {
        return enemy.hasJustFired();
    }
}
