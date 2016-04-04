package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.ablanco.teemo.model.staticdata.ChampionDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public abstract class ChampionsBaseAdapter<VH extends RecyclerView.ViewHolder> extends ItemClickAdapter<VH> {


    protected Context context;
    protected List<ChampionDto> champions = new ArrayList<>();
    protected Map<Integer, Boolean> freeToPlayChampions = new HashMap<>();

    public ChampionsBaseAdapter(Context context){
        this.context = context;
    }

    public void setFreeToPlayChampions(Map<Integer, Boolean> freeToPlayChampions){
        this.freeToPlayChampions = freeToPlayChampions;
        notifyDataSetChanged();
    }

    public void setChampions(List<ChampionDto> champions){
        this.champions.clear();
        notifyDataSetChanged();
        this.champions.addAll(champions);
        notifyDataSetChanged();
    }
    public ChampionDto getItemAtPosition(int position){
        return champions.get(position);
    }

    @Override
    public final int getItemCount() {
        return champions.size();
    }
}
