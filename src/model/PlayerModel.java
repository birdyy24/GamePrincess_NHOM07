package model;

/**
 * Model đại diện cho nhân vật công chúa Peach.
 * Chứa toàn bộ thuộc tính vị trí, trạng thái animation, điểm, mạng.
 */
public class PlayerModel {

    // Vị trí & kích thước
    private double x, y;
    private final int width  = 48;
    private final int height = 80;

    // Tốc độ
    private double velX, velY;

    // Trạng thái
    private int     lives;
    private int     score;
    private boolean isJumping;
    private boolean isFacingRight;
    private boolean isOnGround;

    // Animation: "idle" | "run" | "jump" | "dead"
    private String currentAnimation;

    // Bất tử tạm thời sau khi bị đánh (số frame)
    private int invincibleTimer;

    public PlayerModel(double startX, double startY) {
        this.x                = startX;
        this.y                = startY;
        this.velX             = 0;
        this.velY             = 0;
        this.lives            = 3;
        this.score            = 0;
        this.isJumping        = false;
        this.isFacingRight    = true;
        this.isOnGround       = false;
        this.currentAnimation = "idle";
        this.invincibleTimer  = 0;
    }

    // ── Getters ──
    public double  getX()                 { return x; }
    public double  getY()                 { return y; }
    public int     getWidth()             { return width; }
    public int     getHeight()            { return height; }
    public double  getVelX()              { return velX; }
    public double  getVelY()              { return velY; }
    public int     getLives()             { return lives; }
    public int     getScore()             { return score; }
    public boolean isJumping()            { return isJumping; }
    public boolean isFacingRight()        { return isFacingRight; }
    public boolean isOnGround()           { return isOnGround; }
    public String  getCurrentAnimation()  { return currentAnimation; }
    public int     getInvincibleTimer()   { return invincibleTimer; }

    // ── Setters ──
    public void setX(double x)                          { this.x = x; }
    public void setY(double y)                          { this.y = y; }
    public void setVelX(double velX)                    { this.velX = velX; }
    public void setVelY(double velY)                    { this.velY = velY; }
    public void setLives(int lives)                     { this.lives = lives; }
    public void setScore(int score)                     { this.score = score; }
    public void setJumping(boolean jumping)             { this.isJumping = jumping; }
    public void setFacingRight(boolean facingRight)     { this.isFacingRight = facingRight; }
    public void setOnGround(boolean onGround)           { this.isOnGround = onGround; }
    public void setCurrentAnimation(String anim)        { this.currentAnimation = anim; }
    public void setInvincibleTimer(int invincibleTimer) { this.invincibleTimer = invincibleTimer; }
}
