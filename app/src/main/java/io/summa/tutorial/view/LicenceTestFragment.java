package io.summa.tutorial.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.summa.tutorial.R;
import io.summa.tutorial.adapter.AnswersRecyclerView;
import io.summa.tutorial.listener.AnswerMarkedCallback;
import io.summa.tutorial.model.Answer;
import io.summa.tutorial.model.Question;
import io.summa.tutorial.util.Helper;

public class LicenceTestFragment extends Fragment implements View.OnClickListener, AnswerMarkedCallback {

    public static final String FRAGMENT_TAG = "LicenceTestFragment";

    private TextView mQuestionTitle, mCounter, mAnsweredCorrect;
    private ImageView mQuestionImage;
    private Button mPrevious, mNext;
    private AnswersRecyclerView mAdapter;

    private List<Question> mQuestions;
    private int mPageIndex = 0;

    private int mAnswered, mCorrect;
    private int mTestType;

    OnFinishListener mOnFinishListener;

    public LicenceTestFragment() {
    }

    public static LicenceTestFragment newInstance(List<Question> questions, OnFinishListener listener, int testType) {

        LicenceTestFragment fragment = new LicenceTestFragment();
        fragment.mQuestions = questions;
        fragment.mOnFinishListener = listener;
        fragment.mTestType = testType;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_licence_test, container, false);


        mQuestionTitle = (TextView) view.findViewById(R.id.tvTitle);
        mCounter = (TextView) view.findViewById(R.id.tvCounter);
        mAnsweredCorrect = (TextView) view.findViewById(R.id.tvAnsweredCorrect);
        mQuestionImage = (ImageView) view.findViewById(R.id.ivTitleImage);
        mPrevious = (Button) view.findViewById(R.id.btnPrevious);
        mNext = (Button) view.findViewById(R.id.btnNext);
        RecyclerView answerRecyclerView = (RecyclerView) view.findViewById(R.id.rvAnswers);

        if (mTestType != LicenceTestActivity.EXTRA_REVIEW)
            setHasOptionsMenu(true);
        else
            mAnsweredCorrect.setVisibility(View.GONE);

        mPrevious.setOnClickListener(this);
        mNext.setOnClickListener(this);

        answerRecyclerView.setLayoutManager(new LinearLayoutManager(answerRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new AnswersRecyclerView(this);

        answerRecyclerView.setAdapter(mAdapter);

        loadQuestion();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnNext) {
            if (mQuestions.size() - 1 <= mPageIndex)
                return;
            nextQuestion();
            loadQuestion();
        } else if (view.getId() == R.id.btnPrevious) {
            previousQuestion();
            loadQuestion();
        }
    }

    @Override
    public void onMarked(Answer answer) {
        Question question = mQuestions.get(mPageIndex);

        int answerIndex = question.getAnswerList().indexOf(answer);

        question.setAnsweredIndex(answerIndex);
        question.setMarked(true);

        mAnswered++;
        if (question.getAnsweredIndex() == question.getCorrectIndex())
            mCorrect++;

        String answeredCorrectText = getResources().getString(R.string.answered_correct, mAnswered, mCorrect);
        mAnsweredCorrect.setText(answeredCorrectText);

        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.questions_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {
            getActivity().finish();
            return true;
        } else if (i == R.id.menuFinish) {
            if (getAnswered() == getQuestions().size())
                mOnFinishListener.onFinish(getQuestions(), mAnswered, mCorrect);
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(R.string.dialog_not_all_answered)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mOnFinishListener.onFinish(getQuestions(), mAnswered, mCorrect);
                            }
                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
            }
            return true;

        } else return super.onOptionsItemSelected(item);
    }

    private void loadQuestion() {
        String counterText = getResources().getString(R.string.counter_text, mPageIndex + 1, mQuestions.size());
        mCounter.setText(counterText);

        String answeredCorrectText = getResources().getString(R.string.answered_correct, mAnswered, mCorrect);
        mAnsweredCorrect.setText(answeredCorrectText);

        mQuestionTitle.setText(mQuestions.get(mPageIndex).getQuestionTitle());
        mAdapter.setNewAnswersAndRefresh(mQuestions.get(mPageIndex));

        if (mQuestions.get(mPageIndex).getQuestionImage() != null) {
            mQuestionImage.setImageResource(Helper.getDrawableIdentifier(getActivity(), mQuestions.get(mPageIndex).getQuestionImage()));
            mQuestionImage.setVisibility(View.VISIBLE);
        } else
            mQuestionImage.setVisibility(View.GONE);


    }

    private void previousQuestion() {
        mNext.setVisibility(View.VISIBLE);
        if (--mPageIndex <= 0)
            mPrevious.setVisibility(View.GONE);
        else
            mPrevious.setVisibility(View.VISIBLE);


    }

    private void nextQuestion() {
        mPrevious.setVisibility(View.VISIBLE);
        if (++mPageIndex >= mQuestions.size() - 1)
            mNext.setVisibility(View.GONE);
        else
            mNext.setVisibility(View.VISIBLE);

    }

    public List<Question> getQuestions() {
        return mQuestions;
    }

    public int getAnswered() {
        return mAnswered;
    }

    public int getCorrect() {
        return mCorrect;
    }

    public interface OnFinishListener {
        void onFinish(List<Question> questions, int answered, int correct);
    }

}
