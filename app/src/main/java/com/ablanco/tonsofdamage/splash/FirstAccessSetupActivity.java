package com.ablanco.tonsofdamage.splash;

import android.os.Bundle;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;

public class FirstAccessSetupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access_setup);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, FirstAccessSetupFragment.newInstance()).commit();
    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_SETUP;
    }
}
