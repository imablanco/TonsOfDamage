package com.ablanco.tonsofdamage.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.MasteriesActivity;
import com.ablanco.tonsofdamage.summoner.RunesActivity;
import com.ablanco.tonsofdamage.utils.Constants;
import com.ablanco.tonsofdamage.utils.HomeErrorUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.ablanco.tonsofdamage.views.ProfileHeaderNavigationView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import hotchemi.android.rate.AppRate;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final static int HOME = 0;
    public final static int CHAMPIONS = 1;
    public final static int ITEMS = 2;
    public final static int SUMMONERS = 3;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.nav_view)
    NavigationView mNavigationView;
    @Bind(R.id.pager)
    ViewPager pager;

    private BottomBar mBottomBar;

    private boolean backEnabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(null);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavigationView.setNavigationItemSelectedListener(this);

        mDrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                showCaseProfileView();
            }
        });

        HomeErrorUtils.init();

        monitorRateApp();

        ProfileHeaderNavigationView profileHeaderNavigationView = new ProfileHeaderNavigationView(this);
        mNavigationView.addHeaderView(profileHeaderNavigationView);
        profileHeaderNavigationView.update();

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

        if (mBottomBar.findViewById(R.id.bb_bottom_bar_background_view) != null) {
            mBottomBar.findViewById(R.id.bb_bottom_bar_background_view).setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        mBottomBar.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabSelected(int i) {
                toolbar.setTitle(adapter.getPageTitle(i));
                pager.setCurrentItem(i, false);
                HomeErrorUtils.getInstance().setCurrentPage(i);
                Utils.hideKeyBoard(HomeActivity.this);

                //send section navigation event
                String section;
                switch (i) {
                    case HOME:
                    default:
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

        startsShowCaseFlow();

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_HOMEACTIVIY;
    }

    @Override
    public String getNavigationItemId() {
        return null;
    }

    private void monitorRateApp() {
        AppRate.with(this)
                .setInstallDays(2)
                .setCancelable(true)
                .monitor();

        // Show a dialog if meets conditions
        AppRate.showRateDialogIfMeetsConditions(this);

    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START) && backEnabled) {
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

        } else if (id == R.id.nav_settings) {
            NavigationHandler.navigateTo(this, NavigationHandler.SETTINGS);
        } else if (id == R.id.nav_contact) {
            String defaultMessage = "Phone model: " + Utils.getDeviceModel() + "\n" +
                    "Phone OS Version: " + Utils.getDeviceOS() + "\n" +
                    "Language: " + SettingsHandler.getLanguage(this) + "\n" +
                    "App Version: " + Utils.getAppVersion(this);

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

            switch (position) {
                case HOME:
                default:
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
            switch (position) {
                case HOME:
                default:
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



    /**
     * Show case methods
     */
    public void startsShowCaseFlow() {
        new MaterialIntroView.Builder(this)
                .enableFadeAnimation(false)
                .enableIcon(false)
                .enableDotAnimation(true)
                .setMaskColor(ContextCompat.getColor(this, R.color.black_alpha_more))
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.NORMAL)
                .performClick(false)
                .setListener(new MaterialIntroListener() {
                    @Override
                    public void onUserClicked(String s) {
                        showCaseMenu();
                    }
                })
                .setTextColor(ContextCompat.getColor(this, R.color.text_color))
                .setInfoText(getString(R.string.showcase_step1))
                .setTarget(mBottomBar.getBar())
                .setUsageId(Constants.ShowCase.SHOW_CASE_FAV) //THIS SHOULD BE UNIQUE ID
                .show();
    }

    private void showCaseMenu() {
        new MaterialIntroView.Builder(this)
                .enableFadeAnimation(true)
                .enableIcon(false)
                .setMaskColor(ContextCompat.getColor(this, R.color.black_alpha_more))
                .setFocusGravity(FocusGravity.CENTER)
                .setFocusType(Focus.MINIMUM)
                .performClick(true)
                .setTextColor(ContextCompat.getColor(this, R.color.text_color))
                .setInfoText(getString(R.string.showcase_step2))
                .setTarget(getNavButtonView(toolbar))
                .setUsageId(Constants.ShowCase.SHOW_CASE_MENU) //THIS SHOULD BE UNIQUE ID
                .show();

        backEnabled = false;
    }


    public void showCaseProfileView() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {

            new MaterialIntroView.Builder(this)
                    .enableFadeAnimation(true)
                    .enableIcon(false)
                    .setMaskColor(ContextCompat.getColor(this, R.color.black_alpha_more))
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.NORMAL)
                    .performClick(true)
                    .setListener(new MaterialIntroListener() {
                        @Override
                        public void onUserClicked(String s) {
                            backEnabled = true;
                        }
                    })
                    .setTextColor(ContextCompat.getColor(this, R.color.text_color))
                    .setInfoText(getString(R.string.showcase_step3))
                    .setTarget(((ProfileHeaderNavigationView) mNavigationView.getHeaderView(0)).getHeaderImage())
                    .setUsageId(Constants.ShowCase.SHOW_CASE_PROFILE) //THIS SHOULD BE UNIQUE ID
                    .show();

        }
    }

    private View getNavButtonView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return toolbar.getChildAt(i);

        return toolbar;
    }
}
