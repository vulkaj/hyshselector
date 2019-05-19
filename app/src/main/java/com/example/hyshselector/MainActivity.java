package com.example.hyshselector;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyshselector.adapters.AdapterPhotos;
import com.example.hyshselector.entities.PhotoHysh;
import com.example.hyshselector.fragments.ViewImageExtended;
import com.example.hyshselector.utils.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.hyshselector.utils.Constants.BASE_DIRECTORY;
import static com.example.hyshselector.utils.Constants.PRICE_10;
import static com.example.hyshselector.utils.Constants.PRICE_20;
import static com.example.hyshselector.utils.Constants.PRICE_5;
import static com.example.hyshselector.utils.Constants.SESSION_NAME;
import static com.example.hyshselector.utils.Constants.TAG_BITMAP;
import static com.example.hyshselector.utils.Constants.TAG_INFO;
import static com.example.hyshselector.utils.Constants.THUMBNAILS_DIRECTORY;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.recycler_pictures)
    RecyclerView recyclerPictures;
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
    private List<PhotoHysh> listOriginal;
    private AdapterPhotos adapterPhotos;
    private Constants constants;
    private ViewImageExtended viewImageExtended;
    private PhotoHysh pic;
    private int totalSelected;
    private int extraPhotos;
    private int amount;
    private String sessionName;
    private String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        settingToolbar();


        Intent intent = getIntent();
        sessionName = intent.getStringExtra(SESSION_NAME);

        context = this;
        listString = new ArrayList<>();
        listOriginal = new ArrayList<>();

        //TODO poner una barra de progreso para cuando esté creando las thumbnails
        /*
        progressBar = new ProgressBar(youractivity.this,null,android.R.attr.progressBarStyleLarge);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(100,100);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        layout.addView(progressBar,params);
        progressBar.setVisibility(View.VISIBLE);  //To show ProgressBar
        */
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

                        listString.clear();
                        listString.addAll(listOriginal);
                        adapterPhotos.notifyDataSetChanged();
                        drawerLayout.closeDrawer(Gravity.LEFT);

                        break;
                    case R.id.nav_checked:
                        returnOnlySelected();
                        drawerLayout.closeDrawer(Gravity.LEFT);

                        break;

                }
                return true;
            }
        });

    }

    private void returnOnlySelected() {
        listString.clear();
        for (int i = 0; i < listOriginal.size(); i++) {
            if (listOriginal.get(i).isSelected()) {
                PhotoHysh photoHysh = new PhotoHysh();
                photoHysh.setName(listOriginal.get(i).getName());
                photoHysh.setId(listOriginal.get(i).getId());
                listString.add(photoHysh);

            }
        }

        adapterPhotos.notifyDataSetChanged();

    }

    private void settingToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishSelection();
            }
        });

    }

    private void finishSelection() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set title
        alertDialogBuilder.setTitle("TERMINAR SELECCIÓN");


        // set dialog message
        alertDialogBuilder
                .setMessage("¿Lo tienes claro ya? ¿Seguro que has terminado la selección?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        copyRawFilesToDirectory();
                        Intent intent = new Intent(context, ResumeOfSelection.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void copyRawFilesToDirectory() {
        String pathBase = Environment.getExternalStorageDirectory().toString() + "/" + BASE_DIRECTORY;
        String pathOrigin = pathBase + "/" + sessionName;
        String pathDirectorySelection = pathOrigin + "/selection";

        File f = new File(pathOrigin);
        File newPath = new File(pathDirectorySelection);
/* TEST WORKS PERFECTLY!!
        try {
            File fileOrigin = new File(pathOrigin + "/_F2G6096.CR2");
            File fileDestiny = new File(pathDirectorySelection + "/_F2G6096.CR2");


            copyFiles(fileOrigin, fileDestiny); //TODO falta el "is Selected"


        } catch (Exception e) {

        }
*/


        if (!newPath.exists()) {
            newPath.mkdirs();
        }
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {

            String j = files[i].getName();

            if (j.contains(".CR2")) {
                File fileOrigin = new File(pathOrigin + "/" + j);
                File fileDestiny = new File(pathDirectorySelection + "/" + j);

                //File fileOrigin = new File(pathOrigin + s);
                //File fileDestiny = new File(pathDirectorySelection + s);

                try {
                    copyFiles(fileOrigin, fileDestiny); //TODO falta el "is Selected"
                } catch (Exception y) {
                    System.out.println("BOQUEPACHA");
                    y.printStackTrace();
                }

            }
        }


    }

    public static void copyFiles(File src, File dst) throws IOException {
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(dst)) {
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
            }
        }
    }


    private void gettingFiles() {


        path = Environment.getExternalStorageDirectory().toString() + "/" + BASE_DIRECTORY + "/" + THUMBNAILS_DIRECTORY + "/" + sessionName;
        File directory = new File(path);
        File[] files = directory.listFiles();
        listString.clear();
        listOriginal.clear();


        try {
            for (int i = 0; i < files.length; i++) {
                if (files[i].getName().contains(".jpg")) {
                    PhotoHysh photoHysh = new PhotoHysh();
                    photoHysh.setName(files[i].getName());
                    photoHysh.setId(i);
                    listString.add(photoHysh);

                }

            }

        } catch (Exception e) {

        }
        listOriginal.addAll(listString);
        listString.size();

    }

    private void settingRecycler() {

        adapterPhotos = new AdapterPhotos(context, listString, sessionName, new AdapterPhotos.ClickInImage() {
            @Override
            public void clickOnPicture(PhotoHysh photoHysh, int position, Bitmap bitmap) {


                if (viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()) {


                    pic = listString.get(position);
                    FragmentManager fm = getSupportFragmentManager();

                    Bundle arguments = new Bundle();


                    // Aqui le pasas el bitmap de la imagen
                    arguments.putParcelable(TAG_BITMAP, bitmap);
                    arguments.putParcelable(TAG_INFO, pic);
                    arguments.putInt("position", position);
                    arguments.putString("session_name", sessionName);

                    ArrayList<PhotoHysh> passingArrayList = new ArrayList<>();
                    passingArrayList.addAll(listString);

                    arguments.putParcelableArrayList("list_images", passingArrayList);
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
                adapterPhotos.notifyItemChanged(position);

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


        //TODO poner bien lo de los precios
        if (totalSelected >= 5 && totalSelected <= 9) {
            extraPhotos = totalSelected - 5;
            amount = extraPhotos * 6;
            amount = amount + 60; //TODO make Constants with the price of each session
            message = totalSelected + " " + PRICE_5;
        } else if (totalSelected >= 10 && totalSelected <= 19) {
            extraPhotos = totalSelected - 10;
            amount = extraPhotos * 6;
            amount = amount + 90;
            message = totalSelected + " " + PRICE_10;
        } else if (totalSelected == 20) {
            message = totalSelected + " " + PRICE_20;
        } else {
            extraPhotos = totalSelected - 20;
            amount = extraPhotos * 6;
            amount = amount + 120;
            message = totalSelected + " " + "Pack de 20 + " + extraPhotos + " fotos extra = " + amount;
        }

        if (totalSelected <= 4) {
            message = String.valueOf(totalSelected);
        }


        textSelectedPics.setText(message);


    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        Toast toast = Toast.makeText(context, String.valueOf(viewImageExtended.getPosition()), Toast.LENGTH_LONG);
        toast.show();

        listString.get(viewImageExtended.getPosition()).setSelected(viewImageExtended.isImageSelected());
        listString.clear();
        listString.addAll(viewImageExtended.getListImages());
        adapterPhotos.notifyDataSetChanged();

        updatingTotal();
    }
}
