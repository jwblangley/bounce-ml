package jwblangley.bounceml.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Game extends Application {

  @Override
  public void start(Stage window) throws Exception {

    GameLayout gameLayout = new GameLayout();
    Scene scene = new Scene(gameLayout.layout(window));
    window.setTitle("Bounce");
    window.setScene(scene);
    window.show();
  }
}
