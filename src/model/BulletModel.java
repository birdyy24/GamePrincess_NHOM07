package model;

/**
 * Model đại diện cho viên đạn hoa mà công chúa bắn ra.
 */
public class BulletModel {

    private double  x, y;
    private final int width  = 12;
    private final int height = 8;
    private double  speedX;
    private boolean active;

    /**
     * @param startX     Tọa độ X ban đầu
     * @param startY     Tọa độ Y ban đầu
     * @param goingRight true = bắn sang phải, false = bắn sang trái
     */
    public BulletModel(double startX, double startY, boolean goingRight) {
        this.x       = startX;
        this.y       = startY;
        this.speedX  = goingRight ? 10.0 : -10.0;
        this.active  = true;
    }

    // ── Getters ──
    public double  getX()       { return x; }
    public double  getY()       { return y; }
    public int     getWidth()   { return width; }
    public int     getHeight()  { return height; }
    public double  getSpeedX()  { return speedX; }
    public boolean isActive()   { return active; }

    // ── Setters ──
    public void setX(double x)            { this.x = x; }
    public void setY(double y)            { this.y = y; }
    public void setActive(boolean active) { this.active = active; }
}
