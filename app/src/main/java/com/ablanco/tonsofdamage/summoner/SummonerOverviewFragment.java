package com.ablanco.tonsofdamage.summoner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Queue;
import com.ablanco.teemo.constants.Season;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.championmastery.ChampionMasteryDto;
import com.ablanco.teemo.model.leagues.League;
import com.ablanco.teemo.model.leagues.LeagueEntry;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.stats.ChampionStats;
import com.ablanco.teemo.model.stats.RankedStats;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.teemo.utils.ImageUris;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ChampionStatsData;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.adapter.SummonerMostPlayedChampionsAdapter;
import com.ablanco.tonsofdamage.adapter.TopChampionMasteryAdapter;
import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.home.SmartChampionMastery;
import com.ablanco.tonsofdamage.utils.ErrorUtils;
import com.ablanco.tonsofdamage.utils.Utils;
import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public class SummonerOverviewFragment extends BaseSummonerDetailFragment {

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
    @Bind(R.id.recycler_view)
    RecyclerView mostPlayedChampionsRecyclerView;
    @Bind(R.id.tv_summoner_level)
    TextView mTvSummonerLevel;
    @Bind(R.id.cv_most_played_champions)
    View cvMostPlayedChampions;
    @Bind(R.id.recycler_view_top_champion_mastery)
    RecyclerView recyclerTopChampionMastery;
    @Bind(R.id.cv_champion_mastery)
    View cvChampionMastery;

    private SummonerMostPlayedChampionsAdapter mostPlayedChampionsAdapter;
    private TopChampionMasteryAdapter topChampionMasteryAdapter;

    public static Fragment newInstance(long id) {
        BaseSummonerDetailFragment fragment = new SummonerOverviewFragment();
        fragment.setSummonerId(id);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summoner_detail_overview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mostPlayedChampionsRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mostPlayedChampionsAdapter = new SummonerMostPlayedChampionsAdapter(getActivity());
        mostPlayedChampionsRecyclerView.setAdapter(mostPlayedChampionsAdapter);

        recyclerTopChampionMastery.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        topChampionMasteryAdapter = new TopChampionMasteryAdapter(getActivity());
        recyclerTopChampionMastery.setAdapter(topChampionMasteryAdapter);

        topChampionMasteryAdapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, topChampionMasteryAdapter.getItemAtPosition(position).getChampion().getId());
                NavigationHandler.navigateTo(getContext(), NavigationHandler.CHAMPION_DETAIL, bundle);
            }
        });

        loadTopChampions();
        loadSummoner();
    }

    private void loadTopChampions() {
        Teemo.getInstance(getActivity()).getChampionMasteryHandler().getTopChampionsMastery(Utils.getPlatformForRegion(getActivity()), summonerId, 3, new ServiceResponseListener<List<ChampionMasteryDto>>() {
            @Override
            public void onResponse(List<ChampionMasteryDto> response) {
                if (getActivity() != null) {
                    cvChampionMastery.setVisibility(View.VISIBLE);
                    for (final ChampionMasteryDto championMasteryDto : response) {
                        Teemo.getInstance(getActivity()).getStaticDataHandler().getChampionById(championMasteryDto.getChampionId().intValue(), SettingsHandler.getLanguage(getActivity()), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                            @Override
                            public void onResponse(ChampionDto response) {
                                topChampionMasteryAdapter.addChampionMastery(new SmartChampionMastery(response, championMasteryDto));
                            }

                            @Override
                            public void onError(TeemoException e) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }

    private void loadSummoner() {
        Teemo.getInstance(getActivity()).getSummonersHandler().getSummonerById(String.valueOf(summonerId), new ServiceResponseListener<Summoner>() {
            @Override
            public void onResponse(Summoner response) {
                if (getActivity() != null) {

                    Glide.with(getActivity()).load(ImageUris.getProfileIcon(SettingsHandler.getCDNVersion(getActivity()), String.valueOf(response.getProfileIconId()))).into(mImgSummoner);
                    mTvSummonerName.setText(response.getName());
                    mTvSummonerLevel.setText(String.valueOf(response.getSummonerLevel()));
                    loadRankedInfo();
                    loadMostPlayedChampions();
                }
            }

            @Override
            public void onError(TeemoException e) {
                ErrorUtils.showError(getView());
            }
        });
    }

    private void loadMostPlayedChampions() {
        Teemo.getInstance(getActivity()).getStatsHandler().getRankedStatsBySummonerAndSeason(summonerId, Season.SEASON2016, new ServiceResponseListener<RankedStats>() {
            @Override
            public void onResponse(RankedStats response) {
                if (getActivity() != null) {
                    cvMostPlayedChampions.setVisibility(View.VISIBLE);
                    List<ChampionStats> mostPlayed = response.getChampions();
                    Collections.sort(mostPlayed, new Comparator<ChampionStats>() {
                        @Override
                        public int compare(ChampionStats lhs, ChampionStats rhs) {
                            return rhs.getStats().getTotalSessionsPlayed().compareTo(lhs.getStats().getTotalSessionsPlayed());
                        }
                    });

                    Iterator<ChampionStats> championStatsIterator = mostPlayed.iterator();

                    while (championStatsIterator.hasNext()) {
                        if (championStatsIterator.next().getId() == 0) {
                            championStatsIterator.remove();
                        }
                    }

                    if (mostPlayed.size() >= 4) { ////
                        mostPlayed = mostPlayed.subList(0, 4);
                    }

                    for (final ChampionStats mostPlayedChamp : mostPlayed) {
                        if (getActivity() != null) {
                            Teemo.getInstance(getActivity()).getStaticDataHandler().getChampionById(mostPlayedChamp.getId(), SettingsHandler.getLanguage(getActivity()), null, StaticAPIQueryParams.Champions.IMAGE, new ServiceResponseListener<ChampionDto>() {
                                @Override
                                public void onResponse(ChampionDto response) {
                                    mostPlayedChampionsAdapter.addChampion(new ChampionStatsData(mostPlayedChamp.getStats(), response.getId(), response.getImage().getFull()));

                                }

                                @Override
                                public void onError(TeemoException e) {

                                }
                            });
                        }

                    }
                }

            }

            @Override
            public void onError(TeemoException e) {
            }
        });
    }

    private void loadRankedInfo() {
        Teemo.getInstance(getContext()).getLeaguesHandler().getLeaguesBySummoners(Collections.singletonList(String.valueOf(summonerId)), true, new ServiceResponseListener<Map<String, List<League>>>() {
            @Override
            public void onResponse(Map<String, List<League>> response) {
                if (getActivity() != null && response.get(String.valueOf(summonerId)) != null) {
                    LeagueEntry entry;
                    for (League league : response.get(String.valueOf(summonerId))) {
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
            public void onError(TeemoException e) {
            }
        });
    }


}
