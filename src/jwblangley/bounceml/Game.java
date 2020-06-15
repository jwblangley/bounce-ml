package jwblangley.bounceml;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import jwblangley.bounceml.GameApp.Mode;

public class Game extends Stage {

  public static final int WIDTH = 300;
  public static final int HEIGHT = 500;

  public static final double GRAVITY = 0.5d;
  public static final int MILLIS_BETWEEN_JUMPS = 150;
  public static final double WIN_TIME = 3d;

  private Label scoreLabel;

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
   * @param aiSprites list of neural network-controlled sprites
   */
  public Game(List<Sprite> aiSprites) {
    this.singlePlayer = false;
    this.sprites = aiSprites;
  }

  public void init() {
    Group root = new Group();
    Scene scene = new Scene(root, WIDTH, HEIGHT);
    this.setScene(scene);

    scoreLabel = new Label("Time: 0.00s");
    root.getChildren().add(scoreLabel);

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

    final long[] score = {0};
    double startTime = System.currentTimeMillis();

    new AnimationTimer() {
      @Override
      public void handle(long now) {
        double time = (System.currentTimeMillis() - startTime) / 1000d;

        // Update scoreLabel
        score[0]++;
        scoreLabel.setText(String.format("Score: %.1f", score[0] / 100d));

        // Update sprites
        for (Sprite sprite : sprites) {
          // Win condition
          if (time > WIN_TIME) {
            sprite.kill(100 * score[0] / 100d);
          }

          // Lose condition
          if (sprite.getHeight() > HEIGHT - sprite.getRadius() || sprite.getHeight() <  1.5 * sprite.getRadius()) {
            sprite.kill(score[0] / 100d);
          }

          // Update sprite
          sprite.update();
        }

        if (sprites.stream().noneMatch(Sprite::isAlive)) {
          Game.this.close();
        }
      }
    }.start();
  }

  public double getOnlyPlayersScore() {
    if (GameApp.mode != Mode.SINGLE_PLAYER && GameApp.mode != Mode.WATCH) {
      throw new UnsupportedOperationException("Cannot get single player score in non single player game");
    }

    return sprites.get(0).getScore();
  }


}
