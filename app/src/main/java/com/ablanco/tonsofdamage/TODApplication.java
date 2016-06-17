package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.teemo.Teemo;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.google.android.gms.analytics.GoogleAnalytics;

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

        GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(!SettingsHandler.getSendAnalytics(this));

        //Teemo.setArmedAndReady(this,SecurityUtils.getWhatIsMine(this));
        Teemo.setArmedAndReady(this, "ce700ddc-51d4-44f5-b220-139e33e3d92d");
        if(SettingsHandler.getRegion(this) != null){
            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(this));
        }
    }

}
