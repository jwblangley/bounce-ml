package jwblangley.bounceml;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    Game game = new Game();
    game.init();
    game.showAndWait();
  }
}
