package quinzical.Questions;

public class Category {

    private Question[] _questions = new Question[5];
    private int lowestValuedQuestionIndex = 0;

    private String _categoryName;

    public Category(String categoryName) {
        _categoryName = categoryName;
    }

    public Question[] getQuestions() {
        return _questions;
    }

    public String toString() {
        return _categoryName;
    }

    public void advanceLowestValuedQuestionIndex() {
        lowestValuedQuestionIndex++;
    }

    public int getLowestValuedQuestionIndex() {
        return lowestValuedQuestionIndex;
    }

}
