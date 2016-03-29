package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.constants.Regions;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.dialogs.BaseAnimatedDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class ChooseRegionDialogFragment extends BaseAnimatedDialog {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    private List<String> mRegions = new ArrayList<>();
    private Action1<String> callback;


    public static ChooseRegionDialogFragment newInstance(Action1<String> callback){
        ChooseRegionDialogFragment f = new ChooseRegionDialogFragment();
        f.setCallback(callback);
        return f;
    }

    public ChooseRegionDialogFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogTheme);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_choose_region, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        mRegions.addAll(Regions.getAll());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new RegionAdapter());

    }

    public void setCallback(Action1<String> callback){
        this.callback = callback;
    }

    class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder>{

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_choose_region, parent, false));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.tv.setText(mRegions.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(callback != null){
                        callback.call(mRegions.get(position));
                        dismiss();
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return mRegions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            @Bind(R.id.tv) TextView tv;
            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
