package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ItemView extends SquareRelativeLayout {

    @Bind(R.id.img_item)
    ImageView mImgItem;

    private int mId;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_item, this);
        ButterKnife.bind(this, this);
    }

    public void setItemId(int id){
        this.mId = id;
        Glide.with(getContext()).load(ImageUris.getItemIcon(String.valueOf(mId))).into(mImgItem);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ButterKnife.unbind(this);
    }
}
