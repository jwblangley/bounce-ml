package jwblangley.bounceml.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GameLayout {

  public static int WIDTH = 300;
  public static int HEIGHT = 500;

  public Pane layout(Stage window) {
    Pane layout = new Pane();
    layout.setPrefSize(WIDTH, HEIGHT);

    BufferedImage circle = new BufferedImage(25, 25, BufferedImage.TYPE_INT_ARGB);
    Graphics graphics = circle.getGraphics();
    graphics.setColor(Color.BLACK);
    graphics.fillOval(0,0, 24, 24);

    ImageView sprite = new ImageView(SwingFXUtils.toFXImage(circle, null));
    sprite.setLayoutX(100);
    sprite.setLayoutY(100);
    layout.getChildren().add(sprite);

    return layout;
  }

}
