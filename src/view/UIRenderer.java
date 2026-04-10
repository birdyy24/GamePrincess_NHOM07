package view.render;

import controller.GameController;

import java.awt.*;

public class UIRenderer {

    public UIRenderer(GameController controller) {}

    public void drawMenu(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString("PRINCESS GAME", 200, 200);

        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Press ENTER to Start", 250, 300);
    }

    public void drawHUD(Graphics2D g2) {}

    public void drawPause(Graphics2D g2) {
        g2.drawString("PAUSED", 350, 250);
    }

    public void drawGameOver(Graphics2D g2) {
        g2.drawString("GAME OVER", 320, 250);
    }

    public void drawWin(Graphics2D g2) {
        g2.drawString("YOU WIN!", 330, 250);
    }
}
