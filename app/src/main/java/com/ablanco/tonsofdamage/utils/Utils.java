package com.ablanco.tonsofdamage.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.ablanco.teemo.constants.LeagueTier;
import com.ablanco.teemo.constants.Platform;
import com.ablanco.teemo.constants.Regions;
import com.ablanco.teemo.model.games.RawStats;
import com.ablanco.teemo.model.stats.AggregatedStats;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class Utils {

    private static ColorFilter grayScaleFilter;

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
        String[] splittedLang = lang.split("_");
        if (splittedLang.length == 2) {
            cfg.locale = new Locale(splittedLang[0], splittedLang[1]);

            Locale.setDefault(cfg.locale);
            ctx.getResources().updateConfiguration(cfg,
                    ctx.getResources().getDisplayMetrics());
            ctx.getResources().updateConfiguration(cfg, null);
        }

    }

    public static void setTransitionNameForView(View view, String transitionName) {
        ViewCompat.setTransitionName(view, transitionName);

    }

    public static String getFormattedStats(Integer value) {
        if (value > 1000000) {
            return String.format(Locale.getDefault(), "%.1fM", value.doubleValue() / 1000000);
        } else if (value > 1000) {
            return String.format(Locale.getDefault(), "%.1fK", value.doubleValue() / 1000);
        } else return String.valueOf(value);
    }

    public static void applyGrayScaleFilter(ImageView imageView) {
        if (grayScaleFilter == null) {
            ColorMatrix matrix = new ColorMatrix();
            matrix.setSaturation(0);
            grayScaleFilter = new ColorMatrixColorFilter(matrix);
        }
        imageView.setColorFilter(grayScaleFilter);
    }

    public static String getDeviceModel() {
        return Build.MANUFACTURER + " " + Build.MODEL;
    }

    public static String getDeviceOS() {
        return "Android " + Build.VERSION.RELEASE + " (" + Build.VERSION.SDK_INT + ")";
    }

    public static String getAppVersion(Context ctx) {
        PackageInfo pInfo;
        try {
            pInfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }

    }

    public static void resetColorFilter(ImageView imageView) {
        imageView.setColorFilter(null);
    }

    public static boolean isContextValid(Context context){
        return !((Activity)context).isFinishing();
    }


    public static List<Locale> getAvailableLocales(){
        return Arrays.asList(Locale.US,  new Locale("es","ES"));
    }

    public static String getPlatformForRegion(Context context){
        switch (SettingsHandler.getRegion(context)){
            case Regions.REGION_EUW:default:
                return Platform.EUW1;
            case Regions.REGION_BR:
                return Platform.BR1;
            case Regions.REGION_EUNE:
                return Platform.EUN1;
            case Regions.REGION_KR:
                return Platform.KR;
            case Regions.REGION_LAN:
                return Platform.LA1;
            case Regions.REGION_LAS:
                return Platform.LA2;
            case Regions.REGION_NA:
                return Platform.NA1;
            case Regions.REGION_OCE:
                return Platform.OC1;
            case Regions.REGION_RU:
                return Platform.RU;
            case Regions.REGION_TR:
                return Platform.TR1;
        }
    }
}
