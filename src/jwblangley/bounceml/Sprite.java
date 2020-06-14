package jwblangley.bounceml;

import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Sprite extends Circle {

  public static final double RADIUS = 12.5;
  public static final double JUMP_POWER = 7d;

  private final AtomicReference<Boolean> canJump;

  private double fallVelocity;
  private double score;
  private boolean alive;

  public Sprite(double centreX, double centreY) {
    super(centreX, centreY, RADIUS, Color.BLACK);
    canJump = new AtomicReference<>(true);
    alive = true;
  }

  public void requestJump() {
    if (canJump.getAndUpdate(a -> a)) {
      jump();

      canJump.compareAndSet(true, false);
      new Thread(() -> {
        try {
          Thread.sleep(Game.MILLIS_BETWEEN_JUMPS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        canJump.compareAndSet(false, true);
      }).start();
    }
  }

  public void update() {
    if (alive) {
      fallVelocity += Game.GRAVITY;
      setCenterY(Math.max(getHeight() + fallVelocity, getRadius()));
    }
  }

  private void jump() {
    fallVelocity = -1 * JUMP_POWER;
  }

  public void kill(double score) {
    alive = false;
    this.score = score;
  }

  public boolean isAlive() {
    return alive;
  }

  public double getScore() {
    return score;
  }

  public double getHeight() {
    return getCenterY();
  }
}
