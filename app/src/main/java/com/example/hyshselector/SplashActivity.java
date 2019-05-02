package com.example.hyshselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import com.example.hyshselector.utils.Constants;

import java.io.File;

public class SplashActivity extends AppCompatActivity {

    private Context context;
    private Constants constants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        createFolderForApp();


        context = this;
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    private void createFolderForApp() {

        File f = new File(Environment.getExternalStorageDirectory(), "HyshSelections");
        if (!f.exists()) {
            f.mkdirs();
        }

    }
}
