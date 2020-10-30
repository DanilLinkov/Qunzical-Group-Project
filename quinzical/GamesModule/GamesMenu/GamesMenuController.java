package quinzical.GamesModule.GamesMenu;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.MainMenu.MainMenu;
import quinzical.Utilities.HelpUtilities;
import quinzical.Utilities.Notification;

import java.io.IOException;
import java.net.URL;
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
    private final MainMenu mainMenuModel = MainMenu.getInstance();
    private final GameManager gameManager = GameManager.getInstance();
    private static GamesMenuController instance;

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static GamesMenuController getInstance() {
        return instance;
    }

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;

        userScoreLabel.setText("Current Score: $" + gameManager.getCurrentScore());

        playGameButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());
        scoreBoardButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());
        resetGameButton.prefWidthProperty().bind(returnToMainMenuButton.widthProperty());

        gameManager.setQuestionBoardInUse(GameType.NZ);

        // Checks whether international section should be unlocked
        if (gameManager.isTwoCategoriesComplete()) {
            gameManager.unlockInternationalGame();
            setGameType(GameType.NZ);
        }
        if (gameManager.isEveryQuestionAnswered()) {
            gameManager.setCurrentGameFinished();
        }
    }

    /**
     * Handles the event of "Play Game" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the select question scene.
     */
    public void handlePlayGameButtonAction() {
        if (gameManager.isGameFinished(gameManager.getCurrentGameType())) {
            Notification.smallInformationPopup("Game Finished",
                    "Current game mode has been completed",
                    "Please play the other game mode or reset to play this game mode again.");
            return;
        }
        try {
            if(!gameManager.isQuestionBoardSetUp()) {
                Parent selectCategories = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectCategories/SelectCategories.fxml"));
                mainMenuModel.setMainStageScene(new Scene(selectCategories, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
            } else {
                Parent selectQuestion = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectQuestion/SelectQuestion.fxml"));
                mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
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
        boolean resetGame = Notification.confirmationPopup("Reset Game",
                "Do you really want to reset game?",
                "This will clear current winning and question board status.");

        if (resetGame) {
            // Reset game and update score labels
            gameManager.resetGame();
            // Revert back to NZ Game Type
            setGameType(GameType.NZ);
            lockInternationalSection();
            userScoreLabel.setText("Current Score: $" + gameManager.getCurrentScore());

            // Another alert pop up notifying the player that the game has successfully reset.
            Notification.smallInformationPopup("Reset Game", "Your game has been reset!", "Press OK to continue.");
        }
    }

    /**
     * Handles the event of "Return to Main Menu" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the main menu scene.
     */
    public void handleReturnToMainMenuButtonAction() {
        mainMenuModel.returnToMainMenuScene();
    }

    /**
     * Changes the scene of the main stage to the main menu.
     */
    public void setMainStageToGamesMenuScene() {
        // Refresh score labels for possible change.
        userScoreLabel.setText("Current Score: $" + gameManager.getCurrentScore());

        mainMenuModel.setMainStageScene(playGameButton.getScene());
    }

    public void handleScoreBoardButton() {
        try {
            Parent scoreBoard = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/ScoreBoard/ScoreBoardScene.fxml"));
            mainMenuModel.setMainStageScene(new Scene(scoreBoard, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
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
            ImageView buttonImage = new ImageView();

        if (gameTypeToUseButton == GameType.NZ) {
            buttonImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/GamesMenu/Images/globe.png").toExternalForm()));
            switchGameTypeButton.setOnAction(e -> setGameType(GameType.INTERNATIONAL));
        } else if (gameTypeToUseButton == GameType.INTERNATIONAL) {
            buttonImage.setImage(new Image(this.getClass().getResource("/quinzical/GamesModule/GamesMenu/Images/nz.png").toExternalForm()));
            switchGameTypeButton.setOnAction(e -> setGameType(GameType.NZ));
        }

            buttonImage.setFitHeight(50);
            buttonImage.setFitWidth(50);
            switchGameTypeButton.setGraphic(buttonImage);
    }

    public void setGameType(GameType gameTypeToSet) {
        configureSwitchGameTypeButton(gameTypeToSet);

        if (gameTypeToSet == GameType.NZ) {
            switchGameTypeLabel.setText("to Int'l Game");
            gameTypeLabel.setVisible(false);
            gameManager.setQuestionBoardInUse(GameType.NZ);
        } else if (gameTypeToSet == GameType.INTERNATIONAL) {
            switchGameTypeLabel.setText("to NZ Game");
            gameTypeLabel.setVisible(true);
            gameManager.setQuestionBoardInUse(GameType.INTERNATIONAL);
        }

        switchGameTypeArea.setVisible(true);

    }

    public void lockInternationalSection() {
        setGameType(GameType.NZ);
        switchGameTypeArea.setVisible(false);
        gameTypeLabel.setVisible(false);
    }

}
