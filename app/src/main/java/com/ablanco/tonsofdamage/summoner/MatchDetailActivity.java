package com.ablanco.tonsofdamage.summoner;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.constants.TeamID;
import com.ablanco.teemo.model.games.Game;
import com.ablanco.teemo.model.games.Player;
import com.ablanco.teemo.model.games.RawStats;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.MapDataDto;
import com.ablanco.teemo.model.staticdata.MapDetailsDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.RecentGamesData;
import com.ablanco.tonsofdamage.base.BaseActivity;
import com.ablanco.tonsofdamage.handler.AnalyticsHandler;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MatchDetailActivity extends BaseActivity implements AppBarLayout.OnOffsetChangedListener {

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    private final static String CHART_GOLD = "GOLD";
    private final static String CHART_DAMAGE_DEALT = "DAMAGE_DEALT";
    private final static String CHART_DAMAGE_TAKEN = "DAMAGE_TAKEN";

    public static final String EXTRA_TRANSITION_NAME = "extra_transition_name";
    public static final String EXTRA_SUMMONER_ID = "extra_summoner_id";
    public static final String EXTRA_DATA = "extra_data";

    @Bind(R.id.tv_game_type)
    TextView tvGameType;
    @Bind(R.id.tv_date)
    TextView tvDate;
    @Bind(R.id.tv_duration)
    TextView tvDuration;
    @Bind(R.id.tv_score)
    TextView tvScore;
    @Bind(R.id.tv_gold)
    TextView tvGold;
    @Bind(R.id.tv_minion)
    TextView tvMinions;
    @Bind(R.id.tv_title)
    TextView mSecondTitle;
    @Bind(R.id.main_toolbar)
    Toolbar mToolbar;
    @Bind(R.id.champion_bg_img)
    ImageView mChampionBg;
    @Bind(R.id.img_champion)
    CircleImageView mImgChampion;
    @Bind(R.id.tv_title2)
    TextView mTitle;
    @Bind(R.id.main_linearlayout_title)
    LinearLayout mTitleContainer;
    @Bind(R.id.main_appbar)
    AppBarLayout mAppBarLayout;
    @Bind(R.id.tv_map_name)
    TextView tvMapName;
    @Bind(R.id.ll_participants)
    LinearLayout llParticipants;
    @Bind(R.id.dv_gold)
    DecoView dvGold;
    @Bind(R.id.tv_gold_eraned)
    TextView tvGoldEarned;
    @Bind(R.id.tv_gold_spent)
    TextView tvGoldSpent;
    @Bind(R.id.dv_damage_dealt)
    DecoView dvDamageDealt;
    @Bind(R.id.tv_damage_dealt)
    TextView tvDamageDealt;
    @Bind(R.id.tv_physical_damage_dealt)
    TextView tvPhysicalDamageDealt;
    @Bind(R.id.tv_magical_damage_dealt)
    TextView tvMagicalDamageDealt;
    @Bind(R.id.tv_true_damage_dealt)
    TextView tvTrueDamageDealt;
    @Bind(R.id.dv_damage_taken)
    DecoView dvDamageTaken;
    @Bind(R.id.tv_damage_taken)
    TextView tvDamageTaken;
    @Bind(R.id.tv_physical_damage_taken)
    TextView tvPhysicalDamageTaken;
    @Bind(R.id.tv_magical_damage_taken)
    TextView tvMagicalDamageTaken;
    @Bind(R.id.tv_true_damage_taken)
    TextView tvTrueDamageTaken;
    @Bind(R.id.nested_scroll)
    NestedScrollView nestedScrollView;

    @Bind(R.id.cv_stats)
    View cardViewStats;
    @Bind(R.id.rl_gold_chart)
    View rlGoldChart;
    @Bind(R.id.rl_dmg_dealt_chart)
    View rlDmgDealtChart;
    @Bind(R.id.rl_dmg_taken_chart)
    View rlDmgTakenChart;

    private Map<String, List<DecoEvent.Builder>> mEventsMap = new HashMap<>();
    boolean goldHandled = false;
    boolean damageTakenHandled = false;
    boolean damageDealtHandled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);

        final RecentGamesData data = (RecentGamesData) getIntent().getSerializableExtra(EXTRA_DATA);
        long summonerId = getIntent().getLongExtra(EXTRA_SUMMONER_ID, 0);

        Utils.setTransitionNameForView(mImgChampion, getIntent().getStringExtra(EXTRA_TRANSITION_NAME));
        mAppBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(mTitle, 0, View.INVISIBLE);

        if (data != null) {
            boolean isWin = data.getGame().getStats().isWin();

            mImgChampion.setBorderColorResource(isWin ? R.color.green : R.color.red);
            Glide.with(this).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(this), data.getChampionDto().getImage().getFull())).into(mImgChampion);
            Glide.with(this).load(ImageUris.getChampionSplashArt(data.getChampionDto().getKey(), data.getChampionDto().getSkins().get(0).getNum())).into(mChampionBg);
            mTitle.setText(getString(isWin ? R.string.win_at : R.string.lose_at, data.getChampionDto().getName()));
            mSecondTitle.setText(getString(isWin ? R.string.win_at : R.string.lose_at, data.getChampionDto().getName()));

            tvGameType.setText(data.getGame().getGameMode());
            tvDate.setText(new PrettyTime().format(new Date(data.getGame().getCreateDate())));
            long millis = data.getGame().getStats().getTimePlayed() * 1000;
            String duration = String.format(Locale.getDefault(), "%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(millis),
                    TimeUnit.MILLISECONDS.toMinutes(millis));

            tvDuration.setText(duration);

            Teemo.getInstance(this).getStaticDataHandler().getMapData(SettingsHandler.getLanguage(this), null, new ServiceResponseListener<MapDataDto>() {
                @Override
                public void onResponse(MapDataDto response) {
                    for (MapDetailsDto detailsDto : response.getData().values()) {
                        if (data.getGame().getMapId() == detailsDto.getMapId().intValue()) {
                            tvMapName.setText(detailsDto.getMapName());
                        }
                    }
                }

                @Override
                public void onError(TeemoException e) {

                }
            });

            tvScore.setText(Utils.getGameScore(data.getGame().getStats()));
            tvGold.setText(Utils.getFormattedStats(data.getGame().getStats().getGoldEarned()));
            tvMinions.setText(String.valueOf(data.getGame().getStats().getMinionsKilled()));

            //teams participants
            TeamsAdapter adapter = new TeamsAdapter(this, data.getGame(), summonerId);
            for (int i = 0; i < adapter.getCount(); i++) {
                llParticipants.addView(adapter.getView(i, null, null));
            }

            setUpStats(data.getGame().getStats());

            final int screenHeight = SizeUtils.getScreenHeight(this);

            nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                @Override
                public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY + screenHeight - mToolbar.getHeight() >= cardViewStats.getTop() + rlGoldChart.getTop() + dvGold.getHeight() - dvGold.getHeight()/3 && !goldHandled){
                        if(mEventsMap.get(CHART_GOLD) != null){
                            goldHandled = true;
                            for (DecoEvent.Builder builder : mEventsMap.get(CHART_GOLD)){
                                dvGold.addEvent(builder.build());
                            }
                        }
                    }

                    if (scrollY + screenHeight - mToolbar.getHeight() >= cardViewStats.getTop() + rlDmgDealtChart.getTop() + dvDamageDealt.getHeight() - dvDamageDealt.getHeight()/3 && !damageDealtHandled){
                        if(mEventsMap.get(CHART_DAMAGE_DEALT) != null){
                            damageDealtHandled = true;
                            for (DecoEvent.Builder builder : mEventsMap.get(CHART_DAMAGE_DEALT)){
                                dvDamageDealt.addEvent(builder.build());
                            }
                        }
                    }

                    if (scrollY + screenHeight - mToolbar.getHeight() >= cardViewStats.getTop() + rlDmgTakenChart.getTop() + dvDamageTaken.getHeight() - dvDamageTaken.getHeight()/3 && !damageTakenHandled){
                        if(mEventsMap.get(CHART_DAMAGE_TAKEN) != null){
                            damageTakenHandled = true;
                            for (DecoEvent.Builder builder : mEventsMap.get(CHART_DAMAGE_TAKEN)){
                                dvDamageTaken.addEvent(builder.build());
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    public String getClassName() {
        return AnalyticsHandler.CLASS_NAME_MATCH_DETAIL;
    }

    private void setUpStats(RawStats stats) {

        int golEarned = 0;
        int goldSpent = 0;
        int dmgDealt = 0;
        int pDmgDealt = 0;
        int mDmgDealt = 0;
        int tDmgDealt = 0;
        int dmgTaken = 0;
        int pDmgTaken = 0;
        int mDmgTaken = 0;
        int tDmgTaken = 0;

        if(stats.getGoldEarned() != null){
            golEarned = stats.getGoldEarned();
        }

        if(stats.getGoldSpent() != null){
            goldSpent = stats.getGoldSpent();
        }

        if(stats.getTotalDamageDealt() != null){
            dmgDealt = stats.getTotalDamageDealt();
        }

        if(stats.getPhysicalDamageDealtPlayer() != null){
            pDmgDealt = stats.getPhysicalDamageDealtPlayer();
        }

        if(stats.getMagicDamageDealtPlayer() != null){
            mDmgDealt = stats.getMagicDamageDealtPlayer();
        }

        if(stats.getTrueDamageDealtPlayer() != null){
            tDmgDealt = stats.getTrueDamageDealtPlayer();
        }

        if(stats.getTotalDamageTaken() != null){
            dmgTaken = stats.getTotalDamageTaken();
        }

        if(stats.getPhysicalDamageTaken() != null){
            pDmgTaken = stats.getPhysicalDamageTaken();
        }

        if(stats.getMagicDamageTaken() != null){
            mDmgTaken = stats.getMagicDamageTaken();
        }
        if(stats.getTrueDamageTaken() != null){
            tDmgTaken = stats.getTrueDamageTaken();
        }


        SeriesItem goldSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.colorAccent))
                .setRange(0, golEarned,0)
                .setCapRounded(false)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .build();

        SeriesItem goldSpentSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.red))
                .setRange(0, golEarned, 0)
                .setCapRounded(false)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .build();

        mEventsMap.put(CHART_GOLD, new ArrayList<DecoEvent.Builder>());

        int index = dvGold.addSeries(goldSeries);
        mEventsMap.get(CHART_GOLD).add(getBuilder(index, golEarned));

        index = dvGold.addSeries(goldSpentSeries);
        mEventsMap.get(CHART_GOLD).add(getBuilder(index, goldSpent));

        tvGoldEarned.setText(getString(R.string.gold_earned, Utils.getFormattedStats(golEarned)));
        tvGoldSpent.setText(getString(R.string.gold_spent, Utils.getFormattedStats(goldSpent)));


        mEventsMap.put(CHART_DAMAGE_DEALT, new ArrayList<DecoEvent.Builder>());

        SeriesItem totalDamageDealtSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.colorAccent))
                .setCapRounded(false)
                .setRange(0, dmgDealt, 0)
                .build();

        SeriesItem physicalDamageDealtSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.red))
                .setRange(0, dmgDealt, 0)
                .setCapRounded(false)
                .build();

        SeriesItem magicalDamageDealtSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.magenta))
                .setCapRounded(false)
                .setRange(0, dmgDealt, 0)
                .build();

        SeriesItem trueDamageDealtSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.white))
                .setRange(0, dmgDealt, 0)
                .setCapRounded(false)
                .build();

        index = dvDamageDealt.addSeries(totalDamageDealtSeries);
        mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, dmgDealt));


        if(pDmgDealt > mDmgDealt){
            index = dvDamageDealt.addSeries(physicalDamageDealtSeries);
            mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, pDmgDealt));

            if(mDmgDealt > tDmgDealt){
                index = dvDamageDealt.addSeries(magicalDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, mDmgDealt));
                index = dvDamageDealt.addSeries(trueDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, tDmgDealt));
            }else {

                index = dvDamageDealt.addSeries(trueDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, tDmgDealt));
                index = dvDamageDealt.addSeries(magicalDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, mDmgDealt));
            }

        }else {
            index = dvDamageDealt.addSeries(magicalDamageDealtSeries);
            mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, mDmgDealt));

            if(pDmgDealt > tDmgDealt){
                index = dvDamageDealt.addSeries(physicalDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index,pDmgDealt));
                index = dvDamageDealt.addSeries(trueDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, tDmgDealt));
            }else {

                index = dvDamageDealt.addSeries(trueDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, tDmgDealt));
                index = dvDamageDealt.addSeries(physicalDamageDealtSeries);
                mEventsMap.get(CHART_DAMAGE_DEALT).add(getBuilder(index, pDmgDealt));
            }

        }



        tvDamageDealt.setText(getString(R.string.total_damage_dealt, Utils.getFormattedStats(dmgDealt)));
        tvPhysicalDamageDealt.setText(getString(R.string.damage_p_dealt, Utils.getFormattedStats(pDmgDealt)));
        tvMagicalDamageDealt.setText(getString(R.string.damage_m_dealt, Utils.getFormattedStats(mDmgDealt)));
        tvTrueDamageDealt.setText(getString(R.string.damage_t_dealt, Utils.getFormattedStats(tDmgDealt)));

        SeriesItem totalDamageTakenSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.colorAccent))
                .setRange(0, dmgTaken, 0)
                .setCapRounded(false)
                .build();

        SeriesItem physicalDamageTakenSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.red))
                .setRange(0, dmgTaken, 0)
                .setCapRounded(false)
                .build();

        SeriesItem magicalDamageTakenSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.magenta))
                .setRange(0, dmgTaken, 0)
                .setCapRounded(false)
                .build();
        SeriesItem trueDamageTakenSeries = new SeriesItem.Builder(ContextCompat.getColor(this, R.color.white))
                .setRange(0, dmgTaken, 0)
                .setCapRounded(false)
                .build();

        mEventsMap.put(CHART_DAMAGE_TAKEN, new ArrayList<DecoEvent.Builder>());

        index = dvDamageTaken.addSeries(totalDamageTakenSeries);
        mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, dmgTaken));

        if(pDmgTaken > mDmgTaken){
            index = dvDamageTaken.addSeries(physicalDamageTakenSeries);
            mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, pDmgTaken));
            index = dvDamageTaken.addSeries(magicalDamageTakenSeries);
            mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, mDmgTaken));
        }else {
            index = dvDamageTaken.addSeries(magicalDamageTakenSeries);
            mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, mDmgTaken));
            index = dvDamageTaken.addSeries(physicalDamageTakenSeries);
            mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, pDmgTaken));
        }

        index = dvDamageTaken.addSeries(trueDamageTakenSeries);
        mEventsMap.get(CHART_DAMAGE_TAKEN).add(getBuilder(index, tDmgTaken));

        tvDamageTaken.setText(getString(R.string.total_damage_taken, Utils.getFormattedStats(dmgTaken)));
        tvPhysicalDamageTaken.setText(getString(R.string.damage_p_taken, Utils.getFormattedStats(pDmgTaken)));
        tvMagicalDamageTaken.setText(getString(R.string.damage_m_taken, Utils.getFormattedStats(mDmgTaken)));
        tvTrueDamageTaken.setText(getString(R.string.damage_t_taken, Utils.getFormattedStats(tDmgTaken)));
    }

    private DecoEvent.Builder getBuilder(int index, float value){
        return new DecoEvent.Builder(value).setDuration(600).setIndex(index);
    }


    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }


    static class TeamsAdapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater inflater;
        private List<Pair<Player, Player>> mParticipants = new ArrayList<>();
        private long summonerId = 0;

        public TeamsAdapter(Context context, Game game, long summonerId) {
            this.mContext = context;
            this.mParticipants.addAll(buildData(game));
            this.inflater = LayoutInflater.from(context);
            this.summonerId = summonerId;

        }

        @Override
        public int getCount() {
            return mParticipants.size();
        }

        @Override
        public Object getItem(int position) {
            return mParticipants.get(position);
        }


        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.view_match_detail_team_participant, parent);
                holder.tvName1 = (TextView) convertView.findViewById(R.id.tv_summoner_name1);
                holder.tvName2 = (TextView) convertView.findViewById(R.id.tv_summoner_name2);
                holder.img1 = (ImageView) convertView.findViewById(R.id.img_summoner1);
                holder.img2 = (ImageView) convertView.findViewById(R.id.img_summoner2);
                holder.root1 = convertView.findViewById(R.id.summoner1);
                holder.root2 = convertView.findViewById(R.id.summoner2);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Pair<Player, Player> item = mParticipants.get(position);
            if (item.first != null) {
                if (item.first.getSummonerId() != summonerId) {
                    holder.root1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putLong(SummonerDetailActivity.EXTRA_ID, item.first.getSummonerId());
                            NavigationHandler.navigateTo(mContext, NavigationHandler.SUMMONER_DETAIL, bundle);
                        }
                    });

                }

                Teemo.getInstance(mContext).getSummonersHandler().getSummonerNameById(String.valueOf(item.first.getSummonerId()), new ServiceResponseListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        holder.tvName1.setText(response);
                    }

                    @Override
                    public void onError(TeemoException e) {

                    }
                });

                Teemo.getInstance(mContext).getStaticDataHandler().getChampionById(item.first.getChampionId(), SettingsHandler.getLanguage(mContext), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                    @Override
                    public void onResponse(ChampionDto response) {
                        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                            Glide.with(mContext).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(mContext), response.getImage().getFull())).into(holder.img1);
                        }
                    }

                    @Override
                    public void onError(TeemoException e) {

                    }
                });
            }

            if (item.second != null) {

                if (item.second.getSummonerId() != summonerId) {
                    holder.root2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle = new Bundle();
                            bundle.putLong(SummonerDetailActivity.EXTRA_ID, item.second.getSummonerId());
                            NavigationHandler.navigateTo(mContext, NavigationHandler.SUMMONER_DETAIL, bundle);
                        }
                    });
                }

                Teemo.getInstance(mContext).getSummonersHandler().getSummonerNameById(String.valueOf(item.second.getSummonerId()), new ServiceResponseListener<String>() {
                    @Override
                    public void onResponse(String response) {
                        holder.tvName2.setText(response);
                    }

                    @Override
                    public void onError(TeemoException e) {
                        Log.d("TeamsAdapter", e.getMessage());
                    }
                });

                Teemo.getInstance(mContext).getStaticDataHandler().getChampionById(item.second.getChampionId(), SettingsHandler.getLanguage(mContext), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                    @Override
                    public void onResponse(ChampionDto response) {
                        if (mContext instanceof Activity && !((Activity) mContext).isFinishing()) {
                            Glide.with(mContext).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(mContext), response.getImage().getFull())).into(holder.img2);
                        }
                    }

                    @Override
                    public void onError(TeemoException e) {
                        Log.d("TeamsAdapter", e.getMessage());
                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            View root1;
            View root2;
            TextView tvName1;
            TextView tvName2;
            ImageView img1;
            ImageView img2;
        }

        private List<Pair<Player, Player>> buildData(Game game) {
            List<Player> blueTeamParticipants = getParticipantsByTeam(game.getFellowPlayers(), TeamID.BLUE_TEAM);
            List<Player> purpleTeamParticipants = getParticipantsByTeam(game.getFellowPlayers(), TeamID.PURPLE_TEAM);

            Player player = new Player();
            player.setChampionId(game.getChampionId());
            player.setTeamId(game.getTeamId());
            player.setSummonerId(SettingsHandler.getSummoner(mContext));

            if (game.getTeamId() == TeamID.BLUE_TEAM) {
                blueTeamParticipants.add(player);
            } else {
                purpleTeamParticipants.add(player);
            }

            List<Pair<Player, Player>> data = new ArrayList<>();

            for (int i = 0; i < game.getFellowPlayers().size() / 2 + game.getFellowPlayers().size() % 2; i++) {
                data.add(new Pair<Player, Player>(
                        blueTeamParticipants.size() >= i ? blueTeamParticipants.get(i) : null,
                        purpleTeamParticipants.size() >= i ? purpleTeamParticipants.get(i) : null));
            }

            return data;
        }

        private List<Player> getParticipantsByTeam(List<Player> players, int teamId) {
            List<Player> filteredPlayers = new ArrayList<>();
            for (Player player : players) {
                if (player.getTeamId() == teamId) filteredPlayers.add(player);
            }

            return filteredPlayers;
        }
    }

}
