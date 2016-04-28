package com.ablanco.tonsofdamage.ui.activities;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.DownloadsHandler;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChampionSkinDetailActivity extends AppCompatActivity {

    public final static int RC_WRITE_PERMISSON = 201;

    public final static String EXTRA_SKIN_NAME = "skin_name";
    public final static String EXTRA_SKIN_URL = "skin_url";


    @Bind(R.id.img)
    ImageView mImg;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private String skinUrl;
    private String skinName;

    private long mDownloadId;

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                long downloadId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if(downloadId == mDownloadId){
                    Snackbar.make(mImg, R.string.download_completed, Snackbar.LENGTH_SHORT).setAction(R.string.show, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showDownload();
                        }
                    }).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_champion_skin_detail_activity);
        ButterKnife.bind(this);

        skinName = getIntent().getStringExtra(EXTRA_SKIN_NAME);
        skinUrl = getIntent().getStringExtra(EXTRA_SKIN_URL);

        if(skinName != null){
            setSupportActionBar(mToolbar);
            getSupportActionBar().setTitle(skinName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if(skinUrl != null){
            Glide.with(this).load(skinUrl).into(mImg);
        }

        hideUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideUI();
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.champion_skin_detail, menu);
        return skinUrl != null && skinName != null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }else if(id == R.id.action_download){
            if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
               mDownloadId =  DownloadsHandler.getInstance(getApplicationContext()).requestDownLoad(skinName, getString(R.string.downloading),skinUrl, skinName.concat(".png"));
            }else {
                ActivityCompat.requestPermissions(ChampionSkinDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE_PERMISSON);
            }
        }
        return true;
    }

    public void showDownload() {
        Intent i = new Intent();
        i.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
        startActivity(i);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        hideUI();
    }

    private void hideUI(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == RC_WRITE_PERMISSON)
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mDownloadId = DownloadsHandler.getInstance(getApplicationContext()).requestDownLoad(skinName, getString(R.string.downloading),skinUrl, skinName.concat(".png"));
        }else {
            Snackbar.make(mImg, R.string.permisson_needed, Snackbar.LENGTH_LONG).setAction(R.string.give_permisson, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(ChampionSkinDetailActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RC_WRITE_PERMISSON);
                }
            }).show();
        }
    }
}
