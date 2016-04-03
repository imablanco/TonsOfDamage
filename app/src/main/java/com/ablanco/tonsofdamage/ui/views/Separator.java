package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SizeUtils;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class Separator extends View {

    public Separator(Context context) {
        this(context, null);
    }

    public Separator(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Separator);
        int color = a.getColor(R.styleable.Separator_separatorColor, ContextCompat.getColor(context, R.color.white));
        a.recycle();
        this.setBackgroundColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(heightMeasureSpec), SizeUtils.convertDpToPixel(1));
    }
}
