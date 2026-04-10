package controller;

import model.*;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

public class CollisionManager {

    private final PlayerModel player;
    private final List<PlatformModel> platforms;
    private final List<EnemyModel> enemies;
    private final List<CoinModel> coins;

    // Cooldown để tránh trừ mạng liên tục khi chạm enemy
    private int invincibleTimer = 0;
    private static final int INVINCIBLE_DURATION = 60; // ~1 giây nếu 60 FPS

    public CollisionManager(PlayerModel player,
                            List<PlatformModel> platforms,
                            List<EnemyModel> enemies,
                            List<CoinModel> coins) {
        this.player = player;
        this.platforms = platforms;
        this.enemies = enemies;
        this.coins = coins;
    }

    /**
     * Gọi mỗi frame để giảm bộ đếm bất tử.
     */
    public void update() {
        if (invincibleTimer > 0) {
            invincibleTimer--;
        }
    }

    // ------------------------------------------------------------------ //
    //  Kiểm tra va chạm với mặt đất
    // ------------------------------------------------------------------ //
    public void checkGround(int groundY) {
        if (player.getY() + player.getHeight() >= groundY) {
            player.setY(groundY - player.getHeight());
            player.setVelY(0);
            player.setOnGround(true);
            player.setJumping(false); // FIX: reset trạng thái nhảy
        }
    }

    // ------------------------------------------------------------------ //
    //  Kiểm tra va chạm với platform
    // ------------------------------------------------------------------ //
    public void checkPlatform() {
        // Dùng hitbox hẹp hơn cho player (chỉ phần thân, không kể đầu)
        // để tránh bị "dính" bên hông khi nhảy sát platform
        Rectangle pRect = new Rectangle(
                (int) player.getX() + 4,
                (int) player.getY(),
                player.getWidth() - 8,
                player.getHeight());

        for (PlatformModel pl : platforms) {
            // Chỉ dùng phần trên của platform (dày 4px) làm mặt đứng thực sự
            Rectangle bRect = new Rectangle(
                    pl.getX(), pl.getY(),
                    pl.getWidth(), pl.getHeight());

            if (!pRect.intersects(bRect)) continue;

            if (player.getVelY() > 0) {
                int playerBottom = (int) player.getY() + player.getHeight();
                int platformTop  = pl.getY();

                // Chỉ đáp xuống khi chân player gần mặt trên platform
                if (playerBottom - player.getVelY() <= platformTop + 6) {
                    player.setY(platformTop - player.getHeight());
                    player.setVelY(0);
                    player.setOnGround(true);
                    player.setJumping(false);
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Kiểm tra va chạm với enemy
    // ------------------------------------------------------------------ //
    public void checkEnemy() {
        Rectangle pRect = new Rectangle(
                (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());

        Iterator<EnemyModel> it = enemies.iterator(); // FIX: dùng Iterator để có thể xóa

        while (it.hasNext()) {
            EnemyModel e = it.next();

            if (e == null || !e.isAlive()) continue;

            Rectangle eRect = new Rectangle(
                    (int) e.getX(), (int) e.getY(),
                    e.getWidth(), e.getHeight());

            if (!pRect.intersects(eRect)) continue;

            // FIX: Nhảy lên đầu enemy → tiêu diệt enemy, player nảy lên
            boolean jumpedOnTop = player.getVelY() > 0
                    && (player.getY() + player.getHeight()) <= (e.getY() + e.getHeight() / 2);

            if (jumpedOnTop) {
                e.setAlive(false);          // đánh dấu enemy chết
                it.remove();                // xóa khỏi danh sách
                player.setVelY(-8);         // nảy nhẹ lên sau khi đạp
                player.setScore(player.getScore() + 200); // thưởng điểm
            } else {
                // FIX: chỉ trừ mạng khi hết cooldown (tránh trừ liên tục mỗi frame)
                if (invincibleTimer == 0) {
                    player.setLives(player.getLives() - 1);
                    invincibleTimer = INVINCIBLE_DURATION;
                }
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Kiểm tra va chạm với coin
    // ------------------------------------------------------------------ //
    public void checkCoin() {
        Rectangle pRect = new Rectangle(
                (int) player.getX(), (int) player.getY(),
                player.getWidth(), player.getHeight());

        Iterator<CoinModel> it = coins.iterator();

        while (it.hasNext()) {
            CoinModel c = it.next();

            Rectangle cRect = new Rectangle(c.getX(), c.getY(), 16, 20);

            if (pRect.intersects(cRect)) {
                it.remove();
                player.setScore(player.getScore() + 100);
            }
        }
    }
}