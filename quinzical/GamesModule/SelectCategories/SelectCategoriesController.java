package quinzical.GamesModule.SelectCategories;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Category;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

public class SelectCategoriesController implements Initializable {

    public Button selectButton;
    public Button randomButton;
    public Button backButton;
    public VBox gridArea;

    private GameManager gameManager = GameManager.getInstance();
    private GamesMenuController gamesMenuController = GamesMenuController.getInstance();
    private final MainMenu _mainMenuModel = MainMenu.getInstance();
    private static SelectCategoriesController _instance;

    private ArrayList<String> _categories;
    private ObservableList<String> selectedCategories;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;
        loadAllCategories();
        selectedCategories = FXCollections.observableArrayList();

        gridArea.getChildren().clear();
        gridArea.getChildren().add(getQuestionBoard());
    }

    public static SelectCategoriesController getInstance() {
        return _instance;
    }

    public GridPane getQuestionBoard() {
        GridPane categoriesBoard = new GridPane();
        categoriesBoard.setGridLinesVisible(false);
        categoriesBoard.setStyle("-fx-background-color:#FFFFFF");

        int i = 0;
        int j = 0;

        for (String category : _categories) {
            categoriesBoard.add(createToggleButton(category),j,i);
            j++;
            if (j == 3) {
                j = 0;
                i++;
            }
        }

        evenlySpreadOut(categoriesBoard,i);
        categoriesBoard.setVgap(20);
        return categoriesBoard;
    }

    private void evenlySpreadOut(GridPane categoriesBoard,int rows) {
        // Format each rows to be center aligned and have identical height.
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setPercentHeight(100d / rows);
        for (int i = 0; i <= rows; i++) {
            categoriesBoard.getRowConstraints().add(rowConstraints);
        }

        // Format each columns to be center aligned and have identical width.
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setPercentWidth(100d / (3));
        for (int i = 0; i < 3; i++) {
            categoriesBoard.getColumnConstraints().add(columnConstraints);
        }
    }

    private ToggleButton createToggleButton(String category) {
        ToggleButton toggleButton = new ToggleButton(category);
        toggleButton.setStyle("-fx-font-size:18px");
        toggleButton.setPadding(new Insets(15,0,15,0));
        toggleButton.setPrefHeight(100);
        toggleButton.setPrefWidth(200);
        toggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean isSelected = newValue;
            if (isSelected) {
                if (selectedCategories.size() < 5) {
                    selectedCategories.add(category);
                }
                else {
                    toggleButton.setSelected(false);
                }
            }
            else {
                selectedCategories.remove(category);
            }
        });

        return toggleButton;
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

    public void handleReturnToGamesMenuButtonAction() {
        gamesMenuController.setMainStageToGamesMenuScene();
    }

    public void handleSelectButton() {
        gameManager.newGame(new ArrayList<>(selectedCategories));

        try {
            Parent selectQuestion = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectQuestion/SelectQuestion.fxml"));
            _mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
