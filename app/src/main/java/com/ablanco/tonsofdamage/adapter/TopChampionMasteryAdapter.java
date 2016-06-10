package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.TextView;

import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.home.SmartChampionMastery;
import com.bumptech.glide.Glide;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco Cabrero on 10/6/16
 * TonsOfDamage
 */
public class TopChampionMasteryAdapter extends ItemClickAdapter<TopChampionMasteryAdapter.ChampionViewHolder> {


    private List<SmartChampionMastery> masteryDtos = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public TopChampionMasteryAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChampionViewHolder(inflater.inflate(R.layout.item_summoner_champion_mastery, parent, false));
    }

    @Override
    public void onBindViewHolder(ChampionViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        SmartChampionMastery championMastery = masteryDtos.get(position);

        if(championMastery.getChampion().getImage() != null){
            Glide.with(context).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(context), championMastery.getChampion().getImage().getFull())).into(holder.championImg);
        }

        holder.tvChampionLevel.setText(String.valueOf(championMastery.getChampionMasteryDto().getChampionLevel()));

        SeriesItem championSinceMasteryItem = new SeriesItem.Builder(ContextCompat.getColor(context, R.color.magenta))
                .setRange(0, championMastery.getChampionMasteryDto().getChampionPointsSinceLastLevel() + championMastery.getChampionMasteryDto().getChampionPointsUntilNextLevel(), 0)
                .setInitialVisibility(false)
                .setCapRounded(false)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .build();


        int index = holder.dvChampionMastery.addSeries(championSinceMasteryItem);

        holder.dvChampionMastery.addEvent(new DecoEvent.Builder(championMastery.getChampionMasteryDto().getChampionPointsSinceLastLevel())
                .setIndex(index)
                .setDuration(1500)
                .setDelay(300)
                .build());

    }

    public SmartChampionMastery getItemAtPosition(int position){
        return masteryDtos.get(position);
    }

    public void addChampionMastery(SmartChampionMastery smartChampionMastery){
        this.masteryDtos.add(smartChampionMastery);
        Collections.sort(masteryDtos, new Comparator<SmartChampionMastery>() {
            @Override
            public int compare(SmartChampionMastery lhs, SmartChampionMastery rhs) {
                return lhs.getChampion().getId().compareTo(rhs.getChampion().getId());
            }
        });

        notifyItemInserted(masteryDtos.indexOf(smartChampionMastery));
    }

    @Override
    public int getItemCount() {
        return masteryDtos.size();
    }

    public class ChampionViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.champion_img)
        CircleImageView championImg;
        @Bind(R.id.dv_champion_mastery)
        DecoView dvChampionMastery;
        @Bind(R.id.tv_champion_level)
        TextView tvChampionLevel;

        public ChampionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
