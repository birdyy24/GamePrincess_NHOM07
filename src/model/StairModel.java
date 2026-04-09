package model;

/**
 * Bậc thang gồm nhiều bậc xếp chồng lên nhau.
 * Mỗi StairModel đại diện cho MỘT BẬC trong cầu thang.
 */
public class StairModel {

    private final int x, y, width, height;
    private final int step; // thứ tự bậc (0 = dưới cùng)

    public StairModel(int x, int y, int width, int height, int step) {
        this.x      = x;
        this.y      = y;
        this.width  = width;
        this.height = height;
        this.step   = step;
    }

    public int getX()      { return x; }
    public int getY()      { return y; }
    public int getWidth()  { return width; }
    public int getHeight() { return height; }
    public int getStep()   { return step; }
}