package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;

import com.ablanco.tonsofdamage.ui.activities.ChampionDetailActivity;
import com.ablanco.tonsofdamage.ui.activities.ChampionSkinDetailActivity;
import com.ablanco.tonsofdamage.ui.activities.FirstAccessSetupActivity;
import com.ablanco.tonsofdamage.ui.activities.HomeActivity;
import com.ablanco.tonsofdamage.ui.activities.MatchDetailActivity;
import com.ablanco.tonsofdamage.ui.activities.SummonerDetailActivity;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class NavigationHandler {

    public static final String SETUP = "SETUP";
    public static final String HOME = "HOME";
    public static final String CHAMPION_DETAIL = "CHAMPION_DETAIL";
    public static final String CHAMPION_SKIN_DETAIL = "CHAMPION_SKIN_DETAIL";
    public static final String SUMMONER_DETAIL = "SUMMONER_DETAIL";
    public static final String MATCH_DETAIL = "MATCG_DETAIL";

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
            classDestination = FirstAccessSetupActivity.class;
        } else if(destination.equalsIgnoreCase(CHAMPION_DETAIL)){
            classDestination = ChampionDetailActivity.class;
        } else if(destination.equalsIgnoreCase(CHAMPION_SKIN_DETAIL)){
            classDestination = ChampionSkinDetailActivity.class;
        } else if(destination.equalsIgnoreCase(SUMMONER_DETAIL)){
            classDestination = SummonerDetailActivity.class;
        } else if(destination.equalsIgnoreCase(MATCH_DETAIL)){
            classDestination = MatchDetailActivity.class;
        }

        return classDestination;
    }
}
