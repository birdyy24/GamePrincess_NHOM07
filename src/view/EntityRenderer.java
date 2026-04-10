package view.render;

import controller.GameController;
import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import model.*;

public class EntityRenderer {

    private final GameController controller;

    private BufferedImage princessImg;
    private BufferedImage blockImg, pipeImg, coinImg;

    public EntityRenderer(GameController controller) {
        this.controller = controller;
        try {
            princessImg = ImageIO.read(getClass().getResource("/assets/princess.png"));
        } catch (Exception e) { System.out.println("Khong load duoc anh Cong chua!"); }
        try {
            blockImg = ImageIO.read(getClass().getResource("/assets/block.png"));
            pipeImg  = ImageIO.read(getClass().getResource("/assets/pipe.png"));
            coinImg  = ImageIO.read(getClass().getResource("/assets/coin.png"));
        } catch (Exception e) { System.out.println("Khong load duoc anh block/pipe/coin!"); }
    }

    public void setAnim(int tick, int frame) { }

    public void draw(Graphics2D g2, int cam) {
        drawStairs(g2, cam);
        drawPlatforms(g2, cam);
        drawObstacles(g2, cam);
        drawCoins(g2, cam);
        drawGolds(g2, cam);
        drawEnemies(g2, cam);
        drawTrophy(g2, cam);      // ← CÚP
        drawPlayer(g2, cam);
        drawHUD(g2);
        drawOverlay(g2);          // ← TẤT CẢ OVERLAY
    }

    // ── 1. Bậc thang ──
    private void drawStairs(Graphics2D g2, int cam) {
        for (StairModel s : controller.getStairs()) {
            int x = s.getX() - cam, y = s.getY(), w = s.getWidth(), h = s.getHeight();
            int tileH = 40;
            for (int ty = y; ty < y + h; ty += tileH) {
                int drawH = Math.min(tileH, (y + h) - ty);
                g2.setColor(new Color(160, 82, 45));  g2.fillRect(x, ty, w, drawH);
                g2.setColor(new Color(120, 60, 30));  g2.fillRect(x, ty + drawH / 2, w, 2);
                g2.fillRect(x + w / 2, ty, 2, drawH);
                g2.setColor(new Color(80, 40, 10));   g2.drawRect(x, ty, w - 1, drawH - 1);
                g2.setColor(new Color(200, 120, 60, 80)); g2.fillRect(x + 2, ty + 2, w / 3, 3);
            }
            g2.setColor(new Color(100, 50, 20)); g2.fillRect(x, y, w, 3);
        }
    }

    // ── 2. Platforms ──
    private void drawPlatforms(Graphics2D g2, int cam) {
        for (PlatformModel p : controller.getPlatforms()) {
            int x = p.getX() - cam, y = p.getY();
            BufferedImage img = null;
            switch (p.getType()) { case 0: case 1: img = blockImg; break; case 2: img = pipeImg; break; }
            if (img != null) {
                g2.drawImage(img, x, y, p.getWidth(), p.getHeight(), null);
            } else {
                g2.setColor(p.getType() == 2 ? new Color(34, 139, 34) : new Color(139, 90, 43));
                g2.fillRect(x, y, p.getWidth(), p.getHeight());
                g2.setColor(Color.BLACK); g2.drawRect(x, y, p.getWidth(), p.getHeight());
            }
        }
    }

