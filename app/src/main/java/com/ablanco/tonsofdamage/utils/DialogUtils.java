package com.ablanco.tonsofdamage.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Álvaro Blanco Cabrero on 10/6/16
 * TonsOfDamage
 */
public class DialogUtils {

    public static void showDialog(Context context, String title, String message, String possitiveText, DialogInterface.OnClickListener possitiveListener, String negativeText, DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(context).setMessage(message).setTitle(title).setPositiveButton(possitiveText, possitiveListener).setNegativeButton(negativeText, negativeListener).create().show();
    }

    public static void showDialog(Context context, int title, int message, int possitiveText, DialogInterface.OnClickListener possitiveListener, int negativeText, DialogInterface.OnClickListener negativeListener){
        new AlertDialog.Builder(context).setMessage(message).setTitle(title).setPositiveButton(possitiveText, possitiveListener).setNegativeButton(negativeText, negativeListener).create().show();
    }
}
