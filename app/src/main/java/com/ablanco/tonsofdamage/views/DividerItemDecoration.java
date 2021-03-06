package com.ablanco.tonsofdamage.views;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class DividerItemDecoration extends RecyclerView.ItemDecoration {

    private final int mSpace;

    // TODO: 03/04/2016 make customizabe as RM

    public DividerItemDecoration(int mSpace) {
        this.mSpace = mSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.bottom = mSpace;
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.top = mSpace;
    }
}