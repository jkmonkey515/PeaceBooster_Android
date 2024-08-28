package com.alek.peacebooster.fragment.summary;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.activity.LanguagesActivity;
import com.alek.peacebooster.databinding.FragmentSummary2Binding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.fragment.steps.ImageSquaresFragment;
import com.alek.peacebooster.fragment.steps.Step2Fragment;
import com.alek.peacebooster.fragment.steps.Step3Fragment;
import com.alek.peacebooster.R;

public class Summary2Fragment extends BaseFragment {

    String[] summaryQuestions;
    int queryIndex = 0;

    String imageName;
    String userName;

    private FragmentSummary2Binding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSummary2Binding.inflate(inflater,container,false);

        setTitle(R.string.summary_title);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();
        userName = getUserName();

        summaryQuestions = getResources().getStringArray(R.array.summary_questions);
        String question = summaryQuestions[queryIndex];

        setHighlightText(question);

        mBinding.imvQuestion.setImageBitmap(getBitmap());

        mBinding.lyOptions.setVisibility(View.GONE);

        mBinding.btnYes.setOnClickListener(view -> {
            switch (queryIndex) {
                case 0: // The only difference between you are the names that were assigned to you. ....
                    queryIndex = 3;
                    changeQuestion();
                    break;
                case 1: // If NAME exists without her/his own-self (own-being) and ...
                    queryIndex += 1;
                    showIncorrectAlert();
                    break;
                case 2: // Only singular entities can have their own-self (being, existence) which you discovered after completing
                    queryIndex += 1;
                    changeQuestion();
                    break;
                case 3: //
                    gotoFinalConclusion();
                    break;
                case 4: //
                    break;
            }
        });

        mBinding.btnNo.setOnClickListener(view -> {
            switch (queryIndex) {
                case 0:
                case 2:
                case 3:
                    queryIndex += 1;
                    showIncorrectAlert();
                    break;
                case 1:
                    queryIndex = 3;
                    changeQuestion();
                    break;
                case 4:
                    break;
            }
        });

        mBinding.tvLinkStep1.setOnClickListener(view -> goBackStep1());
        mBinding.tvLinkStep2.setOnClickListener(view -> goBackStep2());
        mBinding.tvLinkStep3.setOnClickListener(view -> goBackStep3());

        mBinding.txvLink.setOnClickListener(view -> {
            if (queryIndex == 0) {
                goBackStep3();
            }
            if (queryIndex == 2) {
                goBackStep1();
            }
        });
        mBinding.tvLinkStartOver.setOnClickListener(view -> {
            final Intent intent = new Intent(requireContext(), LanguagesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void changeQuestion() {

        String question = summaryQuestions[queryIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }

        if (question.contains("(==)")) {
            question = question.replace("(==)", userName);
        }

        setHighlightText(question);
    }

    private void setHighlightText(String question) {
        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }

        if (question.contains("(==)")) {
            question = question.replace("(==)", userName);
        }

        String viceStr = getResources().getString(R.string.viceversa);
        if (question.contains(viceStr)) { // for index==3
            question = question.replace(viceStr, viceStr+"<br><br>");
        }

        String highlightedStr = question;

        mBinding.txvSummary.setText(Html.fromHtml(highlightedStr));

        if (queryIndex == 0) {
            mBinding.txvLink.setVisibility(View.VISIBLE);

            String link3Str = getResources().getString(R.string.link_step3);

            mBinding.txvLink.setText(Html.fromHtml(link3Str));

            mBinding.txvEnding.setVisibility(View.VISIBLE);
            String endingString = "<span><br>" + mBinding.txvEnding.getText() + "</span>";

            mBinding.txvEnding.setText(Html.fromHtml(endingString));
        } else {
            mBinding.txvEnding.setVisibility(View.GONE);
            mBinding.txvLink.setVisibility(View.GONE);
            if (queryIndex == 2) {
                mBinding.txvLink.setVisibility(View.VISIBLE);
                String link1Str = "<span>" + getResources().getString(R.string.link_step1) + "</span>";
                mBinding.txvLink.setText(Html.fromHtml(link1Str));
            }
        }

        // for index===4
        if (queryIndex == 4) {
            mBinding.lyOptions.setVisibility(View.VISIBLE);
            String link1Str = "<span ><u>" + getResources().getString(R.string.link_step1) + "</u></span>";
            mBinding.tvLinkStep1.setText(Html.fromHtml(link1Str));
            String link2Str = "<span ><u>" + getResources().getString(R.string.link_step2) + "</u></span>";
            mBinding.tvLinkStep2.setText(Html.fromHtml(link2Str));
            String link3Str = "<span ><u>" + getResources().getString(R.string.link_step3) + "</u></span>";
            mBinding.tvLinkStep3.setText(Html.fromHtml(link3Str));
            String linkStartOverStr = "<span ><u>" + getResources().getString(R.string.button_start_over_or) + "</u></span>";
            mBinding.tvLinkStartOver.setText(Html.fromHtml(linkStartOverStr));

            mBinding.lyButtons.setVisibility(View.GONE);
        } else {
            mBinding.lyOptions.setVisibility(View.GONE);
            mBinding.lyButtons.setVisibility(View.VISIBLE);
        }
    }

    private void showIncorrectAlert() {

        String contentStr = getResources().getString(R.string.step3_incorrect_alert);

        if (queryIndex==3) {
            contentStr = getResources().getString(R.string.step3_incorrect_alert2);
        }
        if (queryIndex==4) {
            contentStr = getResources().getString(R.string.step3_incorrect_alert3);
        }

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_incorrect));
        contentView.setText(contentStr);
        alertButton.setText(getResources().getString(R.string.button_next));

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();

            if (queryIndex == 3) {
                goBackStep1();
            } else {
                changeQuestion();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(()->{
            if (queryIndex == 0) {
                close();
            } else {
                queryIndex -= 1;
                changeQuestion();
            }
        });
    }

    private void gotoFinalConclusion() {
        setNextFragment(new SummaryFinalFragment());
    }

    private void goBackStep1() {
        setNextFragment(new ImageSquaresFragment());
    }

    private void goBackStep2() {
        setNextFragment(new Step2Fragment());
    }

    private void goBackStep3() {
        setNextFragment(new Step3Fragment());
    }

}
