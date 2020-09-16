package quinzical.PracticeModule;

import javafx.scene.control.Button;
import quinzical.MainMenu.MainMenu;

public class PracticeMenuController {

    public Button selectCategoryButton;
    public Button backToMainMenuButton;

    public void handleSelectCategoryButton() {
        // Ask Question
    }

    public void handleBackToMainMenuButton() {
        MainMenu.getInstance().returnToMainMenuScene();
    }

}
