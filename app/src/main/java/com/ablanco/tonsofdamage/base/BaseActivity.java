package com.ablanco.tonsofdamage.base;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public abstract String getClassName();

    @Override
    protected void onResume() {
        super.onResume();
        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));
        AnalyticsHandler.getInstance(this).trackScreenNavigation(getClassName(), getNavigationItemId());
    }

    public abstract String getNavigationItemId();
}
