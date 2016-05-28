package com.ablanco.tonsofdamage.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import java.util.Arrays;

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


    protected void showViews(View... views){
        ButterKnife.apply(Arrays.asList(views), ENABLE);
    }

    protected void hideViews(View... views){
        ButterKnife.apply(Arrays.asList(views), DISABLE);

    }

    private final ButterKnife.Action<View> DISABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setVisibility(View.GONE);
        }
    };

    private final ButterKnife.Action<View> ENABLE = new ButterKnife.Action<View>() {
        @Override public void apply(View view, int index) {
            view.setVisibility(View.VISIBLE);
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
