package com.ablanco.tonsofdamage.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;

import java.util.List;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (SettingsHandler.isSetupNeeded(SplashActivity.this)) {
            NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.SETUP);
            finish();
        } else {

            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(SplashActivity.this));
            //// TODO: 10/04/2016 in future, save in DB
            ResourcesHandler.init(getApplicationContext());
            Teemo.getInstance(SplashActivity.this).getStaticDataHandler().getVersions(new ServiceResponseListener<List<String>>() {
                @Override
                public void onResponse(List<String> response) {
                    SettingsHandler.setCDNVersion(SplashActivity.this, response.get(0));
                    NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.HOME);
                    finish();
                }

                @Override
                public void onError(TeemoException e) {
                    NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.HOME);
                    finish();
                }
            });
        }

    }

}
