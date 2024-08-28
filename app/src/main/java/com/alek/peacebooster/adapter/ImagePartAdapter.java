package com.alek.peacebooster.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.alek.peacebooster.fragment.steps.ImageSquaresFragment;
import com.alek.peacebooster.model.ImagePart;
import com.alek.peacebooster.R;

import java.util.ArrayList;


public class ImagePartAdapter extends BaseAdapter {

    private final ImageSquaresFragment mFragment;
    private final ArrayList<ImagePart> mImageParts;

    public ImagePartAdapter(ImageSquaresFragment fragment, ArrayList<ImagePart> images){
        mFragment = fragment;
        mImageParts = images;
    }

    public int getCount() {
        return mImageParts.size();
    }

    public Object getItem(int position) {
        return mImageParts.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView picture;
        ImageView redBlurView;

        if (convertView == null) {
            convertView = mFragment.getLayoutInflater().inflate(R.layout.image_square_item, parent, false);
        }

        picture = convertView.findViewById(R.id.picture);
        picture.setImageBitmap(mImageParts.get(position).getPartImage());

        redBlurView = convertView.findViewById(R.id.imvRedBlur);
        if (mImageParts.get(position).getStatus()) {
            redBlurView.setVisibility(View.VISIBLE);
        } else {
            redBlurView.setVisibility(View.GONE);
        }

        picture.setOnClickListener(view -> {
            mFragment.showSelectedImage(mImageParts.get(position).getPartImage());
            redBlurView.setVisibility(View.VISIBLE);
            mImageParts.get(position).setStatus(true);
        });

        return convertView;
    }
}

