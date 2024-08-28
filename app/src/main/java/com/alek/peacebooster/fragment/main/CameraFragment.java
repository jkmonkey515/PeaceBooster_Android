package com.alek.peacebooster.fragment.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.R;
import com.alek.peacebooster.activity.MainActivity;
import com.alek.peacebooster.fragment.steps.Step1Fragment;
import com.alek.peacebooster.databinding.FragmentCameraBinding;
import com.alek.peacebooster.fragment.BaseFragment;

import java.io.IOException;


public class CameraFragment extends BaseFragment {

    Dialog camera_dialog;
    TextView txt_gallery, txt_take_photo;
    Button btn_cancel;

    Dialog name_dialog;
    EditText etName;
    Button btn_save;

    FragmentCameraBinding mBinding;

    private final ActivityResultLauncher<Intent> mResultLauncherCamera = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        setMyImage(imageBitmap);
                    }
                }
            });

    private final ActivityResultLauncher<Intent> mResultLauncherGallery = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), data.getData());
                            setMyImage(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentCameraBinding.inflate(inflater, container, false);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mBinding.showOfficial.setOnClickListener(view -> setNextFragment(new OfficialRulesFragment()));

        mBinding.btnTakePhoto.setOnClickListener(view -> setVerifiedDialog());
        mBinding.btnTakePhoto.setOnClickListener(view -> setVerifiedDialog());

        mBinding.btnStart.setOnClickListener(view -> {
            String name = getPhotoName();
            if (!TextUtils.isEmpty(name)) {
                setNextFragment(new Step1Fragment());
            } else {
                AskNameDialog();
            }
        });
        setTitle(R.string.app_name);
        return mBinding.getRoot();
    }

    private void setVerifiedDialog() {

        camera_dialog = new Dialog(requireContext());
        camera_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        camera_dialog.setContentView(R.layout.dialog_camera);
        camera_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        camera_dialog.show();
        camera_dialog.setCancelable(false);

        txt_gallery = camera_dialog.findViewById(R.id.txt_gallery);
        txt_take_photo = camera_dialog.findViewById(R.id.txt_take_photo);
        btn_cancel = camera_dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(view -> camera_dialog.dismiss());

        txt_gallery.setOnClickListener(view -> {
            camera_dialog.dismiss();
            galleryIntent();
        });
        txt_take_photo.setOnClickListener(view -> {
            if (Build.VERSION.SDK_INT >= 23) {
                if (requireContext().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ((MainActivity)requireActivity()).checkPermissions();
                    return;
                }
            }
            camera_dialog.dismiss();
            cameraIntent();
        });
    }

    private void setMyImage(Bitmap bitmap) {
        mBinding.lyImageSet.setVisibility(View.VISIBLE);
        mBinding.lyTakePhoto.setVisibility(View.GONE);
        mBinding.btnTakePhoto.setVisibility(View.GONE);
        mBinding.imvBuildingImg.setVisibility(View.VISIBLE);
        mBinding.imvBuildingImg.setImageBitmap(bitmap);
        setBitmap(bitmap);

        AskNameDialog();
    }

    private void AskNameDialog() {
        name_dialog = new Dialog(requireContext());
        name_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        name_dialog.setContentView(R.layout.dialog_ask_name);
        name_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        name_dialog.show();
        name_dialog.setCancelable(false);


        etName = name_dialog.findViewById(R.id.edtimagename);
        etName.setFocusable(true);
        btn_save = name_dialog.findViewById(R.id.btn_save);

        btn_save.setOnClickListener(view -> {
            String photoName = etName.getText().toString();
            if (!TextUtils.isEmpty(photoName)) {
                setPhotoName(photoName);
                name_dialog.dismiss();
            } else {
                showToast(R.string.enter_photo_name);
            }
        });
    }

    private void cameraIntent() {
        mResultLauncherCamera.launch(new Intent(MediaStore.ACTION_IMAGE_CAPTURE));
    }

    private void galleryIntent() {
        mResultLauncherGallery.launch(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI));
    }


}
