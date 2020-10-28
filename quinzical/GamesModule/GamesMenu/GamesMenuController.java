package quinzical.GamesModule.GamesMenu;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.MainMenu.MainMenu;
import quinzical.Utilities.HelpUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The controller for "GamesMenu" view.
 * <p></p>
 * It takes care of how events caused by button presses in the "GamesMenu" view are handled.
 *
 * @author Hyung Park
 */
public class GamesMenuController implements Initializable {

    // Components in the view.
    public Button playGameButton;
    public Button resetGameButton;
    public Button returnToMainMenuButton;
    public Button scoreBoardButton;
    public Button switchGameTypeButton;
    public Label userScoreLabel;
    public Label switchGameTypeLabel;
    public Label gameTypeLabel;
    public VBox switchGameTypeArea;

    public Button helpCloseButton;
    public Button helpButton;
    public Label helpLabel;
    public HBox helpArea;

    // Frequently used instances of classes, including current class.
    private final MainMenu _mainMenuModel = MainMenu.getInstance();
    private final GameManager _gameManager = GameManager.getInstance();
    private static GamesMenuController _instance;

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static GamesMenuController getInstance() {
        return _instance;
    }

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;

        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());

        playGameButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());
        scoreBoardButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());
        resetGameButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());

        _gameManager.setQuestionBoardInUse(GameType.NZ);
        checkTwoCategoriesComplete();
    }

    /**
     * Handles the event of "Play Game" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the select question scene.
     */
    public void handlePlayGameButtonAction() {
        if (_gameManager.isGameFinished(_gameManager.getCurrentGameType())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game Finished!");
            alert.setHeaderText("Current game mode has been completed!");
            alert.setContentText("Please play the other game mode or reset to play this game mode again.");
            alert.showAndWait();
            return;
        }
        try {
            if(!_gameManager.isQuestionBoardSetUp()) {
                Parent selectCategories = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectCategories/SelectCategoriesScene.fxml"));
                _mainMenuModel.setMainStageScene(new Scene(selectCategories, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
            } else {
                Parent selectQuestion = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectQuestion/SelectQuestion.fxml"));
                _mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event of "Reset Game" button being pressed.
     * <p></p>
     * It confirms whether the player really wants to reset the game,
     * and if the answer is yes, it resets the game. Then, it updates
     * score labels in GamesMenu to the newly reset scores.
     */
    public void handleResetGameButtonAction() {
        Alert confirmReset = new Alert(Alert.AlertType.CONFIRMATION);

        // Formats texts inside the pop up.
        confirmReset.setTitle("Reset Game");
        confirmReset.setHeaderText("Do you really want to reset game?");
        confirmReset.getDialogPane().setContent(new Label("This will clear current winning and question board status."));

        Optional<ButtonType> result = confirmReset.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Reset game and update score labels
            GameManager.getInstance().resetGame();
            // Revert back to NZ Game Type
            setGameType(GameType.NZ);
            lockInternationalSection();
            _gameManager.resetGameFinished();
            userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());

            // Another alert pop up notifying the player that the game has successfully reset.
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reset Game");
            alert.setHeaderText("Your game has been reset!");
            alert.setContentText("Press OK to continue.");
            alert.showAndWait();
        }
    }

    /**
     * Handles the event of "Return to Main Menu" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the main menu scene.
     */
    public void handleReturnToMainMenuButtonAction() {
        _mainMenuModel.returnToMainMenuScene();
    }

    /**
     * Changes the scene of the main stage to the main menu.
     */
    public void setMainStageToGamesMenuScene() {
        // Refresh score labels for possible change.
        userScoreLabel.setText("Current Score: $" + _gameManager.getCurrentScore());

        _mainMenuModel.setMainStageScene(playGameButton.getScene());
    }

    public void handleScoreBoardButton() {
        try {
            Parent scoreBoard = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/ScoreBoard/ScoreBoardScene.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(scoreBoard, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"text");
        HelpUtilities.bringToFront(helpArea);
    }

    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

    private void configureSwitchGameTypeButton(GameType gameTypeToUseButton) {
        if (gameTypeToUseButton == GameType.NZ) {
            ImageView buttonImage = new ImageView();
            buttonImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/GamesMenu/Images/globe.png").toExternalForm()));
            buttonImage.setFitHeight(50);
            buttonImage.setFitWidth(50);
            switchGameTypeButton.setGraphic(buttonImage);
            switchGameTypeButton.setOnAction(e -> setGameType(GameType.INTERNATIONAL));
        } else if (gameTypeToUseButton == GameType.INTERNATIONAL) {
            ImageView buttonImage = new ImageView();
            buttonImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/GamesMenu/Images/nz.png").toExternalForm()));
            buttonImage.setFitHeight(50);
            buttonImage.setFitWidth(50);
            switchGameTypeButton.setGraphic(buttonImage);
            switchGameTypeButton.setOnAction(e -> setGameType(GameType.NZ));
        }
    }

    public void setGameType(GameType gameTypeToSet) {
        configureSwitchGameTypeButton(gameTypeToSet);

        if (gameTypeToSet == GameType.NZ) {
            switchGameTypeLabel.setText("to Int'l Game");
            gameTypeLabel.setVisible(false);
            _gameManager.setQuestionBoardInUse(GameType.NZ);
        } else if (gameTypeToSet == GameType.INTERNATIONAL) {
            switchGameTypeLabel.setText("to NZ Game");
            gameTypeLabel.setVisible(true);
            _gameManager.setQuestionBoardInUse(GameType.INTERNATIONAL);
        }

        switchGameTypeArea.setVisible(true);

    }

    public void lockInternationalSection() {
        setGameType(GameType.NZ);
        switchGameTypeArea.setVisible(false);
        gameTypeLabel.setVisible(false);
    }

    private void checkTwoCategoriesComplete() {
        int numCategoriesComplete = 0;
        for (int categoryIndex = 0; categoryIndex < 5; categoryIndex++) {
            if (_gameManager.isCategoryComplete(categoryIndex)) {
                numCategoriesComplete++;
            }
        }
        if (numCategoriesComplete >= 2) {
            _gameManager.unlockInternationalGame();
            setGameType(GameType.NZ);
        }
    }

}
