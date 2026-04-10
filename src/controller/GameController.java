package controller;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JOptionPane;
import model.CoinModel;
import model.EnemyModel;
import model.GameState;
import model.GoldModel;
import model.ObstacleModel;
import model.PlatformModel;
import model.PlayerModel;
import model.StairModel;
import model.TrophyModel;
import view.render.MenuRenderer;
import view.render.MenuRenderer.Difficulty;

public class GameController {
   private final PlayerModel player;
   private final List<EnemyModel> enemies;
   private final List<PlatformModel> platforms;
   private final List<CoinModel> coins;
   private final List<ObstacleModel> obstacles;
   private final List<StairModel> stairs;
   private final List<GoldModel> golds;
   private final PlayerController playerController;
   private final EnemyController enemyController;
   private final CollisionManager collisionManager;
   private final LevelManager levelManager;
   private GameState gameState;
   private MenuRenderer.Difficulty selectedDifficulty;
   private int obstacleSpawnTimer;
   private int deathCooldown;
   private static final int DEATH_COOLDOWN_MAX = 90;
   private static final int LEVEL_END_X = 2800;
   private long elapsedFrames;

   // ── CÚP ──────────────────────────────────────────────────────────
   private TrophyModel trophy;
   private int levelCompleteTimer = 0;
   private static final int LEVEL_COMPLETE_DELAY = 360; // 6 giây

   // ── GAME OVER → về menu ──────────────────────────────────────────
   private int gameOverTimer = 0;
   private static final int GAME_OVER_DELAY = 600; // 10 giây

   public GameController() {
      this.gameState = GameState.MENU;
      this.selectedDifficulty = Difficulty.EASY;
      this.obstacleSpawnTimer = 0;
      this.deathCooldown = 0;
      this.elapsedFrames = 0L;
      this.player = new PlayerModel(100.0, 350.0);
      this.enemies = new ArrayList<>();
      this.platforms = new ArrayList<>();
      this.coins = new ArrayList<>();
      this.obstacles = new ArrayList<>();
      this.stairs = new ArrayList<>();
      this.golds = new ArrayList<>();
      this.playerController = new PlayerController(this.player);
      this.enemyController = new EnemyController(this.enemies, this.platforms, 3200);
      this.collisionManager = new CollisionManager(this.player, this.platforms, this.enemies, this.coins);
      this.levelManager = new LevelManager();
      this.levelManager.loadLevel(this.platforms, this.coins, this.stairs, this.golds);
      spawnTrophy();
   }

   // ─────────────────────────────────────────────────────────────────
   public void update() {
      if (this.gameState == GameState.PLAYING) {
         this.playerController.update();
         this.enemyController.update();
         this.collisionManager.update();
         this.collisionManager.checkGround(this.levelManager.getGroundY());
         this.collisionManager.checkPlatform();
         this.collisionManager.checkEnemy();
         this.collisionManager.checkCoin();

         if (this.deathCooldown > 0) --this.deathCooldown;

         this.spawnObstacles();
         this.updateObstacles();
         this.checkStairCollision();
         this.checkPipeCollision();
         this.checkObstacleCollision();
         this.checkGoldCollision();
         this.checkFallDeath();

         // Cập nhật cúp + kiểm tra chạm
         if (trophy != null) trophy.update();
         this.checkTrophyCollision();

         if (this.player.getLives() <= 0) {
            this.handleGameOver("Ban da het luot choi! Nhap ten de luu ky luc:");
         }

         ++this.elapsedFrames;

      } else if (this.gameState == GameState.LEVEL_COMPLETE) {
         // Vẫn animate cúp trong lúc chờ
         if (trophy != null) trophy.update();
         --levelCompleteTimer;
         if (levelCompleteTimer <= 0) proceedToNextLevel();

      } else if (this.gameState == GameState.GAME_OVER) {
         // Đếm ngược về menu
         if (gameOverTimer > 0) {
            --gameOverTimer;
            if (gameOverTimer <= 0) returnToMenu();
         }
      }
      // PAUSED → không update gì cả
   }

   // ── CÚP ──────────────────────────────────────────────────────────
   private void spawnTrophy() {
      trophy = new TrophyModel(LEVEL_END_X - 60, levelManager.getGroundY());
   }

   private void checkTrophyCollision() {
      if (trophy == null || trophy.isCollected()) return;
      Rectangle pRect = new Rectangle((int) player.getX(), (int) player.getY(),
                                       player.getWidth(), player.getHeight());
      Rectangle tRect = new Rectangle((int) trophy.getX(), (int) trophy.getY(),
                                       trophy.getWidth(), trophy.getHeight());
      if (pRect.intersects(tRect)) {
         trophy.setCollected(true);
         player.setScore(player.getScore() + 500); // thưởng 500 điểm
         this.gameState       = GameState.LEVEL_COMPLETE;
         this.levelCompleteTimer = LEVEL_COMPLETE_DELAY;
         // Dừng di chuyển nhân vật
         this.playerController.setLeft(false);
         this.playerController.setRight(false);
      }
   }

