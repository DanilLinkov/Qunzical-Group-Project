package quinzical.MainMenu;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;

public class MainMenuController {

    // Button components in MainMenu view.
    public Button gamesModuleButton;
    public Button practiceModuleButton;
    public Button exitButton;

    // Instance of the model of this controller.
    private MainMenu _mainMenuModel = MainMenu.getInstance();

    public void handleGamesModuleButtonClick() {
        try {
            Parent gamesMenu = FXMLLoader.load(getClass().getResource("../GamesModule/GamesMenu.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(gamesMenu));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handlePracticeModuleButtonClick() {
        try {
            Parent practiceMenu = FXMLLoader.load(getClass().getResource("../PracticeModule/PracticeMenu.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(practiceMenu));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleExitButton() {
        _mainMenuModel.closeMainStage();
    }

}
