package controller;

import java.util.List;
import model.*;

public class LevelManager {

    private int currentLevel = 1;
    private static final int MAX_LEVEL = 6;

    //                                        1     2     3     4     5     6
    private static final int[]    SPAWN_INTERVALS = {0, 150, 100,  60,  45,  30,  20};
    private static final double[] OBS_SPEED_MIN   = {0, 2.5, 3.5, 5.0, 5.5, 6.5, 8.0};
    private static final double[] OBS_SPEED_MAX   = {0, 3.5, 5.0, 7.5, 8.0, 9.5,12.0};
    private static final int[]    GROUND_Y        = {0, 440, 440, 440, 440, 440, 440};

    public int     getCurrentLevel()  { return currentLevel; }
    public int     getMaxLevel()      { return MAX_LEVEL; }
    public boolean isLastLevel()      { return currentLevel >= MAX_LEVEL; }
    public int     getSpawnInterval() { return SPAWN_INTERVALS[currentLevel]; }
    public double  getObsSpeedMin()   { return OBS_SPEED_MIN[currentLevel]; }
    public double  getObsSpeedMax()   { return OBS_SPEED_MAX[currentLevel]; }
    public int     getGroundY()       { return GROUND_Y[currentLevel]; }

    public void nextLevel() { if (currentLevel < MAX_LEVEL) currentLevel++; }
    public void reset()     { currentLevel = 1; }

    public void loadLevel(List<PlatformModel> platforms,
                          List<CoinModel>     coins,
                          List<StairModel>    stairs,
                          List<GoldModel>     golds) {
        platforms.clear();
        coins.clear();
        stairs.clear();
        golds.clear();

        switch (currentLevel) {
            case 1: loadLevel1(platforms, coins, stairs, golds); break;
            case 2: loadLevel2(platforms, coins, stairs, golds); break;
            case 3: loadLevel3(platforms, coins, stairs, golds); break;
            case 4: loadLevel4(platforms, coins, stairs, golds); break;
            case 5: loadLevel5(platforms, coins, stairs, golds); break;
            case 6: loadLevel6(platforms, coins, stairs, golds); break;
        }
    }

    // =================================================================
    //  LEVEL 1 — Dễ: làm quen di chuyển & nhảy
    // =================================================================
    private void loadLevel1(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        addRow(platforms, 300, 350, 3, 0);
        addRow(platforms, 600, 300, 2, 0);
        platforms.add(new PlatformModel(500, 300, 40, 40, 1));
        platforms.add(new PlatformModel(720, 250, 40, 40, 1));
        platforms.add(new PlatformModel(900,  350, 60, 80, 2));
        platforms.add(new PlatformModel(1600, 350, 60, 80, 2));

        addCoins(coins, 310, 310, 3);
        addCoins(coins, 610, 260, 2);

        addStairs(stairs, 1100, 430, 4, true);
        golds.add(new GoldModel(1238, 305));
        golds.add(new GoldModel(1278, 305));
        addStairs(stairs, 1310, 430, 4, false);
    }

    // =================================================================
    //  LEVEL 2 — Trung bình: platform cao hơn, nhiều ống cống hơn
    // =================================================================
    private void loadLevel2(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        addRow(platforms, 200, 370, 4, 0);
        addRow(platforms, 550, 320, 3, 0);
        addRow(platforms, 900, 280, 2, 0);
        addRow(platforms, 1200, 340, 3, 0);
        platforms.add(new PlatformModel(400,  270, 40, 40, 1));
        platforms.add(new PlatformModel(700,  220, 40, 40, 1));
        platforms.add(new PlatformModel(1000, 180, 40, 40, 1));
        platforms.add(new PlatformModel(800,  350, 60, 80, 2));
        platforms.add(new PlatformModel(1500, 350, 60, 80, 2));
        platforms.add(new PlatformModel(2100, 310, 60, 120, 2));

        addCoins(coins, 210, 330, 4);
        addCoins(coins, 560, 280, 3);

        addStairs(stairs, 500, 430, 5, true);
        golds.add(new GoldModel(698, 258));
        golds.add(new GoldModel(738, 258));
        golds.add(new GoldModel(778, 258));

        addStairs(stairs, 1700, 430, 4, true);
        golds.add(new GoldModel(1858, 298));
        golds.add(new GoldModel(1898, 298));
        addStairs(stairs, 1950, 430, 4, false);
    }

