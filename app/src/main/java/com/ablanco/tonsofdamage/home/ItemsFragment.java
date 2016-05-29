package com.ablanco.tonsofdamage.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.ablanco.teemo.TeemoException;
import com.ablanco.teemo.model.staticdata.ItemDto;
import com.ablanco.tonsofdamage.R;
import com.ablanco.tonsofdamage.adapter.ItemsGridAdapter;
import com.ablanco.tonsofdamage.handler.StaticDataHandler;
import com.ablanco.tonsofdamage.items.ItemsFilterDialog;
import com.ablanco.tonsofdamage.utils.HomeErrorUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by Álvaro Blanco on 10/04/2016.
 * TonsOfDamage
 */
public class ItemsFragment extends BaseHomeFragment implements SearchView.OnQueryTextListener, ItemsFilterDialog.ItemsFilterDialogListener {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.loading)
    ProgressBar loading;

    private List<ItemDto> mItems = new ArrayList<>();
    private ItemsGridAdapter adapter;
    private ArrayList<String> mTags = new ArrayList<>();
    private List<String> mSelectedTags = new ArrayList<>();
    private List<ItemDto> mSearchedItems = new ArrayList<>();
    private List<ItemDto> mFilteredItems = new ArrayList<>();
    private ItemsFilterDialog dialog;

    public static Fragment newInstance() {
        return new ItemsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_items, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        adapter = new ItemsGridAdapter(getActivity());
        mRecyclerView.setAdapter(adapter);

        loadItemData();
    }

    private void loadItemData() {
        StaticDataHandler.getInstance().getItems(getActivity(), new StaticDataHandler.ResponseListener<List<ItemDto>>() {
            @Override
            public void onResponse(List<ItemDto> response) {
                if(loading != null){
                    loading.setVisibility(View.GONE);
                }

                Observable.from(response).filter(new Func1<ItemDto, Boolean>() {
                    @Override
                    public Boolean call(ItemDto itemDto) {
                        return itemDto.getName() != null && itemDto.getDescription() != null;
                    }
                }).subscribe(new Subscriber<ItemDto>() {
                    @Override
                    public void onCompleted() {
                        sortByName(mItems);
                        sortByName(mFilteredItems);
                        adapter.setItems(mItems);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ItemDto itemDto) {
                        mItems.add(itemDto);
                        mFilteredItems.add(itemDto);
                    }
                });

                for (ItemDto item : response){
                    if(item.getTags() != null){
                        for (String tag : item.getTags()){
                            if(!mTags.contains(tag)){
                                mTags.add(tag);
                            }
                        }
                    }

                }

                if (dialog != null && dialog.isVisible()){
                    dialog.setTags(mTags);
                }



            }

            @Override
            public void onError(TeemoException e) {
                if(loading != null){
                    loading.setVisibility(View.GONE);
                }

                HomeErrorUtils.getInstance().showPersistentError(HomeErrorUtils.ITEMS, getView(), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loading.setVisibility(View.VISIBLE);
                        loadItemData();
                    }
                });
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.items, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_filter) {
            dialog = ItemsFilterDialog.newInstance(mTags, (ArrayList<String>) mSelectedTags);
            dialog.setListener(ItemsFragment.this);
            dialog.show(getActivity().getSupportFragmentManager(), "filter_items");
        }

        return true;
    }

    private void sortByName(List<ItemDto> itemDtos){
        Collections.sort(itemDtos, new Comparator<ItemDto>() {
            @Override
            public int compare(ItemDto lhs, ItemDto rhs) {
                return lhs.getName().compareTo(rhs.getName());
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
        mSearchedItems.clear();
        Observable.from(mFilteredItems).filter(new Func1<ItemDto, Boolean>() {
            @Override
            public Boolean call(ItemDto itemDto) {
                return itemDto.getName().toLowerCase().contains(queryText.toLowerCase());
            }
        }).subscribe(new Action1<ItemDto>() {
            @Override
            public void call(ItemDto itemDto) {
                mSearchedItems.add(itemDto);
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                //nothing
            }
        }, new Action0() {
            @Override
            public void call() {
                adapter.setItems(mSearchedItems);
            }
        });
    }

    @Override
    public void onTagsSelected(List<String> tags) {
        mFilteredItems.clear();
        mSelectedTags = tags;
        if(tags.isEmpty()){
            mFilteredItems.addAll(mItems);
            adapter.setItems(mItems);
        }else {
            Observable.from(mItems).filter(new Func1<ItemDto, Boolean>() {
                @Override
                public Boolean call(ItemDto itemDto) {
                    boolean shouldReturn = false;
                    if(itemDto.getTags() != null){
                        for (String s : mSelectedTags){
                            if(contains(s, itemDto.getTags())){
                                shouldReturn = true;
                            }else {
                                shouldReturn = false;
                                break;
                            }
                        }
                    }

                    return shouldReturn;
                }
            }).subscribe(new Subscriber<ItemDto>() {
                @Override
                public void onCompleted() {
                    adapter.setItems(mFilteredItems);
                }

                @Override
                public void onError(Throwable e) {
                    //nthing
                }

                @Override
                public void onNext(ItemDto itemDto) {
                    mFilteredItems.add(itemDto);
                }
            });
        }

    }

    private boolean contains(String tag, List<String> tags){
        for (String s : tags){
            if(s.equalsIgnoreCase(tag)){
                return true;
            }
        }
        return false;
    }

}
