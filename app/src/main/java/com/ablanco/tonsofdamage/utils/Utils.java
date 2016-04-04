package com.ablanco.tonsofdamage.utils;

import android.text.TextUtils;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class Utils {

    public static String buildStaticQueryParams(String... params){
        StringBuilder queryParamsBuilder = new StringBuilder();

        for (String param : params){
            if(!TextUtils.isEmpty(queryParamsBuilder)){
                queryParamsBuilder.append(",");
            }

            queryParamsBuilder.append(param);
        }

        return queryParamsBuilder.toString();
    }

    public static String getChampionAbilityVideoUrl(int championId, int abilityNumber){
        return Constants.CHAMPION_VIDEO_URI.replace("{0}",championId <= 10 ? ("00").concat(String.valueOf(championId)) : championId <= 100 ?("0").concat(String.valueOf(championId)) : String.valueOf(championId)).replace("{1}", String.valueOf(abilityNumber));
    }
}
