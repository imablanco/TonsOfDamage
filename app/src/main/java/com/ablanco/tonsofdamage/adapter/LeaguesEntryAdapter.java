package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.model.leagues.LeagueEntry;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.summoner.SummonerDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco Cabrero on 8/6/16
 * TonsOfDamage
 */
public class LeaguesEntryAdapter extends RecyclerView.Adapter<LeaguesEntryAdapter.EntryViewHolder> {

    private final Context context;
    private List<LeagueEntry> entries = new ArrayList<>();
    private LayoutInflater inflater;
    private long summonerId;
    private String currentShowingDivision;


    public LeaguesEntryAdapter(Context context, long summonerId) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.summonerId = summonerId;
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EntryViewHolder(inflater.inflate(R.layout.item_league_entry, parent, false));
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {

        final LeagueEntry entry = entries.get(position);

        holder.tvPoints.setText(String.valueOf(entry.getLeaguePoints()));
        holder.tvWins.setText(String.valueOf(entry.getWins()));
        holder.tvPosition.setText(String.valueOf(position + 1));
        holder.tvSummonerName.setText(entry.getPlayerOrTeamName());

        if(entry.getPlayerOrTeamId() != null && entry.getPlayerOrTeamId().equals(String.valueOf(summonerId))){
            holder.tvSummonerName.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.itemView.setOnClickListener(null);
        }else {
            holder.tvSummonerName.setTextColor(ContextCompat.getColor(context, R.color.text_color));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle b = new Bundle();
                    b.putLong(SummonerDetailActivity.EXTRA_ID, Long.parseLong(entry.getPlayerOrTeamId()));
                    NavigationHandler.navigateTo(context, NavigationHandler.SUMMONER_DETAIL, b);
                }
            });

        }

        holder.itemView.setBackgroundColor(ContextCompat.getColor(context, position % 2 == 0 ? R.color.colorPrimary : R.color.colorPrimaryDark));
    }


    public String getCurrentShowingDivision(){
        return currentShowingDivision;
    }

    public void addEntries(String division, List<LeagueEntry> entries){
        this.currentShowingDivision = division;
        this.entries.clear();
        this.entries.addAll(entries);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public class EntryViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_position)
        TextView tvPosition;
        @Bind(R.id.tv_summoner_name)
        TextView tvSummonerName;
        @Bind(R.id.tv_wins)
        TextView tvWins;
        @Bind(R.id.tv_points)
        TextView tvPoints;

        public EntryViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
