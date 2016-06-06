package com.ablanco.tonsofdamage.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.constants.TeamID;
import com.ablanco.teemo.model.featuredgames.FeaturedGameInfo;
import com.ablanco.teemo.model.featuredgames.Participant;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 25/05/2016.
 * TonsOfDamage
 */
@SuppressLint("ViewConstructor")
public class FeaturedGamesPlaceHolderItem extends RelativeLayout {

    @Bind(R.id.tv_game_type)
    TextView mTvGameType;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.team1_layout)
    LinearLayout mTeam1Layout;
    @Bind(R.id.team2_layout)
    LinearLayout mTeam2Layout;

    private int dp7 = SizeUtils.convertPixelsToDp(7);

    public FeaturedGamesPlaceHolderItem(Context context, FeaturedGameInfo gameInfo) {
        super(context);
        inflate(context, R.layout.item_ph_featured_games, this);
        ButterKnife.bind(this);

        setPadding(dp7, dp7, dp7, dp7);

        mTvGameType.setText(gameInfo.getGameMode());

        tvStartTime.setText(new PrettyTime().format(new Date(gameInfo.getGameStartTime())));

        mTeam1Layout.removeAllViews();
        mTeam2Layout.removeAllViews();

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);

        for (final Participant participant : gameInfo.getParticipants()) {
            Teemo.getInstance(getContext()).getStaticDataHandler().getChampionById(participant.getChampionId().intValue(), SettingsHandler.getLanguage(getContext()), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                @Override
                public void onResponse(final ChampionDto response) {

                    FeaturedGamesPlayerView playerView = new FeaturedGamesPlayerView(getContext(), response, participant);
                    playerView.setLayoutParams(params);
                    playerView.setTag(participant.getSummonerName());
                    if(participant.getTeamId() == TeamID.BLUE_TEAM){
                        mTeam1Layout.addView(playerView);
                    }else {
                        mTeam2Layout.addView(playerView);
                    }

                }

                @Override
                public void onError(TeemoException e) {

                }
            });


        }


    }

}
