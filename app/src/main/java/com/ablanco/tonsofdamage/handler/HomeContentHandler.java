package com.ablanco.tonsofdamage.handler;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.ui.fragments.ChampionsFragment;
import com.ablanco.tonsofdamage.ui.fragments.HomeFragment;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class HomeContentHandler {

    public final static int HOME = 0;
    public final static int CHAMPIONS = 1;

    private FragmentManager mManager;

    public HomeContentHandler(FragmentManager fragmentManager){
        this.mManager = fragmentManager;
    }

    public void showContent(int content){

        FragmentTransaction transaction = mManager.beginTransaction();
        Fragment fragment;

        switch (content){
            case HOME:default:
                fragment = HomeFragment.newInstance();
                break;
            case CHAMPIONS:
                fragment = ChampionsFragment.newInstance();
                break;
        }

        transaction.replace(R.id.content, fragment).commit();

    }

    public String getTitleForContent(Context context, int content){
        switch (content){
            case HOME:default:
                return context.getString(R.string.title_home);
            case CHAMPIONS:
                return context.getString(R.string.title_champions);
        }
    }
}
