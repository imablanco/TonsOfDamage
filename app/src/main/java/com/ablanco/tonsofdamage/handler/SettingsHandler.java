package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private static final String KEY_SEND_NOTIFS = "send_notifs";
    private static final String KEY_SEND_ANALYTICS = "send_analytics";
    private static final String KEY_LANGUAGE = "language";


    public static boolean isSetupNeeded(Context context) {
        return getRegion(context) == null || getSummoner(context) == -1 || getLanguage(context).isEmpty();
    }

    public static void setRegion(Context context, String region) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_REGION, region).apply();
        AnalyticsHandler.getInstance(context).setUserProperty(AnalyticsHandler.UserProperty.PROPERTY_REGION, region);
    }

    public static String getRegion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_REGION, null);
    }

    public static void setSummoner(Context context, long id) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putLong(KEY_SUMMONER, id).apply();
    }

    public static long getSummoner(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getLong(KEY_SUMMONER, -1);
    }

    public static void setLanguage(Context context, String lang) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_LANGUAGE, lang).apply();
        AnalyticsHandler.getInstance(context).setUserProperty(AnalyticsHandler.UserProperty.PROPERTY_LANG, lang);
    }

    public static String getLanguage(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_LANGUAGE, "");
    }

    public static void setSendNotifs(Context context, boolean send) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_SEND_NOTIFS, send).apply();
    }

    public static boolean getSendNotifs(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_SEND_NOTIFS, true);
    }

    public static void setSendAnalytics(Context context, boolean send) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(KEY_SEND_ANALYTICS, send).apply();
    }

    public static boolean getSendAnalytics(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(KEY_SEND_ANALYTICS, true);
    }

    public static void setCDNVersion(Context context, String version) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(KEY_CDN_VERSION, version).apply();
    }

    public static String getCDNVersion(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(KEY_CDN_VERSION, "");
    }

    public static void addFavoriteChampion(Context context, int champId) {
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, new HashSet<String>());
        Set<String> tempChamps = new HashSet<>(champs);


        if (tempChamps.add(String.valueOf(champId))) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_CHAMPIONS, tempChamps).apply();
        }
    }

    public static void removeFavoriteChampion(Context context, int champId) {
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, new HashSet<String>());
        Set<String> tempChamps = new HashSet<>(champs);

        if (tempChamps.remove(String.valueOf(champId))) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_CHAMPIONS, tempChamps).apply();
        }

    }

    public static boolean isChampionMarkedAsFavorite(Context context, int champId) {
        Set<String> champs = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, new HashSet<String>());

        return champs.contains(String.valueOf(champId));
    }

    public static List<String> getFavoriteChampions(Context context) {
        return new ArrayList<>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_CHAMPIONS, new HashSet<String>()));
    }

    public static void addFavoriteSummoner(Context context, long summonerId) {
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, new HashSet<String>());
        Set<String> tempSummoners = new HashSet<>(summoners);

        if (tempSummoners.add(String.valueOf(summonerId))) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_SUMMONERS, tempSummoners).apply();
        }
    }

    public static void removeFavoriteSummoner(Context context, long summonerId) {
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, new HashSet<String>());
        Set<String> tempSummoners = new HashSet<>(summoners);

        if (tempSummoners.remove(String.valueOf(summonerId))) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_SUMMONERS, tempSummoners).apply();
        }
    }

    public static boolean isSummonerMarkedAsFavorite(Context context, long summonerId) {
        Set<String> summoners = PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, new HashSet<String>());

        return summoners.contains(String.valueOf(summonerId));
    }

    public static List<String> getFavoriteSummoners(Context context) {
        return new ArrayList<>(PreferenceManager.getDefaultSharedPreferences(context).getStringSet(KEY_FAVORITE_SUMMONERS, new HashSet<String>()));
    }

    public static void clearFavoriteSummoners(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_SUMMONERS, new HashSet<String>()).apply();
    }

    public static void clearFavoriteChampions(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putStringSet(KEY_FAVORITE_CHAMPIONS, new HashSet<String>()).apply();
    }
}
