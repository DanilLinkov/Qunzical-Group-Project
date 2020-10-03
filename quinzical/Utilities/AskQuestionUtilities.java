package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.io.IOException;

/**
 * A utility class that contains methods that are used in AskQuestion views across different modules.
 * <p></p>
 * It provides three major functionalities; unknownAnswer pop up, cleaning up answers,
 * and speak methods.
 *
 * @author Danil Linkov, Hyung Park
 */
public class AskQuestionUtilities {

    private static final int _defaultReadingSpeed = 160;
    private static int _readingSpeed = _defaultReadingSpeed;
    private static Process _espeakProcess;

    /**
     * Displays a pop up notifying the player what the answer to the unknown question was.
     * @param questionAnswer The answer to the question.
     */
    public static void answerUnknown(String questionAnswer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        // Formats texts inside the pop up.
        alert.setTitle("Don't Know");
        alert.setHeaderText("Don't know the question?");
        String contentText = "That's alright, we all learn new things everyday.\n\n" +
                "The correct answer was: " + questionAnswer;

        // Revert currently reading speed to default, then say "Correct".
        revertReadingSpeedToDefault();
        speak("The correct answer was " + questionAnswer);

        // Formats the pop-up.
        alert.getDialogPane().setContent(new Label(contentText));
        alert.getDialogPane().setMinWidth(alert.getDialogPane().getWidth());
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.showAndWait();
    }

    /**
     * User or actual answer input clean up method so that
     * the player's answer and the actual answer are comparable
     * even if the player used a different way of expressing the answer
     * @param answer
     * @return
     */
    public static String answerCleanUp(String answer) {
        // Removing a, the, an and changing mt to mount, nz to new zealand
        // Also trimming and lower casing the answer
        String cleanAnswer = answer.toLowerCase()
                .replace("a ","")
                .replace("the ","")
                .replace("an ","")
                .trim()
                .replace("mt","mount")
                .replace("nz","new zealand");

        return cleanAnswer;
    }

    /**
     * Using a bash function "espeak", a string inside the argument is read by "espeak".
     * @param text A string for espeak to read.
     */
    public static void speak(String text) {

        // End any previously running speak processes.
        endSpeakingProcess();

        // Add "\" in front of quotation marks to make bash read this as normal character.
        text = text.replaceAll("\"", "\\\\\"");

        String command = "espeak \"" + text + "\"" + " -s " + _readingSpeed;
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command);
            _espeakProcess = pb.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ends any previously running speak processes.
     */
    public static void endSpeakingProcess() {
        if (_espeakProcess != null && _espeakProcess.isAlive()) {
            _espeakProcess.destroy();
        }
    }

    /**
     * Returns the default reading speed; 160 WPM (words per minute)
     * @return The default reading speed; 160 WPM.
     */
    public static int getDefaultReadingSpeed() {
        return _defaultReadingSpeed;
    }

    /**
     * Reverts the reading speed of the reading process to default; 160 WPM (words per minute)
     */
    public static void revertReadingSpeedToDefault() {
        _readingSpeed = _defaultReadingSpeed;
    }

    /**
     * Sets the reading speed of the reading process to the given speed.
     * @param newSpeed A new reading speed to set, in WPM (words per minute)
     */
    public static void setReadingSpeed(int newSpeed) {
        _readingSpeed = newSpeed;
    }
}
