package quinzical.GamesModule.AskQuestion;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import quinzical.GamesModule.GameManager;
import quinzical.GamesModule.GameType;
import quinzical.GamesModule.GamesMenu.GamesMenuController;
import quinzical.GamesModule.SelectQuestion.SelectQuestionController;
import quinzical.MainMenu.MainMenu;
import quinzical.Questions.Question;
import quinzical.Utilities.AskQuestionUtilities;
import quinzical.Utilities.HelpUtilities;
import quinzical.Utilities.Notification;
import quinzical.Utilities.TTSUtilities;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * The controller for a view to ask the question to the player,
 * and tell the player about whether the player was correct or not.
 * <p></p>
 * It takes care of how events caused by button presses in the "AskQuestion" view are handled.
 *
 * @author Hyung Park
 */
public class AskQuestionController implements Initializable {

    @FXML
    private Button submitAnswerButton, playClueButton, dontKnowButton, helpButton, helpCloseButton,
            macronAButton, macronEButton, macronIButton, macronOButton, macronUButton, switchMacronCapsButton;
    @FXML
    private Label questionInfoLabel, timeLabel, helpLabel;
    @FXML
    private ComboBox<String> selectQuestionType;
    @FXML
    private TextField answerField;
    @FXML
    private Slider speedAdjustSlider;
    @FXML
    private HBox helpArea;

    // Frequently used instances of classes.
    private final GameManager gameManager = GameManager.getInstance();
    private final SelectQuestionController selectQuestionController = SelectQuestionController.getInstance();
    private final GamesMenuController gamesMenuController = GamesMenuController.getInstance();

    // Reference to the question currently being asked.
    private Question question;

    // Timer related.
    private final int questionTime = 60*1000;
    private long endTime = System.currentTimeMillis()+1000*5;
    private boolean done = false;

    // Macron functionality related.
    private boolean isMacronCaps = false;
    private Button[] macronButtons;

    /**
     * The initial method that fxml view calls from this controller as it loads.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Retrieve selected question.
        question = selectQuestionController.getSelectedQuestion();
        // Mark the question as answered.
        question.getParent().advanceLowestValuedQuestionIndex();

        // Allocating respective labels for the question.
        questionInfoLabel.setText(question.getParent() + ": $" + question.getValue());
        selectQuestionType.setItems(FXCollections.observableList(AskQuestionUtilities.getQuestionTypes()));

        // Initializing speed adjust slider (setting default view and event handler)
        speedAdjustSlider.setValue(TTSUtilities.getDefaultReadingSpeed());
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> TTSUtilities.setReadingSpeed(newSpeed.intValue()));

        // Only enable submit button when both question type has been selected and some answer has been entered.
        submitAnswerButton.disableProperty().bind(Bindings.or(
                answerField.textProperty().isEmpty(),
                selectQuestionType.valueProperty().isNull()
        ));

        macronButtons = new Button[]{macronAButton, macronEButton, macronIButton, macronOButton, macronUButton};
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
        TTSUtilities.speak(question.getClue());
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
            for (String correctAnswer : question.getAnswer()) {
                if (cleanedPlayerAnswer.equals(AskQuestionUtilities.answerCleanUp(correctAnswer))
                && selectQuestionType.getValue().toLowerCase().equals(question.getQuestionType())) {
                    isUserAnswerCorrect = true;
                    correctAnswerGiven();
                }
            }

            // If none of the correct answer matches the player's answer
            if (!isUserAnswerCorrect) {
                incorrectAnswerGiven();
            }

            TTSUtilities.endTTSSpeaking();
            if (!gameManager.isInternationalGameUnlocked() && isTwoCategoriesComplete()) {
                gameManager.unlockInternationalGame();
                notifyInternationalGameUnlock();

                // Because international section has now been enabled, go back to Games menu.
                gamesMenuController.setMainStageToGamesMenuScene();
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
        AskQuestionUtilities.answerUnknown(question.getAnswer()[0], question.getQuestionType());

        TTSUtilities.endTTSSpeaking();
        if (!gameManager.isInternationalGameUnlocked() && isTwoCategoriesComplete()) {
            gameManager.unlockInternationalGame();
            notifyInternationalGameUnlock();

            // Because international section has now been enabled, go back to Games menu.
            gamesMenuController.setMainStageToGamesMenuScene();
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
        gameManager.incrementCurrentScore(question.getValue());

        // Revert currently reading speed to default, then say "Correct".
        TTSUtilities.revertReadingSpeedToDefault();
        TTSUtilities.speak("Correct");

        String contentText = "Added $" + question.getValue() + " to the current score.\n\n"
                + "Your current score is now $" + gameManager.getCurrentScore();

        Notification.smallInformationPopup("Correct", "Correct!", contentText);
    }

    /**
     * Displays a pop up notifying the player that the player answer was incorrect,
     * then display and read out the correct answer, then decrement the current
     * score by the value of the question.
     */
    public void incorrectAnswerGiven() {
        // Decrement current score.
        gameManager.decrementCurrentScore(question.getValue());

        // Revert currently reading speed to default, then say "Correct".
        TTSUtilities.revertReadingSpeedToDefault();
        TTSUtilities.speak("The correct answer was: "
                + question.getQuestionType() + " " + question.getAnswer()[0]);

        String contentText = "The correct answer was: "
                + question.getQuestionType().substring(0,1).toUpperCase()
                + question.getQuestionType().substring(1) + " "
                + question.getAnswer()[0].replaceAll("`", "")
                + "\n$" + question.getValue() + " has been deducted from your current winning.\n\n"
                + "Your current winning is now $" + gameManager.getCurrentScore();

        Notification.largeInformationPopup("Incorrect", "Incorrect!", contentText);
    }

    public boolean isTwoCategoriesComplete() {
        int numCategoriesComplete = 0;
        for (int categoryIndex = 0; categoryIndex < 5; categoryIndex++) {
            if (gameManager.isCategoryComplete(categoryIndex)) {
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
        if (gameManager.isEveryQuestionAnswered()) {
            gameManager.setCurrentGameFinished();
            if (gameManager.isGameFinished(GameType.NZ) && gameManager.isGameFinished(GameType.INTERNATIONAL)) {
                setSceneToEndGameScene();
            } else {
                Notification.largeInformationPopup("Every Question Answered",
                        "Congratulations!",
                        "You have completed every question in this game mode.\n\n" +
                                "Please finish the other game mode to complete the game or " +
                                "reset game to play this game again.");
                gamesMenuController.setMainStageToGamesMenuScene();
            }
        }
        else {
            // End any currently-running speaking methods and return to the question board.
            TTSUtilities.endTTSSpeaking();
            selectQuestionController.setMainStageToSelectQuestionScene();
        }
    }

    private void notifyInternationalGameUnlock() {
        String contentText = "Congratulations on completing two categories!\n\n"
                + "You have unlocked international game module.\n"
                + "You can now switch between two modules by pressing a button on the "
                + "bottom right section of the games menu screen.\n\n"
                + "Let's now take a tour outside NZ...";

        Notification.largeInformationPopup("International Unlocked", "International Game Mode has been unlocked!", contentText);
    }

    private void setSceneToEndGameScene() {
        try {
            Parent scoreBoard = FXMLLoader.load(getClass().getResource("/quinzical/GamesModule/EndGame/GameEnd.fxml"));
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
