package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.championmastery.ChampionMasteryDto;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.adapter.TopChampionMasteryAdapter;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco Cabrero on 10/6/16
 * TonsOfDamage
 */
public class TopChampionMasteryPlaceholder extends HomeViewPlaceholder implements HomePlaceholder {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.tv_error)
    TextView tvError;
    @Bind(R.id.loading)
    ProgressBar loading;

    private TopChampionMasteryAdapter adapter;
    private boolean firstTime = true;

    public TopChampionMasteryPlaceholder(Context context) {
        this(context, null);
    }

    public TopChampionMasteryPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.ph_top_champion_mastery, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new TopChampionMasteryAdapter(context);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, adapter.getItemAtPosition(position).getChampion().getId());
                NavigationHandler.navigateTo(getContext(), NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });
    }

    @Override
    public void update(boolean forceUpdate) {
        if(forceUpdate || firstTime){
            firstTime = false;
            adapter.clearAdapter();
            Teemo.getInstance(getContext()).getChampionMasteryHandler().getTopChampionsMastery(Utils.getPlatformForRegion(getContext()), SettingsHandler.getSummoner(getContext()), 3, new ServiceResponseListener<List<ChampionMasteryDto>>() {
                @Override
                public void onResponse(List<ChampionMasteryDto> response) {
                    if (getContext() != null) {
                        loading.setVisibility(GONE);

                        if(response != null && !response.isEmpty()){
                            for (final ChampionMasteryDto championMasteryDto : response) {
                                Teemo.getInstance(getContext()).getStaticDataHandler().getChampionById(championMasteryDto.getChampionId().intValue(), SettingsHandler.getLanguage(getContext()), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                                    @Override
                                    public void onResponse(ChampionDto response) {
                                        adapter.addChampionMastery(new SmartChampionMastery(response, championMasteryDto));
                                    }

                                    @Override
                                    public void onError(TeemoException e) {
                                        tvError.setVisibility(VISIBLE);
                                        if(e.getCode() != 404){
                                            tvError.setText(R.string.error_text);
                                        }
                                    }
                                });
                            }
                        } else {
                            tvError.setVisibility(VISIBLE);
                            tvError.setText(R.string.error_text);
                        }

                    }

                }

                @Override
                public void onError(TeemoException e) {
                    loading.setVisibility(GONE);
                    tvError.setVisibility(VISIBLE);
                    tvError.setText(R.string.error_text);
                }
            });
        }
    }
}