   private void proceedToNextLevel() {
      if (this.levelManager.isLastLevel()) {
         this.handleGameOver("Chuc mung! Ban da pha dao! Nhap ten de luu ky luc:");
      } else {
         this.levelManager.nextLevel();
         this.resetForNextLevel();
         this.gameState = GameState.PLAYING;
      }
   }

   // ── PAUSE ─────────────────────────────────────────────────────────
   public void togglePause() {
      if (this.gameState == GameState.PLAYING) {
         this.gameState = GameState.PAUSED;
         this.playerController.setLeft(false);
         this.playerController.setRight(false);
      } else if (this.gameState == GameState.PAUSED) {
         this.gameState = GameState.PLAYING;
      }
   }

   // ── GAME OVER ─────────────────────────────────────────────────────
   private void handleGameOver(String message) {
      this.gameState    = GameState.GAME_OVER;
      this.gameOverTimer = GAME_OVER_DELAY;

      String playerName = JOptionPane.showInputDialog(null,
            message, "Ghi Danh Ky Luc", JOptionPane.QUESTION_MESSAGE);
      String finalName = (playerName != null && !playerName.trim().isEmpty())
            ? playerName.trim() : "Nguoi choi";
      HighScoreDAO.saveGameHistory(finalName, this.player.getScore());
      JOptionPane.showMessageDialog(null,
            "Da luu " + this.player.getScore() + " diem cho " + finalName + "!");
   }

   public void returnToMenu() {
      this.gameState    = GameState.MENU;
      this.gameOverTimer = 0;
   }

   // ── DIFFICULTY ────────────────────────────────────────────────────
   public void applyDifficulty(MenuRenderer.Difficulty var1) {
      this.selectedDifficulty = var1;
      if (var1 == Difficulty.HARD)
         this.obstacleSpawnTimer = this.levelManager.getSpawnInterval() / 2;
   }

   public void startPlaying() {
      this.restartGame();
      this.gameState = GameState.PLAYING;
   }

   // ── OBSTACLES ─────────────────────────────────────────────────────
   private void spawnObstacles() {
      ++this.obstacleSpawnTimer;
      int var1 = this.selectedDifficulty == Difficulty.HARD
            ? (int)((double)this.levelManager.getSpawnInterval() * 0.7)
            : this.levelManager.getSpawnInterval();
      if (this.obstacleSpawnTimer >= var1) {
         this.obstacleSpawnTimer = 0;
         double var2 = this.player.getX() + 900.0;
         double var4 = (double)(this.levelManager.getGroundY() - 50);
         String[] var6 = {"rock", "spike", "fireball"};
         String var7 = var6[(int)(Math.random() * var6.length)];
         double var8 = this.levelManager.getObsSpeedMin();
         double var10 = this.levelManager.getObsSpeedMax();
         if (this.selectedDifficulty == Difficulty.EASY)      { var8 *= 0.8; var10 *= 0.8; }
         else if (this.selectedDifficulty == Difficulty.HARD) { var8 *= 1.2; var10 *= 1.2; }
         double var12 = var8 + Math.random() * (var10 - var8);
         this.obstacles.add(new ObstacleModel(var2, var4, var12, var7));
      }
   }

   private void updateObstacles() {
      Iterator<ObstacleModel> it = this.obstacles.iterator();
      while (it.hasNext()) {
         ObstacleModel o = it.next();
         o.update();
         if (o.getX() < this.player.getX() - 500.0 || !o.isActive()) it.remove();
      }
   }

   // ── COLLISION HELPERS ─────────────────────────────────────────────
   private void checkStairCollision() {
      Rectangle var1 = new Rectangle((int)this.player.getX() + 4, (int)this.player.getY(),
            this.player.getWidth() - 8, this.player.getHeight());
      for (StairModel s : this.stairs) {
         Rectangle var4 = new Rectangle(s.getX(), s.getY(), s.getWidth(), s.getHeight());
         if (var1.intersects(var4) && this.player.getVelY() >= 0.0) {
            int var5 = (int)this.player.getY() + this.player.getHeight();
            if (var5 - (int)this.player.getVelY() <= s.getY() + 6) {
               this.player.setY((double)(s.getY() - this.player.getHeight()));
               this.player.setVelY(0.0);
               this.player.setOnGround(true);
               this.player.setJumping(false);
            }
         }
      }
   }

