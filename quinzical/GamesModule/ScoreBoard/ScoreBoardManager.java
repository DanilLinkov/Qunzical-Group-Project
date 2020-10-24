package quinzical.GamesModule.ScoreBoard;

import quinzical.GamesModule.GameManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class ScoreBoardManager {

    private static ScoreBoardManager _instance;
    private Map<String,Integer> scoreBoardMap;

    private ScoreBoardManager() {
        _instance = this;
    }

    public static ScoreBoardManager getInstance() {
        return _instance == null ? new ScoreBoardManager() : _instance;
    }

    public Map<String,Integer> getScoreBoardMap() {
        loadQuestionBoard();
        return scoreBoardMap;
    }

    public Integer getBestScore() {
        loadQuestionBoard();
        return (Integer) scoreBoardMap.values().toArray()[0];
    }

    public void addScore(String playerName,Integer score) {
        try {
            Files.write(Paths.get((new File("").getAbsolutePath())+"/scoreBoard/score.txt"), ("\n"+playerName + "," + Integer.toString(score)).getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadQuestionBoard() {
        scoreBoardMap = new HashMap<>();

        // Getting the path of the save folder outside of the application
        String scorePath = new File("").getAbsolutePath();
        scorePath+="/scoreBoard";

        // Creating a file object based on that path
        File scoreDir = new File(scorePath);

        // If that folder does not exist then create it
        if (!Files.exists(Paths.get(scorePath))) {
            scoreDir.mkdir();
        }

        // Change the savePath to be to the save.txt file
        scorePath+="/score.txt";
        // Create a file object of the save.txt file
        File scoreFile = new File(scorePath);

        if(!Files.exists(Paths.get(scorePath))) {
            try {
                scoreFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                List<String> allLines = Files.readAllLines(Paths.get(scorePath));

                for (int i = 0; i < allLines.size(); i++) {
                    List<String> lineSplit = Arrays.asList(allLines.get(i).split("\\s*,\\s*"));
                    scoreBoardMap.put(lineSplit.get(0).trim(),Integer.parseInt(lineSplit.get(1).trim()));
                }
                scoreBoardMap = sortByComparator(scoreBoardMap,false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, final boolean order)
    {

        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

        // Sorting the list based on values
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>()
        {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                if (order)
                {
                    return o1.getValue().compareTo(o2.getValue());
                }
                else
                {
                    return o2.getValue().compareTo(o1.getValue());

                }
            }
        });

        // Maintaining insertion order with the help of LinkedList
        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

}