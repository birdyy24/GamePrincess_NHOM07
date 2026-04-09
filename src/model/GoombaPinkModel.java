package model;

/**
 * Kẻ địch GoombaPink — phiên bản hồng dễ thương nhưng nguy hiểm.
 * Kế thừa EnemyModel, cho điểm 100 khi bị giẫm.
 */
public class GoombaPinkModel extends EnemyModel {

    public GoombaPinkModel(double x, double y) {
        super(x, y, 1.5);
    }

    @Override
    public int getScoreValue() { return 100; }

    @Override
    public String getType() { return "goomba_pink"; }
}
