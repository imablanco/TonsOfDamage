package com.ablanco.tonsofdamage;

import android.app.Application;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.constants.Regions;

/**
 * Created by Álvaro Blanco Cabrero on 26/3/16
 * TonsOfDamage
 */
public class TODApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Teemo.setArmedAndReady(this, Regions.REGION_EUW);
    }
}
