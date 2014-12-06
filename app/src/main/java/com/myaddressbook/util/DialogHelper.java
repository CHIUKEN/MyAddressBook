package com.myaddressbook.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by K on 2014/12/6.
 */
public class DialogHelper {
    public final static DialogInterface.OnClickListener NO_ACTION = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //Do nothing
        }
    };

    public static AlertDialog createDialog(Context context, String title, String message) {
        return createDialog(context, title, message, NO_ACTION);
    }

    public static AlertDialog createDialog(Context context, String title, String message, DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("確定", positiveListener);

        return builder.create();
    }
}
