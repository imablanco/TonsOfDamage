package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.fragments.RecentGamesFragment;
import com.ablanco.tonsofdamage.ui.fragments.SummonerOverviewFragment;
import com.ablanco.tonsofdamage.utils.ErrorUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SummonerDetailActivity extends AppCompatActivity {

    private final static int PAGE_OVERVIEW = 0;
    private final static int PAGE_MATCH_LIST = 1;

    public static final String EXTRA_ID = "extra_id";
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.pager)
    ViewPager mPager;

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


    private void setUp(){
        Teemo.getInstance(this).getSummonersHandler().getSummonerById(String.valueOf(mId), new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(Summoner response) {
                if(!isFinishing()){
                    mToolbar.setTitle(response.getName());
                    mPager.setAdapter(new SummonerDetailPagerAdapter());
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
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case PAGE_OVERVIEW:default:
                    return SummonerOverviewFragment.newInstance(mId);
                case PAGE_MATCH_LIST:
                    return RecentGamesFragment.newInstance(mId);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}