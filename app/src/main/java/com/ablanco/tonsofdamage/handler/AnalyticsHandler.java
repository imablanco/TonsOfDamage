package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

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

    public final static class UserProperty {
        public final static String PROPERTY_LANG = "Language";
        public final static String PROPERTY_REGION = "Region";
        public final static String PROPERTY_NOTIFS = "NotificationsEnabled";
    }

    public final static class Event {
        public static final String EVENT_SCREEN = "screen_navigation";
    }

    public final static class Param {
        public static final String PARAM_SCREEN_NAME = "screen_name";
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

    public void trackScreenName(String screen){
        Bundle params = new Bundle();
        params.putString(Param.PARAM_SCREEN_NAME, screen);
        analytics.logEvent(Event.EVENT_SCREEN, params);
    }

    public void enableSendAnalyticsEvents(boolean enable){
        analytics.setAnalyticsCollectionEnabled(enable);
    }

    public void setUserProperty(String property, @Nullable String value){
        this.analytics.setUserProperty(property, value);
    }
}
