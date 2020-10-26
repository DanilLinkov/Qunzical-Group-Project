package quinzical.GamesModule;

import javafx.scene.layout.GridPane;
import quinzical.Questions.Category;
import quinzical.Questions.Question;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manager of the game module.
 * <p></p>
 * It manages the overall game module of Quinzical such as
 * saving or loading the game.
 * <p></p>
 * This is a singleton class, meaning only one instance of
 * this class is expected to be generated. (Only one manager
 * should manage one session of game.)
 *
 * @author Danil Linkov, Hyung Park
 */
public class GameManager {

    // Question board used in this game.
    private QuestionBoard _questionBoard;

    // Score status.
    private int _currentScore;
    private int _bestScore;

    // Singleton instance of this class.
    private static GameManager _instance;

    /**
     * Singleton constructor of this class.
     * It loads {@link GameManager#loadGame()}.
     */
    private GameManager() {
        _instance = this;
        loadGame();
    }

    /**
     * Checks whether there is any previously generated instance of this class,
     * if it has, it returns that instance, if not, it generates a new instance
     * and returns it.
     * @return An instance of this class.
     */
    public static GameManager getInstance() {
        return _instance == null ? new GameManager() : _instance;
    }

    public boolean questionBoardExists() {
        return (_questionBoard!=null);
    }

    /**
     * Returns the question board for the current game.
     * @return GridPane component which contains the question board.
     */
    public GridPane getQuestionBoard() {
        return _questionBoard.getQuestionBoard();
    }

    /**
     * Get the question object for the given category index and given question index
     * @param categoryIndex
     * @param questionIndex
     * @return
     */
    public Question getQuestionInCategory(int categoryIndex, int questionIndex) {
        return _questionBoard.getCategory(categoryIndex).getQuestion(questionIndex);
    }

    /**
     * This method checks whether every question in each category has been answered
     * @return
     */
    public boolean isEveryQuestionAnswered() {
        // Going over all the categories
        for (int i = 0; i < 5; i++) {
            // If its smallest value question index is not equal to 5 then return false
            // as that category is not yet done
            if (_questionBoard.getCategory(i).getLowestValuedQuestionIndex() != 5) {
                return false;
            }
        }
        // Return true if every category is at 5 for their lowest value question index
        return true;
    }

    /**
     * Return the player's current score
     * @return
     */
    public int getCurrentScore() {
        return _currentScore;
    }

    /**
     * Increments the player's current score
     * @param value
     */
    public void incrementCurrentScore(int value) {
        _currentScore += value;
    }

    /**
     * Decrements the player's current score
     * @param value
     */
    public void decrementCurrentScore(int value) {
        _currentScore -= value;
    }

    /**
     * Updating the users best score if the current score is greater than the current best score
     */
    public void updateBestScore() {
        if (_currentScore > _bestScore) {
            _bestScore = _currentScore;
        }
    }

    /**
     * Return the player's best score
     * @return
     */
    public int getBestScore() {
        return _bestScore;
    }

    /**
     * Creates a new question board and resets the player's current score to 0 and saves the game
     */
    public void newGame(ArrayList<String> selectedCategories) {
        _questionBoard = new QuestionBoard();
        _questionBoard.createBoard(selectedCategories);
        _currentScore = 0;
        saveGame();
    }

    public void resetGame() {
        _questionBoard = null;
        _currentScore = 0;

        String savePath = new File("").getAbsolutePath();
        savePath+="/save/save.txt";

        if (Files.exists(Paths.get(savePath))) {
            File saveFile = new File(savePath);
            saveFile.delete();
        }
    }

