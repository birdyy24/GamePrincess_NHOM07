package view.render;

import java.awt.*;

/**
 * MenuRenderer — vẽ màn hình MENU chính theo phong cách pixel/Mario.
 * Có nút PLAY, chọn độ khó EASY / MEDIUM / HARD, và hiệu ứng động.
 */
public class MenuRenderer {

    // ── Độ khó ──
    public enum Difficulty { EASY, MEDIUM, HARD }

    private Difficulty selectedDifficulty = Difficulty.EASY;

    private long       animTick           = 0;

    // Vị trí các nút (tính sau khi draw)
    private Rectangle btnEasy, btnMedium, btnHard, btnPlay;

    // ──────────────────────────────────────────────
    //  ENTRY POINT
    // ──────────────────────────────────────────────

    public void draw(Graphics2D g2, int width, int height) {
        animTick++;

        drawBackground(g2, width, height);
        drawTitle(g2, width);
        drawDifficultyPanel(g2, width, height);
        drawPlayButton(g2, width, height);
        drawFooter(g2, width, height);
    }

    // ──────────────────────────────────────────────
    //  BACKGROUND — bầu trời + mây + đất giống game
    // ──────────────────────────────────────────────

    private void drawBackground(Graphics2D g2, int width, int height) {
        // Bầu trời gradient
        GradientPaint sky = new GradientPaint(
                0, 0,      new Color(82, 178, 255),
                0, height, new Color(173, 223, 255));
        g2.setPaint(sky);
        g2.fillRect(0, 0, width, height);

        // Mây trang trí
        drawCloud(g2, 60,  60);
        drawCloud(g2, 560, 45);
        drawCloud(g2, 280, 90);
        drawCloud(g2, 680, 80);

        // Đồi cỏ dưới
        g2.setColor(new Color(66, 165, 72));
        g2.fillOval(-60, height - 120, 280, 160);
        g2.fillOval(580, height - 100, 320, 140);

        // Mặt đất
        g2.setColor(new Color(66, 165, 72));
        g2.fillRect(0, height - 70, width, 12);
        g2.setColor(new Color(46, 125, 50));
        g2.fillRect(0, height - 60, width, 4);
        g2.setColor(new Color(139, 90, 43));
        g2.fillRect(0, height - 56, width, 56);

        // Texture gạch đất
        g2.setColor(new Color(101, 67, 33));
        for (int x = 0; x < width; x += 40) {
            g2.drawLine(x, height - 56, x, height);
        }
        g2.drawLine(0, height - 36, width, height - 36);
        g2.drawLine(0, height - 16, width, height - 16);
    }

    private void drawCloud(Graphics2D g2, int x, int y) {
        g2.setColor(Color.WHITE);
        g2.fillOval(x,      y + 18, 66, 34);
        g2.fillOval(x + 16, y,      56, 40);
        g2.fillOval(x + 40, y + 10, 58, 34);
        g2.setColor(new Color(195, 228, 255, 110));
        g2.fillOval(x + 8,  y + 24, 58, 24);
    }

    // ──────────────────────────────────────────────
    //  TIÊU ĐỀ — "Princess Adventure" pixel style
    // ──────────────────────────────────────────────

    private void drawTitle(Graphics2D g2, int width) {
        // Nền biển hiệu gỗ
        int bx = width / 2 - 280, by = 30, bw = 560, bh = 100;
        drawWoodenPanel(g2, bx, by, bw, bh);

        // Chữ PRINCESS
        g2.setFont(new Font("Monospaced", Font.BOLD, 38));
        String title1 = "PRINCESS";
        int tw1 = g2.getFontMetrics().stringWidth(title1);

        // Shadow chữ
        g2.setColor(new Color(80, 30, 0));
        g2.drawString(title1, width / 2 - tw1 / 2 + 3, by + 53);
        // Chữ chính màu vàng sáng
        g2.setColor(new Color(255, 230, 50));
        g2.drawString(title1, width / 2 - tw1 / 2, by + 50);

        // Chữ ADVENTURE
        g2.setFont(new Font("Monospaced", Font.BOLD, 28));
        String title2 = "ADVENTURE  \u2728";
        int tw2 = g2.getFontMetrics().stringWidth(title2);
        g2.setColor(new Color(80, 30, 0));
        g2.drawString(title2, width / 2 - tw2 / 2 + 2, by + 87);
        g2.setColor(new Color(255, 200, 80));
        g2.drawString(title2, width / 2 - tw2 / 2, by + 84);
    }

