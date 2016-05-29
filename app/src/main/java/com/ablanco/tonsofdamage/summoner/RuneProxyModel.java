package com.ablanco.tonsofdamage.summoner;

import com.ablanco.teemo.model.staticdata.RuneDto;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class RuneProxyModel {
    
    private RuneDto runeDto;
    private int count;

    public RuneProxyModel(RuneDto runeDto, int count) {
        this.runeDto = runeDto;
        this.count = count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public RuneDto getRuneDto() {
        return runeDto;
    }

    public int getCount() {
        return count;
    }
}
