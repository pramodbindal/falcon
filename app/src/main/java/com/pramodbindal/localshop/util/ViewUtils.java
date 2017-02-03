package com.pramodbindal.localshop.util;

import android.app.Activity;
import android.app.AlertDialog;

public class ViewUtils {

    public static void showErrorDialog(Activity activity, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage(message).show();
    }


}


