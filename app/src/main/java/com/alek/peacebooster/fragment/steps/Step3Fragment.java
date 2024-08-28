package com.alek.peacebooster.fragment.steps;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alek.peacebooster.databinding.FragmentStep3Binding;
import com.alek.peacebooster.fragment.BaseFragment;
import com.alek.peacebooster.fragment.summary.Summary1Fragment;
import com.alek.peacebooster.R;
import com.alek.peacebooster.activity.LanguagesActivity;

public class Step3Fragment extends BaseFragment {

    String[] step3Questions;
    int questionIndex = 0;

    String imageName;
    String userName;

    private FragmentStep3Binding mBinding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentStep3Binding.inflate(inflater,container,false);

        setTitle(R.string.step3);
        loadInitData();


        return mBinding.getRoot();
    }

    private void loadInitData() {

        imageName = getPhotoName();

        String titleStr = "3: " + getResources().getString(R.string.step3_title);
        if (titleStr.contains("(--)")) {
            titleStr = titleStr.replace("(--)", imageName);
        }
       setTitle(titleStr);

        step3Questions = getResources().getStringArray(R.array.step3_questions);
        String question = step3Questions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }
        mBinding.txvQuestion.setText(question);

        mBinding.imvQuestion.setImageBitmap(getBitmap());

        AskUserNameDialog();

        if (questionIndex == 6) {
            setLastQuery();
        } else {
            mBinding.lyLastQuestion.setVisibility(View.GONE);
            mBinding.lyButtons.setVisibility(View.VISIBLE);
        }

        mBinding.btnYes.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0: // Can (name) exist as (name) without being named (name)?
                case 2: // If you did not name yourself (user name), would people know who you are, or what to call you?
                case 3: // If you did not name your photo (name), would it exist conventionally as (name)?
                case 4: //Does it make sense to name (name) ("a name"), if he/she was already (name)?
                    showIncorrectAlert();
                    break;
                case 1: // Can (name) exist as (name) without being named (name)?
                case 5: // Only what is without a name is given a name for the sake of convenience. This way, we can function easily as families and societies.
                    showCongAlert();
                    break;
            }
        });
        mBinding.btnNo.setOnClickListener(view -> {
            switch (questionIndex) {
                case 0:
                case 2:
                case 3:
                case 4:
                    showCongAlert();
                    break;
                case 1:
                case 5:
                    showIncorrectAlert();
                    break;
            }
        });

        mBinding.tvLinkStep1.setOnClickListener(view -> setNextFragment(new ImageSquaresFragment()));
        mBinding.tvLinkStep2.setOnClickListener(view -> setNextFragment(new Step2Fragment()));
        mBinding.tvLinkStep3.setOnClickListener(view -> {
            questionIndex = -1;
            changeQuestion();
        });
        mBinding.tvLinkStartOver.setOnClickListener(view -> {
            final Intent intent = new Intent(requireContext(), LanguagesActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    private void showIncorrectAlert() {

        String contentStr = getResources().getString(R.string.step3_incorrect_alert);

        Dialog exitDialog = new Dialog(requireContext());
        exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exitDialog.setContentView(R.layout.dialog_alert);
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        exitDialog.show();
        exitDialog.setCancelable(false);

        TextView titleView = exitDialog.findViewById(R.id.alert_title);
        TextView contentView = exitDialog.findViewById(R.id.alert_content);
        Button alertButton = exitDialog.findViewById(R.id.alert_button);

        titleView.setText(getResources().getString(R.string.title_incorrect));
        contentView.setText(contentStr);
        alertButton.setText(getResources().getString(R.string.button_next));

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();
            changeQuestion();
        });
    }

    private void changeQuestion() {
        questionIndex += 1;

        String question = step3Questions[questionIndex];

        if (question.contains("(--)")) {
            question = question.replace("(--)", imageName);
        }

        if (question.contains("(==)")) {
            question = question.replace("(==)", userName);
        }

        mBinding.txvQuestion.setText(question);

        if (questionIndex == 6) {
            setLastQuery();
        } else {
            mBinding.lyLastQuestion.setVisibility(View.GONE);
            mBinding.lyButtons.setVisibility(View.VISIBLE);
        }
    }

    private void showCongAlert() {

        String cont = getResources().getString(R.string.complete_step3);
        String temp = cont.replace("(--)", imageName);
        temp = temp.replace("(==)", userName);

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
        alertButton.setText(getResources().getString(R.string.go_to_conclusion));

        alertButton.setOnClickListener(view -> {
            exitDialog.dismiss();
            gotoConclusion();
        });
    }

    private void AskUserNameDialog() {
        Dialog name_dialog = new Dialog(requireContext());
        name_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        name_dialog.setContentView(R.layout.dialog_ask_name);
        name_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        name_dialog.show();
        name_dialog.setCancelable(false);

        TextView titleView = name_dialog.findViewById(R.id.name_title);
        EditText etName = name_dialog.findViewById(R.id.edtimagename);
        Button btn_save = name_dialog.findViewById(R.id.btn_save);

        titleView.setText(getResources().getString(R.string.title_username));
        etName.setHint(getResources().getString(R.string.enter_username));
        btn_save.setText(getResources().getString(R.string.buton_save));

        btn_save.setOnClickListener(view -> {

            String username = etName.getText().toString();

            setUserName(username);
            userName = username;
            name_dialog.dismiss();
        });
    }

    private void gotoConclusion() {
        setNextFragment(new Summary1Fragment());
    }

    @Override
    public void onResume() {
        super.onResume();
        setCustomBackListener(()->{
            if (questionIndex == 0) {
                close();
            } else {
                questionIndex -= 2;
                changeQuestion();
            }
        });
    }

    private void setLastQuery() {
        mBinding.lyLastQuestion.setVisibility(View.VISIBLE);

        mBinding.lyButtons.setVisibility(View.GONE);

        String strLinkStep3 = "<span><u>" + mBinding.tvLinkStep3.getText() + "</u></span>";
        mBinding.tvLinkStep3.setText(Html.fromHtml(strLinkStep3));

        String strLinkStep1 = "<span><u>" + mBinding.tvLinkStep1.getText() + "</u></span>";
        mBinding.tvLinkStep1.setText(Html.fromHtml(strLinkStep1));

        String strLinkStep2 = "<span><u>" + mBinding.tvLinkStep2.getText() + "</u></span>";
        mBinding.tvLinkStep2.setText(Html.fromHtml(strLinkStep2));

        String strLinkStartOver = "<span><u>" + mBinding.tvLinkStartOver.getText() + "</u></span>";
        mBinding.tvLinkStartOver.setText(Html.fromHtml(strLinkStartOver));
    }
}
