package quinzical.GamesModule.SelectQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.QuestionBoard;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectQuestionController implements Initializable {

    public Button backToGameMenuButton;
    public VBox questionBoardArea;
    public Label userScoreLabel;
    public Label bestScoreLabel;

    private GameManager _gameManager = GameManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());

        questionBoardArea.getChildren().add(GameManager.getInstance().getQuestionBoard());
//        GameModelClass.saveGame();
    }

    public void handleReturnToGameMenuButtonAction() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

}
