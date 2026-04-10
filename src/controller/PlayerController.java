package controller;
import model.PlayerModel;

public class PlayerController {
    private static final double GRAVITY    = 0.55;
    private static final double MOVE_SPEED = 3.0;
    private static final double JUMP_FORCE = -13.0;
    private static final int    WORLD_WIDTH = 3200;

    private final PlayerModel player;
    private boolean movingLeft  = false;
    private boolean movingRight = false;

    public PlayerController(PlayerModel player) {
        this.player = player;
    }

    public void update() {
        handleMovement();
        applyGravity();
        move();
    }

    private void handleMovement() {
        if (movingLeft && !movingRight) {
            player.setVelX(-MOVE_SPEED);
            player.setFacingRight(false);
        } else if (movingRight && !movingLeft) {
            player.setVelX(MOVE_SPEED);
            player.setFacingRight(true);
        } else {
            player.setVelX(0); // Đứng yên khi không nhấn phím
        }
    }

    private void applyGravity() {
        player.setVelY(player.getVelY() + GRAVITY);
    }

    private void move() {
        player.setX(player.getX() + player.getVelX());
        player.setY(player.getY() + player.getVelY());
        if (player.getX() < 0) player.setX(0);
        if (player.getX() > WORLD_WIDTH - player.getWidth()) {
            player.setX(WORLD_WIDTH - player.getWidth());
        }
        player.setOnGround(false);
    }

    public void jump() {
        if (player.isOnGround()) {
            player.setVelY(JUMP_FORCE);
            player.setJumping(true);
            player.setOnGround(false);
        }
    }

    public void setLeft(boolean val)  { movingLeft  = val; }
    public void setRight(boolean val) { movingRight = val; }
}