package quinzical.PracticeModule.AskQuestion;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import quinzical.PracticeModule.PracticeMenuController;
import quinzical.Utilities.AskQuestionUtilities;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * The controller for a view to ask the question to the player in
 * practice module after they have selected a category, and then to
 * tell them whether they are right or not
 * <p></p>
 * It takes care of how events caused by button presses in the "AskPracticeQuestion" view are handled.
 *
 * @author Danil Linkov
 */
public class AskPracticeQuestionController implements Initializable {

    // Fxml objects used in the scene
    public Label categoryLabel;
    public Label questionLabel;
    public Label hintLabel;
    public Label questionTypeLabel;
    public Slider speedAdjustSlider;
    public TextField answerInput;
    public Button submitButton;
    public Button dontKnowButton;
    public Button playClueButton;
    public Label timeLabel;

    // Storing the question, answers and question type
    private String question;
    private String[] answer;
    private String qType;

    // Number of attempts they have left
    private int attempts;
    final int questionTime = 60*1000*1;

    // Used for scene transitioning
    private static AskPracticeQuestionController _instance;
    long endTime = System.currentTimeMillis()+1000*5;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        _instance = this;
        // Setting the number of attempts to 3
        attempts = 3;

        // Adding an event handler to the speed slider to save the speed to the question reading speed variable
        speedAdjustSlider.setValue(AskQuestionUtilities.getDefaultReadingSpeed());
        speedAdjustSlider.valueProperty().addListener((e, oldSpeed, newSpeed) -> {
            AskQuestionUtilities.setReadingSpeed(newSpeed.intValue());
        });
        setTimer();
        showTimer();

