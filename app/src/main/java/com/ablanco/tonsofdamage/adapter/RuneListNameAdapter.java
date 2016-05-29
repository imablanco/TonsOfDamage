package com.ablanco.tonsofdamage.adapter;

import android.content.Context;

import com.ablanco.tonsofdamage.summoner.RunePageProxyModel;

import java.util.List;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class RuneListNameAdapter extends SpinnerSimpleHighlighttemAdapter<RunePageProxyModel> {
    public RuneListNameAdapter(Context context, List<RunePageProxyModel> objects) {
        super(context, objects);
    }

    @Override
    protected boolean shouldHighLightItem(int position) {
        return getItem(position).getPage() != null && getItem(position).getPage().isCurrent();
    }
}
