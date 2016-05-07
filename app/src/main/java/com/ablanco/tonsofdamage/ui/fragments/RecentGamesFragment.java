package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.games.Game;
import com.ablanco.teemo.model.games.RecentGames;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.SummonerSpellDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.RecentGamesAdapter;
import com.ablanco.tonsofdamage.adapter.RecentGamesData;
import com.ablanco.tonsofdamage.handler.SettingsHandler;

import java.util.Collections;
import java.util.Comparator;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func3;

/**
 * Created by Álvaro Blanco on 21/04/2016.
 * TonsOfDamage
 */
public class RecentGamesFragment extends BaseSummonerDetailFragment {

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    private RecentGamesAdapter adapter;

    public static Fragment newInstance(long id) {
        BaseSummonerDetailFragment f = new RecentGamesFragment();
        f.setSummonerId(id);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_summoner_recent_games, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecentGamesAdapter(getActivity());
        recyclerView.setAdapter(adapter);

        Teemo.getInstance(getActivity()).getGamesHandler().getRecentGames(summonerId, new ServiceResponseListener<RecentGames>() {
            @Override
            public void onResponse(RecentGames response) {

                Collections.sort(response.getGames(), new Comparator<Game>() {
                    @Override
                    public int compare(Game lhs, Game rhs) {
                        return rhs.getCreateDate().compareTo(lhs.getCreateDate());
                    }
                });


                for (final Game game : response.getGames()) {

                    Observable<RecentGamesData> s = Observable.zip(Observable.create(new Observable.OnSubscribe<ChampionDto>() {
                                @Override
                                public void call(final Subscriber<? super ChampionDto> subscriber) {
                                    if (getActivity() != null) {
                                        Teemo.getInstance(getActivity()).getStaticDataHandler().getChampionById(game.getChampionId(), SettingsHandler.getLanguage(getActivity()), null,
                                                new StaticAPIQueryParams.StaticQueryParamsBuilder().include(StaticAPIQueryParams.Champions.IMAGE).include(StaticAPIQueryParams.Champions.SKINS).build(), new ServiceResponseListener<ChampionDto>() {
                                            @Override
                                            public void onResponse(ChampionDto response) {
                                                subscriber.onNext(response);
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(TeemoException e) {
                                                subscriber.onError(e);
                                            }
                                        });
                                    }
                                }

                            }), Observable.create(new Observable.OnSubscribe<SummonerSpellDto>() {
                                @Override
                                public void call(final Subscriber<? super SummonerSpellDto> subscriber) {
                                    if (getActivity() != null) {
                                        Teemo.getInstance(getActivity()).getStaticDataHandler().getSummonerSpell(game.getSpell1(), SettingsHandler.getLanguage(getActivity()), null, StaticAPIQueryParams.SummonerSpells.image, new ServiceResponseListener<SummonerSpellDto>() {
                                            @Override
                                            public void onResponse(SummonerSpellDto response) {
                                                subscriber.onNext(response);
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(TeemoException e) {
                                                subscriber.onError(e);
                                            }
                                        });
                                    }
                                }

                            }), Observable.create(new Observable.OnSubscribe<SummonerSpellDto>() {
                                @Override
                                public void call(final Subscriber<? super SummonerSpellDto> subscriber) {
                                    if (getActivity() != null) {
                                        Teemo.getInstance(getActivity()).getStaticDataHandler().getSummonerSpell(game.getSpell2(), SettingsHandler.getLanguage(getActivity()), null, StaticAPIQueryParams.SummonerSpells.image, new ServiceResponseListener<SummonerSpellDto>() {
                                            @Override
                                            public void onResponse(SummonerSpellDto response) {
                                                subscriber.onNext(response);
                                                subscriber.onCompleted();
                                            }

                                            @Override
                                            public void onError(TeemoException e) {
                                                subscriber.onError(e);
                                            }
                                        });
                                    }
                                }

                            }),  new Func3<ChampionDto, SummonerSpellDto, SummonerSpellDto, RecentGamesData>() {
                                @Override
                                public RecentGamesData call(ChampionDto championDto, SummonerSpellDto summonerSpellDto, SummonerSpellDto summonerSpellDto2) {
                                    return new RecentGamesData(game, championDto, summonerSpellDto, summonerSpellDto2);
                                }
                            });

                            s.subscribe(new Subscriber<RecentGamesData>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    // TODO: 1/5/16 handle error
                                }

                                @Override
                                public void onNext(RecentGamesData recentGamesData) {
                                    adapter.addGame(recentGamesData);

                                }
                            });


                }
            }

            @Override
            public void onError(TeemoException e) {
                // TODO: 1/5/16 handle error
            }
        });

    }
}
