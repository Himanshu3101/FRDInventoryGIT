package com.yoeki.kalpnay.frdinventry.custom_control;

import android.app.Activity;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
    static Toast toast;

    public static void showToast(Activity activity, String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(activity,
                message,
                Toast.LENGTH_SHORT);

        toast.show();
    }

    public static void showCustomToast(Activity activity, String message) {

        TextView textView = new TextView(activity);
        textView.setText("");
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = new Toast(activity);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(textView);
        toast.show();
    }
}
