package com.ablanco.tonsofdamage.adapter;

import com.ablanco.teemo.model.stats.AggregatedStats;

/**
 * Created by Álvaro Blanco on 18/04/2016.
 * TonsOfDamage
 */
public class ChampionStatsData {
    private AggregatedStats stats;
    private String name;
    private int id;

    public ChampionStatsData(AggregatedStats stats, int id, String name) {
        this.stats = stats;
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public AggregatedStats getStats() {
        return stats;
    }

    public String getName() {
        return name;
    }
}
