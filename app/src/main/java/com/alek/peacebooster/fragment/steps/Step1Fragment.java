package com.alek.peacebooster.fragment.steps;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.databinding.FragmentStep1Binding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.R;

public class Step1Fragment extends BaseFragment {

    String[] step1FirstQuestions;
    int questionIndex = 0;

    Dialog mExitDialog;

    private FragmentStep1Binding mBinding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentStep1Binding.inflate(inflater,container,false);

        setTitle(R.string.step1);
        loadInitData();
        return mBinding.getRoot();
    }

    private void loadInitData() {

        step1FirstQuestions = getResources().getStringArray(R.array.step1_first_questions);
        String question = step1FirstQuestions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", getPhotoName());
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);
        mBinding.imvStep1Img.setImageBitmap(getBitmap());

        mBinding.btnNo.setOnClickListener(view->{
            switch (questionIndex) {
                case 0: // disagree to the first question, show question 2
                    changeQuestion();
                    break;
                case 1: // realized his problem and click NO, so that go to next page
                case 2:
                    gotoNext();
                    break;
            }
        });

        mBinding.btnYes.setOnClickListener(view->{
            switch (questionIndex) {
                case 0: // agree to the question 1, go to next page
                    gotoNext();
                    break;
                case 1: // click Yes to the question 2, show the next question
                    changeQuestion();
                    break;
                case 2:
                    showExitAlert();
                    break;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(() -> {
            if (questionIndex == 0) {
                close();
            } else {
                questionIndex -= 2;
                changeQuestion();
            }
        });
    }

    private void changeQuestion() {
        questionIndex += 1;

        String question = step1FirstQuestions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", getPhotoName());
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);
    }

    private void gotoNext() {
        setNextFragment(new AskEntityFragment());
    }

    private void showExitAlert() {

        mExitDialog = new Dialog(requireContext());
        mExitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mExitDialog.setContentView(R.layout.dialog_alert);
        mExitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mExitDialog.show();
        mExitDialog.setCancelable(false);

        Button btnExit = mExitDialog.findViewById(R.id.alert_button);

        TextView txvContent = mExitDialog.findViewById(R.id.alert_content);

        String question = getResources().getString(R.string.q3_dialog_alert);
        if (question.contains("(--)")) {
            question = question.replace("(--)", getPhotoName());
        }
        txvContent.setText(question);

        btnExit.setOnClickListener(view -> {
            mExitDialog.dismiss();
            close();
        });
    }
}
