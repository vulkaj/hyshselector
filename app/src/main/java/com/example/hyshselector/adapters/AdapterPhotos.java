package com.example.hyshselector.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.hyshselector.R;
import com.example.hyshselector.entities.Picture;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.MyViewHolder> {


    private Context context;
    private List<Picture> listString;
    private ClickInImage clickInImage;

    public AdapterPhotos(Context context, List<Picture> listString, ClickInImage clickInImage) {
        this.context = context;
        this.listString = listString;
        this.clickInImage = clickInImage;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_photo, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        /*
        Glide.with(context)
                .load("http://mouse.latercera.com/wp-content/uploads/2017/03/ren-stimpy-1.jpg")
                .into(holder.imagePicture);
        */



        final Picture picture = listString.get(position);
        String path = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/Sesion01/";
        File f = new File(path, listString.get(position).getName());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bmOptions);
        holder.imagePicture.setImageBitmap(bitmap);

        holder.relativePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInImage.clickOnPicture(picture,position);

            }
        });


        //TODO hacer que la imagen seleccionada se quede marcada
        if(!listString.get(position).isSelected()){
            holder.imageIconAdd.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorPrimary)));
        } else {
            holder.imageIconAdd.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context,R.color.colorAccent)));
        }



    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.image_picture)
        ImageView imagePicture;
        @BindView(R.id.image_icon_add)
        ImageView imageIconAdd;
        @BindView(R.id.relative_picture)
        RelativeLayout relativePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickInImage{
        void clickOnPicture(Picture picture, int position);
    }
}
