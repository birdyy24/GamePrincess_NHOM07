package model;

/**
 * Lớp trừu tượng đại diện cho kẻ địch trong game.
 * Các subclass cụ thể kế thừa và override getType(), getScoreValue().
 */
public abstract class EnemyModel {

    protected double x, y;
    protected int    width  = 48;
    protected int    height = 48;
    protected double speed;
    protected boolean alive;
    protected boolean movingRight;

    // Số frame bị dẹt sau khi bị giẫm (hiệu ứng chết)
    protected int deadTimer;

    public EnemyModel(double x, double y, double speed) {
        this.x           = x;
        this.y           = y;
        this.speed       = speed;
        this.alive       = true;
        this.movingRight = false;
        this.deadTimer   = 0;
    }

    /** Điểm thưởng khi tiêu diệt kẻ địch này */
    public abstract int    getScoreValue();

    /** Loại kẻ địch: "goomba_pink" ... */
    public abstract String getType();

    // ── Getters ──
    public double  getX()           { return x; }
    public double  getY()           { return y; }
    public int     getWidth()       { return width; }
    public int     getHeight()      { return height; }
    public double  getSpeed()       { return speed; }
    public boolean isAlive()        { return alive; }
    public boolean isMovingRight()  { return movingRight; }
    public int     getDeadTimer()   { return deadTimer; }

    // ── Setters ──
    public void setX(double x)                    { this.x = x; }
    public void setY(double y)                    { this.y = y; }
    public void setAlive(boolean alive)           { this.alive = alive; }
    public void setMovingRight(boolean right)     { this.movingRight = right; }
    public void setDeadTimer(int deadTimer)       { this.deadTimer = deadTimer; }
}
