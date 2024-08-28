package com.alek.peacebooster.activity;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alek.peacebooster.R;
import com.alek.peacebooster.fragment.main.CameraFragment;
import com.alek.peacebooster.databinding.ActivityMainBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.fragment.main.FragmentArticles;
import com.alek.peacebooster.fragment.main.FragmentQuizzes;
import com.alek.peacebooster.fragment.main.FragmentVideos;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private ActivityMainBinding mBinding;
    private ImageView mImageViewBack;
    private TextView mTextViewTitle;

    private String mCurrentFragmentClass;
    private Bitmap mBitmap;
    private String mPhotoName = "";
    private String mUserName = "";

    private BackListener mBackListener;

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("new_content");

        mDrawerToggle = new ActionBarDrawerToggle(this, mBinding.drawerLayout, R.string.open,R.string.close);
        mBinding.drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        ActionBar bar = getSupportActionBar();
        if(bar!=null) {
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            bar.setCustomView(R.layout.toolbar);
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeButtonEnabled(true);
        }

        mImageViewBack = findViewById(R.id.ivBack);
        mTextViewTitle = findViewById(R.id.tvTitle);

        checkPermissions();

        mBinding.navView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if(itemId == R.id.itemArticles) setFragment(new FragmentArticles());
            else if(itemId == R.id.itemVideos) setFragment(new FragmentVideos());
            else if(itemId == R.id.itemQuizzes) setFragment(new FragmentQuizzes());
            else if(itemId == R.id.itemAsk)  askForClarification();
            else if(itemId == R.id.itemShare) onShare();

            return true;
        });

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.flMain,new CameraFragment());
        ft.commit();
    }

    public void setFragment(BaseFragment fragment){
        if(fragment.getClass().getName().equals(mCurrentFragmentClass)) return;
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        ft.replace(R.id.flMain,fragment);
        ft.addToBackStack(null);
        mBinding.drawerLayout.closeDrawers();
        ft.commit();
    }

    public void updateData(BaseFragment fragment){
        mCurrentFragmentClass = fragment.getClass().getName();
        if(getSupportFragmentManager().getBackStackEntryCount()>0){
            mImageViewBack.setVisibility(View.VISIBLE);
            mImageViewBack.setOnClickListener(view-> getSupportFragmentManager().popBackStack());
        } else{
            mImageViewBack.setVisibility(View.GONE);
        }
        mBackListener = null;
    }

    public void setCustomBackListener(BackListener listener){
        mImageViewBack.setVisibility(View.VISIBLE);
        mImageViewBack.setOnClickListener(view -> mBackListener.onBack());
        mBackListener = listener;
    }

    @Override
    public void onBackPressed() {
        if(mBackListener == null) super.onBackPressed();
        else mBackListener.onBack();
    }

    public interface BackListener{
        void onBack();
    }

    private void onShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Wisdom Over Wars");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.alek.wisdomoverwars");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    private void askForClarification(){
        FirebaseFirestore.getInstance().collection("contacts").document("askClarification")
                .get().addOnCompleteListener(task -> {
                    if(task.isSuccessful() && task.getResult()!=null){
                        DocumentSnapshot document = task.getResult();
                        String email = document.getString("email");
                        String subject = document.getString("subject");

                        Intent selectorIntent = new Intent(Intent.ACTION_SENDTO);
                        selectorIntent.setData(Uri.parse("mailto:"));

                        final Intent emailIntent = new Intent(Intent.ACTION_SEND);
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        emailIntent.setSelector( selectorIntent );

                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    }
                });
    }

    public void setTitle(String text){
        int size = text.length();
        if(size<20) mTextViewTitle.setTextSize(20.0f);
        else if(size<40) mTextViewTitle.setTextSize(17.0f);
        else mTextViewTitle.setTextSize(14.0f);
        mTextViewTitle.setText(text);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)) return true;
        return super.onOptionsItemSelected(item);
    }

    public void checkPermissions() {
        final List<String> permissionsList = new ArrayList<>();

        checkMissingPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE);
        checkMissingPermission(permissionsList, Manifest.permission.CAMERA);

        if (Build.VERSION.SDK_INT >= 23 && permissionsList.size() > 0) {
            requestPermissions(permissionsList.toArray(new String[0]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    private void checkMissingPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS) {
            boolean allGranted = true;
            for (int result : grantResults) {
                if (result != PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }
            if (!allGranted) Toast.makeText(getApplicationContext(),R.string.permission_denied,Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public String getPhotoName() {
        return mPhotoName;
    }

    public void setPhotoName(String photoName) {
        mPhotoName = photoName;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }
}