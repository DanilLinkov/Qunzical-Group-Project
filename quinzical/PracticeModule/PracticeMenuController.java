package quinzical.PracticeModule;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import quinzical.MainMenu.MainMenu;
import quinzical.PracticeModule.AskQuestion.AskPracticeQuestionController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PracticeMenuController implements Initializable {

    // fxml objects used in the scene
    public Button selectCategoryButton;
    public Button backToMainMenuButton;
    public ComboBox<String> dropDownMenu;

    // Used to store the selected category from the drop down menu
    private String selectedCategory;

    // Used for scene transitioning
    private MainMenu _mainMenuModel = MainMenu.getInstance();
    private PracticeGameManager _practiceGameManager;

    // Creating its own static instance for scene transitioning
    private static PracticeMenuController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;
        _practiceGameManager = new PracticeGameManager();
        // Setting the drop down list to have all the category files
        dropDownMenu.setItems(FXCollections.observableList(_practiceGameManager.getCategories()));
    }

    /**
     * This method is used to return the instance of this controller
     * @return
     */
    public static PracticeMenuController getInstance() {
        return _instance;
    }

    /**
     * Return back to the main menu
     */
    public void handleBackToMainMenuButton() {
        MainMenu.getInstance().returnToMainMenuScene();
    }

    /**
     * When the user selects a category and clicks submit
     */
    public void handleSelectCategoryButton() {
        try {
            // Check that they have actually selected a category
            if(selectedCategory!=null) {
                // Load the ask practice question scene
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/PracticeModule/AskQuestion/AskPracticeQuestion.fxml"));
                Parent root = loader.load();

                AskPracticeQuestionController askPracticeQuestionController = loader.getController();
                // Set its category name equal to the selected category name which is then used inside that controller
                askPracticeQuestionController.setCategoryName(selectedCategory);

                _mainMenuModel.setMainStageScene(new Scene(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Save the selected category when the user selects something from the drop down list
     */
    public void handleDropDownClick() {
        selectedCategory = dropDownMenu.getValue();
    }

    /**
     * Method used to set the main stage into this scene
     */
    public void setMainStageToPracticeMenuScene() {
        _mainMenuModel.setMainStageScene(selectCategoryButton.getScene());
    }

}
