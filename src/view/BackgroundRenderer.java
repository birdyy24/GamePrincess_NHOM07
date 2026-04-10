package view.render;

import java.awt.*;

/**
 * BackgroundRenderer — vẽ nền game theo phong cách Mario.
 * Bao gồm: trời xanh gradient, mây trắng, đồi cỏ xanh, mặt đất gạch nâu.
 */
public class BackgroundRenderer {

    /**
     * Vẽ toàn bộ nền game.
     *
     * @param g2     Graphics2D để vẽ
     * @param width  Chiều rộng panel
     * @param height Chiều cao panel
     * @param camX   Vị trí camera (để scroll nền)
     */
    public void draw(Graphics2D g2, int width, int height, int camX) {
        drawSky(g2, width, height);
        drawClouds(g2, width, camX);
        drawHills(g2, width, height, camX);
        drawGround(g2, width, height, camX);
    }

    /**
     * Gọi hàm này nếu không dùng camera (camX = 0).
     */
    public void draw(Graphics2D g2, int width, int height) {
        draw(g2, width, height, 0);
    }

    // ─────────────────────────────────────────────
    //  TRỜI XANH GRADIENT
    // ─────────────────────────────────────────────

    private void drawSky(Graphics2D g2, int width, int height) {
        // Gradient từ xanh đậm trên → xanh nhạt dưới (giống bầu trời thật)
        GradientPaint sky = new GradientPaint(
                0, 0,      new Color(82, 178, 255),   // xanh trời đậm
                0, height, new Color(173, 223, 255));  // xanh nhạt gần đất
        g2.setPaint(sky);
        g2.fillRect(0, 0, width, height);
    }

    // ─────────────────────────────────────────────
    //  MÂY TRẮNG (parallax 1/3 tốc độ camera)
    // ─────────────────────────────────────────────

    /**
     * Vẽ các đám mây trắng, cuộn nhẹ theo camera.
     * Mây di chuyển chậm hơn nền để tạo hiệu ứng chiều sâu.
     */
    private void drawClouds(Graphics2D g2, int width, int camX) {
        // Vị trí X và Y của từng đám mây trong thế giới
        int[] cloudWorldX = {80,  280, 500, 720, 950, 1180, 1400, 1620, 1850, 2080};
        int[] cloudY      = {55,  85,  50,  100, 65,  88,   55,   95,   70,   60  };

        for (int i = 0; i < cloudWorldX.length; i++) {
            // Parallax: mây di chuyển bằng 1/3 tốc độ camera
            int screenX = cloudWorldX[i] - camX / 3;

            // Wrap để mây xuất hiện liên tục
            int wrapW = width + 300;
            screenX = ((screenX % wrapW) + wrapW) % wrapW - 100;

            drawOneCloud(g2, screenX, cloudY[i]);
        }
    }

    /** Vẽ một đám mây bằng các hình tròn chồng lên nhau */
    private void drawOneCloud(Graphics2D g2, int x, int y) {
        // Thân mây trắng
        g2.setColor(Color.WHITE);
        g2.fillOval(x,      y + 18, 66, 34);
        g2.fillOval(x + 16, y,      56, 40);
        g2.fillOval(x + 40, y + 10, 58, 34);

        // Bóng mờ xanh nhạt phía dưới mây
        g2.setColor(new Color(195, 228, 255, 110));
        g2.fillOval(x + 8,  y + 24, 58, 24);
    }

    // ─────────────────────────────────────────────
    //  ĐỒI CỎ XANH (parallax 1/2 tốc độ camera)
    // ─────────────────────────────────────────────

    /**
     * Vẽ các ngọn đồi cỏ xanh ở phía trước mây, sau mặt đất.
     */
    private void drawHills(Graphics2D g2, int width, int height, int camX) {
        int groundY = 430; // Y bắt đầu mặt đất

        // Vị trí X và chiều rộng từng ngọn đồi trong thế giới
        int[] hillWorldX = {0,   200, 480, 730, 1010, 1310, 1580, 1860};
        int[] hillWidth  = {170, 130, 190, 150, 175,  135,  165,  150 };

        for (int i = 0; i < hillWorldX.length; i++) {
            // Parallax: đồi di chuyển bằng 1/2 tốc độ camera
            int sx = hillWorldX[i] - camX / 2;
            int hw = hillWidth[i];

            // Bỏ qua nếu ngoài màn hình
            if (sx + hw * 2 < 0 || sx > width) continue;

            // Đồi xanh đậm (hình bán nguyệt)
            g2.setColor(new Color(46, 125, 50));
            g2.fillArc(sx, groundY - hw / 2, hw * 2, hw + 20, 0, 180);

            // Highlight xanh nhạt trên đỉnh đồi
            g2.setColor(new Color(102, 187, 106, 160));
            g2.fillArc(sx + hw / 4, groundY - hw / 2 + 14, hw, hw / 2, 10, 160);
        }
    }

    // ─────────────────────────────────────────────
    //  MẶT ĐẤT GẠCH NÂU (cuộn theo camera)
    // ─────────────────────────────────────────────

    /**
     * Vẽ mặt đất gồm lớp cỏ xanh trên cùng và gạch nâu bên dưới.
     * Gạch có texture đường kẻ ngang + dọc lệch nhau giống Mario.
     */
    private void drawGround(Graphics2D g2, int width, int height, int camX) {
        int groundY = 430;  // Y bắt đầu mặt đất
        int tileW   = 40;   // kích thước một viên gạch

        // ── Lớp cỏ xanh trên đỉnh đất ──
        g2.setColor(new Color(66, 165, 72));
        g2.fillRect(0, groundY, width, 10);

        // Viền dưới cỏ (đậm hơn)
        g2.setColor(new Color(46, 125, 50));
        g2.fillRect(0, groundY + 8, width, 4);

        // ── Gạch đất nâu có texture ──
        // Tính offset để gạch cuộn cùng camera
        int offsetX = camX % tileW;

        for (int tx = -offsetX; tx < width + tileW; tx += tileW) {
            for (int ty = groundY + 12; ty < height; ty += tileW) {
                drawBrickTile(g2, tx, ty, tileW, tileW);
            }
        }
    }

    /**
     * Vẽ một viên gạch nâu với đường texture ngang + dọc.
     *
     * @param x Tọa độ X
     * @param y Tọa độ Y
     * @param w Chiều rộng
     * @param h Chiều cao
     */
    private void drawBrickTile(Graphics2D g2, int x, int y, int w, int h) {
        // Nền nâu chính
        g2.setColor(new Color(139, 90, 43));
        g2.fillRect(x, y, w, h);

        // Đường ngang giữa viên gạch
        g2.setColor(new Color(101, 67, 33));
        g2.fillRect(x, y + h / 2, w, 2);

        // Đường dọc lệch nhau tạo hiệu ứng gạch xây
        g2.fillRect(x + w / 2, y,       2, h / 2);
        g2.fillRect(x + w / 4, y + h/2, 2, h / 2);

        // Viền ngoài viên gạch
        g2.setColor(new Color(80, 50, 20));
        g2.drawRect(x, y, w - 1, h - 1);

        // Highlight nhỏ góc trên trái (ánh sáng)
        g2.setColor(new Color(180, 120, 60, 80));
        g2.fillRect(x + 2, y + 2, w / 4, 3);
    }
}