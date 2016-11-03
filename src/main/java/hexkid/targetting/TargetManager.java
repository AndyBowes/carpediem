package hexkid.targetting;

import hexkid.stats.Enemy;

import java.util.HashMap;
import java.util.Map;

public class TargetManager {

    Map<String,PositionPredictor> predictors = new HashMap<String,PositionPredictor>();

    public TargetManager(double fieldWidth, double fieldHeight, double robotWidth) {
        PredicatorProperties properties = new PredicatorProperties(fieldWidth, fieldHeight, robotWidth);
        predictors.put(CircularPredictor.class.getName(), new CircularPredictor(properties));
        predictors.put(LinearPredictor.class.getName(), new LinearPredictor(properties));
    }

    public PositionPredictor getPositionPredictor(Enemy enemy){
        PositionPredictor predictor = isCircularMovement(enemy) ? getInstance(CircularPredictor.class) : getInstance(LinearPredictor.class);
        return predictor;
    }

    private PositionPredictor getInstance(Class predictorClass){
        return predictors.get(predictorClass.getName());
    }

    private boolean isCircularMovement(Enemy enemy){
        if (enemy.sightings.size() < 2){
            return false;
        }
        return Math.toDegrees(Math.abs(enemy.latestSighting().heading - enemy.sightings.get(1).heading)) > 1.0;
    }

}
