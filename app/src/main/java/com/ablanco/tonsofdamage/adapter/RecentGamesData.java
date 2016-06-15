package com.ablanco.tonsofdamage.adapter;

import com.ablanco.teemo.model.games.Game;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.SummonerSpellDto;

import java.io.Serializable;

/**
 * Created by Álvaro Blanco on 21/04/2016.
 * TonsOfDamage
 */
public class RecentGamesData implements Serializable{

    private Game game;
    private Long summonerId;
    private SummonerSpellDto summonerSpellDto1;
    private SummonerSpellDto summonerSpellDto2;
    private ChampionDto championDto;

    public RecentGamesData(Game game, long summonerId, ChampionDto championDto, SummonerSpellDto summonerSpellDto1, SummonerSpellDto summonerSpellDto2) {
        this.game = game;
        this.summonerSpellDto1 = summonerSpellDto1;
        this.summonerSpellDto2 = summonerSpellDto2;
        this.summonerId = summonerId;
        this.championDto = championDto;
    }


    public SummonerSpellDto getSummonerSpellDto1() {
        return summonerSpellDto1;
    }

    public SummonerSpellDto getSummonerSpellDto2() {
        return summonerSpellDto2;
    }

    public Game getGame() {
        return game;
    }

    public ChampionDto getChampionDto() {
        return championDto;
    }

    public Long getSummonerId() {
        return summonerId;
    }
}