    // ── 3. Chướng ngại vật ──
    private void drawObstacles(Graphics2D g2, int cam) {
        for (ObstacleModel obs : controller.getObstacles()) {
            if (!obs.isActive()) continue;
            int x = (int) obs.getX() - cam, y = (int) obs.getY(), w = obs.getWidth(), h = obs.getHeight();
            switch (obs.getType()) {
                case "rock":
                    g2.setColor(new Color(100, 100, 100)); g2.fillRoundRect(x, y, w, h, 10, 10);
                    g2.setColor(new Color(160, 160, 160)); g2.fillRoundRect(x+4, y+4, w/2, h/3, 6, 6);
                    g2.setColor(Color.DARK_GRAY);          g2.drawRoundRect(x, y, w, h, 10, 10);
                    break;
                case "spike":
                    g2.setColor(new Color(80, 80, 80)); g2.fillRect(x, y + h/2, w, h/2);
                    g2.setColor(new Color(140, 140, 140));
                    for (int i = 0; i < 3; i++) {
                        Polygon sp = new Polygon();
                        sp.addPoint(x+i*(w/3), y+h/2); sp.addPoint(x+i*(w/3)+w/6, y); sp.addPoint(x+i*(w/3)+w/3, y+h/2);
                        g2.fillPolygon(sp);
                    }
                    break;
                case "fireball":
                    g2.setColor(new Color(255, 80, 0));  g2.fillOval(x, y, w, h);
                    g2.setColor(new Color(255, 210, 0)); g2.fillOval(x+w/4, y+h/4, w/2, h/2);
                    break;
                default: g2.setColor(Color.RED); g2.fillRect(x, y, w, h);
            }
        }
    }

    // ── 4. Coin ──
    private void drawCoins(Graphics2D g2, int cam) {
        for (CoinModel c : controller.getCoins()) {
            int x = c.getX() - cam, y = c.getY();
            if (coinImg != null) { g2.drawImage(coinImg, x, y, 32, 32, null); }
            else {
                g2.setColor(Color.YELLOW); g2.fillOval(x+4, y+4, 24, 24);
                g2.setColor(Color.ORANGE); g2.drawOval(x+4, y+4, 24, 24);
            }
        }
    }

    // ── 5. Gold ──
    private void drawGolds(Graphics2D g2, int cam) {
        for (GoldModel g : controller.getGolds()) {
            if (g.isCollected()) continue;
            int x = g.getX()-cam, y = g.getY(), w = g.getWidth(), h = g.getHeight();
            g2.setColor(new Color(255, 180, 0));    g2.fillRoundRect(x, y, w, h, 6, 6);
            g2.setColor(new Color(255, 230, 80));   g2.fillRoundRect(x+2, y+2, w-4, h/2-1, 4, 4);
            g2.setColor(new Color(180, 100, 0));    g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString("$", x+w/2-4, y+h/2+5);
            g2.setColor(new Color(200, 140, 0));    g2.drawRoundRect(x, y, w-1, h-1, 6, 6);
            g2.setColor(new Color(255, 255, 180, 200)); g2.fillOval(x+w-7, y+2, 5, 5);
        }
    }

    // ── 6. Enemy ──
    private void drawEnemies(Graphics2D g2, int cam) {
        for (EnemyModel e : controller.getEnemies()) {
            if (e == null) continue;
            int x = (int)e.getX()-cam, y = (int)e.getY();
            g2.setColor(new Color(139, 69, 19)); g2.fillRect(x, y-32, 32, 32);
            g2.setColor(Color.WHITE);
            g2.fillRect(x+5, y-25, 6, 6); g2.fillRect(x+21, y-25, 6, 6);
        }
    }

