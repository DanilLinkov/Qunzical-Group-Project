package quinzical.Utilities;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class HelpUtilities {

    public static void bringToBack(HBox helpArea) {
        helpArea.toBack();
    }

    public static void bringToFront(HBox helpArea) {
        helpArea.toFront();
    }

    public static void setHelpText(Label helpAreaLabel, String helpAreaText) {
        helpAreaLabel.setText(helpAreaText);
    }

}
