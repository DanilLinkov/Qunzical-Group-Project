package quinzical.Utilities;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * This is a helper class which makes sure that the future implementation would only have to be
 * changed in one spot
 */
public class HelpUtilities {

    /**
     * Brings a HBox area to the back of the view
     * @param helpArea
     */
    public static void bringToBack(HBox helpArea) {
        helpArea.toBack();
    }

    /**
     * Brings a HBox area to the front of the view
     * @param helpArea
     */
    public static void bringToFront(HBox helpArea) {
        helpArea.toFront();
    }

    /**
     * Set helper text of the label
     * @param helpAreaLabel
     * @param helpAreaText
     */
    public static void setHelpText(Label helpAreaLabel, String helpAreaText) {
        helpAreaLabel.setText(helpAreaText);
    }

}
