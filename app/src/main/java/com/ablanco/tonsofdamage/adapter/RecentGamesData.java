package com.ablanco.tonsofdamage.adapter;

import android.support.v4.util.Pair;

import com.ablanco.teemo.model.games.Game;
import com.ablanco.teemo.model.matches.MatchDetail;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.SummonerSpellDto;

/**
 * Created by Álvaro Blanco on 21/04/2016.
 * TonsOfDamage
 */
public class RecentGamesData {

    private Game game;
    private Pair<SummonerSpellDto, SummonerSpellDto> summonerSpells;
    private ChampionDto championDto;
    private MatchDetail matchDetail;

    public RecentGamesData(Game game, ChampionDto championDto,MatchDetail detail, SummonerSpellDto summonerSpellDto1, SummonerSpellDto summonerSpellDto2) {
        this.game = game;
        this.summonerSpells = new Pair<>(summonerSpellDto1, summonerSpellDto2);
        this.championDto = championDto;
        this.matchDetail = detail;
    }

    public Pair<SummonerSpellDto, SummonerSpellDto> getSummonerSpells() {
        return summonerSpells;
    }

    public Game getGame() {
        return game;
    }

    public ChampionDto getChampionDto() {
        return championDto;
    }

    public MatchDetail getMatchDetail() {
        return matchDetail;
    }
}
