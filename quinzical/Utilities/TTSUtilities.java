package quinzical.Utilities;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * A utility class that provides TTS related functionalities.
 *
 * @author Hyung Park
 */
public class TTSUtilities {

    private static final int _defaultReadingSpeed = 160;
    private static int _readingSpeed = _defaultReadingSpeed;
    private static MediaPlayer _ttsAudioPlayer;

    /**
     * Processes given English text so that certain words are pronounced
     * more correctly with espeak, our current TTS engine.
     * <p></p>
     * Word of notice: It makes the entire string be in lower case to simplify
     * the process.
     * @param englishTextToProcess English word to process.
     * @return String after processing.
     */
    private static String processEnglishStringForEspeak(String englishTextToProcess) {
        return englishTextToProcess.toLowerCase()
                .replaceAll("christchurch", "christ church");
    }

    /**
     * Processes given Māori text so that certain words are pronounced
     * more correctly with espeak, our current TTS engine.
     * <p></p>
     * Word of notice: It makes the entire string be in lower case to simplify
     * the process.
     * @param maoriTextToProcess Māori word to process.
     * @return String after processing.
     */
    private static String processMaoriStringForEspeak(String maoriTextToProcess) {
        // Make it all lowercase for easy detection.
        maoriTextToProcess = maoriTextToProcess.toLowerCase();

        // Check whether any word starts with "ng", and if it does, change it to "n".
        if (maoriTextToProcess.startsWith("ng") || maoriTextToProcess.contains(" ng")) {
            maoriTextToProcess = maoriTextToProcess.replaceFirst("ng", "n")
                    .replaceFirst(" ng", " n");
        }

        // Change any vowels that are not pronounced in Māori way.
        maoriTextToProcess = maoriTextToProcess.replaceAll("ae", "a-e")
                .replaceAll("ei", "e-i")
                .replaceAll("eu", "e-u")
                .replaceAll("ie", "i-e")
                .replaceAll("oe", "o-e")
                .replaceAll("ou", "o-u");

        return maoriTextToProcess;
    }

    /**
     * Using a bash function "espeak", a string inside the argument is read by "espeak".
     * @param text A string for espeak to read.
     */
    public static void speak(String text) {
        // End any previously running speak processes.
        endTTSSpeaking();

        // Add "\" in front of quotation marks to make bash read this as normal character.
        text = text.replaceAll("\"", "\\\\\"");

        LinkedList<String> texts = new LinkedList<>(Arrays.asList(text.split("`")));
        StringBuilder command = new StringBuilder("mkdir tts; cd tts");
        boolean isSubStringMaori = false;
        if (text.charAt(0) == '`') {
            texts.pop();
            isSubStringMaori = true;
        }

        int subStringIndex = 0;
        for (String textToSpeak : texts) {
            command.append("; ");

            textToSpeak = textToSpeak.trim();
            if (isSubStringMaori) {
                textToSpeak = processMaoriStringForEspeak(textToSpeak);
            } else {
                textToSpeak = processEnglishStringForEspeak(textToSpeak);
            }

            command.append("espeak ").append(isSubStringMaori ? "-vde " : "");
            command.append("\"").append(textToSpeak).append("\"");
            command.append(" -s ").append(_readingSpeed);
            command.append(" -w ").append(subStringIndex).append(".wav");

            subStringIndex++;
            isSubStringMaori = !isSubStringMaori;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", command.toString());
            Process espeakProcess = pb.start();
            espeakProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        ArrayList<Media> medias = new ArrayList<>();
        for (int i = 0; i < texts.size(); i++) {
            String fileDirectory = "file:" + System.getProperty("user.dir").replaceAll(" ", "%20") + "/tts/" + i + ".wav";
            medias.add(new Media(fileDirectory));
        }

        ObservableList<Media> mediaList = FXCollections.observableArrayList();
        mediaList.addAll(medias);

        playMediaTracks(mediaList);
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
