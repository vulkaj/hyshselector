package com.example.hyshselector.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hyshselector.MainActivity;
import com.example.hyshselector.R;
import com.example.hyshselector.SessionSelector;
import com.example.hyshselector.utils.CreatingThumbnails;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdapterSessions extends RecyclerView.Adapter<AdapterSessions.MyViewHolder> {


    private Context context;
    private List<String> listString;
    private String sessionName;

    public AdapterSessions(Context context, List<String> listString) {
        this.context = context;
        this.listString = listString;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sessions, viewGroup, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        holder.textSelection.setText(listString.get(position));

        holder.textSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.textSelection.setTextColor(ContextCompat.getColor(context, R.color.hyshPink));
                new CountDownTimer(500, 50) {

                    @Override
                    public void onTick(long arg0) {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onFinish() {
                        holder.textSelection.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                    }
                }.start();


                sessionName = listString.get(position);

                CreatingThumbnails creatingThumbnails = new CreatingThumbnails(context, sessionName, position, listString);
                creatingThumbnails.execute();


            }
        });
    }

    @Override
    public int getItemCount() {
        return listString.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.text_selection)
        TextView textSelection;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }


    private void intentToGallery(int position) {
        //TODO crear un loader dialog que no deje avanzar para que de tiempo al programa a crear las thumbnails para cargarlas después


    }


}