    // ──────────────────────────────────────────────
    //  PANEL CHỌN ĐỘ KHÓ
    // ──────────────────────────────────────────────

    private void drawDifficultyPanel(Graphics2D g2, int width, int height) {
        int panelW = 340, panelH = 230;
        int panelX = width / 2 - panelW / 2;
        int panelY = 160;

        // Khung gỗ chính
        drawWoodenPanel(g2, panelX, panelY, panelW, panelH);

        // Chữ "DIFFICULTY"
        g2.setFont(new Font("Monospaced", Font.BOLD, 20));
        String diff = "- DIFFICULTY -";
        int dw = g2.getFontMetrics().stringWidth(diff);
        g2.setColor(new Color(80, 40, 0));
        g2.drawString(diff, width / 2 - dw / 2 + 2, panelY + 34);
        g2.setColor(new Color(255, 220, 100));
        g2.drawString(diff, width / 2 - dw / 2, panelY + 32);

        // 3 nút độ khó
        int btnW = 240, btnH = 44, btnX = width / 2 - btnW / 2;

        btnEasy   = new Rectangle(btnX, panelY + 50,  btnW, btnH);
        btnMedium = new Rectangle(btnX, panelY + 104, btnW, btnH);
        btnHard   = new Rectangle(btnX, panelY + 158, btnW, btnH);

        drawDiffButton(g2, btnEasy,   "EASY",   Difficulty.EASY,   new Color(76, 175, 80));
        drawDiffButton(g2, btnMedium, "MEDIUM", Difficulty.MEDIUM, new Color(255, 165, 0));
        drawDiffButton(g2, btnHard,   "HARD",   Difficulty.HARD,   new Color(211, 47, 47));
    }

    private void drawDiffButton(Graphics2D g2, Rectangle r,
                                 String label, Difficulty diff, Color baseColor) {
        boolean selected = (selectedDifficulty == diff);

        // Màu nền nút
        Color bg = selected
                ? baseColor
                : new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 100);

        // Nền nút bo góc
        g2.setColor(new Color(50, 25, 0, 160));
        g2.fillRoundRect(r.x + 3, r.y + 3, r.width, r.height, 10, 10);
        g2.setColor(bg);
        g2.fillRoundRect(r.x, r.y, r.width, r.height, 10, 10);

