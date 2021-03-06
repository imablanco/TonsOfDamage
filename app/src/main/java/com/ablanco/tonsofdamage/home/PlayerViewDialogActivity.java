package com.ablanco.tonsofdamage.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.featuredgames.Participant;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.SummonerDetailActivity;
import com.ablanco.tonsofdamage.utils.AnimationUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 29/05/2016.
 * TonsOfDamage
 */
public class PlayerViewDialogActivity extends BaseActivity {

    public final static String EXTRA_PARTICIPANT = "extra_participant";
    public final static String EXTRA_CHAMPION = "extra_champion";
    @Bind(R.id.tv_summoner_name)
    TextView mTvSummonerName;
    @Bind(R.id.img_champion)
    CircleImageView mImgChampion;
    @Bind(R.id.root)
    RelativeLayout mRoot;
    @Bind(R.id.bt_view_summoner)
    Button mBtViewSummoner;
    private ChampionDto championDto;
    private Participant participant;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_player_detail);
        ButterKnife.bind(this);

        championDto = (ChampionDto) getIntent().getSerializableExtra(EXTRA_CHAMPION);
        participant = (Participant) getIntent().getSerializableExtra(EXTRA_PARTICIPANT);

        mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (championDto != null && participant != null) {
            Utils.setTransitionNameForView(mImgChampion, participant.getSummonerName());
            Glide.with(this).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(this), championDto.getImage().getFull())).into(mImgChampion);

            mImgChampion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putSerializable(ChampionDetailActivity.EXTRA_CHAMPION_ID, championDto.getId());
                    NavigationHandler.navigateTo(PlayerViewDialogActivity.this, NavigationHandler.CHAMPION_DETAIL, b);
                }
            });

            boolean isBot = participant.isBot() != null && participant.isBot();
            mTvSummonerName.setText(!isBot ? participant.getSummonerName() : "Bot");

            if (!isBot) {
                Teemo.getInstance(this).getSummonersHandler().getSummonerByName(participant.getSummonerName(), new ServiceResponseListener<Summoner>() {
                    @Override
                    public void onResponse(Summoner response) {
                        final Long summonerId = response.getId();
                        if(!isFinishing()){
                            mBtViewSummoner.post(new Runnable() {
                                @Override
                                public void run() {
                                    AnimationUtils.revealView(mBtViewSummoner);
                                }
                            });
                            mBtViewSummoner.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Bundle b = new Bundle();
                                    b.putSerializable(SummonerDetailActivity.EXTRA_ID, summonerId);
                                    NavigationHandler.navigateTo(PlayerViewDialogActivity.this, NavigationHandler.SUMMONER_DETAIL, b);
                                }
                            });
                        }

                    }

                    @Override
                    public void onError(TeemoException e) {
                    }
                });
            }

        }

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_HOME_PLAYER_VIEW;
    }

    @Override
    public String getNavigationItemId() {
        if(championDto != null && participant != null){
            return participant.getSummonerName() + ", " + championDto.getName() + "(" + championDto.getId() + ")";
        }else {
            return null;
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }
}
