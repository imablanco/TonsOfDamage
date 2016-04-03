package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
public class ChampionListListAdapter extends ChampionsBaseAdapter<ChampionListListAdapter.ChampionHolder> {

    public ChampionListListAdapter(Context context) {
        super(context);
    }

    @Override
    public ChampionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ChampionHolder holder = new ChampionHolder(LayoutInflater.from(context).inflate(R.layout.item_champion_detailed, parent, false));

        holder.pgDifficult.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, R.color.magenta), android.graphics.PorterDuff.Mode.SRC_IN);

        holder.pgAttack.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);

        holder.pgTank.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, R.color.green), android.graphics.PorterDuff.Mode.SRC_IN);

        holder.pgMagic.getProgressDrawable().setColorFilter(
                ContextCompat.getColor(context, R.color.blue), android.graphics.PorterDuff.Mode.SRC_IN);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChampionHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ChampionDto championDto = champions.get(position);

        holder.tvChampionName.setText(championDto.getName());

        holder.tvChampionTitle.setText(championDto.getTitle());

        if(freeToPlayChampions.get(championDto.getId()) != null && freeToPlayChampions.get(championDto.getId())){
            holder.freeIcon.setVisibility(View.VISIBLE);
        }else {
            holder.freeIcon.setVisibility(View.GONE);
        }

        if(championDto.getInfo() != null){
            holder.pgAttack.setProgress(championDto.getInfo().getAttack());
            holder.pgTank.setProgress(championDto.getInfo().getDefense());
            holder.pgMagic.setProgress(championDto.getInfo().getMagic());
            holder.pgDifficult.setProgress(championDto.getInfo().getDifficulty());
        }

        Glide.clear(holder.imgChampionSquare);
        Glide.with(context).load(ImageUris.getChampionSquareIcon(championDto.getImage().getFull())).dontAnimate().into(holder.imgChampionSquare);

        if(championDto.getSkins() != null && !championDto.getSkins().isEmpty()){
            Glide.with(context).load(ImageUris.getChampionSplashArt(championDto.getName(), championDto.getSkins().get(0).getNum())).into(holder.imgChampionSplash);
        }
    }

    public class ChampionHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.pg_attack)
        ProgressBar pgAttack;

        @Bind(R.id.pg_tank)
        ProgressBar pgTank;

        @Bind(R.id.pg_magic)
        ProgressBar pgMagic;

        @Bind(R.id.pg_difficult)
        ProgressBar pgDifficult;

        @Bind(R.id.img_champion_splash)
        ImageView imgChampionSplash;

        @Bind(R.id.img_champion_square)
        ImageView imgChampionSquare;

        @Bind(R.id.tv_champion_name)
        TextView tvChampionName;

        @Bind(R.id.tv_champion_title)
        TextView tvChampionTitle;

        @Bind(R.id.ic_free)
        View freeIcon;


        public ChampionHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
