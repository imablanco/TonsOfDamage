package com.ablanco.tonsofdamage.splash;

import android.os.Bundle;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;

public class FirstAccessSetupActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access_setup);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, FirstAccessSetupFragment.newInstance()).commit();
    }
}
