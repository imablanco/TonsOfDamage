package com.ablanco.tonsofdamage.base;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.tonsofdamage.handler.AnalyticsHandler;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public abstract String getClassName();

    @Override
    protected void onResume() {
        super.onResume();
        AnalyticsHandler.getInstance(this).trackScreenName(getClassName());
    }
}
