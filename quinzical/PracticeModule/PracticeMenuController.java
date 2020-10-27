package quinzical.PracticeModule;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import quinzical.MainMenu.MainMenu;
import quinzical.PracticeModule.AskQuestion.AskPracticeQuestionController;
import quinzical.Utilities.HelpUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for a view for the player to select
 * a category to practice and then pass down the category selected to
 * ask practice question controller
 * <p></p>
 * It takes care of how events caused by button presses in the "PracticeMenu" view are handled.
 *
 * @author Danil Linkov
 */
public class PracticeMenuController implements Initializable {

    // fxml objects used in the scene
    public Button selectCategoryButton;
    public Button backToMainMenuButton;
    public ComboBox<String> dropDownMenu;

    public Button helpCloseButton;
    public Button helpButton;
    public Label helpLabel;
    public HBox helpArea;
    public ToggleButton locationToggle;

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
        _practiceGameManager.loadAllCategories("NZ");
        // Setting the drop down list to have all the category files
        dropDownMenu.setItems(FXCollections.observableList(_practiceGameManager.getCategories()));

        BooleanBinding isCategoryNull = Bindings.isNull(dropDownMenu.valueProperty());
        selectCategoryButton.disableProperty().bind(isCategoryNull);
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
     * When the player selects a category and clicks submit
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
                askPracticeQuestionController.setCategoryName(selectedCategory,locationToggle.isSelected() ? "international":"NZ" );

                _mainMenuModel.setMainStageScene(new Scene(root));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleToggleClick() {
        String location = locationToggle.isSelected() ? "international":"NZ";
        _practiceGameManager.loadAllCategories(location);
        dropDownMenu.setItems(FXCollections.observableList(_practiceGameManager.getCategories()));
        dropDownMenu.setPromptText("Select "+location + " category");
    }

    /**
     * Save the selected category when the player selects something from the drop down list
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

    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"text");
        HelpUtilities.bringToFront(helpArea);
    }

    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

}
