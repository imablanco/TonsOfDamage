package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.summoner.RuneProxyModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class RunesAdapter extends ItemClickAdapter<RunesAdapter.RuneHolder> {

    private final Context mContext;

    private List<RuneProxyModel> mRunes = new ArrayList<>();
    private LayoutInflater inflater;


    public RunesAdapter(Context context) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public RuneHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RuneHolder(inflater.inflate(R.layout.item_rune, parent, false));
    }

    @Override
    public void onBindViewHolder(RuneHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        RuneProxyModel rune = mRunes.get(position);

        if(rune.getRuneDto().getImage() != null){
            Glide.with(mContext).load(ImageUris.getRuneIcon(SettingsHandler.getCDNVersion(mContext), rune.getRuneDto().getImage().getFull())).into(holder.mImgRune);
        }

        holder.mTvRuneCount.setText(String.valueOf(rune.getCount()).concat("x"));
        holder.mTvRuneName.setText(Html.fromHtml(rune.getRuneDto().getName()));
    }

    @Override
    public int getItemCount() {
        return mRunes.size();
    }

    public void setRunes(List<RuneProxyModel> runes) {
        this.mRunes.clear();
        this.mRunes.addAll(runes);
        notifyDataSetChanged();
    }

    public class RuneHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_rune)
        ImageView mImgRune;
        @Bind(R.id.tv_rune_count)
        TextView mTvRuneCount;
        @Bind(R.id.tv_rune_name)
        TextView mTvRuneName;

        public RuneHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
