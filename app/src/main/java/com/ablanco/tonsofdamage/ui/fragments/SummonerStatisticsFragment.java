package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Season;
import com.ablanco.teemo.model.stats.AggregatedStats;
import com.ablanco.teemo.model.stats.ChampionStats;
import com.ablanco.teemo.model.stats.RankedStats;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.ErrorUtils;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 07/05/2016.
 * TonsOfDamage
 */
public class SummonerStatisticsFragment extends BaseSummonerDetailFragment {


    /**
     * Stats
     */
    int totalGamesPlayed = 0;
    int gamesWon = 0;
    int gamesLost = 0;
    int totalKills = 0;
    int normalKills = 0;
    int doubleKills = 0;
    int tripleKills = 0;
    int quadraKills = 0;
    int pentaKills = 0;

    @Bind(R.id.tv_games_played)
    TextView mTvGamesPlayed;
    @Bind(R.id.tv_games_won)
    TextView mTvGamesWon;
    @Bind(R.id.tv_games_lost)
    TextView mTvGamesLost;
    @Bind(R.id.dv_games)
    DecoView mDvGames;
    @Bind(R.id.tv_total_kills)
    TextView mTvTotalKills;
    @Bind(R.id.tv_normal_kills)
    TextView mTvNormalKills;
    @Bind(R.id.tv_double_kills)
    TextView mTvDoubleKills;
    @Bind(R.id.tv_triple_kills)
    TextView mTvTripleKills;
    @Bind(R.id.tv_quadra_kills)
    TextView mTvQuadraKills;
    @Bind(R.id.tv_penta_kills)
    TextView mTvPentaKills;
    @Bind(R.id.dv_kills)
    DecoView mDvKills;

    @Bind(R.id.cv_stats)
    View cardViewStats;
    private boolean alreadyAnimated = false;


    public static BaseSummonerDetailFragment newInstance(long summonerId) {
        BaseSummonerDetailFragment f = new SummonerStatisticsFragment();
        f.setSummonerId(summonerId);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summoner_stats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    private void loadData() {
        Teemo.getInstance(getActivity()).getStatsHandler().getRankedStatsBySummonerAndSeason(summonerId, Season.SEASON2016, new ServiceResponseListener<RankedStats>() {
            @Override
            public void onResponse(RankedStats response) {
                cardViewStats.setVisibility(View.VISIBLE);
                for (ChampionStats stats : response.getChampions()) {
                    if (stats.getId() == 0) {
                        fillGamesPlayedSection(stats.getStats());
                        fillKillsSection(stats.getStats());
                    }
                }
            }

            @Override
            public void onError(TeemoException e) {
                if(e.getCode() == TeemoException.CODE_NOT_FOUND){
                    ErrorUtils.showError(getView()); // TODO: 08/05/2016 more specific error
                }else {
                    ErrorUtils.showPersistentError(getView(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loadData();
                        }
                    });
                }
            }
        });
    }

    private void fillGamesPlayedSection(AggregatedStats stats) {

        if (stats.getTotalSessionsPlayed() != null) {
            totalGamesPlayed = stats.getTotalSessionsPlayed();
        }

        if (stats.getTotalSessionsWon() != null) {
            gamesWon = stats.getTotalSessionsWon();
        }

        if (stats.getTotalSessionsLost() != null) {
            gamesLost = stats.getTotalSessionsLost();
        }

        mTvGamesPlayed.setText(String.valueOf(totalGamesPlayed));
        mTvGamesWon.setText(String.valueOf(gamesWon));
        mTvGamesLost.setText(String.valueOf(gamesLost));
    }

    private void fillKillsSection(AggregatedStats stats){
        if (stats.getTotalChampionKills() != null) {
            totalKills = stats.getTotalChampionKills();
        }

        if (stats.getTotalDoubleKills() != null) {
            doubleKills = stats.getTotalDoubleKills();
        }

        if (stats.getTotalTripleKills() != null) {
            tripleKills = stats.getTotalTripleKills();
        }

        if (stats.getTotalQuadraKills() != null) {
            quadraKills = stats.getTotalQuadraKills();
        }

        if (stats.getTotalPentaKills() != null) {
            pentaKills = stats.getTotalPentaKills();
        }

        normalKills = totalKills - doubleKills - tripleKills - quadraKills - pentaKills;

        mTvTotalKills.setText(String.valueOf(totalKills));
        mTvNormalKills.setText(String.valueOf(normalKills));
        mTvDoubleKills.setText(String.valueOf(doubleKills));
        mTvTripleKills.setText(String.valueOf(tripleKills));
        mTvQuadraKills.setText(String.valueOf(quadraKills));
        mTvPentaKills.setText(String.valueOf(pentaKills));
    }


