package quinzical.Questions;

public class Question {

    private String _clue;
    private String[] _answer;
    private int _lineNumber;
    private Category _parent;
    private String _whatIs;
    private int _value;

    // String[] pass into || _parent = parent; Category parent
    public Question(String clue, String[] answer, Category parent, int value) {
        _clue = clue;
        _answer = answer;
        _parent = parent;
        _value = value;
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

    public String[] get_answer() {
        return _answer;
    }

    public Category getParent() {
        return _parent;
    }

    public void set_whatIs(String _whatIs) {
        this._whatIs = _whatIs;
    }

    public String get_whatIs() {
        return _whatIs;
    }

    public int getValue() {
        return _value;
    }
}
