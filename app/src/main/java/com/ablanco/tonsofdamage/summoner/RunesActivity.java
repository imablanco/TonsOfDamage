package com.ablanco.tonsofdamage.summoner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
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
public class RunesActivity extends AppCompatActivity {

    public static final String EXTRA_SUMMONER_ID = "extra_summoner_id";
    @Bind(R.id.spinner_page_name)
    Spinner mSpinnerPageName;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.loading)
    ProgressBar mLoading;
    @Bind(R.id.bottom_sheet)
    View bottomSheet;
    @Bind(R.id.fab_show_bs)
    FloatingActionButton fabShowBs;
    @Bind(R.id.ll_stats)
    LinearLayout llStats;

    private Map<Long, List<RuneProxyModel>> mProxyRunes = new HashMap<>();
    private Map<String, RuneDto> mRunes = new HashMap<>();
    private RuneListNameAdapter spinnerAdapter;
    private List<RunePageProxyModel> mRunePages = new ArrayList<>();

    private Map<Long, Map<String, Double>> mCombinedStatsByRunePageMap = new HashMap<>();
    private int dp3 = SizeUtils.convertDpToPixel(3);
    private BottomSheetBehavior viewBottomSheetBehavior;

    //private Map<Long, Map<Integer, Integer>> mRunesPerPage = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_runes);
        ButterKnife.bind(this);

        final long mId = getIntent().getLongExtra(EXTRA_SUMMONER_ID, 0);

        if (mId > 0) {

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            final RunesAdapter adapter = new RunesAdapter(this);

            viewBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

            fabShowBs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    viewBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });

            viewBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                        switch (newState){
                            case BottomSheetBehavior.STATE_EXPANDED:
                                fabShowBs.setVisibility(View.INVISIBLE);
                                break;
                            case BottomSheetBehavior.STATE_COLLAPSED:
                                AnimationUtils.revealView(fabShowBs);
                                break;
                        }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {

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
                                if(response.getPages().get(i) != null && response.getPages().get(i).isCurrent() != null && response.getPages().get(i).isCurrent()){
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
                            }else {
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

        llStats.removeAllViews();
        TextView textView;
        for (String key : stats.keySet()) {
            textView = new TextView(this);
            textView.setPadding(dp3, dp3, dp3, dp3);
            textView.setText(ResourcesHandler.getInstance(this).getResourceForKey(key) + " " + String.format(Locale.getDefault(), "%.2f", stats.get(key)));
            llStats.addView(textView);
        }

        viewBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        if(viewBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
            viewBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

    }

    @Override
    public void onBackPressed() {
        if (viewBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            viewBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
