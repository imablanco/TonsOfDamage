package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;

import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 22/06/2016.
 * TonsOfDamage
 */
public class RuneCreatorPlaceholder extends HomeViewPlaceholder implements HomePlaceholder {

    public RuneCreatorPlaceholder(final Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.ph_rune_creator, this);
        ButterKnife.bind(this);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationHandler.navigateTo(context, NavigationHandler.RUNE_CREATOR);
            }
        });
    }

    @Override
    public void update(boolean forceUpdate) {
        //not needed at this moment
    }
}
