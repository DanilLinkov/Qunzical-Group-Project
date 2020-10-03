package quinzical.Utilities;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;

import java.io.IOException;

public class AskQuestionUtilities {

    private static final int _defaultReadingSpeed = 160;
    private static int _readingSpeed = _defaultReadingSpeed;
    private static Process _espeakProcess;

    public static void answerUnknown(String questionAnswer) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

//        alert.setTitle("Answer");
        alert.setHeaderText("Don't know the question?");
        String contentText = "That's alright, we all learn new things everyday.\n\n" +
                "The correct answer was: " + questionAnswer;

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
     * the user's answer and the actual answer are comparable
     * even if the user used a different way of expressing the answer
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

        if (_espeakProcess != null && _espeakProcess.isAlive()) {
            _espeakProcess.destroy();
        }

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
     *
     */
    public static void revertReadingSpeedToDefault() {
        _readingSpeed = _defaultReadingSpeed;
    }

    public static int getDefaultReadingSpeed() {
        return _defaultReadingSpeed;
    }

    public static void setReadingSpeed(int newSpeed) {
        _readingSpeed = newSpeed;
    }

}
