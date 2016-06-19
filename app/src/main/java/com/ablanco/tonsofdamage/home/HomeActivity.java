package com.ablanco.tonsofdamage.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.MasteriesActivity;
import com.ablanco.tonsofdamage.summoner.RunesActivity;
import com.ablanco.tonsofdamage.utils.HomeErrorUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.ablanco.tonsofdamage.views.ProfileHeaderNavigationView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import hotchemi.android.rate.AppRate;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static int HOME = 0;
    public final static int CHAMPIONS = 1;
    public final static int ITEMS = 2;
    public final static int SUMMONERS = 3;

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view) NavigationView mNavigationView;
    @Bind(R.id.pager)
    ViewPager pager;

    private BottomBar mBottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        HomeErrorUtils.init();

        monitorRateApp();

        ProfileHeaderNavigationView profileHeaderNavigationView = new ProfileHeaderNavigationView(this);
        mNavigationView.addHeaderView(profileHeaderNavigationView);
        profileHeaderNavigationView.update();

        //// TODO: 10/04/2016 in future, save in DB
        ResourcesHandler.init(getApplicationContext());



        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.coordinator_layout),
                findViewById(R.id.content), savedInstanceState);

        mBottomBar.noTabletGoodness();
        mBottomBar.noNavBarGoodness();
        final HomeContentPagerAdapter adapter = new HomeContentPagerAdapter();
        pager.setAdapter(adapter);
        pager.setOffscreenPageLimit(3);
        mBottomBar.setItems(
                new BottomBarTab(R.drawable.ic_home, R.string.title_home),
                new BottomBarTab(R.drawable.ic_champion, R.string.title_champions),
                new BottomBarTab(R.drawable.ic_items, R.string.title_items),
                new BottomBarTab(R.drawable.ic_person, getString(R.string.summoners))
        );


        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int i) {
                toolbar.setTitle(adapter.getPageTitle(i));
                pager.setCurrentItem(i,false);
                HomeErrorUtils.getInstance().setCurrentPage(i);
                Utils.hideKeyBoard(HomeActivity.this);

                //send section navigation event
                String section;
                switch (i){
                    case HOME:default:
                        section = AnalyticsHandler.CLASS_NAME_HOME_HOME;
                        break;
                    case CHAMPIONS:
                        section = AnalyticsHandler.CLASS_NAME_HOME_CHAMPIONS;
                        break;
                    case ITEMS:
                        section = AnalyticsHandler.CLASS_NAME_HOME_ITEMS;
                        break;
                    case SUMMONERS:
                        section = AnalyticsHandler.CLASS_NAME_HOME_SUMMONERS;
                        break;
                }

                AnalyticsHandler.getInstance(HomeActivity.this).trackScreenSectionNavigation(AnalyticsHandler.CLASS_NAME_HOMEACTIVIY, section, null);
            }

            @Override
            public void onTabReSelected(int i) {

            }
        });

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_HOMEACTIVIY;
    }

    @Override
    public String getNavigationItemId() {
        return null;
    }

    private void monitorRateApp(){
        AppRate.with(this)
                .setInstallDays(2)
                .setCancelable(true)
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

    }
    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        mBottomBar.onSaveInstanceState(outState);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_masteries) {
            Bundle bundle = new Bundle();
            bundle.putLong(MasteriesActivity.EXTRA_SUMMONER_ID, SettingsHandler.getSummoner(this));
            NavigationHandler.navigateTo(this, NavigationHandler.MASTERIES_DETAIL, bundle);
        } else if (id == R.id.nav_runes) {
            Bundle bundle = new Bundle();
            bundle.putLong(RunesActivity.EXTRA_SUMMONER_ID, SettingsHandler.getSummoner(this));
            NavigationHandler.navigateTo(this, NavigationHandler.RUNES_DETAIL, bundle);

        }else if(id == R.id.nav_settings){
            NavigationHandler.navigateTo(this, NavigationHandler.SETTINGS);
        } else if(id == R.id.nav_contact){
            String defaultMessage = "Phone model: " + Utils.getDeviceModel() + "\n"+
                    "Phone OS Version: " + Utils.getDeviceOS()+"\n"+
                    "Language: " + SettingsHandler.getLanguage(this) +"\n"+
                    "App Version: "+ Utils.getAppVersion(this);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("message/rfc822");
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"alvaro.bl91@gmail.com"}); // TODO: 11/6/16 change for correct email
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, defaultMessage);

            startActivity(Intent.createChooser(intent, getString(R.string.action_contact)));
        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    class HomeContentPagerAdapter extends FragmentPagerAdapter {

        public HomeContentPagerAdapter() {
            super(getSupportFragmentManager());
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;

            switch (position){
                case HOME:default:
                    fragment = HomeFragment.newInstance();
                    break;
                case CHAMPIONS:
                    fragment = ChampionsFragment.newInstance();
                    break;
                case ITEMS:
                    fragment = ItemsFragment.newInstance();
                    break;
                case SUMMONERS:
                    fragment = SummonersFragment.newInstance();
                    break;
            }

            return fragment;

        }

       @Override
       public CharSequence getPageTitle(int position) {
           switch (position){
               case HOME:default:
                   return getString(R.string.title_home);
               case CHAMPIONS:
                   return getString(R.string.title_champions);
               case ITEMS:
                   return getString(R.string.title_items);
               case SUMMONERS:
                   return getString(R.string.summoners);
           }
       }

       @Override
        public int getCount() {
            return 4;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
