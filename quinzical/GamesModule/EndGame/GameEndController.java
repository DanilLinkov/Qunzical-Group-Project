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

/**
 * The controller for a view of the game end screen which is the
 * reward screen which depending on whether the user got a new
 * best score shows a different gif and asks them to enter
 * their name to save their score or they can choose not to save the score
 * <p></p>
 * It takes core of how events caused by the GameEnd view are handled
 *
 * @author Hyung Park, Danil Linkov
 */
public class GameEndController implements Initializable {

    @FXML
    private Button submitButton, dontSaveButton;
    @FXML
    private Label scoreLabel;
    @FXML
    private TextField nameTextField;
    @FXML
    private ImageView rewardImage;

    // Common class instances used throughout this class
    private static GameEndController instance;
    private final GameManager gameManager = GameManager.getInstance();
    private final ScoreBoardManager scoreBoardManager = ScoreBoardManager.getInstance();
    private final GamesMenuController gamesMenuController = GamesMenuController.getInstance();

    /**
     * This method initializes this controller when a scene transitions to the view of this controller
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        // Setting the reward screen scene based on the score the user achieved
        setScoreLabel();

        // Disabling the submit button if the user has not entered their name
        BooleanBinding isTextFieldEmpty = Bindings.isEmpty(nameTextField.textProperty());
        submitButton.disableProperty().bind(isTextFieldEmpty);
    }

    /**
     * Returns an instance of this class to be able to allow for scene transitioning
     * @return
     */
    public static GameEndController getInstance() {
        return instance;
    }

    /**
     * Sets the reward screen label and background gif image based on if the user got a higher score
     * than the current best score in the score.txt
     */
    private void setScoreLabel() {
        // Getting the current score the user achieved
        int playerScore = gameManager.getCurrentScore();

        // If the current score is better than the best score than show this
        if(playerScore > scoreBoardManager.getBestScore()) {
            rewardImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/EndGame/RewardGifs/best.gif").toExternalForm()));
            scoreLabel.setText("Congratulations on completing the game!\n\nYou have achieved a new high score of $" + playerScore + "!" +
                    "\nPlease enter your name and hit submit if you would like to save your score." +
                    "\nOtherwise you could click Don't Save Score.");
        }
        // Else a different message and gif is shown
        else {
            rewardImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/EndGame/RewardGifs/notBest.gif").toExternalForm()));
            scoreLabel.setText("Congratulations on completing the game!\n\nYou have achieved a score of $" + playerScore + "!" +
                    "\nPlease enter your name and hit submit if you would like to save your score." +
                    "\nOtherwise you could click Don't Save Score.");
        }
    }

    /**
     * Handles the submit button for saving the user score after they have entered their name
     */
    public void handleSubmitButton() {
        // Get the current user score
        int userScore = gameManager.getCurrentScore();
        // Add it to the score board
        scoreBoardManager.addScore(nameTextField.getText(), userScore);
        // Delete the save file
        finishGameEnd();
    }

    /**
     * Don;t save the user score and just delete the save file to reset the game
     */
    public void handleDontSaveButton() {
        finishGameEnd();
    }

    /**
     * Resets teh game by deleting the save file
     */
    private void finishGameEnd() {
        // Reset the use score in the game manager and the question board
        gameManager.resetGame();

        // Delete the save file
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
