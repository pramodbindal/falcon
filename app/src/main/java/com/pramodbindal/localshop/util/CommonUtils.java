package com.pramodbindal.localshop.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Pramod.Bindal on 1/21/2017.
 */

public class CommonUtils {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd-MM-yyyy", getDefaultLocale());


    private CommonUtils() {
    }

    public static Locale getDefaultLocale() {
        return Locale.getDefault();
    }

    public static String formatDate(Date date) {
        return SDF.format(date);
    }

    public static boolean hasPermission(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    public static String formatAmount(double amount) {
        Locale defaultLocale = getDefaultLocale();
        System.out.println("defaultLocale = " + defaultLocale);
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(defaultLocale);
        return currencyInstance.format(amount);
    }

    public static String getCurrentDate() {
        return formatDate(new Date());
    }

    public static String readRawTextFile(Context ctx, int resId) {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        InputStreamReader inputreader = new InputStreamReader(inputStream);
        BufferedReader buffreader = new BufferedReader(inputreader);
        String line;
        StringBuilder text = new StringBuilder();

        try {
            while ((line = buffreader.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
        } catch (IOException e) {
            return null;
        }
        return text.toString();
    }

}
