package com.alek.peacebooster.fragment.main;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentOfficialRulesBinding;
import com.alek.peacebooster.fragment.BaseFragment;

import android.text.method.ScrollingMovementMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class OfficialRulesFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentOfficialRulesBinding binding = FragmentOfficialRulesBinding.inflate(inflater,container,false);
        setTitle(R.string.official_title);
        binding.txvOfficial.setMovementMethod(new ScrollingMovementMethod());

        return binding.getRoot();
    }

}