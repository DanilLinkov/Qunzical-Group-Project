package quinzical.GamesModule;

import javafx.scene.layout.GridPane;

import quinzical.Questions.Category;

public class QuestionBoard {

    private Category[] _categoriesList = new Category[5];
    private GridPane _questionBoardComponent;

    public QuestionBoard() {
        // Randomly select categories from possible set of categories.
        _questionBoardComponent = new GridPane();
        // Add categories to the top bar of question board
    }

    public GridPane getQuestionBoard() {
        // generate Pressable Question board based on which questions need to be emptied out or pressable or disabled.

        return _questionBoardComponent;
    }

}
