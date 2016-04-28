package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.SkinDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ChampionsSkinsAdapter extends ItemClickAdapter<ChampionsSkinsAdapter.SkinsViewHolder> {


    private final List<SkinDto> mSkins;
    private Context context;
    private String champioName;

    public ChampionsSkinsAdapter(Context context, String champion, List<SkinDto> skins) {
        this.mSkins = skins;
        this.context = context;
        this.champioName = champion;
    }

    @Override
    public SkinsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SkinsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_champion_skin, parent, false));
    }

    @Override
    public void onBindViewHolder(SkinsViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        SkinDto skinDto = mSkins.get(position);

        holder.mTvSkinName.setText(skinDto.getName().equals("default") ? champioName : skinDto.getName());
        Glide.clear(holder.mImgSkin);
        Glide.with(context).load(ImageUris.getChampionSplashArt(champioName, skinDto.getNum())).into(holder.mImgSkin);
    }

    @Override
    public int getItemCount() {
        return mSkins.size();
    }

    public class SkinsViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.img_skin)
        ImageView mImgSkin;
        @Bind(R.id.tv_skin_name)
        TextView mTvSkinName;

        public SkinsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
