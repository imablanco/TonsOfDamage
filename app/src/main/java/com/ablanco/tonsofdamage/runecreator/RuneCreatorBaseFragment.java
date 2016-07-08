package com.ablanco.tonsofdamage.runecreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ablanco.tonsofdamage.base.BaseFragment;

/**
 * Created by Álvaro Blanco on 03/07/2016.
 * TonsOfDamage
 */
public class RuneCreatorBaseFragment extends BaseFragment {

    protected RuneCreatorFlowListener mRuneCreatorFlowListener;

    public void setRuneCreatorFlowListener(RuneCreatorFlowListener listener) {
        this.mRuneCreatorFlowListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = false;
        if(item.getItemId() == android.R.id.home && mRuneCreatorFlowListener != null){
            handled = true;
            mRuneCreatorFlowListener.onBackNavigationButtonClicked();
        }
        return handled;
    }

}
