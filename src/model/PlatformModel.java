package model;

/**
 * Model đại diện cho một block nền tảng trong màn chơi.
 * type = 0: gạch thường | type = 1: khối "?" | type = 2: đất cứng
 */
public class PlatformModel {

    private int x, y, width, height;
    private int type;       // 0=brick, 1=question, 2=solid
    private boolean hit;    // đã bị đập chưa (chỉ dùng cho type=1)

    public PlatformModel(int x, int y, int width, int height, int type) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.type   = type;
        this.hit    = false;
    }

    // ── Getters ──
    public int     getX()      { return x; }
    public int     getY()      { return y; }
    public int     getWidth()  { return width; }
    public int     getHeight() { return height; }
    public int     getType()   { return type; }
    public boolean isHit()     { return hit; }

    // ── Setters ──
    public void setHit(boolean hit) { this.hit = hit; }
}
