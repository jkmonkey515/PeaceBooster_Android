package com.alek.peacebooster.fragment.steps;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.adapter.ImagePartAdapter;
import com.alek.peacebooster.databinding.FragmentImageSquaresBinding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.model.ImagePart;

import java.util.ArrayList;

public class ImageSquaresFragment extends BaseFragment {

    String photoName;
    ArrayList<ImagePart> imgPartsArr = new ArrayList<>();

    int imgViewWidth, imgViewHeight;

    Dialog scannerDialog;
    ObjectAnimator animator;

    CountDownTimer delayTimer;
    String[] step1ThirdQuestions;
    int questionIndex = 0;

    private FragmentImageSquaresBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentImageSquaresBinding.inflate(inflater, container, false);

        setTitle(R.string.step1);
        loadInitData();

        return mBinding.getRoot();
    }

    private void loadInitData() {

        photoName = getPhotoName();

        String titleStr = "1: " + getResources().getString(R.string.step1_third_question_title);
        if (titleStr.contains("(--)")) {
            titleStr = titleStr.replace("(--)", photoName);
        }
        setTitle(titleStr);

        step1ThirdQuestions = getResources().getStringArray(R.array.step1_third_questions);
        String question = step1ThirdQuestions[questionIndex];
        if (question.contains("(--)")) {
            question = question.replace("(--)", photoName);
        }
        mBinding.txvQuestion.setText(question);

        mBinding.imvEntity.setVisibility(View.VISIBLE);
        mBinding.myGridView.setVisibility(View.GONE);

        mBinding.imvEntity.setImageBitmap(getBitmap());

        showScannerView();

        mBinding.btnYes.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0: // Do you see that (NAME) is made of parts?
                case 2: // The analysis found (NAME) to be made of parts that you seem unable to see. ...
                case 1: // Do you see these red squares indicating parts of (NAME)?
                case 3: // I found that (NAME) is made of parts and because what is made of parts ...
                case 5: // The fact that (NAME) is made of many parts, but you say (NAME) is a single entity; ...
                    showCongAlert();
                    break;
                case 4: // If (NAME) is made of different parts, can (she/he) be counted as a singular entity?
                    showIncorrectAlert();
                    break;
            }
        });
        mBinding.btnNo.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0:// Do you see that (NAME) is made of parts?
                case 1:// Do you see these red squares indicating parts of (NAME)?
                case 2:// The analysis found (NAME) to be made of parts that you seem unable to see. ...
                case 3:// the analysis found that (NAME) is made of parts and because what is made of parts ...
                case 5:// The fact that (NAME) is made of many parts, but you say (NAME) is a single entity; ...
                    showIncorrectAlert();
                    break;
                case 4:// If (NAME) is made of different parts, can (she/he) be counted as a singular entity?
                    showCongAlert();
                    break;
            }
        });

        delayTimer = new CountDownTimer(2800, 1000) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                if (delayTimer != null) {
                    delayTimer.cancel();
                    delayTimer = null;
                }
                scannerDialog.dismiss();

                splitImage(getBitmap());
                displayImageGrid();
            }

        }.start();
    }

    private void showIncorrectAlert() {

        String contentStr = getResources().getString(R.string.step1_incorrect_alert);
        if (questionIndex == 3) {
            contentStr = getResources().getString(R.string.step1_incorrect_alert2);
        }
        if (questionIndex == 4) {
            contentStr = getResources().getString(R.string.step1_incorrect_alert2);
        }
        if (questionIndex == 5) {
            contentStr = getResources().getString(R.string.step1_incorrect_alert3);
            questionIndex = -1;
        }

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_notice));
        contentView.setText(contentStr);
        alertButton.setText(getResources().getString(R.string.button_next));

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();

            changeQuestion();
        });
    }

    private void changeQuestion() {
        questionIndex += 1;

        if (questionIndex >= 2) {
            mBinding.myGridView.setVisibility(View.GONE);

            mBinding.imvEntity.setVisibility(View.VISIBLE);
            mBinding.lyVerticalLine.setVisibility(View.VISIBLE);
        } else {
            mBinding.myGridView.setVisibility(View.VISIBLE);

            mBinding.imvEntity.setVisibility(View.GONE);
            mBinding.lyVerticalLine.setVisibility(View.GONE);
        }

        String question = step1ThirdQuestions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", getPhotoName());
        }
        mBinding.txvQuestion.setText(question);
        mBinding.txvQuestion.setFocusable(true);
    }

    private void showCongAlert() {

        String cont = getResources().getString(R.string.complete_step1);
        String temp = cont.replace("(--)", photoName);

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_congratulation));
        contentView.setText(temp);
        alertButton.setText(R.string.go_to_point2);

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();
            setNextFragment(new Step2Fragment());
        });
    }

    private void splitImage(Bitmap bitmap) {

        imgViewWidth = mBinding.imvEntity.getWidth();
        imgViewHeight = mBinding.imvEntity.getHeight();

        imgPartsArr.clear();
        int rows = 8, cols = 6;
        //For height and width of the small image chunks
        int chunkHeight, chunkWidth;

        try {
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (imgViewWidth - 10 * (cols - 1)),
                    (imgViewHeight - 10 * (rows - 1)), true);

            chunkWidth = (imgViewWidth - 10 * (cols - 1)) / cols;
            chunkHeight = (imgViewHeight - 10 * (rows - 1)) / rows;

            //x0 and y0 are the pixel positions of the image chunks
            int y0 = 0;
            for (int x = 0; x < rows; x++) {
                int x0 = 0;
                for (int y = 0; y < cols; y++) {
                    Bitmap bmp = Bitmap.createBitmap(scaledBitmap, x0, y0, chunkWidth, chunkHeight);
                    ImagePart imagePart = new ImagePart(bmp, false);
                    imgPartsArr.add(imagePart);
                    x0 += chunkWidth;
                }
                y0 += chunkHeight;
            }

            // make default squares
            imgPartsArr.get(5).setStatus(true);
            imgPartsArr.get(7).setStatus(true);
            imgPartsArr.get(20).setStatus(true);
            imgPartsArr.get(28).setStatus(true);
            imgPartsArr.get(42).setStatus(true);
        } catch (Exception e) {
            Log.d("Error", e.getMessage());
        }
    }

    private void showScannerView() {
        scannerDialog = new Dialog(requireContext());
        scannerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scannerDialog.setContentView(R.layout.dialog_scanner);
        scannerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        scannerDialog.show();

        LinearLayout scannerLayout = scannerDialog.findViewById(R.id.scannerLayout);
        View scannerBard = scannerDialog.findViewById(R.id.scannerBarView);
        ImageView scanImageView = scannerDialog.findViewById(R.id.imvSource);
        scanImageView.setImageBitmap(getBitmap());

        ViewTreeObserver vto = scannerLayout.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scannerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                float destination = (scannerLayout.getY() + scannerLayout.getHeight());
                animator = ObjectAnimator.ofFloat(scannerBard, "translationY", scannerLayout.getY(), destination);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(1000);
                animator.start();
            }
        });
    }

    private void displayImageGrid() {
        mBinding.imvEntity.setVisibility(View.GONE);
        mBinding.lyVerticalLine.setVisibility(View.GONE);
        mBinding.myGridView.setVisibility(View.VISIBLE);

        mBinding.myGridView.setNumColumns((int) Math.sqrt(imgPartsArr.size()));
        mBinding.myGridView.setAdapter(new ImagePartAdapter(ImageSquaresFragment.this, imgPartsArr));


        clickImageAlert();
    }

    public void showSelectedImage(Bitmap bitmap) {

        Dialog displayImgDialog = new Dialog(requireContext());
        displayImgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        displayImgDialog.setContentView(R.layout.dialog_show_image_part);
        displayImgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        displayImgDialog.show();

        ImageView splitImageView = displayImgDialog.findViewById(R.id.imv_split);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, imgViewWidth, imgViewHeight, true);
        splitImageView.setImageBitmap(scaledBitmap);

        splitImageView.setOnClickListener(v -> displayImgDialog.dismiss());
    }

    private void clickImageAlert() {

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_notice));

        String cont = getResources().getString(R.string.click_photo);
        String temp = cont.replace("(--)", photoName);

        contentView.setText(temp);
        alertButton.setText(getResources().getString(R.string.button_ok));

        alertButton.setOnClickListener(view -> exitDialog.dismiss());
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(() -> {
            if (questionIndex == 0) {
                close();
            } else {
                questionIndex -= 2;
                changeQuestion();
            }
        });

    }
}
