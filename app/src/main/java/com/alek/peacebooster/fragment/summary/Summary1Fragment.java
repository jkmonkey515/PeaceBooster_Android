package com.alek.peacebooster.fragment.summary;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentSummary1Binding;
import com.alek.peacebooster.fragment.BaseFragment;

public class Summary1Fragment extends BaseFragment {

    String[] summaryTexts;
    int summaryIndex = 0;

    String imageName;

    private FragmentSummary1Binding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSummary1Binding.inflate(inflater,container,false);

        setTitle(R.string.summary_title);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();

        summaryTexts = getResources().getStringArray(R.array.summary_texts);
        String question = summaryTexts[summaryIndex];

        setHighlightText(question);

        mBinding.imvQuestion.setImageBitmap(getBitmap());
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

        String howeverStr = getResources().getString(R.string.however);
        if (question.contains(howeverStr)) {
            question = question.replace(howeverStr, "<br><br>"+howeverStr);
        }

        String highlightedStr = question;

        mBinding.txvSummary.setText(Html.fromHtml(highlightedStr));

        mBinding.btnNext.setOnClickListener(view->{
            if (summaryIndex == summaryTexts.length-1) {
                setNextFragment(new Summary2Fragment());
            } else {
                changeSummary();
            }
        });
    }

    private void changeSummary() {
        summaryIndex += 1;

        String question = summaryTexts[summaryIndex];

        setHighlightText(question);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(()->{
            if (summaryIndex == 0) {
                close();
            } else {
                summaryIndex -= 2;
                changeSummary();
            }
        });
    }
}
