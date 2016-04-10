package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class SettingsHandler {

    private static final String KEY_REGION = "region";
    private static final String KEY_SUMMONER = "summoner";
    private static final String KEY_FAVORITE_CHAMPIONS = "favorite_champions";

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


    public static void addFavoriteChampion(Context context, int champId){
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, null);
        if(champs == null){
            champs = new HashSet<>();
        }

        if(champs.add(String.valueOf(champId))){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_CHAMPIONS, champs).apply();
        }
    }

    public static void removeFavoriteChampion(Context context, int champId){
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, null);

        if(champs != null && champs.remove(String.valueOf(champId))){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_CHAMPIONS, champs).apply();
        }
    }

    public static boolean isChampionMarkedAsFavorite(Context context, int champId){
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, null);

        return champs != null && champs.contains(String.valueOf(champId));
    }
}
