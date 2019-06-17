package com.yoeki.kalpnay.frdinventry.custom_control;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class CustomDialogForMessages {
    static AlertDialog alertDialog;

    public static void showMessageAlert(final Activity activity, String title, String Message) {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        alertDialog = new AlertDialog.Builder(
                activity, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT).create();

        // Setting Dialog Title
        alertDialog.setTitle(title);

        // Setting Dialog Message
        alertDialog.setMessage(Message);

        // Setting Icon to Dialog

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

                setOnClickEvent(dialog);
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }



    public static boolean setOnClickEvent(DialogInterface dialog) {
        dialog.dismiss();
        return true;
    }
    public static boolean closeDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        return true;
    }
}
