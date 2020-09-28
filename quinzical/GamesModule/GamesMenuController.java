package quinzical.GamesModule;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import quinzical.MainMenu.MainMenu;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GamesMenuController implements Initializable {

    public Button playGameButton;
    public Button resetGameButton;
    public Button returnToMainMenuButton;
    public Label userScoreLabel;
    public Label bestScoreLabel;

    private MainMenu _mainMenuModel = MainMenu.getInstance();
    private GameManager _gameManager = GameManager.getInstance();

//    private Scene _gamesMenuScene;
    private static GamesMenuController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _instance = this;

        // For testing purposes...
//        _gameManager.incrementCurrentScore(120);
//        _gameManager.updateBestScore();
//        _gameManager.decrementCurrentScore(95);

        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());

        // Update UserScoreLabel and BestScoreLabel with corresponding values.
    }

    public void handlePlayGameButtonAction() {
        try {
            Parent selectQuestion = FXMLLoader.load(getClass().getResource("SelectQuestion/SelectQuestion.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(selectQuestion));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleResetGameButtonAction() {
        GameManager.getInstance().newGame();
        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());
        bestScoreLabel.setText("Best Score: $" + _gameManager.getBestScore());
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
