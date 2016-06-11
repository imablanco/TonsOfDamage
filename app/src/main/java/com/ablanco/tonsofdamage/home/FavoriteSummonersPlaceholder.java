package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
public class FavoriteSummonersPlaceholder extends CardView implements HomePlaceholder {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.circleIndicator)
    InkPageIndicator circleIndicator;
    @Bind(R.id.tv_no_favorite_yet)
    TextView tvNoFavoriteYet;

    private List<List<Summoner>> mPagedSummoners = new ArrayList<>();
    private List<String> mFavSummonersIds = new ArrayList<>();

    public FavoriteSummonersPlaceholder(Context context) {
        this(context, null);
    }

    public FavoriteSummonersPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.ph_favorite_summoners, this);
        ButterKnife.bind(this);

        this.setUseCompatPadding(true);
        this.setRadius(SizeUtils.convertDpToPixel(2));
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

    }

    @Override
    public void update(boolean forceUpdate) {


        List<String> mSummonerIds = SettingsHandler.getFavoriteSummoners(getContext());

        if (mFavSummonersIds.size() != mSummonerIds.size() || forceUpdate) {

            mFavSummonersIds = mSummonerIds;

            mPagedSummoners.clear();
            pager.setAdapter(new FavoriteSummonersAdapter());

            if(!mFavSummonersIds.isEmpty()){

                Teemo.getInstance(getContext()).getSummonersHandler().getSummonersByIds(mFavSummonersIds, new ServiceResponseListener<Map<String, Summoner>>() {
                    @Override
                    public void onResponse(Map<String, Summoner> response) {

                        if (Utils.isContextValid(getContext())) {

                            List<Summoner> row = new ArrayList<>();

                            List<Summoner> summoners = new ArrayList<Summoner>(response.values());
                            Collections.sort(summoners, new Comparator<Summoner>() {
                                @Override
                                public int compare(Summoner lhs, Summoner rhs) {
                                    return lhs.getId().compareTo(rhs.getId());
                                }
                            });

                            for (Summoner summoner : summoners) {
                                row.add(summoner);

                                if (row.size() % 2 == 0) {
                                    mPagedSummoners.add(row);
                                    row = new ArrayList<>();
                                }
                            }


                            if (!row.isEmpty()) {
                                mPagedSummoners.add(row);
                            }

                            pager.setAdapter(new FavoriteSummonersAdapter());

                            if (!mPagedSummoners.isEmpty()) {
                                pager.setOffscreenPageLimit(mPagedSummoners.size() - 1);
                                circleIndicator.setViewPager(pager);
                                tvNoFavoriteYet.setVisibility(GONE);
                            } else {
                                tvNoFavoriteYet.setVisibility(VISIBLE);
                            }
                        }

                    }

                    @Override
                    public void onError(TeemoException e) {
                        tvNoFavoriteYet.setVisibility(VISIBLE);
                        tvNoFavoriteYet.setText(R.string.error_text);
                    }
                });
            } else {
                mPagedSummoners.clear();
                pager.setAdapter(new FavoriteSummonersAdapter());
                tvNoFavoriteYet.setVisibility(VISIBLE);
            }
        } else if(mSummonerIds.isEmpty()){
            mPagedSummoners.clear();
            pager.setAdapter(new FavoriteSummonersAdapter());
            tvNoFavoriteYet.setVisibility(VISIBLE);
        }


    }

    class FavoriteSummonersAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagedSummoners.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = new FavoriteSummonersPlaceholderItem(getContext(), mPagedSummoners.get(position));
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
