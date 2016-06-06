package com.ablanco.tonsofdamage.summoner;

/**
 * Created by Álvaro Blanco Cabrero on 5/6/16
 * TonsOfDamage
 */
public class RuneStat {
    private String statName;
    private Double statValue;

    public RuneStat(String statName, Double statValue) {
        this.statName = statName;
        this.statValue = statValue;
    }

    public String getStatName() {
        return statName;
    }

    public Double getStatValue() {
        return statValue;
    }


    public void setStatValue(Double statValue) {
        this.statValue = statValue;
    }
}
