package quinzical.GamesModule.ScoreBoard;

import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GamesMenuController;
import quinzical.MainMenu.MainMenu;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class ScoreBoardController implements Initializable {

    public Button backButton;
    public VBox nameArea;
    public VBox scoreArea;

    private static ScoreBoardController _instance;
    private MainMenu _mainMenuModel = MainMenu.getInstance();
    private GamesMenuController gamesMenuController = GamesMenuController.getInstance();

    private Map<String,Integer> scoreBoardMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;
        scoreBoardMap = ScoreBoardManager.getInstance().getScoreBoardMap();
        loadScoreBoard();
    }

    public static ScoreBoardController getInstance() {
        return _instance;
    }

    public void handleBackButton() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    public void loadScoreBoard() {
        GridPane playerBoardGrid = new GridPane();
        playerBoardGrid.setGridLinesVisible(false);

        int i = 0;
        for (String name : scoreBoardMap.keySet()) {
            Label playerName = new Label(name);
            playerBoardGrid.add(playerName,0,i);
            i++;
        }

        GridPane scoreBoardGrid = new GridPane();
        scoreBoardGrid.setGridLinesVisible(false);

        int j = 0;
        for (Integer score : scoreBoardMap.values()) {
            Label playerScore = new Label(Integer.toString(score));
            scoreBoardGrid.add(playerScore,0,j);
            j++;
        }

        playerBoardGrid.setAlignment(Pos.CENTER);
        nameArea.getChildren().clear();
        nameArea.getChildren().add(playerBoardGrid);

        scoreBoardGrid.setAlignment(Pos.CENTER);
        scoreArea.getChildren().clear();
        scoreArea.getChildren().add(scoreBoardGrid);
    }

}
