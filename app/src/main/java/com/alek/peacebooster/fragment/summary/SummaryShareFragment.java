package com.alek.peacebooster.fragment.summary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.activity.LanguagesActivity;
import com.alek.peacebooster.databinding.FragmentSummaryShareBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.fragment.steps.Step3Fragment;
import com.alek.peacebooster.fragment.steps.ImageSquaresFragment;
import com.alek.peacebooster.fragment.steps.Step2Fragment;

public class SummaryShareFragment extends BaseFragment {

    String[] summaryQuestions;
    int queryIndex = 1;

    String imageName;

    private FragmentSummaryShareBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSummaryShareBinding.inflate(inflater,container,false);

        setTitle(R.string.summary_title);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();

        summaryQuestions = getResources().getStringArray(R.array.summary_finals);
        String question = summaryQuestions[queryIndex];

        setHighlightText(question);

        mBinding.imvQuestion.setImageBitmap(getBitmap());

        String link1Str = "<span ><u>" + getResources().getString(R.string.link_step1) + "</u></span>";
        mBinding.tvLinkStep1.setText(Html.fromHtml(link1Str));
        String link2Str = "<span ><u>" + getResources().getString(R.string.link_step2) + "</u></span>";
        mBinding.tvLinkStep2.setText(Html.fromHtml(link2Str));
        String link3Str = "<span ><u>" + getResources().getString(R.string.link_step3) + "</u></span>";
        mBinding.tvLinkStep3.setText(Html.fromHtml(link3Str));

        mBinding.btnNext.setOnClickListener(view -> setNextFragment(new WebDonateFragment()));
    }

    private void setHighlightText(String question) {
        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }

        if (question.contains("1.")) {
            question = question.replace("1.", "<br>1.");
        }

        if (question.contains("2.")) {
            question = question.replace("2.", "<br>2.");
        }

        if (question.contains("3.")) {
            question = question.replace("3.", "<br>3.");
        }

        if (question.contains("4.")) {
            question = question.replace("4.", "<br>4.");
        }

        String strThank = getResources().getString(R.string.thankyou);
        if (question.contains(strThank)) {
            question = question.replace(strThank, "<br><br>"+strThank+"<br>");
        }

        String highlightedStr = "<span >" + question + "</span>";

        mBinding.txvSummary.setText(Html.fromHtml(highlightedStr));

        mBinding.tvLinkStep1.setOnClickListener(view -> setNextFragment(new ImageSquaresFragment()));
        mBinding.tvLinkStep2.setOnClickListener(view -> setNextFragment(new Step2Fragment()));
        mBinding.tvLinkStep3.setOnClickListener(view -> setNextFragment(new Step3Fragment()));
        mBinding.btnAnotherPicture.setOnClickListener(view -> {
            final Intent intent = new Intent(requireContext(), LanguagesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
        mBinding.btnSmsText1.setOnClickListener(view -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:50409?&body=SIGN%20PPQAGR"));
            startActivity(smsIntent);
        });
        mBinding.btnSmsText2.setOnClickListener(view -> {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:50409?&body=SIGN%20PVWHOA"));
            startActivity(smsIntent);
        });
        mBinding.btnTweetMsg1.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/messages/compose?recipient_id=50409&text=SIGN%20PPQAGR"));
            startActivity(browserIntent);
        });
        mBinding.btnTweetMsg2.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://twitter.com/messages/compose?recipient_id=50409&text=SIGN%20PVWHOA"));
            startActivity(browserIntent);
        });
    }
}