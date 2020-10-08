package com.example.mylaundry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.mylaundry.ui.login.LoginActivity;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EasySplashScreen splash = new EasySplashScreen(SplashScreenActivity.this)
                .withSplashTimeOut(5000)
                .withBackgroundColor(Color.WHITE)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withLogo(R.drawable.launch);

                View splashView = splash.create();
                setContentView(splashView);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ((Activity) SplashScreenActivity.this).getWindow().setStatusBarColor(ContextCompat.getColor(SplashScreenActivity.this, R.color.colorAccent));
        }
    }
}