package quinzical.GamesModule.SelectCategories;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.MainMenu.MainMenu;
import quinzical.Utilities.HelpUtilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * The controller for a view of the SelectCategoriesScene which allows the player
 * to select 5 buttons at a time, allows for a random button which selects it for them,
 * submit button which takes those categories and loads them into the SelectQuestionController
 * and a back button to the games menu.
 * <p></p>
 * It takes core of how events caused by the SelectCategories are managed.
 *
 * @author Hyung Park, Danil Linkov
 */
public class SelectCategoriesController implements Initializable {

    @FXML
    private Label userScoreLabel, helpLabel;
    @FXML
    private Button selectButton;
    @FXML
    private VBox gridArea;
    @FXML
    private HBox helpArea;

    // Instances of commonly used classes
    private final GameManager gameManager = GameManager.getInstance();
    private final GamesMenuController gamesMenuController = GamesMenuController.getInstance();
    private final MainMenu mainMenuModel = MainMenu.getInstance();
    private static SelectCategoriesController instance;

    // This list is used to store the strings of all the categories in the categories folder
    private ArrayList<String> categories;
    // All the selected buttons
    private ObservableList<String> selectedCategories;
    // All the created buttons
    private ArrayList<ToggleButton> toggleButtons;

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        userScoreLabel.setText("Current Score: $" + gameManager.getCurrentScore());

        // Loads the categories from the folder depending on whether they are in NZ or international mode
        loadAllCategories();
        selectedCategories = FXCollections.observableArrayList();
        toggleButtons = new ArrayList<>();

        // Adding the buttons grid to the view
        gridArea.getChildren().clear();
        gridArea.getChildren().add(getQuestionBoard());

