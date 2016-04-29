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
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.handler.SettingsHandler;
import com.ablanco.tonsofdamage.utils.Animationutils;
import com.ablanco.tonsofdamage.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by Álvaro Blanco on 29/04/2016.
 * TonsOfDamage
 */
public class ChooseLanguageFragment extends BaseFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecycler;
    @Bind(R.id.fab_choose_language)
    FloatingActionButton mChooseLanguageFab;
    @Bind(R.id.tv_selected_language)
    TextView tvSelectedLanguage;

    private List<Locale> mLanguages = new ArrayList<>();
    private Locale selectedLocale;
    private LanguageAdapter mLanguageAdapter;

    public static Fragment newInstance() {
        return new ChooseLanguageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_language, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLanguageAdapter = new LanguageAdapter(mLanguages);
        mRecycler.setAdapter(mLanguageAdapter);

        Teemo.getInstance(getActivity()).getStaticDataHandler().getAvailableLanguages(new ServiceResponseListener<List<String>>() {
            @Override
            public void onResponse(final List<String> response) {

                Observable.create(new Observable.OnSubscribe<Locale>() {
                    @Override
                    public void call(Subscriber<? super Locale> subscriber) {
                        for (Locale locale : Arrays.asList(Locale.getAvailableLocales())) {
                            subscriber.onNext(locale);
                        }

                        subscriber.onCompleted();
                    }
                }).filter(new Func1<Locale, Boolean>() {
                    @Override
                    public Boolean call(Locale locale) {
                        return response.contains(locale.toString());
                    }
                }).subscribe(new Subscriber<Locale>() {
                    @Override
                    public void onCompleted() {
                        mLanguageAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Locale locale) {
                        mLanguages.add(locale);

                    }
                });
            }

            @Override
            public void onError(TeemoException e) {

            }
        });
    }

    public void revealChooseRegionFab() {

        if (mChooseLanguageFab.getVisibility() != View.VISIBLE) {
            Animationutils.revealView(mChooseLanguageFab);
        }
    }

    @OnClick(R.id.fab_choose_language)
    public void setRegionAndNavigate() {
        SettingsHandler.setLanguage(getActivity(), selectedLocale.toString());
        getActivity().getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.content, PickSummonerFragment.newInstance())
                .commit();
    }

    class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.ViewHolder> {

        private List<Locale> locales;

        public LanguageAdapter(List<Locale> locales) {
            this.locales = locales;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_list_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.tv.setAllCaps(false);
            holder.tv.setText(locales.get(position).getDisplayName(Locale.getDefault()));
            Utils.updateLanguage(getActivity(), SettingsHandler.getLanguage(getActivity()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedLocale = locales.get(holder.getAdapterPosition());
                    revealChooseRegionFab();
                    tvSelectedLanguage.setText(selectedLocale.getDisplayName(Locale.getDefault()));
                }
            });
        }


        @Override
        public int getItemCount() {
            return locales.size();
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
