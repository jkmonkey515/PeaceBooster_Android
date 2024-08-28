package com.alek.peacebooster.fragment.steps;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.databinding.FragmentAskEntityBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.R;

public class AskEntityFragment extends BaseFragment {

    String photoName;

    FragmentAskEntityBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentAskEntityBinding.inflate(inflater,container,false);

        setTitle(R.string.step1);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {
        photoName = getPhotoName();
        String question = getResources().getString(R.string.step1_second_question);

        if (question.contains("(--)")) {
            question = question.replace("(--)", photoName);
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);

        mBinding.tvOptionExplain.setText(getResources().getString(R.string.option_explain).replace("(--)", photoName));

        String[] optionsStr = getResources().getStringArray(R.array.step1_options);
        mBinding.btnOption1Single.setText(optionsStr[0].replace("(--)", photoName));
        mBinding.btnOption2Alone.setText(optionsStr[1].replace("(--)", photoName));
        mBinding.btnOption3Named.setText(optionsStr[2].replace("(--)", photoName));

        mBinding.imvStep1Img.setImageBitmap(getBitmap());
        mBinding.btnOption1Single.setOnClickListener(view->setNextFragment(new ImageSquaresFragment()));
    }

}
