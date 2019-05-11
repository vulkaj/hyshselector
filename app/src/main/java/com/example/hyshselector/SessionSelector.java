package com.example.hyshselector;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.example.hyshselector.adapters.AdapterSessions;
import com.example.hyshselector.entities.PhotoHysh;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionSelector extends AppCompatActivity {

    @BindView(R.id.recycler)
    RecyclerView recycler;
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
        listString = new ArrayList<>();

        path = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections";

        directory = new File(path);
        arrayDirectories = directory.listFiles();

        try {
            for (int i = 0; i < arrayDirectories.length; i++) {

                if (!arrayDirectories[i].getName().contains(".CR2")) {
                    String s = arrayDirectories[i].getName();
                    listString.add(s);
                }

            }

        } catch (Exception e) {
        }


        context = this;


        settingAdapter();
    }

    private void settingAdapter() {

        adapterSessions = new AdapterSessions(context, listString);
        recycler.setAdapter(adapterSessions);

    }
}
