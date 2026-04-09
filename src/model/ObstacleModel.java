package model;

/**
 * Chướng ngại vật tự động di chuyển từ phải sang trái.
 * Khi công chúa chạm vào → chết.
 */
public class ObstacleModel {

    private double x, y;
    private final int width  = 40;
    private final int height = 40;
    private final double speed;
    private boolean active;

    // type: "rock" | "spike" | "fireball"
    private final String type;

    public ObstacleModel(double x, double y, double speed, String type) {
        this.x      = x;
        this.y      = y;
        this.speed  = speed;
        this.active = true;
        this.type   = type;
    }

    public void update() {
        x -= speed; // di chuyển sang trái
    }

    // Getters
    public double  getX()       { return x; }
    public double  getY()       { return y; }
    public int     getWidth()   { return width; }
    public int     getHeight()  { return height; }
    public boolean isActive()   { return active; }
    public String  getType()    { return type; }

    // Setters
    public void setActive(boolean active) { this.active = active; }
}
