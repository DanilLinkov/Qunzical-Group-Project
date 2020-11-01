package quinzical.GamesModule.ScoreBoard;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.Utilities.HelpUtilities;

import java.net.URL;
import java.util.*;

/**
 * The controller for a view of the Score board scene which displays
 * all the recorded scores with the name of the player that achieved it
 * in a grid.
 * <p></p>
 * It takes core of how events caused by the ScoreBoard view are managed.
 *
 * @author Hyung Park, Danil Linkov
 */
public class ScoreBoardController implements Initializable {

    @FXML
    private VBox nameArea, scoreArea;
    @FXML
    private Label helpLabel;
    @FXML
    private HBox helpArea;

    private static ScoreBoardController instance;

    // Hashmap used to store the score of the player
    private Map<String,Integer> scoreBoardMap;

    /**
     * Initialize method called when the scene is created
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        // Loading the score.txt and converting it to a hashmap
        scoreBoardMap = ScoreBoardManager.getInstance().getScoreBoardMap();
        // Loading the score board
        loadScoreBoard();
    }

    /**
     * Returns an instance of the ScoreBoard controller
     * @return the currently used instance of ScoreBoardController
     */
    public static ScoreBoardController getInstance() {
        return instance;
    }

    /**
     * Handles the back to the games menu button
     */
    public void handleBackButton() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    /**
     * Goes over the hashmap for the scoreBoard and creates
     * a grid from it, one for the names and one for the scores.
     * These grids are then displayed in different areas of the view.
     */
    private void loadScoreBoard() {
        GridPane playerBoardGrid = new GridPane();
        playerBoardGrid.setGridLinesVisible(false);

        // Going over the names of the players and creating a label
        // and putting it into the grid
        int i = 0;
        for (String name : scoreBoardMap.keySet()) {
            Label playerName = createLabel(name);
            playerBoardGrid.add(playerName,0,i);
            i++;
        }

        GridPane scoreBoardGrid = new GridPane();
        scoreBoardGrid.setGridLinesVisible(false);

        // Going over the scores of the players and creating a label
        // and putting it into the grid
        int j = 0;
        for (Integer score : scoreBoardMap.values()) {
            Label playerScore = createLabel(Integer.toString(score));
            scoreBoardGrid.add(playerScore,0,j);
            j++;
        }

        // Setting alignments, padding and adding the grid to the views

        playerBoardGrid.setAlignment(Pos.CENTER_RIGHT);
        nameArea.getChildren().clear();
        nameArea.getChildren().add(playerBoardGrid);
        nameArea.setPadding(new Insets(5,60,0,0));

        scoreBoardGrid.setAlignment(Pos.CENTER_LEFT);
        scoreArea.getChildren().clear();
        scoreArea.getChildren().add(scoreBoardGrid);
        scoreArea.setPadding(new Insets(5,0,0,60));
    }

    /**
     * Creates a label with the ScoreBoard.css properties to put into the grid
     * @param labelName The name of the label to add.
     * @return A label created with the given name as a text.
     */
    private Label createLabel(String labelName) {
        Label label = new Label(labelName);

        label.getStyleClass().clear();
        label.getStyleClass().add("label");
        label.getStylesheets().add(getClass().getClassLoader().getResource("quinzical/GamesModule/ScoreBoard/ScoreBoard.css").toExternalForm());

        return label;
    }

    /**
     * Help button functionality which brings the help area to the front so the user can see it
     */
    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"This scoreboard displays all the scores of the previous players" +
                "\n\nTo get your name and score on here finish both the NZ and the International questions");
        HelpUtilities.bringToFront(helpArea);
    }

    /**
     * Help close functionality which brings teh help area to the back so the user can not see it
     */
    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

}
