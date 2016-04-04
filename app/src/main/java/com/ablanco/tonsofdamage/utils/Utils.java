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
}