    public void animateViews() {

        if(!alreadyAnimated){
            alreadyAnimated = true;
            int index;

            if(totalGamesPlayed > 0){
                //animate games
                SeriesItem totalGamesItem = createSeries(totalGamesPlayed, ContextCompat.getColor(getContext(), R.color.colorAccent));
                SeriesItem wonGamesItem = createSeries(totalGamesPlayed, ContextCompat.getColor(getContext(), R.color.green), "%.0f%% Won");
                SeriesItem lostGamesItem = createSeries(totalGamesPlayed, ContextCompat.getColor(getContext(), R.color.red), "%.0f%% Lost");

                index = mDvGames.addSeries(totalGamesItem);
                startAnimation(mDvGames, index, 300, 1500, 800, totalGamesPlayed);

                if (gamesWon > gamesLost) {
                    index = mDvGames.addSeries(wonGamesItem);
                    startAnimation(mDvGames, index, 600, 1500, 800, gamesWon);
                    index = mDvGames.addSeries(lostGamesItem);
                    startAnimation(mDvGames, index, 900, 1500, 800, gamesLost);
                } else {
                    index = mDvGames.addSeries(lostGamesItem);
                    startAnimation(mDvGames, index, 600, 1500, 800, gamesLost);
                    index = mDvGames.addSeries(wonGamesItem);
                    startAnimation(mDvGames, index, 900, 1500, 800, gamesWon);
                }
            }

            if(totalKills > 0){
                //kills
                SeriesItem totalKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.colorAccent));
                SeriesItem normalKillsItem  = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.red),"%.0f%% Normal");
                SeriesItem doubleKillsItem  = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.green),"%.0f%% Double");
                SeriesItem tripleKillsItem  = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.blue),"%.0f%% Triple");
                SeriesItem quadraKillsItem  = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.magenta),"%.0f%% Quadra");
                SeriesItem pentaKillsItem  = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.white),"%.0f%% Penta");

                index = mDvKills.addSeries(totalKillsItem);
                startAnimation(mDvKills, index, 600, 1500, 800, totalKills);

                List<AnimationHelper> mOrderedKills = Arrays.asList(new AnimationHelper(normalKills, normalKillsItem, AnimationHelper.TYPE_NORMAL_KILLS),
                                                                    new AnimationHelper(doubleKills, doubleKillsItem, AnimationHelper.TYPE_DOUBLE_KILLS),
                                                                    new AnimationHelper(tripleKills, tripleKillsItem, AnimationHelper.TYPE_TRIPLE_KILLS),
                                                                    new AnimationHelper(quadraKills, quadraKillsItem, AnimationHelper.TYPE_QUADRA_KILLS),
                                                                    new AnimationHelper(pentaKills, pentaKillsItem, AnimationHelper.TYPE_PENTA_KILLS));


                Collections.sort(mOrderedKills, new Comparator<AnimationHelper>() {
                    @Override
                    public int compare(AnimationHelper lhs, AnimationHelper rhs) {
                        return rhs.getValue().compareTo(lhs.getValue());
                    }
                });

                for (int i = 0; i < mOrderedKills.size(); i++) {
                    long delay = 900 + 300*i;
                    index = mDvKills.addSeries(mOrderedKills.get(i).getItem());
                    startAnimation(mDvKills, index, delay,1500, 800, mOrderedKills.get(i).getValue());
                }

            }

        }

    }

    private SeriesItem createSeries(float max, int color, String label) {
        return new SeriesItem.Builder(color)
                .setRange(0, max, 0)
                .setInitialVisibility(false)
                /*.setSeriesLabel(new SeriesLabel.Builder(label)
                        .setColorBack(Color.argb(80, 0, 0, 0))
                        .setColorText(Color.argb(255, 255, 255, 255))
                        .setFontSize(10f)
                        .build())*/
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .build();
    }

    private SeriesItem createSeries(float max, int color) {
        return new SeriesItem.Builder(color)
                .setRange(0, max, 0)
                .setInitialVisibility(false)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .build();
    }

    private void startAnimation(DecoView view, int index, long spiralDelay, long spiralDuration, long fillDuration, float fillValue) {
        view.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(index)
                .setDuration(spiralDuration)
                .setDelay(spiralDelay)
                .build());

        view.addEvent(new DecoEvent.Builder(fillValue)
                .setIndex(index)
                .setDuration(fillDuration)
                .setDelay(spiralDelay + spiralDuration)
                .build());
    }

    protected static class AnimationHelper{
        public static int TYPE_NORMAL_KILLS = 0;
        public static int TYPE_DOUBLE_KILLS = 0;
        public static int TYPE_TRIPLE_KILLS = 0;
        public static int TYPE_QUADRA_KILLS = 0;
        public static int TYPE_PENTA_KILLS = 0;
        Integer value;
        int type;
        SeriesItem item;

        public AnimationHelper(Integer value, SeriesItem item, int type) {
            this.value = value;
            this.item = item;
            this.type = type;
        }

        public SeriesItem getItem() {
            return item;
        }

        public Integer getValue() {
            return value;
        }

        public int getType() {
            return type;
        }
    }

}
