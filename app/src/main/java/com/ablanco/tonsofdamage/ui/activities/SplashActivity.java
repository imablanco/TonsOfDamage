package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.fragments.ChooseRegionDialogFragment;
import com.ablanco.tonsofdamage.utils.SettingsHandler;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(SettingsHandler.geRegion(this) == null){
            ChooseRegionDialogFragment.newInstance(new ChooseRegionDialogFragment.ChooseRegionDialogChooseListener() {
                @Override
                public void onRegionChoosed(String region) {
                    //SettingsHandler.setRegion(SplashActivity.this, region);
                    Log.d("SplashActivity", "region choosed " + region);
                    goHome();
                }

            }).show(getSupportFragmentManager(), "choose_region");

        }else {
            goHome();
        }

    }

    private void goHome(){

    }
}
