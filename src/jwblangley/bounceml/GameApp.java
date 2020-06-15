package jwblangley.bounceml;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.stage.Stage;
import jwblangley.neat.evolution.Evolution;
import jwblangley.neat.evolution.EvolutionFactory;
import jwblangley.neat.phenotype.Network;
import jwblangley.neat.proto.ProtoIO;

public class GameApp extends Application {

  public static Mode mode;

  public static Network watchNetwork;

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
      System.out.println("Your score was: " + game.getOnlyPlayersScore());
    } else if (mode == Mode.WATCH) {
      Game game = new Game(Collections.singletonList(new Sprite(watchNetwork)));
      game.init();
      game.showAndWait();
      System.out.println("The AI's score was: " + game.getOnlyPlayersScore());
    } else if (mode == Mode.TRAIN) {

      final int populationSize = 1000;
      final int targetNumSpecies = 100;
      final int evolutions = 100;

      Evolution evolution = EvolutionFactory
          .createOptimisation(1, 1, populationSize, targetNumSpecies, genotypes -> {
            List<Sprite> sprites = genotypes.stream()
                .map(Network::createSigmoidOutputNetworkFromGenotype)
                .map(Sprite::new)
                .collect(Collectors.toList());

            Game game = new Game(sprites);
            game.init();
            game.showAndWait();

            return sprites.stream()
                .map(Sprite::getScore)
                .map(d -> Math.pow(d * 10, 3))
                .collect(Collectors.toList());
          });

      Random random = new Random();

      double highestScoreEver = Double.MIN_VALUE;
      int saves = 0;
      File saveDir = new File("saves");
      if (!saveDir.exists()) {
        saveDir.mkdir();
      }

      evolution.setVerbose(true);
      for (int i = 0; i < evolutions; i++) {
        evolution.evolve(random);
        System.out.println();

        if (evolution.getHighestFitness() > highestScoreEver) {
          highestScoreEver = evolution.getHighestFitness();
          saves++;

          ProtoIO.toFile(evolution.getFittestGenotype(), new File("saves/save" + saves + ".geno"));
        }
      }

    }

  }
}
