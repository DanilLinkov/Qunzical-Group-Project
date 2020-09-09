package quinzical.GamesModule.SelectQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class SelectQuestionController implements Initializable {

    public Button BackToGameMenuButton;
    public VBox QuestionBoardArea;
    public Label UserScoreLabel;
    public Label BestScoreLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Add question board to QuestionBoard area
        // Update UserScoreLabel and BestScoreLabel with corresponding scores.
    }

    public void handleReturnToGameMenuButtonAction() {
        // Implementation
    }

}
