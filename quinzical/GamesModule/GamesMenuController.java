package quinzical.GamesModule;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import quinzical.MainMenu.MainMenu;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class GamesMenuController implements Initializable {

    public Button playGameButton;
    public Button resetGameButton;
    public Button returnToMainMenuButton;
    public Label userScoreLabel;
    public Label bestScoreLabel;

    private MainMenu _mainMenuModel = MainMenu.getInstance();
    private GameManager _gameManager = GameManager.getInstance();

    private static GamesMenuController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;

        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());
    }

    public void handlePlayGameButtonAction() {
        try {
            Parent selectQuestion = FXMLLoader.load(getClass().getResource("SelectQuestion/SelectQuestion.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResetGameButtonAction() {
        Alert confirmReset = new Alert(Alert.AlertType.CONFIRMATION);
        confirmReset.setTitle("Reset Game");
        confirmReset.setHeaderText("Do you really want to reset game?");
        confirmReset.getDialogPane().setContent(
                new Label("This will clear current winning and question board status."));

        Optional<ButtonType> result = confirmReset.showAndWait();
        if (result.get() == ButtonType.OK) {
            GameManager.getInstance().newGame();
            userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
            bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reset Game");
            alert.setHeaderText("Your game has been reset!");
            alert.setContentText("Press OK to continue.");
            alert.showAndWait();
        }
    }

    public void handleReturnToMainMenuButtonAction() {
        _mainMenuModel.returnToMainMenuScene();
    }

    public static GamesMenuController getInstance() {
        return _instance;
    }

    public void setMainStageToGamesMenuScene() {

        // Refresh score labels for possible change.
        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());

        _mainMenuModel.setMainStageScene(playGameButton.getScene());
    }

}
