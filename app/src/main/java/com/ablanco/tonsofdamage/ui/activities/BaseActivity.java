package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.ablanco.tonsofdamage.R;

/**
 * Created by Álvaro Blanco on 02/04/2016.
 * TonsOfDamage
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_base);
    }

    protected final void setMainContent(Fragment f){
        getSupportFragmentManager().beginTransaction().replace(R.id.content, f).commit();
    }
}
