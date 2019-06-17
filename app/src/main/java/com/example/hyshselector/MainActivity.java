package com.example.hyshselector;

import android.app.AlertDialog;
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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyshselector.adapters.AdapterPhotos;
import com.example.hyshselector.entities.PhotoHysh;
import com.example.hyshselector.fragments.ViewImageExtended;
import com.example.hyshselector.utils.AlertsHysh;
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

import static com.example.hyshselector.utils.Constants.AMOUNT_EXTRA_PHOTO;
import static com.example.hyshselector.utils.Constants.AMOUNT_FIVE;
import static com.example.hyshselector.utils.Constants.AMOUNT_TEN;
import static com.example.hyshselector.utils.Constants.AMOUNT_TWENTY;
import static com.example.hyshselector.utils.Constants.BASE_DIRECTORY;
import static com.example.hyshselector.utils.Constants.LIST_IMAGES;
import static com.example.hyshselector.utils.Constants.PACK_10;
import static com.example.hyshselector.utils.Constants.PACK_20;
import static com.example.hyshselector.utils.Constants.PACK_5;
import static com.example.hyshselector.utils.Constants.POSITION;
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
    private ViewImageExtended viewImageExtended;
    private PhotoHysh pic;
    private int totalSelected;
    private int extraPhotos;
    private int amount;
    private String sessionName;
    private String path;
    private int pack;
    private AlertsHysh alertsHysh;


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

        gettingFiles();
        settingRecycler();
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
                if (pack == 1 || pack == 2 || pack == 3) {
                    finishSelection();
                } else {
                    Toast.makeText(context, getString(R.string.up_to_five), Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void finishSelection() {

        alertsHysh = new AlertsHysh(context, getString(R.string.finish_selection), getString(R.string.r_u_sure), new AlertsHysh.SettingInterface() {
            @Override
            public void dothings() {
                copyRawFilesToDirectory();
                Intent intent = new Intent(context, ResumeOfSelection.class);
                intent.putExtra(SESSION_NAME, sessionName);
                intent.putExtra("total_selected", totalSelected);
                intent.putExtra("amount", amount);
                intent.putExtra("pack", pack);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });


    }

    private void copyRawFilesToDirectory() {
        String pathBase = Environment.getExternalStorageDirectory().toString() + "/" + BASE_DIRECTORY;
        String pathOrigin = pathBase + "/" + sessionName;
        String pathDirectorySelection = pathOrigin + "/selection";

        File f = new File(pathOrigin);
        File newPath = new File(pathDirectorySelection);


        if (!newPath.exists()) {
            newPath.mkdirs();
        }
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {

            String j = files[i].getName();
            String getFileName = files[i].getName();
            getFileName = getFileName.substring(0, getFileName.length() - 4);
            if (j.contains(".CR2")) {
                for (int r = 0; r < listString.size(); r++) {
                    String getName = listString.get(r).getName();
                    getName = getName.substring(0, getName.length() - 4);

                    if (listString.get(r).isSelected()) {


                        if (getFileName.equals(getName)) {

                            File fileOrigin = new File(pathOrigin + "/" + j);
                            File fileDestiny = new File(pathDirectorySelection + "/" + j);


                            try {
                                copyFiles(fileOrigin, fileDestiny);
                            } catch (Exception y) {

                                y.printStackTrace();
                            }

                        }

                    }

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

        recyclerPictures.setItemViewCacheSize(listOriginal.size());
        recyclerPictures.setHasFixedSize(true);
        adapterPhotos = new AdapterPhotos(context, listString, sessionName, new AdapterPhotos.ClickInImage() {
            @Override
            public void clickOnPicture(PhotoHysh photoHysh, int position, Bitmap bitmap) {


                if (viewImageExtended == null || viewImageExtended.getDialog() == null || !viewImageExtended.getDialog().isShowing()) {


                    pic = listString.get(position);
                    FragmentManager fm = getSupportFragmentManager();

                    Bundle arguments = new Bundle();


                    arguments.putParcelable(TAG_BITMAP, bitmap);
                    arguments.putParcelable(TAG_INFO, pic);
                    arguments.putInt(POSITION, position);
                    arguments.putString(SESSION_NAME, sessionName);

                    ArrayList<PhotoHysh> passingArrayList = new ArrayList<>();
                    passingArrayList.addAll(listString);

                    arguments.putParcelableArrayList(LIST_IMAGES, passingArrayList);
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
        pack = 0;
        String message = " ";
        for (int i = 0; i < listString.size(); i++) {
            if (listString.get(i).isSelected()) {
                totalSelected++;
            }
        }


        if (totalSelected >= 5 && totalSelected <= 9) {
            extraPhotos = totalSelected - 5;
            amount = extraPhotos * AMOUNT_EXTRA_PHOTO;
            amount = amount + AMOUNT_FIVE; //TODO make Constants with the price of each session
            pack = 1;

            if (extraPhotos > 0) {
                message = totalSelected + PACK_5 + extraPhotos + " fotos extra = " + amount + " € ";
            } else {
                message = totalSelected + " " + PRICE_5;
            }
        } else if (totalSelected >= 10 && totalSelected <= 19) {
            extraPhotos = totalSelected - 10;
            amount = extraPhotos * AMOUNT_EXTRA_PHOTO;
            amount = amount + AMOUNT_TEN;
            pack = 2;

            if (extraPhotos > 0) {
                message = totalSelected + PACK_10 + extraPhotos + " fotos extra = " + amount + " € ";
            } else {
                message = totalSelected + " " + PRICE_10;
            }
        } else if (totalSelected == 20) {
            message = totalSelected + " " + PRICE_20;
            pack = 3;
        } else {
            extraPhotos = totalSelected - 20;
            amount = extraPhotos * AMOUNT_EXTRA_PHOTO;
            amount = amount + AMOUNT_TWENTY;
            pack = 3;
            message = totalSelected + PACK_20 + extraPhotos + " fotos extra = " + amount;
        }

        if (totalSelected <= 4) {
            message = String.valueOf(totalSelected);
            pack = 0;
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

    @Override
    public void onBackPressed() {

        new AlertsHysh(context, "¿Volver?", "Perderás toda la selección si vuelves atrás. ¿Volver de todos modos?", new AlertsHysh.SettingInterface() {
            @Override
            public void dothings() {
                Intent intent = new Intent(context,SessionSelector.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }
}
