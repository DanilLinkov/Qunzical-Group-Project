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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestionBoard {

    private final int _numQuestions = 5;
    private final int _numCategories = 5;
    private ArrayList<Category> _categoriesList = new ArrayList<>();

    public QuestionBoard() {
        // Randomly select categories from possible set of categories.
        //createBoard();
    }

    public GridPane initializeBoard() {
        GridPane questionBoardComponent = new GridPane();
        questionBoardComponent.setGridLinesVisible(true);
        // Possibly add this if any more padding is needed.
        questionBoardComponent.setPadding(new Insets(5, 5, 5, 5));

        // Pre-initializes variables to be frequently re-defined.
        Label categoryLabel;
        int categoryIndex = 0;
        // Add categories to the top bar of question board
        System.out.println(_categoriesList);
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

    public void createBoard() {
        ArrayList<String> filePaths;

        String categoriesPath = new File("").getAbsolutePath();
        categoriesPath+="/categories";
        File categoriesFolder = new File(categoriesPath);

        filePaths = new ArrayList(Arrays.asList(categoriesFolder.list()));

        for (int i = 0; i < 5; i++) {
            int randomCategoryIndex = (int)(Math.random() * (filePaths.size()-1));
            Category newCategory = new Category(filePaths.get(randomCategoryIndex).replace(".txt","")); // parent
            filePaths.remove(randomCategoryIndex);

            try {
                List<String> allLines = Files.readAllLines(Paths.get(categoriesPath+"/"+filePaths.get(randomCategoryIndex)));

                ArrayList<Integer> savedLines = new ArrayList<Integer>();
                for (int j = 0; j < 5; j++) {
                    int randomLineIndex = (int)(Math.random() * (allLines.size()-1));
                    String line = allLines.get(randomLineIndex);

                    String question;
                    String answer;

                    line.replaceAll("\\s+","");

                    List<String> questionSplit = Arrays.asList(line.split("\\s*,\\s*"));

                    question = questionSplit.get(0);
                    answer = questionSplit.get(1);

                    allLines.remove(randomLineIndex);
                    Question newQuestion = new Question(question,answer);
                    newQuestion.setLineNumber(randomLineIndex);
                    // set parent or constructor for category

                    newCategory.addQuestion(newQuestion);
                    savedLines.add(randomLineIndex);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            _categoriesList.add(newCategory);
        }
    }

    public GridPane getQuestionBoard() {
        // generate Pressable Question board based on which questions need to be emptied out or pressable or disabled.

        GridPane questionBoardComponent = initializeBoard();

        Button pointButton;
        for (int categoryIndex = 0; categoryIndex < _numCategories; categoryIndex++) {
            int lowestValuedQuestionIndex = _categoriesList.get(categoryIndex).getLowestValuedQuestionIndex();
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
                        System.out.println("YAY BUTTON PRESSED!"));
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

    public int getSize()
    {
        return _categoriesList.size();
    }

    public void addCategory(Category category){
        _categoriesList.add(category);
    }

    public Category getCategory(int index){
        return _categoriesList.get(index);
    }

    public Category getCategory(String name){

        for (Category c : _categoriesList) {
            if (c.toString().equals(name)) {
                return c;
            }
        }

        return null;
    }

}