    /**
     * This method is used to save the current question board grid in games module
     * into a txt file which can then be loaded with the loadGame method
     */
    public void saveGame() {
        if (_questionBoard!=null) {
            // Getting the path of the save folder outside of the application
            String savePath = new File("").getAbsolutePath();
            savePath+="/save";

            // Creating a file object based on that path
            File saveDir = new File(savePath);

            // If that folder does not exist then create it
            if (!Files.exists(Paths.get(savePath))) {
                saveDir.mkdir();
            }

            // Change the savePath to be to the save.txt file
            savePath+="/save.txt";
            // Create a file object of the save.txt file
            File saveFile = new File(savePath);
            // Delete it to start from scratch
            saveFile.delete();

            // Create a new save file
            try {
                saveFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                // Create a file writer for that save.txt file
                FileWriter saveWriter = new FileWriter(savePath);
                // Write the first line with the current score, best score
                saveWriter.write(_currentScore + "," + _bestScore + "\n");

                // Go over the question board categories
                for(int i = 0; i < _questionBoard.getNumCategories(); i++) {
                    // Get the current category
                    Category currentCategory = _questionBoard.getCategory(i);
                    // This string saveLine is used as the line that will be saved to the txt
                    String saveLine = currentCategory.toString();

                    // Go over the questions in the categories
                    for (int j = 0; j < _questionBoard.getNumQuestions(); j++) {
                        // Get the current question
                        Question currentQuestion = currentCategory.getQuestion(j);
                        // Append this question's line number to the saveLine
                        saveLine += ","+currentQuestion.getLineNumber();
                    }

                    // Write this line with the last index being the lowest valued question index to indicate which questions
                    // were answered or not
                    saveWriter.write(saveLine + "," + currentCategory.getLowestValuedQuestionIndex() + "\n");
                }

                saveWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is used to load the save.txt file and create the question board grid for
     * the games module based on that + get the users current and best scores
     */
    public void loadGame() {
        // Get the save.txt file path
        String savePath = new File("").getAbsolutePath();
        savePath+="/save";
        savePath+="/save.txt";

        // If the save file exists then load it
        if (Files.exists(Paths.get(savePath))) {
            try {
                // Get all the lines in the save file
                List<String> allLines = Files.readAllLines(Paths.get(savePath));
                // Split the first line to get the current and best scores
                List<String> lineSplit = Arrays.asList(allLines.get(0).split("\\s*,\\s*"));
                _currentScore = Integer.parseInt(lineSplit.get(0));
                _bestScore = Integer.parseInt(lineSplit.get(1));

                // Initialise a new question board
                _questionBoard = new QuestionBoard();

                // Go over every line in the save.txt file
                for (int i = 1; i < allLines.size(); i++) {
                    // Split it
                    lineSplit = Arrays.asList(allLines.get(i).split("\\s*,\\s*"));
                    // Create the new category to load and add it
                    Category newCategory = new Category(lineSplit.get(0)); // parent
                    _questionBoard.addCategory(newCategory);

                    // Load the categories questions by going to the categories folder to find the question line
                    savePath = new File("").getAbsolutePath()+"/categories/NZ/"+lineSplit.get(0)+".txt";
                    List<String> questionLines = Files.readAllLines(Paths.get(savePath));
                    // Set the lowest answered question index of that category
                    newCategory.setLowestValuedQuestionIndex(Integer.parseInt(lineSplit.get(6)));

                    // Go over every question line number in the save.txt
                    for(int j = 1; j < 6; j++) {
                        // Get that question
                        String selectedQuestionLine = questionLines.get(Integer.parseInt(lineSplit.get(j)));
                        // Split it
                        List<String> selectedQSplit = Arrays.asList(selectedQuestionLine.split("\\s*\\|\\s*"));
                        // Get the answer array and create a question object
                        String[] answerSplit = selectedQSplit.get(2).split("/");
                        Question newQuestionToAdd = new Question(selectedQSplit.get(0),answerSplit,newCategory,j*100);

                        // Add all the properties to it
                        newQuestionToAdd.setQuestionType(selectedQSplit.get(1));
                        newQuestionToAdd.setLineNumber(Integer.parseInt(lineSplit.get(j)));
                        newCategory.addQuestion(newQuestionToAdd);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            _questionBoard = null;
        }
    }
}
