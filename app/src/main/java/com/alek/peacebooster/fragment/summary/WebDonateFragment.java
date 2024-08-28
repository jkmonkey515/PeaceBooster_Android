package com.alek.peacebooster.fragment.summary;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentWebDonateBinding;
import com.alek.peacebooster.fragment.BaseFragment;

public class WebDonateFragment extends BaseFragment {

    String strUrl = "";

    FragmentWebDonateBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentWebDonateBinding.inflate(inflater,container,false);

        setTitle(R.string.please_donate);
        loadInitData();

        return mBinding.getRoot();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void loadInitData() {
        strUrl = "https://vastself.org/donate";

        mBinding.donateWebView.setWebViewClient(new WebViewClient());
        mBinding.donateWebView.setWebChromeClient(new WebChromeClient());
        mBinding.donateWebView.getSettings().setJavaScriptEnabled(true);
        mBinding.donateWebView.loadUrl(strUrl);
    }
}
