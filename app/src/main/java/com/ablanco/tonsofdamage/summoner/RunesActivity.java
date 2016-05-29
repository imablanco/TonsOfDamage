package com.ablanco.tonsofdamage.summoner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.RuneDto;
import com.ablanco.teemo.model.staticdata.RuneListDto;
import com.ablanco.teemo.model.summoners.RunePage;
import com.ablanco.teemo.model.summoners.RunePages;
import com.ablanco.teemo.model.summoners.RuneSlot;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.RuneListNameAdapter;
import com.ablanco.tonsofdamage.adapter.RunesAdapter;
import com.ablanco.tonsofdamage.handler.SettingsHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    private Map<Long, List<RuneProxyModel>> mProxyRunes = new HashMap<>();
    private Map<String, RuneDto> mRunes = new HashMap<>();
    private RuneListNameAdapter spinnerAdapter;
    private List<RunePageProxyModel> mRunePages = new ArrayList<>();

    //private Map<Long, Map<Integer, Integer>> mRunesPerPage = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summoner_runes);
        ButterKnife.bind(this);

        final long mId = getIntent().getLongExtra(EXTRA_SUMMONER_ID, 0);

        if(mId > 0){

            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
            final RunesAdapter adapter = new RunesAdapter(this);

            mRecyclerView.setAdapter(adapter);
            spinnerAdapter = new RuneListNameAdapter(this, mRunePages);
            mSpinnerPageName.setAdapter(spinnerAdapter);
            mSpinnerPageName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    adapter.setRunes(mProxyRunes.get(mRunePages.get(position).getPage().getId()));

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            mLoading.setVisibility(View.GONE);

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
                                    if(mRuneCountMap.get(runeSlot.getRuneId()) == null){
                                        mRuneCountMap.put(runeSlot.getRuneId(), 0);
                                    }
                                    mRuneCountMap.put(runeSlot.getRuneId(), mRuneCountMap.get(runeSlot.getRuneId()) + 1);
                                }

                                RuneDto dto;
                                mProxyRunes.put(runePage.getId(), new ArrayList<RuneProxyModel>());
                                for (Integer runeId : mRuneCountMap.keySet()) {
                                    dto = mRunes.get(String.valueOf(runeId));
                                    if(dto != null){
                                        mProxyRunes.get(runePage.getId()).add(new RuneProxyModel(dto, mRuneCountMap.get(runeId)));
                                    }
                                }


                                /*for (RuneSlot runeSlot : runePage.getSlots()) {
                                    if(mRunesPerPage.get(runePage.getId()) == null){
                                        mRunesPerPage.put(runePage.getId(), new HashMap<Integer, Integer>());
                                    }

                                    if(mRunesPerPage.get(runePage.getId()).get(runeSlot.getRuneId()) != 0){
                                        mRunesPerPage.get(runePage.getId()).put(runeSlot.getRuneId(), mRunesPerPage.get(runePage.getId()).get(runeSlot.getRuneId() + 1));
                                    }
                                }*/
                            }

                            /*for (Long pageId : mRunesPerPage.keySet()) {
                                mProxyRunes.put(pageId, new ArrayList<RuneProxyModel>());
                                RuneDto dto;
                                for (Integer runeId : mRunesPerPage.get(pageId).keySet()) {
                                    dto = mRunes.get(String.valueOf(runeId));
                                    if(dto != null){
                                        mProxyRunes.get(pageId).add(new RuneProxyModel(dto, mRunesPerPage.get(pageId).get(runeId)));
                                    }
                                }


                            }*/

                            spinnerAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(TeemoException e) {
                            // TODO: 28/05/2016 handler error

                        }
                    });
                }

                @Override
                public void onError(TeemoException e) {
                    // TODO: 28/05/2016 handler error
                }
            });
        }else {
            finish();
        }


    }
}
