package com.ablanco.tonsofdamage.utils;

import android.content.Context;
import android.content.Intent;

import com.ablanco.tonsofdamage.ui.activities.FirstAccessSetupActivity;
import com.ablanco.tonsofdamage.ui.activities.HomeActivity;

/**
 * Created by Álvaro Blanco Cabrero on 27/3/16
 * TonsOfDamage
 */
public class NavigationHandler {

    public static final String SETUP = "SETUP";
    public static final String HOME = "HOME";

    private NavigationHandler(){}

    public static void navigateTo(Context context, String destination){
        Class clazz = getDestinationClass(destination);

        if(destination != null){
            context.startActivity(new Intent(context, clazz));
        }
    }

    private static Class getDestinationClass(String destination){
        Class classDestination = null;

        if(destination.equalsIgnoreCase(HOME)){
            classDestination = HomeActivity.class;
        } else if(destination.equalsIgnoreCase(SETUP)){
            classDestination = FirstAccessSetupActivity.class;
        }

        return classDestination;
    }
}
