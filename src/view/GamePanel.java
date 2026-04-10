package view;

import controller.GameController;
import model.GameState;
import view.render.GameRenderer;
import view.render.MenuRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDTH  = 800;
    public static final int HEIGHT = 600;
    private static final int FPS   = 60;

    private final GameController controller;
    private final GameRenderer   renderer;
    private final MenuRenderer   menuRenderer;

    private Thread  gameThread;
    private boolean running = false;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setDoubleBuffered(true);

        controller   = new GameController();
        renderer     = new GameRenderer(controller);
        menuRenderer = new MenuRenderer();

        // ── Bàn phím ──
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (controller.getGameState() == GameState.MENU) {
                    // ENTER ở menu → bắt đầu game
                    if (menuRenderer.handleKey(e.getKeyCode())) {
                        controller.applyDifficulty(menuRenderer.getSelectedDifficulty());
                        controller.startPlaying();
                    }
                } else {
                    controller.keyPressed(e.getKeyCode());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (controller.getGameState() != GameState.MENU) {
                    controller.keyReleased(e.getKeyCode());
                }
            }
        });

        // ── Chuột (click chọn độ khó / nút PLAY) ──
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (controller.getGameState() == GameState.MENU) {
                    boolean startGame = menuRenderer.handleClick(e.getX(), e.getY());
                    if (startGame) {
                        controller.applyDifficulty(menuRenderer.getSelectedDifficulty());
                        controller.startPlaying();
                    }
                }
            }
        });
    }

    public void startGame() {
        running    = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long   lastTime = System.nanoTime();
        double ns       = 1_000_000_000.0 / FPS;
        double delta    = 0;

        while (running) {
            long now = System.nanoTime();
            delta   += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1) {
                controller.update();
                delta--;
            }

            repaint();

            try { Thread.sleep(1); } catch (Exception ignored) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        if (controller.getGameState() == GameState.MENU) {
            menuRenderer.draw(g2, getWidth(), getHeight());
        } else {
            renderer.draw(g2, getWidth(), getHeight());
        }
    }
}
