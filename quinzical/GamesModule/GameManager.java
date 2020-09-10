package quinzical.GamesModule;

import javafx.scene.layout.GridPane;

public class GameManager {

    private QuestionBoard _questionBoard;
    private int _currentScore;
    private int _bestScore;

    private static GameManager _instance;

    public GameManager() {
        _instance = this;

        /*
        if (settings_file_exists) {
            // Load previous game from the file
            loadGame();
        } else {
            // Create a new game
            newGame();
            saveGame();
        }

         */
    }

    public static GameManager getInstance() {
        return _instance;
    }

    public GridPane getQuestionBoard() {
        return _questionBoard.getQuestionBoard();
    }

    public void newGame() {
        _questionBoard = new QuestionBoard();
        _currentScore = 0;
        saveGame();
    }

    public void saveGame() {

    }

    public void loadGame() {

    }

}
