package hexkid.targetting;


import hexkid.util.Point;

public abstract class AbstractPredictor implements PositionPredictor {

    final protected PredicatorProperties properties;

    public AbstractPredictor(PredicatorProperties properties) {
        this.properties = properties;
    }

    protected double getBattleFieldHeight() {
        return properties.fieldHeight;
    }

    protected double getBattleFieldWidth() {
        return properties.fieldWidth;
    }

    protected double getHalfRobotWidth() {
        return properties.robotWidth/2;
    }

    private double normaliseHeight(double estimatedY) {
        return Math.max(getHalfRobotWidth(), Math.min(estimatedY, getBattleFieldHeight()- getHalfRobotWidth()));
    }

    private double normaliseWidth(double estimatedX) {
        return Math.max(getHalfRobotWidth(), Math.min(estimatedX, getBattleFieldWidth()- getHalfRobotWidth()));
    }

    protected Point getNormalisedPoint(double estimatedX, double estimatedY) {
        estimatedX = normaliseWidth(estimatedX);
        estimatedY = normaliseHeight(estimatedY);
        return new Point(estimatedX, estimatedY);
    }
}
