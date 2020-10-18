package quinzical.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import quinzical.GamesModule.GameManager;
import quinzical.Utilities.AskQuestionUtilities;

import java.util.Optional;

/**
 * The main class / application of Quinzical, and the model for "MainMenu" MVC.
 * <p></p>
 * This class holds application-wide information and methods that can be used
 * regardless of scenes or views. (such as window sizes or closing window)
 *
 * @author Hyung Park
 */
public class MainMenu extends Application {

    // Constants for the window size of the application.
    private final static int _appWidth = 800;
    private final static int _appHeight = 600;

    // References of MainMenu stage and scene to be reused.
    private Stage _mainStage;
    private Scene _mainMenuScene;

    // Frequently used instances of classes, including current class.
    private GameManager _gameManager;
    private static MainMenu _instance;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A method that returns an instance of this class.
     * @return The instance of the class Main.
     */
    public static MainMenu getInstance() {
        return _instance;
    }

    /**
     * Method called to start JavaFX application.
     * @param stage The main stage of this application.
     * @throws Exception Possible exceptions created in the process of starting the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        _instance = this;
        _gameManager = GameManager.getInstance();

        _mainStage = stage;
        _mainStage.setOnCloseRequest(e -> {
            e.consume();
            closeMainStage();
        });

        Parent mainMenu = FXMLLoader.load(getClass().getResource("/quinzical/MainMenu/MainMenu.fxml"));
        stage.setTitle("Quinzical");
        stage.setScene((_mainMenuScene = new Scene(mainMenu, _appWidth, _appHeight)));
        stage.show();
    }

    /**
     * Returns the width of this application.
     * @return the width of this application.
     */
    public static int getAppWidth() {
        return _appWidth;
    }

    /**
     * Returns the height of this application.
     * @return the height of this application.
     */
    public static int getAppHeight() {
        return _appHeight;
    }

    /**
     * Changes the scene of the main stage back to the main menu.
     */
    public void returnToMainMenuScene() {
        _mainStage.setScene(_mainMenuScene);
    }

    /**
     * Changes the scene of the main stage to the given scene from the argument.
     * (essentially changing what is being displayed on the application window)
     * @param sceneToSet A scene to set to the main stage.
     */
    public void setMainStageScene(Scene sceneToSet) {
        _mainStage.setScene(sceneToSet);
    }

    /**
     * Sends a confirmation pop-up asking whether the player really wants to exit game,
     * if the player confirms, saves current state of the game and closes the main stage.
     * Otherwise, aborts closing action.
     */
    public void closeMainStage() {
        Alert confirmClose = new Alert(Alert.AlertType.CONFIRMATION);

        // Formats texts inside the pop up.
        confirmClose.setTitle("Close Game");
        confirmClose.setHeaderText("Do you really want to close game?");
        confirmClose.setContentText("Current game state will be saved.");

        Optional<ButtonType> result = confirmClose.showAndWait();
        if (result.get() == ButtonType.OK) {
            // Clean up temporary TTS files.
            AskQuestionUtilities.ttsCleanUp();

            _gameManager.saveGame();
            _mainStage.close();

            System.exit(0);
        }
    }
}
