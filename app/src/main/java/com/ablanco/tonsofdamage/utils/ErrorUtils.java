package com.ablanco.tonsofdamage.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.ablanco.tonsofdamage.R;

/**
 * Created by Álvaro Blanco on 18/04/2016.
 * TonsOfDamage
 */
public class ErrorUtils {


    public static void showError(View v){
        if(v != null){
            Snackbar.make(v, R.string.error_text, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showPersistentError(View v, View.OnClickListener listener){
        if(v != null){
            Snackbar.make(v, R.string.error_text, Snackbar.LENGTH_INDEFINITE).setAction(R.string.reload, listener).show();
        }
    }
}
