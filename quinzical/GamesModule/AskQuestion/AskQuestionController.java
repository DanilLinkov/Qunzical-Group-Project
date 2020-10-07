package quinzical.GamesModule.AskQuestion;

import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.Questions.Question;
import quinzical.Utilities.AskQuestionUtilities;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The controller for a view to ask the question to the player,
 * and tell the player about whether the player was correct or not.
 * <p></p>
 * It takes care of how events caused by button presses in the "AskQuestion" view are handled.
 *
 * @author Hyung Park
 */
public class AskQuestionController implements Initializable {

    // Components in the view
    public Label questionInfoLabel;
    public Label questionTypeLabel;
    public Button submitAnswerButton;
    public TextField answerField;
    public Slider speedAdjustSlider;
    public Button playClueButton;
    public Button dontKnowButton;

    // Frequently used instances of classes.
    private GameManager _gameManager = GameManager.getInstance();
    private SelectQuestionController _selectQuestionController = SelectQuestionController.getInstance();

    // Reference to the question currently being asked.
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
                _question.getQuestionType().substring(0, 1).toUpperCase() + _question.getQuestionType().substring(1)
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
     * It speaks the clue of the selected question.
     */
    public void handlePlayClueButton() {
        AskQuestionUtilities.speak(_question.getClue());
    }

    /**
     * Handles the event of "Submit Answer" button being pressed.
     * <p></p>
     * It retrieves the player's answer from the answer field, cleans it up,
     * then checks whether the player has got the correct answer or not. After that,
     * it returns the main stage to the SelectQuestion view.
     * <p></p>
     * If the player has entered the correct answer, it runs the procedure for correct
     * answer given which is described in the following method.
     * {@link quinzical.GamesModule.AskQuestion.AskQuestionController#correctAnswerGiven()}
     * <p></p>
     * If the player has entered the incorrect answer, it runs the procedure for
     * incorrect answer given which is described in the following method.
     * {@link quinzical.GamesModule.AskQuestion.AskQuestionController#incorrectAnswerGiven()}
     */
    public void handleSubmitAnswerButtonAction() {
        // Retrieve and clean up player's answer
        String cleanedPlayerAnswer = AskQuestionUtilities.answerCleanUp(answerField.getText());

        if (!cleanedPlayerAnswer.isEmpty()) {
            // Checking whether any of the correct answer matches the player's answer
            boolean isUserAnswerCorrect = false;
            for (String correctAnswer : _question.getAnswer()) {
                if (cleanedPlayerAnswer.equals(AskQuestionUtilities.answerCleanUp(correctAnswer))) {
                    isUserAnswerCorrect = true;
                    correctAnswerGiven();
                }
            }

            // If none of the correct answer matches the player's answer
            if (!isUserAnswerCorrect) {
                incorrectAnswerGiven();
            }

            // Check whether every question in the question board has been answered.
            checkIsEveryQuestionAnswered();

            // End any currently-running speaking methods and return to the question board.
            AskQuestionUtilities.endSpeakingProcess();
            _selectQuestionController.setMainStageToSelectQuestionScene();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            // Formats texts inside the pop up.
            alert.setTitle("Invalid Input");
            alert.setHeaderText("Invalid Input!");
            String contentText = "Please enter a non-empty answer.";

            // Formats the pop-up.
            alert.getDialogPane().setContent(new Label(contentText));
            alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    /**
     * Handles the event of "Don't Know" button being pressed.
     * <p></p>
     * Displays a pop up showing the player the answer to the clue,
     * then checks whether every question has been answered,
     * then returns the main stage to SelectQuestion view.
     */
    public void handleDontKnowButtonAction() {
        AskQuestionUtilities.answerUnknown(_question.getAnswer()[0]);

        checkIsEveryQuestionAnswered();

        // End any currently-running speaking methods and return to the question board.
        AskQuestionUtilities.endSpeakingProcess();
        _selectQuestionController.setMainStageToSelectQuestionScene();
    }

    /**
     * Displays a pop up notifying the player that the player answer was correct,
     * and that the current score has incremented by the value of the question.
     * <p></p>
     * After incrementing the current score by the value of this question, compare
     * and update the best score for possible occasions of current score being
     * higher than the best score.
     */
    public void correctAnswerGiven() {
        // Increment current score and check-and-update best score.
        _gameManager.incrementCurrentScore(_question.getValue());
        _gameManager.updateBestScore();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle("Correct");
        alert.setHeaderText("Correct!");
        String contentText = "Added $" + _question.getValue() + " to the current score.\n\n"
                + "Your current score is now $" + _gameManager.getCurrentScore();

        // Revert currently reading speed to default, then say "Correct".
        AskQuestionUtilities.revertReadingSpeedToDefault();
        AskQuestionUtilities.speak("Correct");

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Displays a pop up notifying the player that the player answer was incorrect,
     * then display and read out the correct answer, then decrement the current
     * score by the value of the question.
     */
    public void incorrectAnswerGiven() {
        // Decrement current score.
        _gameManager.decrementCurrentScore(_question.getValue());

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        String contentText = "The correct answer was: " + _question.getAnswer()[0] + "\n"
                + "$" + _question.getValue() + " has been deducted from your current winning.\n\n"
                + "Your current winning is now $" + _gameManager.getCurrentScore();

        // Revert currently reading speed to default, then say "Correct".
        AskQuestionUtilities.revertReadingSpeedToDefault();
        AskQuestionUtilities.speak("The correct answer was " + _question.getAnswer()[0]);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * Checks whether every question in the question has been answered by
     * checking whether the lowest valued question index in every category
     * is 5.
     * <p></p>
     * If every question has been answered, display a pop up notifying the
     * player that the player has completed every question in the question
     * board, the player's total score, and that the game will now reset.
     */
    public void checkIsEveryQuestionAnswered() {
        if (_gameManager.isEveryQuestionAnswered()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);

            // Formats texts inside the pop up.
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
