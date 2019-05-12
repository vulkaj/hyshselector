package com.example.hyshselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import static com.example.hyshselector.utils.Constants.BASE_DIRECTORY;
import static com.example.hyshselector.utils.Constants.SPLASH_DELAY;

public class SplashActivity extends AppCompatActivity {

    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = this;
        createFolderForApp();


    }

    private void launchLogin() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(context, SessionSelector.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DELAY);
    }

    private void createFolderForApp() {
        File f = new File(Environment.getExternalStorageDirectory(), BASE_DIRECTORY);
        if (!f.exists()) {
            f.mkdirs();
        }

        launchLogin();

    }
}
