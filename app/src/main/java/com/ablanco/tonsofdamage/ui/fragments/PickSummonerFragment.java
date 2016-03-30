package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.utils.SettingsHandler;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class PickSummonerFragment extends BaseFragment {

    @Bind(R.id.floating_search_view)
    FloatingSearchView floatingSearchView;

    @Bind(R.id.profile_image)
    CircleImageView circleImageView;

    @Bind(R.id.tv_summoner_name)
    TextView tvSummonerName;

    @Bind(R.id.tv_summoner_level)
    TextView tvSummonerLevel;

    @Bind(R.id.tv_summoner_region)
    TextView tvSummonerRegion;


    private String mSearchQuery = "";

    public static Fragment newInstance(){
        return new PickSummonerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pick_summoner, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction() {
                search(mSearchQuery);
            }
        });

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mSearchQuery = newQuery;
            }
        });
    }

    private void search(final String summonerName){
        Teemo.getInstance(getActivity()).getSummonersHandler().getSummonerByName(summonerName, new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(Summoner response) {
                Glide.with(PickSummonerFragment.this).load(ImageUris.getProfileIcon(String.valueOf(response.getProfileIconId()))).into(circleImageView);
                tvSummonerName.setText(response.getName());
                tvSummonerLevel.setVisibility(View.VISIBLE);
                tvSummonerLevel.setText(String.valueOf(response.getSummonerLevel()));
                tvSummonerRegion.setText(SettingsHandler.getRegion(getActivity()));
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }
}
