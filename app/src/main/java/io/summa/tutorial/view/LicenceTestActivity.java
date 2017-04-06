package io.summa.tutorial.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.summa.tutorial.R;
import io.summa.tutorial.model.Question;
import io.summa.tutorial.util.Helper;

public class LicenceTestActivity extends AppCompatActivity implements LicenceTestFragment.OnFinishListener, ResultsFragment.ResultsListener {

    public static final String EXTRA_CONTEXT = "extra_context";
    public static final int EXTRA_SIMULATION = 1;
    public static final int EXTRA_PRACTICE = 2;
    public static final int EXTRA_REVIEW = 3;

    public static final int TEST_QUESTION_POOL = 50;


    int testType;
    LicenceTestFragment mLicenceTestFragment, mReviewLicenceTestFragment;
    ResultsFragment mResultsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licence_test);

        Bundle bundle = getIntent().getExtras();

        if (bundle == null) {
            finish();
            return;
        }

        testType = bundle.getInt(LicenceTestActivity.EXTRA_CONTEXT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Question> questions = populateData(testType);

        if (savedInstanceState == null) {
            mLicenceTestFragment = LicenceTestFragment.newInstance(questions, this, testType);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.rlRootLayout, mLicenceTestFragment, LicenceTestFragment.FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onFinish(List<Question> questions, int answered, int correct) {

        mResultsFragment = ResultsFragment.newInstance(questions, answered, correct, testType, this);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rlRootLayout, mResultsFragment, ResultsFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onResultsRetry(int type) {
        List<Question> questions = populateData(testType);
        testType = type;

        mLicenceTestFragment = LicenceTestFragment.newInstance(questions, this, testType);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rlRootLayout, mLicenceTestFragment, LicenceTestFragment.FRAGMENT_TAG)
                .commit();
    }

    @Override
    public void onResultsReview(List<Question> questions) {
        testType = EXTRA_REVIEW;

        mReviewLicenceTestFragment = LicenceTestFragment.newInstance(questions, this, testType);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rlRootLayout, mReviewLicenceTestFragment, LicenceTestFragment.FRAGMENT_TAG)
                .commit();
    }

    List<Question> populateData(int testType) {

        List<Question> questionList = new ArrayList<>();
        try {
            String jsonData = Helper.loadJSONFromRaw(this, R.raw.data);

            questionList = Helper.parseQuestionsFromJsonString(jsonData);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (testType == LicenceTestActivity.EXTRA_PRACTICE)
            return questionList;
        else {
            List<Question> testQuestionList = new ArrayList<>();
            for (int i = 0; i < TEST_QUESTION_POOL; i++) {
                int seed = new Random().nextInt(questionList.size());
                testQuestionList.add(questionList.get(seed));
                questionList.remove(seed);
            }
            return testQuestionList;
        }
    }
}
