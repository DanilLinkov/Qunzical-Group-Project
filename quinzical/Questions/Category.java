package quinzical.Questions;

import java.util.ArrayList;

/**
 * This class is used to represent everything about a category
 * which is stored in a question board.
 *
 * Authors: Hyung Park, Danil Linkov
 */
public class Category {

    // Array list used to store the questions belonging to this category that have
    // been randomly selected
    private ArrayList<Question> questions = new ArrayList<>();
    // Shows the current index of the first un answered question
    private int lowestValuedQuestionIndex = 0;

    // Category name
    private String categoryName;

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public String toString() {
        return categoryName;
    }

    // Standard getters, setters and Array list operations

    public void advanceLowestValuedQuestionIndex() {
        lowestValuedQuestionIndex++;
    }

    public int getLowestValuedQuestionIndex() {
        return lowestValuedQuestionIndex;
    }

    public void addQuestion(Question question){
        questions.add(question);
    }

    public Question getQuestion(int index){
        return questions.get(index);
    }

    public void setLowestValuedQuestionIndex(int lowestValuedQuestionIndex) {
        this.lowestValuedQuestionIndex = lowestValuedQuestionIndex;
    }

}
