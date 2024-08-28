package com.alek.peacebooster.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alek.peacebooster.R;
import com.alek.peacebooster.databinding.FragmentQuizzesBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Quiz;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentQuizzes extends BaseFragment {

    private final ArrayList<Quiz> mQuizzes = new ArrayList<>();
    private QuizAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentQuizzesBinding binding = FragmentQuizzesBinding.inflate(inflater, container, false);

        setTitle(getString(R.string.take_quiz));

        binding.rvQuizzes.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new QuizAdapter();
        binding.rvQuizzes.setAdapter(mAdapter);

        getQuizzes();

        return binding.getRoot();
    }

    private void getQuizzes(){
        FirebaseFirestore.getInstance().collection("quizzes").addSnapshotListener((value, error) -> {
            if(value!=null){
                mQuizzes.clear();
                for(DocumentSnapshot document:value.getDocuments()){
                    mQuizzes.add(document.toObject(Quiz.class));
                }
                if(mAdapter!=null){
                    mAdapter.differ.submitList(mQuizzes);
                }
            }
        });
    }

    public class QuizHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Quiz mQuiz;
        private final TextView mTextViewTitle;
        private final TextView mTextViewDescription;

        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextViewTitle = itemView.findViewById(R.id.tvTitle);
            mTextViewDescription = itemView.findViewById(R.id.tvDescription);
        }

        public void bindItem(Quiz quiz){
            mQuiz = quiz;
            mTextViewTitle.setText(quiz.getTitle());
            mTextViewDescription.setText(quiz.getDescription());
        }

        @Override
        public void onClick(View view) {
            setNextFragment(FragmentQuiz.newInstance(mQuiz));
        }
    }

    public class QuizAdapter extends RecyclerView.Adapter<QuizHolder>{

        @NonNull
        @Override
        public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_quiz,parent,false);
            return new QuizHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
            holder.bindItem(differ.getCurrentList().get(position));
        }

        @Override
        public int getItemCount() {
            return differ.getCurrentList().size();
        }

        private final AsyncListDiffer<Quiz> differ = new AsyncListDiffer<>
                (this, new DiffUtil.ItemCallback<Quiz>() {
            @Override
            public boolean areItemsTheSame(@NonNull Quiz oldItem, @NonNull Quiz  newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Quiz  oldItem, @NonNull Quiz  newItem) {
                return oldItem.equals(newItem);
            }
        });
    }
}