   private void checkPipeCollision() {
      if (this.deathCooldown > 0) return;
      Rectangle var1 = new Rectangle((int)this.player.getX() + 6, (int)this.player.getY() + 4,
            this.player.getWidth() - 12, this.player.getHeight() - 4);
      for (PlatformModel p : this.platforms) {
         if (p.getType() == 2) {
            Rectangle var4 = new Rectangle(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            if (var1.intersects(var4)) { this.killPlayer(); return; }
         }
      }
   }

   private void checkObstacleCollision() {
      if (this.deathCooldown > 0) return;
      Rectangle var1 = new Rectangle((int)this.player.getX() + 6, (int)this.player.getY() + 4,
            this.player.getWidth() - 12, this.player.getHeight() - 4);
      for (ObstacleModel o : this.obstacles) {
         if (o.isActive()) {
            Rectangle var4 = new Rectangle((int)o.getX() + 4, (int)o.getY() + 4,
                  o.getWidth() - 8, o.getHeight() - 4);
            if (var1.intersects(var4)) { o.setActive(false); this.killPlayer(); return; }
         }
      }
   }

   private void checkGoldCollision() {
      Rectangle var1 = new Rectangle((int)this.player.getX(), (int)this.player.getY(),
            this.player.getWidth(), this.player.getHeight());
      for (GoldModel g : this.golds) {
         if (!g.isCollected()) {
            Rectangle var4 = new Rectangle(g.getX(), g.getY(), g.getWidth(), g.getHeight());
            if (var1.intersects(var4)) {
               g.setCollected(true);
               this.player.setScore(this.player.getScore() + g.getScoreValue());
            }
         }
      }
   }

   private void checkFallDeath() {
      if (this.player.getY() > (double)(this.levelManager.getGroundY() + 100))
         this.killPlayer();
   }

   private void killPlayer() {
      this.player.setLives(this.player.getLives() - 1);
      this.deathCooldown = DEATH_COOLDOWN_MAX;
      if (this.player.getLives() > 0) {
         this.player.setX(100.0); this.player.setY(350.0);
         this.player.setVelX(0.0); this.player.setVelY(0.0);
      }
   }

   private void resetForNextLevel() {
      this.player.setX(100.0); this.player.setY(350.0);
      this.player.setVelX(0.0); this.player.setVelY(0.0);
      this.obstacles.clear(); this.enemies.clear();
      this.obstacleSpawnTimer = 0; this.deathCooldown = 0;
      this.elapsedFrames = 0L; this.levelCompleteTimer = 0;
      this.levelManager.loadLevel(this.platforms, this.coins, this.stairs, this.golds);
      spawnTrophy(); // tạo cúp mới cho màn mới
   }

   private void restartGame() {
      this.levelManager.reset();
      this.player.setLives(3);
      this.player.setScore(0);
      this.resetForNextLevel();
   }

   // ── KEY INPUT ─────────────────────────────────────────────────────
   public void keyPressed(int var1) {
      switch (var1) {
         case 27: // ESC
            if (this.gameState == GameState.PLAYING || this.gameState == GameState.PAUSED)
               this.togglePause();
            else if (this.gameState == GameState.GAME_OVER)
               this.returnToMenu();
            break;
         case 80: // P
            if (this.gameState == GameState.PLAYING || this.gameState == GameState.PAUSED)
               this.togglePause();
            break;
         case 10: // ENTER
            if (this.gameState == GameState.GAME_OVER) {
               this.restartGame(); this.gameState = GameState.PLAYING;
            } else if (this.gameState == GameState.PAUSED) {
               this.togglePause();
            }
            break;
         case 32: case 38: case 87:
            if (this.gameState == GameState.PLAYING) this.playerController.jump();
            break;
         case 37: case 65:
            if (this.gameState == GameState.PLAYING) this.playerController.setLeft(true);
            break;
         case 39: case 68:
            if (this.gameState == GameState.PLAYING) this.playerController.setRight(true);
            break;
      }
   }

   public void keyReleased(int var1) {
      switch (var1) {
         case 37: case 65: this.playerController.setLeft(false); break;
         case 39: case 68: this.playerController.setRight(false); break;
      }
   }

   // ── GETTERS ───────────────────────────────────────────────────────
   public PlayerModel getPlayer()       { return this.player; }
   public List<EnemyModel> getEnemies() { return this.enemies; }
   public List<CoinModel> getCoins()    { return this.coins; }
   public List<PlatformModel> getPlatforms() { return this.platforms; }
   public List<ObstacleModel> getObstacles() { return this.obstacles; }
   public List<StairModel> getStairs()  { return this.stairs; }
   public List<GoldModel> getGolds()    { return this.golds; }
   public TrophyModel getTrophy()       { return this.trophy; }
   public int getGroundY()              { return this.levelManager.getGroundY(); }
   public int getCameraX()              { return (int)this.player.getX() - 200; }
   public GameState getGameState()      { return this.gameState; }
   public int getCurrentLevel()         { return this.levelManager.getCurrentLevel(); }
   public int getMaxLevel()             { return this.levelManager.getMaxLevel(); }
   public boolean isLastLevel()         { return this.levelManager.isLastLevel(); }
   public int getElapsedSeconds()       { return (int)(this.elapsedFrames / 60L); }

   /** 0.0 → 1.0 dùng cho thanh tiến trình overlay LEVEL_COMPLETE */
   public float getLevelCompleteProgress() {
      return 1.0f - (levelCompleteTimer / (float) LEVEL_COMPLETE_DELAY);
   }

   /** 0.0 → 1.0 dùng cho thanh đếm ngược GAME_OVER */
   public float getGameOverProgress() {
      return 1.0f - (gameOverTimer / (float) GAME_OVER_DELAY);
   }
}