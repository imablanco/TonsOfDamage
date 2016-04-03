package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.ChampionListDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ChampionListAdapter;
import com.ablanco.tonsofdamage.ui.views.DividerItemDecoration;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ChampionsFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.loading)
    ProgressBar loading;

    private List<ChampionDto> mChampions = new ArrayList<>()
;
    public static Fragment newInstance(){
        return new ChampionsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(new ChampionListAdapter(getActivity(), mChampions));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SizeUtils.convertDpToPixel(4)));


        Teemo.getInstance(getActivity()).getStaticDataHandler().getChampions(Locale.getDefault().toString(), null, null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionListDto>() {
            @Override
            public void onResponse(ChampionListDto response) {

                mChampions.addAll(response.getData().values());

                Collections.sort(mChampions, new Comparator<ChampionDto>() {
                    @Override
                    public int compare(ChampionDto lhs, ChampionDto rhs) {
                        return lhs.getName().compareTo(rhs.getName());
                    }
                });

                mRecyclerView.getAdapter().notifyDataSetChanged();

                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(TeemoException e) {
                loading.setVisibility(View.GONE);
            }
        });
    }
}
