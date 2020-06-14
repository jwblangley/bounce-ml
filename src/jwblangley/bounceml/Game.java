package jwblangley.bounceml;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class Game extends Application {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  public static final double GRAVITY = 0.5d;
  public static final int MILLIS_BETWEEN_JUMPS = 150;
  public static final double WIN_TIME = 10d;

  private Label timerLabel;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Bounce");

    Group root = new Group();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    stage.setScene(scene);

    timerLabel = new Label("Time: 0.00s");
    root.getChildren().add(timerLabel);

    Sprite sprite = new Sprite(WIDTH / 2d, HEIGHT / 2d);
    root.getChildren().add(sprite);

    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.SPACE) {
        sprite.requestJump();
      }
    });

    double startTime = System.currentTimeMillis();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Update timerLabel
        double time = System.currentTimeMillis() - startTime;
        timerLabel.setText(String.format("Time: %.3f", time / 1000d));

        // Win condition
        if (time / 1000d > 10) {
          sprite.kill(100 * time / 1000);
        }

        // Lose condition
        if (sprite.getHeight() > HEIGHT - sprite.getRadius()) {
          sprite.kill(time / 1000d);
        }

        // Update sprite
        sprite.update();
      }
    }.start();

    stage.show();
  }



}
