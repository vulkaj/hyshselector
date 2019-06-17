package com.example.hyshselector.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.example.hyshselector.R;
import com.example.hyshselector.ResumeOfSelection;

import static com.example.hyshselector.utils.Constants.SESSION_NAME;

public class AlertsHysh {

    private SettingInterface settingInterface;
    private Context context;
    private String title;
    private String message;

    public AlertsHysh(Context context, String title, final String message, final SettingInterface settingInterface) {
        this.context = context;
        this.settingInterface = settingInterface;
        this.title = title;
        this.message = message;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setTitle(title);

        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        settingInterface.dothings();
                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public interface SettingInterface {
        void dothings();
    }
}



