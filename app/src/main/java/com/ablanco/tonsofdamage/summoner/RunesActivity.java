package com.ablanco.tonsofdamage.summoner;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.BasicDataStatsDto;
import com.ablanco.teemo.model.staticdata.RuneDto;
import com.ablanco.teemo.model.staticdata.RuneListDto;
import com.ablanco.teemo.model.summoners.RunePage;
import com.ablanco.teemo.model.summoners.RunePages;
import com.ablanco.teemo.model.summoners.RuneSlot;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.RuneListNameAdapter;
import com.ablanco.tonsofdamage.adapter.RunesAdapter;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.AnimationUtils;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class RunesActivity extends BaseActivity {

    public static final String EXTRA_SUMMONER_ID = "extra_summoner_id";
    public final static float STATS_LAYOUT_HEIGHT = SizeUtils.convertDpToPixel(200);

    @Bind(R.id.spinner_page_name)
    Spinner mSpinnerPageName;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.loading)
    ProgressBar mLoading;
    @Bind(R.id.cv_combined_stats)
    View cvCombinedStats;
    @Bind(R.id.fab_show_bs)
    FloatingActionButton fabExpand;
    @Bind(R.id.fab_collapse)
    FloatingActionButton fabCollapse;
    @Bind(R.id.recycler_view_stats)
    RecyclerView mRecyclerViewStats;

    private StatsAdapter mStatsAdapter;
    private Map<Long, List<RuneProxyModel>> mProxyRunes = new HashMap<>();
    private Map<String, RuneDto> mRunes = new HashMap<>();
    private RuneListNameAdapter spinnerAdapter;
    private List<RunePageProxyModel> mRunePages = new ArrayList<>();

    private Map<Long, Map<String, Double>> mCombinedStatsByRunePageMap = new HashMap<>();

    private ValueAnimator expandAnimator = ValueAnimator.ofFloat(STATS_LAYOUT_HEIGHT, 0);
    private ValueAnimator collapseAnimator = ValueAnimator.ofFloat(0, STATS_LAYOUT_HEIGHT);
    private boolean mExpanded = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_runes);
        ButterKnife.bind(this);

        final long mId = getIntent().getLongExtra(EXTRA_SUMMONER_ID, 0);

        if (mId > 0) {

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            final RunesAdapter adapter = new RunesAdapter(this);
            mRecyclerViewStats.setLayoutManager(new LinearLayoutManager(this));

            mStatsAdapter = new StatsAdapter();
            mRecyclerViewStats.setAdapter(mStatsAdapter);

            fabExpand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (collapseAnimator.isRunning()) {
                        collapseAnimator.cancel();
                    }
                    expandAnimator.start();
                }
            });

            fabCollapse.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (expandAnimator.isRunning()) {
                        expandAnimator.cancel();
                    }
                    collapseAnimator.start();
                }
            });

            expandAnimator.setInterpolator(new DecelerateInterpolator());
            expandAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    cvCombinedStats.setTranslationY((Float) animation.getAnimatedValue());
                }
            });

            expandAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isDestroyed()) {
                        fabExpand.setVisibility(View.INVISIBLE);
                        AnimationUtils.revealView(fabCollapse);
                        mExpanded = true;
                    }

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            collapseAnimator.setInterpolator(new AccelerateInterpolator());
            collapseAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    cvCombinedStats.setTranslationY((Float) animation.getAnimatedValue());
                }
            });

            collapseAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!isDestroyed()) {
                        fabCollapse.setVisibility(View.INVISIBLE);
                        AnimationUtils.revealView(fabExpand);
                        mExpanded = false;
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


            mRecyclerView.setAdapter(adapter);
            spinnerAdapter = new RuneListNameAdapter(this, mRunePages);
            mSpinnerPageName.setAdapter(spinnerAdapter);
            mSpinnerPageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setRunes(mProxyRunes.get(mRunePages.get(position).getPage().getId()));
                    try {
                        buildCombinedStats(mRunePages.get(position));

                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Teemo.getInstance(this).getStaticDataHandler().getRunes(SettingsHandler.getLanguage(this), null, new StaticAPIQueryParams.StaticQueryParamsBuilder()
                    .include(StaticAPIQueryParams.Runes.image)
                    .include(StaticAPIQueryParams.Runes.stats).build(), new ServiceResponseListener<RuneListDto>() {
                @Override
                public void onResponse(RuneListDto response) {

                    mRunes.putAll(response.getData());

                    Teemo.getInstance(RunesActivity.this).getSummonersHandler().getSummonerRunePages(String.valueOf(mId), new ServiceResponseListener<RunePages>() {
                        @SuppressLint("UseSparseArrays")
                        @Override
                        public void onResponse(RunePages response) {
                            HashMap<Integer, Integer> mRuneCountMap = new HashMap<>();
                            for (RunePage runePage : response.getPages()) {
                                mRunePages.add(new RunePageProxyModel(runePage));

                                mRuneCountMap.clear();

                                for (RuneSlot runeSlot : runePage.getSlots()) {
                                    if (mRuneCountMap.get(runeSlot.getRuneId()) == null) {
                                        mRuneCountMap.put(runeSlot.getRuneId(), 0);
                                    }
                                    mRuneCountMap.put(runeSlot.getRuneId(), mRuneCountMap.get(runeSlot.getRuneId()) + 1);
                                }

                                RuneDto dto;
                                mProxyRunes.put(runePage.getId(), new ArrayList<RuneProxyModel>());
                                for (Integer runeId : mRuneCountMap.keySet()) {
                                    dto = mRunes.get(String.valueOf(runeId));
                                    if (dto != null) {
                                        mProxyRunes.get(runePage.getId()).add(new RuneProxyModel(dto, mRuneCountMap.get(runeId)));
                                    }
                                }

                            }


                            spinnerAdapter.notifyDataSetChanged();

                            for (int i = 0; i < response.getPages().size(); i++) {
                                if (response.getPages().get(i) != null && response.getPages().get(i).isCurrent() != null && response.getPages().get(i).isCurrent()) {
                                    mSpinnerPageName.setSelection(i, false);
                                }
                            }

                            mLoading.setVisibility(View.GONE);

                        }

                        @Override
                        public void onError(TeemoException e) {
                            // TODO: 28/05/2016 handler error
                            mLoading.setVisibility(View.GONE);

                        }
                    });
                }

                @Override
                public void onError(TeemoException e) {
                    // TODO: 28/05/2016 handler error
                    mLoading.setVisibility(View.GONE);
                }
            });
        } else {
            finish();
        }


    }

    private void buildCombinedStats(RunePageProxyModel runePage) throws NoSuchFieldException, IllegalAccessException {

        if (mCombinedStatsByRunePageMap.get(runePage.getPage().getId()) != null) {
            buildStats(mCombinedStatsByRunePageMap.get(runePage.getPage().getId()));
        } else {
            HashMap<String, Double> summarizedStats = new HashMap<>();
            mCombinedStatsByRunePageMap.put(runePage.getPage().getId(), summarizedStats);

            BasicDataStatsDto statsDto;
            Double field;

            if (mProxyRunes.get(runePage.getPage().getId()) != null) {

                for (RuneProxyModel runeProxyModel : mProxyRunes.get(runePage.getPage().getId())) {
                    statsDto = runeProxyModel.getRuneDto().getStats();

                    for (Field f : BasicDataStatsDto.class.getDeclaredFields()) {
                        f.setAccessible(true);
                        field = (Double) f.get(statsDto);
                        if (field != null) {
                            field = field * runeProxyModel.getCount();

                            if (summarizedStats.get(f.getName()) == null) {
                                summarizedStats.put(f.getName(), field);
                            } else {
                                summarizedStats.put(f.getName(), summarizedStats.get(f.getName()) != null ? summarizedStats.get(f.getName()) + field : field);
                            }
                        }
                    }
                }
            }

            buildStats(mCombinedStatsByRunePageMap.get(runePage.getPage().getId()));
        }

    }

    private void buildStats(Map<String, Double> stats) {

        List<RuneStat> runeStats = new ArrayList<>();
        for (String s : stats.keySet()) {
            runeStats.add(new RuneStat(s, stats.get(s)));
        }

        mStatsAdapter.addStats(runeStats);

        if (!mExpanded) {
            if (collapseAnimator.isRunning()) {
                collapseAnimator.cancel();
            }
            expandAnimator.start();
        }

    }

    @Override
    public void onBackPressed() {
        if (mExpanded) {
            if (expandAnimator.isRunning()) {
                expandAnimator.cancel();
            }
            collapseAnimator.start();
        } else {
            super.onBackPressed();
        }
    }

    private class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.StatViewHolder> {

        private List<RuneStat> mStats = new ArrayList<>();

        @Override
        public StatsAdapter.StatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new StatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rune_stat, parent, false));
        }

        @Override
        public void onBindViewHolder(StatsAdapter.StatViewHolder holder, int position) {
            RuneStat stat = mStats.get(position);

            ((TextView) holder.itemView).setText(String.format("%s %s", ResourcesHandler.getInstance(RunesActivity.this).getResourceForKey(stat.getStatName()), String.format(Locale.getDefault(), "%.2f", stat.getStatValue())));
        }

        @Override
        public int getItemCount() {
            return mStats.size();
        }

        public void addStats(List<RuneStat> stats) {
            this.mStats = stats;
            notifyDataSetChanged();
        }

        public class StatViewHolder extends RecyclerView.ViewHolder {

            public StatViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