        BooleanBinding isTextFieldEmpty = Bindings.isEmpty(answerInput.textProperty());
        submitButton.disableProperty().bind(isTextFieldEmpty);
    }

    private void setTimer() {
        int oldAttempts = attempts;
        Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask(){
            @Override
            public void run() {
                if (oldAttempts == attempts) {
                    Platform.runLater(() -> onAnswerSubmit());
                }
            }
        }, questionTime);
        endTime = System.currentTimeMillis()+questionTime;
    }

    private void showTimer() {
        DateFormat timeFormat = new SimpleDateFormat( "mm:ss" );
        final Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis( 200 ),
                        event -> {
                            final long diff = endTime - System.currentTimeMillis();
                            if ( diff < 0 ) {
                                timeLabel.setText( timeFormat.format( 0 ) );
                            } else {
                                timeLabel.setText( timeFormat.format( diff ) );
                            }
                        }
                )
        );
        timeline.setCycleCount( Animation.INDEFINITE );
        timeline.play();
    }

    /**
     * This method is used to return this instance for scene transitioning
     * @return
     */
    public static AskPracticeQuestionController getInstance() {
        return _instance;
    }

    /**
     * This method is called in the practice menu to pass down the category name which the player
     * selected and then to load the questions in that category and select a random question
     * @param name
     */
    public void setCategoryName(String name) {
        // Setting the label to be category + its name
        categoryLabel.setText("Category: " + name);

        // Getting the specific categories file path
        String categoriesPath = new File("").getAbsolutePath() + "/categories/" + name + ".txt";

        try {
            // Getting all the lines in that category
            List<String> allLines = Files.readAllLines(Paths.get(categoriesPath));
            // Selecting a random line from that category for the question
            int randomLineIndex = (int)(Math.random() * (allLines.size()-1));

            String randomQuestion = allLines.get(randomLineIndex);

            // Splitting it to get the information about it
            List<String> randomQuestionSplit = Arrays.asList(randomQuestion.split("\\s*\\|\\s*"));

            question = randomQuestionSplit.get(0);
            qType = randomQuestionSplit.get(1);
            answer = randomQuestionSplit.get(2).trim().split("/");

            // Setting the question label and the question type label
            questionLabel.setText("Question: " + question);
            questionTypeLabel.setText(qType.substring(0, 1).toUpperCase() + qType.substring(1));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to speak the question when the play button is pressed
     */
    public void handlePlayClueButton() {
        AskQuestionUtilities.speak(question);
    }

    /**
     * This method handles the event when the player submits their answer and checks whether its correct
     * or not and acts accordingly
     */
    public void onAnswerSubmit() {
        // Format the players input
        String clearPlayerAnswer = AskQuestionUtilities.answerCleanUp(answerInput.getText());
            // Boolean variable which is set to true if the answer is correct
            boolean eventFinished = false;
            // Going over every potential answer for that question
            for (String correctAnswer : answer) {
                // Formatting both the player and the correct answer
                String clearCorrectAnswer = AskQuestionUtilities.answerCleanUp(correctAnswer);

                // Checking if its equal
                if (clearPlayerAnswer.equals(clearCorrectAnswer)) {
                    // Setting the eventFinished to true as the question has been answered correctly
                    eventFinished = true;
                    // Calling the correct answer given method
                    correctAnswerGiven();
                    // Transitioning the scene to the practice menu scene
                    PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();;
                }
            }

            // If the answer was not answered correctly
            if (!eventFinished) {
                // Call the incorrect answer given method
                incorrectAnswerGiven();
                // If all the attempts have been used up then transition the player to the practice menu scene
                if (attempts==0) {
                    // End any currently speaking process before transition.
                    AskQuestionUtilities.endSpeakingProcess();
                    PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();
                }
                else {
                    setTimer();
                }
            }
    }

    /**
     * This method handles the dont know button event and gives the used the answer and sends them
     * to the practice main menu scene
     */
    public void handleDontKnowButtonAction() {
        attempts = -1;
        AskQuestionUtilities.answerUnknown(answer[0]);
        AskQuestionUtilities.endSpeakingProcess();
        PracticeMenuController.getInstance().setMainStageToPracticeMenuScene();
    }

    /**
     * This method is used when the player enters the correct answer and gives
     * them an alert box saying they are right
     */
    private void correctAnswerGiven() {
        attempts = -1;
        // Creating a new alert box object
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Setting the title, header and context
        alert.setTitle("Correct");
        alert.setHeaderText("Correct!");
        String contentText = "You are correct!";

        // Resetting the espeak speed
        AskQuestionUtilities.revertReadingSpeedToDefault();
        // Speaking Correct
        AskQuestionUtilities.speak("Correct!");

        // Setting the alert box, showing it and waiting for the player to click ok
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * This method is used for when the player provides the incorrect answer and gives
     * them an alert box depending on the number of attempts they have left
     */
    private void incorrectAnswerGiven() {
        // Creating a new alert box object
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Decrementing the number of attempts
        attempts--;

        // Setting the title and the header of the alert box
        alert.setTitle("Incorrect");
        alert.setHeaderText("Incorrect!");
        // Resetting the reading speed of espeak
        AskQuestionUtilities.revertReadingSpeedToDefault();
        // Context string which is set depending on the number of attempts
        String contentText = "";

        // If the player has more than 1 attempts left
        if (attempts>1) {
            // Set the context to this and speak it
            contentText = "You have " + attempts + " attempts left!";
            AskQuestionUtilities.speak(contentText);
        }

        // If the player is on their last attempt
        if (attempts==1) {
            // Set the context to this and speak it
            contentText = "You have " + attempts + " attempt left!";
            AskQuestionUtilities.speak(contentText);

            // Get all the answers for this question and show their first letter as a hint
            String answers = "";
            // If there are multiple answers
            if (answer.length>1) {
                for(int i=0;i<answer.length;i++) {
                    // If its not a number then add it
                    if(!isNumeric(answer[i])){
                        answers+=answer[i].charAt(0);

                        // If its not the last answer then add or to it
                        if(i!=answer.length-1){
                            answers+=" or ";
                        }
                    }
                }
            }
            else {
                // Otherwise if there is only one answer then just add the single letter
                answers += answer[0].charAt(0);
            }

            // Display it
            hintLabel.setText("Hint: the first letter of the answer is " + answers);
        }

        // If the player has used up all their attempts
        if (attempts==0) {
            // Get all the answers
            String answers = "";
            if (answer.length>1) {
                for(int i=0;i<answer.length;i++) {
                    answers+=answer[i];
                    if(i!=answer.length-1){
                        answers+=" or ";
                    }
                }
            }
            else {
                answers+=answer[0];
            }

            // Display the correct answers and say they have run out of attempts and speak it
            contentText = "You have run out of attempts!" + "\n\nThe answer to the question \n\n" + question + "\n\nWas " + answers.replaceAll("`", "");
            AskQuestionUtilities.speak(contentText);
        }

        // Show the alert box with these properties
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        // Wait for the player to close it to continue with the main application
        alert.showAndWait();
    }

    /**
     * Method used to check whether a string is numeric or not
     * @param strNum
     * @return
     */
    private boolean isNumeric(String strNum) {
        // If the string is null then return false
        if (strNum == null) {
            return false;
        }
        // If the string is parsable into an integer then return true
        try {
            int d = Integer.parseInt(strNum);
        } catch (NumberFormatException nfe) {
            // Otherwise return false
            return false;
        }
        return true;
    }

}
