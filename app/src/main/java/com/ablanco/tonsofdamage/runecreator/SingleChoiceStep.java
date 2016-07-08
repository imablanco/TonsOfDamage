package com.ablanco.tonsofdamage.runecreator;

/**
 * Created by Álvaro Blanco on 05/07/2016.
 * TonsOfDamage
 */
public class SingleChoiceStep {

    private String explain;
    private int runeId;
    private int runeAmount;

    public SingleChoiceStep(String explain, int runeId, int runeAmount) {
        this.explain = explain;
        this.runeId = runeId;
        this.runeAmount = runeAmount;
    }

    public String getExplain() {
        return explain;
    }

    public int getRuneId() {
        return runeId;
    }

    public int getRuneAmount() {
        return runeAmount;
    }
}
