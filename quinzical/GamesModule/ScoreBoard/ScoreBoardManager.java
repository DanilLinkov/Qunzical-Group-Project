package quinzical.GamesModule.ScoreBoard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

/**
 * This is the manager class for the score board which loads the score.txt file
 * and creates a hashmap out of it. It also allows for the addition of scores
 * and getting the best score from the file
 * <p></p>
 *
 * @author Hyung Park, Danil Linkov
 */
public class ScoreBoardManager {

    private static ScoreBoardManager instance;
    // The map that the score.txt file will be loaded into
    private Map<String,Integer> scoreBoardMap;

    private ScoreBoardManager() {
        instance = this;
    }

    /**
     * Returns an instance of the ScoreBoardManager otherwise creates a new one
     * @return
     */
    public static ScoreBoardManager getInstance() {
        return instance == null ? new ScoreBoardManager() : instance;
    }

    /**
     * Returns the loaded score map
     * @return
     */
    public Map<String,Integer> getScoreBoardMap() {
        loadQuestionBoard();
        return scoreBoardMap;
    }

    /**
     * Returns the best score from the score.txt
     * @return
     */
    public Integer getBestScore() {
        loadQuestionBoard();
        return !scoreBoardMap.values().isEmpty() ? (Integer) scoreBoardMap.values().toArray()[0] : 0;
    }

    /**
     * Adds the score for the player name into the score.txt
     * @param playerName
     * @param score
     */
    public void addScore(String playerName,Integer score) {
        try {
            // If the file is empty then don't create a new line
            if (scoreBoardMap.isEmpty()) {
                Files.write(Paths.get((new File("").getAbsolutePath())+"/scoreBoard/score.txt"), (playerName + "," + score).getBytes(), StandardOpenOption.APPEND);
            }
            // Otherwise create a new line so that it does not append to the same line
            else {
                Files.write(Paths.get((new File("").getAbsolutePath())+"/scoreBoard/score.txt"), ("\n"+playerName + "," + score).getBytes(), StandardOpenOption.APPEND);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the score.txt into the map to be able to then use
     */
    private void loadQuestionBoard() {
        scoreBoardMap = new HashMap<>();

        // Getting the path of the score folder outside of the application
        String scorePath = new File("").getAbsolutePath();
        scorePath+="/scoreBoard";

        // Creating a file object based on that path
        File scoreDir = new File(scorePath);

        // If that folder does not exist then create it
        if (!Files.exists(Paths.get(scorePath))) {
            scoreDir.mkdir();
        }

        // Change the scorePath to be to the score.txt file
        scorePath+="/score.txt";
        // Create a file object of the score.txt file
        File scoreFile = new File(scorePath);

        // if the file does not exist then create it
        if(!Files.exists(Paths.get(scorePath))) {
            try {
                scoreFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                // get all the lines in the file
                List<String> allLines = Files.readAllLines(Paths.get(scorePath));

                // go over each line, parse it and add it into the map
                for (String line : allLines) {
                    List<String> lineSplit = Arrays.asList(line.split("\\s*,\\s*"));
                    scoreBoardMap.put(lineSplit.get(0).trim(),Integer.parseInt(lineSplit.get(1).trim()));
                }
                scoreBoardMap = sortByComparator(scoreBoardMap,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sorts the map depending on the order that was passed in
     * @param unsortedMap
     * @param order
     * @return
     */
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortedMap, final boolean order) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
