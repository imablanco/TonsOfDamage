package com.ablanco.tonsofdamage.summoner;

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
    int totalDamageDealt = 0;
    int phDamageDealt = 0;
    int mgDamageDealt = 0;
    int totalDamageTaken = 0;
    int healingDone = 0;
    int largestCriticalStrike = 0;


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
    @Bind(R.id.tv_season)
    TextView mTvSeason;
    @Bind(R.id.tv_total_damage)
    TextView mTvTotalDamage;
    @Bind(R.id.tv_physical_damage_dealt)
    TextView mTvPhysicalDamageDealt;
    @Bind(R.id.tv_magical_damage_dealt)
    TextView mTvMagicalDamageDealt;
    @Bind(R.id.tv_damage_taken)
    TextView mTvDamageTaken;
    @Bind(R.id.tv_total_healing)
    TextView mTvTotalHealing;
    @Bind(R.id.tv_larges_critical_strike)
    TextView mTvLargesCriticalStrike;
    @Bind(R.id.dv_dmg)
    DecoView mDvDamage;
    @Bind(R.id.tv_no_stats)
    TextView tvNoStats;

    private boolean alreadyAnimated = false;
    private boolean mDataLoaded = false;


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
                if(cardViewStats != null && mTvSeason != null){
                    mDataLoaded = true;
                    cardViewStats.setVisibility(View.VISIBLE);
                    mTvSeason.setText(response.getSeason());
                    for (ChampionStats stats : response.getChampions()) {
                        if (stats.getId() == 0) {
                            fillGamesPlayedSection(stats.getStats());
                            fillKillsSection(stats.getStats());
                            fillDamageSection(stats.getStats());
                        }
                    }
                }

            }

            @Override
            public void onError(TeemoException e) {
                tvNoStats.setVisibility(View.VISIBLE);
            }
        });
    }

    private void fillDamageSection(AggregatedStats stats) {
        if (stats.getTotalDamageDealt() != null) {
            totalDamageDealt = stats.getTotalDamageDealt();
        }

        if (stats.getTotalPhysicalDamageDealt() != null) {
            phDamageDealt = stats.getTotalPhysicalDamageDealt();
        }

        if (stats.getTotalMagicDamageDealt() != null) {
            mgDamageDealt = stats.getTotalMagicDamageDealt();
        }

        if (stats.getTotalDamageTaken() != null) {
            totalDamageTaken = stats.getTotalDamageTaken();
        }

        if (stats.getTotalHeal() != null) {
            healingDone = stats.getTotalHeal();
        }

        if (stats.getMaxLargestCriticalStrike() != null) {
            largestCriticalStrike = stats.getMaxLargestCriticalStrike();
        }

        mTvTotalDamage.setText(String.valueOf(totalDamageDealt));
        mTvPhysicalDamageDealt.setText(String.valueOf(phDamageDealt));
        mTvMagicalDamageDealt.setText(String.valueOf(mgDamageDealt));
        mTvDamageTaken.setText(String.valueOf(totalDamageTaken));
        mTvTotalHealing.setText(String.valueOf(healingDone));
        mTvLargesCriticalStrike.setText(String.valueOf(largestCriticalStrike));
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

    private void fillKillsSection(AggregatedStats stats) {
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

        if (!alreadyAnimated && mDataLoaded) {
            alreadyAnimated = true;
            int index;

            if (totalGamesPlayed > 0) {
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

            if (totalKills > 0) {
                //kills
                SeriesItem totalKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.colorAccent));
                SeriesItem normalKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.red), "%.0f%% Normal");
                SeriesItem doubleKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.green), "%.0f%% Double");
                SeriesItem tripleKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.blue), "%.0f%% Triple");
                SeriesItem quadraKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.magenta), "%.0f%% Quadra");
                SeriesItem pentaKillsItem = createSeries(totalKills, ContextCompat.getColor(getActivity(), R.color.white), "%.0f%% Penta");

                index = mDvKills.addSeries(totalKillsItem);
                startAnimation(mDvKills, index, 600, 1500, 800, totalKills);

                List<AnimationHelper> mOrderedKills = Arrays.asList(new AnimationHelper(normalKills, normalKillsItem),
                        new AnimationHelper(doubleKills, doubleKillsItem),
                        new AnimationHelper(tripleKills, tripleKillsItem),
                        new AnimationHelper(quadraKills, quadraKillsItem),
                        new AnimationHelper(pentaKills, pentaKillsItem));


                Collections.sort(mOrderedKills, new Comparator<AnimationHelper>() {
                    @Override
                    public int compare(AnimationHelper lhs, AnimationHelper rhs) {
                        return rhs.getValue().compareTo(lhs.getValue());
                    }
                });

                for (int i = 0; i < mOrderedKills.size(); i++) {
                    long delay = 900 + 300 * i;
                    index = mDvKills.addSeries(mOrderedKills.get(i).getItem());
                    startAnimation(mDvKills, index, delay, 1500, 800, mOrderedKills.get(i).getValue());
                }

            }

            if(totalDamageDealt > 0){
                //damage

                int greaterValue = totalDamageDealt > totalDamageTaken ? totalDamageDealt : totalDamageTaken;
                SeriesItem totalDamageDealtItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.colorAccent));
                SeriesItem phDamageDealtItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.red));
                SeriesItem mgDamageDealtItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.green));
                SeriesItem totalDamageTakenItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.blue));
                SeriesItem healingDoneItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.white));
                SeriesItem largesCriticalStrikeItem = createSeries(greaterValue, ContextCompat.getColor(getActivity(), R.color.green_bright));

                List<AnimationHelper> mOrderedDamages = Arrays.asList(new AnimationHelper(totalDamageDealt, totalDamageDealtItem),
                        new AnimationHelper(phDamageDealt, phDamageDealtItem),
                        new AnimationHelper(mgDamageDealt, mgDamageDealtItem),
                        new AnimationHelper(totalDamageTaken, totalDamageTakenItem),
                        new AnimationHelper(healingDone, healingDoneItem),
                        new AnimationHelper(largestCriticalStrike, largesCriticalStrikeItem));


                Collections.sort(mOrderedDamages, new Comparator<AnimationHelper>() {
                    @Override
                    public int compare(AnimationHelper lhs, AnimationHelper rhs) {
                        return rhs.getValue().compareTo(lhs.getValue());
                    }
                });

                for (int i = 0; i < mOrderedDamages.size(); i++) {
                    long delay = 900 + 300 * i;
                    index = mDvDamage.addSeries(mOrderedDamages.get(i).getItem());
                    startAnimation(mDvDamage, index, delay, 1500, 800, mOrderedDamages.get(i).getValue());
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

    protected static class AnimationHelper {


        Integer value;
        SeriesItem item;

        public AnimationHelper(Integer value, SeriesItem item) {
            this.value = value;
            this.item = item;
        }

        public SeriesItem getItem() {
            return item;
        }

        public Integer getValue() {
            return value;
        }

    }

}
