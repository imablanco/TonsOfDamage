package com.ablanco.tonsofdamage.home;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;

import com.ablanco.tonsofdamage.base.BaseFragment;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class BaseHomeFragment extends BaseFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //remove previously menu items added
        if(menu != null){
            menu.clear();
        }
    }
}
