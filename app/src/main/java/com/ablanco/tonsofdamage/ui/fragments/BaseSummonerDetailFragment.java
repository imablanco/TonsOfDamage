package com.ablanco.tonsofdamage.ui.fragments;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public abstract class BaseSummonerDetailFragment extends BaseFragment {

    protected long summonerId;

    public void setSummonerId(long summonerId) {
        this.summonerId = summonerId;
    }
}
