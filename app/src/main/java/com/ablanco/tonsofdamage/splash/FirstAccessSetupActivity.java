package com.ablanco.tonsofdamage.splash;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.splash.FirstAccessSetupFragment;

public class FirstAccessSetupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access_setup);

        getSupportFragmentManager().beginTransaction().replace(R.id.content, FirstAccessSetupFragment.newInstance()).commit();
    }
}
