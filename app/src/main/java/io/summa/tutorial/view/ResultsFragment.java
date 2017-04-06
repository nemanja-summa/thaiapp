package io.summa.tutorial.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.summa.tutorial.R;
import io.summa.tutorial.model.Question;

public class ResultsFragment extends Fragment implements View.OnClickListener {

    public static final String FRAGMENT_TAG = "ResultsFragment";

    public static final int PERCENTAGE_TO_PASS = 90;
    List<Question> mQuestions;
    int mAnswered, mCorrect, mTestType;

    ResultsListener mResultsListener;

    Button mBtnReview, mBtnTryAgain, mBtnMainMenu;
    TextView mTvTitle, mTvStatistic, mTvSubReview, mTvPercentageDone;

    public ResultsFragment() {
    }

    public static ResultsFragment newInstance(List<Question> questions, int answered, int correct, int testType, ResultsListener listener) {
        ResultsFragment fragment = new ResultsFragment();
        fragment.mQuestions = questions;
        fragment.mAnswered = answered;
        fragment.mCorrect = correct;
        fragment.mTestType = testType;
        fragment.mResultsListener = listener;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        mBtnMainMenu = (Button) view.findViewById(R.id.btnMainMenu);
        mBtnReview = (Button) view.findViewById(R.id.btnReviewMistakes);
        mBtnTryAgain = (Button) view.findViewById(R.id.btnTryAgain);
        mTvStatistic = (TextView) view.findViewById(R.id.tvAnsweredCorrect);
        mTvSubReview = (TextView) view.findViewById(R.id.tvReviewSubtitle);
        mTvTitle = (TextView) view.findViewById(R.id.tvResultsTitle);
        mTvPercentageDone = (TextView) view.findViewById(R.id.tvPercentageToPass);

        String answeredCorrectText = getResources().getString(R.string.answered_correct_unanswered, mAnswered, mCorrect, mQuestions.size() - mAnswered);
        mTvStatistic.setText(answeredCorrectText);

        if (mAnswered == mCorrect) {
            mBtnReview.setEnabled(false);
            mTvSubReview.setVisibility(View.VISIBLE);
        } else {
            mBtnReview.setEnabled(true);
            mTvSubReview.setVisibility(View.GONE);
        }

        if (mTestType == LicenceTestActivity.EXTRA_SIMULATION) {
            int percentageCorrect = calculatePercentageCorrect(mQuestions.size(), mCorrect);
            String percentageDone = getResources().getString(R.string.percentage_to_pass, PERCENTAGE_TO_PASS, percentageCorrect);

            mTvPercentageDone.setVisibility(View.VISIBLE);
            mTvPercentageDone.setText(percentageDone);

            if (percentageCorrect < PERCENTAGE_TO_PASS) {
                mTvTitle.setText(R.string.sorry_you_failed);
            } else {
                mTvTitle.setText(R.string.congrats_you_passed);
            }

        } else {
            mTvTitle.setText(R.string.congrats_you_are_done);
            mTvPercentageDone.setVisibility(View.GONE);

        }

        mBtnMainMenu.setOnClickListener(this);
        mBtnReview.setOnClickListener(this);
        mBtnTryAgain.setOnClickListener(this);


        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btnMainMenu) {
            getActivity().finish();
        } else if (id == R.id.btnTryAgain) {
            mResultsListener.onResultsRetry(mTestType);
        } else if (id == R.id.btnReviewMistakes) {

            List<Question> subQuestions = getQuestionsForReview();

            mResultsListener.onResultsReview(subQuestions);
        }
    }

    private List<Question> getQuestionsForReview() {
        List<Question> subQuestions = new ArrayList<>();
        for (Question question : mQuestions) {
            if (question.isMarked() && question.getCorrectIndex() != question.getAnsweredIndex())
                subQuestions.add(question);
        }
        return subQuestions;
    }

    private int calculatePercentageCorrect(int size, int correct) {
        return (int) (correct * 100f / size);
    }

    interface ResultsListener {
        void onResultsReview(List<Question> questions);

        void onResultsRetry(int type);

    }

}
