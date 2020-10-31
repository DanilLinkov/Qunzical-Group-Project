package quinzical.Questions;

/**
 * This class is used to store everything about a question
 * which is then stored in a category object
 *
 * Authors: Hyung Park, Danil Linkov
 */
public class Question {

    // Question properties
    private String clue;
    private String[] answer;
    private int lineNumber;
    private Category parent;
    private String questionType;
    private int value;

    // String[] pass into || _parent = parent; Category parent
    public Question(String clue, String[] answer, Category parent, int value) {
        this.clue = clue;
        this.answer = answer;
        this.parent = parent;
        this.value = value;
    }

    // Standard getters and setters

    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public String getClue() {
        return clue;
    }

    public void setClue(String clue) {
        this.clue = clue;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public String getQuestionType() {
        return questionType;
    }

    public String[] getAnswer() {
        return answer;
    }

    public Category getParent() {
        return parent;
    }

    public int getValue() {
        return value;
    }
}
