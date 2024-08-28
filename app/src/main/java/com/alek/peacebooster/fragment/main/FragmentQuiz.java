package com.alek.peacebooster.fragment.main;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentQuizBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Quiz;

import java.util.Locale;

public class FragmentQuiz extends BaseFragment {

    public static final String QUIZ = "quiz";

    private Quiz mQuiz;

    public static FragmentQuiz newInstance(Quiz quiz) {
        FragmentQuiz fragment = new FragmentQuiz();
        Bundle args = new Bundle();
        args.putParcelable(QUIZ, quiz);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        FragmentQuizBinding binding = FragmentQuizBinding.inflate(inflater, container, false);

        mQuiz = getArguments().getParcelable(QUIZ);
        setTitle(mQuiz.getTitle());
        binding.tvDescription.setText(mQuiz.getDescription());
        binding.rvQuestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvQuestions.setAdapter(new QuestionAdapter());
        binding.bFinish.setOnClickListener(view->setNextFragment(FragmentQuizResults.newInstance(mQuiz)));

        return binding.getRoot();
    }

    public class QuestionHolder extends RecyclerView.ViewHolder {

        private final TextView mTextViewQuestion;
        private final RadioGroup mRadioGroupAnswers;

        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewQuestion = itemView.findViewById(R.id.tvQuestion);
            mRadioGroupAnswers = itemView.findViewById(R.id.rgAnswers);
        }

        public void bindItem(int position) {
            Quiz.Question question = mQuiz.getQuestions().get(position);
            mTextViewQuestion.setText(String.format(Locale.getDefault(), "%d. %s",
                    position + 1, question.getText()));
            mRadioGroupAnswers.removeAllViews();
            for (String answer : question.getAnswers()) {
                RadioButton radioButton = new RadioButton(requireContext());
                radioButton.setText(answer);
                radioButton.setTextColor(Color.WHITE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ColorStateList colorStateList = new ColorStateList(
                            new int[][]{new int[]{android.R.attr.state_enabled},
                                    new int[]{android.R.attr.state_selected}},
                            new int[]{Color.WHITE, Color.BLUE}
                    );
                    radioButton.setButtonTintList(colorStateList);
                    radioButton.invalidate();
                }

                mRadioGroupAnswers.addView(radioButton);
            }
            mRadioGroupAnswers.setOnCheckedChangeListener((radioGroup, i) ->
                    question.setUserAnswer(radioGroup.indexOfChild(radioGroup.findViewById(i))));

        }
    }

    public class QuestionAdapter extends RecyclerView.Adapter<QuestionHolder> {

        @NonNull
        @Override
        public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_question, parent, false);
            return new QuestionHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuestionHolder holder, int position) {
            holder.bindItem(position);
        }

        @Override
        public int getItemCount() {
            return mQuiz.getQuestions().size();
        }
    }
}
