package com.ablanco.tonsofdamage.utils;

import android.os.Parcel;

import com.ablanco.teemo.model.summoners.Summoner;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by Álvaro Blanco on 17/04/2016.
 * TonsOfDamage
 */
public class SummonerSuggestion implements SearchSuggestion {

    private String name;
    private Long level;
    private Integer iconId;

    public SummonerSuggestion(Summoner summoner){
        name = summoner.getName();
        level = summoner.getSummonerLevel();
        iconId = summoner.getProfileIconId();
    }

    public SummonerSuggestion(Parcel in) {
        name = in.readString();
        level = in.readLong();
        iconId = in.readInt();
    }

    public String getName() {
        return name;
    }

    public Long getLevel() {
        return level;
    }

    public Integer getIconId() {
        return iconId;
    }

    @Override
    public String getBody() {
        return name;
    }

    @Override
    public Creator getCreator() {
        return CREATOR;
    }

    public static final Creator<SummonerSuggestion> CREATOR = new Creator<SummonerSuggestion>() {
        @Override
        public SummonerSuggestion createFromParcel(Parcel in) {
            return new SummonerSuggestion(in);
        }

        @Override
        public SummonerSuggestion[] newArray(int size) {
            return new SummonerSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeLong(level);
        dest.writeInt(iconId);
    }

}