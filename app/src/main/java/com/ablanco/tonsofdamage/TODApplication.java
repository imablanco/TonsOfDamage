package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.teemo.Teemo;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;

/**
 * Created by Álvaro Blanco Cabrero on 26/3/16
 * TonsOfDamage
 */
public class TODApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

        Teemo.setArmedAndReady(this);
        if(SettingsHandler.getRegion(this) != null){
            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(this));
        }
    }
}