    // ── 7. CÚP ĐÍCH ───────────────────────────────────────────────────
    private void drawTrophy(Graphics2D g2, int cam) {
        TrophyModel trophy = controller.getTrophy();
        if (trophy == null || trophy.isCollected()) return;

        int tx = (int) trophy.getX() - cam;
        int ty = (int) trophy.getY();
        int tw = trophy.getWidth();
        int th = trophy.getHeight();

        // Hào quang nhấp nháy
        float glow = (float)(0.3 + 0.25 * Math.sin(trophy.getAnimTick() * 2));
        g2.setColor(new Color(1f, 0.85f, 0f, glow));
        g2.fillOval(tx - 12, ty - 10, tw + 24, th + 20);

        // Đế cúp
        g2.setColor(new Color(160, 100, 0));
        g2.fillRoundRect(tx + 9, ty + th - 13, tw - 18, 13, 6, 6);
        g2.fillRoundRect(tx + 5, ty + th - 20, tw - 10, 9, 4, 4);

        // Thân chén
        g2.setColor(new Color(255, 200, 0));
        int[] xp = {tx+4, tx+tw-4, tx+tw-11, tx+11};
        int[] yp = {ty+8, ty+8, ty+th-20, ty+th-20};
        g2.fillPolygon(xp, yp, 4);
        g2.fillOval(tx+1, ty+1, tw-2, 22);

        // Highlight
        g2.setColor(new Color(255, 240, 120));
        g2.fillOval(tx+5, ty+3, tw-10, 10);

        // Tay cầm
        g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setColor(new Color(210, 150, 0));
        g2.drawArc(tx-12, ty+9, 20, 18, 90, -180);
        g2.drawArc(tx+tw-8, ty+9, 20, 18, 90, 180);
        g2.setStroke(new BasicStroke(1f));

        // Ngôi sao đỉnh
        drawStar(g2, tx + tw/2, ty - 6, 9, new Color(255, 230, 50));

        // Chữ DICH nhấp nháy
        float ta = (float)(0.65 + 0.35 * Math.sin(trophy.getAnimTick() * 3));
        g2.setColor(new Color(1f, 1f, 0.2f, ta));
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        FontMetrics fm = g2.getFontMetrics();
        String lbl = "DICH";
        g2.drawString(lbl, tx + tw/2 - fm.stringWidth(lbl)/2, ty - 18);
    }

    private void drawStar(Graphics2D g2, int cx, int cy, int r, Color color) {
        int[] xs = new int[10], ys = new int[10];
        for (int i = 0; i < 10; i++) {
            double a = Math.PI / 5 * i - Math.PI / 2;
            int rr = (i % 2 == 0) ? r : r / 2;
            xs[i] = cx + (int)(rr * Math.cos(a));
            ys[i] = cy + (int)(rr * Math.sin(a));
        }
        g2.setColor(color); g2.fillPolygon(xs, ys, 10);
    }

    // ── 8. Player ──
    private void drawPlayer(Graphics2D g2, int cam) {
        PlayerModel p = controller.getPlayer();
        int drawX = (int) p.getX() - cam, drawY = (int) p.getY();
        if (princessImg != null) {
            int dstH = p.getHeight(), dstW = 173 * dstH / 296;
            int offsetX = (p.getWidth() - dstW) / 2;
            g2.drawImage(princessImg, drawX+offsetX, drawY, drawX+offsetX+dstW, drawY+dstH,
                    25, 33, 198, 329, null);
        } else {
            g2.setColor(new Color(255, 105, 180));
            g2.fillRoundRect(drawX, drawY, p.getWidth(), p.getHeight(), 10, 10);
        }
    }

    // ── 9. HUD ──
    private void drawHUD(Graphics2D g2) {
        PlayerModel p = controller.getPlayer();
        int secs = controller.getElapsedSeconds();

        g2.setColor(new Color(0, 0, 0, 120));
        g2.fillRoundRect(8, 8, 220, 100, 12, 12);

        g2.setFont(new Font("Arial", Font.BOLD, 16));
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + p.getScore(), 18, 30);

        g2.setColor(new Color(255, 100, 100));
        StringBuilder h = new StringBuilder();
        for (int i = 0; i < p.getLives(); i++) h.append("\u2764 ");
        g2.drawString(h.toString(), 18, 52);

        g2.setColor(Color.CYAN);
        g2.drawString("Level: " + controller.getCurrentLevel() + " / " + controller.getMaxLevel(), 18, 74);

        g2.setColor(new Color(180, 255, 180));
        g2.drawString(String.format("Time: %02d:%02d", secs/60, secs%60), 18, 96);

