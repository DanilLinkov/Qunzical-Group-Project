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
     * @return the instance of the ScoreBoardManager currently being used.
     */
    public static ScoreBoardManager getInstance() {
        return instance == null ? new ScoreBoardManager() : instance;
    }

    /**
     * Returns the loaded score map
     * @return A map of score board with score records in it.
     */
    public Map<String,Integer> getScoreBoardMap() {
        loadQuestionBoard();
        return scoreBoardMap;
    }

    /**
     * Returns the best score from the score.txt
     * @return the best score from the score.txt
     */
    public int getBestScore() {
        loadQuestionBoard();
        return !scoreBoardMap.values().isEmpty() ? (Integer) scoreBoardMap.values().toArray()[0] : 0;
    }

    /**
     * Adds the score for the player name into the score.txt
     * @param playerName The name of the player to add
     * @param score The score of the player to add
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
                scoreBoardMap = sortByComparator(scoreBoardMap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sorts the map with the highest value on the first index to the lowest value on the bottom index.
     * @param unsortedMap An unsorted map that is to be sorted using this comparator.
     * @return A sorted Map
     */
    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortedMap) {

        List<Map.Entry<String, Integer>> list = new LinkedList<>(unsortedMap.entrySet());

        // Sorting the list based on values
        list.sort((o1, o2) -> o2.getValue().compareTo(o1.getValue()));

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}
