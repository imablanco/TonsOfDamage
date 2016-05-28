package com.ablanco.tonsofdamage.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.ResourcesHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 10/04/2016.
 * TonsOfDamage
 */
public class ItemFilterAdapter extends RecyclerView.Adapter<ItemFilterAdapter.CheckBoxViewHolder> {


    private final Context mContext;
    private List<String> tags;
    private List<String> mSelectedTags = new ArrayList<>();

    private CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked){
                if(!mSelectedTags.contains(buttonView.getTag().toString())) mSelectedTags.add(buttonView.getTag().toString());
            }else {
                mSelectedTags.remove(buttonView.getTag().toString());
            }
        }
    };

    public ItemFilterAdapter(Context context,  List<String> tags, List<String> mSelectedTags){
        this.tags = tags;
        this.mSelectedTags = mSelectedTags;
        this.mContext = context;
    }

    @Override
    public CheckBoxViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CheckBoxViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_checkbox, parent, false));
    }

    @Override
    public void onBindViewHolder(CheckBoxViewHolder holder, int position) {

        holder.checkBox.setText(ResourcesHandler.getInstance(mContext).getResourceForKey(tags.get(position))); // TODO: 10/04/2016 change for sanitized description
        holder.checkBox.setTag(tags.get(position));
        holder.checkBox.setChecked(mSelectedTags.contains(tags.get(position)));
        holder.checkBox.setOnCheckedChangeListener(checkedChangeListener);
    }


    class CheckBoxViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.checkbox)
        CheckBox checkBox;

        public CheckBoxViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public List<String> getSelectedTags(){
        return mSelectedTags;
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
