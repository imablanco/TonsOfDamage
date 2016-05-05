package com.ablanco.tonsofdamage.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ablanco.teemo.model.games.RawStats;
import com.ablanco.teemo.model.matches.MatchTeam;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.ui.activities.MatchDetailActivity;
import com.ablanco.tonsofdamage.utils.SizeUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 21/04/2016.
 * TonsOfDamage
 */
public class RecentGamesAdapter extends RecyclerView.Adapter<RecentGamesAdapter.GameViewHolder> {

    private final static int ITEM_VIEW_SIZE = 30;//dp

    private int dp5 = SizeUtils.convertDpToPixel(3);
    private LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE), SizeUtils.convertDpToPixel(ITEM_VIEW_SIZE));

    private List<RecentGamesData> mGames = new ArrayList<>();
    private Context mContext;

    public RecentGamesAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public GameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GameViewHolder(LayoutInflater.from(mContext).inflate(R.layout.view_recent_game, parent, false));
    }

    @Override
    public void onBindViewHolder(final GameViewHolder holder, int position) {
        final RecentGamesData data = mGames.get(position);
        Glide.with(mContext).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(mContext), data.getChampionDto().getImage().getFull())).into(holder.mImgChampionSquare);
        Glide.with(mContext).load(ImageUris.getSummonerSpellIcon(SettingsHandler.getCDNVersion(mContext), String.valueOf(data.getSummonerSpells().first.getImage().getFull()))).into(holder.mImgSpell1);
        Glide.with(mContext).load(ImageUris.getSummonerSpellIcon(SettingsHandler.getCDNVersion(mContext), String.valueOf(data.getSummonerSpells().second.getImage().getFull()))).into(holder.mImgSpell2);

        holder.llGameItems.removeAllViews();
        buildItemList(data.getGame().getStats(), holder.llGameItems);

        holder.mImgChampionSquare.setBorderColor(ContextCompat.getColor(mContext, isWin(data.getGame().getTeamId(), data.getMatchDetail().getTeams()) ? R.color.green : R.color.red));
        holder.tvGameType.setText(data.getGame().getGameMode());
        holder.tvDate.setText(new PrettyTime().format(new Date(data.getGame().getCreateDate())));
        long millis = data.getMatchDetail().getMatchDuration() * 1000;
        String duration = String.format(Locale.getDefault(), "%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis));

        holder.tvDuration.setText(duration);

        holder.tvScore.setText(Utils.getGameScore(data.getGame().getStats()));
        holder.tvGold.setText(Utils.getFormattedGold(data.getGame().getStats().getGoldEarned()));
        holder.tvMinions.setText(String.valueOf(data.getGame().getStats().getMinionsKilled()));

        Utils.setTransitionNameForView(holder.mImgChampionSquare, mContext.getString(R.string.match_detail_transition, position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transitionName = mContext.getString(R.string.match_detail_transition, holder.getAdapterPosition());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) mContext, holder.mImgChampionSquare, transitionName);
                Bundle bundle = new Bundle();
                bundle.putInt(MatchDetailActivity.EXTRA_MATCH_ID, data.getMatchDetail().getMatchId().intValue());
                bundle.putString(MatchDetailActivity.EXTRA_TRANISITION_NAME, transitionName);
                NavigationHandler.navigateTo(mContext, NavigationHandler.MATCH_DETAIL, bundle, options);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void addGame(RecentGamesData data) {
        this.mGames.add(data);
        Collections.sort(mGames, new Comparator<RecentGamesData>() {
            @Override
            public int compare(RecentGamesData lhs, RecentGamesData rhs) {
                return rhs.getGame().getCreateDate().compareTo(lhs.getGame().getCreateDate());
            }
        });
        notifyItemInserted(this.mGames.indexOf(data));
    }

    private void buildItemList(RawStats stats, LinearLayout layout) {
        createItem(stats.getItem0(), layout);
        createItem(stats.getItem1(), layout);
        createItem(stats.getItem2(), layout);
        createItem(stats.getItem3(), layout);
        createItem(stats.getItem4(), layout);
        createItem(stats.getItem5(), layout);
        createItem(stats.getItem6(), layout);
    }

    private boolean isWin(int teamId, List<MatchTeam> teams) {
        for (MatchTeam matchTeam : teams) {
            if (teamId == matchTeam.getTeamId()) {
                return matchTeam.getWinner();
            }
        }


        return false;


    }

    public RecentGamesData getGame(int position){
        return mGames.get(position);
    }

    private void createItem(final Integer id, LinearLayout layout) {
        ImageView imageView = new ImageView(mContext);
        imageView.setPadding(SizeUtils.convertDpToPixel(1), SizeUtils.convertDpToPixel(1), SizeUtils.convertDpToPixel(1), SizeUtils.convertDpToPixel(1));
        imageView.setBackgroundResource(R.drawable.image_bg);
        itemParams.leftMargin = dp5;
        itemParams.rightMargin = dp5;
        imageView.setLayoutParams(itemParams);
        if(id != null){
            Glide.with(mContext).load(ImageUris.getItemIcon(SettingsHandler.getCDNVersion(mContext), String.valueOf(id))).into(imageView);
        }else {
            imageView.setImageResource(R.drawable.empty_item);
        }
        layout.addView(imageView);
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_champion_square)
        CircleImageView mImgChampionSquare;
        @Bind(R.id.img_spell1)
        ImageView mImgSpell1;
        @Bind(R.id.img_spell2)
        ImageView mImgSpell2;
        @Bind(R.id.ll_game_items)
        LinearLayout llGameItems;
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

        public GameViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
