package com.ablanco.tonsofdamage.champions;

import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.tonsofdamage.base.BaseFragment;

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
