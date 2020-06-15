package jwblangley.bounceml;

import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import jwblangley.bounceml.GameApp.Mode;
import jwblangley.neat.phenotype.Network;
import jwblangley.neat.proto.ProtoIO;

public class Main {

  public static void main(String[] args) {
    if (args.length < 1 || args.length > 2) {
      printUsage();
      return;
    }

    if (args[0].equals("--singleplayer")) {
      GameApp.mode = Mode.SINGLE_PLAYER;
    } else if (args[0].equals("--train")) {
      GameApp.mode = Mode.TRAIN;
    } else if (args[0].equals("--watch")) {
      if (args.length != 2) {
        printUsage();
        return;
      }
      File networkFile = new File(args[1]);

      GameApp.mode = Mode.WATCH;
      try {
        GameApp.watchNetwork = Network
            .createSigmoidOutputNetworkFromGenotype(ProtoIO.networkFromFile(networkFile));
      } catch (IOException e) {
        System.err.println("Could not load network");
        return;
      }
    }

    Application.launch(GameApp.class);
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
