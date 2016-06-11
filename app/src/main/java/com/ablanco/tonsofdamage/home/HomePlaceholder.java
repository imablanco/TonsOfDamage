package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.AnimationUtils;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 25/05/2016.
 * TonsOfDamage
 */
public abstract class HomePlaceholder extends CardView{

    private View editOverlayView;

    public HomePlaceholder(Context context) {
        super(context);
    }

    public HomePlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, getLayout(), this);
        ButterKnife.bind(this);

        editOverlayView = inflate(context, R.layout.view_home_edit, null);
        addView(editOverlayView);

        this.setUseCompatPadding(true);
        this.setRadius(SizeUtils.convertDpToPixel(2));
        setCardBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
    }

    public final void onEditStarted(){
        AnimationUtils.revealView(editOverlayView);
    }

    public final void onEditFinished(){
        editOverlayView.setVisibility(INVISIBLE);
    }

    abstract void update(boolean forceUpdate);
    abstract int getLayout();
}