        // Viền trắng nếu selected
        if (selected) {
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
            g2.setStroke(new BasicStroke(1));
        } else {
            g2.setColor(new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), 180));
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(r.x, r.y, r.width, r.height, 10, 10);
            g2.setStroke(new BasicStroke(1));
        }

        // Icon + label
        String icon = diff == Difficulty.EASY ? "\u2665 " :
                      diff == Difficulty.MEDIUM ? "\u2605 " : "\u2620 ";
        g2.setFont(new Font("Monospaced", Font.BOLD, 18));
        String text = icon + label;
        int tw = g2.getFontMetrics().stringWidth(text);
        // Shadow
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(text, r.x + r.width / 2 - tw / 2 + 2, r.y + 28);
        // Chữ
        g2.setColor(selected ? Color.WHITE : new Color(240, 240, 240));
        g2.drawString(text, r.x + r.width / 2 - tw / 2, r.y + 26);

        // Dấu ✓ nếu selected
        if (selected) {
            g2.setFont(new Font("Monospaced", Font.BOLD, 16));
            g2.setColor(Color.WHITE);
            g2.drawString("\u2713", r.x + r.width - 28, r.y + 26);
        }
    }

    // ──────────────────────────────────────────────
    //  NÚT PLAY — nảy lên xuống
    // ──────────────────────────────────────────────

    private void drawPlayButton(Graphics2D g2, int width, int height) {
        // Hiệu ứng nảy sin
        double bounce = Math.sin(animTick * 0.07) * 5;

        int bw = 200, bh = 56;
        int bx = width / 2 - bw / 2;
        int by = (int)(415 + bounce);
        btnPlay = new Rectangle(bx, by, bw, bh);

        // Shadow
        g2.setColor(new Color(0, 0, 0, 80));
        g2.fillRoundRect(bx + 4, by + 4, bw, bh, 14, 14);

        // Nền xanh lá đậm
        GradientPaint btnGrad = new GradientPaint(
                bx, by,      new Color(56, 210, 56),
                bx, by + bh, new Color(30, 140, 30));
        g2.setPaint(btnGrad);
        g2.fillRoundRect(bx, by, bw, bh, 14, 14);

        // Viền trắng
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(3));
        g2.drawRoundRect(bx, by, bw, bh, 14, 14);
        g2.setStroke(new BasicStroke(1));

        // Highlight trên
        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRoundRect(bx + 4, by + 4, bw - 8, bh / 2 - 4, 10, 10);

        // Chữ ▶ PLAY
        g2.setFont(new Font("Monospaced", Font.BOLD, 24));
        String playTxt = "\u25B6  PLAY";
        int tw = g2.getFontMetrics().stringWidth(playTxt);
        g2.setColor(new Color(0, 80, 0, 150));
        g2.drawString(playTxt, bx + bw / 2 - tw / 2 + 2, by + 36);
        g2.setColor(Color.WHITE);
        g2.drawString(playTxt, bx + bw / 2 - tw / 2, by + 34);
    }

    // ──────────────────────────────────────────────
    //  FOOTER
    // ──────────────────────────────────────────────

    private void drawFooter(Graphics2D g2, int width, int height) {
        g2.setFont(new Font("Monospaced", Font.PLAIN, 13));
        String hint = "[ \u2190 \u2192 ] Di chuyen   [ SPACE ] Nhay";
        int hw = g2.getFontMetrics().stringWidth(hint);
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString(hint, width / 2 - hw / 2 + 1, height - 68);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.drawString(hint, width / 2 - hw / 2, height - 70);
    }

    // ──────────────────────────────────────────────
    //  HELPER — khung gỗ pixel
    // ──────────────────────────────────────────────

    private void drawWoodenPanel(Graphics2D g2, int x, int y, int w, int h) {
        // Shadow
        g2.setColor(new Color(0, 0, 0, 100));
        g2.fillRoundRect(x + 5, y + 5, w, h, 16, 16);

        // Nền gỗ nâu
        GradientPaint wood = new GradientPaint(
                x, y,     new Color(180, 110, 40),
                x, y + h, new Color(140, 80, 20));
        g2.setPaint(wood);
        g2.fillRoundRect(x, y, w, h, 16, 16);

        // Vân gỗ
        g2.setColor(new Color(160, 95, 30, 80));
        for (int i = 0; i < h; i += 18) {
            g2.drawLine(x + 10, y + i, x + w - 10, y + i + 4);
        }

        // Viền ngoài đậm
        g2.setColor(new Color(90, 50, 10));
        g2.setStroke(new BasicStroke(4));
        g2.drawRoundRect(x, y, w, h, 16, 16);
        g2.setStroke(new BasicStroke(1));

        // Viền trong sáng (highlight)
        g2.setColor(new Color(220, 160, 70, 120));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(x + 4, y + 4, w - 8, h - 8, 12, 12);
        g2.setStroke(new BasicStroke(1));

        // Đinh 4 góc
        int[] nailOffsets = {8, w - 16};
        int[] nailOffsetsY = {8, h - 16};
        for (int nx : nailOffsets) {
            for (int ny : nailOffsetsY) {
                g2.setColor(new Color(180, 140, 60));
                g2.fillOval(x + nx, y + ny, 8, 8);
                g2.setColor(new Color(100, 70, 20));
                g2.drawOval(x + nx, y + ny, 8, 8);
                g2.setColor(new Color(220, 200, 100, 150));
                g2.fillOval(x + nx + 2, y + ny + 2, 3, 3);
            }
        }
    }

    // ──────────────────────────────────────────────
    //  MOUSE INTERACTION
    // ──────────────────────────────────────────────

    /**
     * Gọi khi mouse click — trả về true nếu nhấn PLAY.
     */
    public boolean handleClick(int mx, int my) {
        if (btnEasy   != null && btnEasy.contains(mx, my))   { selectedDifficulty = Difficulty.EASY;   return false; }
        if (btnMedium != null && btnMedium.contains(mx, my)) { selectedDifficulty = Difficulty.MEDIUM; return false; }
        if (btnHard   != null && btnHard.contains(mx, my))   { selectedDifficulty = Difficulty.HARD;   return false; }
        if (btnPlay   != null && btnPlay.contains(mx, my))   { return true; }
        return false;
    }

    /**
     * Gọi khi nhấn ENTER để bắt đầu game ngay.
     */
    public boolean handleKey(int keyCode) {
        return keyCode == java.awt.event.KeyEvent.VK_ENTER;
    }

    public Difficulty getSelectedDifficulty() { return selectedDifficulty; }
}