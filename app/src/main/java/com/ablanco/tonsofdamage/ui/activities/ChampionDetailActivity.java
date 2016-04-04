package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.ui.fragments.ChampionOverviewFragment;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ChampionDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CHAMPION_ID = "championId";

    private static final int CHAMPION_OVERVIEW = 0;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.champion_img_splash)
    ImageView championSplashImage;

    @Bind(R.id.tab_layout)
    TabLayout tabLayout;

    @Bind(R.id.pager)
    ViewPager pager;


    private ChampionDto mChampion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_champion_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        collapsingToolbarLayout.setTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        int championId = getIntent().getIntExtra(EXTRA_CHAMPION_ID, -1);

        if(championId >= 0){
            Teemo.getInstance(this).getStaticDataHandler().getChampionById(championId, Locale.getDefault().toString(), null,
                    Utils.buildStaticQueryParams(StaticAPIQueryParams.Champions.ALL), new ServiceResponseListener<ChampionDto>() {
                        @Override
                        public void onResponse(ChampionDto response) {
                            getSupportActionBar().setTitle(response.getName());
                            getSupportActionBar().setSubtitle(response.getTitle());
                            Glide.with(ChampionDetailActivity.this).load(ImageUris.getChampionSplashArt(response.getName(), response.getSkins().get(0).getNum())).into(championSplashImage);

                            mChampion = response;
                            setUpViewPager();
                        }

                        @Override
                        public void onError(TeemoException e) {

                        }
                    });
        }

    }

    private void setUpViewPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.champion_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            NavigationHandler.navigateTo(ChampionDetailActivity.this, NavigationHandler.HOME);
        }else if(id == R.id.action_like){

        }

        return true;
    }

    class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

        public FragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case CHAMPION_OVERVIEW:default:
                    return ChampionOverviewFragment.newInstance(mChampion);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case CHAMPION_OVERVIEW:default:
                    return getString(R.string.champion_overview);
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    }
}
