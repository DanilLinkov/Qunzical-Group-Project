package quinzical.GamesModule.SelectQuestion;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Question;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SelectQuestionController implements Initializable {

    public Button backToGameMenuButton;
    public VBox questionBoardArea;
    public Label userScoreLabel;
    public Label bestScoreLabel;

    // Instances stored for accessing respective instances.
    private static SelectQuestionController _instance;
    private GameManager _gameManager = GameManager.getInstance();

    private Question _selectedQuestion;

    /**
     * A constructor for this class which generates an instance of this class and stores it for other classes to use.
     */
    public SelectQuestionController() {
        _instance = this;
    }

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static SelectQuestionController getInstance() {
        return _instance;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());

        questionBoardArea.getChildren().add(GameManager.getInstance().getQuestionBoard());
    }

    /**
     * Handles the event of points button in the question board being pressed.
     * <p></p>
     * It creates a Question object with a given question, then stores it
     * in this class for AskQuestionController to access it to ask question.
     * @param categoryIndex The index of the category of the question in the list of categories.
     * @param questionIndex The index of the question in its category.
     */
    public void handlePointButtonAction(int categoryIndex, int questionIndex) {

        _selectedQuestion = _gameManager.getQuestionInCategory(categoryIndex, questionIndex);

        try {
            Parent askQuestionWindow = FXMLLoader.load(getClass().getResource("../AskQuestion/askQuestion.fxml"));
            MainMenu.getInstance().setMainStageScene(new Scene(askQuestionWindow, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the question the user has selected.
     * @return The question the user has selected.
     */
    public Question getSelectedQuestion() {
        return _selectedQuestion;
    }

    public void handleReturnToGameMenuButtonAction() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

}
