package io.summa.tutorial.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import io.summa.tutorial.R;
import io.summa.tutorial.adapter.AnswersRecyclerView;
import io.summa.tutorial.model.Question;
import io.summa.tutorial.util.Helper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionTitle, mCounter;
    private ImageView mQuestionImage;
    private Button mPrevious, mNext;
    private AnswersRecyclerView mAdapter;

    private List<Question> mQuestions;
    int pageIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQuestions = populateData();

        mQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        mCounter = (TextView) findViewById(R.id.tvCounter);
        mQuestionImage = (ImageView) findViewById(R.id.ivTitleImage);
        RecyclerView answerRecyclerView = (RecyclerView) findViewById(R.id.rvAnswers);
        mPrevious = (Button) findViewById(R.id.btnPrevious);
        mNext = (Button) findViewById(R.id.btnNext);


        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);

        answerRecyclerView.setLayoutManager(new LinearLayoutManager(answerRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new AnswersRecyclerView();

        answerRecyclerView.setAdapter(mAdapter);

        loadQuestion();

    }

    List<Question> populateData() {

        List<Question> questionList = null;
        try {
            String jsonData = Helper.loadJSONFromRaw(this, R.raw.data);

            questionList = Helper.parseQuestionsFromJsonString(jsonData);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return questionList;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNext) {
            nextQuestion();
            loadQuestion();
        } else if (view.getId() == R.id.btnPrevious) {
            previousQuestion();
            loadQuestion();
        }
    }

    private void loadQuestion() {
        String counterText = getResources().getString(R.string.counter_text, pageIndex + 1, mQuestions.size());
        mCounter.setText(counterText);


        mQuestionTitle.setText(mQuestions.get(pageIndex).getQuestionTitle());
        mAdapter.setNewAnswersAndRefresh(mQuestions.get(pageIndex).getAnswerList());


    }

    private void previousQuestion() {
        mNext.setVisibility(View.VISIBLE);
        if (--pageIndex <= 0)
            mPrevious.setVisibility(View.GONE);
        else
            mPrevious.setVisibility(View.VISIBLE);


    }

    private void nextQuestion() {
        mPrevious.setVisibility(View.VISIBLE);
        if (++pageIndex >= mQuestions.size() - 1)
            mNext.setVisibility(View.GONE);
        else
            mNext.setVisibility(View.VISIBLE);

    }
}
