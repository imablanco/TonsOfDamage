package com.ablanco.tonsofdamage.runecreator;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 22/06/2016.
 * TonsOfDamage
 */
public class RuneCreatorActivity extends BaseActivity implements RuneCreatorFlowListener{

    public static final int STATE_FIRST_TIME = 0;
    public static final int STATE_LANDING = 1;
    public static final int STATE_EDITING = 2;

    private int mState = STATE_FIRST_TIME;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rune_creator);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // TODO: 03/07/2016 check first time// user dont have rune pages

        getSupportFragmentManager().beginTransaction().add(R.id.content, FirstTimeFragment.newInstance(this)).commit();
    }


    @Override
    public void onAddButtonClicked() {
        switch (mState){
            case STATE_FIRST_TIME:default:
                mState = STATE_EDITING;
                getSupportFragmentManager().beginTransaction().replace(R.id.content, RuneCreatorLandingFragment.newInstance(this)).commit();
                getSupportFragmentManager().beginTransaction().add(R.id.content, RuneCreatorEditorFragment.newInstance(this)).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;
            // TODO: 03/07/2016 check between 2 states 1: we are in land and click add, 2: we are in editor and click more,
            case STATE_LANDING:
                mState = STATE_EDITING;
                getSupportFragmentManager().beginTransaction().add(R.id.content, RuneCreatorEditorFragment.newInstance(this)).addToBackStack(null).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
                break;

        }

    }

    @Override
    public void onBackNavigationButtonClicked() {
        if(mState == STATE_EDITING){
            DialogUtils.showDialog(this, "Atencion", "Perderes los cambios", "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mState = STATE_LANDING;
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }, "Cancel", null);
        }else {
            NavUtils.navigateUpFromSameTask(RuneCreatorActivity.this);

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    public void onBackPressed() {
        if(mState == STATE_EDITING){
            DialogUtils.showDialog(this, "Atencion", "Perderes los cambios", "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mState = STATE_LANDING;
                    getSupportFragmentManager().popBackStackImmediate();
                }
            }, "Cancel", null);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public String getClassName() {
        return NavigationHandler.RUNE_CREATOR;
    }

    @Override
    public String getNavigationItemId() {
        return null;
    }
}
