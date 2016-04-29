package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.Regions;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Animationutils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Álvaro Blanco on 30/03/2016.
 * TonsOfDamage
 */
public class ChooseRegionFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecycler;
    @Bind(R.id.fab_choose_region)
    FloatingActionButton mChooseRegionFab;
    @Bind(R.id.tv_selected_region)
    TextView tvSelectedRegion;

    private String selectedRegion;

    public static Fragment newInstance() {
        return new ChooseRegionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_region, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setAdapter(new RegionAdapter(Regions.getAll()));
    }

    public void revealChooseRegionFab() {

        if (mChooseRegionFab.getVisibility() != View.VISIBLE) {
            Animationutils.revealView(mChooseRegionFab);
        }
    }

    @OnClick(R.id.fab_choose_region)
    public void setRegionAndNavigate() {
        SettingsHandler.setRegion(getActivity(), selectedRegion);
        Teemo.getInstance(getActivity()).setRegion(selectedRegion);

        Teemo.getInstance(getActivity()).getStaticDataHandler().getVersions(new ServiceResponseListener<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                if (getActivity() != null) {
                    SettingsHandler.setCDNVersion(getActivity(), response.get(0));
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.content, ChooseLanguageFragment.newInstance())
                            .commit();
                }

            }

            @Override
            public void onError(TeemoException e) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .replace(R.id.content, ChooseLanguageFragment.newInstance())
                            .commit();
                }
            }
        });

    }

    class RegionAdapter extends RecyclerView.Adapter<RegionAdapter.ViewHolder> {

        private List<String> regions;

        public RegionAdapter(List<String> regions) {
            this.regions = regions;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.tv.setText(regions.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedRegion = regions.get(holder.getAdapterPosition());
                    revealChooseRegionFab();
                    tvSelectedRegion.setText(selectedRegion);
                }
            });
        }


        @Override
        public int getItemCount() {
            return regions.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.tv)
            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
