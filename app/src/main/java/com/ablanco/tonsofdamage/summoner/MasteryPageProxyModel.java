package com.ablanco.tonsofdamage.summoner;

import android.support.annotation.NonNull;

import com.ablanco.teemo.model.summoners.Mastery;
import com.ablanco.teemo.model.summoners.MasteryPage;

import java.util.List;

/**
 * Created by Álvaro Blanco on 27/05/2016.
 * TonsOfDamage
 */
public class MasteryPageProxyModel{

    private MasteryPage masteryPage;

    public MasteryPageProxyModel(@NonNull  MasteryPage masteryPage) {
        this.masteryPage = masteryPage;
    }

    public Boolean isCurrent() {
        return masteryPage.isCurrent();
    }

    public Long getId() {
        return masteryPage.getId();
    }


    public List<Mastery> getMasteries() {
        return masteryPage.getMasteries();
    }


    public String getName() {
        return masteryPage.getName();
    }

    public MasteryPage getMasteryPage() {
        return masteryPage;
    }

    @Override
    public String toString() {
        return masteryPage.getName();
    }
}
