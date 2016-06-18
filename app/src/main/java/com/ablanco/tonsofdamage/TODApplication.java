package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Álvaro Blanco Cabrero on 26/3/16
 * TonsOfDamage
 */
public class TODApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("friz_quad.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

        AnalyticsHandler.getInstance(getApplicationContext()).enableSendAnalyticsEvents(SettingsHandler.getSendAnalytics(this));
    }

}
