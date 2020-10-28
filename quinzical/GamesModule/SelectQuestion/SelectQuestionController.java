package quinzical.GamesModule.SelectQuestion;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Question;
import quinzical.Utilities.HelpUtilities;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for a view to select question.
 * <p></p>
 * It takes care of how events caused by button presses in the "select question" view are handled.
 *
 * @author Hyung Park
 */
public class SelectQuestionController implements Initializable {

    // Components in the view.
    public Button backToGameMenuButton;
    public VBox questionBoardArea;
    public Label userScoreLabel;

    public Button helpCloseButton;
    public Button helpButton;
    public Label helpLabel;
    public HBox helpArea;

    // Frequently used instances of classes, including current class.
    private final MainMenu mainMenuModel = MainMenu.getInstance();
    private final GameManager gameManager = GameManager.getInstance();
    private static SelectQuestionController instance;

    // The selected question of the player.
    private Question selectedQuestion;

    /**
     * Returns the instance of this class.
     * @return The instance of this class.
     */
    public static SelectQuestionController getInstance() {
        return instance;
    }

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        initialization();
    }

    /**
     * Initializes the view by filling in the current score, the best score, and the question board.
     */
    private void initialization() {
        userScoreLabel.setText("Current Score: $" + gameManager.getCurrentScore());

        // Clear any child nodes in the VBox used to store question board, then add the question board.
        questionBoardArea.getChildren().clear();
        questionBoardArea.getChildren().add(gameManager.getQuestionBoard());
    }

    /**
     * Handles the event of points button in the question board being pressed.
     * <p></p>
     * It retrieves the Question object with the given question information, then stores
     * the reference of that object
     * in this class for {@link quinzical.GamesModule.AskQuestion.AskQuestionController}
     * to access it to ask question.
     * @param categoryIndex The index of the category of the question in the list of categories.
     * @param questionIndex The index of the question in its category.
     */
    public void handlePointButtonAction(int categoryIndex, int questionIndex) {
        selectedQuestion = gameManager.getQuestionInCategory(categoryIndex, questionIndex);

        try {
            Parent askQuestionWindow = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/AskQuestion/AskQuestion.fxml"));
            MainMenu.getInstance().setMainStageScene(new Scene(askQuestionWindow, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event of "Return to Game Menu" button being pressed.
     * <p></p>
     * It changes the scene of the main stage to the game menu scene.
     */
    public void handleReturnToGameMenuButtonAction() {
        GamesMenuController.getInstance().setMainStageToGamesMenuScene();
    }

    /**
     * Returns the question the player has selected.
     * @return The question the player has selected.
     */
    public Question getSelectedQuestion() {
        return selectedQuestion;
    }

    /**
     * Changes the scene of the main stage to the main menu.
     */
    public void setMainStageToSelectQuestionScene() {
        initialization();
        mainMenuModel.setMainStageScene(backToGameMenuButton.getScene());
    }

    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"text");
        HelpUtilities.bringToFront(helpArea);
    }

    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

}
