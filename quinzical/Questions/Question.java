package quinzical.Questions;

/**
 * This class is used to store everything about a question
 * which is then stored in a category object
 *
 * Authors: Hyung Park, Danil Linkov
 */
public class Question {

    // Question properties
    private String _clue;
    private String[] _answer;
    private int _lineNumber;
    private Category _parent;
    private String _questionType;
    private int _value;

    // String[] pass into || _parent = parent; Category parent
    public Question(String clue, String[] answer, Category parent, int value) {
        _clue = clue;
        _answer = answer;
        _parent = parent;
        _value = value;
    }

    // Standard getters and setters

    public int getLineNumber() {
        return _lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        _lineNumber = lineNumber;
    }

    public String getClue() {
        return _clue;
    }

    public void setClue(String clue) {
        _clue = clue;
    }

    public void setQuestionType(String questionType) {
        _questionType = questionType;
    }

    public String getQuestionType() {
        return _questionType;
    }

    public String[] getAnswer() {
        return _answer;
    }

    public Category getParent() {
        return _parent;
    }

    public int getValue() {
        return _value;
    }
}
