package com.example.hyshselector;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyshselector.adapters.AdapterPhotos;
import com.example.hyshselector.entities.PhotoHysh;
import com.example.hyshselector.fragments.ViewImageExtended;
import com.example.hyshselector.utils.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.recycler_pictures)
    RecyclerView recyclerPictures;
    @BindView(R.id.text_directories)
    TextView textDirectories;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_selected_pics)
    TextView textSelectedPics;

    private Context context;
    private List<PhotoHysh> listString;
    private AdapterPhotos adapterPhotos;
    private Constants constants;
    private ViewImageExtended viewImageExtended;
    private PhotoHysh pic;
    private int totalSelected;
    private int extraPhotos;
    private int amount;
    private String sessionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settingToolbar();

        Intent intent = getIntent();
        sessionName = intent.getStringExtra("session_name");

        context = this;
        listString = new ArrayList<>();

        settingRecycler();
        gettingFiles();
        navigationViewSelector();

    }

    private void navigationViewSelector() {

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {


                switch (menuItem.getItemId()) {
                    case R.id.nav_all_photos:
                        Toast.makeText(context, "all pics", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_checked:
                        Toast.makeText(context, "only selected", Toast.LENGTH_SHORT).show();
                        break;


                }
                return true;
            }
        });

    }

    private void settingToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void gettingFiles() {


        String path = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/Thumbnails/" + sessionName;
        //String path = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        File directory = new File(path);
        File[] files = directory.listFiles();



        try {
            for (int i = 0; i < files.length; i++) {


                if (files[i].getName().contains(".jpg")) {
                    PhotoHysh photoHysh = new PhotoHysh();
                    photoHysh.setName(files[i].getName());
                    photoHysh.setId(i);
                    listString.add(photoHysh);
                    stringBuilder.append(files[i].getName() + " ");
                }

            }

        } catch (Exception e) {

        }
        listString.size();
        textDirectories.setText(stringBuilder.toString());
    }

    private void settingRecycler() {
        adapterPhotos = new AdapterPhotos(context, listString,sessionName, new AdapterPhotos.ClickInImage() {
            @Override
            public void clickOnPicture(PhotoHysh photoHysh, int position, Bitmap bitmap) {


                if (viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()) {


                    pic = listString.get(position);
                    FragmentManager fm = getSupportFragmentManager();

                    Bundle arguments = new Bundle();


                    // Aqui le pasas el bitmap de la imagen
                    arguments.putParcelable("bitmap", bitmap);
                    arguments.putParcelable("info", pic);
                    viewImageExtended = ViewImageExtended.newInstance(arguments);
                    viewImageExtended.show(fm, "ViewImageExtended");

                }

            }

            @Override
            public void longClickOnPicture(PhotoHysh photoHysh, int position) {

                pic = listString.get(position);
                if (pic.isSelected()) {
                    pic.setSelected(false);
                } else {
                    pic.setSelected(true);
                }

                updatingTotal();
                adapterPhotos.notifyDataSetChanged();

            }
        });
        recyclerPictures.setAdapter(adapterPhotos);


    }

    private void updatingTotal() {
        extraPhotos = 0;
        totalSelected = 0;
        amount = 0;
        String message = " ";
        for (int i = 0; i < listString.size(); i++) {
            if (listString.get(i).isSelected()) {
                totalSelected++;

            }
        }


        if (totalSelected <= 4) {
            message = String.valueOf(totalSelected);
        }

        //TODO poner bien lo de los precios
        if (totalSelected >= 5 && totalSelected <= 9) {
            message = totalSelected + " " + "Pack de 5 = 60 €";
        } else if (totalSelected >= 10 && totalSelected <= 19) {
            message = totalSelected + " " + "Pack de 10 = 90 €";
        } else if (totalSelected == 20) {
            message = totalSelected + " " + "Pack de 20 = 150 €";
        } else {
            extraPhotos = totalSelected - 20;
            amount = extraPhotos * 6;
            message = totalSelected + " " + "Pack de 20 + " + extraPhotos + " fotos extra = " + amount;
        }


        textSelectedPics.setText(message);


    }


}
