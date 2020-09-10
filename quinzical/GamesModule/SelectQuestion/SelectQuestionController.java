package quinzical.GamesModule.SelectQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.QuestionBoard;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectQuestionController implements Initializable {

    public Button BackToGameMenuButton;
    public VBox QuestionBoardArea;
    public Label UserScoreLabel;
    public Label BestScoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        GameManager jsdlfd = new GameManager();
        QuestionBoardArea.getChildren().add(GameManager.getInstance().getQuestionBoard());
//        GameModelClass.saveGame();
    }

    public void handleReturnToGameMenuButtonAction() {
        // Implementation
    }

}
