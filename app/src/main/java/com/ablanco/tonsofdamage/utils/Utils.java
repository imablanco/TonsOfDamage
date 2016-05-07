package com.ablanco.tonsofdamage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.ablanco.teemo.constants.LeagueTier;
import com.ablanco.teemo.model.games.RawStats;
import com.ablanco.teemo.model.stats.AggregatedStats;
import com.ablanco.tonsofdamage.R;

import java.util.Locale;

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

    public static String getItemPrice(Integer total, Integer base) {
        StringBuilder builder = new StringBuilder();

        if (total != null) {
            builder.append(String.valueOf(total));
        }

        if (base != null) {
            builder.append(" (");
            builder.append(String.valueOf(base));
            builder.append(")");
        }

        return builder.toString();
    }

    public static
    @DrawableRes
    int getResourceForTier(String tier) {
        if (tier.equalsIgnoreCase(LeagueTier.BRONZE)) {
            return R.drawable.base_bronze;
        } else if (tier.equalsIgnoreCase(LeagueTier.SILVER)) {
            return R.drawable.base_silver;
        } else if (tier.equalsIgnoreCase(LeagueTier.GOLD)) {
            return R.drawable.base_gold;
        } else if (tier.equalsIgnoreCase(LeagueTier.PLATINUM)) {
            return R.drawable.base_platinum;
        } else if (tier.equalsIgnoreCase(LeagueTier.DIAMOND)) {
            return R.drawable.base_diamond;
        } else if (tier.equalsIgnoreCase(LeagueTier.CHALLENGER)) {
            return R.drawable.base_challenger;
        } else if (tier.equalsIgnoreCase(LeagueTier.MASTER)) {
            return R.drawable.base_master;
        }

        return R.drawable.base_provisional;
    }

    public static double getKDA(AggregatedStats stats) {
        return ((double) stats.getTotalChampionKills() + (double) stats.getTotalAssists()) / (double) stats.getTotalDeathsPerSession();
    }

    public static String getAverage(AggregatedStats stats) {
        return String.format(Locale.getDefault(), "%.0f/%.0f/%.0f", (double) stats.getTotalChampionKills() / (double) stats.getTotalSessionsPlayed(),
                (double) stats.getTotalDeathsPerSession() / (double) stats.getTotalSessionsPlayed(),
                (double) stats.getTotalAssists() / (double) stats.getTotalSessionsPlayed());
    }

    public static String getGameScore(RawStats stats) {
        return String.format(Locale.getDefault(), "%d/%d/%d", stats.getChampionsKilled(),
                stats.getNumDeaths(), stats.getAssists());
    }

    public static double getWinRatio(AggregatedStats stats) {
        if (stats.getTotalSessionsLost() == 0) return 100;
        return (double) stats.getTotalSessionsWon() * 100 / (double) stats.getTotalSessionsPlayed();
    }

    public static void updateLanguage(Context ctx, String lang) {
        Configuration cfg = new Configuration();
        if (lang != null && !lang.isEmpty())
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();

        Locale.setDefault(cfg.locale);
        ctx.getResources().updateConfiguration(cfg,
                ctx.getResources().getDisplayMetrics());
        ctx.getResources().updateConfiguration(cfg, null);
    }

    public static void setTransitionNameForView(View view, String transitionName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName(transitionName);
        }
    }

    public static String getFormattedStats(Integer value){
        if(value > 1000000){
            return String.format(Locale.getDefault(), "%.1fM", value.doubleValue()/ 1000000);
        }else if(value > 1000){
            return String.format(Locale.getDefault(), "%.1fK", value.doubleValue()/ 1000);
        }else return String.valueOf(value);
    }
}
