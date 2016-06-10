package com.ablanco.tonsofdamage.home;

import com.ablanco.teemo.model.championmastery.ChampionMasteryDto;
import com.ablanco.teemo.model.staticdata.ChampionDto;

/**
 * Created by Álvaro Blanco Cabrero on 10/6/16
 * TonsOfDamage
 */
public class SmartChampionMastery {

    private ChampionDto champion;
    private ChampionMasteryDto championMasteryDto;

    public SmartChampionMastery(ChampionDto champion, ChampionMasteryDto championMasteryDto) {
        this.champion = champion;
        this.championMasteryDto = championMasteryDto;
    }

    public ChampionDto getChampion() {
        return champion;
    }

    public void setChampion(ChampionDto champion) {
        this.champion = champion;
    }

    public ChampionMasteryDto getChampionMasteryDto() {
        return championMasteryDto;
    }

    public void setChampionMasteryDto(ChampionMasteryDto championMasteryDto) {
        this.championMasteryDto = championMasteryDto;
    }
}
