package io.summa.tutorial.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.summa.tutorial.R;
import io.summa.tutorial.listener.AnswerMarkedCallback;
import io.summa.tutorial.model.Answer;
import io.summa.tutorial.model.Question;
import io.summa.tutorial.util.Helper;

/**
 * Created by nemanja on 4/1/17.
 */

public class AnswersRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Question mQuestion;
    private AnswerMarkedCallback mMarkedCallback;

    public AnswersRecyclerView(AnswerMarkedCallback callback) {
        mMarkedCallback = callback;
    }

    public void setNewAnswersAndRefresh(Question question) {
        mQuestion = question;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (viewType) {
            case Answer.TEXT:
                return new TextAnswerHolder(inflater.inflate(R.layout.text_answer, parent, false));
            case Answer.IMAGE:
                return new ImageAnswerHolder(inflater.inflate(R.layout.image_answer, parent, false));
            default:
                throw new RuntimeException("woopsie");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((BaseViewHolder) holder).bind(mQuestion.getAnswerList().get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mQuestion.getAnswerList().get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mQuestion.getAnswerList() != null ? mQuestion.getAnswerList().size() : 0;
    }


    private void markAnswer(Answer answer) {
        mMarkedCallback.onMarked(answer);
    }

    private abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(Answer answer);
    }

    private class TextAnswerHolder extends BaseViewHolder {
        TextView mTextAnswer;
        LinearLayout mllHolder;

        TextAnswerHolder(View itemView) {
            super(itemView);
            mTextAnswer = (TextView) itemView.findViewById(R.id.tvAnswerText);
            mllHolder = (LinearLayout) itemView.findViewById(R.id.llTextHolder);
        }

        @Override
        public void bind(final Answer answer) {

            mTextAnswer.setText(answer.getAnswer());
            mllHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mQuestion.isMarked())
                        markAnswer(answer);
                }
            });

            if (mQuestion.isMarked()) {
                int answerIndex = mQuestion.getAnswerList().indexOf(answer);
                if (answerIndex == mQuestion.getAnsweredIndex() && answerIndex != mQuestion.getCorrectIndex())
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.incorrectBackground));
                else if (answerIndex == mQuestion.getCorrectIndex())
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.correctBackground));
                else
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.defaultBackground));

            } else
                mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.defaultBackground));

        }
    }

    private class ImageAnswerHolder extends BaseViewHolder {
        ImageView mImageAnswer;
        LinearLayout mllHolder;

        ImageAnswerHolder(View itemView) {
            super(itemView);
            mImageAnswer = (ImageView) itemView.findViewById(R.id.ivAnswerImage);
            mllHolder = (LinearLayout) itemView.findViewById(R.id.llImageHolder);
        }

        @Override
        public void bind(final Answer answer) {

            mImageAnswer.setImageResource(Helper.getDrawableIdentifier(mImageAnswer.getContext(), answer.getAnswer()));
            mllHolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!mQuestion.isMarked())
                        markAnswer(answer);
                }
            });
            if (mQuestion.isMarked()) {
                int answerIndex = mQuestion.getAnswerList().indexOf(answer);
                if (answerIndex == mQuestion.getAnsweredIndex() && answerIndex != mQuestion.getCorrectIndex())
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.incorrectBackground));
                else if (answerIndex == mQuestion.getCorrectIndex())
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.correctBackground));
                else
                    mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.defaultBackground));

            } else
                mllHolder.setBackgroundColor(ContextCompat.getColor(mllHolder.getContext(), R.color.defaultBackground));
        }

    }
}
