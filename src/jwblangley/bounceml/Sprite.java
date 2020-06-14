package jwblangley.bounceml;

import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jwblangley.neat.phenotype.Network;

public class Sprite extends Circle {

  public static final double RADIUS = 12.5;
  public static final double JUMP_POWER = 7d;

  private final AtomicReference<Boolean> canJump;

  private double fallVelocity;
  private double score;
  private boolean alive;

  private final Network neuralNetwork;

  public Sprite() {
    this(null);
  }

  public Sprite(Network neuralNetwork) {
    super(Game.WIDTH / 2, Game.HEIGHT / 2, RADIUS, Color.BLACK);
    this.neuralNetwork = neuralNetwork;
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

      if (neuralNetwork != null) {
        // TODO: neural network control
      }
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
