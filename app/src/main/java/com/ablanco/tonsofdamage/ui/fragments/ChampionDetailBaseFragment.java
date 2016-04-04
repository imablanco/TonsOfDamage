package com.ablanco.tonsofdamage.ui.fragments;

import com.ablanco.teemo.model.staticdata.ChampionDto;

/**
 * Created by Álvaro Blanco on 04/04/2016.
 * TonsOfDamage
 */
public abstract class ChampionDetailBaseFragment extends BaseFragment {

    protected ChampionDto mChampion;

    protected void setChampion(ChampionDto champion){
        this.mChampion = champion;
    }
}
