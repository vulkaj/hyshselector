package com.example.hyshselector;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hyshselector.adapters.AdapterPhotos;
import com.example.hyshselector.entities.Picture;
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

    private Context context;
    private List<Picture> listString;
    private AdapterPhotos adapterPhotos;
    private Constants constants;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        context = this;
        listString = new ArrayList<>();

        settingRecycler();
        gettingDirectoriesNames();

    }

    private void gettingDirectoriesNames() {


        String path = Environment.getExternalStorageDirectory().toString() + "/"+"HyshSelections/Sesion01";
        //String path = Environment.getExternalStorageDirectory().toString();
        StringBuilder stringBuilder = new StringBuilder();
        File directory = new File(path);
        File[] files = directory.listFiles();

        for (int i = 0; i < files.length; i++) {


            if(!files[i].getName().contains(".CR2")){
                Picture picture = new Picture();
                picture.setName(files[i].getName());
                listString.add(picture);
                stringBuilder.append(files[i].getName()+" ");
            }

        }

        textDirectories.setText(stringBuilder.toString());
    }

    private void settingRecycler() {
        adapterPhotos = new AdapterPhotos(context, listString, new AdapterPhotos.ClickInImage() {
            @Override
            public void clickOnPicture(Picture picture, int position) {
                if(picture.isSelected()==true){
                    picture.setSelected(false);
                } else {
                    picture.setSelected(true);
                }
                //adapterPhotos.notifyDataSetChanged();

            }
        });
        recyclerPictures.setAdapter(adapterPhotos);
    }


}
