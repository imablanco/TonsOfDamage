package com.ablanco.tonsofdamage.ui.home;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 25/05/2016.
 * TonsOfDamage
 */
public abstract class HomePlaceholder extends RelativeLayout {

    public HomePlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, getLayout(), this);
        ButterKnife.bind(this);
    }


    public abstract int getLayout();

    public abstract void update();
}
