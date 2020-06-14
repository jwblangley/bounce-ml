package jwblangley.bounceml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jwblangley.neat.phenotype.Network;

public class Game extends Stage {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  public static final double GRAVITY = 0.5d;
  public static final int MILLIS_BETWEEN_JUMPS = 150;
  public static final double WIN_TIME = 10d;

  private Label timerLabel;

  private final List<Sprite> sprites;
  private final boolean singlePlayer;

  /**
   * Constructs a new single (human) player game
   */
  public Game() {
    this.setTitle("Bounce");
    this.singlePlayer = true;
    this.sprites = new ArrayList<>(1);
    this.sprites.add(new Sprite());
  }

  /**
   * Constructs a new game with AI players
   *
   * @param neuralNetworks list of neural network players
   */
  public Game(List<Network> neuralNetworks) {
    this.singlePlayer = false;
    sprites = neuralNetworks.stream()
        .map(Sprite::new)
        .collect(Collectors.toList());
  }

  public void init() {
    Group root = new Group();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    this.setScene(scene);

    timerLabel = new Label("Time: 0.00s");
    root.getChildren().add(timerLabel);

    for (Sprite sprite : sprites) {
      root.getChildren().add(sprite);
    }

    if (singlePlayer) {
      scene.setOnKeyPressed(event -> {
        if (event.getCode() == KeyCode.SPACE) {
          sprites.get(0).requestJump();
        }
      });
    }

    double startTime = System.currentTimeMillis();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        // Update timerLabel
        double time = System.currentTimeMillis() - startTime;
        timerLabel.setText(String.format("Time: %.3f", time / 1000d));

        // Update sprites
        for (Sprite sprite : sprites) {
          // Win condition
          if (time / 1000d > WIN_TIME) {
            sprite.kill(100 * time / 1000);
          }

          // Lose condition
          if (sprite.getHeight() > HEIGHT - sprite.getRadius()) {
            sprite.kill(time / 1000d);
          }

          // Update sprite
          sprite.update();
        }
      }
    }.start();
  }


}
