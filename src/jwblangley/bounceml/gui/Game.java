package jwblangley.bounceml.gui;

import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Game extends Application {

  public static int WIDTH = 300;
  public static int HEIGHT = 500;

  @Override
  public void start(Stage stage) throws Exception {
    stage.setTitle("Bounce");

    Group root = new Group();
    Scene scene = new Scene(root);
    stage.setScene(scene);

    Canvas canvas = new Canvas(WIDTH, HEIGHT);
    root.getChildren().add(canvas);

    GraphicsContext gc = canvas.getGraphicsContext2D();

    AtomicReference<Boolean> show = new AtomicReference<>(false);
    scene.setOnKeyPressed(event -> {
      if (event.getCode() == KeyCode.SPACE) {
        show.set(true);
      }
    });
    scene.setOnKeyReleased(event -> {
      if (event.getCode() == KeyCode.SPACE) {
        show.set(false);
      }
    });

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        gc.clearRect(0,0,WIDTH, HEIGHT);
        if (show.get()) {
          gc.setFill(Color.BLACK);
          gc.fillOval(100, 100, 25, 25);
        }
      }
    }.start();

    stage.show();
  }
}
