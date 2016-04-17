package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SizeUtils;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public class ErrorView extends TextView {

    private ErrorViewTapListener listener;

    public interface ErrorViewTapListener {
        void loadData();
    }
    public ErrorView(Context context) {
        this(context, null);
    }

    public ErrorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ErrorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(!isInEditMode()) setVisibility(GONE);
        setPadding(SizeUtils.convertDpToPixel(30), SizeUtils.convertDpToPixel(30), SizeUtils.convertDpToPixel(30),SizeUtils.convertDpToPixel(30));
        setText(context.getString(R.string.error_text));
        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
        setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        setCompoundDrawablePadding(SizeUtils.convertDpToPixel(20));
        Drawable image = ContextCompat.getDrawable(context, R.drawable.ic_error);
        image.setBounds( 0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight() );
        setCompoundDrawables(null, image, null, null );
        setGravity(Gravity.CENTER);
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    setVisibility(GONE);
                    listener.loadData();
                }
            }
        });

    }

    public void setListener(ErrorViewTapListener listener){
        this.listener = listener;
    }

    public void show(){
        this.setVisibility(VISIBLE);
    }

    public void hide(){
        this.setVisibility(GONE);
    }
}
