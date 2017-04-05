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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.summa.tutorial.R;
import io.summa.tutorial.adapter.AnswersRecyclerView;
import io.summa.tutorial.listener.AnswerMarkedCallback;
import io.summa.tutorial.model.Answer;
import io.summa.tutorial.model.Question;
import io.summa.tutorial.util.Helper;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener, AnswerMarkedCallback {

    public static final String EXTRA_CONTEXT = "extra_context";
    public static final int EXTRA_SIMULATION = 1;
    public static final int EXTRA_PRACTICE = 2;

    private TextView mQuestionTitle, mCounter;
    private ImageView mQuestionImage;
    private Button mPrevious, mNext;
    private AnswersRecyclerView mAdapter;

    private List<Question> mQuestions;
    int pageIndex = 0;
    int testType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            finish();
            return;
        }

        testType = bundle.getInt(QuestionsActivity.EXTRA_CONTEXT);


        mQuestions = populateData(testType);

        mQuestionTitle = (TextView) findViewById(R.id.tvTitle);
        mCounter = (TextView) findViewById(R.id.tvCounter);
        mQuestionImage = (ImageView) findViewById(R.id.ivTitleImage);
        mPrevious = (Button) findViewById(R.id.btnPrevious);
        mNext = (Button) findViewById(R.id.btnNext);
        RecyclerView answerRecyclerView = (RecyclerView) findViewById(R.id.rvAnswers);

        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);

        answerRecyclerView.setLayoutManager(new LinearLayoutManager(answerRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new AnswersRecyclerView(this);

        answerRecyclerView.setAdapter(mAdapter);

        loadQuestion();

    }

    @Override
    public void onMarked(Answer answer) {
        Question question = mQuestions.get(pageIndex);

        int answerIndex = question.getAnswerList().indexOf(answer);

        question.setAnsweredIndex(answerIndex);
        question.setMarked(true);

        mAdapter.notifyDataSetChanged();
    }

    List<Question> populateData(int testType) {

        List<Question> questionList = new ArrayList<>();
        try {
            String jsonData = Helper.loadJSONFromRaw(this, R.raw.data);

            questionList = Helper.parseQuestionsFromJsonString(jsonData);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (testType == QuestionsActivity.EXTRA_PRACTICE)
            return questionList;
        else {
            List<Question> testQuestionList = new ArrayList<>();
            for (int i = 0; i < 60; i++) {
                int seed = new Random().nextInt(questionList.size());
                testQuestionList.add(questionList.get(seed));
                questionList.remove(seed);
            }
            return testQuestionList;
        }
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
        mAdapter.setNewAnswersAndRefresh(mQuestions.get(pageIndex));

        if (mQuestions.get(pageIndex).getQuestionImage() != null) {
            mQuestionImage.setImageResource(Helper.getDrawableIdentifier(this, mQuestions.get(pageIndex).getQuestionImage()));
            mQuestionImage.setVisibility(View.VISIBLE);
        } else
            mQuestionImage.setVisibility(View.GONE);


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
