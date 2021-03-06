package com.ablanco.tonsofdamage.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.model.staticdata.ItemDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.items.ItemDetailDialogActivity;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 10/04/2016.
 * TonsOfDamage
 */
public class ItemsGridAdapter extends RecyclerView.Adapter<ItemsGridAdapter.ItemViewHolder> {

    private final Context context;
    private final List<ItemDto> items = new ArrayList<>();



    public ItemsGridAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_grid_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final ItemDto item = items.get(position);
        holder.mTvItemName.setText(item.getName());
        Glide.clear(holder.mImgItem);
        Glide.with(context).load(ImageUris.getItemIcon(SettingsHandler.getCDNVersion(context), String.valueOf(item.getId()))).into(holder.mImgItem);

        if(item.getGold() != null){
            holder.mTvPrice.setVisibility(View.VISIBLE);
            holder.mTvPrice.setText(Utils.getItemPrice(item.getGold().getTotal(), item.getGold().getBase()));
            holder.mIcCoins.setImageResource(R.drawable.ic_gold);
            holder.mIcCoins.setVisibility(View.VISIBLE);
        }else {
            holder.mIcCoins.setVisibility(View.GONE);
            holder.mTvPrice.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.setTransitionNameForView(holder.mImgItem, String.valueOf(item.getId()));

                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.mImgItem, String.valueOf(item.getId()));
                Bundle bundle = new Bundle();
                bundle.putInt(ItemDetailDialogActivity.EXTRA_ID_ITEM, item.getId());
                NavigationHandler.navigateTo(context, NavigationHandler.ITEM_DETAIL, bundle, options);
            }
        });
    }

    public void setItems(List<ItemDto> items) {
        this.items.clear();
        notifyDataSetChanged();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public ItemDto getItemAtPosition(int position){
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.img_item)
        ImageView mImgItem;
        @Bind(R.id.tv_item_name)
        TextView mTvItemName;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.ic_coins)
        ImageView mIcCoins;

        public ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
