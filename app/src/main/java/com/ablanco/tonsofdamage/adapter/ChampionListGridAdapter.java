package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ChampionListGridAdapter extends ChampionsBaseAdapter<ChampionListGridAdapter.ChampionViewHolder> {


    public ChampionListGridAdapter(Context context) {
        super(context);
    }

    @Override
    public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChampionViewHolder(LayoutInflater.from(context).inflate(R.layout.item_champion, parent, false));
    }

    @Override
    public void onBindViewHolder(ChampionViewHolder holder, int position) {

        super.onBindViewHolder(holder, position);

        ChampionDto championDto = champions.get(position);

        holder.name.setText(championDto.getName());

        if(freeToPlayChampions.get(championDto.getId()) != null && freeToPlayChampions.get(championDto.getId())){
            holder.freeIcon.setVisibility(View.VISIBLE);
        }else {
            holder.freeIcon.setVisibility(View.GONE);
        }

        Glide.clear(holder.img);
        Glide.with(context).load(ImageUris.getChampionSquareIcon(championDto.getImage().getFull())).into(holder.img);
    }


    public class ChampionViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_name) TextView name;
        @Bind(R.id.img) ImageView img;
        @Bind(R.id.ic_free) View freeIcon;


        public ChampionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}