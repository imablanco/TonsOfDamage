package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Álvaro Blanco on 07/05/2016.
 * TonsOfDamage
 */
public class SummonerStatisticsFragment extends BaseSummonerDetailFragment {

    public static BaseSummonerDetailFragment newInstance(long summonerId){
        BaseSummonerDetailFragment f = new SummonerStatisticsFragment();
        f.setSummonerId(summonerId);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
