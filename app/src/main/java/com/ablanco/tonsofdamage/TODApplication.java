package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.teemo.Teemo;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Álvaro Blanco Cabrero on 26/3/16
 * TonsOfDamage
 */
public class TODApplication extends Application {
    private Tracker mTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("friz_quad.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

        Teemo.setArmedAndReady(this);
        if(SettingsHandler.getRegion(this) != null){
            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(this));
        }
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }
}
