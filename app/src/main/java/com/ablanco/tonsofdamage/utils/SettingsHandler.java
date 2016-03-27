package com.ablanco.tonsofdamage.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class SettingsHandler {

    private static final String KEY_REGION = "region";

    public static void setRegion(Context context, String region){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_REGION, region).apply();
    }

    public static String geRegion(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_REGION,null);
    }
}
