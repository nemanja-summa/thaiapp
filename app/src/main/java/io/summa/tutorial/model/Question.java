package io.summa.tutorial.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nemanja on 4/1/17.
 */

public class Question {
    private String mQuestionTitle;
    private String mQuestionImage;
    private List<Answer> mAnswerList = new ArrayList<>();

    private int mCorrectIndex;
    private int mAnsweredIndex;
    private boolean mMarked;


    public String getQuestionTitle() {
        return mQuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        mQuestionTitle = questionTitle;
    }

    public String getQuestionImage() {
        return mQuestionImage;
    }

    public void setQuestionImage(String questionImage) {
        mQuestionImage = questionImage;
    }

    public List<Answer> getAnswerList() {
        return mAnswerList;
    }

    public void setAnswerList(List<Answer> answerList) {
        mAnswerList = answerList;
    }

    public int getCorrectIndex() {
        return mCorrectIndex;
    }

    public void setCorrectIndex(int correctIndex) {
        mCorrectIndex = correctIndex;
    }

    public boolean isMarked() {
        return mMarked;
    }

    public void setMarked(boolean marked) {
        this.mMarked = marked;
    }

    public int getAnsweredIndex() {
        return mAnsweredIndex;
    }

    public void setAnsweredIndex(int answeredIndex) {
        mAnsweredIndex = answeredIndex;
    }
}
