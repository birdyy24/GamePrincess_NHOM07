package model;

/**
 * TrophyModel — Cúp xuất hiện tại vị trí đích.
 * Animation nảy lên xuống nhẹ theo sin.
 */
public class TrophyModel {

    private final double x;
    private final double baseY;
    private double y;
    private final int width  = 48;
    private final int height = 56;
    private double animTick  = 0;
    private boolean collected = false;

    public TrophyModel(double x, double groundY) {
        this.x     = x;
        this.baseY = groundY - height - 8;
        this.y     = this.baseY;
    }

    public void update() {
        if (!collected) {
            animTick += 0.07;
            y = baseY + Math.sin(animTick) * 6.0;
        }
    }

    public double  getX()        { return x; }
    public double  getY()        { return y; }
    public int     getWidth()    { return width; }
    public int     getHeight()   { return height; }
    public boolean isCollected() { return collected; }
    public double  getAnimTick() { return animTick; }
    public void    setCollected(boolean v) { collected = v; }
}