package quinzical.GamesModule.EndGame;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.GamesModule.ScoreBoard.ScoreBoardManager;

import java.net.URL;
import java.util.ResourceBundle;

public class GameEndController implements Initializable {
    public Label scoreLabel;
    public TextField nameTextField;
    public Button submitButton;
    public Button dontSaveButton;
    public ImageView rewardImage;

    private static GameEndController instance;
    private final GameManager gameManager = GameManager.getInstance();
    private final ScoreBoardManager scoreBoardManager = ScoreBoardManager.getInstance();

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
    }

    public void handleDontSaveButton() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

}
