package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.teemo.Teemo;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SecurityUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.google.android.gms.ads.MobileAds;

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
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));

        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

        AnalyticsHandler.getInstance(getApplicationContext()).enableSendAnalyticsEvents(SettingsHandler.getSendAnalytics(this));

        Teemo.setArmedAndReady(this, SecurityUtils.getWhatIsMine(this));
        if(SettingsHandler.getRegion(this) != null){
            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(this));
        }

    }

}
