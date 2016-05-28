package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 18/04/2016.
 * TonsOfDamage
 */
public class SummonerMostPlayedChampionsAdapter extends RecyclerView.Adapter<SummonerMostPlayedChampionsAdapter.ChampionViewHolder> {


    private Context context;
    private List<ChampionStatsData> championStatses = new ArrayList<>();

    public SummonerMostPlayedChampionsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChampionViewHolder(LayoutInflater.from(context).inflate(R.layout.view_most_played_champion, parent, false));
    }

    @Override
    public void onBindViewHolder(ChampionViewHolder holder, int position) {

        final ChampionStatsData stats = championStatses.get(position);
        Glide.with(context).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(context), stats.getName())).into(holder.mImgChampionSquare);
        holder.mIcSword.setImageResource(R.drawable.ic_score);
        holder.mTvKda.setText(String.format(Locale.getDefault(), " %.2f KDA", Utils.getKDA(stats.getStats())));
        holder.mTvAverages.setText(Utils.getAverage(stats.getStats()));
        holder.mTvWinsLoses.setText(String.format(Locale.getDefault(), "W: %.0f%%", Utils.getWinRatio(stats.getStats())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, stats.getId());
                NavigationHandler.navigateTo(context, NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return championStatses.size();
    }

    public void addChampion(ChampionStatsData data){
        championStatses.add(data);
        notifyItemInserted(championStatses.size() - 1);
    }

    public class ChampionViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_champion_square)
        ImageView mImgChampionSquare;
        @Bind(R.id.ic_sword)
        ImageView mIcSword;
        @Bind(R.id.tv_kda)
        TextView mTvKda;
        @Bind(R.id.tv_wins_loses)
        TextView mTvWinsLoses;
        @Bind(R.id.tv_averages)
        TextView mTvAverages;
        public ChampionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
