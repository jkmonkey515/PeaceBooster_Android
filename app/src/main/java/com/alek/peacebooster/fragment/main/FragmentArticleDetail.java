package com.alek.peacebooster.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentArticleDetailBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Article;

public class FragmentArticleDetail extends BaseFragment {

    public static final String ARTICLE = "article";

    public static FragmentArticleDetail newInstance(Article article){
        FragmentArticleDetail fragment = new FragmentArticleDetail();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE,article);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentArticleDetailBinding binding = FragmentArticleDetailBinding.inflate(inflater, container, false);
        setTitle(R.string.read_articles);

        Article article = null;
        if (getArguments() != null) {
            article = getArguments().getParcelable(ARTICLE);
        }
        if(article == null) close();
        else{
            binding.tvTitle.setText(article.getHeader());
            binding.wvArticle.loadData(article.getText(), "text/html", "UTF-8");
        }

        return binding.getRoot();
    }
}
