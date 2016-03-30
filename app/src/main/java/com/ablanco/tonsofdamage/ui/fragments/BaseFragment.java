package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 *
 * Base fragment that binds Views with ButterKnife once onViewCreated has been called. Its mandatory to do view related operations after this call and make sure to call super {@link #onViewCreated(View, Bundle)}
 */
public class BaseFragment extends Fragment {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}
