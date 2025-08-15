package com.example.football_field_management;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.football_field_management.activity.BookingManagementActivity;
import com.example.football_field_management.activity.LoginActivity;
import com.google.android.material.textview.MaterialTextView;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        ImageView imvLogo = findViewById(R.id.imvLogo);
        MaterialTextView tvTitle = findViewById(R.id.tvTitle);

        // Load animation
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_anim);
        Animation textAnim = AnimationUtils.loadAnimation(this, R.anim.text_anim);

        // Start animation
        imvLogo.startAnimation(logoAnim);
        tvTitle.startAnimation(textAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
            }
        }, 3000);
    }
}
