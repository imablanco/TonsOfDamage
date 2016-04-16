package com.ablanco.tonsofdamage.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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
import com.ablanco.teemo.model.champions.Champion;
import com.ablanco.teemo.model.champions.ChampionList;
import com.ablanco.teemo.model.staticdata.ChampionDto;
import com.ablanco.teemo.service.base.ServiceResponseListener;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ChampionListGridAdapter;
import com.ablanco.tonsofdamage.adapter.ChampionListListAdapter;
import com.ablanco.tonsofdamage.adapter.ChampionsBaseAdapter;
import com.ablanco.tonsofdamage.adapter.ItemClickAdapter;
import com.ablanco.tonsofdamage.adapter.ListPopUpWindowAdapter;
import com.ablanco.tonsofdamage.handler.NavigationHandler;
import com.ablanco.tonsofdamage.handler.StaticDataHandler;
import com.ablanco.tonsofdamage.ui.activities.ChampionDetailActivity;
import com.ablanco.tonsofdamage.ui.views.DividerItemDecoration;
import com.ablanco.tonsofdamage.utils.SizeUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

    private final static int MODE_LIST = 0;
    private final static int MODE_GRID = 1;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.loading)
    ProgressBar loading;

    private List<ChampionDto> mChampions = new ArrayList<>();
    private List<ChampionDto> mFilteredChampions = new ArrayList<>();
    private List<ChampionDto> mSearchedChampions = new ArrayList<>();
    private ListPopupWindow listPopupWindow;

    private ListAdapter filterAdapter;
    private Map<Integer, Boolean> mFreeToPlayChampions = new HashMap<>();
    private ChampionsBaseAdapter adapter;

    private int mMode = MODE_GRID;

    private ItemClickAdapter.OnItemClickListener itemClickListener = new ItemClickAdapter.OnItemClickListener() {
        @Override
        public void onItemClicked(int position) {
            Bundle bundle = new Bundle();
            bundle.putInt(ChampionDetailActivity.EXTRA_CHAMPION_ID, adapter.getItemAtPosition(position).getId());
            NavigationHandler.navigateTo(getActivity(), NavigationHandler.CHAMPION_DETAIL, bundle);
        }
    };

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

        adapter = new ChampionListGridAdapter(getActivity());
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(SizeUtils.convertDpToPixel(1)));


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

        StaticDataHandler.getInstance().getChampions(getActivity(), new StaticDataHandler.ResponseListener<List<ChampionDto>>() {
            @Override
            public void onResponse(List<ChampionDto> response) {
                mChampions.addAll(response);
                mFilteredChampions.addAll(response);

                sortByName(mChampions);
                sortByName(mFilteredChampions);
                adapter.setChampions(mChampions);

                if(loading != null){
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onError(TeemoException e) {
                if(loading != null){
                    loading.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.champions, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
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
        } else if(id == R.id.action_view_mode){
            item.setIcon(mMode == MODE_GRID ? R.drawable.ic_grid : R.drawable.ic_list);
            mMode = mMode == MODE_GRID ? MODE_LIST : MODE_GRID;
            toggleMode();
        }

        return true;
    }

    private void toggleMode(){
        if(mMode == MODE_GRID){
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
            adapter = new ChampionListGridAdapter(getActivity());

        }else{
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            adapter = new ChampionListListAdapter(getActivity());
        }

        adapter.setChampions(mFilteredChampions);
        adapter.setFreeToPlayChampions(mFreeToPlayChampions);
        adapter.setOnItemClickListener(itemClickListener);
        mRecyclerView.setAdapter(adapter);
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
        mFilteredChampions.clear();
        mFilteredChampions.addAll(mChampions);
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
                            break;
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
        mFilteredChampions.clear();
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
        mSearchedChampions.clear();
        Observable.from(mFilteredChampions).filter(new Func1<ChampionDto, Boolean>() {
            @Override
            public Boolean call(ChampionDto championDto) {
                return championDto.getName().toLowerCase().contains(queryText.toLowerCase());
            }
        }).subscribe(new Action1<ChampionDto>() {
            @Override
            public void call(ChampionDto championDto) {
                mSearchedChampions.add(championDto);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //nothing
            }
        }, new Action0() {
            @Override
            public void call() {
                adapter.setChampions(mSearchedChampions);
            }
        });
    }

}
