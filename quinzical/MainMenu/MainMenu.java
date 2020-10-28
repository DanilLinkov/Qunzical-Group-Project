package quinzical.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.GamesModule.GameManager;
import quinzical.Utilities.Notification;
import quinzical.Utilities.TTSUtilities;

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
    private final static int APP_WIDTH = 800;
    private final static int APP_HEIGHT = 600;

    // References of MainMenu stage and scene to be reused.
    private Stage mainStage;
    private Scene mainMenuScene;

    // Frequently used instances of classes, including current class.
    private GameManager gameManager;
    private static MainMenu instance;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * A method that returns an instance of this class.
     * @return The instance of the class Main.
     */
    public static MainMenu getInstance() {
        return instance;
    }

    /**
     * Method called to start JavaFX application.
     * @param stage The main stage of this application.
     * @throws Exception Possible exceptions created in the process of starting the application.
     */
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        gameManager = GameManager.getInstance();

        mainStage = stage;
        mainStage.setOnCloseRequest(e -> {
            e.consume();
            closeMainStage();
        });

        Parent mainMenu = FXMLLoader.load(getClass().getResource("/quinzical/MainMenu/MainMenu.fxml"));
        stage.setTitle("Quinzical");
        stage.setScene((mainMenuScene = new Scene(mainMenu, APP_WIDTH, APP_HEIGHT)));
        stage.show();
    }

    /**
     * Returns the width of this application.
     * @return the width of this application.
     */
    public static int getAppWidth() {
        return APP_WIDTH;
    }

    /**
     * Returns the height of this application.
     * @return the height of this application.
     */
    public static int getAppHeight() {
        return APP_HEIGHT;
    }

    /**
     * Changes the scene of the main stage back to the main menu.
     */
    public void returnToMainMenuScene() {
        mainStage.setScene(mainMenuScene);
    }

    /**
     * Changes the scene of the main stage to the given scene from the argument.
     * (essentially changing what is being displayed on the application window)
     * @param sceneToSet A scene to set to the main stage.
     */
    public void setMainStageScene(Scene sceneToSet) {
        mainStage.setScene(sceneToSet);
    }

    /**
     * Sends a confirmation pop-up asking whether the player really wants to exit game,
     * if the player confirms, saves current state of the game and closes the main stage.
     * Otherwise, aborts closing action.
     */
    public void closeMainStage() {
        boolean closeGame = Notification.confirmationPopup("Close Game",
                "Do you really want to close game?",
                "Current game state will be saved.");

        if (closeGame) {
            // Clean up temporary TTS files.
            TTSUtilities.ttsCleanUp();

            gameManager.saveGame();
            mainStage.close();

            System.exit(0);
        }
    }
}
