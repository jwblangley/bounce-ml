package jwblangley.bounceml.gui;

import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Game extends Application {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  public static final double GRAVITY = 0.5d;
  public static final double JUMP_POWER = 7d;
  public static final int MILLIS_BETWEEN_JUMPS = 150;

  private Circle sprite;
  private double fallVelocity = 0;

  private Label timerLabel;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Bounce");

    Group root = new Group();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    stage.setScene(scene);

    timerLabel = new Label("Time: 0.00s");
    root.getChildren().add(timerLabel);

    sprite = new Circle(WIDTH / 2d, HEIGHT / 2d, 12.5, Color.BLACK);
    root.getChildren().add(sprite);

    AtomicReference<Boolean> jump = new AtomicReference<>(false);
    AtomicReference<Boolean> canJump = new AtomicReference<>(true);
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.SPACE) {
        requestJump(jump, canJump);
      }
    });

    double startTime = System.currentTimeMillis();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Update timerLabel
        double time = System.currentTimeMillis() - startTime;
        timerLabel.setText(String.format("Time: %.3f", time / 1000d));

        fallVelocity += GRAVITY;
        if (jump.getAndUpdate(a -> a)) {
          jump.set(false);
          fallVelocity = -1 * JUMP_POWER;
        }

        sprite.setCenterY(clamp(sprite.getCenterY() + fallVelocity, sprite.getRadius(),
            HEIGHT - sprite.getRadius()));
      }
    }.start();

    stage.show();
  }

  public double getFallVelocity() {
    return fallVelocity;
  }

  private double getSpiteY() {
    return sprite.getCenterY();
  }

  public void requestJump(AtomicReference<Boolean> jump, AtomicReference<Boolean> canJump) {
    if (canJump.getAndUpdate(a -> a)) {
      jump.compareAndSet(false, true);
      canJump.compareAndSet(true, false);
      new Thread(() -> {
        try {
          Thread.sleep(MILLIS_BETWEEN_JUMPS);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        canJump.compareAndSet(false, true);
      }).start();
    }
  }

  private static double clamp(double value, double min, double max) {
    return Math.max(min, Math.min(max, value));
  }
}
