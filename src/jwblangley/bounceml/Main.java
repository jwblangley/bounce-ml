package jwblangley.bounceml;

import javafx.application.Application;
import jwblangley.bounceml.GameApp.Mode;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1 || args.length > 2) {
      printUsage();
      return;
    }

    if (args[0].equals("--singleplayer")) {
      GameApp.mode = Mode.SINGLE_PLAYER;
      Application.launch(GameApp.class);
    }
  }

  public static void printUsage() {
    System.out.println("Use one of the following:");
    System.out.println("\t--train");
    System.out.println("\t\ttrain neural network to play the game");
    System.out.println("\t--watch <network>");
    System.out.println("\t\twatch a neural network (from file) play the game");
    System.out.println("\t--singleplayer");
    System.out.println("\t\tplay the game yourself");
  }

}
