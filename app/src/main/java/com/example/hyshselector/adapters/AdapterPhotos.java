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
    private Bitmap realBitmap;
    private String thumbnailPath;
    private String path;
    private File realFile;
    private PhotoHysh photoHysh;
    private BitmapFactory.Options bmOptions;
    private BitmapFactory.Options options;

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
        thumbnailPath = Environment.getExternalStorageDirectory().toString() + "/HyshSelections/Thumbnails/" + sessionName;
        path = Environment.getExternalStorageDirectory().toString() + "/HyshSelections/" + sessionName;
        bmOptions = new BitmapFactory.Options();
        bmOptions.inSampleSize = 2;
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {


        photoHysh = listString.get(position);
        File f = new File(thumbnailPath, listString.get(position).getName());
        final Bitmap bitmap = BitmapFactory.decodeFile(f.getAbsolutePath());


        holder.roundedPicture.setImageBitmap(bitmap);


        realFile = new File(path, listString.get(position).getName());
        realBitmap = BitmapFactory.decodeFile(realFile.getAbsolutePath(), bmOptions);


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
