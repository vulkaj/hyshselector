package com.example.hyshselector.adapters;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hyshselector.MainActivity;
import com.example.hyshselector.R;
import com.example.hyshselector.SessionSelector;

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

                sessionName = listString.get(position);

                creatingThumbNails();


                //TODO crear un loader dialog que no deje avanzar para que de tiempo al programa a crear las thumbnails para cargarlas después
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("session_name", listString.get(position));
                context.startActivity(intent);
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

    private void creatingThumbNails() {

        String path = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/" + sessionName;
        String pathThumbnails = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/Thumbnails/" + sessionName;
        File directory = new File(path);
        File fileDirectoryThumbnails = new File(pathThumbnails);
        File[] files = directory.listFiles();


        if (!fileDirectoryThumbnails.exists()) {
            try {


                for (int i = 0; i < files.length; i++) {

                    if (files[i].getName().contains(".jpg")) {


                        //TODO creo que al hacer toda la mandanga del bitmap la cosa hace que el scroll vaya regular. Mirar esto.
                        BitmapFactory.Options scaleOptions = new BitmapFactory.Options();
                        scaleOptions.inJustDecodeBounds = true;
                        BitmapFactory.decodeFile(path, scaleOptions);
                        int scale = 1;
                        while (scaleOptions.outWidth / scale / 2 >= 250 //ancho
                                && scaleOptions.outHeight / scale / 2 >= 167) { //alto
                            scale *= 2;
                        }


                        // decode with the sample size
                        BitmapFactory.Options outOptions = new BitmapFactory.Options();
                        outOptions.inSampleSize = scale;


                        Bitmap bitmap = BitmapFactory.decodeFile(path + "/" + files[i].getName(), outOptions);
                        int width = bitmap.getWidth();
                        int height = bitmap.getHeight();
                        float scaleWidth = ((float) 250) / width;
                        float scaleHeight = ((float) 167) / height;

                        File compressed = new File(pathThumbnails + "/" + files[i].getName()); //TODO comprobar si le tengo que poner extensión o no

                        File f = new File(pathThumbnails);
                        if (!f.exists()) {
                            f.mkdirs();
                        }

                        Matrix matrix = new Matrix();
                        // RESIZE THE BIT MAP
                        matrix.postScale(scaleWidth, scaleHeight);

                        // "RECREATE" THE NEW BITMAP
                        Bitmap resizedBitmap = Bitmap.createBitmap(
                                bitmap, 0, 0, width, height, matrix, false);

                        //convert the decoded bitmap to stream
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                        resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);

                    /*
                    Right now, we have our bitmap inside byteArrayOutputStream Object, all we need next is to write it to the compressed file we created earlier,
                    java.io.FileOutputStream can help us do just That!
                     */
                        FileOutputStream fileOutputStream = new FileOutputStream(compressed);
                        fileOutputStream.write(byteArrayOutputStream.toByteArray());
                        fileOutputStream.flush();

                        fileOutputStream.close();

                    }

                }

            } catch (Exception e) {

            }

        }


    }


}
