package com.ablanco.tonsofdamage.champions;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.counters.CounterPickTask;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco Cabrero on 18/6/16
 * TonsOfDamage
 */
public class ChampionCounterPicksFragment extends ChampionDetailBaseFragment{

    @Bind(R.id.recycler_view_weaks)
    RecyclerView recyclerViewWeaks;
    @Bind(R.id.recycler_view_strongs)
    RecyclerView recyclerStrongs;

    private List<ChampionDto> mWeaks = new ArrayList<>();
    private List<ChampionDto> mStrongs = new ArrayList<>();

    private CounterPickTask mWeaksTask;
    private CounterPickTask mStrongsTask;

    private CounterPicksAdapter weaksAdapter;
    private CounterPicksAdapter strongsAdapter;

    public static Fragment newInstance(ChampionDto champion) {
        ChampionDetailBaseFragment f = new ChampionCounterPicksFragment();
        f.setChampion(champion);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champion_counter_picks, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerStrongs.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        recyclerViewWeaks.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        weaksAdapter = new CounterPicksAdapter(getActivity(), mWeaks);
        strongsAdapter = new CounterPicksAdapter(getActivity(), mStrongs);
        recyclerStrongs.setAdapter(strongsAdapter);
        recyclerViewWeaks.setAdapter(weaksAdapter);

        mWeaksTask = new CounterPickTask(getActivity(), CounterPickTask.COUNTERS_WEAK, new ServiceResponseListener<List<ChampionDto>>() {
            @Override
            public void onResponse(List<ChampionDto> response) {
                mWeaks.addAll(response);
                weaksAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(TeemoException e) {
                // TODO: 18/6/16 handler error

            }
        });

        mStrongsTask = new CounterPickTask(getActivity(), CounterPickTask.COUNTERS_STRONG, new ServiceResponseListener<List<ChampionDto>>() {
            @Override
            public void onResponse(List<ChampionDto> response) {
                mStrongs.addAll(response);
                strongsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(TeemoException e) {
                // TODO: 18/6/16 handler error
            }
        });

        weaksAdapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, mWeaks.get(position).getId());
                NavigationHandler.navigateTo(getActivity(), NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });

        strongsAdapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, mStrongs.get(position).getId());
                NavigationHandler.navigateTo(getActivity(), NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });

        mWeaksTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mChampion.getKey());
        mStrongsTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mChampion.getKey());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mWeaksTask != null && (mWeaksTask.getStatus() == AsyncTask.Status.RUNNING || mWeaksTask.getStatus() == AsyncTask.Status.PENDING)){
            mWeaksTask.cancel(true);
        }

        if(mStrongsTask != null && (mStrongsTask.getStatus() == AsyncTask.Status.RUNNING || mStrongsTask.getStatus() == AsyncTask.Status.PENDING)){
            mStrongsTask.cancel(true);
        }
    }

    class CounterPicksAdapter extends ItemClickAdapter<CounterPicksAdapter.CounterPickViewHolder>{

        private final Context context;
        private List<ChampionDto> champions;
        private LayoutInflater inflater;

        public CounterPicksAdapter(Context context, List<ChampionDto> champions){
            this.champions = champions;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public CounterPickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CounterPickViewHolder(inflater.inflate(R.layout.item_free_champion, parent, false));
        }

        @Override
        public void onBindViewHolder(CounterPickViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);

            if(context != null){
                Glide.with(context).load(ImageUris.getChampionSquareIcon(SettingsHandler.getCDNVersion(getContext()), champions.get(position).getImage().getFull())).into(holder.imgChampion);
            }
            holder.tvChampionName.setText(champions.get(position).getName());

        }

        @Override
        public int getItemCount() {
            return champions.size();
        }

        public class CounterPickViewHolder extends RecyclerView.ViewHolder{
            @Bind(R.id.img_champion)
            CircleImageView imgChampion;
            @Bind(R.id.tv_champion_name)
            TextView tvChampionName;

            public CounterPickViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
