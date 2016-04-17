package com.ablanco.tonsofdamage.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ablanco.teemo.constants.LeagueTier;
import com.ablanco.tonsofdamage.R;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class Utils {

    public static String buildStaticQueryParams(String... params) {
        StringBuilder queryParamsBuilder = new StringBuilder();

        for (String param : params) {
            if (!TextUtils.isEmpty(queryParamsBuilder)) {
                queryParamsBuilder.append(",");
            }

            queryParamsBuilder.append(param);
        }

        return queryParamsBuilder.toString();
    }

    public static String getChampionAbilityVideoUrl(int championId, int abilityNumber) {
        return Constants.CHAMPION_VIDEO_URI.replace("{0}", championId <= 10 ? ("00").concat(String.valueOf(championId)) : championId <= 100 ? ("0").concat(String.valueOf(championId)) : String.valueOf(championId)).replace("{1}", String.valueOf(abilityNumber));
    }

    public static void hideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getItemPrice(Integer total, Integer base){
        StringBuilder builder = new StringBuilder();

        if(total != null){
            builder.append(String.valueOf(total));
        }

        if(base != null){
            builder.append(" (");
            builder.append(String.valueOf(base));
            builder.append(")");
        }

        return builder.toString();
    }

    public static @DrawableRes int getResourceForTier(String tier){
        if(tier.equalsIgnoreCase(LeagueTier.BRONZE)){
            return R.drawable.base_bronze;
        }else if(tier.equalsIgnoreCase(LeagueTier.SILVER)){
            return R.drawable.base_silver;
        }else if(tier.equalsIgnoreCase(LeagueTier.GOLD)){
            return R.drawable.base_gold;
        }else if(tier.equalsIgnoreCase(LeagueTier.PLATINUM)){
            return R.drawable.base_platinum;
        }else if(tier.equalsIgnoreCase(LeagueTier.DIAMOND)){
            return R.drawable.base_diamond;
        }else if(tier.equalsIgnoreCase(LeagueTier.CHALLENGER)){
            return R.drawable.base_challenger;
        }else if(tier.equalsIgnoreCase(LeagueTier.MASTER)){
            return R.drawable.base_master;
        }

        return R.drawable.base_provisional;
    }
}
