package com.ablanco.tonsofdamage.ui.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.featuredgames.FeaturedGameInfo;
import com.ablanco.teemo.model.featuredgames.FeaturedGames;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 25/05/2016.
 * TonsOfDamage
 */
public class FeaturedGamesPlaceholder extends HomePlaceholder {

    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.circleIndicator)
    InkPageIndicator mCircleIndicator;
    @Bind(R.id.loading)
    View mLoading;

    private List<FeaturedGameInfo> mFeaturedGameInfos = new ArrayList<>();

    public FeaturedGamesPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPager.setAdapter(new FeaturedGamesPagerAdapter());
        setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

    }

    @Override
    public int getLayout() {
        return R.layout.ph_featured_games;
    }

    @Override
    public void update() {
        mLoading.setVisibility(VISIBLE);
        Teemo.getInstance(getContext()).getFeaturedGamesHandler().getFeaturedGames(new ServiceResponseListener<FeaturedGames>() {
            @Override
            public void onResponse(FeaturedGames response) {
                mLoading.setVisibility(GONE);
                mFeaturedGameInfos.clear();
                mFeaturedGameInfos.addAll(response.getGameList());
                mPager.getAdapter().notifyDataSetChanged();
                mCircleIndicator.setViewPager(mPager);
                mPager.setOffscreenPageLimit(response.getGameList().size() - 1);
            }

            @Override
            public void onError(TeemoException e) {
            }
        });
    }

    private class FeaturedGamesPagerAdapter extends PagerAdapter{
        @Override
        public int getCount() {
            return mFeaturedGameInfos.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = new FeaturedGamesPlaceHolderItem(getContext(), mFeaturedGameInfos.get(position));
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
