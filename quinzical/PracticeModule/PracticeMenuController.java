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

    public Button selectCategoryButton;
    public Button backToMainMenuButton;
    public ComboBox<String> dropDownMenu;

    private String selectedCategory;

    private MainMenu _mainMenuModel = MainMenu.getInstance();
    private PracticeGameManager _practiceGameManager;

    private static PracticeMenuController _instance;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        _instance = this;

        _practiceGameManager = new PracticeGameManager();
        dropDownMenu.setItems(FXCollections.observableList(_practiceGameManager.getCategories()));

    }

    public static PracticeMenuController getInstance() {
        return _instance;
    }

    public void handleBackToMainMenuButton() {
        MainMenu.getInstance().returnToMainMenuScene();
    }

    public void handleSelectCategoryButton() {
        try {
            if(selectedCategory!=null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/quinzical/PracticeModule/AskQuestion/AskPracticeQuestion.fxml"));
                Parent root = loader.load();

                AskPracticeQuestionController askPracticeQuestionController = loader.getController();
                askPracticeQuestionController.setCategoryName(selectedCategory);

                _mainMenuModel.setMainStageScene(new Scene(root, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleDropDownClick() {
        selectedCategory = dropDownMenu.getValue();
    }

    public void setMainStageToPracticeMenuScene() {
        _mainMenuModel.setMainStageScene(selectCategoryButton.getScene());
    }

}
