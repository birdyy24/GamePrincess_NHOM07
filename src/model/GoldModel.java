package model;

/**
 * Vàng thỏi — giá trị cao hơn coin thường (500 điểm).
 * Xuất hiện trên đỉnh bậc thang hoặc vị trí khó lấy.
 */
public class GoldModel {

    private int x, y;
    private final int width  = 24;
    private final int height = 20;
    private boolean collected;
    private static final int SCORE_VALUE = 500;

    public GoldModel(int x, int y) {
        this.x         = x;
        this.y         = y;
        this.collected = false;
    }

    public int     getX()          { return x; }
    public int     getY()          { return y; }
    public int     getWidth()      { return width; }
    public int     getHeight()     { return height; }
    public boolean isCollected()   { return collected; }
    public int     getScoreValue() { return SCORE_VALUE; }

    public void setCollected(boolean collected) { this.collected = collected; }
}
