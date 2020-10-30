package quinzical.GamesModule.EndGame;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.GamesModule.ScoreBoard.ScoreBoardManager;
import quinzical.Utilities.TTSUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class GameEndController implements Initializable {

    @FXML
    private Button submitButton, dontSaveButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private ImageView rewardImage;

    private static GameEndController instance;
    private final GameManager gameManager = GameManager.getInstance();
    private final ScoreBoardManager scoreBoardManager = ScoreBoardManager.getInstance();
    private final GamesMenuController gamesMenuController = GamesMenuController.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        setScoreLabel();

        BooleanBinding isTextFieldEmpty = Bindings.isEmpty(nameTextField.textProperty());
        submitButton.disableProperty().bind(isTextFieldEmpty);
    }

    public static GameEndController getInstance() {
        return instance;
    }

    private void setScoreLabel() {
        int playerScore = gameManager.getCurrentScore();
        if(playerScore > scoreBoardManager.getBestScore()) {
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
        System.out.println(userScore);
        scoreBoardManager.addScore(nameTextField.getText(), userScore);
        finishGameEnd();
    }

    public void handleDontSaveButton() {
        finishGameEnd();
    }

    private void finishGameEnd() {
        gameManager.resetGame();

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "rm -r save/");
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Revert everything in games menu back to initial state
        gamesMenuController.setGameType(GameType.NZ);
        gamesMenuController.lockInternationalSection();
        gamesMenuController.setMainStageToGamesMenuScene();
    }

}
