package com.alek.peacebooster.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentArticlesBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Article;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentArticles extends BaseFragment {

    private final ArrayList<Article> mArticles = new ArrayList<>();
    private ArticleAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.alek.peacebooster.databinding.FragmentArticlesBinding binding = FragmentArticlesBinding.inflate(inflater, container, false);

        setTitle(R.string.read_articles);

        binding.rvArticles.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new ArticleAdapter();
        binding.rvArticles.setAdapter(mAdapter);

        getArticles();

        return binding.getRoot();
    }

    private void getArticles(){
        FirebaseFirestore.getInstance().collection("articles").get().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult()!=null){
                mArticles.clear();
                for(DocumentSnapshot document:task.getResult().getDocuments()){
                    mArticles.add(document.toObject(Article.class));
                }
                if(mAdapter!=null){
                    mAdapter.differ.submitList(mArticles);
                }
            }
        });
    }

    public class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Article mArticle;
        private final TextView mTextViewTitle;
        private final TextView mTextViewText;
        private final ImageView mImageViewShare;

        public ArticleHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextViewTitle = itemView.findViewById(R.id.tvTitle);
            mTextViewText = itemView.findViewById(R.id.tvContent);
            mImageViewShare = itemView.findViewById(R.id.ivShare);
        }

        public void bindItem(Article article){
            mArticle = article;
            mTextViewTitle.setText(article.getHeader());
            mTextViewText.setText(Html.fromHtml(article.getText()));
            mImageViewShare.setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, article.getText());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, article.getHeader());
                sendIntent.setType("text/html");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            });
        }

        @Override
        public void onClick(View view) {
            setNextFragment(FragmentArticleDetail.newInstance(mArticle));
        }
    }

    public class ArticleAdapter extends RecyclerView.Adapter<ArticleHolder>{

        @NonNull
        @Override
        public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_article,parent,false);
            return new ArticleHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
            holder.bindItem(differ.getCurrentList().get(position));
        }

        @Override
        public int getItemCount() {
            return differ.getCurrentList().size();
        }

        private final AsyncListDiffer<Article> differ = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<Article>() {
            @Override
            public boolean areItemsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Article oldItem, @NonNull Article newItem) {
                return oldItem.equals(newItem);
            }
        });
    }
}
