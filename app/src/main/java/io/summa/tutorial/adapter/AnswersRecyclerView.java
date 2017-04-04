package io.summa.tutorial.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.summa.tutorial.R;
import io.summa.tutorial.model.Answer;

/**
 * Created by nemanja on 4/1/17.
 */

public class AnswersRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Answer> mAnswers;

    public AnswersRecyclerView() {
    }

    public void setNewAnswersAndRefresh(List<Answer> answers) {
        mAnswers = answers;
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
        ((BaseViewHolder) holder).bind(mAnswers.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return mAnswers.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return mAnswers != null ? mAnswers.size() : 0;
    }

    private abstract class BaseViewHolder extends RecyclerView.ViewHolder {
        BaseViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bind(Answer answer);
    }

    private class TextAnswerHolder extends BaseViewHolder {
        TextView mTextAnswer;

        TextAnswerHolder(View itemView) {
            super(itemView);
            mTextAnswer = (TextView) itemView.findViewById(R.id.tvAnswerText);
        }

        @Override
        public void bind(Answer answer) {

            mTextAnswer.setText(answer.getAnswer());
            mTextAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo set answer
                }
            });
        }
    }

    private class ImageAnswerHolder extends BaseViewHolder implements View.OnClickListener {
        ImageView mImageAnswer;

        ImageAnswerHolder(View itemView) {
            super(itemView);
            mImageAnswer = (ImageView) itemView.findViewById(R.id.ivAnswerImage);
        }

        @Override
        public void bind(Answer answer) {

            //todo set image

            mImageAnswer.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //todo set answer
        }
    }
}
