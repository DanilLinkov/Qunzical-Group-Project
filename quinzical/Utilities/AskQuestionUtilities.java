package quinzical.Utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

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
    private static MediaPlayer _ttsAudioPlayer;
    private static String[] macronsLowerCase = {"ā", "ē", "ī", "ō", "ū"};
    private static String[] macronsUpperCase = {"Ā", "Ē", "Ī", "Ō", "Ū"};

    /**
     * Configures the event action of an array of macron buttons and returns a boolean value on
     * whether a upper case macrons has been added. (simply, a boolean value of isMacronCaps)
     * Such output was decided to simplify any further implementation changes.
     * @param macronButtons an array of buttons with respective macrons to add to the answer field.
     * @param answerField an answer field for macrons to be added when the buttons are pressed.
     * @return a boolean value on whether the macrons set for buttons are in upper case; capitalized.
     */
    public static void configureMacronButtons(Button[] macronButtons, TextField answerField, boolean isMacronCaps) {
        for (int i = 0; i < macronButtons.length; i++) {
            int finalI = i;
            macronButtons[i].setOnAction(e -> answerField.appendText(isMacronCaps ? macronsUpperCase[finalI] : macronsLowerCase[finalI]));
        }
    }

    public static void macronSwitchCaps(Button[] macronButtons, boolean isMacronCaps, TextField answerField) {
        for (int i = 0; i < macronButtons.length; i++) {
            macronButtons[i].setText(isMacronCaps ? macronsLowerCase[i] : macronsUpperCase[i]);
        }
        configureMacronButtons(macronButtons, answerField, !isMacronCaps);
    }

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
                "The correct answer was: " + questionAnswer.replaceAll("`", "");

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
                .replace("`","")
                .replace("a ","")
                .replace("the ","")
                .replace("an ","")
                .replace("`", "")
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
//        // End any previously running speak processes.
//        endTTSSpeaking();
//
//        // Add "\" in front of quotation marks to make bash read this as normal character.
//        text = text.replaceAll("\"", "\\\\\"");
//
//        LinkedList<String> texts = new LinkedList<>(Arrays.asList(text.split("`")));
//        StringBuilder command = new StringBuilder("mkdir tts; cd tts");
//        boolean isSubStringMaori = false;
//        if (text.charAt(0) == '`') {
//            texts.pop();
//            isSubStringMaori = true;
//        }
//
//        int subStringIndex = 0;
//        for (String textToSpeak : texts) {
//            command.append("; ");
//
//            textToSpeak = textToSpeak.trim();
//
//            if (isSubStringMaori) {
//                command.append("espeak -vde \"" + textToSpeak + "\"");
//                command.append(" -s " + _readingSpeed);
//                command.append(" -w " + subStringIndex + ".wav");
//            } else {
//                command.append("espeak \"" + textToSpeak + "\"");
//                command.append(" -s " + _readingSpeed);
//                command.append(" -w " + subStringIndex + ".wav");
//            }
//
//            subStringIndex++;
//            isSubStringMaori = !isSubStringMaori;
//        }
//
//        try {
//            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command.toString());
//            _espeakProcess = pb.start();
//            _espeakProcess.waitFor();
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        ArrayList<Media> medias = new ArrayList<>();
//        for (int i = 0; i < texts.size(); i++) {
//            String fileDirectory = "file:" + System.getProperty("user.dir").replaceAll(" ", "%20") + "/tts/" + i + ".wav";
//            medias.add(new Media(fileDirectory));
//        }
//
//        ObservableList<Media> mediaList = FXCollections.observableArrayList();
//        for (Media media : medias) {
//            mediaList.add(media);
//        }
//
//        playMediaTracks(mediaList);
    }

    /**
     * Plays medias inside a provided list of media by recursively playing the very first
     * media in the list.
     * When the list has a size of 0, it stops.
     * @param mediaList List of media to play.
     */
    private static void playMediaTracks(ObservableList<Media> mediaList) {
        if (mediaList.size() == 0) {
            return;
        }

        _ttsAudioPlayer = new MediaPlayer(mediaList.remove(0));
        _ttsAudioPlayer.play();

        _ttsAudioPlayer.setOnEndOfMedia(() -> playMediaTracks(mediaList));
    }

    /**
     * Ends any previously running speak processes.
     */
    public static void endTTSSpeaking() {
        if (_ttsAudioPlayer != null && (_ttsAudioPlayer.getStatus() == MediaPlayer.Status.PLAYING)) {
            _ttsAudioPlayer.stop();
        }
    }

    /**
     * Cleans up tts audio player and related temporary files.
     */
    public static void ttsCleanUp() {
        endTTSSpeaking();

        if (_ttsAudioPlayer != null) {
            _ttsAudioPlayer.dispose();
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "rm -r tts/");
            pb.start();
        } catch (IOException e) {
            e.printStackTrace();
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
