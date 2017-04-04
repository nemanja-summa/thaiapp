package io.summa.tutorial.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import io.summa.tutorial.model.Answer;
import io.summa.tutorial.model.Question;

/**
 * Created by nemanja on 4/4/17.
 */

public class Helper {

    public static String loadJSONFromRaw(Context context, int resource_id) throws IOException {
        InputStream is = context.getResources().openRawResource(resource_id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }


    public static List<Question> parseQuestionsFromJsonString(String jsonString) throws JSONException {
        List<Question> questionsList = new ArrayList<>();

        JSONArray jsonWrapper = new JSONArray(jsonString);
        for (int i = 0; i < jsonWrapper.length(); i++) {
            Question question = new Question();

            JSONObject qaBlock = jsonWrapper.getJSONObject(i);

            JSONObject questionBlock = qaBlock.getJSONObject("question");
            JSONArray answersBlock = qaBlock.getJSONArray("answers");

            question.setCorrectIndex(qaBlock.getInt("correct"));
            question.setMarked(false); //always init as false

            if (questionBlock.has("text"))
                question.setQuestionTitle(questionBlock.getString("text"));
            if (questionBlock.has("url"))
                question.setQuestionImage(questionBlock.getString("url"));

            for (int j = 0; j < answersBlock.length(); j++) {
                JSONObject answerBlock = answersBlock.getJSONObject(j);
                Answer answer = new Answer();
                answer.setAnswer(answerBlock.getString("answer"));
                answer.setType(answerBlock.getInt("type"));

                question.getAnswerList().add(answer);

            }

            questionsList.add(question);
        }

        return questionsList;
    }


}
