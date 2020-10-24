package quinzical.GamesModule.EndGame;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.ScoreBoard.ScoreBoardManager;

import java.net.URL;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    public Label scoreLabel;
    public TextField nameTextField;
    public Button submitButton;
    public Button dontSaveButton;

    private static EndGameController _instance;
    private GameManager gameManager = GameManager.getInstance();
    private ScoreBoardManager scoreBoardManager = ScoreBoardManager.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;
        setScoreLabel();
    }

    public static EndGameController getInstance() {
        return _instance;
    }

    private void setScoreLabel() {
        int playerScore = GameManager.getInstance().getCurrentScore();
        if(playerScore > ScoreBoardManager.getInstance().getBestScore()) {
            scoreLabel.setText("Congratulations on completing the game!\n You have achieved a new high score of $" + playerScore + "!" +
                    "\nPlease enter your name and hit submit if you would like to save your score." +
                    "\nOtherwise you could click Don't Save Score.");
        }
        else {
            scoreLabel.setText("Congratulations on completing the game!\n You have achieved a score of $" + playerScore + "!" +
                    "\nPlease enter your name and hit submit if you would like to save your score." +
                    "\nOtherwise you could click Don't Save Score.");
        }
    }

    public void handleSubmitButton() {
        int userScore = gameManager.getCurrentScore();
        scoreBoardManager.addScore(nameTextField.getText(),userScore);
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
        GameManager.getInstance().resetGame();
    }

    public void handleDontSaveButton() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
        GameManager.getInstance().resetGame();
    }

}
