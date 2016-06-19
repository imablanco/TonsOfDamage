package com.ablanco.tonsofdamage.splash;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.DialogUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.List;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        String door = FirebaseRemoteConfig.getInstance().getString(getString(R.string.the_door));
        if(door != null && !door.isEmpty()){
            Teemo.setArmedAndReady(SplashActivity.this, door);
            goToHome();
        }else {
            getDoor();
        }
    }

    private void getDoor() {

        FirebaseRemoteConfig.getInstance().fetch(0).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                FirebaseRemoteConfig.getInstance().activateFetched();
                String door = FirebaseRemoteConfig.getInstance().getString(getString(R.string.the_door));
                if (door != null && !door.isEmpty()) {
                    Teemo.setArmedAndReady(SplashActivity.this, door);
                    goToHome();
                } else {
                    DialogUtils.showDialog(SplashActivity.this, R.string.atention, R.string.init_error, R.string.retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getDoor();

                        }
                    }, R.string.exit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    });
                }


            }
        });
    }

    private void goToHome() {
        if (SettingsHandler.isSetupNeeded(SplashActivity.this)) {
            NavigationHandler.navigateTo(SplashActivity.this, NavigationHandler.SETUP);
            finish();
        } else {

            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(SplashActivity.this));
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
