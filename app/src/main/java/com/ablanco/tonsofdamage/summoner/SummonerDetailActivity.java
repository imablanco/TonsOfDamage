package com.ablanco.tonsofdamage.summoner;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.ErrorUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummonerDetailActivity extends AppCompatActivity {

    private final static int PAGE_OVERVIEW = 0;
    private final static int PAGE_MATCH_LIST = 1;
    private final static int PAGE_STATS = 2;
    private final static int PAGE_MASTERIES = 3;

    public static final String EXTRA_ID = "extra_id";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.pager)
    ViewPager mPager;

    private List<Fragment> mPages = new ArrayList<>();
    private long mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_detail);
        ButterKnife.bind(this);

        mId = getIntent().getLongExtra(EXTRA_ID, 0);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mId != 0){
            setUp();
        }else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.summoner_detail, menu);
        MenuItem m = menu.findItem(R.id.action_like);
        if(m != null){
            m.setIcon(SettingsHandler.isSummonerMarkedAsFavorite(this, mId) ? R.drawable.ic_like_filled : R.drawable.ic_like);
            m.setTitle(SettingsHandler.isSummonerMarkedAsFavorite(this, mId) ? R.string.remove_from_favorites : R.string.add_to_favorites);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }else if(id == R.id.action_like){
            if(!SettingsHandler.isSummonerMarkedAsFavorite(this, mId)){
                SettingsHandler.addFavoriteSummoner(this, mId);
                item.setIcon(R.drawable.ic_like_filled);
                item.setTitle(R.string.remove_from_favorites);
            }else {
                SettingsHandler.removeFavoriteSummoner(this, mId);
                item.setIcon(R.drawable.ic_like);
                item.setTitle(R.string.add_to_favorites);
            }
        } else if(id == R.id.action_masteries){
            Bundle b = new Bundle();
            b.putLong(MasteriesActivity.EXTRA_SUMMONER_ID, mId);
            NavigationHandler.navigateTo(this, NavigationHandler.MASTERIES_DETAIL, b);

        } else if(id == R.id.action_runes){}



        return true;
    }


    private void setUp(){
        Teemo.getInstance(this).getSummonersHandler().getSummonerById(String.valueOf(mId), new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(Summoner response) {
                if(!isFinishing()){
                    mToolbar.setTitle(response.getName());

                    mPages.add(SummonerOverviewFragment.newInstance(mId));
                    mPages.add(RecentGamesFragment.newInstance(mId));
                    mPages.add(SummonerStatisticsFragment.newInstance(mId));

                    mPager.setAdapter(new SummonerDetailPagerAdapter());
                    mPager.setOffscreenPageLimit(mPages.size() - 1);
                    mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                        @Override
                        public void onPageSelected(int position) {
                            if(position == PAGE_STATS){
                                ((SummonerStatisticsFragment)mPages.get(PAGE_STATS)).animateViews();
                            }
                        }
                    });
                    mTabLayout.setupWithViewPager(mPager);
                }

            }

            @Override
            public void onError(TeemoException e) {
                if(!isFinishing()){
                    ErrorUtils.showPersistentError(mPager, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setUp();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    public class SummonerDetailPagerAdapter extends FragmentPagerAdapter{

        public SummonerDetailPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case PAGE_OVERVIEW:default:
                    return getString(R.string.overview);
                case PAGE_MATCH_LIST:
                    return getString(R.string.recent_games);
                case PAGE_STATS:
                    return getString(R.string.stats);
            }
        }

        @Override
        public Fragment getItem(int position) {
            return mPages.get(position);
        }

        @Override
        public int getCount() {
            return mPages.size();
        }
    }
}
