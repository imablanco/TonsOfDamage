package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.championspells.ChampionSpellsAdapter;

import butterknife.Bind;
import im.ene.lab.toro.Toro;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public class ChampionSpellsFragment extends ChampionDetailBaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Fragment newInstance(ChampionDto champion){
        ChampionDetailBaseFragment f = new ChampionSpellsFragment();
        f.setChampion(champion);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champion_spells, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(new ChampionSpellsAdapter(getActivity(), mChampion.getId(), mChampion.getPassive(), mChampion.getSpells()));

    }

    @Override
    public void onPause() {
        Toro.unregister(mRecyclerView);
        super.onPause();
    }

    @Override
    public void onResume() {
        Toro.register(mRecyclerView);
        super.onResume();
    }
}
