package com.ablanco.tonsofdamage.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 02/04/2016.
 * TonsOfDamage
 */
public class ProfileHeaderNavigationView extends RelativeLayout {

    @Bind(R.id.profile_image) AvatarImageView avatarImageView;
    @Bind(R.id.tv_summoner_name) TextView tvSummonerName;
    @Bind(R.id.tv_summoner_region) TextView tvSummonerRegion;

    public ProfileHeaderNavigationView(Context context) {
        this(context, null);
    }

    public ProfileHeaderNavigationView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProfileHeaderNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflate(context, R.layout.nav_header_home, this);
        ButterKnife.bind(this, this);

        update();
    }

    public void update() {

        Teemo.getInstance(getContext()).getSummonersHandler().getSummonerById(String.valueOf(SettingsHandler.getSummoner(getContext())), new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(final Summoner response) {
                Glide.with(getContext()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getContext()), String.valueOf(response.getProfileIconId()))).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        tvSummonerRegion.setText(SettingsHandler.getRegion(getContext()).toUpperCase());
                        tvSummonerName.setText(response.getName());
                        avatarImageView.setImage(resource);

                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        avatarImageView.setPlaceholder();
                    }
                });
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }
}
