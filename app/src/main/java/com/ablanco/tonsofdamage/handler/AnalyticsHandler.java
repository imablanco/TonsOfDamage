package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.ablanco.tonsofdamage.BuildConfig;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by Álvaro Blanco Cabrero on 8/6/16
 * TonsOfDamage
 */
public class AnalyticsHandler {

    public static final String CLASS_NAME_SETTINGS = "Settings";
    public static final String CLASS_NAME_CHAMPION_DETAIL = "ChampionDetail";
    public static final String CLASS_NAME_CHAMPION_SKIN_DETAIL = "ChampionSkin";
    public static final String CLASS_NAME_HOMEACTIVIY = "Home";
    public static final String CLASS_NAME_HOME_PLAYER_VIEW = "HomePlayerView";
    public static final String CLASS_NAME_ITEM_DETAIL = "ItemDetail";
    public static final String CLASS_NAME_SETUP = "Setup";
    public static final String CLASS_NAME_MASTERY_DETAIL = "MasteryDetail";
    public static final String CLASS_NAME_MASTERIES = "Masteries";
    public static final String CLASS_NAME_MATCH_DETAIL = "MatchDetail";
    public static final String CLASS_NAME_RUNE_DETAIL = "RuneDetail";
    public static final String CLASS_NAME_RUNES = "Runes";
    public static final String CLASS_NAME_SUMMONER_DETAIL = "SummonerDetail";
    public static final String CLASS_NAME_VIDEO = "Video";
    public static final String CLASS_NAME_HOME_HOME = "HomeMain";
    public static final String CLASS_NAME_HOME_CHAMPIONS = "HomeChampions";
    public static final String CLASS_NAME_HOME_ITEMS = "HomeItems";
    public static final String CLASS_NAME_HOME_SUMMONERS = "HomeSummoners";

    public final static class UserProperty {
        public final static String PROPERTY_LANG = "Language";
        public final static String PROPERTY_REGION = "Region";
        public final static String PROPERTY_NOTIFS = "NotificationsEnabled";
    }

    public final static class Event {
        public static final String EVENT_SCREEN = "screen_navigation";
        public static final String EVENT_SCREEN_SECTION = "screen_navigation_section";
        public static final String EVENT_CONTENT_CLICK = "content_click";
    }


    private FirebaseAnalytics analytics;

    private static AnalyticsHandler mInstance;

    public static AnalyticsHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new AnalyticsHandler(context);
        }

        return mInstance;
    }

    private AnalyticsHandler(Context context){
        this.analytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
        if(!SettingsHandler.getLanguage(context).isEmpty()){
            this.analytics.setUserProperty(UserProperty.PROPERTY_LANG, SettingsHandler.getLanguage(context));
        }
        if(SettingsHandler.getRegion(context) != null){
            this.analytics.setUserProperty(UserProperty.PROPERTY_REGION, SettingsHandler.getRegion(context));
        }

        this.analytics.setUserProperty(UserProperty.PROPERTY_NOTIFS, String.valueOf(SettingsHandler.getSendNotifs(context)));

    }


    public void trackScreenNavigation(String screen, String itemId){
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.DESTINATION, screen);
        if(itemId != null){
            params.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        }
        analytics.logEvent(Event.EVENT_SCREEN, params);

        if(BuildConfig.DEBUG){
            StringBuilder builder = new StringBuilder();
            for (String s : params.keySet()) {
                builder.append(s);
                builder.append(" : ");
                builder.append(params.get(s));
                builder.append("\n");
            }

            Log.d("AnalyticsHandler", builder.toString());
        }
    }

    public void trackSearchEvent(String screen, String term){
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.SEARCH_TERM, term);
        params.putString(FirebaseAnalytics.Param.ORIGIN, screen);

        analytics.logEvent(FirebaseAnalytics.Event.SEARCH, params);

        if(BuildConfig.DEBUG){
            StringBuilder builder = new StringBuilder();
            for (String s : params.keySet()) {
                builder.append(s);
                builder.append(" : ");
                builder.append(params.get(s));
                builder.append("\n");
            }

            Log.d("AnalyticsHandler", builder.toString());
        }
    }

    public void trackScreenSectionNavigation(String screenParent, String section, String itemId){
        Bundle params = new Bundle();
        params.putString(FirebaseAnalytics.Param.ORIGIN, screenParent);
        params.putString(FirebaseAnalytics.Param.DESTINATION, section);

        if(itemId != null){
            params.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        }

        analytics.logEvent(Event.EVENT_SCREEN_SECTION, params);

        if(BuildConfig.DEBUG){
            StringBuilder builder = new StringBuilder();
            for (String s : params.keySet()) {
                builder.append(s);
                builder.append(" : ");
                builder.append(params.get(s));
                builder.append("\n");
            }

            Log.d("AnalyticsHandler", builder.toString());
        }
    }

    public void enableSendAnalyticsEvents(boolean enable){
        analytics.setAnalyticsCollectionEnabled(enable);
    }

    public void setUserProperty(String property, @Nullable String value){
        this.analytics.setUserProperty(property, value);
    }
}