        // Gợi ý ESC
        g2.setColor(new Color(255, 255, 255, 90));
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("ESC / P : Pause", 650, 22);
    }

    // ── 10. OVERLAY tổng ─────────────────────────────────────────────
    private void drawOverlay(Graphics2D g2) {
        GameState state = controller.getGameState();
        if      (state == GameState.LEVEL_COMPLETE) drawLevelCompleteOverlay(g2);
        else if (state == GameState.PAUSED)         drawPauseOverlay(g2);
        else if (state == GameState.GAME_OVER)      drawGameOverOverlay(g2);
    }

    // ── LEVEL COMPLETE ────────────────────────────────────────────────
    private void drawLevelCompleteOverlay(Graphics2D g2) {
        int W = 800, H = 600;
        float progress = controller.getLevelCompleteProgress();
        int level = controller.getCurrentLevel(), maxLevel = controller.getMaxLevel();

        g2.setColor(new Color(0, 0, 0, 140)); g2.fillRect(0, 0, W, H);

        int panelW = 440, panelH = 230;
        int panelX = (W - panelW) / 2;
        int panelY = (int)(-panelH + progress * (H/2f - panelH/2f + panelH));

        // Panel vàng
        GradientPaint grad = new GradientPaint(panelX, panelY, new Color(255, 210, 0),
                panelX, panelY + panelH, new Color(190, 110, 0));
        g2.setPaint(grad);
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 28, 28);
        g2.setPaint(null);
        g2.setColor(new Color(255, 245, 120));
        g2.setStroke(new BasicStroke(3f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 28, 28);
        g2.setStroke(new BasicStroke(1f));

        // Sao hai bên
        drawStar(g2, panelX + 34, panelY + 42, 16, new Color(255, 240, 60));
        drawStar(g2, panelX + panelW - 34, panelY + 42, 16, new Color(255, 240, 60));

        // Tiêu đề
        g2.setFont(new Font("Arial", Font.BOLD, 28));
        g2.setColor(Color.WHITE);
        drawCentered(g2, "HOAN THANH MAN " + level + "!", W, panelY + 55);

        // Dòng phụ
        g2.setFont(new Font("Arial", Font.BOLD, 17));
        boolean isLast = (level >= maxLevel);
        String sub = isLast ? "Ban da pha dao toan bo game!" : "Chuan bi sang Man " + (level+1) + " / " + maxLevel;
        g2.setColor(new Color(255, 255, 190));
        drawCentered(g2, sub, W, panelY + 88);

        // +500 điểm
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(new Color(255, 255, 80));
        drawCentered(g2, "+500 diem thuong!", W, panelY + 118);

        // Thanh tiến trình
        int barW = panelW - 70, barX = panelX + 35, barY = panelY + 138, barH = 16;
        g2.setColor(new Color(0, 0, 0, 100));    g2.fillRoundRect(barX, barY, barW, barH, barH, barH);
        g2.setColor(new Color(80, 220, 80));     g2.fillRoundRect(barX, barY, (int)(barW*progress), barH, barH, barH);
        g2.setColor(new Color(150, 255, 150));
        g2.setStroke(new BasicStroke(1.5f));     g2.drawRoundRect(barX, barY, barW, barH, barH, barH);
        g2.setStroke(new BasicStroke(1f));

        // Chú thích
        g2.setFont(new Font("Arial", Font.PLAIN, 13));
        g2.setColor(new Color(255, 255, 200));
        String hint = isLast ? "Dang luu ky luc..." : "Dang tai man tiep theo...";
        drawCentered(g2, hint, W, barY + barH + 20);

        // Số giây còn lại
        int secsLeft = (int)Math.ceil((1f - progress) * 6);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(255, 230, 100));
        drawCentered(g2, "Chuyen man sau " + secsLeft + "s...", W, barY + barH + 40);
    }

    // ── PAUSE ─────────────────────────────────────────────────────────
    private void drawPauseOverlay(Graphics2D g2) {
        int W = 800, H = 600;
        g2.setColor(new Color(0, 0, 30, 170)); g2.fillRect(0, 0, W, H);

        int panelW = 360, panelH = 280;
        int panelX = (W-panelW)/2, panelY = (H-panelH)/2;

        g2.setColor(new Color(10, 20, 60, 230));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 24, 24);
        g2.setColor(new Color(100, 160, 255));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 24, 24);
        g2.setStroke(new BasicStroke(1f));

        // Icon pause
        g2.setColor(new Color(100, 180, 255));
        g2.fillRoundRect(W/2-22, panelY+28, 14, 36, 4, 4);
        g2.fillRoundRect(W/2+8,  panelY+28, 14, 36, 4, 4);

        g2.setFont(new Font("Arial", Font.BOLD, 36));
        g2.setColor(new Color(180, 220, 255));
        drawCentered(g2, "TAM DUNG", W, panelY + 108);

        g2.setColor(new Color(100, 160, 255, 80));
        g2.fillRect(panelX+30, panelY+118, panelW-60, 1);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.setColor(new Color(180, 210, 255));
        drawCentered(g2, "Nhan ESC / P de tiep tuc", W, panelY+152);

        g2.setFont(new Font("Arial", Font.PLAIN, 15));
        g2.setColor(new Color(140, 180, 220));
        drawCentered(g2, "Nhan ENTER de tiep tuc", W, panelY+178);

        // Thông tin nhanh
        g2.setColor(new Color(0,0,0,80));
        g2.fillRoundRect(panelX+20, panelY+200, panelW-40, 60, 10, 10);

        PlayerModel p = controller.getPlayer();
        g2.setFont(new Font("Arial", Font.BOLD, 14));
        g2.setColor(Color.YELLOW);
        g2.drawString("Score: " + p.getScore(), panelX+36, panelY+224);
        g2.setColor(new Color(255,120,120));
        StringBuilder hh = new StringBuilder();
        for (int i = 0; i < p.getLives(); i++) hh.append("\u2764 ");
        g2.drawString(hh.toString(), panelX+36, panelY+245);
        g2.setColor(Color.CYAN);
        g2.drawString("Man: " + controller.getCurrentLevel() + "/" + controller.getMaxLevel(), panelX+200, panelY+224);
        int s = controller.getElapsedSeconds();
        g2.setColor(new Color(180,255,180));
        g2.drawString(String.format("TG: %02d:%02d", s/60, s%60), panelX+200, panelY+245);
    }

    // ── GAME OVER ─────────────────────────────────────────────────────
    private void drawGameOverOverlay(Graphics2D g2) {
        int W = 800, H = 600;
        float progress = controller.getGameOverProgress();

        g2.setColor(new Color(0, 0, 0, 160)); g2.fillRect(0, 0, W, H);

        int panelW = 420, panelH = 210;
        int panelX = (W-panelW)/2, panelY = (H-panelH)/2-20;

        g2.setColor(new Color(40,10,10,220));
        g2.fillRoundRect(panelX, panelY, panelW, panelH, 24, 24);
        g2.setColor(new Color(180,50,50));
        g2.setStroke(new BasicStroke(2.5f));
        g2.drawRoundRect(panelX, panelY, panelW, panelH, 24, 24);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(new Font("Arial", Font.BOLD, 52));
        g2.setColor(new Color(220, 50, 50));
        drawCentered(g2, "GAME OVER", W, panelY + 70);

        g2.setFont(new Font("Arial", Font.BOLD, 22));
        g2.setColor(Color.WHITE);
        drawCentered(g2, "Score: " + controller.getPlayer().getScore(), W, panelY + 108);

        // Thanh đếm ngược về menu
        int barW = panelW-80, barX = panelX+40, barY = panelY+128, barH = 12;
        g2.setColor(new Color(80,20,20));    g2.fillRoundRect(barX, barY, barW, barH, barH, barH);
        g2.setColor(new Color(220,60,60));   g2.fillRoundRect(barX, barY, (int)(barW*(1f-progress)), barH, barH, barH);
        g2.setColor(new Color(255,120,120,120));
        g2.setStroke(new BasicStroke(1.2f)); g2.drawRoundRect(barX, barY, barW, barH, barH, barH);
        g2.setStroke(new BasicStroke(1f));

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.setColor(new Color(180,180,180));
        drawCentered(g2, "Nhan ENTER de choi lai  |  ESC de ve menu", W, barY+barH+22);

        int secsLeft = (int)Math.ceil((1f-progress) * 10);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(new Color(200,100,100));
        drawCentered(g2, "Ve menu sau " + secsLeft + "s...", W, barY+barH+42);
    }

    private void drawCentered(Graphics2D g2, String text, int width, int y) {
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(text, (width - fm.stringWidth(text)) / 2, y);
    }
}