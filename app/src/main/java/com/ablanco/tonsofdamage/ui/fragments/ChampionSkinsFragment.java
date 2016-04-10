package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.ChampionsSkinsAdapter;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.ui.activities.ChampionSkinDetailActivity;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 09/04/2016.
 * TonsOfDamage
 */
public class ChampionSkinsFragment extends ChampionDetailBaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Fragment newInstance(ChampionDto championDto) {
        ChampionDetailBaseFragment f = new ChampionSkinsFragment();
        f.setChampion(championDto);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champion_skins, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ChampionsSkinsAdapter adapter = new ChampionsSkinsAdapter(getActivity(), mChampion.getKey(), mChampion.getSkins());
        adapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putString(ChampionSkinDetailActivity.EXTRA_SKIN_URL, ImageUris.getChampionSplashArt(mChampion.getKey(), mChampion.getSkins().get(position).getNum()));
                bundle.putString(ChampionSkinDetailActivity.EXTRA_SKIN_NAME, mChampion.getSkins().get(position).getName().equals("default") ? mChampion.getName() : mChampion.getSkins().get(position).getName());
                NavigationHandler.navigateTo(getActivity(), NavigationHandler.CHAMPION_SKIN_DETAIL, bundle);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }
}
