package com.alek.peacebooster.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alek.peacebooster.activity.MainActivity;

public abstract class BaseFragment extends Fragment {

    private MainActivity mMainActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mMainActivity = (MainActivity) requireActivity();
    }

    public void close(){
        mMainActivity.getSupportFragmentManager().popBackStack();
    }

    public void setNextFragment(BaseFragment fragment){
        mMainActivity.setFragment(fragment);
    }

    public void setBitmap(Bitmap bitmap){
        mMainActivity.setBitmap(bitmap);
    }

    public Bitmap getBitmap() {
        return mMainActivity.getBitmap();
    }

    public String getUserName() {
        return mMainActivity.getUserName();
    }

    public void setUserName(String userName) {
        mMainActivity.setUserName(userName);
    }

    public String getPhotoName() {
        return mMainActivity.getPhotoName();
    }

    public void setPhotoName(String photoName) {
        mMainActivity.setPhotoName(photoName);
    }

    public void setTitle(String title){
        mMainActivity.setTitle(title);
    }

    public void setTitle(int resId){
        mMainActivity.setTitle(getString(resId));
    }

    public void setCustomBackListener(MainActivity.BackListener listener){
        mMainActivity.setCustomBackListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMainActivity.updateData(this);
    }

    public void showToast(final String message) {
        requireActivity().runOnUiThread(() -> Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show());
    }

    public void showToast(int id) {
        showToast(getString(id));
    }
}
