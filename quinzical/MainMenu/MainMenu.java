package quinzical.MainMenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import quinzical.GamesModule.GameManager;

import java.util.Optional;

public class MainMenu extends Application {

    private static MainMenu _instance;

    private final static int _appWidth = 800;
    private final static int _appHeight = 600;

    private Stage _mainStage;
    private Scene _mainMenuScene;

    private GameManager _gameManager;

    public static void main(String[] args) {
        launch(args);
    }

    // Initial Program.
    @Override
    public void start(Stage stage) throws Exception {

        _instance = this;
        _gameManager = GameManager.getInstance();

        _mainStage = stage;
        _mainStage.setOnCloseRequest(e -> {
            e.consume();
            closeMainStage();
        });

        Parent mainMenu = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        stage.setTitle("Quinzical");
        stage.setScene((_mainMenuScene = new Scene(mainMenu, _appWidth, _appHeight)));
        stage.show();
    }

    /**
     * A method that returns an instance of this class.
     * @return The instance of the class Main.
     */
    public static MainMenu getInstance() {
        return _instance;
    }

    public static int getAppWidth() {
        return _appWidth;
    }

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
     * Sends a confirmation pop-up asking whether the user really wants to exit game,
     * if the user confirms, saves current state of the game and closes the main stage.
     * Otherwise, aborts closing action.
     */
    public void closeMainStage() {
        Alert confirmClose = new Alert(Alert.AlertType.CONFIRMATION);
        confirmClose.setTitle("Close Game");
        confirmClose.setHeaderText("Do you really want to close game?");
        confirmClose.setContentText("Current game state will be saved.");

        Optional<ButtonType> result = confirmClose.showAndWait();
        if (result.get() == ButtonType.OK) {
            _gameManager.saveGame();
            _mainStage.close();
        }
    }
}
