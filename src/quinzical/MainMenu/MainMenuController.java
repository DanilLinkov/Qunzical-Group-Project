package quinzical.MainMenu;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * The controller for "MainMenu" view. This view allows
 * the player to go to the games menu, practice menu or exit the game
 * <p></p>
 * It takes care of how events caused by button presses in the "MainMenu" view are handled.
 *
 * @author Hyung Park
 */
public class MainMenuController {

    @FXML
    public Button gamesModuleButton, practiceModuleButton, exitButton;

    // Instance of the model of this controller.
    private final MainMenu mainMenuModel = MainMenu.getInstance();

    /**
     * Handles the event of "Games Module" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to games menu scene.
     */
    public void handleGamesModuleButtonClick() {
        try {
            Parent gamesMenu = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/GamesMenu/GamesMenu.fxml"));
            mainMenuModel.setMainStageScene(new Scene(gamesMenu, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event of "Practice Module" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the practice menu scene.
     */
    public void handlePracticeModuleButtonClick() {
        try {
            Parent practiceMenu = FXMLLoader.load(getClass().getResource("/quinzical/PracticeModule/PracticeMenu/PracticeMenu.fxml"));
            mainMenuModel.setMainStageScene(new Scene(practiceMenu, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event of "Exit" button being pressed.
     * <p></p>
     * It closes the main stage by invoking closeMainStage() method of the Main class.
     */
    public void handleExitButton() {
        mainMenuModel.closeMainStage();
    }

}