        // The player can only submit if they select 5 buttons
        IntegerBinding lengthSize = Bindings.size(selectedCategories);
        BooleanBinding listPopulated = lengthSize.greaterThan(4);
        selectButton.disableProperty().bind(listPopulated.not());
    }

    /**
     * Returns an instance of this controller
     * @return the currently used instance of SelectCategoriesController
     */
    public static SelectCategoriesController getInstance() {
        return instance;
    }

    /**
     * Creates the grid pane of toggle buttons which is added to the view
     * @return A GridPane element containing buttons of categories.
     */
    public GridPane getQuestionBoard() {
        GridPane categoriesBoard = new GridPane();
        categoriesBoard.setGridLinesVisible(false);

        int i = 0;
        int j = 0;

        // Going over every category and creating a toggle button of it
        // and adding it to the grid
        for (String category : categories) {
            categoriesBoard.add(createToggleButton(category),j,i);
            j++;
            if (j == 3) {
                j = 0;
                i++;
            }
        }

        // Spreading out the buttons
        evenlySpreadOut(categoriesBoard, i);
        categoriesBoard.setVgap(20);
        categoriesBoard.setStyle("-fx-background-color:#072365");

        return categoriesBoard;
    }

    /**
     * Spreads out the grid evenly given the number of rows
     * @param categoriesBoard A gridpane element to evenly spread out rows and columns
     * @param rows the number of rows in the grid.
     */
    private void evenlySpreadOut(GridPane categoriesBoard, int rows) {
        // Format each rows to be center aligned and have identical height.
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setPercentHeight(100d / rows);
        for (int i = 0; i < rows; i++) {
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

    /**
     * Creates a toggle button with the SelectCategories.css style and
     * the category name.
     * @param category The category which the button represents.
     * @return A toggle button which represents the given category.
     */
    private ToggleButton createToggleButton(String category) {
        ToggleButton toggleButton = new ToggleButton(category);
        toggleButton.setStyle("-fx-font-size:18px");
        toggleButton.setPadding(new Insets(15,0,15,0));
        toggleButton.setPrefHeight(100);
        toggleButton.setPrefWidth(200);

        // set shadow for button
        DropShadow shadow = new DropShadow();
        shadow.setBlurType(BlurType.THREE_PASS_BOX);
        shadow.setWidth(18.0);
        shadow.setHeight(18.0);
        shadow.setRadius(8.5);
        shadow.setOffsetX(3.0);
        shadow.setOffsetY(3.0);
        shadow.setSpread(0.03);
        shadow.setColor(new Color(0,0,0,0.25));
        toggleButton.setEffect(shadow);

        // Loading the css style
        toggleButton.getStyleClass().clear();
        toggleButton.getStyleClass().add("toggleButton");
        toggleButton.getStylesheets().add(getClass().getClassLoader().getResource(
                "quinzical/GamesModule/SelectCategories/SelectCategories.css").toExternalForm()
        );

        // Adding a listener to the toggle button
        toggleButton.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            boolean isSelected = newValue;

            // If the button was just selected then add it to the list if there are less than
            // 5 buttons already selected
            if (isSelected) {
                if (selectedCategories.size() < 5) {
                    selectedCategories.add(category);
                }
                else {
                    toggleButton.setSelected(false);
                }
            }
            // Else remove it from the list
            else {
                selectedCategories.remove(category);
            }
        });

        // Add it to the toggle buttons list
        toggleButtons.add(toggleButton);

        return toggleButton;
    }

    /**
     * Load all the categories from the categories folder depending on which game mode the player is on
     */
    private void loadAllCategories() {
        // Used to store all the file paths in the folder
        ArrayList<String> filePaths;

        // Getting the categories folder path
        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath += gameManager.getCurrentGameType() == GameType.NZ ? "/categories/NZ" : "/categories/international";

        // Creating a file object based on the path
        File categoriesFolder = new File(categoriesPath);

        // Getting all the file paths in that folder
        filePaths = new ArrayList<>(Arrays.asList(categoriesFolder.list()));
        categories = new ArrayList<>();

        // Adding them to the list
        for (String filePath : filePaths) {
            categories.add(filePath.replace(".txt", ""));
        }
    }

    /**
     * Handles the return to game menu button
     */
    public void handleReturnToGamesMenuButtonAction() {
        gamesMenuController.setMainStageToGamesMenuScene();
    }

    /**
     * Handles the select button and sends them to the select question scene
     */
    public void handleSelectButton() {
        gameManager.newGame(new ArrayList<>(selectedCategories));

        try {
            Parent selectQuestion = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/SelectQuestion/SelectQuestion.fxml"));
            mainMenuModel.setMainStageScene(new Scene(selectQuestion, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Randomly selects 5 categories for the player by choosing 5 random indices
     */
    public void handleRandomSelect() {
        clearSelectedCategories();
        ArrayList<Integer> randomIndexArray = randomIndexArray(categories.size());

        for (int i = 0; i < 5; i++) {
            toggleButtons.get(randomIndexArray.get(i)).setSelected(true);
        }
    }

    /**
     * Deselect all the toggle buttons
     */
    private void clearSelectedCategories() {
        for (ToggleButton tg : toggleButtons) {
            tg.setSelected(false);
        }
    }

    /**
     * This is a private utility method for this class which returns an array of size 5
     * containing random index values from 0 to the length-1 specific
     * @param length of the range of the index values
     * @return An arraylist with random index values.
     */
    private ArrayList<Integer> randomIndexArray (int length) {
        ArrayList<Integer> shuffledArray = new ArrayList<>();

        for(int i = 0; i< length;i++) {
            shuffledArray.add(i);
        }

        // Shuffling the array list and returning it
        Collections.shuffle(shuffledArray);

        return shuffledArray;
    }

    /**
     * Handles the scoreboard button click and take them to the score board scene
     */
    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"Select 5 categories from the list and click select" +
                "\n\nClicking random will randomly pick the 5 categories" +
                "\n\nClicking back will take you back to the games menu");
        HelpUtilities.bringToFront(helpArea);
    }

    /**
     * Help button functionality which brings the help area to the front so the user can see it
     */
    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

}
