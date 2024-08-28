package com.alek.peacebooster.fragment.main;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.alek.peacebooster.databinding.FragmentVideosBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.Video;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FragmentVideos extends BaseFragment {

    private final ArrayList<Video> mVideos = new ArrayList<>();
    private VideoAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.alek.peacebooster.databinding.FragmentVideosBinding binding = FragmentVideosBinding.inflate(inflater, container, false);

        setTitle(R.string.view_videos);

        binding.rvVideos.setLayoutManager(new LinearLayoutManager(requireContext()));
        mAdapter = new VideoAdapter();
        binding.rvVideos.setAdapter(mAdapter);

        getVideos();

        return binding.getRoot();
    }

    private void getVideos() {
        FirebaseFirestore.getInstance().collection("videos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                mVideos.clear();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    mVideos.add(document.toObject(Video.class));
                }
                if(mAdapter!=null){
                    mAdapter.differ.submitList(mVideos);
                }
            }
        });
    }

    public class VideoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Video mVideo;
        private final TextView mTextViewDescription;
        private final ImageView mImageViewThumbnail, mImageViewShare;

        public VideoHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextViewDescription = itemView.findViewById(R.id.tvDescription);
            mImageViewThumbnail = itemView.findViewById(R.id.ivThumbnail);
            mImageViewShare = itemView.findViewById(R.id.ivShare);
        }

        public void bindItem(Video video) {
            mVideo = video;
            mTextViewDescription.setText(video.getDescription());
            Glide.with(requireContext()).load(video.getThumbnail()).into(mImageViewThumbnail);
            mImageViewShare.setOnClickListener(view -> {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.youtube.com/watch?v=" + mVideo.getVideoId());
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, video.getDescription());
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            });
        }

        @Override
        public void onClick(View view) {
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + mVideo.getVideoId()));
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + mVideo.getVideoId()));
            try {
                requireContext().startActivity(appIntent);
            } catch (ActivityNotFoundException ex) {
                requireContext().startActivity(webIntent);
            }
        }
    }

    public class VideoAdapter extends RecyclerView.Adapter<VideoHolder> {

        @NonNull
        @Override
        public VideoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.item_video, parent, false);
            return new VideoHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoHolder holder, int position) {
            holder.bindItem(differ.getCurrentList().get(position));
        }

        @Override
        public int getItemCount() {
            return differ.getCurrentList().size();
        }

        private final AsyncListDiffer<Video> differ = new AsyncListDiffer<>(this, new DiffUtil.ItemCallback<Video>() {
            @Override
            public boolean areItemsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return oldItem.getId().equals(newItem.getId());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Video oldItem, @NonNull Video newItem) {
                return oldItem.equals(newItem);
            }
        });
    }
}
