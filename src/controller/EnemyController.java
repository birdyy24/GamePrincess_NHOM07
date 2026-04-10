package controller;

import model.*;

import java.awt.Rectangle;
import java.util.List;

public class EnemyController {

    private final List<EnemyModel> enemies;
    private final List<PlatformModel> platforms;
    private final int WORLD_WIDTH;

    public EnemyController(List<EnemyModel> enemies, List<PlatformModel> platforms, int worldWidth) {
        this.enemies = enemies;
        this.platforms = platforms;
        this.WORLD_WIDTH = worldWidth;
    }

    public void update() {
        for (EnemyModel e : enemies) {
            if (e == null) continue;

            if (!e.isAlive()) {
                if (e.getDeadTimer() > 0)
                    e.setDeadTimer(e.getDeadTimer() - 1);
                continue;
            }

            double nx = e.isMovingRight()
                    ? e.getX() + e.getSpeed()
                    : e.getX() - e.getSpeed();

            e.setX(nx);

            if (e.getX() <= 0) e.setMovingRight(true);
            if (e.getX() >= WORLD_WIDTH - e.getWidth())
                e.setMovingRight(false);

            for (PlatformModel pl : platforms) {
                Rectangle er = new Rectangle((int)e.getX(), (int)e.getY(), e.getWidth(), e.getHeight());
                Rectangle pr = new Rectangle(pl.getX(), pl.getY(), pl.getWidth(), pl.getHeight());

                if (er.intersects(pr)) {
                    e.setMovingRight(!e.isMovingRight());
                    break;
                }
            }
        }
    }
}
