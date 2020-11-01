package quinzical.GamesModule;

import javafx.scene.layout.GridPane;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
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

    private final QuestionBoard[] questionBoards = new QuestionBoard[2];
    private final boolean[] gameFinished = new boolean[2];

    // Question board currently used in this game type.
    private QuestionBoard questionBoardInUse;
    private GameType currentGameType;
    private boolean isInternationalGameUnlocked = false;

    // Score status.
    private int currentScore = 0;

    // Singleton instance of this class.
    private static GameManager instance;

    /**
     * Singleton constructor of this class.
     * It loads {@link GameManager#loadGame()}.
     */
    private GameManager() {
        instance = this;
        currentGameType = GameType.NZ;
        loadGame();
    }

    /**
     * Checks whether there is any previously generated instance of this class,
     * if it has, it returns that instance, if not, it generates a new instance
     * and returns it.
     * @return An instance of this class.
     */
    public static GameManager getInstance() {
        return instance == null ? new GameManager() : instance;
    }

    /**
     * This method checks whether the question board has been set up by checking if its null
     * and if it has also been created
     * @return a boolean value on whether the question board has been setup.
     */
    public boolean isQuestionBoardSetUp() {
        if (questionBoardInUse == null) {
            return false;
        } else {
            return questionBoardInUse.isQuestionBoardCreated();
        }
    }

    /**
     * Checking if a question board of the given game type has been answered
     * @param gameType A game type of the question board to check.
     * @return A boolean value that is true if game is finished on a given question board.
     */
    public boolean isGameFinished(GameType gameType) {
        return gameFinished[gameType == GameType.NZ ? 0 : 1];
    }

    /**
     * Set the current question board to answered
     */
    public void setCurrentGameFinished() {
        gameFinished[currentGameType == GameType.NZ ? 0 : 1] = true;
    }

    public void unlockInternationalGame() {
        // Unlock international section and allow user to switch between them.
        GamesMenuController.getInstance().setGameType(GameType.NZ);
        // Generates QuestionBoard instance for international game type
        if(questionBoards[1] == null) {
            initializeQuestionBoard(GameType.INTERNATIONAL);
        }
        isInternationalGameUnlocked = true;
    }

    /**
     * Returns a status on whether the international section has been unlocked.
     * @return A boolean value that is true if the international section has been unlocked.
     */
    public boolean isInternationalGameUnlocked() {
        return isInternationalGameUnlocked;
    }

    /**
     * Returns the current game type this instance of Game manager is dealing with.
     * @return The current game type.
     */
    public GameType getCurrentGameType() {
        return currentGameType;
    }

    /**
     * Returns the question board for the current game.
     * @return GridPane component which contains the question board.
     */
    public GridPane getQuestionBoard() {
        return questionBoardInUse.getQuestionBoard();
    }

    /**
     * Get the question object for the given category index and given question index
     * @param categoryIndex The index of the category from a list of categories in this game manager instance.
     * @param questionIndex The index of the question from a list of questions in the given category.
     * @return A specified Question instance.
     */
    public Question getQuestionInCategory(int categoryIndex, int questionIndex) {
        return questionBoardInUse.getCategory(categoryIndex).getQuestion(questionIndex);
    }

    /**
     * This method checks whether in the currently used question board, every question
     * in each category has been answered.
     * @return A boolean value that is true if every question has been answered.
     */
    public boolean isEveryQuestionAnswered() {
        // If question board that is in use is null, obviously just return false.
        if (questionBoardInUse == null) {
            return false;
        }

        // If there exists a category that is incomplete, immediately return false.
        for (int categoryIndex = 0; categoryIndex < 5; categoryIndex++) {
            if (!isCategoryComplete(categoryIndex)) {
                return false;
            }
        }

        // Return true if every category is at 5 for their lowest value question index
        return true;
    }

    /**
     * Returns whether two or more categories have been completed in the currently used question board.
     * @return A boolean value that is true if two or more categories have been completed.
     */
    public boolean isTwoCategoriesComplete() {
        int numCategoriesComplete = 0;
        for (int categoryIndex = 0; categoryIndex < 5; categoryIndex++) {
            if (isCategoryComplete(categoryIndex)) {
                numCategoriesComplete++;
            }
        }
        return numCategoriesComplete >= 2;
    }

    /**
     * Checks whether the category of given index from the list of categories has been completed;
     * aka every question answered.
     * @param categoryIndex The index of the category to check.
     * @return A boolean value that is true if a given category is complete.
     */
    public boolean isCategoryComplete(int categoryIndex) {
        if (questionBoardInUse == null) {
            return false;
        } else {
            return questionBoardInUse.getCategory(categoryIndex).getLowestValuedQuestionIndex() == 5;
        }
    }

    /**
     * Return the player's current score
     * @return the current score of the player.
     */
    public int getCurrentScore() {
        return currentScore;
    }

    /**
     * Increments the player's current score
     * @param value The value to increment the current score by.
     */
    public void incrementCurrentScore(int value) {
        currentScore += value;
    }

    /**
     * Decrements the player's current score
     * @param value The value to decrement the current score by.
     */
    public void decrementCurrentScore(int value) {
        currentScore -= value;
    }

    /**
     * Initializes question board of the given type; aka creates an instance of it.
     * @param questionBoardTypeToInitialize A game type to initialize a question board.
     */
    public void initializeQuestionBoard(GameType questionBoardTypeToInitialize) {
        if (questionBoardTypeToInitialize == GameType.NZ) {
            questionBoards[0] = new QuestionBoard(questionBoardTypeToInitialize);
        } else if (questionBoardTypeToInitialize == GameType.INTERNATIONAL) {
            questionBoards[1] = new QuestionBoard(questionBoardTypeToInitialize);
        }
    }

    /**
     * Sets the question board of the given game type as the currently using question board.
     * @param gameTypeToSet the game type of the question board to set.
     */
    public void setQuestionBoardInUse(GameType gameTypeToSet) {
        questionBoardInUse = gameTypeToSet == GameType.NZ ? questionBoards[0] : questionBoards[1];
        currentGameType = gameTypeToSet;
    }

    /**
     * Creates a new question board and resets the player's current score to 0 and saves the game
     */
    public void newGame(ArrayList<String> selectedCategories) {
        initializeQuestionBoard(currentGameType);
        setQuestionBoardInUse(currentGameType);
        questionBoardInUse.createBoard(selectedCategories);
//        currentScore = 0;
        saveGame();
    }

    /**
     * Reset the current game by restoring game finished status, question boards, and current score to initial state.
     */
    public void resetGame() {
        gameFinished[0] = false;
        gameFinished[1] = false;

        questionBoards[0] = null;
        questionBoards[1] = null;

        currentScore = 0;

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
        if (questionBoardInUse != null) {
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
                saveWriter.write(currentScore + "," + "\n");

                for (QuestionBoard questionBoard : questionBoards) {

                    if (questionBoard == null || !questionBoard.isQuestionBoardCreated() ) {
                        break;
                    }

                    // Go over the question board categories
                    for (int i = 0; i < questionBoard.getNumCategories(); i++) {
                        // Get the current category
                        Category currentCategory = questionBoard.getCategory(i);
                        // This string saveLine is used as the line that will be saved to the txt
                        String saveLine = currentCategory.toString();

                        // Go over the questions in the categories
                        for (int j = 0; j < questionBoard.getNumQuestions(); j++) {
                            // Get the current question
                            Question currentQuestion = currentCategory.getQuestion(j);
                            // Append this question's line number to the saveLine
                            saveLine += "," + currentQuestion.getLineNumber();
                        }

                        // Write this line with the last index being the lowest valued question index to indicate which questions
                        // were answered or not
                        saveWriter.write(saveLine + "," + currentCategory.getLowestValuedQuestionIndex() + "\n");
                    }
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
                currentScore = Integer.parseInt(lineSplit.get(0));

                // Initialise a new question board
                questionBoards[0] = new QuestionBoard(GameType.NZ);
                questionBoards[1] = new QuestionBoard(GameType.INTERNATIONAL);

                loadQuestionBoard(allLines.subList(1,6),lineSplit,savePath, questionBoards[0],"NZ");
                if (allLines.size()>7) {
                    loadQuestionBoard(allLines.subList(6,11),lineSplit,savePath, questionBoards[1],"international");
                }
                questionBoardInUse = questionBoards[0];
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            questionBoardInUse = null;
        }
    }

    /**
     * Given all the lines of the Save file it goes to the categories folder and finds the category and loads
     * the lines written in the save file and then loads it into the given question board given the location
     * @param allLines All lines of the question board in the save.txt
     * @param lineSplit The split line of the first category
     * @param savePath Save path of the save.txt
     * @param questionBoard Question board to be loaded
     * @param location Location of the categories
     * @throws IOException IOException thrown when file input/output error occurs.
     */
    private void loadQuestionBoard(List<String> allLines,List<String> lineSplit,String savePath,QuestionBoard questionBoard,String location) throws IOException {
        // Go over every line in the save.txt file
        for (int i = 0; i < allLines.size(); i++) {
            // Split it
            lineSplit = Arrays.asList(allLines.get(i).split("\\s*,\\s*"));
            // Create the new category to load and add it
            Category newCategory = new Category(lineSplit.get(0)); // parent
            questionBoard.addCategory(newCategory);

            // Load the categories questions by going to the categories folder to find the question line
            savePath = new File("").getAbsolutePath()+"/categories/" + location + "/"+lineSplit.get(0)+".txt";
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
    }

}
