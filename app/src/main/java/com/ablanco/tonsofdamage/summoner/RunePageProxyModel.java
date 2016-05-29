package com.ablanco.tonsofdamage.summoner;

import android.support.annotation.NonNull;

import com.ablanco.teemo.model.summoners.RunePage;

/**
 * Created by Álvaro Blanco on 28/05/2016.
 * TonsOfDamage
 */
public class RunePageProxyModel {

    private RunePage page;

    public RunePageProxyModel(@NonNull RunePage page) {
        this.page = page;
    }

    public RunePage getPage() {
        return page;
    }

    @Override
    public String toString() {
        return page.getName();
    }
}
