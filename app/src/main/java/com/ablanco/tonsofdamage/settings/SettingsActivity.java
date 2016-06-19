package com.ablanco.tonsofdamage.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.ablanco.teemo.persistence.base.DBContext;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.base.BaseDialog;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.handler.StaticDataHandler;
import com.ablanco.tonsofdamage.utils.DialogUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.psdev.licensesdialog.LicensesDialog;
import hotchemi.android.rate.AppRate;

/**
 * Created by Álvaro Blanco Cabrero on 10/6/16
 * TonsOfDamage
 */
public class SettingsActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tv_cehck_server_status)
    TextView tvCehckServerStatus;
    @Bind(R.id.tv_clear_favs)
    TextView tvClearFavs;
    @Bind(R.id.cb_stop_sending_notifs)
    CheckBox cbStopSendingNotifs;
    @Bind(R.id.cb_stop_sending_analytics)
    CheckBox cbStopSendingAnalytics;
    @Bind(R.id.tv_rate_app)
    TextView tvRateApp;
    @Bind(R.id.tv_osl)
    TextView tvOsl;
    @Bind(R.id.tv_about)
    TextView tvAbout;
    @Bind(R.id.tv_change_lang)
    TextView tvChangeLang;
    @Bind(R.id.tv_change_region)
    TextView tvChangeRegion;
    @Bind(R.id.tv_change_summoner)
    TextView tvChangeSummoner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        tvClearFavs.setOnClickListener(this);
        tvOsl.setOnClickListener(this);
        tvRateApp.setOnClickListener(this);
        tvAbout.setOnClickListener(this);
        tvCehckServerStatus.setOnClickListener(this);
        tvChangeLang.setOnClickListener(this);
        tvChangeRegion.setOnClickListener(this);
        tvChangeSummoner.setOnClickListener(this);

        cbStopSendingNotifs.setChecked(!SettingsHandler.getSendNotifs(this));
        cbStopSendingAnalytics.setChecked(!SettingsHandler.getSendAnalytics(this));

        cbStopSendingAnalytics.setOnCheckedChangeListener(this);
        cbStopSendingNotifs.setOnCheckedChangeListener(this);

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_SETTINGS;
    }

    @Override
    public String getNavigationItemId() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_about:
                BaseDialog.newInstance(getString(R.string.action_about), getString(R.string.disclaimer_text)).show(getSupportFragmentManager().beginTransaction(), "disclaimer");
                break;
            case R.id.tv_clear_favs:
                DialogUtils.showDialog(this, R.string.atention, R.string.are_you_sure_delete_favs, R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsHandler.clearFavoriteChampions(SettingsActivity.this);
                        SettingsHandler.clearFavoriteSummoners(SettingsActivity.this);
                    }
                }, R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.tv_osl:
                new LicensesDialog.Builder(this)
                        .setNotices(R.raw.notices)
                        .setIncludeOwnLicense(true)
                        .build()
                        .show();
                break;
            case R.id.tv_rate_app:
                AppRate.with(this).showRateDialog(this);
                break;
            case R.id.tv_cehck_server_status:
                new ServerStatusDialog().show(getSupportFragmentManager(), null);
                break;
            case R.id.tv_change_lang:
                DialogUtils.showDialog(this, R.string.atention, R.string.are_you_sure, R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StaticDataHandler.finish();
                        SettingsHandler.setLanguage(SettingsActivity.this, "");
                        NavigationHandler.navigateTo(SettingsActivity.this, NavigationHandler.SPLASH);
                    }
                }, R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.tv_change_region:
                DialogUtils.showDialog(this, R.string.atention, R.string.are_you_sure, R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsHandler.setRegion(SettingsActivity.this, null);
                        SettingsHandler.setSummoner(SettingsActivity.this, -1);
                        StaticDataHandler.finish();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DBContext.clearDb();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        NavigationHandler.navigateTo(SettingsActivity.this, NavigationHandler.SPLASH);
                                    }
                                });
                            }
                        }).start();
                    }
                }, R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                break;
            case R.id.tv_change_summoner:
                DialogUtils.showDialog(this, R.string.atention, R.string.are_you_sure, R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsHandler.setSummoner(SettingsActivity.this, -1);
                        NavigationHandler.navigateTo(SettingsActivity.this, NavigationHandler.SPLASH);
                    }
                }, R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, final boolean isChecked) {
        if (buttonView == cbStopSendingAnalytics) {
            if(isChecked){
                DialogUtils.showDialog(this, R.string.atention, R.string.analytics_warn, R.string.keep_sending, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cbStopSendingAnalytics.setChecked(false);
                    }
                }, R.string.do_not_send, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SettingsHandler.setSendAnalytics(SettingsActivity.this, false);
                        AnalyticsHandler.getInstance(SettingsActivity.this).enableSendAnalyticsEvents(false);
                    }
                });
            }else {
                SettingsHandler.setSendAnalytics(SettingsActivity.this, true);
                AnalyticsHandler.getInstance(SettingsActivity.this).enableSendAnalyticsEvents(true);
            }




        } else if (buttonView == cbStopSendingNotifs) {
            SettingsHandler.setSendNotifs(this, !isChecked);
            AnalyticsHandler.getInstance(this).setUserProperty(AnalyticsHandler.UserProperty.PROPERTY_NOTIFS, String.valueOf(!isChecked));
            if (isChecked) {
            } else {

            }

        }
    }
}
