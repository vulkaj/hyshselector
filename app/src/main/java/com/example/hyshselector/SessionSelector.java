package com.example.hyshselector;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.hyshselector.adapters.AdapterSessions;
import com.example.hyshselector.adapters.TutorialActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hyshselector.utils.Constants.BASE_DIRECTORY;

public class SessionSelector extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.imageToTutorial)
    ImageView imageToTutorial;

    private Context context;
    private String path;
    private File directory;
    private File[] arrayDirectories;
    private List<String> listString;
    private AdapterSessions adapterSessions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_selector);
        ButterKnife.bind(this);

        context = this;

        gettingListOfSessions();
        settingAdapter();
        listeners();
    }

    private void listeners() {
        imageToTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TutorialActivity.class);
                startActivity(intent);
            }
        });
    }


    private void gettingListOfSessions() {

        listString = new ArrayList<>();
        path = Environment.getExternalStorageDirectory().toString() + "/" + BASE_DIRECTORY;
        directory = new File(path);
        arrayDirectories = directory.listFiles();

        try {
            for (int i = 0; i < arrayDirectories.length; i++) {

                if (!arrayDirectories[i].getName().contains("Thumbnails")) {
                    String s = arrayDirectories[i].getName();
                    listString.add(s);
                }

            }

        } catch (Exception e) {
        }


    }


    private void settingAdapter() {
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        adapterSessions = new AdapterSessions(context, listString);
        recycler.setAdapter(adapterSessions);

    }
}
