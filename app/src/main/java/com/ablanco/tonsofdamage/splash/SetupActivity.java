package com.ablanco.tonsofdamage.splash;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SetupActivity extends BaseActivity implements SetupListener {

    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.tv_hint)
    TextView tvHint;
    @Bind(R.id.loading)
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_access_setup);
        ButterKnife.bind(this);

        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseRegion();
            }
        });

        if (!isRegionSelected() && !isLanguageSelected() && !isSummonerSelected()) {
            enableViews();
        } else if (!isRegionSelected()) {
            chooseRegion();
        } else {
            Teemo.getInstance(getApplicationContext()).setRegion(SettingsHandler.getRegion(SetupActivity.this));
            getCDNVersions();
        }

    }

    private void getCDNVersions() {
        loading.setVisibility(View.VISIBLE);
        Teemo.getInstance(getApplicationContext()).getStaticDataHandler().getVersions(new ServiceResponseListener<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                SettingsHandler.setCDNVersion(SetupActivity.this, response.get(0));
                loading.setVisibility(View.GONE);
                if (!isLanguageSelected()) {
                    chooseLanguage();
                } else if (!isSummonerSelected()) {
                    chooseSummoner();
                } else {
                    NavigationHandler.navigateTo(SetupActivity.this, NavigationHandler.SPLASH);
                }
            }

            @Override
            public void onError(TeemoException e) {
                SettingsHandler.setRegion(SetupActivity.this, null);
                NavigationHandler.navigateTo(SetupActivity.this, NavigationHandler.SPLASH);
            }
        });
    }

    private void chooseRegion() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ChooseRegionFragment.newInstance(this)).commit();
        disableViews();
    }

    private void chooseLanguage() {
        getSupportFragmentManager().beginTransaction().replace(R.id.content, ChooseLanguageFragment.newInstance(this)).commit();
        disableViews();
    }

    private void chooseSummoner() {
        this.getSupportFragmentManager().beginTransaction().replace(R.id.content, PickSummonerFragment.newInstance(this)).commit();
        disableViews();
    }

    private void enableViews() {
        btGo.setVisibility(View.VISIBLE);
        tvHint.setVisibility(View.VISIBLE);
    }

    private void disableViews() {
        btGo.setVisibility(View.GONE);
        tvHint.setVisibility(View.GONE);
    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_SETUP;
    }

    @Override
    public String getNavigationItemId() {
        return null;
    }

    @Override
    public void onRegionSelected(String region) {
        SettingsHandler.setRegion(this, region);
        Teemo.getInstance(this).setRegion(region);
        getCDNVersions();
    }

    @Override
    public void onLanguageSelected(String lang) {
        SettingsHandler.setLanguage(this, lang);
        Utils.updateLanguage(this, SettingsHandler.getLanguage(this));

        if (!isSummonerSelected()) {
            chooseSummoner();
        } else {
            NavigationHandler.navigateTo(this, NavigationHandler.SPLASH);
        }
    }

    @Override
    public void onSummonerSelected(Long summoner) {
        SettingsHandler.setSummoner(this, summoner);
        NavigationHandler.navigateTo(this, NavigationHandler.SPLASH);
    }

    private boolean isRegionSelected() {
        return SettingsHandler.getRegion(this) != null;
    }

    private boolean isLanguageSelected() {
        return !SettingsHandler.getLanguage(this).isEmpty();
    }

    private boolean isSummonerSelected() {
        return SettingsHandler.getSummoner(this) > -1;
    }
}
