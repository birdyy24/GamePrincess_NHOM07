package view.render;

import controller.GameController;

import java.awt.*;

public class GameRenderer {

    private final GameController controller;
    private final EntityRenderer entityRenderer;
    private final BackgroundRenderer background;

    public GameRenderer(GameController controller) {
        this.controller = controller;

        entityRenderer = new EntityRenderer(controller);
        background = new BackgroundRenderer();
    }

    public void draw(Graphics2D g2, int width, int height) {

        int cam = controller.getCameraX();

        // 🌤️ VẼ NỀN TRƯỚC
        background.draw(g2, width, height);

        // 👸 VẼ NHÂN VẬT + ENEMY + COIN
        entityRenderer.draw(g2, cam);
    }
}
