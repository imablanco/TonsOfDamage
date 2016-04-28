package com.ablanco.tonsofdamage.ui.fragments;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.views.AvatarImageView;
import com.ablanco.tonsofdamage.utils.Animationutils;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class PickSummonerFragment extends BaseFragment {

    @Bind(R.id.floating_search_view) FloatingSearchView floatingSearchView;
    @Bind(R.id.profile_image) AvatarImageView avatarImageView;
    @Bind(R.id.tv_summoner_name) TextView tvSummonerName;
    @Bind(R.id.tv_summoner_region) TextView tvSummonerRegion;
    @Bind(R.id.loading) ProgressBar loading;
    @Bind(R.id.fab_continue) FloatingActionButton fabContinue;


    private String mSearchQuery = "";
    private Summoner mSummoner;

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

                search();
            }
        });

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                mSearchQuery = newQuery;
            }
        });
    }

    private void search(){
        avatarImageView.setPlaceholder();
        avatarImageView.hideLevel();
        loading.setVisibility(View.VISIBLE);
        hideViews(tvSummonerName, tvSummonerRegion, fabContinue);

        Teemo.getInstance(getActivity()).getSummonersHandler().getSummonerByName(mSearchQuery, new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(final Summoner response) {
                if(getActivity() != null){
                    mSummoner = response;
                    Glide.with(getActivity()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getActivity()), String.valueOf(response.getProfileIconId()))).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            loading.setVisibility(View.GONE);
                            tvSummonerRegion.setText(getString(R.string.region).concat(" ").concat(SettingsHandler.getRegion(getActivity()).toUpperCase()));
                            tvSummonerName.setText(response.getName());
                            Animationutils.revealView(fabContinue);
                            avatarImageView.setImage(resource);
                            avatarImageView.setLevel(response.getSummonerLevel());
                            avatarImageView.showLevel();
                            showViews(tvSummonerRegion, tvSummonerName, avatarImageView);

                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            loading.setVisibility(View.GONE);
                            avatarImageView.hideLevel();
                            avatarImageView.setPlaceholder();
                        }
                    });
                }
            }

            @Override
            public void onError(TeemoException e) {
                loading.setVisibility(View.GONE);
                if (getView() != null) {
                    Snackbar.make(getView(), R.string.summoner_not_found, Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick(R.id.fab_continue)
    public void navigateToHome(){
        SettingsHandler.setSummoner(getActivity(), mSummoner.getId());
        Teemo.getInstance(getActivity()).getStaticDataHandler().getVersions(new ServiceResponseListener<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                SettingsHandler.setCDNVersion(getActivity(), response.get(0));
                NavigationHandler.navigateTo(getActivity(), NavigationHandler.HOME);
                getActivity().finish();
            }

            @Override
            public void onError(TeemoException e) {
                NavigationHandler.navigateTo(getActivity(), NavigationHandler.HOME);
                getActivity().finish();
            }
        });

    }

}
