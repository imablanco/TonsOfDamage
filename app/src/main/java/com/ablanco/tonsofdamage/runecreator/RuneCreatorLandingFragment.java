package com.ablanco.tonsofdamage.runecreator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.RunesAdapter;

import butterknife.Bind;

/**
 * Created by Álvaro Blanco on 03/07/2016.
 * TonsOfDamage
 */
public class RuneCreatorLandingFragment extends RuneCreatorBaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static Fragment newInstance(RuneCreatorFlowListener listener) {
        RuneCreatorBaseFragment f = new RuneCreatorLandingFragment();
        f.setRuneCreatorFlowListener(listener);
        return f;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.rune_creator, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add && mRuneCreatorFlowListener != null) {
            mRuneCreatorFlowListener.onAddButtonClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_rune_creator_landing, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(new RunesAdapter(getActivity()));
    }
}
