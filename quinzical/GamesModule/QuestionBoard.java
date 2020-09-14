package quinzical.GamesModule;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import javafx.scene.layout.RowConstraints;
import quinzical.Questions.Category;
import quinzical.Questions.Question;

public class QuestionBoard {

    private final int _numQuestions = 5;
    private final int _numCategories = 5;
    private Category[] _categoriesList = new Category[_numCategories];

    public QuestionBoard() {
        // Randomly select categories from possible set of categories.

    }

    public GridPane initializeBoard() {
        GridPane questionBoardComponent = new GridPane();
        questionBoardComponent.setGridLinesVisible(true);
        // Possibly add this if any more padding is needed.
//        questionBoard.setPadding(new Insets(5, 5, 5, 5));

        // Pre-initializes variables to be frequently re-defined.
        Label categoryLabel;
        int categoryIndex = 0;
        // Add categories to the top bar of question board
        for (Category category : _categoriesList) {
            // Extract category name, make it into upper case, and store it in the list of labels of categories.
            categoryLabel = new Label(category.toString().toUpperCase());
            // Format each cell of category
//            categoryLabel.setFont(categoryTextFont);
            categoryLabel.setPadding(new Insets(10, 5, 10, 5));
            // Allocate a category to the corresponding cell.
            GridPane.setConstraints(categoryLabel, categoryIndex++, 0);
            // Adds the category label in the question board.
            questionBoardComponent.getChildren().add(categoryLabel);
        }

        return questionBoardComponent;
    }

    public GridPane getQuestionBoard() {
        // generate Pressable Question board based on which questions need to be emptied out or pressable or disabled.

        GridPane questionBoardComponent = initializeBoard();

        Button pointButton;
        for (int categoryIndex = 0; categoryIndex < _numCategories; categoryIndex++) {
            int lowestValuedQuestionIndex = _categoriesList[categoryIndex].getLowestValuedQuestionIndex();
            for (int questionIndex = 0; questionIndex < _numQuestions; questionIndex++) {
                if (questionIndex < lowestValuedQuestionIndex) {
                    // Advance to the next question and make the cell empty.
                    continue;
                } else {
                    pointButton = createPointButton(categoryIndex, questionIndex);
                    // Make the button fill in each cell.
                    pointButton.setMaxWidth(Integer.MAX_VALUE);
                    pointButton.setMaxHeight(Integer.MAX_VALUE);
                    // Allocate a button to the corresponding cell.
                    GridPane.setConstraints(pointButton, categoryIndex, questionIndex+1);

                    if (questionIndex > lowestValuedQuestionIndex) {  // Create unclickable buttons
                        pointButton.setDisable(true);
                    }

                    // Add the button in the question board.
                    questionBoardComponent.getChildren().add(pointButton);
                }
            }
        }

        evenlySpreadOut(questionBoardComponent);
        return questionBoardComponent;
    }

    private Button createPointButton(int categoryIndex, int questionIndex) {
        Button button = new Button(Integer.toString((questionIndex+1)*100));
        button.setOnAction(e ->
                // Link whatever action this button should do.
                        System.out.println("YAY BUTTON PRESSED!");
//                SelectQuestionController.getInstance().handlePointButtonAction(categoryIndex, questionIndex));
        return button;
    }

    private void evenlySpreadOut(GridPane questionBoard) {
        // Format each rows to be center aligned and have identical height.
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setValignment(VPos.CENTER);
        rowConstraints.setPercentHeight(100d / _numQuestions);
        for (int i = 0; i <= _numQuestions; i++) {
            questionBoard.getRowConstraints().add(rowConstraints);
        }

        // Format each columns to be center aligned and have identical width.
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        columnConstraints.setPercentWidth(100d / (_numCategories));
        for (int i = 0; i < _numCategories; i++) {
            questionBoard.getColumnConstraints().add(columnConstraints);
        }
    }

}
