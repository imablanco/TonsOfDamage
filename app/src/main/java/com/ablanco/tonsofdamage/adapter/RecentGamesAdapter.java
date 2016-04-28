package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 21/04/2016.
 * TonsOfDamage
 */
public class RecentGamesAdapter extends RecyclerView.Adapter<RecentGamesAdapter.GameViewHolder> {


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
    public void onBindViewHolder(GameViewHolder holder, int position) {

        RecentGamesData data = mGames.get(position);
        Glide.with(mContext).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(mContext), data.getChampionDto().getImage().getFull())).into(holder.mImgChampionSquare);
        Glide.with(mContext).load(ImageUris.getSummonerSpellIcon(SettingsHandler.getCDNVersion(mContext), String.valueOf(data.getSummonerSpells().first.getImage().getFull()))).into(holder.mImgSpell1);
        Glide.with(mContext).load(ImageUris.getSummonerSpellIcon(SettingsHandler.getCDNVersion(mContext), String.valueOf(data.getSummonerSpells().second.getImage().getFull()))).into(holder.mImgSpell2);
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public void addGame(RecentGamesData data){
        this.mGames.add(data);
        notifyItemInserted(mGames.size() - 1);
    }

    public class GameViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_champion_square)
        ImageView mImgChampionSquare;
        @Bind(R.id.img_spell1)
        ImageView mImgSpell1;
        @Bind(R.id.img_spell2)
        ImageView mImgSpell2;

        public GameViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
