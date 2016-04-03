package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.ablanco.teemo.Teemo;
import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.constants.StaticAPIQueryParams;
import com.ablanco.teemo.model.champions.Champion;
import com.ablanco.teemo.model.champions.ChampionList;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.model.staticdata.ChampionListDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ChampionListAdapter;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.adapter.ListPopUpWindowAdapter;
import com.ablanco.tonsofdamage.ui.views.DividerItemDecoration;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Álvaro Blanco on 03/04/2016.
 * TonsOfDamage
 */
public class ChampionsFragment extends BaseHomeFragment implements SearchView.OnQueryTextListener {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.loading)
    ProgressBar loading;

    private List<ChampionDto> mChampions = new ArrayList<>();
    private List<ChampionDto> mFilteredChampions = new ArrayList<>();
    private ListPopupWindow listPopupWindow;

    private ListAdapter filterAdapter;
    private ListAdapter orderAdapter;
    private Map<Integer, Boolean> mFreeToPlayChampions = new HashMap<>();
    private ChampionListAdapter adapter;

    public static Fragment newInstance(){
        return new ChampionsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.END);
        listPopupWindow.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_bg));
        listPopupWindow.setWidth(SizeUtils.convertDpToPixel(230));
        listPopupWindow.setAnchorView(getActivity().findViewById(R.id.toolbar));

        filterAdapter = new ListPopUpWindowAdapter(getActivity(), Arrays.asList(R.string.all, R.string.free, R.string.tank, R.string.fighter, R.string.mage, R.string.assassin, R.string.marksman));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_champions, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        adapter = new ChampionListAdapter(getActivity());
        adapter.setOnItemClickListener(new ItemClickAdapter.OnItemClickListener() {
            @Override
            public void onItemClicked(int position) {
                Log.d("ChampionsFragment", "clieck" + position);
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SizeUtils.convertDpToPixel(4)));


        Teemo.getInstance(getActivity()).getChampionsHandler().getChampions(true, new ServiceResponseListener<ChampionList>() {
            @Override
            public void onResponse(ChampionList response) {
                mFreeToPlayChampions = new HashMap<>();
                for (Champion champion : response.getChampions()) {
                    mFreeToPlayChampions.put(champion.getId(), champion.getFreeToPlay());
                }
                adapter.setFreeToPlayChampions(mFreeToPlayChampions);
            }

            @Override
            public void onError(TeemoException e) {

            }
        });

        Teemo.getInstance(getActivity()).getStaticDataHandler().getChampions(Locale.getDefault().toString(), null, null, StaticAPIQueryParams.Champions.IMAGE.concat(",").concat(StaticAPIQueryParams.Champions.TAGS), new ServiceResponseListener<ChampionListDto>() {
            @Override
            public void onResponse(ChampionListDto response) {

                mChampions.addAll(response.getData().values());

                sortByName(mChampions);
                adapter.setChampions(mChampions);


                loading.setVisibility(View.GONE);
            }

            @Override
            public void onError(TeemoException e) {
                loading.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.champions, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            mFilteredChampions.clear();
            listPopupWindow.setAdapter(filterAdapter);
            listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch ((int) id) {
                        case R.string.free:
                            filter(freeToPlayFilter());
                            break;
                        case R.string.tank:
                            filter(typeFilter(getString(R.string.tank)));
                            break;
                        case R.string.fighter:
                            filter(typeFilter(getString(R.string.fighter)));
                            break;
                        case R.string.support:
                            filter(typeFilter(getString(R.string.support)));
                            break;
                        case R.string.mage:
                            filter(typeFilter(getString(R.string.mage)));
                            break;
                        case R.string.assassin:
                            filter(typeFilter(getString(R.string.assassin)));
                            break;
                        case R.string.marksman:
                            filter(typeFilter(getString(R.string.marksman)));
                            break;
                        case R.string.all:
                            filterAll();
                            break;
                    }
                    listPopupWindow.dismiss();
                }
            });
            listPopupWindow.show();
        }

        return true;
    }

    private void sortByName(List<ChampionDto> championDtos){
        Collections.sort(championDtos, new Comparator<ChampionDto>() {
            @Override
            public int compare(ChampionDto lhs, ChampionDto rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    private void filterAll(){
        adapter.setChampions(mChampions);
    }


    public Func1<ChampionDto, Boolean> typeFilter(final String type){
        return new Func1<ChampionDto, Boolean>() {
            @Override
            public Boolean call(ChampionDto championDto) {
                boolean shouldReturn = false;
                if(championDto.getTags() != null){
                    for (String tag : championDto.getTags()){
                        if(tag.equalsIgnoreCase(type)){
                            shouldReturn = true;
                        }
                    }
                }

                return shouldReturn;
            }
        };
    }


    public Func1<ChampionDto, Boolean> freeToPlayFilter(){
        return new Func1<ChampionDto, Boolean>() {
            @Override
            public Boolean call(ChampionDto championDto) {
                return mFreeToPlayChampions.get(championDto.getId()) != null && mFreeToPlayChampions.get(championDto.getId());
            }
        };
    }


    private void filter(Func1<ChampionDto, Boolean> filter){
        Observable.from(mChampions).filter(filter).subscribe(new Action1<ChampionDto>() {
            @Override
            public void call(ChampionDto championDto) {
                mFilteredChampions.add(championDto);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //nothing
            }
        }, new Action0() {
            @Override
            public void call() {
                adapter.setChampions(mFilteredChampions);
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        search(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        search(newText);
        return true;
    }

    private void search(final String queryText){
        mFilteredChampions.clear();
        Observable.from(mChampions).filter(new Func1<ChampionDto, Boolean>() {
            @Override
            public Boolean call(ChampionDto championDto) {
                return championDto.getName().toLowerCase().contains(queryText.toLowerCase());
            }
        }).subscribe(new Action1<ChampionDto>() {
            @Override
            public void call(ChampionDto championDto) {
                mFilteredChampions.add(championDto);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //nothing
            }
        }, new Action0() {
            @Override
            public void call() {
                adapter.setChampions(mFilteredChampions);
            }
        });
    }
}
