package com.ablanco.tonsofdamage.home;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.tonsofdamage.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class HomeFragment extends BaseHomeFragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static long UPDATE_TIME = 1800000;
    private Long mLastUpdate;

    @Bind(R.id.ph_week_rotation)
    HomePlaceholder phWeekRotation;
    @Bind(R.id.ph_top_champion_mastery)
    HomePlaceholder phTopChampionMastery;
    @Bind(R.id.ph_featured_games)
    HomePlaceholder mPhFeaturedGames;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.ph_favorite_champ)
    HomePlaceholder phFavoriteChamp;
    @Bind(R.id.ph_favorite_summoner)
    HomePlaceholder phFavoriteSummoners;

    private boolean mIsInEditMode = false;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_starting_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeRefresh.setOnRefreshListener(this);
        mSwipeRefresh.setColorSchemeColors(ContextCompat.getColor(getActivity(), R.color.colorPrimary), ContextCompat.getColor(getActivity(), R.color.colorAccent));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (hasToUpdate()) {
            mLastUpdate = System.currentTimeMillis();
            phWeekRotation.update(true);
            phTopChampionMastery.update(true);
        }
        mPhFeaturedGames.update(false);
        phFavoriteChamp.update(false);
        phFavoriteSummoners.update(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit) {
            mIsInEditMode = !mIsInEditMode;
            if(mIsInEditMode){
                mPhFeaturedGames.onEditStarted();
                phFavoriteChamp.onEditStarted();
                phTopChampionMastery.onEditStarted();
                phFavoriteSummoners.onEditStarted();
                phWeekRotation.onEditStarted();
            }else {
                mPhFeaturedGames.onEditFinished();
                phFavoriteChamp.onEditFinished();
                phTopChampionMastery.onEditFinished();
                phFavoriteSummoners.onEditFinished();
                phWeekRotation.onEditFinished();
            }
        }

        return true;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private boolean hasToUpdate() {
        return mLastUpdate == null || System.currentTimeMillis() - mLastUpdate > UPDATE_TIME;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mLastUpdate = System.currentTimeMillis();
                mPhFeaturedGames.update(true);
                phFavoriteChamp.update(true);
                phTopChampionMastery.update(true);
                phFavoriteSummoners.update(true);
                phWeekRotation.update(true);
                mSwipeRefresh.setRefreshing(false);
            }
        }, 500);
    }
}
