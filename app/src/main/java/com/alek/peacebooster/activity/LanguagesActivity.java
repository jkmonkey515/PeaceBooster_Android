package com.alek.peacebooster.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.alek.peacebooster.databinding.ActivityLanguagesBinding;


import java.util.Locale;

public class LanguagesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLanguagesBinding binding = ActivityLanguagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnEnglish.setOnClickListener((view)-> setLocale("en"));
        binding.btnHebrew.setOnClickListener((view)-> setLocale("iw"));
        binding.btnArabic.setOnClickListener((view)-> setLocale("ar"));
        binding.btnChinese.setOnClickListener((view)-> setLocale("zh"));

    }


    private void setLocale(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        Intent intent = new Intent(LanguagesActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}