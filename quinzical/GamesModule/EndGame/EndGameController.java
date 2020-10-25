package quinzical.GamesModule.EndGame;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.ScoreBoard.ScoreBoardManager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

public class EndGameController implements Initializable {
    public Label scoreLabel;
    public TextField nameTextField;
    public Button submitButton;
    public Button dontSaveButton;
    public ImageView rewardImage;

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
            rewardImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/EndGame/RewardGifs/best.gif").toExternalForm()));
            scoreLabel.setText("Congratulations on completing the game!\n\nYou have achieved a new high score of $" + playerScore + "!" +
                    "\nPlease enter your name and hit submit if you would like to save your score." +
                    "\nOtherwise you could click Don't Save Score.");
        }
        else {
            rewardImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/EndGame/RewardGifs/notBest.gif").toExternalForm()));
            scoreLabel.setText("Congratulations on completing the game!\n\nYou have achieved a score of $" + playerScore + "!" +
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
