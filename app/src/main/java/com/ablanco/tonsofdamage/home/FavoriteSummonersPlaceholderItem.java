package com.ablanco.tonsofdamage.home;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Queue;
import com.ablanco.teemo.model.leagues.League;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.SummonerDetailActivity;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco Cabrero on 6/6/16
 * TonsOfDamage
 */
@SuppressLint("ViewConstructor")
public class FavoriteSummonersPlaceholderItem extends LinearLayout implements View.OnClickListener {


    @Bind(R.id.img_summoner_left)
    CircleImageView imgSummonerLeft;
    @Bind(R.id.tv_summoner_left_name)
    TextView tvSummonerLeftName;
    @Bind(R.id.cv_left)
    CardView cvLeft;
    @Bind(R.id.img_summoner_middle)
    CircleImageView imgSummonerMiddle;
    @Bind(R.id.tv_summoner_middle_name)
    TextView tvSummonerMiddleName;
    @Bind(R.id.cv_middle)
    CardView cvMiddle;
    @Bind(R.id.img_solo_q_left)
    ImageView imgSoloQLeft;
    @Bind(R.id.tv_solo_q_rank_left)
    TextView tvSoloQRankLeft;
    @Bind(R.id.img_solo_q_middle)
    ImageView imgSoloQMiddle;
    @Bind(R.id.tv_solo_q_rank_middle)
    TextView tvSoloQRankMiddle;

    private List<Summoner> summonerIds = new ArrayList<>();

    public FavoriteSummonersPlaceholderItem(Context context, final List<Summoner> summoners) {
        super(context);

        inflate(context, R.layout.item_ph_favorite_summoner, this);
        ButterKnife.bind(this);

        this.summonerIds = summoners;

        setOrientation(HORIZONTAL);

        if (summoners.size() >= 1 && summoners.get(0) != null) {
            cvLeft.setVisibility(VISIBLE);
            cvLeft.setOnClickListener(FavoriteSummonersPlaceholderItem.this);
            Glide.with(getContext()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getContext()), String.valueOf(summoners.get(0).getProfileIconId()))).into(imgSummonerLeft);
            tvSummonerLeftName.setText(summoners.get(0).getName());

        }

        if (summoners.size() >= 2 && summoners.get(1) != null) {
            cvMiddle.setVisibility(VISIBLE);
            cvMiddle.setOnClickListener(FavoriteSummonersPlaceholderItem.this);
            Glide.with(getContext()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getContext()), String.valueOf(summoners.get(1).getProfileIconId()))).into(imgSummonerMiddle);
            tvSummonerMiddleName.setText(summoners.get(1).getName());

        }

        List<String> summonerIds = new ArrayList<>();
        for (Summoner summoner : summoners) {
            summonerIds.add(String.valueOf(summoner.getId()));
        }

        Teemo.getInstance(getContext()).getLeaguesHandler().getLeaguesBySummoners(summonerIds, true, new ServiceResponseListener<Map<String, List<League>>>() {
            @Override
            public void onResponse(Map<String, List<League>> response) {

                if(!((Activity)getContext()).isDestroyed()){
                    List<League> leagues;
                    if(summoners.size() >= 1 && summoners.get(0) != null){
                        leagues = response.get(String.valueOf(summoners.get(0).getId()));
                        if(leagues != null){
                            for (League league : leagues) {
                                if(league.getQueue().equals(Queue.RANKED_SOLO_5x5)){
                                    tvSoloQRankLeft.setText(league.getName());
                                    imgSoloQLeft.setImageResource(Utils.getResourceForTier(league.getTier()));
                                    break;
                                }
                            }
                        }
                    }

                    if(summoners.size() >= 2 && summoners.get(1) != null){
                        leagues = response.get(String.valueOf(summoners.get(1).getId()));
                        if(leagues != null){
                            for (League league : leagues) {
                                if(league.getQueue().equals(Queue.RANKED_SOLO_5x5)){
                                    tvSoloQRankMiddle.setText(league.getName());
                                    imgSoloQMiddle.setImageResource(Utils.getResourceForTier(league.getTier()));
                                    break;
                                }
                            }
                        }
                    }
                }

            }

            @Override
            public void onError(TeemoException e) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        long id = 0;
        if (v == cvLeft) {
            id = summonerIds.get(0).getId();
        } else if (v == cvMiddle) {
            id = summonerIds.get(1).getId();
        }

        if (id > 0) {
            Bundle bundle = new Bundle();
            bundle.putLong(SummonerDetailActivity.EXTRA_ID, id);
            NavigationHandler.navigateTo(getContext(), NavigationHandler.SUMMONER_DETAIL, bundle);
        }

    }
}
