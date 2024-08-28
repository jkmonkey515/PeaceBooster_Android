package com.alek.peacebooster.fragment.summary;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.databinding.FragmentSummaryFinalBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.R;

public class SummaryFinalFragment extends BaseFragment {

    String[] summaryQuestions;

    String imageName;

    private FragmentSummaryFinalBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSummaryFinalBinding.inflate(inflater,container,false);

        setTitle(R.string.summary_title);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();

        summaryQuestions = getResources().getStringArray(R.array.summary_finals);
        String question = summaryQuestions[0];
        setHighlightText(question);

        mBinding.imvQuestion.setImageBitmap(getBitmap());

        mBinding.btnNext.setOnClickListener(view -> setNextFragment(new SummaryShareFragment()));
    }

    private void setHighlightText(String question) {
        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }
//        if (question.contains("4")) {
//            question = question.replace("4", "\n4");
//        }

        String highlightedStr = question;

        mBinding.txvSummary.setText(Html.fromHtml(highlightedStr));
    }

}
