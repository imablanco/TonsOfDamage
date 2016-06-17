package com.ablanco.tonsofdamage.champions;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
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
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import im.ene.lab.toro.Toro;

public class ChampionDetailActivity extends BaseActivity {

    public static final String EXTRA_CHAMPION_ID = "championId";

    private static final int CHAMPION_OVERVIEW = 0;
    private static final int CHAMPION_ABILITIES = 1;
    private static final int CHAMPION_RECOMMENDED = 2;
    private static final int CHAMPION_SKINS = 3;

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
    private int championId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_champion_detail);
        ButterKnife.bind(this);

        Toro.attach(this);

        setSupportActionBar(toolbar);
        collapsingToolbarLayout.setTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        championId = getIntent().getIntExtra(EXTRA_CHAMPION_ID, -1);

        if(championId >= 0){
            Teemo.getInstance(this).getStaticDataHandler().getChampionById(championId, SettingsHandler.getLanguage(this), null,
                    Utils.buildStaticQueryParams(StaticAPIQueryParams.Champions.ALL), new ServiceResponseListener<ChampionDto>() {
                        @Override
                        public void onResponse(ChampionDto response) {
                            if(getSupportActionBar() != null){
                                getSupportActionBar().setTitle(response.getName());
                                getSupportActionBar().setSubtitle(response.getTitle());
                                Glide.with(ChampionDetailActivity.this).load(ImageUris.getChampionSplashArt(response.getKey(), response.getSkins().get(0).getNum())).into(championSplashImage);

                                mChampion = response;
                                setUpViewPager();
                            }
                        }

                        @Override
                        public void onError(TeemoException e) {

                        }
                    });
        }

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_CHAMPION_DETAIL;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toro.detach(this);
        ButterKnife.unbind(this);
    }

    private void setUpViewPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()));
        pager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.champion_detail, menu);
        MenuItem m = menu.findItem(R.id.action_like);
        if(m != null){
            m.setIcon(SettingsHandler.isChampionMarkedAsFavorite(ChampionDetailActivity.this, championId) ? R.drawable.ic_like_filled : R.drawable.ic_like);
            m.setTitle(SettingsHandler.isChampionMarkedAsFavorite(ChampionDetailActivity.this, championId) ? R.string.remove_from_favorites : R.string.add_to_favorites);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }else if(id == R.id.action_like){
            if(!SettingsHandler.isChampionMarkedAsFavorite(ChampionDetailActivity.this, championId)){
                SettingsHandler.addFavoriteChampion(ChampionDetailActivity.this, championId);
                item.setIcon(R.drawable.ic_like_filled);
                item.setTitle(R.string.remove_from_favorites);
            }else {
                SettingsHandler.removeFavoriteChampion(ChampionDetailActivity.this, championId);
                item.setIcon(R.drawable.ic_like);
                item.setTitle(R.string.add_to_favorites);
            }
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
                case CHAMPION_ABILITIES:
                    return ChampionSpellsFragment.newInstance(mChampion);
                case CHAMPION_RECOMMENDED:
                    return ChampionRecommendedItemsFragment.newInstance(mChampion);
                case CHAMPION_SKINS:
                    return ChampionSkinsFragment.newInstance(mChampion);
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position){
                case CHAMPION_OVERVIEW:default:
                    return getString(R.string.overview);
                case CHAMPION_ABILITIES:
                    return getString(R.string.champion_abilities);
                case CHAMPION_RECOMMENDED:
                    return getString(R.string.champion_recommended);
                case CHAMPION_SKINS:
                    return getString(R.string.champion_skins);
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}