package com.ablanco.tonsofdamage.summoner;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Queue;
import com.ablanco.teemo.model.leagues.League;
import com.ablanco.teemo.model.leagues.LeagueEntry;
import com.ablanco.teemo.model.summoners.Summoner;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.LeaguesEntryAdapter;
import com.ablanco.tonsofdamage.utils.AnimationUtils;
import com.ablanco.tonsofdamage.utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco Cabrero on 8/6/16
 * TonsOfDamage
 */
public class LeagueRankingFragment extends BaseSummonerDetailFragment {

    @Bind(R.id.img_league)
    ImageView imgLeague;
    @Bind(R.id.spinner_division)
    Spinner spinnerDivision;
    @Bind(R.id.tv_league_name)
    TextView tvLeagueName;
    @Bind(R.id.tv_tier)
    TextView tvTier;
    @Bind(R.id.tv_no_league)
    TextView tvNoLeague;
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.cv_league)
    View cvLeague;
    @Bind(R.id.loading)
    View loading;

    private LeagueEntry myEntry;

    private ArrayAdapter<String> divisionsAdapter;
    private LeaguesEntryAdapter adapter;

    private Map<String, List<LeagueEntry>> mLeagueEntriesByDivision = new HashMap<>();

    private List<String> mDivisions = new ArrayList<>();

    private Map<String, List<List<String>>> mSummonerIdsByDivision = new HashMap<>();
    private Map<String, List<Summoner>> mSummonersByDivision = new HashMap<>();

    public static BaseSummonerDetailFragment newInstance(long summonerId) {
        BaseSummonerDetailFragment f = new LeagueRankingFragment();
        f.setSummonerId(summonerId);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summoner_ranked_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        divisionsAdapter = new ArrayAdapter<String>(getActivity(), R.layout.simple_spinner_item, mDivisions);
        divisionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerDivision.setAdapter(divisionsAdapter);
        spinnerDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                List<LeagueEntry> entries = mLeagueEntriesByDivision.get(mDivisions.get(position));

                if(entries != null){
                    adapter.addEntries(mDivisions.get(position), entries);
                    recyclerView.smoothScrollToPosition(0);
                }

                //download pending summoners
                if(mSummonerIdsByDivision.get(mDivisions.get(position)) != null && !mSummonerIdsByDivision.get(mDivisions.get(position)).isEmpty()){
                    getSummoners(mDivisions.get(position), mSummonerIdsByDivision.get(mDivisions.get(position)));
                }

                //already downloaded summoners to fill the list
                if(mSummonersByDivision.get(mDivisions.get(position)) != null){
                    adapter.addSummoners(mSummonersByDivision.get(mDivisions.get(position)));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LeaguesEntryAdapter(getActivity(), summonerId);
        recyclerView.setAdapter(adapter);

        Teemo.getInstance(getActivity()).getLeaguesHandler().getLeaguesBySummoners(Collections.singletonList(String.valueOf(summonerId)), false, new ServiceResponseListener<Map<String, List<League>>>() {
            @Override
            public void onResponse(Map<String, List<League>> response) {
                if(getActivity() != null){
                    boolean found = false;
                    if(response.get(String.valueOf(summonerId)) != null){
                        for (League league : response.get(String.valueOf(summonerId))) {
                            if(league.getQueue().equals(Queue.RANKED_SOLO_5x5)){
                                found = true;
                                loading.setVisibility(View.GONE);
                                AnimationUtils.revealView(cvLeague);
                                fillData(league);
                                break;
                            }
                        }
                    }

                    if(!found){
                        tvNoLeague.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onError(TeemoException e) {
               if(getActivity() != null){
                   tvNoLeague.setVisibility(View.VISIBLE);
                   loading.setVisibility(View.GONE);
               }
            }
        });
    }

    private void fillData(League league){
        if(league.getTier() != null){
            imgLeague.setImageResource(Utils.getResourceForTier(league.getTier()));
        }

        tvLeagueName.setText(league.getName());
        tvTier.setText(league.getTier());

        for (LeagueEntry leagueEntry : league.getEntries()) {
            if(mLeagueEntriesByDivision.get(leagueEntry.getDivision()) == null){
                mLeagueEntriesByDivision.put(leagueEntry.getDivision(), new ArrayList<LeagueEntry>());
            }
            mLeagueEntriesByDivision.get(leagueEntry.getDivision()).add(leagueEntry);
            if(leagueEntry.getPlayerOrTeamId() != null && leagueEntry.getPlayerOrTeamId().equals(String.valueOf(summonerId))){
                myEntry = leagueEntry;
            }

        }

        buildSummonersIdsList();

        //add found divisions to spinner
        mDivisions.addAll(mLeagueEntriesByDivision.keySet());

        //sort by name
        Collections.sort(mDivisions, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        });

        for (String s : mLeagueEntriesByDivision.keySet()) {
            Collections.sort(mLeagueEntriesByDivision.get(s), new Comparator<LeagueEntry>() {
                @Override
                public int compare(LeagueEntry lhs, LeagueEntry rhs) {
                    return rhs.getLeaguePoints().compareTo(lhs.getLeaguePoints());
                }
            });
        }

        //found index of division for current summoner
        divisionsAdapter.notifyDataSetChanged();

        if(myEntry != null){
            int index = mDivisions.indexOf(myEntry.getDivision());
            if(index >= 0){
                spinnerDivision.setSelection(index);
            }
        }

    }

    private void buildSummonersIdsList(){
        List<String> summonerIds;

        for (String divisionKey : mLeagueEntriesByDivision.keySet()) {
            summonerIds = new ArrayList<>();
            for (LeagueEntry leagueEntry : mLeagueEntriesByDivision.get(divisionKey)) {

                if(mSummonerIdsByDivision.get(divisionKey) == null){
                    mSummonerIdsByDivision.put(divisionKey, new ArrayList<List<String>>());
                }

                //fill a list for every 40 summoners (is the maximum size the api is allowed to handle at once)
                summonerIds.add(leagueEntry.getPlayerOrTeamId());

                if(summonerIds.size() % 40 == 0){
                    mSummonerIdsByDivision.get(divisionKey).add(summonerIds);
                    summonerIds = new ArrayList<>();
                }
            }

            //add remaing summoners
            if(!summonerIds.isEmpty()){
                mSummonerIdsByDivision.get(divisionKey).add(summonerIds);
            }
        }
    }

    private void getSummoners(final String division, List<List<String>> summonerIds){

        for (final List<String> id : summonerIds) {
            Teemo.getInstance(getActivity()).getSummonersHandler().getSummonersByIds(id, new ServiceResponseListener<Map<String, Summoner>>() {
                @Override
                public void onResponse(Map<String, Summoner> response) {
                    if(mSummonersByDivision.get(division) == null){
                        mSummonersByDivision.put(division, new ArrayList<Summoner>());
                    }

                    mSummonersByDivision.get(division).addAll(response.values());

                    //remove already downloaded ids from list
                    mSummonerIdsByDivision.get(division).remove(id);

                    if(adapter.getCurrentShowingDivision().equals(division)){
                        adapter.addSummoners(new ArrayList<Summoner>(response.values()));
                    }
                }

                @Override
                public void onError(TeemoException e) {}
            });
        }
    }

}
