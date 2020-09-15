package quinzical.Questions;

public class Question {

    private String _clue;
    private String _answer;
    private int _lineNumber;

    public Question(String clue, String answer) {
        _clue = clue;
        _answer = answer;
    }

    public int getLineNumber() {
        return _lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this._lineNumber = lineNumber;
    }

    public String get_clue() {
        return _clue;
    }

    public void set_clue(String _clue) {
        this._clue = _clue;
    }

    public String get_answer() {
        return _answer;
    }

    public void set_answer(String _answer) {
        this._answer = _answer;
    }

}
