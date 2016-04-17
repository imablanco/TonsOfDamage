package com.ablanco.tonsofdamage.ui.activities;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.HomeContentHandler;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;
import com.ablanco.tonsofdamage.ui.fragments.ChampionsFragment;
import com.ablanco.tonsofdamage.ui.fragments.HomeFragment;
import com.ablanco.tonsofdamage.ui.fragments.ItemsFragment;
import com.ablanco.tonsofdamage.ui.fragments.SummonersFragment;
import com.ablanco.tonsofdamage.ui.views.ProfileHeaderNavigationView;
import com.ablanco.tonsofdamage.utils.Utils;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

        ProfileHeaderNavigationView profileHeaderNavigationView = new ProfileHeaderNavigationView(this);
        mNavigationView.addHeaderView(profileHeaderNavigationView);
        profileHeaderNavigationView.update();

        final HomeContentHandler mHomeContentHandler = new HomeContentHandler(getSupportFragmentManager());

        //// TODO: 10/04/2016 in future, save in DB
        ResourcesHandler.init(getApplicationContext());

        mBottomBar = BottomBar.attachShy((CoordinatorLayout) findViewById(R.id.coordinator_layout),
                findViewById(R.id.content), savedInstanceState);

        mBottomBar.noTabletGoodness();
        mBottomBar.noNavBarGoodness();

        pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
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
                toolbar.setTitle(mHomeContentHandler.getTitleForContent(getApplicationContext(), i));
                //mHomeContentHandler.showContent(i);
                pager.setCurrentItem(i);
                Utils.hideKeyBoard(HomeActivity.this);
            }

            @Override
            public void onTabReSelected(int i) {

            }
        });

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

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

   static class MyPagerAdapter extends FragmentPagerAdapter {
        public final static int HOME = 0;
        public final static int CHAMPIONS = 1;
        public final static int ITEMS = 2;
        public final static int SUMMONERS = 3;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
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
