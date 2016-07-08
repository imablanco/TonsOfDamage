package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SizeUtils;

/**
 * Created by Álvaro Blanco on 22/06/2016.
 * TonsOfDamage
 */
public class HomeViewPlaceholder extends CardView {
    public HomeViewPlaceholder(Context context) {
        this(context, null);
    }

    public HomeViewPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setUseCompatPadding(true);
        this.setRadius(SizeUtils.convertDpToPixel(2));
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }
}
