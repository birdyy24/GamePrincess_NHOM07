package model;

/**
 * Model đại diện cho đồng xu trong game.
 */
public class CoinModel {

    private int     x, y;
    private boolean collected;

    public CoinModel(int x, int y) {
        this.x         = x;
        this.y         = y;
        this.collected = false;
    }

    // ── Getters ──
    public int     getX()           { return x; }
    public int     getY()           { return y; }
    public boolean isCollected()    { return collected; }

    // ── Setters ──
    public void setCollected(boolean collected) { this.collected = collected; }
}
