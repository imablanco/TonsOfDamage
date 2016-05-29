package com.ablanco.tonsofdamage.adapter;

import android.content.Context;

import com.ablanco.tonsofdamage.summoner.MasteryPageProxyModel;

import java.util.List;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class MasteryListNameAdapter extends SpinnerSimpleHighlighttemAdapter<MasteryPageProxyModel> {

    public MasteryListNameAdapter(Context context, List<MasteryPageProxyModel> objects) {
        super(context, objects);
    }

    @Override
    protected boolean shouldHighLightItem(int position) {
        return getItem(position) != null &&  getItem(position).isCurrent() != null && getItem(position).isCurrent();
    }
}
