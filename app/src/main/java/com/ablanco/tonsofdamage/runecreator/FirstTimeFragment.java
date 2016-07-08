package com.ablanco.tonsofdamage.runecreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.tonsofdamage.R;

import butterknife.OnClick;

/**
 * Created by Álvaro Blanco on 03/07/2016.
 * TonsOfDamage
 */
public class FirstTimeFragment extends RuneCreatorBaseFragment {

    public static Fragment newInstance(RuneCreatorFlowListener listener){
        RuneCreatorBaseFragment f = new FirstTimeFragment();
        f.setRuneCreatorFlowListener(listener);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_rune_creator_first_time, container, false);
    }

    @OnClick(R.id.fab_new_rune_page)
    public void onAddClicked(){
        if(mRuneCreatorFlowListener != null){
            mRuneCreatorFlowListener.onAddButtonClicked();
        }
    }
}
