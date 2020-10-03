package quinzical.GamesModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.Questions.Question;
import quinzical.Utilities.AskQuestionUtilities;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for a view to ask the question to the user,
 * and tell the user about whether the user was correct or not.
 * <p></p>
 * It takes care of how events caused by button presses in the "askQuestion" view are handled.
 *
 * @author Hyung Park
 */
public class AskQuestionController implements Initializable {

    public Label questionInfoLabel;
    public Label questionTypeLabel;
    public Button submitAnswerButton;
    public TextField answerField;
    public Slider speedAdjustSlider;
    public Button playClueButton;
    public Button dontKnowButton;

    private GameManager _gameManager = GameManager.getInstance();
    private SelectQuestionController _selectQuestionController = SelectQuestionController.getInstance();

    private Question _question;

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve selected question.
        _question = _selectQuestionController.getSelectedQuestion();
        // Mark the question as answered.
        _question.getParent().advanceLowestValuedQuestionIndex();

        // Allocating respective labels for the question.
        questionInfoLabel.setText(_question.getParent() + ": $" + _question.getValue());
        questionTypeLabel.setText(
                _question.get_whatIs().substring(0, 1).toUpperCase() + _question.get_whatIs().substring(1)
        );

        // Initializing speed adjust slider (setting default view and event handler)
        speedAdjustSlider.setValue(AskQuestionUtilities.getDefaultReadingSpeed());
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> {
            AskQuestionUtilities.setReadingSpeed(newSpeed.intValue());
        });

        // Read out the question.
        handlePlayClueButton();
    }

    /**
     * Handles the event of Play clue button (the large circular button in the middle) being pressed.
     * <p></p>
     * It speaks the clue of the selected question using speak method.
     */
    public void handlePlayClueButton() {
        AskQuestionUtilities.speak(_question.get_clue());
    }

    public void handleSubmitAnswerButtonAction() {
        String cleanedPlayerAnswer = AskQuestionUtilities.answerCleanUp(answerField.getText());

        boolean eventFinished = false;
        for (String correctAnswer : _question.get_answer()) {
            if (cleanedPlayerAnswer.equals(AskQuestionUtilities.answerCleanUp(correctAnswer))) {
                eventFinished = true;
                _gameManager.incrementCurrentScore(_question.getValue());
                correctAnswerGiven();
            }
        }

        if (!eventFinished) {
            _gameManager.decrementCurrentScore(_question.getValue());
            incorrectAnswerGiven();
        }

        checkEveryQuestionAnswered();

        // End any currently-running speaking methods and return to the question board.
        AskQuestionUtilities.endSpeakingProcess();
        _selectQuestionController.setMainStageToGamesMenuScene();
    }

    public void handleDontKnowButtonAction() {
        AskQuestionUtilities.answerUnknown(_question.get_answer()[0]);

        checkEveryQuestionAnswered();

        // End any currently-running speaking methods and return to the question board.
        AskQuestionUtilities.endSpeakingProcess();
        _selectQuestionController.setMainStageToGamesMenuScene();
    }

    public void correctAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Correct");
        alert.setHeaderText("Correct!");
        String contentText = "Added $" + _question.getValue() + " to the current score.\n\n"
                + "Your current score is now $" + _gameManager.getCurrentScore();
        _gameManager.updateBestScore();
        AskQuestionUtilities.revertReadingSpeedToDefault();
        AskQuestionUtilities.speak("Correct");

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void incorrectAnswerGiven() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        String contentText = "The correct answer was: " + _question.get_answer()[0] + "\n"
                + "$" + _question.getValue() + " has been deducted from your current winning.\n\n"
                + "Your current winning is now $" + _gameManager.getCurrentScore();

        AskQuestionUtilities.revertReadingSpeedToDefault();
        AskQuestionUtilities.speak("The correct answer was " + _question.get_answer()[0]);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    public void checkEveryQuestionAnswered() {
        if (_gameManager.isEveryQuestionAnswered()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            alert.setTitle("Every Question Answered");
            alert.setHeaderText("Every Question Answered");
            String contentText = "Congratulations!" + "\n\n"
                    + "You have completed every question in the question board," + "\n"
                    + "and your total score was $" + _gameManager.getCurrentScore() + ".\n\n"
                    + "The game will now reset and a new set of question board will be ready.";

            // Resetting the game.
            _gameManager.newGame();

            // Formats the pop-up.
            alert.getDialogPane().setContent(new Label(contentText));
            alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }
}
