package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.ablanco.tonsofdamage.R;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class ColorProgressBar extends ProgressBar {

    private int mColor;

    public ColorProgressBar(Context context) {
        this(context, null);
    }

    public ColorProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.setIndeterminate(true);

        TypedArray array =  context.obtainStyledAttributes(attrs, R.styleable.ColorProgressBar, 0, 0);
        mColor = array.getColor(R.styleable.ColorProgressBar_indeterminateColor, ContextCompat.getColor(context, R.color.colorAccent));
        array.recycle();

        setColorInternal();
    }

    public void setColor(int color){
        this.mColor = color;
        setColorInternal();
    }

    private void setColorInternal(){
        DrawableCompat.setTint(this.getProgressDrawable(), mColor);
    }
}
