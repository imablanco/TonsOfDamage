package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.NavigationHandler;
import com.ablanco.tonsofdamage.utils.SettingsHandler;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (SettingsHandler.isSetupNeeded(SplashActivity.this)) {
                    NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.SETUP);
                } else {
                    NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.HOME);
                }

                finish();
            }
        }, 750);
    }

}
