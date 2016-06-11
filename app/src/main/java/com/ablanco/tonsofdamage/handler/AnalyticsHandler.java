package com.ablanco.tonsofdamage.handler;

import android.content.Context;

import com.ablanco.tonsofdamage.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Álvaro Blanco Cabrero on 8/6/16
 * TonsOfDamage
 */
public class AnalyticsHandler {

    public static final String CLASS_NAME_SETTINGS = "Settings";
    public static String CLASS_NAME_CHAMPION_DETAIL = "ChampionDetail";
    public static String CLASS_NAME_CHAMPION_SKIN_DETAIL = "ChampionSkin";
    public static String CLASS_NAME_HOMEACTIVIY = "Home";
    public static String CLASS_NAME_HOME_PLAYER_VIEW = "HomePlayerView";
    public static String CLASS_NAME_ITEM_DETAIL = "ItemDetail";
    public static String CLASS_NAME_SETUP = "Setup";
    public static String CLASS_NAME_MASTERY_DETAIL = "MasteryDetail";
    public static String CLASS_NAME_MASTERIES = "Masteries";
    public static String CLASS_NAME_MATCH_DETAIL = "MatchDetail";
    public static String CLASS_NAME_RUNE_DETAIL = "RuneDetail";
    public static String CLASS_NAME_RUNES = "Runes";
    public static String CLASS_NAME_SUMMONER_DETAIL = "SummonerDetail";
    public static String CLASS_NAME_VIDEO = "Video";

    private Tracker tracker;

    private static AnalyticsHandler mInstance;

    public static AnalyticsHandler getInstance(Context context){
        if(mInstance == null){
            mInstance = new AnalyticsHandler(context);
        }

        return mInstance;
    }

    private AnalyticsHandler(Context context){
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(context.getApplicationContext());
        tracker = analytics.newTracker(R.xml.global_tracker);
        tracker.enableExceptionReporting(true);

    }

    /***
     * Tracking event
     *
     * @param action   action of the event
     * @param label    label
     */
    public void trackEvent(String action, String label) {

        // Build and send an Event.
        tracker.send(new HitBuilders.EventBuilder().setAction(action).setLabel(label).build());
    }

    public void trackScreenName(String screen){
        tracker.setScreenName(screen);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