    // =================================================================
    //  LEVEL 3 — Khó: platform hẹp, nhiều ống cống, bậc thang cao
    // =================================================================
    private void loadLevel3(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        addRow(platforms, 150,  380, 2, 0);
        addRow(platforms, 400,  330, 1, 0);
        addRow(platforms, 600,  280, 2, 0);
        addRow(platforms, 850,  340, 1, 0);
        addRow(platforms, 1050, 290, 2, 0);
        platforms.add(new PlatformModel(250,  280, 40, 40, 1));
        platforms.add(new PlatformModel(500,  230, 40, 40, 1));
        platforms.add(new PlatformModel(750,  190, 40, 40, 1));
        platforms.add(new PlatformModel(700,  310, 60, 120, 2));
        platforms.add(new PlatformModel(1200, 310, 60, 120, 2));
        platforms.add(new PlatformModel(1800, 290, 60, 140, 2));
        platforms.add(new PlatformModel(2400, 290, 60, 140, 2));

        addCoins(coins, 160, 340, 2);
        addCoins(coins, 610, 240, 2);

        addStairs(stairs, 400, 430, 6, true);
        golds.add(new GoldModel(638, 218));
        golds.add(new GoldModel(678, 218));
        golds.add(new GoldModel(718, 218));
        golds.add(new GoldModel(758, 218));

        addStairs(stairs, 1350, 430, 5, true);
        golds.add(new GoldModel(1548, 258));
        golds.add(new GoldModel(1588, 258));
        golds.add(new GoldModel(1628, 258));
        addStairs(stairs, 1650, 430, 5, false);

        addStairs(stairs, 2200, 430, 4, true);
        golds.add(new GoldModel(2358, 298));
        golds.add(new GoldModel(2398, 298));
    }

    // =================================================================
    //  LEVEL 4 — Mê cung platform hẹp + nhiều ống cống sát nhau
    //  Chủ đề: "Khu rừng chướng ngại" — nhảy liên tục, tránh ống cống
    // =================================================================
    private void loadLevel4(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        // -- Platform nhỏ xen kẽ nhau (chỉ 1 ô mỗi cái) --
        addRow(platforms, 200,  390, 2, 0);
        addRow(platforms, 380,  350, 1, 0);
        addRow(platforms, 520,  310, 1, 0);
        addRow(platforms, 660,  270, 2, 0);
        addRow(platforms, 860,  310, 1, 0);
        addRow(platforms, 1000, 360, 2, 0);
        addRow(platforms, 1200, 300, 1, 0);
        addRow(platforms, 1380, 250, 2, 0);
        addRow(platforms, 1620, 290, 1, 0);
        addRow(platforms, 1800, 340, 2, 0);
        addRow(platforms, 2050, 300, 2, 0);
        addRow(platforms, 2300, 260, 1, 0);
        addRow(platforms, 2500, 310, 2, 0);

        // -- Ống cống (type=2) đặt dày hơn level trước --
        platforms.add(new PlatformModel(460,  350, 60,  80, 2));
        platforms.add(new PlatformModel(780,  350, 60,  90, 2));
        platforms.add(new PlatformModel(1100, 340, 60, 100, 2));
        platforms.add(new PlatformModel(1560, 350, 60,  80, 2));
        platforms.add(new PlatformModel(1940, 350, 60,  90, 2));
        platforms.add(new PlatformModel(2200, 330, 60, 110, 2));
        platforms.add(new PlatformModel(2680, 340, 60,  90, 2));

        // -- Coin trên các platform --
        addCoins(coins, 200,  350, 2);
        addCoins(coins, 660,  230, 2);
        addCoins(coins, 1380, 210, 2);
        addCoins(coins, 2050, 260, 2);
        addCoins(coins, 2500, 270, 2);

        // -- Bậc thang 1 (6 bậc) --
        addStairs(stairs, 300, 430, 6, true);
        golds.add(new GoldModel(498, 218));
        golds.add(new GoldModel(538, 218));
        golds.add(new GoldModel(578, 218));

        // -- Bậc thang 2 (5 bậc) --
        addStairs(stairs, 1700, 430, 5, true);
        golds.add(new GoldModel(1898, 238));
        golds.add(new GoldModel(1938, 238));
        golds.add(new GoldModel(1978, 238));
        addStairs(stairs, 2000, 430, 5, false);

        // -- Bậc thang 3 gần cuối (6 bậc) --
        addStairs(stairs, 2600, 430, 6, true);
        golds.add(new GoldModel(2838, 218));
        golds.add(new GoldModel(2878, 218));
    }

