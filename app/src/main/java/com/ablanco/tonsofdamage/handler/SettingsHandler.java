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
    private static final String KEY_FAVORITE_SUMMONERS = "favorite_summoners";
    private static final String KEY_CDN_VERSION = "cdn_version";
    private static final String KEY_LANGUAGE = "language";


    public static boolean isSetupNeeded(Context context){
        return getRegion(context) == null || getSummoner(context) == -1 || getLanguage(context).isEmpty() ;
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

    public static void setLanguage(Context context, String lang){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_LANGUAGE, lang).apply();
    }

    public static String getLanguage(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LANGUAGE, "");
    }

    public static void setCDNVersion(Context context, String version){
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_CDN_VERSION, version).apply();
    }

    public static String getCDNVersion(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_CDN_VERSION,"");
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

    public static void addFavoriteSummoner(Context context, long summonerId){
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, null);
        if(summoners == null){
            summoners = new HashSet<>();
        }

        if(summoners.add(String.valueOf(summonerId))){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_SUMMONERS, summoners).apply();
        }
    }

    public static void removeFavoriteSummoner(Context context, long summonerId){
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, null);

        if(summoners != null && summoners.remove(String.valueOf(summonerId))){
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_SUMMONERS, summoners).apply();
        }
    }

    public static boolean isSummonerMarkedAsFavorite(Context context, long summonerId){
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, null);

        return summoners != null && summoners.contains(String.valueOf(summonerId));
    }
}
