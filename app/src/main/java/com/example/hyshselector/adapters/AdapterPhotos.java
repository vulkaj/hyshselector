package com.example.hyshselector.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.hyshselector.R;
import com.example.hyshselector.entities.PhotoHysh;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterPhotos extends RecyclerView.Adapter<AdapterPhotos.MyViewHolder> {


    private Context context;
    private List<PhotoHysh> listString;
    private ClickInImage clickInImage;
    private String sessionName;

    public AdapterPhotos(Context context, List<PhotoHysh> listString, String sessionName, ClickInImage clickInImage) {
        this.context = context;
        this.listString = listString;
        this.sessionName = sessionName;
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


        final PhotoHysh photoHysh = listString.get(position);
        String thumbnailPath = Environment.getExternalStorageDirectory().toString() + "/HyshSelections/Thumbnails/" + sessionName;
        String path = Environment.getExternalStorageDirectory().toString() + "/HyshSelections/" + sessionName;
        File f = new File(thumbnailPath, listString.get(position).getName());
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        //TODO crear Thumbnails mas pequeñas pero que la imagen al seleccionarla se vea a tamaño guay
        bmOptions.inSampleSize = 2;
        final Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());
        holder.roundedPicture.setImageBitmap(bitmap);

        File realFile = new File(path, listString.get(position).getName());
        final Bitmap realBitmap = BitmapFactory.decodeFile(realFile.getAbsolutePath(), bmOptions);


        holder.relativePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickInImage.clickOnPicture(photoHysh, position, realBitmap);
            }
        });

        holder.relativePicture.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                clickInImage.longClickOnPicture(photoHysh, position);
                return true;
            }
        });


        //TODO hacer que la imagen seleccionada se quede marcada
        if (!listString.get(position).isSelected()) {
            holder.roundedPicture.setBorderColor(Color.TRANSPARENT);
        } else {
            holder.roundedPicture.setBorderColor(ContextCompat.getColor(context, R.color.hyshPink));
        }


    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rounded_picture)
        RoundedImageView roundedPicture;
        @BindView(R.id.relative_picture)
        RelativeLayout relativePicture;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public interface ClickInImage {
        void clickOnPicture(PhotoHysh photoHysh, int position, Bitmap bitmap);

        void longClickOnPicture(PhotoHysh photoHysh, int position);
    }
}
