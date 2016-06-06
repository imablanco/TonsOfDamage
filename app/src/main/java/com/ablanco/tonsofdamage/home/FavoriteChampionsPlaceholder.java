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

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.pixelcan.inkpageindicator.InkPageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
public class FavoriteChampionsPlaceholder extends CardView implements HomePlaceholder {

    @Bind(R.id.pager)
    ViewPager pager;
    @Bind(R.id.circleIndicator)
    InkPageIndicator circleIndicator;
    @Bind(R.id.tv_no_favorite_yet)
    TextView tvNoFavoriteYet;

    private List<List<String>> mPagedChampionIds = new ArrayList<>();

    public FavoriteChampionsPlaceholder(Context context) {
        this(context, null);
    }

    public FavoriteChampionsPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.ph_favorite_champions, this);
        ButterKnife.bind(this);

        this.setUseCompatPadding(true);
        this.setRadius(SizeUtils.convertDpToPixel(2));
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    @Override
    public void update() {
        mPagedChampionIds.clear();
        List<String> mChampionIds = SettingsHandler.getFavoriteChampions(getContext());

        List<String> row = new ArrayList<>();

        for (String mChampionId : mChampionIds) {

            row.add(mChampionId);

            if (row.size() % 3 == 0) {
                mPagedChampionIds.add(row);
                row = new ArrayList<>();
            }
        }

        if (!row.isEmpty()) {
            mPagedChampionIds.add(row);
        }


        if (!mPagedChampionIds.isEmpty()) {
            pager.setAdapter(new FavoriteChampionAdapter());
            circleIndicator.setViewPager(pager);
            tvNoFavoriteYet.setVisibility(GONE);
        } else {
            tvNoFavoriteYet.setVisibility(VISIBLE);
        }
    }

    class FavoriteChampionAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return mPagedChampionIds.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = new FavoriteChampionsPlaceholderItem(getContext(), mPagedChampionIds.get(position));
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
