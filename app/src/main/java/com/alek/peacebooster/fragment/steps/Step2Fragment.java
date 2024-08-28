package com.alek.peacebooster.fragment.steps;

import android.app.Dialog;
import android.content.Intent;
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

import com.alek.peacebooster.databinding.FragmentStep2Binding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.R;
import com.alek.peacebooster.activity.LanguagesActivity;


public class Step2Fragment extends BaseFragment {

    String[] step2Questions;
    int questionIndex = 0;

    String imageName;

    private FragmentStep2Binding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentStep2Binding.inflate(inflater, container,false);

        setTitle(R.string.step2_title);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();

        String titleStr = "2: " + getResources().getString(R.string.step2_title);
        if (titleStr.contains("(--)")) {
            titleStr = titleStr.replace("(--)", imageName);
        }
        setTitle(titleStr);

        step2Questions = getResources().getStringArray(R.array.step2_questions);
        String question = step2Questions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);

        mBinding.imvQuestion.setImageBitmap(getBitmap());

        mBinding.btnYes.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0: // Could (name) bring himself/herself into existence without her/his parents?
                case 1: // Did (name) produce (himself/herself) alone without any parents?
                case 2: // Did (name) produce (himself/herself) alone without any artificial laboratory ...
                    showIncorrectAlert();
                    break;
                case 3: // restart from Q1 // Agree
                    showCongAlert();
                    break;
            }
        });
        mBinding.btnNo.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0:
                case 1:
                case 2:
                    showCongAlert();
                    break;
                case 3: // disagree
                    showP4Alert();
                    break;
            }
        });
    }

    private void changeQuestion() {
        questionIndex += 1;

        String question = step2Questions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);

        if (questionIndex == 3) {
            mBinding.btnYes.setText(getResources().getString(R.string.button_agree));
            mBinding.btnNo.setText(getResources().getString(R.string.button_disagree));
        } else {
            mBinding.btnYes.setText(getResources().getString(R.string.answerYes));
            mBinding.btnNo.setText(getResources().getString(R.string.answerNo));
        }
    }

    private void showIncorrectAlert() {

        String contentStr = getResources().getString(R.string.title_notice);

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(contentStr);
        contentView.setText(getResources().getString(R.string.step2_incorrect_alert));
        alertButton.setText(getResources().getString(R.string.button_next));

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();

            changeQuestion();
        });
    }

    private void showCongAlert() {

        String cont = getResources().getString(R.string.complete_step2);
        String temp = cont.replace("(--)", imageName);

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_congratulation));
        contentView.setText(temp);
        alertButton.setText(R.string.go_to_point3);

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();
            setNextFragment(new Step3Fragment());
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(()->{
            if (questionIndex == 0) {
                close();
            } else {
                questionIndex -= 2;
                changeQuestion();
            }
        });
    }

    private void showP4Alert() {

        String contentStr = getResources().getString(R.string.p4_disagree);

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert2);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton1 = exitDialog.findViewById(R.id.alert_button1);
        Button alertButton2 = exitDialog.findViewById(R.id.alert_button2);

        titleView.setText(getResources().getString(R.string.title_notice));
        contentView.setText(contentStr);
        alertButton1.setText(getResources().getString(R.string.link_step2));
        alertButton2.setText(getResources().getString(R.string.button_start_over));

        // restart this step
        alertButton1.setOnClickListener(view -> {
            exitDialog.dismiss();

            questionIndex = -1;
            changeQuestion();
        });

        // start over
        alertButton2.setOnClickListener(view -> {
            exitDialog.dismiss();

            onStartOver();
        });
    }

    private void onStartOver() {
        final Intent intent = new Intent(requireContext(), LanguagesActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK); // remove all activities
        startActivity(intent);
    }
}
