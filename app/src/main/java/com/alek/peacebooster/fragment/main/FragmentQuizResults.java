package com.alek.peacebooster.fragment.main;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentQuizResultsBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Quiz;

public class FragmentQuizResults extends BaseFragment {

    public static final String QUIZ = "quiz";

    private Quiz mQuiz;

    public static FragmentQuizResults newInstance(Quiz quiz) {
        FragmentQuizResults fragment = new FragmentQuizResults();
        Bundle args = new Bundle();
        args.putParcelable(QUIZ, quiz);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentQuizResultsBinding binding = FragmentQuizResultsBinding
                .inflate(inflater, container, false);

        mQuiz = getArguments().getParcelable(QUIZ);
        setTitle(mQuiz.getTitle());

        binding.tvResultScore.setText(mQuiz.getResultCountText(requireContext()));
        binding.tvResultScore.setTextColor(mQuiz.getResultColor());
        binding.tvResultText.setText(mQuiz.getResultMessage());

        binding.rvUserAnswers.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvUserAnswers.setAdapter(new AnswerAdapter());

        return binding.getRoot();
    }

    public class AnswerHolder extends RecyclerView.ViewHolder{

        private final TextView mTextViewQuestion, mTextViewUserAnswer, mTextViewCorrectAnswer, mTextViewDetails;

        public AnswerHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewQuestion = itemView.findViewById(R.id.tvQuestion);
            mTextViewUserAnswer = itemView.findViewById(R.id.tvUserAnswer);
            mTextViewCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            mTextViewDetails = itemView.findViewById(R.id.tvDetails);
        }

        public void bindItem(Quiz.Question question){
            mTextViewQuestion.setText(question.getText());
            if(question.getUserAnswer()>-1) {
                mTextViewUserAnswer.setText(String.format(getString(R.string.your_answer_),
                        question.getAnswers().get(question.getUserAnswer())));
            }else mTextViewUserAnswer.setText(R.string.question_skipped);
            mTextViewCorrectAnswer.setText(String.format(getString(R.string.correct_answer_),
                    question.getAnswers().get((int)(question.getCorrectAnswer()))));
            mTextViewDetails.setText(question.getDetails());

            if(question.isAnsweredCorrectly()){
                mTextViewCorrectAnswer.setVisibility(View.GONE);
                mTextViewUserAnswer.setTextColor(Color.GREEN);
            }else{
                mTextViewCorrectAnswer.setVisibility(View.VISIBLE);
                mTextViewUserAnswer.setTextColor(Color.RED);
            }
        }
    }

    public class AnswerAdapter extends RecyclerView.Adapter<AnswerHolder>{

        @NonNull
        @Override
        public AnswerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_user_answer, parent, false);
            return new AnswerHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AnswerHolder holder, int position) {
            holder.bindItem(mQuiz.getQuestions().get(position));
        }

        @Override
        public int getItemCount() {
            return mQuiz.getQuestions().size();
        }
    }
}