    // =================================================================
    //  LEVEL 5 — Vực sâu: khoảng cách giữa các platform rất rộng
    //  Chủ đề: "Vực thẳm" — phải nhảy xa, sai là rơi chết
    // =================================================================
    private void loadLevel5(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        // -- Platform thưa, khoảng cách lớn --
        addRow(platforms, 100,  400, 2, 0);   // start
        addRow(platforms, 320,  370, 1, 0);   // nhảy 1
        addRow(platforms, 500,  330, 1, 0);   // nhảy 2
        addRow(platforms, 680,  290, 2, 0);   // nhảy 3
        addRow(platforms, 920,  320, 1, 0);   // nhảy 4
        addRow(platforms, 1100, 360, 1, 0);   // nhảy 5
        addRow(platforms, 1300, 310, 2, 0);   // nhảy 6
        addRow(platforms, 1580, 270, 1, 0);   // nhảy cao
        addRow(platforms, 1800, 300, 2, 0);   // hạ xuống
        addRow(platforms, 2080, 340, 1, 0);
        addRow(platforms, 2280, 290, 2, 0);
        addRow(platforms, 2560, 250, 1, 0);   // platform cao nhất
        addRow(platforms, 2760, 310, 2, 0);   // xuống dần

        // -- Ống cống ít hơn nhưng cao hơn (khó né hơn) --
        platforms.add(new PlatformModel(420,  340, 60, 100, 2));
        platforms.add(new PlatformModel(820,  320, 60, 120, 2));
        platforms.add(new PlatformModel(1200, 340, 60, 100, 2));
        platforms.add(new PlatformModel(1700, 350, 60,  90, 2));
        platforms.add(new PlatformModel(2180, 330, 60, 110, 2));
        platforms.add(new PlatformModel(2660, 330, 60, 110, 2));

        // -- Coin --
        addCoins(coins, 320,  330, 1);
        addCoins(coins, 680,  250, 2);
        addCoins(coins, 1300, 270, 2);
        addCoins(coins, 2280, 250, 2);
        addCoins(coins, 2760, 270, 2);

        // -- Bậc thang 1 (5 bậc) --
        addStairs(stairs, 150, 430, 5, true);
        golds.add(new GoldModel(348, 258));
        golds.add(new GoldModel(388, 258));
        golds.add(new GoldModel(428, 258));

        // -- Bậc thang 2 (7 bậc — cao nhất game) --
        addStairs(stairs, 1400, 430, 7, true);
        golds.add(new GoldModel(1678, 178));
        golds.add(new GoldModel(1718, 178));
        golds.add(new GoldModel(1758, 178));
        golds.add(new GoldModel(1798, 178));
        addStairs(stairs, 1840, 430, 7, false);

        // -- Bậc thang 3 (5 bậc) gần cuối --
        addStairs(stairs, 2650, 430, 5, true);
        golds.add(new GoldModel(2848, 238));
        golds.add(new GoldModel(2888, 238));
        golds.add(new GoldModel(2928, 238));
    }

