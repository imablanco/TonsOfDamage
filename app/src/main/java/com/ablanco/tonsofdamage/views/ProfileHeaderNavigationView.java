package com.ablanco.tonsofdamage.views;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.SummonerDetailActivity;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 02/04/2016.
 * TonsOfDamage
 */
public class ProfileHeaderNavigationView extends RelativeLayout {

    @Bind(R.id.profile_image)
    CircleImageView avatarImageView;
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

        avatarImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putLong(SummonerDetailActivity.EXTRA_ID, SettingsHandler.getSummoner(getContext()));
                NavigationHandler.navigateTo(getContext(), NavigationHandler.SUMMONER_DETAIL, bundle);
            }
        });
    }

    public void update() {

        Teemo.getInstance(getContext()).getSummonersHandler().getSummonerById(String.valueOf(SettingsHandler.getSummoner(getContext())), new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(final Summoner response) {
                tvSummonerRegion.setText(SettingsHandler.getRegion(getContext()).toUpperCase());
                tvSummonerName.setText(response.getName());
                Glide.with(getContext()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getContext()), String.valueOf(response.getProfileIconId()))).into(avatarImageView);
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }
}
