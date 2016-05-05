package com.ablanco.tonsofdamage.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.AnimationUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 02/04/2016.
 * TonsOfDamage
 */
public class AvatarImageView extends RelativeLayout {

    @Bind(R.id.avatar) CircleImageView mImage;
    @Bind(R.id.tv_summoner_level) TextView tvLevel;

    public AvatarImageView(Context context) {
        this(context, null);
    }

    public AvatarImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.view_avatar_image, this);
        ButterKnife.bind(this, this);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AvatarImageView, 0, 0);

        boolean showLevel = a.getBoolean(R.styleable.AvatarImageView_showLevel, false);
        a.recycle();

        this.tvLevel.setVisibility(showLevel ? VISIBLE : GONE);

    }

    public void setPlaceholder(){
        this.mImage.setImageResource(R.drawable.default_profile);
    }

    public void setImage(@DrawableRes int resource){
        this.mImage.setImageResource(resource);
    }

    public void setImage(Drawable resource){
        this.mImage.setImageDrawable(resource);
    }

    public void setImage(Bitmap resource){
        this.mImage.setImageBitmap(resource);
    }

    public void setLevel(@StringRes int resource){
        this.tvLevel.setText(resource);
    }

    public void setLevel(long level){
        this.tvLevel.setText(String.valueOf(level));
    }

    public void showLevel(){
        AnimationUtils.revealView(tvLevel);
    }

    public void hideLevel(){
        tvLevel.setVisibility(GONE);
    }
}
