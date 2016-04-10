package com.ablanco.tonsofdamage.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public abstract class ItemClickAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    public interface OnItemClickListener{
        void onItemClicked(int position);
    }

    protected OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mOnItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final VH holder, int position) {
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClicked(holder.getAdapterPosition());
                }
            });
        }
    }
}
