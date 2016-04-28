package com.ablanco.tonsofdamage.adapter;

import com.ablanco.teemo.model.stats.AggregatedStats;

/**
 * Created by Álvaro Blanco on 18/04/2016.
 * TonsOfDamage
 */
public class ChampionStatsData {
    private AggregatedStats stats;
    private String name;

    public ChampionStatsData(AggregatedStats stats, String name) {
        this.stats = stats;
        this.name = name;
    }

    public AggregatedStats getStats() {
        return stats;
    }

    public String getName() {
        return name;
    }
}
