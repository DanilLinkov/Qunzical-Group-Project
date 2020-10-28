package quinzical.GamesModule.AskQuestion;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Question;
import quinzical.Utilities.AskQuestionUtilities;
import quinzical.Utilities.HelpUtilities;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

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
    public Label timeLabel;

    private boolean isMacronCaps = false;
    public Button macronAButton;
    public Button macronEButton;
    public Button macronIButton;
    public Button macronOButton;
    public Button macronUButton;
    public Button switchMacronCapsButton;
    private final Button[] macronButtons = new Button[5];

    public Button helpCloseButton;
    public Button helpButton;
    public Label helpLabel;
    public HBox helpArea;

    // Frequently used instances of classes.
    private final GameManager _gameManager = GameManager.getInstance();
    private final SelectQuestionController _selectQuestionController = SelectQuestionController.getInstance();
    final int questionTime = 60*1000*1;

    // Reference to the question currently being asked.
    private Question _question;
    long endTime = System.currentTimeMillis()+1000*5;
    boolean done = false;

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
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> AskQuestionUtilities.setReadingSpeed(newSpeed.intValue()));

        BooleanBinding isTextFieldEmpty = Bindings.isEmpty(answerField.textProperty());
        submitAnswerButton.disableProperty().bind(isTextFieldEmpty);

        macronButtons[0] = macronAButton;
        macronButtons[1] = macronEButton;
        macronButtons[2] = macronIButton;
        macronButtons[3] = macronOButton;
        macronButtons[4] = macronUButton;
        AskQuestionUtilities.configureMacronButtons(macronButtons, answerField, isMacronCaps);

        setTimer();
        showTimer();

        // Read out the question.
        handlePlayClueButton();
    }

    public void macronSwitchCaps() {
        AskQuestionUtilities.macronSwitchCaps(macronButtons, isMacronCaps, answerField);
        isMacronCaps = !isMacronCaps;
    }

    private void setTimer() {
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                    if (!done) {
                        Platform.runLater(() -> handleSubmitAnswerButtonAction());
                    }
            }
        }, questionTime);
        endTime = System.currentTimeMillis() + questionTime;
    }

    private void showTimer() {
        DateFormat timeFormat = new SimpleDateFormat("ss");
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(200),
                        event -> {
                            final long diff = endTime - System.currentTimeMillis();
                            if ( diff < 0 ) {
                                timeLabel.setText(timeFormat.format(0) + " secs left");
                            } else {
                                timeLabel.setText(timeFormat.format(diff) + " secs left");
                            }
                        }
                )
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
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
        done = true;
        // Retrieve and clean up player's answer
        String cleanedPlayerAnswer = AskQuestionUtilities.answerCleanUp(answerField.getText());
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

            AskQuestionUtilities.endTTSSpeaking();
            if (!_gameManager.isInternationalGameUnlocked() && isTwoCategoriesComplete()) {
                _gameManager.unlockInternationalGame();
                notifyInternationalGameUnlock();

                // Because international section has now been enabled, go back to Games menu.
                GamesMenuController.getInstance().setMainStageToGamesMenuScene();
            } else {
                checkIsEveryQuestionAnswered();
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
        done = true;
        AskQuestionUtilities.answerUnknown(_question.getAnswer()[0]);

        AskQuestionUtilities.endTTSSpeaking();
        if (!_gameManager.isInternationalGameUnlocked() && isTwoCategoriesComplete()) {
            _gameManager.unlockInternationalGame();
            notifyInternationalGameUnlock();

            // Because international section has now been enabled, go back to Games menu.
            GamesMenuController.getInstance().setMainStageToGamesMenuScene();
        } else {
            checkIsEveryQuestionAnswered();
        }
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
        String contentText = "The correct answer was: " + _question.getAnswer()[0].replaceAll("`", "")
                + "\n$" + _question.getValue() + " has been deducted from your current winning.\n\n"
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

    public boolean isTwoCategoriesComplete() {
        int numCategoriesComplete = 0;
        for (int categoryIndex = 0; categoryIndex < 5; categoryIndex++) {
            if (_gameManager.isCategoryComplete(categoryIndex)) {
                numCategoriesComplete++;
            }
        }
        return numCategoriesComplete == 2;
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
            _gameManager.setCurrentGameFinished();
            setSceneToEndGameScene();
            if (_gameManager.isGameFinished(GameType.NZ) && _gameManager.isGameFinished(GameType.INTERNATIONAL)) {
                _gameManager.resetGame();
            }
        }
        else {
            // End any currently-running speaking methods and return to the question board.
            AskQuestionUtilities.endTTSSpeaking();
            SelectQuestionController.getInstance().setMainStageToSelectQuestionScene();
        }
    }

    private void notifyInternationalGameUnlock() {
        // Display popup saying international section is complete here.
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle("International Unlocked");
        alert.setHeaderText("International Game Mode has been unlocked!");
        String contentText = "Congratulations on completing two categories!" + "\n\n"
                + "You have unlocked international game module." + "\n"
                + "You can now switch between two modules by pressing a button on the "
                + "bottom right section of the games menu screen." + "\n\n"
                + "Let's now take a tour outside NZ...";

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    private void setSceneToEndGameScene() {
        try {
            Parent scoreBoard = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/EndGame/EndGameScene.fxml"));
            MainMenu.getInstance().setMainStageScene(new Scene(scoreBoard, MainMenu.getAppWidth(), MainMenu.getAppHeight()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleHelpButton() {
        HelpUtilities.setHelpText(helpLabel,"text");
        HelpUtilities.bringToFront(helpArea);
    }

    public void handleHelpCloseButton() {
        HelpUtilities.bringToBack(helpArea);
    }

}
