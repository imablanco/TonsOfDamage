package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Queue;
import com.ablanco.teemo.model.leagues.League;
import com.ablanco.teemo.model.leagues.LeagueEntry;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.handler.StaticDataHandler;
import com.ablanco.tonsofdamage.ui.activities.SummonerDetailActivity;
import com.ablanco.tonsofdamage.utils.AnimationUtils;
import com.ablanco.tonsofdamage.utils.ErrorUtils;
import com.ablanco.tonsofdamage.utils.SummonerSuggestion;
import com.ablanco.tonsofdamage.utils.Utils;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.view.BodyTextView;
import com.arlib.floatingsearchview.util.view.IconImageView;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public class SummonersFragment extends BaseHomeFragment implements View.OnClickListener {

    @Bind(R.id.floating_search_view)
    FloatingSearchView mFloatingSearchView;
    @Bind(R.id.img_summoner)
    CircleImageView mImgSummoner;
    @Bind(R.id.tv_summoner_name)
    TextView mTvSummonerName;
    @Bind(R.id.img_solo_q)
    ImageView mImgSoloQ;
    @Bind(R.id.tv_solo_q_rank)
    TextView mTvSoloQRank;
    @Bind(R.id.tv_solo_q_name)
    TextView mTvSoloQName;
    @Bind(R.id.tv_solo_q_lp)
    TextView mTvSoloQLp;
    @Bind(R.id.tv_solo_q_wins)
    TextView mTvSoloQWins;
    @Bind(R.id.img_fvf_team)
    ImageView mImgFvfTeam;
    @Bind(R.id.tv_fvf_team_rank)
    TextView mTvFvfTeamRank;
    @Bind(R.id.tv_fvf_team_name)
    TextView mTvFvfTeamName;
    @Bind(R.id.img_tvt_team)
    ImageView mImgTvtTeam;
    @Bind(R.id.tv_tvt_team_rank)
    TextView mTvTvtTeamRank;
    @Bind(R.id.tv_tvt_team_name)
    TextView mTvTvtTeamName;
    @Bind(R.id.card_summoner_overview)
    View mSummonerLayout;
    @Bind(R.id.loading)
    View loading;

    private String mQuery;

    private long mSelectedSummoner;

    public static Fragment newInstance() {
        return new SummonersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summoners, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFloatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

                mQuery = newQuery;

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mFloatingSearchView.clearSuggestions();
                } else {
                    StaticDataHandler.getInstance().findSummoners(getContext(), newQuery, new StaticDataHandler.ResponseListener<List<SummonerSuggestion>>() {
                        @Override
                        public void onResponse(List<SummonerSuggestion> response) {
                            mFloatingSearchView.swapSuggestions(response);
                        }

                        @Override
                        public void onError(TeemoException e) {
                        }
                    });
                }
            }
        });

        mFloatingSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(IconImageView leftIcon, BodyTextView bodyText, SearchSuggestion item, int itemPosition) {

                //here you can set some attributes for the suggestion's left icon and text. For example,
                //you can choose your favorite image-loading library for setting the left icon's image.
                SummonerSuggestion summonerSuggestion = (SummonerSuggestion) item;
                Glide.with(getActivity()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getActivity()), String.valueOf(summonerSuggestion.getIconId()))).into(leftIcon);
            }
        });

        mFloatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                SummonerSuggestion suggestion = (SummonerSuggestion) searchSuggestion;
                loadSummoner(suggestion.getName());
            }

            @Override
            public void onSearchAction() {
                loadSummoner(mQuery);
            }
        });
    }

    private void loadSummoner(final String summonerName){
        initFields();
        loading.setVisibility(View.VISIBLE);
        Teemo.getInstance(getActivity()).getSummonersHandler().getSummonerByName(summonerName, new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(Summoner response) {
                if(getActivity() != null){
                    loading.setVisibility(View.GONE);
                    AnimationUtils.revealView(mSummonerLayout);
                    mSelectedSummoner = response.getId();
                    mSummonerLayout.setOnClickListener(SummonersFragment.this);

                    Glide.with(getActivity()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getActivity()), String.valueOf(response.getProfileIconId()))).into(mImgSummoner);
                    mTvSummonerName.setText(response.getName());
                    Teemo.getInstance(getContext()).getLeaguesHandler().getLeaguesBySummoners(Collections.singletonList(String.valueOf(mSelectedSummoner)), true, new ServiceResponseListener<Map<String, List<League>>>() {
                        @Override
                        public void onResponse(Map<String, List<League>> response) {
                            if(getActivity() != null && response.get(String.valueOf(mSelectedSummoner)) != null){
                                LeagueEntry entry;
                                for (League league : response.get(String.valueOf(mSelectedSummoner))){
                                    switch (league.getQueue()) {
                                        case Queue.RANKED_SOLO_5x5:
                                            mTvSoloQName.setText(league.getName());
                                            mTvSoloQName.setVisibility(View.VISIBLE);
                                            mImgSoloQ.setImageResource(Utils.getResourceForTier(league.getTier()));
                                            if (league.getEntries() != null && !league.getEntries().isEmpty()) {
                                                entry = league.getEntries().get(0);
                                                mTvSoloQRank.setText(league.getTier().concat(" ").concat(entry.getDivision()));
                                                mTvSoloQLp.setText(String.valueOf(entry.getLeaguePoints()).concat(" LP"));
                                                mTvSoloQWins.setText(String.valueOf(entry.getWins()).concat(" W / ").concat(String.valueOf(entry.getLosses()).concat(" L")));
                                                mTvSoloQLp.setVisibility(View.VISIBLE);
                                                mTvSoloQWins.setVisibility(View.VISIBLE);
                                            } else {
                                                mTvSoloQRank.setText(getString(R.string.unranked));
                                            }
                                            break;
                                        case Queue.RANKED_TEAM_5x5:
                                            mTvFvfTeamName.setText(league.getName());
                                            mTvFvfTeamName.setVisibility(View.VISIBLE);
                                            mImgFvfTeam.setImageResource(Utils.getResourceForTier(league.getTier()));
                                            if (league.getEntries() != null && !league.getEntries().isEmpty()) {
                                                entry = league.getEntries().get(0);
                                                mTvFvfTeamRank.setText(league.getTier().concat(" ").concat(entry.getDivision()));
                                            } else {
                                                mTvFvfTeamRank.setText(getString(R.string.unranked));
                                            }
                                            break;
                                        case Queue.RANKED_TEAM_3x3:
                                            mTvTvtTeamName.setText(league.getName());
                                            mTvTvtTeamName.setVisibility(View.VISIBLE);
                                            mImgTvtTeam.setImageResource(Utils.getResourceForTier(league.getTier()));
                                            if (league.getEntries() != null && !league.getEntries().isEmpty()) {
                                                entry = league.getEntries().get(0);
                                                mTvTvtTeamRank.setText(league.getTier().concat(" ").concat(entry.getDivision()));
                                            } else {
                                                mTvTvtTeamRank.setText(getString(R.string.unranked));
                                            }
                                            break;
                                    }
                                }
                            }
                        }

                        @Override
                        public void onError(TeemoException e) {}
                    });
                }
            }

            @Override
            public void onError(TeemoException e) {
                loading.setVisibility(View.GONE);
                ErrorUtils.showError(getView());
            }
        });
    }

    private void initFields(){
        mTvSoloQRank.setText(getString(R.string.unranked));
        mTvFvfTeamRank.setText(getString(R.string.unranked));
        mTvTvtTeamRank.setText(getString(R.string.unranked));
        mImgSoloQ.setImageResource(R.drawable.base_provisional);
        mImgFvfTeam.setImageResource(R.drawable.base_provisional);
        mImgTvtTeam.setImageResource(R.drawable.base_provisional);
        mTvSoloQName.setVisibility(View.GONE);
        mTvSoloQLp.setVisibility(View.GONE);
        mTvSoloQWins.setVisibility(View.GONE);
        mTvTvtTeamName.setVisibility(View.GONE);
        mTvFvfTeamName.setVisibility(View.GONE);
        mSummonerLayout.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        bundle.putLong(SummonerDetailActivity.EXTRA_ID, mSelectedSummoner);
        NavigationHandler.navigateTo(getActivity(), NavigationHandler.SUMMONER_DETAIL, bundle);
    }
}
