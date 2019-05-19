package com.example.hyshselector.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.hyshselector.MainActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class CreatingThumbnails extends AsyncTask<Void, Void, Void> {
    private ProgressDialog mProgressDialog;

    private final Context context;
    private final String sessionName;
    private final int position;
    private List<String> listString;

    public CreatingThumbnails(Context context, String sessionName, int position, List<String> listString) {

        this.context = context;
        this.sessionName = sessionName;
        this.position = position;
        this.listString = listString;
    }

    @Override
    protected Void doInBackground(Void... voids) {


        String path = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/" + sessionName;
        String pathThumbnails = Environment.getExternalStorageDirectory().toString() + "/" + "HyshSelections/Thumbnails/" + sessionName;
        File directory = new File(path);
        File fileDirectoryThumbnails = new File(pathThumbnails);
        File[] files = directory.listFiles();


        if (!fileDirectoryThumbnails.exists()) {
            try {


                for (int i = 0; i < files.length; i++) {

                    if (files[i].getName().contains(".jpg")) {

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


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        mProgressDialog = ProgressDialog.show(context, "Cargando", "Por favor, espere...");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(true);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dlg) {
                CreatingThumbnails.this.cancel(true);
            }
        });
    }


    @Override
    protected void onPostExecute(Void result) {
        if (this.isCancelled()) {
            result = null;
            return;
        }

        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("session_name", listString.get(position));
        context.startActivity(intent);


    }


}