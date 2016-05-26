package com.ablanco.tonsofdamage.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.featuredgames.FeaturedGameInfo;
import com.ablanco.teemo.model.featuredgames.Participant;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.ui.activities.ChampionDetailActivity;
import com.bumptech.glide.Glide;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 25/05/2016.
 * TonsOfDamage
 */
public class FeaturedGamesPlaceHolderItem extends RelativeLayout {

    @Bind(R.id.tv_game_type)
    TextView mTvGameType;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.team1_summoner1)
    CircleImageView mTeam1Summoner1;
    @Bind(R.id.team1_summoner2)
    CircleImageView mTeam1Summoner2;
    @Bind(R.id.team1_summoner3)
    CircleImageView mTeam1Summoner3;
    @Bind(R.id.team1_summoner4)
    CircleImageView mTeam1Summoner4;
    @Bind(R.id.team1_summoner5)
    CircleImageView mTeam1Summoner5;
    @Bind(R.id.team2_summoner1)
    CircleImageView mTeam2Summoner1;
    @Bind(R.id.team2_summoner2)
    CircleImageView mTeam2Summoner2;
    @Bind(R.id.team2_summoner3)
    CircleImageView mTeam2Summoner3;
    @Bind(R.id.team2_summoner4)
    CircleImageView mTeam2Summoner4;
    @Bind(R.id.team2_summoner5)
    CircleImageView mTeam2Summoner5;

    private HashMap<Long, ImageView> mChampionImagesMap = new HashMap<>();

    public FeaturedGamesPlaceHolderItem(Context context, FeaturedGameInfo gameInfo) {
        super(context);
        inflate(context, R.layout.item_ph_featured_games, this);
        ButterKnife.bind(this);

        mTvGameType.setText(gameInfo.getGameMode());

        Collections.sort(gameInfo.getParticipants(), new Comparator<Participant>() {
            @Override
            public int compare(Participant lhs, Participant rhs) {
                return lhs.getTeamId().compareTo(rhs.getTeamId());
            }
        });

        tvStartTime.setText(new PrettyTime().format(new Date(gameInfo.getGameStartTime())));
        mChampionImagesMap.put(gameInfo.getParticipants().get(0).getChampionId(), mTeam1Summoner1);
        mChampionImagesMap.put(gameInfo.getParticipants().get(1).getChampionId(), mTeam1Summoner2);
        mChampionImagesMap.put(gameInfo.getParticipants().get(2).getChampionId(), mTeam1Summoner3);
        mChampionImagesMap.put(gameInfo.getParticipants().get(3).getChampionId(), mTeam1Summoner4);
        mChampionImagesMap.put(gameInfo.getParticipants().get(4).getChampionId(), mTeam1Summoner5);
        mChampionImagesMap.put(gameInfo.getParticipants().get(5).getChampionId(), mTeam2Summoner1);
        mChampionImagesMap.put(gameInfo.getParticipants().get(6).getChampionId(), mTeam2Summoner2);
        mChampionImagesMap.put(gameInfo.getParticipants().get(7).getChampionId(), mTeam2Summoner3);
        mChampionImagesMap.put(gameInfo.getParticipants().get(8).getChampionId(), mTeam2Summoner4);
        mChampionImagesMap.put(gameInfo.getParticipants().get(9).getChampionId(), mTeam2Summoner5);

        for (Participant participant : gameInfo.getParticipants()) {
            Teemo.getInstance(context).getStaticDataHandler().getChampionById(participant.getChampionId().intValue(), SettingsHandler.getLanguage(context), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                @Override
                public void onResponse(final ChampionDto response) {
                    mChampionImagesMap.get(response.getId().longValue()).setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle b = new Bundle();
                            b.putSerializable(ChampionDetailActivity.EXTRA_CHAMPION_ID, response.getId());
                            NavigationHandler.navigateTo(getContext(), NavigationHandler.CHAMPION_DETAIL, b);
                        }
                    });
                    Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), response.getImage().getFull())).into(mChampionImagesMap.get(response.getId().longValue()));
                }

                @Override
                public void onError(TeemoException e) {

                }
            });
        }

    }
}
