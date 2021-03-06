package com.ablanco.tonsofdamage.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.champions.Champion;
import com.ablanco.teemo.model.champions.ChampionList;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco Cabrero on 8/6/16
 * TonsOfDamage
 */
public class WeekRotationPlaceholder extends HomeViewPlaceholder implements HomePlaceholder {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.loading)
    ProgressBar loading;
    @Bind(R.id.tv_error)
    TextView tvError;

    private List<ChampionDto> champions = new ArrayList<>();

    private FreeChampionsAdapter adapter;
    private boolean isFirstTime = true;

    public WeekRotationPlaceholder(Context context) {
        this(context, null);
    }

    public WeekRotationPlaceholder(Context context, AttributeSet attrs) {
        super(context, attrs);

        inflate(context, R.layout.ph_week_rotation, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        adapter = new FreeChampionsAdapter();
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, champions.get(position).getId());
                NavigationHandler.navigateTo(getContext(), NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });

    }

    @Override
    public void update(boolean forceUpdate) {

        if(forceUpdate || isFirstTime){
            isFirstTime = false;
            Teemo.getInstance(getContext()).getChampionsHandler().getChampions(true, new ServiceResponseListener<ChampionList>() {
                @Override
                public void onResponse(ChampionList response) {
                    loading.setVisibility(GONE);
                    for (Champion champion : response.getChampions()) {
                        Teemo.getInstance(getContext()).getStaticDataHandler().getChampionById(champion.getId(), SettingsHandler.getLanguage(getContext()), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                            @Override
                            public void onResponse(ChampionDto response) {
                                adapter.addChampion(response);
                            }

                            @Override
                            public void onError(TeemoException e) {

                            }
                        });
                    }
                }

                @Override
                public void onError(TeemoException e) {
                    loading.setVisibility(GONE);
                    tvError.setVisibility(VISIBLE);
                }
            });


            champions.clear();
            adapter.notifyDataSetChanged();
            tvError.setVisibility(GONE);
        }

    }

    class FreeChampionsAdapter extends ItemClickAdapter<FreeChampionsAdapter.ChampionViewHolder> {

        @Override
        public ChampionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ChampionViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_free_champion, parent, false));
        }

        @Override
        public void onBindViewHolder(ChampionViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            Glide.with(getContext()).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), champions.get(position).getImage().getFull())).into(holder.imgChampion);
            holder.tvChampionName.setText(champions.get(position).getName());

        }

        public void addChampion(ChampionDto champion){
            champions.add(champion);

            Collections.sort(champions, new Comparator<ChampionDto>() {
                @Override
                public int compare(ChampionDto lhs, ChampionDto rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });

            notifyItemInserted(champions.indexOf(champion));
        }

        @Override
        public int getItemCount() {
            return champions.size();
        }

        public class ChampionViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.img_champion)
            CircleImageView imgChampion;
            @Bind(R.id.tv_champion_name)
            TextView tvChampionName;

            public ChampionViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
