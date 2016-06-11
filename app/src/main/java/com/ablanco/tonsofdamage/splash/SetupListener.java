package com.ablanco.tonsofdamage.splash;

/**
 * Created by Álvaro Blanco Cabrero on 11/6/16
 * TonsOfDamage
 */
public interface SetupListener {
    void onRegionSelected(String region);
    void onLanguageSelected(String lang);
    void onSummonerSelected(Long summoner);
}
