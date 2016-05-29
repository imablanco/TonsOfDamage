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
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;
import com.daasuu.ahp.AnimateHorizontalProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ChampionsAdapter extends ItemClickAdapter {

    private static final int VIEW_TYPE_GRID = 0;
    private static final int VIEW_TYPE_LIST = 1;

    protected Context context;
    protected List<ChampionDto> champions = new ArrayList<>();
    protected Map<Integer, Boolean> freeToPlayChampions = new HashMap<>();

    private boolean isGrid = true;

    public ChampionsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        return isGrid ? VIEW_TYPE_GRID : VIEW_TYPE_LIST;
    }

    @Override
    public int getItemCount() {
        return champions.size();
    }

    public void toggleMode(boolean isGrid){
        this.isGrid = isGrid;
        notifyDataSetChanged();
    }

    public ChampionDto getItemAtPosition(int position){
        return champions.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_GRID){
            return new ChampionGridHolder(LayoutInflater.from(context).inflate(R.layout.item_champion, parent, false));
        }else{
            return new ChampionListViewHolder(LayoutInflater.from(context).inflate(R.layout.item_champion_detailed, parent, false));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ChampionDto championDto = champions.get(position);

        if(holder instanceof ChampionGridHolder){

            ((ChampionGridHolder)holder).name.setText(championDto.getName());

            if(freeToPlayChampions.get(championDto.getId()) != null && freeToPlayChampions.get(championDto.getId())){
                ((ChampionGridHolder)holder).freeIcon.setVisibility(View.VISIBLE);
            }else {
                ((ChampionGridHolder)holder).freeIcon.setVisibility(View.GONE);
            }

            Glide.with(context).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(context), championDto.getImage().getFull())).into(((ChampionGridHolder)holder).img);

        } else if(getItemViewType(position) == VIEW_TYPE_LIST){

            ((ChampionListViewHolder)holder).tvChampionName.setText(championDto.getName());
            ((ChampionListViewHolder)holder).tvChampionTitle.setText(championDto.getTitle());

            if(freeToPlayChampions.get(championDto.getId()) != null && freeToPlayChampions.get(championDto.getId())){
                ((ChampionListViewHolder)holder).freeIcon.setVisibility(View.VISIBLE);
            }else {
                ((ChampionListViewHolder)holder).freeIcon.setVisibility(View.GONE);
            }

            ((ChampionListViewHolder)holder).pgAttack.setProgress(0);
            ((ChampionListViewHolder)holder).pgTank.setProgress(0);
            ((ChampionListViewHolder)holder).pgMagic.setProgress(0);
            ((ChampionListViewHolder)holder).pgDifficult.setProgress(0);

            if(championDto.getInfo() != null){
                ((ChampionListViewHolder)holder).pgAttack.setProgressWithAnim(championDto.getInfo().getAttack()*100);
                ((ChampionListViewHolder)holder).pgTank.setProgressWithAnim(championDto.getInfo().getDefense()*100);
                ((ChampionListViewHolder)holder).pgMagic.setProgressWithAnim(championDto.getInfo().getMagic()*100);
                ((ChampionListViewHolder)holder).pgDifficult.setProgressWithAnim(championDto.getInfo().getDifficulty()*100);
            }

            Glide.with(context).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(context), championDto.getImage().getFull())).dontAnimate().into(((ChampionListViewHolder)holder).imgChampionSquare);

            if(championDto.getSkins() != null && !championDto.getSkins().isEmpty()){
                Glide.clear(((ChampionListViewHolder)holder).imgChampionSplash);
                Glide.with(context).load(ImageUris.getChampionSplashArt(championDto.getKey(), championDto.getSkins().get(0).getNum())).into(((ChampionListViewHolder)holder).imgChampionSplash);
            }
        }
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

    public class ChampionGridHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tv_name) TextView name;
        @Bind(R.id.img) ImageView img;
        @Bind(R.id.ic_free) View freeIcon;


        public ChampionGridHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ChampionListViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.pg_attack)
        AnimateHorizontalProgressBar  pgAttack;

        @Bind(R.id.pg_tank)
        AnimateHorizontalProgressBar  pgTank;

        @Bind(R.id.pg_magic)
        AnimateHorizontalProgressBar pgMagic;

        @Bind(R.id.pg_difficult)
        AnimateHorizontalProgressBar  pgDifficult;

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


        public ChampionListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
