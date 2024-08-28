package com.alek.peacebooster.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.alek.peacebooster.R;

public class SplashActivity extends AppCompatActivity {

    static final int SPLASH_DELAY = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(this::next, SPLASH_DELAY);
    }

    private void next() {
        Intent intent = new Intent(SplashActivity.this, LanguagesActivity.class);
        startActivity(intent);
        finish();
    }
}
