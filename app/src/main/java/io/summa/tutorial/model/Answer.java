package io.summa.tutorial.model;

/**
 * Created by nemanja on 4/1/17.
 */

public class Answer {
    public static final int TEXT = 0;
    public static final int IMAGE = 1;

    private String mAnswer;
    private int type;

    public String getAnswer() {
        return mAnswer;
    }

    public void setAnswer(String answer) {
        mAnswer = answer;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