    // =================================================================
    //  LEVEL 6 — Boss Level: tất cả khó nhất, obstacle cực nhanh
    //  Chủ đề: "Pháo đài bóng tối" — platform cực hẹp, ống cống khắp nơi
    // =================================================================
    private void loadLevel6(List<PlatformModel> platforms, List<CoinModel> coins,
                            List<StairModel> stairs, List<GoldModel> golds) {
        // -- Platform rất nhỏ, xen kẽ sát ống cống --
        addRow(platforms, 100,  400, 1, 0);
        addRow(platforms, 260,  370, 1, 0);
        addRow(platforms, 420,  340, 1, 0);
        addRow(platforms, 580,  300, 1, 0);
        addRow(platforms, 740,  260, 1, 0);
        addRow(platforms, 900,  300, 1, 0);
        addRow(platforms, 1060, 340, 1, 0);
        addRow(platforms, 1220, 290, 1, 0);
        addRow(platforms, 1420, 250, 1, 0);
        addRow(platforms, 1620, 280, 1, 0);
        addRow(platforms, 1820, 320, 1, 0);
        addRow(platforms, 2020, 270, 1, 0);
        addRow(platforms, 2220, 230, 1, 0);  // đỉnh cao nhất
        addRow(platforms, 2420, 270, 1, 0);
        addRow(platforms, 2620, 310, 1, 0);
        addRow(platforms, 2820, 350, 2, 0);  // platform cuối rộng hơn

        // -- Ống cống dày đặc, cao, gần nhau --
        platforms.add(new PlatformModel(190,  340, 60,  100, 2));
        platforms.add(new PlatformModel(350,  320, 60,  120, 2));
        platforms.add(new PlatformModel(510,  310, 60,  130, 2));
        platforms.add(new PlatformModel(670,  290, 60,  150, 2));
        platforms.add(new PlatformModel(830,  310, 60,  130, 2));
        platforms.add(new PlatformModel(990,  330, 60,  110, 2));
        platforms.add(new PlatformModel(1150, 300, 60,  140, 2));
        platforms.add(new PlatformModel(1330, 280, 60,  160, 2));
        platforms.add(new PlatformModel(1530, 310, 60,  130, 2));
        platforms.add(new PlatformModel(1730, 330, 60,  110, 2));
        platforms.add(new PlatformModel(1930, 290, 60,  150, 2));
        platforms.add(new PlatformModel(2130, 270, 60,  170, 2));
        platforms.add(new PlatformModel(2330, 290, 60,  150, 2));
        platforms.add(new PlatformModel(2530, 320, 60,  120, 2));
        platforms.add(new PlatformModel(2730, 340, 60,  100, 2));

        // -- Coin ít hơn nhưng ở vị trí khó lấy --
        addCoins(coins, 580,  260, 1);
        addCoins(coins, 740,  220, 1);
        addCoins(coins, 1420, 210, 1);
        addCoins(coins, 2220, 190, 1);
        addCoins(coins, 2820, 310, 2);

        // -- Bậc thang 1 (5 bậc) --
        addStairs(stairs, 200, 430, 5, true);
        golds.add(new GoldModel(398, 258));
        golds.add(new GoldModel(438, 258));
        golds.add(new GoldModel(478, 258));

        // -- Bậc thang 2 (7 bậc — đỉnh boss) --
        addStairs(stairs, 1100, 430, 7, true);
        golds.add(new GoldModel(1378, 178));
        golds.add(new GoldModel(1418, 178));
        golds.add(new GoldModel(1458, 178));
        golds.add(new GoldModel(1498, 178));
        golds.add(new GoldModel(1538, 178));
        addStairs(stairs, 1580, 430, 7, false);

        // -- Bậc thang 3 (6 bậc) gần cuối --
        addStairs(stairs, 2300, 430, 6, true);
        golds.add(new GoldModel(2538, 218));
        golds.add(new GoldModel(2578, 218));
        golds.add(new GoldModel(2618, 218));
        golds.add(new GoldModel(2658, 218));
        addStairs(stairs, 2700, 430, 6, false);
    }

    // ── Tiện ích ──
    private void addRow(List<PlatformModel> platforms, int startX, int y, int count, int type) {
        for (int i = 0; i < count; i++)
            platforms.add(new PlatformModel(startX + i * 40, y, 40, 40, type));
    }

    private void addCoins(List<CoinModel> coins, int startX, int y, int count) {
        for (int i = 0; i < count; i++)
            coins.add(new CoinModel(startX + i * 40, y));
    }

    public void addStairs(List<StairModel> stairs, int startX, int groundY,
                          int steps, boolean goingUp) {
        int tileW = 40;
        int tileH = 40;
        for (int i = 0; i < steps; i++) {
            int bx = startX + i * tileW;
            int bh = goingUp ? tileH * (i + 1) : tileH * (steps - i);
            int by = groundY - bh;
            stairs.add(new StairModel(bx, by, tileW, bh, i));
        }
    }
}