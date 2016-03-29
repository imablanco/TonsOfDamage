package com.ablanco.tonsofdamage.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class SettingsHandler {

    private static final String KEY_REGION = "region";
    private static final String KEY_SUMMONER = "summoner";

    public static boolean isSetupNeeded(Context context){
        return getRegion(context) == null || getSummoner(context) == -1;
    }

    public static void setRegion(Context context, String region){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_REGION, region).apply();
    }

    public static String getRegion(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_REGION,null);
    }

    public static void setSummoner(Context context, long id){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(KEY_SUMMONER, id).apply();
    }

    public static long getSummoner(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_SUMMONER,-1);
    }
}
