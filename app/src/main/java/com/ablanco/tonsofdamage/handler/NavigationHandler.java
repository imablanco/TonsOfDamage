package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ablanco.tonsofdamage.champions.ChampionDetailActivity;
import com.ablanco.tonsofdamage.champions.ChampionSkinDetailActivity;
import com.ablanco.tonsofdamage.home.PlayerViewDialogActivity;
import com.ablanco.tonsofdamage.items.ItemDetailDialogActivity;
import com.ablanco.tonsofdamage.runecreator.RuneCreatorActivity;
import com.ablanco.tonsofdamage.settings.SettingsActivity;
import com.ablanco.tonsofdamage.splash.SetupActivity;
import com.ablanco.tonsofdamage.home.HomeActivity;
import com.ablanco.tonsofdamage.splash.SplashActivity;
import com.ablanco.tonsofdamage.summoner.MasteriesActivity;
import com.ablanco.tonsofdamage.summoner.MasteryDetailActivityDialog;
import com.ablanco.tonsofdamage.summoner.MatchDetailActivity;
import com.ablanco.tonsofdamage.summoner.RuneDetailDialogActivity;
import com.ablanco.tonsofdamage.summoner.RunesActivity;
import com.ablanco.tonsofdamage.summoner.SummonerDetailActivity;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class NavigationHandler {

    public static final String SETUP = "SETUP";
    public static final String SPLASH = "SPLASH";
    public static final String HOME = "HOME";
    public static final String CHAMPION_DETAIL = "CHAMPION_DETAIL";
    public static final String CHAMPION_SKIN_DETAIL = "CHAMPION_SKIN_DETAIL";
    public static final String SUMMONER_DETAIL = "SUMMONER_DETAIL";
    public static final String MATCH_DETAIL = "MATCH_DETAIL";
    public static final String MASTERIES_DETAIL = "MASTERIES_DETAIL";
    public static final String MASTERY_DETAIL = "MASTERY_DETAIL";
    public static final String RUNES_DETAIL = "RUNES_DETAIL";
    public static final String PLAYER_DETAIL = "PLAYER_DETAIL";
    public static final String ITEM_DETAIL = "ITEM_DETAIL";
    public static final String RUNE_DETAIL = "RUNE_DETAIL";
    public static final String SETTINGS = "SETTINGS";
    public static final String RUNE_CREATOR = "RUNE_CREATOR";

    private NavigationHandler(){}

    public static void navigateTo(Context context, String destination){
        navigateTo(context, destination, null, null);
    }

    public static void navigateTo(Context context, String destination, ActivityOptionsCompat options){
        navigateTo(context, destination, null, options);
    }

    public static void navigateTo(Context context, String destination, Bundle extras){
        navigateTo(context, destination, extras, null);
    }

    public static void navigateTo(Context context, String destination, Bundle extras, ActivityOptionsCompat options){
        Class clazz = getDestinationClass(destination);

        if(destination != null){
            Intent intent = new Intent(context, clazz);
            if(extras != null){
                intent.putExtras(extras);
            }

            if(destination.equalsIgnoreCase(HOME)){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(destination.equalsIgnoreCase(SPLASH)){
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            }

            if(options != null){
                context.startActivity(intent, options.toBundle());
            }else {
                context.startActivity(intent);
            }
        }
    }

    private static Class getDestinationClass(String destination){
        Class classDestination = null;

        if(destination.equalsIgnoreCase(HOME)){
            classDestination = HomeActivity.class;
        } else if(destination.equalsIgnoreCase(SETUP)){
            classDestination = SetupActivity.class;
        } else if(destination.equalsIgnoreCase(CHAMPION_DETAIL)){
            classDestination = ChampionDetailActivity.class;
        } else if(destination.equalsIgnoreCase(CHAMPION_SKIN_DETAIL)){
            classDestination = ChampionSkinDetailActivity.class;
        } else if(destination.equalsIgnoreCase(SUMMONER_DETAIL)){
            classDestination = SummonerDetailActivity.class;
        } else if(destination.equalsIgnoreCase(MATCH_DETAIL)){
            classDestination = MatchDetailActivity.class;
        } else if(destination.equalsIgnoreCase(MASTERIES_DETAIL)){
            classDestination = MasteriesActivity.class;
        } else if(destination.equalsIgnoreCase(MASTERY_DETAIL)){
            classDestination = MasteryDetailActivityDialog.class;
        } else if(destination.equalsIgnoreCase(RUNES_DETAIL)){
            classDestination = RunesActivity.class;
        } else if(destination.equalsIgnoreCase(PLAYER_DETAIL)){
            classDestination = PlayerViewDialogActivity.class;
        } else if(destination.equalsIgnoreCase(ITEM_DETAIL)){
            classDestination = ItemDetailDialogActivity.class;
        } else if(destination.equalsIgnoreCase(RUNE_DETAIL)){
            classDestination = RuneDetailDialogActivity.class;
        } else if(destination.equalsIgnoreCase(SETTINGS)){
            classDestination = SettingsActivity.class;
        } else if(destination.equalsIgnoreCase(SPLASH)){
            classDestination = SplashActivity.class;
        } else if(destination.equalsIgnoreCase(RUNE_CREATOR)){
            classDestination = RuneCreatorActivity.class;
        }

        return classDestination;
    }
}
