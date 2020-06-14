package jwblangley.bounceml;

import javafx.application.Application;
import javafx.stage.Stage;

public class GameApp extends Application {

  public static Mode mode;

  public enum Mode {
    TRAIN,
    WATCH,
    SINGLE_PLAYER
  }

  @Override
  public void start(Stage primaryStage) throws Exception {
    if (mode == Mode.SINGLE_PLAYER) {
      Game game = new Game();
      game.init();
      game.showAndWait();
      System.out.println("Your score was: " + game.getSinglePlayerScore());
    }
  }
}
