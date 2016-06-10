package com.ablanco.tonsofdamage.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.featuredgames.Participant;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
@SuppressLint("ViewConstructor")
public class FeaturedGamesPlayerView extends RelativeLayout {

    @Bind(R.id.tv_summoner_name)
    TextView mTvSummonerName;
    @Bind(R.id.img_champion)
    CircleImageView mImgChampion;

    public FeaturedGamesPlayerView(Context context, final ChampionDto champion, final Participant participant) {
        super(context);
        inflate(context, R.layout.ph_featured_games_player_view, this);
        ButterKnife.bind(this);

        Utils.setTransitionNameForView(mImgChampion, participant.getSummonerName());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            this.setBackgroundResource(outValue.resourceId);
        }

        if(Utils.isContextValid(getContext())){

            this.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) getContext(), mImgChampion, participant.getSummonerName());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(PlayerViewDialogActivity.EXTRA_CHAMPION, champion);
                    bundle.putSerializable(PlayerViewDialogActivity.EXTRA_PARTICIPANT, participant);
                    NavigationHandler.navigateTo(getContext(), NavigationHandler.PLAYER_DETAIL, bundle, options);

                }
            });

            Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), champion.getImage().getFull())).into(mImgChampion);

            boolean isBot = participant.isBot() != null && participant.isBot();

            mTvSummonerName.setText(!isBot ? participant.getSummonerName() : "Bot");

        }

    }

}
