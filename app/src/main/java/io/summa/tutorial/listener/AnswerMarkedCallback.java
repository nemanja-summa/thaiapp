package io.summa.tutorial.listener;

import io.summa.tutorial.model.Answer;

/**
 * Created by nemanja on 4/5/17.
 */

public interface AnswerMarkedCallback {
    void onMarked(Answer answer);
}
