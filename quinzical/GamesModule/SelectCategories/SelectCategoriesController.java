package quinzical.GamesModule.SelectCategories;

import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import quinzical.MainMenu.MainMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class SelectCategoriesController implements Initializable {

    public Button CancelButton;
    public Button SubmitButton;

    public ComboBox<String> category1;
    public ComboBox<String> category2;
    public ComboBox<String> category3;
    public ComboBox<String> category4;
    public ComboBox<String> category5;

    private ArrayList<String> _categories;
    private String[] selectedCategories;

    private MainMenu _mainMenuModel = MainMenu.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadAllCategories();
        selectedCategories = new String[5];

        category1.setItems(FXCollections.observableList(_categories));
        category2.setItems(FXCollections.observableList(_categories));
        category3.setItems(FXCollections.observableList(_categories));
        category4.setItems(FXCollections.observableList(_categories));
        category5.setItems(FXCollections.observableList(_categories));

    }

    public void handleCategory1Click() {
        selectedCategories[0] = category1.getValue();
    }

    public void handleCategory2Click() {
        selectedCategories[1] = category2.getValue();
    }

    public void handleCategory3Click() {
        selectedCategories[2] = category3.getValue();
    }

    public void handleCategory4Click() {
        selectedCategories[3] = category4.getValue();
    }

    public void handleCategory5Click() {
        selectedCategories[4] = category5.getValue();
    }

    /**
     * Return back to the main menu
     */
    public void handleBackToMainMenuButton() {
        MainMenu.getInstance().returnToMainMenuScene();
    }

    /**
     * Method used to set the main stage into this scene
     */
    public void handleSubmitButton() {
        try {
            Parent selectQuestion = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectQuestion/SelectQuestion.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadAllCategories() {
        // Used to store all the file paths in the folder
        ArrayList<String> filePaths;

        // Getting the categories folder path
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories";
        // Creating a file object based on the path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the file paths in that folder
        filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));
        _categories = new ArrayList<>();

        // Adding them to the list
        for (String filePath : filePaths) {
            _categories.add(filePath.replace(".txt", ""));
        }
    }

}
